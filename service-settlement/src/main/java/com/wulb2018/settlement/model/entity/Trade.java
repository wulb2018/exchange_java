package com.wulb2018.settlement.model.entity;

import java.time.LocalDateTime;

import com.wulb2018.common.model.BaseEntity;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;
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
     * 交易对
     */
    private Long symbolId;

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
    private Double quantity;

    /**
     * 成交额
     */
    private Double amount;


}

