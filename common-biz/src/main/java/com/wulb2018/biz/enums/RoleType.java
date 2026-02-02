package com.wulb2018.biz.enums;

import com.wulb2018.common.model.BaseEnum;

/**
 * @author wulubin
 * @date 2026/2/1
 * @description TODO
 */
public enum RoleType implements BaseEnum<Integer> {

    MAKER(1, "被动单,先进入的订单"),
    TAKER(2, "主动单,后进入的订单"),
    ;

    RoleType(Integer code, String text) {
        init(code,text);
    }
}
