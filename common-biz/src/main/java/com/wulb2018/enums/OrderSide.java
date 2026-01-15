package com.wulb2018.enums;

import com.wulb2018.model.BaseEnum;
import lombok.Getter;

/**
 * @author wulubin
 * @date 2026/1/15
 * @description TODO
 */
@Getter
public enum OrderSide implements BaseEnum<String> {
    SELL(),
    BUY(),
    ;
}
