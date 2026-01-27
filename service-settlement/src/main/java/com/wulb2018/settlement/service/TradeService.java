package com.wulb2018.settlement.service;


import com.wulb2018.biz.enums.CandlestickType;
import com.wulb2018.biz.model.dto.OrderUpdateDTO;
import com.wulb2018.biz.model.vo.CandlestickVO;
import com.wulb2018.client.order.OrderFeignClient;
import com.wulb2018.common.service.BaseService;
import com.wulb2018.settlement.mapper.TradeMapper;
import com.wulb2018.settlement.model.dto.TradeAddDTO;
import com.wulb2018.settlement.model.dto.TradeDTO;
import com.wulb2018.settlement.model.dto.TradeUpdateDTO;
import com.wulb2018.settlement.model.entity.Candlestick;
import com.wulb2018.settlement.model.entity.Trade;
import com.wulb2018.settlement.model.vo.TradeVO;
import com.wulb2018.settlement.service.convert.TradeConvert;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import static com.wulb2018.settlement.constant.DateTimeFormatterConst.*;

/**
 * 成交记录(t_trade)-业务处理类
 *
 * @author makejava
 * @since 2026-01-18 14:49:00
 */
@Service
@RequiredArgsConstructor
public class TradeService extends BaseService<TradeMapper, Trade> {

    private final TradeConvert tradeConvert;
    private final OrderFeignClient orderFeignClient;
    @Resource
    private CandlestickService candlestickService;
    private final static List<CandlestickVO> LAST_SOME_CANDLESTICK_LIST = new ArrayList<>();
    private final static int LAST_SOME_CANDLESTICK_LIST_SIZE = 3;

    //暂时这么做
    private volatile CandlestickType currentCandlestickType = CandlestickType.SECOND5;

    public void settlementTrade(TradeDTO tradeDTO){
        Trade trade = tradeConvert.toEntity(tradeDTO);
        save(trade);
        //缓存维护 最后几个蜡烛图
        String tradeDatetimeCategory = buildNextDatetimeCategory(trade.getCreateDate());
        Candlestick lastOneCandlestick = candlestickService.getLastOne(currentCandlestickType);

        candlestickService.generateAndSaveNewCandlestick(lastOneCandlestick, tradeDatetimeCategory, currentCandlestickType, trade, trade.getCreateDate());

        //todo 这里以后做最终一致性
        List<OrderUpdateDTO> orderUpdateDTOList = new ArrayList<>();
        //买方
        OrderUpdateDTO buyOrderUpdateDTO = new OrderUpdateDTO();
        buyOrderUpdateDTO.setId(trade.getBuyOrderId());
        buyOrderUpdateDTO.setFilledQuantity(trade.getQuantity());
        orderUpdateDTOList.add(buyOrderUpdateDTO);
        //卖方
        OrderUpdateDTO sellOrderUpdateDTO = new OrderUpdateDTO();
        sellOrderUpdateDTO.setId(trade.getSellOrderId());
        sellOrderUpdateDTO.setFilledQuantity(trade.getQuantity());
        orderUpdateDTOList.add(sellOrderUpdateDTO);
        orderFeignClient.updateOrderList(orderUpdateDTOList);
    }

    public Trade getLastOne() {
        return lambdaQuery().orderByDesc(Trade::getId).last("LIMIT 1").one();
    }

