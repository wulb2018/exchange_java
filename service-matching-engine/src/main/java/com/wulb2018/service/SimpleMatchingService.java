package com.wulb2018.service;


import com.wulb2018.enums.OrderSide;
import com.wulb2018.model.Order;
import org.springframework.stereotype.Service;

import java.util.TreeMap;

/**
 * @author wulubin
 * @date 2026/1/14
 * @description TODO
 */
@Service
public class SimpleMatchingService {
    private final static TreeMap<Integer, TreeMap<Long, Order>> priceAndOrderIdAndSellOrderSortedMap = new TreeMap<>();

    private final static TreeMap<Integer, TreeMap<Long, Order>> priceAndOrderIdAndBuyOrderSortedMap = new TreeMap<>();

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

    private void matching(){

    }
}
