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
public class CandlestickTypeMinute15Process implements ICandlestickTypeProcess {
    DateTimeFormatter formatter;


    @Override
    public LocalDateTime setRoundDateTime(LocalDateTime dateTime) {
        return setRound15Minute(dateTime);
    }

    @Override
    public LocalDateTime plusTime(LocalDateTime startDateTime) {
        return startDateTime.plusMinutes(15);
    }

    @Override
    public String getFormattedDatetime(LocalDateTime startDateTime) {
        return startDateTime.format(formatter);
    }

    private LocalDateTime setRound15Minute(LocalDateTime localDateTime) {
        int dateTimeMinute = localDateTime.getMinute()/15 * 15 + 15;
        if (dateTimeMinute >= 60) {
            dateTimeMinute = 0;
        }
        return localDateTime.withMinute(dateTimeMinute).withSecond(0).withNano(0);
    }
}
