package com.wulb2018.settlement.service;


import cn.hutool.core.bean.BeanUtil;
import com.wulb2018.biz.enums.CandlestickType;
import com.wulb2018.biz.model.vo.CandlestickVO;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wulb2018.settlement.constant.DateTimeFormatterConst.dayFormatter;
import static com.wulb2018.settlement.constant.DateTimeFormatterConst.minuteFormatter;

/**
 * (t_candlestick)-业务处理类
 *
 * @author makejava
 * @since 2026-01-26 20:41:12
 */
@Service
@RequiredArgsConstructor
public class CandlestickService extends BaseService<CandlestickMapper, Candlestick> {

    private final DateTimeFormatter secondFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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
        if (lastOneCandlestick != null) {
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
                LocalDateTime startTime = LocalDateTime.parse(lastOneCandlestick.getDatetimeCategory(), secondFormatter);
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
        Map<String, CandlestickVO> dateAndCandlestickVOMap = new HashMap<>();
        if (CandlestickType.MINUTE1.equals(candlestickType)) {
            startDateTime = setDateTimeZeroSecond(startDateTime);
            endDateTime =setDateTimeZeroSecond(endDateTime);
            buildZeroVolumeCandlestickVOMap(dateAndCandlestickVOMap, startDateTime, endDateTime, minuteFormatter);
        } else if (CandlestickType.DAY1.equals(candlestickType)){
            startDateTime = setDateTimeZeroHour(startDateTime);
            endDateTime = setDateTimeZeroHour(endDateTime);
            buildZeroVolumeCandlestickVOMap(dateAndCandlestickVOMap, startDateTime, endDateTime, dayFormatter);
        } else if (CandlestickType.SECOND5.equals(candlestickType)) {
            startDateTime = setRound5Second(startDateTime);
            endDateTime = setRound5Second(endDateTime);
            buildZeroVolumeCandlestickVOMap(dateAndCandlestickVOMap, startDateTime, endDateTime, secondFormatter);
        }
        //todo 其他的以后再写
        return dateAndCandlestickVOMap;
    }

    private LocalDateTime setDateTimeZeroSecond(LocalDateTime localDateTime) {
        return localDateTime.withSecond(0).withNano(0);
    }

    private LocalDateTime setDateTimeZeroHour(LocalDateTime localDateTime) {
        return localDateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    private LocalDateTime setRound5Second(LocalDateTime localDateTime) {
        int endDateTimeSecond = localDateTime.getSecond()/5 * 5 + 5;
        if (endDateTimeSecond < 60) {
            localDateTime = localDateTime.withSecond(endDateTimeSecond).withNano(0);
        } else {
            localDateTime = localDateTime.plusSeconds(1).withSecond(0).withNano(0);
        }
        return localDateTime;
    }

    private void buildZeroVolumeCandlestickVOMap(Map<String, CandlestickVO> dateAndCandlestickVOMap, LocalDateTime startDateTime, LocalDateTime endDateTime, DateTimeFormatter formatter) {
        while (startDateTime.isBefore(endDateTime) || startDateTime.isEqual(endDateTime)) {
            String formattedDatetime = startDateTime.format(formatter);
            CandlestickVO candlestickVO = buildZeroVolumeCandlestickVO(formattedDatetime);
            dateAndCandlestickVOMap.put(formattedDatetime, candlestickVO);
            startDateTime = startDateTime.plusMinutes(1L);
        }
    }
    /**
     * 构建0交易量蜡烛VO
     * @param formattedDatetime
     * @return
     */
    private CandlestickVO buildZeroVolumeCandlestickVO(String formattedDatetime) {
        CandlestickVO candlestickVO = new CandlestickVO();
        candlestickVO.setDatetimeCategory(formattedDatetime);
        candlestickVO.setClosePrice(0.);
        candlestickVO.setHighestPrice(0.);
        candlestickVO.setVolume(0.);
        return candlestickVO;
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

