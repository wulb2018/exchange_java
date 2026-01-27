package com.wulb2018.biz.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wulubin
 * @date 2026/1/17
 * @description TODO
 */
@Data
@Accessors(chain = true)
@ApiModel("TradeFeign")
public class TradeFeign {
    @ApiModelProperty("交易对ID")
    private Long symbolId;
    @ApiModelProperty("买方订单ID")
    private Long buyOrderId;
    @ApiModelProperty("卖方订单ID")
    private Long sellOrderId;
    @ApiModelProperty("买方用户ID")
    private Long buyUserId;
    @ApiModelProperty("卖方用户ID")
    private Long sellUserId;
    @ApiModelProperty("成交价格")
    private Integer price;
    @ApiModelProperty("成交量")
    private Integer quantity;
    @ApiModelProperty("成交金额")
    private Integer amount;
}
