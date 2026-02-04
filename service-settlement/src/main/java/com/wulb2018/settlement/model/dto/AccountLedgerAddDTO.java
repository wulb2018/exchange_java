package com.wulb2018.settlement.model.dto;

import com.wulb2018.settlement.enums.AccountLedgerBizType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 账务流水，最重要(t_account_ledger)-添加实体参数类
 *
 * @author makejava
 * @since 2026-01-18 18:12:44
 */
@Data
@Accessors(chain = true)
@ApiModel("AccountLedgerAddDTO")
public class AccountLedgerAddDTO {


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

