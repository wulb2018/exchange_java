package com.wulb2018.order.model.vo;

import com.wulb2018.biz.enums.OrderSide;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author wulubin
 * @date 2026/1/25
 * @description TODO
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@ApiModel("OrderBookVO")
public class OrderBookVO {
    @ApiModelProperty("订单价格")
    private Double orderPrice;
    @ApiModelProperty("订单总额度")
    private Double amount;
    @ApiModelProperty("累计交易量")
    private Double cumulativeQuantity;
    @ApiModelProperty("方向")
    private OrderSide side;
}
