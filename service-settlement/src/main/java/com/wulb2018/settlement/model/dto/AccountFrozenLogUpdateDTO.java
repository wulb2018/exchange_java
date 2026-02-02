package com.wulb2018.settlement.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 账户资金冻结记录(t_account_frozen_log)-修改实体参数类
 *
 * @author makejava
 * @since 2026-02-01 18:31:56
 */
@Data
@Accessors(chain = true)
@ApiModel("AccountFrozenLogUpdateDTO")
public class AccountFrozenLogUpdateDTO {

    @ApiModelProperty("记录ID")
    private Long id;

    @ApiModelProperty("资金账户id")
    private Long accountId;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("冻结资金数量")
    private Double frozen;

}

