package com.wulb2018.biz.model.entity;

import com.wulb2018.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * (t_stock)-数据表对应实体类
 *
 * @author makejava
 * @since 2026-02-07 14:53:28
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Stock extends BaseEntity<Stock> {

    /**
     * 股票名字
     */
    private String name;

    /**
     * 股票代码
     */
    private String code;

    /**
     * 发行价格
     */
    private BigDecimal issuePrice;


}

