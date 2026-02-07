package com.wulb2018.settlement.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 成交记录(t_trade)-修改实体参数类
 *
 * @author makejava
 * @since 2026-01-18 14:49:00
 */
@Data
@Accessors(chain = true)
@ApiModel("TradeUpdateDTO")
public class TradeUpdateDTO {

    @ApiModelProperty("成交ID")
    private Long id;

    @ApiModelProperty("股票id")
    private Long stockId;

    @ApiModelProperty("买单ID")
    private Long buyOrderId;

    @ApiModelProperty("卖单ID")
    private Long sellOrderId;

    @ApiModelProperty("买方")
    private Long buyUserId;

    @ApiModelProperty("卖方")
    private Long sellUserId;

    @ApiModelProperty("成交价")
    private Double price;

    @ApiModelProperty("成交数量")
    private Integer quantity;

    @ApiModelProperty("成交额")
    private Double amount;


}

