package com.wulb2018.settlement.model.entity;

import com.wulb2018.common.model.BaseEntity;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 手续费规则(t_fee_rule)-数据表对应实体类
 *
 * @author makejava
 * @since 2026-01-18 19:21:57
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FeeRule extends BaseEntity<FeeRule> {

    /**
     * 交易对
     */
    private Long symbolId;

    /**
     * Maker费率
     */
    private Double makerFeeRate;

    /**
     * Taker费率
     */
    private Double takerFeeRate;

    /**
     * 是否生效
     */
    private Integer status;

}

