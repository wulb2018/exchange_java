package com.wulb2018.biz.model.dto;

import com.wulb2018.biz.enums.OrderSide;
import lombok.Data;

/**
 * @author wulubin
 * @date 2026/1/17
 * @description TODO
 */
@Data
public class TradeCommonDTO {
    /**
     * 交易对
     */
    private Long symbolId;

    /**
     * 买单ID
     */
    private Long buyOrderId;

    /**
     * 卖单ID
     */
    private Long sellOrderId;

    /**
     * 买方
     */
    private Long buyUserId;

    /**
     * 卖方
     */
    private Long sellUserId;

    /**
     * 成交价
     */
    private Integer price;

    /**
     * 成交数量
     */
    private Integer quantity;

    /**
     * 成交额
     */
    private Integer amount;
    /**
     * maker方向，是买方还是卖方
     */
    private OrderSide makerSide;

    @Override
    public String toString(){
        //用于测试
        return "交易对ID = " + symbolId + ", 买方订单ID = " + buyOrderId + ", 卖方订单ID = " + sellOrderId
                + ", 成交价格 = " + price + ",成交数量 = " + quantity + "， 成交额 = " + amount + "\n";
    }
}
