package com.wulb2018.service;


import com.wulb2018.enums.OrderSide;
import com.wulb2018.enums.TradeType;
import com.wulb2018.model.Order;
import com.wulb2018.model.Trade;
import io.swagger.v3.oas.models.security.SecurityScheme;
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
    private final static TreeMap<Integer, TreeMap<Long, Order>> priceAndOrderIdAndSellOrderSortedMap = new TreeMap<>();
    //买方需要先拿到最大的所以是拿最后一个
    private final static TreeMap<Integer, TreeMap<Long, Order>> priceAndOrderIdAndBuyOrderSortedMap = new TreeMap<>();

    private final static List<Trade> tradeList = new ArrayList<>();

    private Integer lastTradePrice = 0;

    public void addOrder(Order order){
        if(OrderSide.SELL.equals(order.getSide())) {
            addSellOrder(order);
        } else {
            addBuyOrder(order);
        }
        matching();
    }

    private void addSellOrder(Order sellOrder) {
        doAddOrder(sellOrder, priceAndOrderIdAndSellOrderSortedMap);
    }

    private void addBuyOrder(Order buyOrder) {
        doAddOrder(buyOrder, priceAndOrderIdAndBuyOrderSortedMap);
    }

    private void doAddOrder(Order order, TreeMap<Integer, TreeMap<Long, Order>> priceAndOrderIdAndOrderSortedMap){
        TreeMap<Long, Order> orderIdAndOrderSortedMap =  priceAndOrderIdAndOrderSortedMap.get(order.getPrice());
        if (orderIdAndOrderSortedMap == null) {
            orderIdAndOrderSortedMap = new TreeMap<>();
            orderIdAndOrderSortedMap.put(order.getId(), order);
        } else {
            orderIdAndOrderSortedMap.put(order.getId(), order);
        }
        priceAndOrderIdAndOrderSortedMap.put(order.getPrice(), orderIdAndOrderSortedMap);
    }

    public void matching(){
        //卖方需要拿到最小的所以是拿第一个
        Map.Entry<Integer, TreeMap<Long, Order>> priceAndOrderIdAndMinSellOrderMapEntry = priceAndOrderIdAndSellOrderSortedMap.firstEntry();
        if (priceAndOrderIdAndMinSellOrderMapEntry == null) {
            return;
        }
        Integer minSellPrice = priceAndOrderIdAndMinSellOrderMapEntry.getKey();
        System.out.println("卖方最小价格 = " + minSellPrice);
        //买方需要先拿到最大的所以是拿最后一个
        Map.Entry<Integer, TreeMap<Long, Order>> priceAndOrderIdAndMaxBuyOrderMapEntry = priceAndOrderIdAndBuyOrderSortedMap.lastEntry();
        if (priceAndOrderIdAndMaxBuyOrderMapEntry == null) {
            return;
        }
        Integer maxBuyPrice = priceAndOrderIdAndMaxBuyOrderMapEntry.getKey();
        System.out.println("买方最大价格 = " + maxBuyPrice);

        if (maxBuyPrice >= minSellPrice) {
            //买方价格比卖方大于等于，那么就一定成交，否则不成交
            System.out.println("买方价格大于等于卖方价格");
            //获取卖方最早的（最小订单id）一个订单
            Map.Entry<Long, Order> earliestSellOrderEntry = priceAndOrderIdAndMinSellOrderMapEntry.getValue().firstEntry();
            if (earliestSellOrderEntry == null) {
                return;
            }
            Order earliestSellOrder = earliestSellOrderEntry.getValue();
            //获取买方最早的（最小订单id）一个订单
            Map.Entry<Long, Order> earliestBuyOrderEntry = priceAndOrderIdAndMaxBuyOrderMapEntry.getValue().firstEntry();
            if (earliestBuyOrderEntry == null) {
                return;
            }
            Order earliestBuyOrder = earliestBuyOrderEntry.getValue();

            if (earliestSellOrder.getUserId().equals(earliestBuyOrder.getUserId())) {
                //用户id相同则不进行交易
                return;
            }
            if (!earliestSellOrder.getSymbolId().equals(earliestBuyOrder.getSymbolId())) {
                //交易对不同不进行交易
                return;
            }
            lastTradePrice = getLastTradePrice(earliestSellOrder, earliestBuyOrder);
            if (earliestSellOrder.getQuantity().equals(earliestBuyOrder.getQuantity())) {
                //买方价格大于等于卖方价格，且数量相等，直接成交
                System.out.println("数量相等，直接全部成交");
                //添加成交记录
                Trade trade = createTrade(earliestSellOrder, earliestBuyOrder, lastTradePrice, TradeType.COMPLETE);
                tradeList.add(trade);
                //分别在有序map中移除两个订单
                //删除卖方订单
                removeSellOrderFromMap(priceAndOrderIdAndMinSellOrderMapEntry, earliestSellOrder, minSellPrice);
                //删除买方订单
                removeBuyOrderFromMap(priceAndOrderIdAndMaxBuyOrderMapEntry, earliestBuyOrder, maxBuyPrice);
            } else if (earliestSellOrder.getQuantity() > earliestBuyOrder.getQuantity()) {
                //卖方数量比买方大 卖方部分成交
                System.out.println("卖方数量比买方大 卖方部分成交");
                Trade trade = createTrade(earliestSellOrder, earliestBuyOrder, lastTradePrice, TradeType.SELL_PART);
                tradeList.add(trade);
                //删除买方订单
                removeBuyOrderFromMap(priceAndOrderIdAndMaxBuyOrderMapEntry, earliestBuyOrder, maxBuyPrice);
            } else {
                //买方数量比卖方大 买方部分成交
                System.out.println("买方数量比卖方大 买方部分成交");
                Trade trade = createTrade(earliestSellOrder, earliestBuyOrder, lastTradePrice, TradeType.BUY_PART);
                tradeList.add(trade);
                //删除卖方订单
                removeSellOrderFromMap(priceAndOrderIdAndMinSellOrderMapEntry, earliestSellOrder, minSellPrice);
            }
        }
    }

    /**
     * 删除卖方订单
     * @param priceAndOrderIdAndMinSellOrderMapEntry
     * @param earliestSellOrder
     * @param minSellPrice
     */
    private void removeSellOrderFromMap(Map.Entry<Integer, TreeMap<Long, Order>> priceAndOrderIdAndMinSellOrderMapEntry,
                                        Order earliestSellOrder, Integer minSellPrice) {
        TreeMap<Long, Order> orderIdAndMinSellOrderMap = priceAndOrderIdAndMinSellOrderMapEntry.getValue();
        orderIdAndMinSellOrderMap.remove(earliestSellOrder.getId());
        if (orderIdAndMinSellOrderMap.isEmpty()) {
            //该价位的订单数量为0时删除该价位
            priceAndOrderIdAndSellOrderSortedMap.remove(minSellPrice);
        }
    }

    /**
     * 删除买方订单
     * @param priceAndOrderIdAndMaxBuyOrderMapEntry
     * @param earliestBuyOrder
     * @param maxBuyPrice
     */
    private void removeBuyOrderFromMap(Map.Entry<Integer, TreeMap<Long, Order>> priceAndOrderIdAndMaxBuyOrderMapEntry,
                                       Order earliestBuyOrder, Integer maxBuyPrice) {
        TreeMap<Long, Order> orderIdAndMaxBuyOrderMap = priceAndOrderIdAndMaxBuyOrderMapEntry.getValue();
        orderIdAndMaxBuyOrderMap.remove(earliestBuyOrder.getId());
        if(orderIdAndMaxBuyOrderMap.isEmpty()) {
            //该价位的订单数量为0时删除该价位
            priceAndOrderIdAndBuyOrderSortedMap.remove(maxBuyPrice);
        }
    }


    private Trade createTrade(Order earliestSellOrder, Order earliestBuyOrder, Integer lastTradePrice,TradeType tradeType) {
        Trade trade = new Trade();
        trade.setSymbolId(earliestSellOrder.getSymbolId());
        trade.setBuyOrderId(earliestBuyOrder.getId());
        trade.setSellOrderId(earliestSellOrder.getId());
        trade.setPrice(lastTradePrice);
        if (TradeType.SELL_PART.equals(tradeType)) {
            //成交数量取较小值
            trade.setQuantity(earliestBuyOrder.getQuantity());
            //卖单 较大值减去 买单较小值，重置卖单数量
            earliestSellOrder.setQuantity(earliestSellOrder.getQuantity() - earliestBuyOrder.getQuantity());
        } else if(TradeType.BUY_PART.equals(tradeType)) {
            //成交数量取较小值
            trade.setQuantity(earliestSellOrder.getQuantity());
            //买单 较大值减去 卖单较小值，重置买单数量
            earliestBuyOrder.setQuantity(earliestBuyOrder.getQuantity() - earliestSellOrder.getQuantity());
        } else {
            //全部成交 就取 卖方订单数量
            trade.setQuantity(earliestSellOrder.getQuantity());
        }
        trade.setAmount(trade.getPrice() * trade.getQuantity());
        return trade;
    }


    private Integer getLastTradePrice(Order earliestSellOrder, Order earliestBuyOrder) {
        //比较谁先挂单
        if (earliestSellOrder.getId() < earliestBuyOrder.getId()) {
            System.out.println("成交价格 = " + earliestSellOrder.getPrice());
            return earliestSellOrder.getPrice();
        } else {
            System.out.println("成交价格 = " + earliestBuyOrder.getPrice());
            return earliestBuyOrder.getPrice();
        }
    }

    public void testPrintTradeList() {
        System.out.println(tradeList);
    }

    public String getTradeListString() {
        return tradeList.toString();
    }
}
