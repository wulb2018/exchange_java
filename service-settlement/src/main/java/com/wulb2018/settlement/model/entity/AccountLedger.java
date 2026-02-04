package com.wulb2018.settlement.model.entity;

import com.wulb2018.common.model.BaseEntity;
import com.wulb2018.settlement.enums.AccountLedgerBizType;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 账务流水，最重要(t_account_ledger)-数据表对应实体类
 *
 * @author makejava
 * @since 2026-01-18 18:12:44
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AccountLedger extends BaseEntity<AccountLedger> {

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 业务类型（TRADE、FEE 等）
     */
    private AccountLedgerBizType bizType;

    /**
     * 业务ID（trade_id）
     */
    private Long bizId;

    /**
     * 变动金额（正负）
     */
    private Double changeAmount;

    /**
     * 变动后余额
     */
    private Double balanceAfter;


}

