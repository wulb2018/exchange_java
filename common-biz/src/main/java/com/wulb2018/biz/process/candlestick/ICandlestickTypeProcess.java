package com.wulb2018.biz.process.candlestick;

import com.wulb2018.biz.model.vo.CandlestickVO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author wulubin
 * @date 2026/2/1
 * @description TODO
 */
public interface ICandlestickTypeProcess {

    LocalDateTime setRoundDateTime(LocalDateTime dateTime);
    LocalDateTime plusTime(LocalDateTime startDateTime);
    String getFormattedDatetime(LocalDateTime startDateTime);
    void setFormatter(DateTimeFormatter dateTimeFormatter);

    default Map<String, CandlestickVO> buildZeroVolumeCandlestickVOMap(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        startDateTime = setRoundDateTime(startDateTime);
        endDateTime = setRoundDateTime(endDateTime);
        Map<String, CandlestickVO> dateAndCandlestickVOMap = new TreeMap<>();
        while (startDateTime.isBefore(endDateTime) || startDateTime.isEqual(endDateTime)) {
            String formattedDatetime = getFormattedDatetime(startDateTime);
            //String formattedDatetime = startDateTime.format(formatter);
            CandlestickVO candlestickVO = buildZeroVolumeCandlestickVO(formattedDatetime);
            dateAndCandlestickVOMap.put(formattedDatetime, candlestickVO);
            startDateTime = plusTime(startDateTime);
        }
        return dateAndCandlestickVOMap;
    }

    /**
     * 构建0交易量蜡烛VO
     * @param formattedDatetime
     * @return
     */
    default CandlestickVO buildZeroVolumeCandlestickVO(String formattedDatetime) {
        CandlestickVO candlestickVO = new CandlestickVO();
        candlestickVO.setDatetimeCategory(formattedDatetime);
        candlestickVO.setClosePrice(0.);
        candlestickVO.setHighestPrice(0.);
        candlestickVO.setVolume(0.);
        return candlestickVO;
    }
}