package com.wulb2018.settlement.model.entity;

import com.wulb2018.biz.enums.RoleType;
import com.wulb2018.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 手续费记录(t_fee_record)-数据表对应实体类
 *
 * @author makejava
 * @since 2026-02-01 20:15:08
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FeeRecord extends BaseEntity<FeeRecord> {

    /**
     * 成交ID
     */
    private Long tradeId;

    /**
     * 扣费用户
     */
    private Long userId;

    /**
     * 手续费币种
     */
    private String asset;

    /**
     * 手续费金额
     */
    private Double amount;

    /**
     * 1=Maker，2=Taker
     */
    private RoleType role;

}

