package com.wulb2018.settlement.model.dto;

import com.wulb2018.biz.enums.CandlestickType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * (t_candlestick)-修改实体参数类
 *
 * @author makejava
 * @since 2026-01-26 20:41:12
 */
@Data
@Accessors(chain = true)
@ApiModel("CandlestickUpdateDTO")
public class CandlestickUpdateDTO {

    private Long id;

    @ApiModelProperty("蜡烛类型，类目类型")
    private CandlestickType candlestickType;

    @ApiModelProperty("时间类目")
    private String datetimeCategory;

    @ApiModelProperty("开盘价")
    private Double openPrice;

    @ApiModelProperty("收盘价")
    private Double closePrice;

    @ApiModelProperty("最低价")
    private Double lowestPrice;

    @ApiModelProperty("最高价")
    private Double highestPrice;

}

