package com.wulb2018.enums;

import com.wulb2018.model.BaseEnum;
import lombok.Getter;

/**
 * @author wulubin
 * @date 2026/1/15
 * @description TODO
 */
@Getter
public enum OrderSide implements BaseEnum<Integer> {
    SELL(1, "卖方"),
    BUY(2, "买方"),
    ;

    OrderSide(Integer code, String text) {
        init(code,text);
    }
}
