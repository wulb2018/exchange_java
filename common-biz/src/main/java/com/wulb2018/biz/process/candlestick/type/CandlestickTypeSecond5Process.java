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
public class CandlestickTypeSecond5Process implements ICandlestickTypeProcess {
    DateTimeFormatter formatter;


    @Override
    public LocalDateTime setRoundDateTime(LocalDateTime dateTime) {
        return setRound5Second(dateTime);
    }

    @Override
    public LocalDateTime plusTime(LocalDateTime startDateTime) {
        return startDateTime.plusSeconds(5);
    }

    @Override
    public String getFormattedDatetime(LocalDateTime startDateTime) {
        return startDateTime.format(formatter);
    }


    private LocalDateTime setRound5Second(LocalDateTime localDateTime) {
        int dateTimeSecond = localDateTime.getSecond()/5 * 5 + 5;
        if (dateTimeSecond < 60) {
            localDateTime = localDateTime.withSecond(dateTimeSecond).withNano(0);
        } else {
            localDateTime = localDateTime.plusSeconds(1).withSecond(0).withNano(0);
        }
        return localDateTime;
    }
}
