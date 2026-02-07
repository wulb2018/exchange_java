package com.wulb2018.settlement.model.entity;

import com.wulb2018.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户持仓表(t_user_positions)-数据表对应实体类
 *
 * @author makejava
 * @since 2026-02-07 16:25:46
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserPositions extends BaseEntity<UserPositions> {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 股票id
     */
    private Long stockId;

    /**
     * 可用数量（未冻结）
     */
    private Integer availableQuantity;

    /**
     * 冻结数量（挂单卖出未成交）
     */
    private Integer frozenQuantity;

    /**
     * 平均成本
     */
    private Object averageCost;

    /**
     * 已实现盈亏
     */
    private Object realizedPnl;

    /**
     * 首次买入时间
     */
    private LocalDateTime firstBuyDate;

    /**
     * 最后买入时间
     */
    private LocalDateTime lastBuyDate;

    /**
     * 最后卖出时间
     */
    private LocalDateTime lastSellDate;
}

