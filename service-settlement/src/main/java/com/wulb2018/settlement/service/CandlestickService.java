package com.wulb2018.settlement.service;


import cn.hutool.core.bean.BeanUtil;
import com.wulb2018.biz.enums.CandlestickType;
import com.wulb2018.biz.model.vo.CandlestickVO;
import com.wulb2018.biz.process.candlestick.ICandlestickTypeProcess;
import com.wulb2018.common.service.BaseService;
import com.wulb2018.settlement.mapper.CandlestickMapper;
import com.wulb2018.settlement.model.dto.CandlestickAddDTO;
import com.wulb2018.settlement.model.dto.CandlestickUpdateDTO;
import com.wulb2018.settlement.model.entity.Candlestick;
import com.wulb2018.settlement.model.entity.Trade;
import com.wulb2018.settlement.service.convert.CandlestickConvert;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * (t_candlestick)-业务处理类
 *
 * @author makejava
 * @since 2026-01-26 20:41:12
 */
@Service
@RequiredArgsConstructor
public class CandlestickService extends BaseService<CandlestickMapper, Candlestick> {

    private final CandlestickConvert candlestickConvert;
    @Resource
    private TradeService tradeService;

    public Candlestick getLastOne(CandlestickType candlestickType) {
        return lambdaQuery().eq(Candlestick::getCandlestickType, candlestickType).orderByDesc(Candlestick::getId).last("LIMIT 1").one();
    }

    public List<CandlestickVO> getLastSomeCandlestickList(CandlestickType candlestickType) {
        List<Candlestick> candlesticks = lambdaQuery().eq(Candlestick::getCandlestickType, candlestickType).orderByDesc(Candlestick::getId).last("LIMIT 3").list();
        if (candlesticks.isEmpty()) {
            return new ArrayList<>();
        }
        String currentDatetimeCategory = tradeService.formatTradeCreateTime(candlestickType, LocalDateTime.now());
        if (!currentDatetimeCategory.equals(candlesticks.get(0).getDatetimeCategory())) {
            Trade lastTrade = tradeService.getLastOne();
            generateAndSaveNewCandlestick(candlesticks.get(0), currentDatetimeCategory, candlestickType, lastTrade, LocalDateTime.now());
            candlesticks = lambdaQuery().eq(Candlestick::getCandlestickType, candlestickType).orderByDesc(Candlestick::getId).last("LIMIT 3").list();
        }
        return BeanUtil.copyToList(candlesticks, CandlestickVO.class);
    }

    /**
     * 生成新的蜡烛并保存
     * @param lastOneCandlestick
     * @param lastDatetimeCategory
     * @param candlestickType
     * @param lastTrade
     * @param lastDatetime
     */
    public synchronized void generateAndSaveNewCandlestick(Candlestick lastOneCandlestick, String lastDatetimeCategory, CandlestickType candlestickType, Trade lastTrade, LocalDateTime lastDatetime) {
        Candlestick reQueryLastOneCandlestick = getLastOne(candlestickType);
        if (lastOneCandlestick != null) {
            //双重检测
            if (!lastOneCandlestick.getId().equals(reQueryLastOneCandlestick.getId())) {
                return;
            }
            if (lastDatetimeCategory.equals(lastOneCandlestick.getDatetimeCategory())) {
                lastOneCandlestick.setClosePrice(lastTrade.getPrice());
                if (lastOneCandlestick.getLowestPrice() > lastTrade.getPrice()) {
                    lastOneCandlestick.setLowestPrice(lastTrade.getPrice());
                }
                if (lastOneCandlestick.getHighestPrice() < lastTrade.getPrice()) {
                    lastOneCandlestick.setHighestPrice(lastTrade.getPrice());
                }
                updateById(lastOneCandlestick);
            } else {
                LocalDateTime startTime = LocalDateTime.parse(lastOneCandlestick.getDatetimeCategory(), candlestickType.getDateTimeFormatter());
                Map<String, CandlestickVO> cateAndCandlestickVOmap = buildDateAndCandlestickVOMap(startTime, lastDatetime, candlestickType);
                for (Map.Entry<String, CandlestickVO> candlestickVOEntry: cateAndCandlestickVOmap.entrySet()) {
                    if (candlestickVOEntry.getKey().equals(lastOneCandlestick.getDatetimeCategory())) {
                        continue;
                    }
                    Candlestick newLastOneCandlestick = buildLineCandlestick(candlestickType, candlestickVOEntry.getKey(), lastOneCandlestick.getClosePrice());
                    save(newLastOneCandlestick);
                }
            }
        } else {
            //双重检测
            if (reQueryLastOneCandlestick != null) {
                return;
            }
            lastOneCandlestick = buildLineCandlestick(candlestickType, lastDatetimeCategory, lastTrade.getPrice());
            save(lastOneCandlestick);
        }
    }

    /**
     * 构建线型蜡烛
     * @param candlestickType
     * @param datetimeCategory
     * @param price
     * @return
     */
    private Candlestick buildLineCandlestick(CandlestickType candlestickType, String datetimeCategory, Double price) {
        Candlestick lineCandlestick = new Candlestick();
        lineCandlestick.setCandlestickType(candlestickType);
        lineCandlestick.setDatetimeCategory(datetimeCategory);
        lineCandlestick.setOpenPrice(price);
        lineCandlestick.setClosePrice(price);
        lineCandlestick.setLowestPrice(price);
        lineCandlestick.setHighestPrice(price);
        return lineCandlestick;
    }

    public Map<String, CandlestickVO> buildDateAndCandlestickVOMap(LocalDateTime startDateTime, LocalDateTime endDateTime, CandlestickType candlestickType) {
        ICandlestickTypeProcess process = candlestickType.getCandlestickTypeProcess();
        return process.buildZeroVolumeCandlestickVOMap(startDateTime, endDateTime);
    }

    public void safeguardCandlestickCompleteness(Trade trade) {
        CandlestickType[] candlestickTypes = CandlestickType.values();
        //缓存维护 最后几个蜡烛图
        for (CandlestickType candlestickType: candlestickTypes) {
            String tradeDatetimeCategory = buildNextDatetimeCategory(trade.getCreateDate(), candlestickType);
            Candlestick lastOneCandlestick = getLastOne(candlestickType);
            generateAndSaveNewCandlestick(lastOneCandlestick, tradeDatetimeCategory, candlestickType, trade, trade.getCreateDate());
        }
    }

    private String buildNextDatetimeCategory(LocalDateTime tradeCreateDate, CandlestickType candlestickType) {
        ICandlestickTypeProcess candlestickTypeProcess = candlestickType.getCandlestickTypeProcess();
        LocalDateTime roundCreateDate = candlestickTypeProcess.setRoundDateTime(tradeCreateDate);
        return candlestickTypeProcess.getFormattedDatetime(roundCreateDate);
    }

    public CandlestickVO getOne(Serializable id) {
        return candlestickConvert.toVo(super.getById(id));
    }

    public Boolean save(CandlestickAddDTO candlestickAddDTO) {
        return this.save(candlestickConvert.toEntity(candlestickAddDTO));
    }

    public Boolean updateById(CandlestickUpdateDTO candlestickUpdateDTO) {
        return this.updateById(candlestickConvert.toEntity(candlestickUpdateDTO));
    }

}

