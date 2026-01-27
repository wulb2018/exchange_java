package com.wulb2018.biz.model.entity;

import com.wulb2018.common.model.BaseEntity;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 交易标的 / 交易对(t_trade_symbol)-数据表对应实体类
 *
 * @author makejava
 * @since 2026-01-18 19:47:05
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TradeSymbol extends BaseEntity<TradeSymbol> {

    /**
     * 交易对标识，如 BTC/USDT
     */
    private String symbol;

    /**
     * 基础资产（BTC）
     */
    private String baseAsset;

    /**
     * 计价资产（USDT）
     */
    private String quoteAsset;

    /**
     * 价格精度
     */
    private Integer pricePrecision;

    /**
     * 数量精度
     */
    private Integer quantityPrecision;

    /**
     * 1=可交易，0=下线
     */
    private Integer status;


}

