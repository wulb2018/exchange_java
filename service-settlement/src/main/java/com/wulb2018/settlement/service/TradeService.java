package com.wulb2018.settlement.service;


import com.wulb2018.biz.enums.CandlestickType;
import com.wulb2018.biz.model.dto.OrderUpdateDTO;
import com.wulb2018.biz.model.vo.CandlestickVO;
import com.wulb2018.biz.model.vo.TradeVO;
import com.wulb2018.biz.process.candlestick.ICandlestickTypeProcess;
import com.wulb2018.client.order.OrderFeignClient;
import com.wulb2018.common.service.BaseService;
import com.wulb2018.settlement.mapper.TradeMapper;
import com.wulb2018.settlement.model.dto.TradeAddDTO;
import com.wulb2018.settlement.model.dto.TradeDTO;
import com.wulb2018.settlement.model.dto.TradeUpdateDTO;
import com.wulb2018.settlement.model.entity.Trade;
import com.wulb2018.settlement.service.convert.TradeConvert;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
    @Resource
    private AccountService accountService;
    @Resource
    private UserPositionsService userPositionsService;
//    private final static List<CandlestickVO> LAST_SOME_CANDLESTICK_LIST = new ArrayList<>();
//    private final static int LAST_SOME_CANDLESTICK_LIST_SIZE = 3;

    /**
     * 结算交易
     * @param tradeDTO
     */
    public void settlementTrade(TradeDTO tradeDTO){
        //保存交易记录
        Trade trade = tradeConvert.toEntity(tradeDTO);
        //Trade trade = BeanUtil.copyProperties(tradeDTO, Trade.class);
        //trade.setMakerSide(tradeDTO.getMakerSide().getCode());
        //todo 这边会把OrderSide makerSide 在数据保存为字符串，，这里正常情况下应该int型，有空这里要研究研究
        save(trade);
        //缓存维护 最后几个蜡烛图
        candlestickService.safeguardCandlestickCompleteness(trade);

        userPositionsService.initUserPositions(tradeDTO.getBuyUserId(), tradeDTO.getStockId());
        userPositionsService.initUserPositions(tradeDTO.getSellUserId(), tradeDTO.getStockId());

        accountService.settlementAccount(trade);
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



    public List<CandlestickVO> getCandlestickInitData(CandlestickType candlestickType) {
        List<Trade> list = lambdaQuery().orderByAsc(Trade::getId).list();
        Trade firstTrade = list.get(0);
        Trade lastTrade = list.get(list.size() - 1);
        Map<String,CandlestickVO> dateAndCandlestickVOMap = candlestickService.buildDateAndCandlestickVOMap(firstTrade.getCreateDate(), lastTrade.getCreateDate(), candlestickType);
        for (Trade trade : list) {
            String formattedTradeCreateTime = formatTradeCreateTime(candlestickType, trade.getCreateDate());
            CandlestickVO candlestickVO = dateAndCandlestickVOMap.get(formattedTradeCreateTime);
            if (candlestickVO == null) {
                System.out.println(formattedTradeCreateTime);
                continue;
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


        //buildLastSomeCandlestickList(candlestickVOList);

        return candlestickVOList;
    }

//    private synchronized void  buildLastSomeCandlestickList(List<CandlestickVO> candlestickVOList) {
//        int size = candlestickVOList.size();
//        for (int i = 1; i <= 3; i++) {
//            CandlestickVO candlestickVO = candlestickVOList.get(size - i);
//            if (candlestickVO != null) {
//                LAST_SOME_CANDLESTICK_LIST.add(candlestickVO);
//                if (LAST_SOME_CANDLESTICK_LIST.size() > LAST_SOME_CANDLESTICK_LIST_SIZE) {
//                    LAST_SOME_CANDLESTICK_LIST.remove(LAST_SOME_CANDLESTICK_LIST.size() - 1);
//                }
//            }
//        }
//    }

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
        ICandlestickTypeProcess candlestickTypeProcess = candlestickType.getCandlestickTypeProcess();
        return candlestickTypeProcess.getFormattedDatetime(tradeCreateDate);
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

