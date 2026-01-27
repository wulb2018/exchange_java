package com.wulb2018.biz.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 交易标的 / 交易对(t_trade_symbol)-修改实体参数类
 *
 * @author makejava
 * @since 2026-01-18 19:47:05
 */
@Data
@Accessors(chain = true)
@ApiModel("TradeSymbolUpdateDTO")
public class TradeSymbolUpdateDTO {

    @ApiModelProperty("交易对ID")
    private Long id;

    @ApiModelProperty("交易对标识，如 BTC/USDT")
    private String symbol;

    @ApiModelProperty("基础资产（BTC）")
    private String baseAsset;

    @ApiModelProperty("计价资产（USDT）")
    private String quoteAsset;

    @ApiModelProperty("价格精度")
    private Integer pricePrecision;

    @ApiModelProperty("数量精度")
    private Integer quantityPrecision;

    @ApiModelProperty("1=可交易，0=下线")
    private Integer status;


}

