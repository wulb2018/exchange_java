package com.wulb2018.settlement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wulb2018.settlement.model.entity.UserPositions;

import java.time.LocalDateTime;

/**
 * 用户持仓表(t_user_positions)-数据库访问层
 *
 * @author makejava
 * @since 2026-02-07 16:25:47
 */
public interface UserPositionsMapper extends BaseMapper<UserPositions> {
    int settlementBuyUserPositions(Integer quantity, LocalDateTime modifyDate, Long userId, Long stockId);

    int settlementSellUserPositions(Integer quantity, LocalDateTime modifyDate, Long userId, Long stockId);

    int frozenSell(Integer quantity, LocalDateTime modifyDate, Long userId, Long stockId);
}

