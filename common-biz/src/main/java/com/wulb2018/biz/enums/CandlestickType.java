package com.wulb2018.biz.enums;

import com.wulb2018.common.model.BaseEnum;

/**
 * @author wulubin
 * @date 2026/1/25
 * @description TODO
 */
public enum CandlestickType implements BaseEnum<String> {


    SECOND5("second5", "5秒"),
    MINUTE1("1minute", "1分钟"),
    MINUTE15("15minute", "15分钟"),
    HOUR1("1hour", "1小时"),
    HOUR4("4hour",  "4小时"),
    DAY1("1day", "1天"),
    ;

    CandlestickType(String code, String text) {
        init(code,text);
    }

}
