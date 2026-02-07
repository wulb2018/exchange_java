package com.wulb2018.settlement.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


/**
 * (t_stock)-添加实体参数类
 *
 * @author makejava
 * @since 2026-02-07 14:53:28
 */
@Data
@Accessors(chain = true)
@ApiModel("StockAddDTO")
public class StockAddDTO {


    @ApiModelProperty("股票名字")
    private String name;

    @ApiModelProperty("股票代码")
    private String code;

    @ApiModelProperty("发行价格")
    private BigDecimal issuePrice;

}

