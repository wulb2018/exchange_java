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
public class CandlestickTypeHour1Process implements ICandlestickTypeProcess {
    DateTimeFormatter formatter;


    @Override
    public LocalDateTime setRoundDateTime(LocalDateTime dateTime) {
        return setRound1Hour(dateTime);
    }

    @Override
    public LocalDateTime plusTime(LocalDateTime startDateTime) {
        return startDateTime.plusHours(1);
    }

    @Override
    public String getFormattedDatetime(LocalDateTime startDateTime) {
        return startDateTime.format(formatter);
    }

    private LocalDateTime setRound1Hour(LocalDateTime localDateTime) {
        return localDateTime.withMinute(0).withSecond(0).withNano(0);
    }
}