    //todo 这个考虑删除
    public void rebuildLastSomeCandlestickList(Trade trade) {
        String formattedTradeCreateTime = formatTradeCreateTime(currentCandlestickType, trade.getCreateDate());
        boolean isFindDatetimeCategory = false;
        for (CandlestickVO candlestickVO: LAST_SOME_CANDLESTICK_LIST) {
            if (formattedTradeCreateTime.equals(candlestickVO.getDatetimeCategory())) {
                isFindDatetimeCategory = true;
                candlestickVO.setVolume(candlestickVO.getVolume() + trade.getQuantity());
                candlestickVO.setClosePrice(trade.getPrice());
                if (candlestickVO.getLowestPrice() > trade.getPrice()) {
                    candlestickVO.setLowestPrice(trade.getPrice());
                }
                if (candlestickVO.getHighestPrice() < trade.getPrice()) {
                    candlestickVO.setHighestPrice(trade.getPrice());
                }
            }
        }
        if (!isFindDatetimeCategory) {
            int size = LAST_SOME_CANDLESTICK_LIST.size();
            CandlestickVO candlestickVO = LAST_SOME_CANDLESTICK_LIST.get(size - 1);
            if (candlestickVO == null) {
                return;
            }
            String currentDatetimeCategory = candlestickVO.getDatetimeCategory();
            //先预判下一个日期类目
            String preNextDatetimeCategory = buildNextDatetimeCategory(currentDatetimeCategory);
            String tradeDatetimeCategory = buildNextDatetimeCategory(trade.getCreateDate());
            CandlestickVO newCandlestickVO = new CandlestickVO();
            while (true) {
                if (preNextDatetimeCategory.equals(tradeDatetimeCategory)) {
                    newCandlestickVO.setDatetimeCategory(preNextDatetimeCategory);
                    newCandlestickVO.setOpenPrice(trade.getPrice());
                    newCandlestickVO.setClosePrice(trade.getPrice());
                    newCandlestickVO.setLowestPrice(trade.getPrice());
                    newCandlestickVO.setHighestPrice(trade.getPrice());
                    newCandlestickVO.setVolume(trade.getQuantity());
                    buildLastSomeCandlestickList(List.of(newCandlestickVO));
                    break;
                } else {
                    newCandlestickVO.setDatetimeCategory(tradeDatetimeCategory);
                    newCandlestickVO.setOpenPrice(candlestickVO.getClosePrice());
                    newCandlestickVO.setClosePrice(candlestickVO.getClosePrice());
                    newCandlestickVO.setLowestPrice(candlestickVO.getClosePrice());
                    newCandlestickVO.setHighestPrice(candlestickVO.getClosePrice());
                    newCandlestickVO.setVolume(0.);
                    buildLastSomeCandlestickList(List.of(newCandlestickVO));
                }
            }
        }
    }

    private String buildNextDatetimeCategory(String datetimeCategory) {
        if (currentCandlestickType.equals(CandlestickType.SECOND5)) {
            LocalDateTime cateDataTime = LocalDateTime.parse(datetimeCategory, secondFormatter);
            return cateDataTime.plusSeconds(5).format(secondFormatter);
        }
        return datetimeCategory;
    }

    private String buildNextDatetimeCategory(LocalDateTime tradeCreateDate) {
        if (currentCandlestickType.equals(CandlestickType.SECOND5)) {
            return tradeCreateDate.withSecond(tradeCreateDate.getSecond()/5 * 5 + 5).withNano(0).format(secondFormatter);
        }
        return "";
    }

    public List<CandlestickVO> getCandlestickInitData(CandlestickType candlestickType) {
        currentCandlestickType = candlestickType;
        List<Trade> list = lambdaQuery().orderByAsc(Trade::getId).list();
        Trade firstTrade = list.get(0);
        Trade lastTrade = list.get(list.size() - 1);
        Map<String,CandlestickVO> dateAndCandlestickVOMap = candlestickService.buildDateAndCandlestickVOMap(firstTrade.getCreateDate(), lastTrade.getCreateDate(), candlestickType);
        for (Trade trade : list) {
            String formattedTradeCreateTime = formatTradeCreateTime(candlestickType, trade.getCreateDate());
            CandlestickVO candlestickVO = dateAndCandlestickVOMap.get(formattedTradeCreateTime);
            if (candlestickVO == null) {
                System.out.println(formattedTradeCreateTime);
            }
            if (candlestickVO.getOpenPrice() == null) {
                candlestickVO.setOpenPrice(trade.getPrice());
            }
            //最低价
            if (candlestickVO.getLowestPrice() == null || candlestickVO.getLowestPrice() > trade.getPrice()) {
                candlestickVO.setLowestPrice(trade.getPrice());
            }
            //最高价
            if (candlestickVO.getHighestPrice() == null || candlestickVO.getHighestPrice() < trade.getPrice()) {
                candlestickVO.setHighestPrice(trade.getPrice());
            }
            //处理收盘价格，可以把最新的价格设置一直为收盘价格
            candlestickVO.setClosePrice(trade.getPrice());
            //交易量
            candlestickVO.setVolume(candlestickVO.getVolume() + trade.getQuantity());
        }

        rebuildDateAndCandlestickVOMap(dateAndCandlestickVOMap);

        List<CandlestickVO> candlestickVOList = dateAndCandlestickVOMap.values().stream().sorted(Comparator.comparing(CandlestickVO::getDatetimeCategory)).toList();


        buildLastSomeCandlestickList(candlestickVOList);

        return candlestickVOList;
    }

