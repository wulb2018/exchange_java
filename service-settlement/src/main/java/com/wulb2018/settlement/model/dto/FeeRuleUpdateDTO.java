package com.wulb2018.settlement.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 手续费规则(t_fee_rule)-修改实体参数类
 *
 * @author makejava
 * @since 2026-01-18 19:21:58
 */
@Data
@Accessors(chain = true)
@ApiModel("FeeRuleUpdateDTO")
public class FeeRuleUpdateDTO {

    @ApiModelProperty("规则ID")
    private Long id;

    @ApiModelProperty("交易对")
    private Long symbolId;

    @ApiModelProperty("Maker费率")
    private Double makerFeeRate;

    @ApiModelProperty("Taker费率")
    private Double takerFeeRate;

    @ApiModelProperty("是否生效")
    private Integer status;

}

