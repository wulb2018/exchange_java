package com.wulb2018.biz.model.dto;

import com.wulb2018.biz.enums.OrderSide;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wulubin
 * @date 2026/1/26
 * @description TODO
 */
@Data
@Accessors(chain = true)
@ApiModel("OrderBookFeignDTO")
public class OrderBookCommonDTO {

    @ApiModelProperty("订单价格")
    private Double orderPrice;
    @ApiModelProperty("订单总额度")
    private Double amount;
    @ApiModelProperty("累计交易量")
    private Double cumulativeQuantity;
    @ApiModelProperty("方向")
    private OrderSide side;
}
