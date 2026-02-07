package com.wulb2018.settlement.model.vo;

import com.wulb2018.common.model.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * (t_stock)-展现层实体类
 *
 * @author makejava
 * @since 2026-02-07 14:53:28
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@ApiModel("StockVO")
public class StockVO extends BaseVO<StockVO> {

    @ApiModelProperty("股票名字")
    private String name;

    @ApiModelProperty("股票代码")
    private String code;

    @ApiModelProperty("发行价格")
    private BigDecimal issuePrice;



}

