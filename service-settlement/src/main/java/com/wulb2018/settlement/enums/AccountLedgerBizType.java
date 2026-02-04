package com.wulb2018.settlement.enums;

import com.wulb2018.common.model.BaseEnum;

/**
 * @author wulubin
 * @date 2026/2/3
 * @description TODO
 */
public enum AccountLedgerBizType implements BaseEnum<String> {
    TRADE("TRADE", "TRADE"),
    FEE("FEE", "FEE"),
    ;

    AccountLedgerBizType(String code, String text) {
        init(code,text);
    }
}
