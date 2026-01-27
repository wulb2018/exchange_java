package com.wulb2018.biz.model.vo;

import com.wulb2018.biz.enums.CandlestickType;
import com.wulb2018.common.model.BaseVO;
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
@ApiModel("CandlestickVO")
public class CandlestickVO extends BaseVO<CandlestickVO> {
    @ApiModelProperty("蜡烛类型")
    private CandlestickType candlestickType;
    @ApiModelProperty("k线分类时间")
    private String datetimeCategory;
    @ApiModelProperty("开盘价格")
    private Double openPrice;
    @ApiModelProperty("收盘价格")
    private Double closePrice;
    @ApiModelProperty("最低价格")
    private Double lowestPrice;
    @ApiModelProperty("最高价格")
    private Double highestPrice;
    @ApiModelProperty("交易量")
    private Double volume;

}
