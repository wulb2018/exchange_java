package com.wulb2018.settlement.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * 用户持仓表(t_user_positions)-实体类
 *
 * @author makejava
 * @since 2026-02-07 16:25:47
 */
@Data
@Accessors(chain = true)
@ApiModel("UserPositionsDTO")
public class UserPositionsDTO {


    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("股票id")
    private Long stockId;

    @ApiModelProperty("可用数量（未冻结）")
    private Integer availableQuantity;

    @ApiModelProperty("冻结数量（挂单卖出未成交）")
    private Integer frozenQuantity;

    @ApiModelProperty("平均成本")
    private Object averageCost;

    @ApiModelProperty("已实现盈亏")
    private Object realizedPnl;

    @ApiModelProperty("首次买入时间")
    private LocalDateTime firstBuyDate;

    @ApiModelProperty("最后买入时间")
    private LocalDateTime lastBuyDate;

    @ApiModelProperty("最后卖出时间")
    private LocalDateTime lastSellDate;

}

