package com.wulb2018.settlement.model.entity;

import com.wulb2018.biz.enums.OrderSide;
import com.wulb2018.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 成交记录(t_trade)-数据表对应实体类
 *
 * @author makejava
 * @since 2026-01-18 14:48:59
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Trade extends BaseEntity<Trade> {

    /**
     * 股票ID
     */
    private Long stockId;

    /**
     * 买单ID
     */
    private Long buyOrderId;

    /**
     * 卖单ID
     */
    private Long sellOrderId;

    /**
     * 买方
     */
    private Long buyUserId;

    /**
     * 卖方
     */
    private Long sellUserId;

    /**
     * 成交价
     */
    private Double price;

    /**
     * 成交数量
     */
    private Integer quantity;

    /**
     * 成交额
     */
    private Double amount;
    /**
     * maker方向，是买方还是卖方
     */
    private OrderSide makerSide;

}

