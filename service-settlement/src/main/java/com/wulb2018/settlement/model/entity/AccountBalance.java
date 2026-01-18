package com.wulb2018.settlement.model.entity;

import java.time.LocalDateTime;

import com.wulb2018.common.model.BaseEntity;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 余额表(t_account_balance)-数据表对应实体类
 *
 * @author makejava
 * @since 2026-01-18 18:12:07
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AccountBalance extends BaseEntity<AccountBalance> {

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 可用余额
     */
    private Double available;

    /**
     * 冻结余额
     */
    private Double frozen;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

}

