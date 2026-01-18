package com.wulb2018.engine.service;


import com.wulb2018.biz.enums.OrderSide;
import com.wulb2018.engine.enums.TradeType;
import com.wulb2018.client.model.OrderFeign;
import com.wulb2018.biz.model.Trade;
import com.wulb2018.engine.model.dto.OrderDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author wulubin
 * @date 2026/1/14
 * @description TODO
 */
@Service
public class SimpleMatchingService {
    //卖方需要先拿到最小的所以是拿第一个
    private final static TreeMap<Integer, TreeMap<Long, OrderDTO>> priceAndOrderIdAndSellOrderSortedMap = new TreeMap<>();
    //买方需要先拿到最大的所以是拿最后一个
    private final static TreeMap<Integer, TreeMap<Long, OrderDTO>> priceAndOrderIdAndBuyOrderSortedMap = new TreeMap<>();

    private final static List<Trade> tradeList = new ArrayList<>();

    private Integer lastTradePrice = 0;

    public void addOrder(OrderDTO orderDTO){
        if(OrderSide.SELL.equals(orderDTO.getSide())) {
            addSellOrder(orderDTO);
        } else {
            addBuyOrder(orderDTO);
        }
        matching();
    }

    private void addSellOrder(OrderDTO sellOrderDTO) {
        doAddOrder(sellOrderDTO, priceAndOrderIdAndSellOrderSortedMap);
    }

    private void addBuyOrder(OrderDTO buyOrderDTO) {
        doAddOrder(buyOrderDTO, priceAndOrderIdAndBuyOrderSortedMap);
    }

    private void doAddOrder(OrderDTO orderDTO, TreeMap<Integer, TreeMap<Long, OrderDTO>> priceAndOrderIdAndOrderSortedMap){
        TreeMap<Long, OrderDTO> orderIdAndOrderSortedMap =  priceAndOrderIdAndOrderSortedMap.get(orderDTO.getPrice());
        if (orderIdAndOrderSortedMap == null) {
            orderIdAndOrderSortedMap = new TreeMap<>();
            orderIdAndOrderSortedMap.put(orderDTO.getId(), orderDTO);
        } else {
            orderIdAndOrderSortedMap.put(orderDTO.getId(), orderDTO);
        }
        priceAndOrderIdAndOrderSortedMap.put(orderDTO.getPrice(), orderIdAndOrderSortedMap);
    }

    public void matching(){
        //卖方需要拿到最小的所以是拿第一个
        Map.Entry<Integer, TreeMap<Long, OrderDTO>> priceAndOrderIdAndMinSellOrderMapEntry = priceAndOrderIdAndSellOrderSortedMap.firstEntry();
        if (priceAndOrderIdAndMinSellOrderMapEntry == null) {
            return;
        }
        Integer minSellPrice = priceAndOrderIdAndMinSellOrderMapEntry.getKey();
        System.out.println("卖方最小价格 = " + minSellPrice);
        //买方需要先拿到最大的所以是拿最后一个
        Map.Entry<Integer, TreeMap<Long, OrderDTO>> priceAndOrderIdAndMaxBuyOrderMapEntry = priceAndOrderIdAndBuyOrderSortedMap.lastEntry();
        if (priceAndOrderIdAndMaxBuyOrderMapEntry == null) {
            return;
        }
        Integer maxBuyPrice = priceAndOrderIdAndMaxBuyOrderMapEntry.getKey();
        System.out.println("买方最大价格 = " + maxBuyPrice);

        if (maxBuyPrice >= minSellPrice) {
            //买方价格比卖方大于等于，那么就一定成交，否则不成交
            System.out.println("买方价格大于等于卖方价格");
            //获取卖方最早的（最小订单id）一个订单
            Map.Entry<Long, OrderDTO> earliestSellOrderEntry = priceAndOrderIdAndMinSellOrderMapEntry.getValue().firstEntry();
            if (earliestSellOrderEntry == null) {
                return;
            }
            OrderDTO earliestSellOrderFeign = earliestSellOrderEntry.getValue();
            //获取买方最早的（最小订单id）一个订单
            Map.Entry<Long, OrderDTO> earliestBuyOrderEntry = priceAndOrderIdAndMaxBuyOrderMapEntry.getValue().firstEntry();
            if (earliestBuyOrderEntry == null) {
                return;
            }
            OrderDTO earliestBuyOrderFeign = earliestBuyOrderEntry.getValue();

            if (earliestSellOrderFeign.getUserId().equals(earliestBuyOrderFeign.getUserId())) {
                //用户id相同则不进行交易
                return;
            }
            if (!earliestSellOrderFeign.getSymbolId().equals(earliestBuyOrderFeign.getSymbolId())) {
                //交易对不同不进行交易
                return;
            }
            lastTradePrice = getLastTradePrice(earliestSellOrderFeign, earliestBuyOrderFeign);
            if (earliestSellOrderFeign.getQuantity().equals(earliestBuyOrderFeign.getQuantity())) {
                //买方价格大于等于卖方价格，且数量相等，直接成交
                System.out.println("数量相等，直接全部成交");
                //添加成交记录
                Trade trade = createTrade(earliestSellOrderFeign, earliestBuyOrderFeign, lastTradePrice, TradeType.COMPLETE);
                tradeList.add(trade);
                //分别在有序map中移除两个订单
                //删除卖方订单
                removeSellOrderFromMap(priceAndOrderIdAndMinSellOrderMapEntry, earliestSellOrderFeign, minSellPrice);
                //删除买方订单
                removeBuyOrderFromMap(priceAndOrderIdAndMaxBuyOrderMapEntry, earliestBuyOrderFeign, maxBuyPrice);
            } else if (earliestSellOrderFeign.getQuantity() > earliestBuyOrderFeign.getQuantity()) {
                //卖方数量比买方大 卖方部分成交
                System.out.println("卖方数量比买方大 卖方部分成交");
                Trade trade = createTrade(earliestSellOrderFeign, earliestBuyOrderFeign, lastTradePrice, TradeType.SELL_PART);
                tradeList.add(trade);
                //删除买方订单
                removeBuyOrderFromMap(priceAndOrderIdAndMaxBuyOrderMapEntry, earliestBuyOrderFeign, maxBuyPrice);
            } else {
                //买方数量比卖方大 买方部分成交
                System.out.println("买方数量比卖方大 买方部分成交");
                Trade trade = createTrade(earliestSellOrderFeign, earliestBuyOrderFeign, lastTradePrice, TradeType.BUY_PART);
                tradeList.add(trade);
                //删除卖方订单
                removeSellOrderFromMap(priceAndOrderIdAndMinSellOrderMapEntry, earliestSellOrderFeign, minSellPrice);
            }
        }
    }

