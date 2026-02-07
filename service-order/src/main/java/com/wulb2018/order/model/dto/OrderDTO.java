package com.wulb2018.order.model.dto;

import com.wulb2018.biz.enums.OrderSide;
import com.wulb2018.biz.enums.OrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 委托订单表(t_order)-实体类
 *
 * @author makejava
 * @since 2026-01-12 22:30:54
 */
@Data
@Accessors(chain = true)
@ApiModel("OrderDTO")
public class OrderDTO {


    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("股票ID")
    private Long stockId;

    @ApiModelProperty("方向：1=买，2=卖")
    private OrderSide side;

    @ApiModelProperty("类型：1=限价")
    private Integer type;

    @ApiModelProperty("委托价格")
    private Double price;

    @ApiModelProperty("委托数量")
    private Integer quantity;

    @ApiModelProperty("已成交数量")
    private Integer filledQuantity;

    @ApiModelProperty("冻结资金/资产")
    private Double frozenAmount;

    @ApiModelProperty("0=新建，1=部分成交，2=全部成交，3=已取消")
    private OrderStatus status;

}

