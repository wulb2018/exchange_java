package com.wulb2018.settlement.model.vo;

import com.wulb2018.common.model.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 手续费规则(t_fee_rule)-展现层实体类
 *
 * @author makejava
 * @since 2026-01-18 19:21:57
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@ApiModel("FeeRuleVO")
public class FeeRuleVO extends BaseVO<FeeRuleVO> {

    @ApiModelProperty("交易对")
    private Long symbolId;

    @ApiModelProperty("Maker费率")
    private Double makerFeeRate;

    @ApiModelProperty("Taker费率")
    private Double takerFeeRate;

    @ApiModelProperty("是否生效")
    private Integer status;


}

