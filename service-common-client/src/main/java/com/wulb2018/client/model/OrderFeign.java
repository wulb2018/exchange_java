package com.wulb2018.client.model;

import com.wulb2018.biz.enums.OrderSide;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wulubin
 * @date 2026/1/15
 * @description TODO
 */
@Data
@Accessors(chain = true)
@ApiModel("OrderFeign")
public class OrderFeign {
    @ApiModelProperty("委托订单ID")
    private Long id;
    @ApiModelProperty("用户ID")
    private Long userId;
    @ApiModelProperty("交易对ID")
    private Long symbolId;
    @ApiModelProperty("方向：1=买，2=卖")
    private OrderSide side;
    @ApiModelProperty("类型：1=限价")
    private Integer type;
    @ApiModelProperty("委托价格")
    private Integer price;
    @ApiModelProperty("委托数量")
    private Integer quantity;
}
