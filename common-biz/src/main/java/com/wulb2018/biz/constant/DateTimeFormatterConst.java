package com.wulb2018.biz.constant;

import java.time.format.DateTimeFormatter;

/**
 * @author wulubin
 * @date 2026/1/27
 * @description TODO
 */
public interface DateTimeFormatterConst {
    DateTimeFormatter secondFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter minuteFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
    DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
}