    /**
     * 删除卖方订单
     * @param priceAndOrderIdAndMinSellOrderMapEntry
     * @param earliestSellOrderDTO
     * @param minSellPrice
     */
    private void removeSellOrderFromMap(Map.Entry<Integer, TreeMap<Long, OrderDTO>> priceAndOrderIdAndMinSellOrderMapEntry,
                                        OrderDTO earliestSellOrderDTO, Integer minSellPrice) {
        TreeMap<Long, OrderDTO> orderIdAndMinSellOrderMap = priceAndOrderIdAndMinSellOrderMapEntry.getValue();
        orderIdAndMinSellOrderMap.remove(earliestSellOrderDTO.getId());
        if (orderIdAndMinSellOrderMap.isEmpty()) {
            //该价位的订单数量为0时删除该价位
            priceAndOrderIdAndSellOrderSortedMap.remove(minSellPrice);
        }
    }

    /**
     * 删除买方订单
     * @param priceAndOrderIdAndMaxBuyOrderMapEntry
     * @param earliestBuyOrderDTO
     * @param maxBuyPrice
     */
    private void removeBuyOrderFromMap(Map.Entry<Integer, TreeMap<Long, OrderDTO>> priceAndOrderIdAndMaxBuyOrderMapEntry,
                                       OrderDTO earliestBuyOrderDTO, Integer maxBuyPrice) {
        TreeMap<Long, OrderDTO> orderIdAndMaxBuyOrderMap = priceAndOrderIdAndMaxBuyOrderMapEntry.getValue();
        orderIdAndMaxBuyOrderMap.remove(earliestBuyOrderDTO.getId());
        if(orderIdAndMaxBuyOrderMap.isEmpty()) {
            //该价位的订单数量为0时删除该价位
            priceAndOrderIdAndBuyOrderSortedMap.remove(maxBuyPrice);
        }
    }


    private Trade createTrade(OrderDTO earliestSellOrderDTO, OrderDTO earliestBuyOrderDTO, Integer lastTradePrice, TradeType tradeType) {
        Trade trade = new Trade();
        trade.setSymbolId(earliestSellOrderDTO.getSymbolId());
        trade.setBuyOrderId(earliestBuyOrderDTO.getId());
        trade.setSellOrderId(earliestSellOrderDTO.getId());
        trade.setPrice(lastTradePrice);
        if (TradeType.SELL_PART.equals(tradeType)) {
            //成交数量取较小值
            trade.setQuantity(earliestBuyOrderDTO.getQuantity());
            //卖单 较大值减去 买单较小值，重置卖单数量
            earliestSellOrderDTO.setQuantity(earliestSellOrderDTO.getQuantity() - earliestBuyOrderDTO.getQuantity());
        } else if(TradeType.BUY_PART.equals(tradeType)) {
            //成交数量取较小值
            trade.setQuantity(earliestSellOrderDTO.getQuantity());
            //买单 较大值减去 卖单较小值，重置买单数量
            earliestBuyOrderDTO.setQuantity(earliestBuyOrderDTO.getQuantity() - earliestSellOrderDTO.getQuantity());
        } else {
            //全部成交 就取 卖方订单数量
            trade.setQuantity(earliestSellOrderDTO.getQuantity());
        }
        trade.setAmount(trade.getPrice() * trade.getQuantity());
        return trade;
    }


    private Integer getLastTradePrice(OrderDTO earliestSellOrderDTO, OrderDTO earliestBuyOrderDTO) {
        //比较谁先挂单
        if (earliestSellOrderDTO.getId() < earliestBuyOrderDTO.getId()) {
            System.out.println("成交价格 = " + earliestSellOrderDTO.getPrice());
            return earliestSellOrderDTO.getPrice();
        } else {
            System.out.println("成交价格 = " + earliestBuyOrderDTO.getPrice());
            return earliestBuyOrderDTO.getPrice();
        }
    }

    public void testPrintTradeList() {
        System.out.println(tradeList);
    }

    public String getTradeListString() {
        return tradeList.toString();
    }
}
