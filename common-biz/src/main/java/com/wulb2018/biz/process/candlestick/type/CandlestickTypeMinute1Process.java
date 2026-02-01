package com.wulb2018.biz.process.candlestick.type;

import com.wulb2018.biz.process.candlestick.ICandlestickTypeProcess;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author wulubin
 * @date 2026/2/1
 * @description TODO
 */
@Data
public class CandlestickTypeMinute1Process implements ICandlestickTypeProcess {
    DateTimeFormatter formatter;

    @Override
    public LocalDateTime setRoundDateTime(LocalDateTime localDateTime) {
        return setDateTimeZeroSecond(localDateTime);
    }
    @Override
    public LocalDateTime plusTime(LocalDateTime dateTime) {
        return dateTime.plusMinutes(1L);
    }
    @Override
    public String getFormattedDatetime(LocalDateTime startDateTime) {
        return startDateTime.format(formatter);
    }


    private LocalDateTime setDateTimeZeroSecond(LocalDateTime localDateTime) {
        return localDateTime.withSecond(0).withNano(0);
    }

}
