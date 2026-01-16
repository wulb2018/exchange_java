package com.wulb2018.service;


import com.wulb2018.enums.OrderSide;
import com.wulb2018.model.Order;
import org.springframework.stereotype.Service;

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
        Order sellOrder1 = new Order();
        sellOrder1.setId(1L);
        sellOrder1.setType(1);
        sellOrder1.setSide(OrderSide.SELL);
        sellOrder1.setPrice(80);
        sellOrder1.setQuantity(1);
        sellOrder1.setSymbolId(1L);
        addSellOrder(sellOrder1);

//        Order sellOrder2 = new Order();
//        sellOrder2.setId(3L);
//        sellOrder2.setType(1);
//        sellOrder2.setSide(OrderSide.SELL);
//        sellOrder2.setPrice(99);
//        sellOrder2.setQuantity(1);
//        sellOrder2.setSymbolId(1L);
//        addSellOrder(sellOrder2);

        Order buyOrder1 = new Order();
        buyOrder1.setId(2L);
        buyOrder1.setType(1);
        buyOrder1.setSide(OrderSide.BUY);
        buyOrder1.setPrice(100);
        buyOrder1.setQuantity(1);
        buyOrder1.setSymbolId(1L);
        addBuyOrder(buyOrder1);

        //卖方需要拿到最小的所以是拿第一个
        Map.Entry<Integer, TreeMap<Long, Order>> minSellOrderMapEntry = priceAndOrderIdAndSellOrderSortedMap.firstEntry();
        Integer minSellPrice = minSellOrderMapEntry.getKey();
        System.out.println("卖方最小价格 = " + minSellPrice);
        //买方需要先拿到最大的所以是拿最后一个
        Map.Entry<Integer, TreeMap<Long, Order>> maxBuyOrderMapEntry = priceAndOrderIdAndBuyOrderSortedMap.lastEntry();
        Integer maxBuyPrice = maxBuyOrderMapEntry.getKey();
        System.out.println("买方最大价格 = " + maxBuyPrice);

        if (maxBuyPrice >= minSellPrice) {
            //买方价格比卖方大于等于，那么就一定成交，否则不成交
            //获取卖方最早的（最小订单id）一个订单
            Map.Entry<Long, Order> earliestSellOrderEntry = minSellOrderMapEntry.getValue().firstEntry();
            Order earliestSellOrder = earliestSellOrderEntry.getValue();
            //获取买方最早的（最小订单id）一个订单
            Map.Entry<Long, Order> earliestBuyOrderEntry = maxBuyOrderMapEntry.getValue().firstEntry();
            Order earliestBuyOrder = earliestBuyOrderEntry.getValue();
            if (earliestSellOrder.getQuantity().equals(earliestBuyOrder.getQuantity())) {
                //买方价格大于等于卖方价格，且数量相等，直接成交
                System.out.println("买方价格大于等于卖方价格，且数量相等，直接成交");
                //比较谁先挂单
                if (earliestSellOrder.getId() < earliestBuyOrder.getId()) {
                    lastTradePrice = earliestSellOrder.getPrice();
                } else {
                    lastTradePrice = earliestBuyOrder.getPrice();
                }
                System.out.println("成交价格 = " + lastTradePrice);
            }
        }
    }
}
