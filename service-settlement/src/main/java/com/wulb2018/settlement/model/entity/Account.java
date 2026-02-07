package com.wulb2018.settlement.model.entity;

import com.wulb2018.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 账户主表(t_account)-数据表对应实体类
 *
 * @author makejava
 * @since 2026-01-18 18:11:50
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Account extends BaseEntity<Account> {

    /**
     * 用户ID
     */
    private Long userId;

//    /**
//     * 资产币种
//     */
//    private String asset;

    /**
     * 可用余额
     */
    private Double available;

    /**
     * 冻结余额
     */
    private Double frozen;



}

