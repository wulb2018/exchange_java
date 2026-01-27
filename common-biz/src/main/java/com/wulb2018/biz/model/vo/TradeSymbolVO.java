package com.wulb2018.biz.model.vo;


import com.wulb2018.common.model.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 交易标的 / 交易对(t_trade_symbol)-展现层实体类
 *
 * @author makejava
 * @since 2026-01-18 19:47:05
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@ApiModel("TradeSymbolVO")
public class TradeSymbolVO extends BaseVO<TradeSymbolVO> {

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

