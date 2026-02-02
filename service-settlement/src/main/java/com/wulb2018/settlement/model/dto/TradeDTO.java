package com.wulb2018.settlement.model.dto;

import com.wulb2018.biz.enums.OrderSide;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 成交记录(t_trade)-实体类
 *
 * @author makejava
 * @since 2026-01-18 14:48:59
 */
@Data
@Accessors(chain = true)
@ApiModel("TradeDTO")
public class TradeDTO {


    @ApiModelProperty("交易对")
    private Long symbolId;

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
    private Double quantity;

    @ApiModelProperty("成交额")
    private Double amount;
    @ApiModelProperty("maker方向，是买方还是卖方")
    private OrderSide makerSide;

}

