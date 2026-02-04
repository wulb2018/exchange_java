package com.wulb2018.settlement.model.vo;

import com.wulb2018.common.model.BaseVO;
import com.wulb2018.settlement.enums.AccountLedgerBizType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 账务流水，最重要(t_account_ledger)-展现层实体类
 *
 * @author makejava
 * @since 2026-01-18 18:12:44
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@ApiModel("AccountLedgerVO")
public class AccountLedgerVO extends BaseVO<AccountLedgerVO> {

    @ApiModelProperty("账户ID")
    private Long accountId;

    @ApiModelProperty("业务类型（TRADE、FEE 等）")
    private AccountLedgerBizType bizType;

    @ApiModelProperty("业务ID（trade_id）")
    private Long bizId;

    @ApiModelProperty("变动金额（正负）")
    private Double changeAmount;

    @ApiModelProperty("变动后余额")
    private Double balanceAfter;



}

