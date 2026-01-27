package com.wulb2018.biz.util;

import java.math.BigDecimal;

/**
 * 精度转换工具
 * @author wulubin
 * @date 2026/1/24
 * @description TODO
 */
public class DataPrecisionConvert {
    public static int decimalToInt(double value, int precision) {
        // BigDecimal.TEN.pow();
        return  (int) (value * Math.pow( 10,precision));
    }

    public static double intToDecimal(int value, int precision) {
        double doubleValue = (double) value;
        doubleValue = doubleValue / Math.pow( 10,precision);
        return doubleValue;
    }

    public static void main(String[] args) {
        int intValue = 198;
        int precision = 4;
        double ret = DataPrecisionConvert.intToDecimal(intValue, precision);
        System.out.println(ret);
        double doubleValue = 2.098;
        int intRet = DataPrecisionConvert.decimalToInt(doubleValue, precision);
        System.out.println(intRet);
    }
}
