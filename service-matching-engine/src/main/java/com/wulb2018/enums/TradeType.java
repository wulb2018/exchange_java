package com.wulb2018.enums;

import com.wulb2018.model.BaseEnum;
import lombok.Getter;

/**
 * @author wulubin
 * @date 2026/1/17
 * @description TODO
 */
@Getter
public enum TradeType implements BaseEnum<Integer> {
    COMPLETE(1, "全部成交"),
    SELL_PART(2, "卖方部分成交"),
    BUY_PART(3, "买方部分成交"),
    ;

    TradeType(Integer code, String text) {
        init(code,text);
    }
}
