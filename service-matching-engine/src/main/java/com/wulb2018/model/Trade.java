package com.wulb2018.model;

import lombok.Data;

/**
 * @author wulubin
 * @date 2026/1/17
 * @description TODO
 */
@Data
public class Trade {
    private Long symbolId;

    private Long buyOrderId;

    private Long sellOrderId;

    private Integer price;

    private Integer quantity;

    private Integer amount;

    @Override
    public String toString(){
        //用于测试
        return "交易对ID = " + symbolId + ", 买方订单ID = " + buyOrderId + ", 卖方订单ID = " + sellOrderId
                + ", 成交价格 = " + price + ",成交数量 = " + quantity + "， 成交额 = " + amount + "\n";
    }
}
