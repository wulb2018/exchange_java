package com.wulb2018.biz.enums;

import com.wulb2018.common.model.BaseEnum;

/**
 * @author wulubin
 * @date 2026/1/15
 * @description TODO
 */
public enum OrderSide implements BaseEnum<Integer> {
    BUY(1, "买方"),
    SELL(2, "卖方"),
    ;

    OrderSide(Integer code, String text) {
        init(code,text);
    }

//    public static OrderSide fromCode(Integer code) {
//        //todo 是不是因为这个没有在EnumPool里面统一管理的原因导致在保存到数据库中没办法转为数字类型
//        for (OrderSide orderSide: values()) {
//            if(orderSide.getCode().equals(code)) {
//                return orderSide;
//            }
//        }
//        throw new IllegalArgumentException("Unknown OrderSide code: " + code);
//    }
}
