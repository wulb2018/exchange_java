package com.wulb2018.settlement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wulb2018.settlement.model.entity.Account;

import java.time.LocalDateTime;

/**
 * 账户主表(t_account)-数据库访问层
 *
 * @author makejava
 * @since 2026-01-18 18:11:50
 */
public interface AccountMapper extends BaseMapper<Account> {
    int frozenAsset(Double frozen, LocalDateTime modifyDate, Long userId);

    int unfrozenAsset(Double amount, LocalDateTime modifyDate, Long userId, String asset);

    int settlementBuyAccount(Double account, LocalDateTime modifyDate, Long userId);

    int settlementSellAccount(Double account, LocalDateTime modifyDate, Long userId);
}

