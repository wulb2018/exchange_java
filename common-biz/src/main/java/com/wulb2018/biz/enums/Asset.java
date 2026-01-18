package com.wulb2018.biz.enums;

import com.wulb2018.common.model.BaseEnum;

/**
 * @author wulubin
 * @date 2026/1/18
 * @description TODO
 */
public enum Asset implements BaseEnum<String> {

    USDT("USDT", "USDT"),
    BTC("BTC", "BTC"),
    ;

    Asset(String code, String text) {
        init(code,text);
    }
}
