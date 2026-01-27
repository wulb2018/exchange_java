package com.wulb2018.settlement.model.entity;

import com.wulb2018.biz.enums.CandlestickType;
import com.wulb2018.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * (t_candlestick)-数据表对应实体类
 *
 * @author makejava
 * @since 2026-01-26 20:41:12
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Candlestick extends BaseEntity<Candlestick> {

    /**
     * 蜡烛类型，类目类型
     */
    private CandlestickType candlestickType;

    /**
     * 时间类目
     */
    private String datetimeCategory;

    /**
     * 开盘价
     */
    private Double openPrice;

    /**
     * 收盘价
     */
    private Double closePrice;

    /**
     * 最低价
     */
    private Double lowestPrice;

    /**
     * 最高价
     */
    private Double highestPrice;


}

