package com.wulb2018.biz.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @author wulubin
 * @date 2026/1/25
 * @description TODO
 */
public class PriceGenerator implements Iterable<Double>{
    private final static Random random = new Random();
    private final double startPrice;

    private final double drift;
    private final double volatility;
    private final String tickSize;

    public PriceGenerator(double startPrice, double drift, double volatility, String tickSize) {
        this.startPrice = startPrice;
        this.drift = drift;
        this.volatility = volatility;
        this.tickSize = tickSize;
    }

    public PriceGenerator(double startPrice, double drift, double volatility) {
        this.startPrice = startPrice;
        this.drift = drift;
        this.volatility = volatility;
        this.tickSize = "0.01";
    }

    @Override
    public Iterator<Double> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return true;
            }
            @Override
            public Double next() {
                double price = startPrice;
                // 正态分布噪声
                double noise = random.nextGaussian() * volatility;
                // 带趋势的随机游走
                price += drift + noise;
                // 价格不能为负
                if (price < Double.parseDouble(tickSize)) {
                    price = Double.parseDouble(tickSize);
                }
                BigDecimal bigDecimalTickSize = new BigDecimal(tickSize);
                BigDecimal bdPrice = BigDecimal.valueOf(price)
                        .divide(bigDecimalTickSize, 0, RoundingMode.HALF_UP)
                        .multiply(bigDecimalTickSize);
                // 对齐 tick size
                return bdPrice.doubleValue();
            }
        };
    }



    //todo 后期删除下面的方法
    /**
     * 生成带趋势的随机价格序列
     *
     * @param startPrice 初始价格
     * @param steps      生成多少个价格
     * @param drift      趋势项 μ（如 0.01 表示缓慢上涨）
     * @param volatility 波动率 σ（如 0.2）
     * @param tickSize   最小变动价位（如 0.01）
     */
    public static List<Double> generatePrices(
            double startPrice,
            int steps,
            double drift,
            double volatility,
            double tickSize) {

        List<Double> prices = new ArrayList<>(steps);
        double price = startPrice;

        for (int i = 0; i < steps; i++) {
            // 正态分布噪声
            double noise = random.nextGaussian() * volatility;

            // 带趋势的随机游走
            price += drift + noise;

            // 价格不能为负
            if (price < tickSize) {
                price = tickSize;
            }

            // 对齐 tick size
            price = Math.round(price / tickSize) * tickSize;

            prices.add(price);
        }

        return prices;
    }
}