    private synchronized void  buildLastSomeCandlestickList(List<CandlestickVO> candlestickVOList) {
        int size = candlestickVOList.size();
        for (int i = 1; i <= 3; i++) {
            CandlestickVO candlestickVO = candlestickVOList.get(size - i);
            if (candlestickVO != null) {
                LAST_SOME_CANDLESTICK_LIST.add(candlestickVO);
                if (LAST_SOME_CANDLESTICK_LIST.size() > LAST_SOME_CANDLESTICK_LIST_SIZE) {
                    LAST_SOME_CANDLESTICK_LIST.remove(LAST_SOME_CANDLESTICK_LIST.size() - 1);
                }
            }
        }
    }

    private void rebuildDateAndCandlestickVOMap(Map<String,CandlestickVO> dateAndCandlestickVOMap) {
        CandlestickVO lastCandlestickVO = null;
        List<String> needDeleteDateTimeKeyList = new ArrayList<>();
        for (Map.Entry<String,CandlestickVO> candlestickVOEntry: dateAndCandlestickVOMap.entrySet()) {
            CandlestickVO candlestickVO = candlestickVOEntry.getValue();
            if (lastCandlestickVO == null) {
                if (candlestickVO.getOpenPrice() == null) {
                    //如果lastCandlestickVO是null说明是最开始，如果最开始的几个蜡烛的开盘价都是null说明要把前几个给删除了
                    needDeleteDateTimeKeyList.add(candlestickVOEntry.getKey());
                } else {
                    lastCandlestickVO = candlestickVO;
                }
            } else {
                if (candlestickVO.getOpenPrice() == null) {
                    candlestickVO.setOpenPrice(lastCandlestickVO.getOpenPrice());
                    candlestickVO.setClosePrice(lastCandlestickVO.getClosePrice());
                    candlestickVO.setLowestPrice(lastCandlestickVO.getLowestPrice());
                    candlestickVO.setHighestPrice(lastCandlestickVO.getHighestPrice());
                }
            }
        }

        if (!needDeleteDateTimeKeyList.isEmpty()) {
            for (String needDeleteDateTimeKey: needDeleteDateTimeKeyList) {
                dateAndCandlestickVOMap.remove(needDeleteDateTimeKey);
            }
        }
    }

    public String formatTradeCreateTime(CandlestickType candlestickType, LocalDateTime tradeCreateDate) {
        return switch (candlestickType) {
            case SECOND5 -> tradeCreateDate.withSecond((tradeCreateDate.getSecond() / 5) * 5).format(secondFormatter);
            case MINUTE15 ->
                    tradeCreateDate.withMinute((tradeCreateDate.getMinute() / 15) * 15).format(minuteFormatter);
            case HOUR1 -> tradeCreateDate.format(hourFormatter);
            case HOUR4 -> tradeCreateDate.withHour((tradeCreateDate.getHour() / 4) * 4).format(hourFormatter);
            case DAY1 -> tradeCreateDate.format(dayFormatter);
            default -> tradeCreateDate.format(minuteFormatter);
        };
    }

    public TradeVO getOne(Serializable id) {
        return tradeConvert.toVo(super.getById(id));
    }

    public Boolean save(TradeAddDTO tradeAddDTO) {
        return this.save(tradeConvert.toEntity(tradeAddDTO));
    }

    public Boolean updateById(TradeUpdateDTO tradeUpdateDTO) {
        return this.updateById(tradeConvert.toEntity(tradeUpdateDTO));
    }

}

