package com.wulb2018.biz.enums;

import com.wulb2018.common.model.BaseEnum;
import lombok.Getter;

/**
 * @author wulubin
 * @date 2026/1/19
 * @description TODO
 */
@Getter
public enum OrderStatus implements BaseEnum<Integer> {

    NEW(0, "新建"),
    PART_TRADE(1, "部分成交"),
    COMPLETE(2, "全部成交"),
    CANCEL(3, "已取消"),
    ;

    OrderStatus(Integer code, String text) {
        init(code,text);
    }
}
