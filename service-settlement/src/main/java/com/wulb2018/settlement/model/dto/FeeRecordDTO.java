package com.wulb2018.settlement.model.dto;

import com.wulb2018.biz.enums.RoleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 手续费记录(t_fee_record)-实体类
 *
 * @author makejava
 * @since 2026-02-01 20:15:09
 */
@Data
@Accessors(chain = true)
@ApiModel("FeeRecordDTO")
public class FeeRecordDTO {


    @ApiModelProperty("成交ID")
    private Long tradeId;

    @ApiModelProperty("扣费用户")
    private Long userId;

    @ApiModelProperty("手续费币种")
    private String asset;

    @ApiModelProperty("手续费金额")
    private Double amount;

    @ApiModelProperty("1=Maker，2=Taker")
    private RoleType role;


}

