package com.wulb2018.biz.enums;

import com.wulb2018.biz.constant.DateTimeFormatterConst;
import com.wulb2018.biz.process.candlestick.ICandlestickTypeProcess;
import com.wulb2018.biz.process.candlestick.type.*;
import com.wulb2018.common.model.BaseEnum;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

/**
 * @author wulubin
 * @date 2026/1/25
 * @description TODO
 */
@Getter
public enum CandlestickType implements BaseEnum<String> {

    SECOND5("second5", "5秒", DateTimeFormatterConst.secondFormatter, new CandlestickTypeSecond5Process()),
    MINUTE1("1minute", "1分钟", DateTimeFormatterConst.minuteFormatter, new CandlestickTypeMinute1Process()),
    MINUTE15("15minute", "15分钟", DateTimeFormatterConst.minuteFormatter,new CandlestickTypeMinute15Process()),
    HOUR1("1hour", "1小时", DateTimeFormatterConst.hourFormatter, new CandlestickTypeHour1Process()),
    HOUR4("4hour",  "4小时", DateTimeFormatterConst.hourFormatter, new CandlestickTypeHour4Process()),
    DAY1("1day", "1天", DateTimeFormatterConst.dayFormatter, new CandlestickTypeDay1Process()),
    ;

    private ICandlestickTypeProcess candlestickTypeProcess;
    private DateTimeFormatter dateTimeFormatter;

    CandlestickType(String code, String text, DateTimeFormatter dateTimeFormatter, ICandlestickTypeProcess candlestickTypeProcess) {
        init(code,text);
        this.dateTimeFormatter = dateTimeFormatter;
        this.candlestickTypeProcess = candlestickTypeProcess;
        this.candlestickTypeProcess.setFormatter(dateTimeFormatter);
    }

}
