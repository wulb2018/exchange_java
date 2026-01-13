package com.wulb2018.model.dto;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 委托订单表(t_order)-添加实体参数类
 *
 * @author makejava
 * @since 2026-01-12 22:30:54
 */
@Data
@Accessors(chain = true)
@ApiModel("OrderAddDTO")
public class OrderAddDTO {


    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("交易对ID")
    private Long symbolId;

    @ApiModelProperty("方向：1=买，2=卖")
    private Integer side;

    @ApiModelProperty("类型：1=限价")
    private Integer type;

    @ApiModelProperty("委托价格")
    private Double price;

    @ApiModelProperty("委托数量")
    private Double quantity;

    @ApiModelProperty("已成交数量")
    private Double filledQuantity;

    @ApiModelProperty("冻结资金/资产")
    private Double frozenAmount;

    @ApiModelProperty("0=新建，1=部分成交，2=全部成交，3=已取消")
    private Integer status;

    @ApiModelProperty("下单时间")
    private LocalDateTime createdAt;

    @ApiModelProperty("状态更新时间")
    private LocalDateTime updatedAt;

}

