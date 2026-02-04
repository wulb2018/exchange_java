package com.wulb2018.settlement.service;

import com.wulb2018.common.service.BaseService;
import com.wulb2018.settlement.enums.AccountLedgerBizType;
import com.wulb2018.settlement.mapper.AccountLedgerMapper;
import com.wulb2018.settlement.model.dto.AccountLedgerAddDTO;
import com.wulb2018.settlement.model.dto.AccountLedgerUpdateDTO;
import com.wulb2018.settlement.model.entity.Account;
import com.wulb2018.settlement.model.entity.AccountLedger;
import com.wulb2018.settlement.model.vo.AccountLedgerVO;
import com.wulb2018.settlement.service.convert.AccountLedgerConvert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 账务流水，最重要(t_account_ledger)-业务处理类
 *
 * @author makejava
 * @since 2026-01-18 18:12:44
 */
@Service
@RequiredArgsConstructor
public class AccountLedgerService extends BaseService<AccountLedgerMapper, AccountLedger> {

    private final AccountLedgerConvert accountLedgerConvert;


    public boolean saveTradeBizTypeLedger(Account lastAccount, Long tradeId, Double changeAmount) {
        BigDecimal availableBigDecimal = BigDecimal.valueOf(lastAccount.getAvailable());
        BigDecimal frozenBigDecimal = BigDecimal.valueOf(lastAccount.getFrozen());
        BigDecimal balanceAfterBigDecimal = availableBigDecimal.add(frozenBigDecimal);
        AccountLedger accountLedger = new AccountLedger();
        accountLedger.setAccountId(lastAccount.getId());
        accountLedger.setBizType(AccountLedgerBizType.TRADE);
        accountLedger.setBizId(tradeId);
        accountLedger.setChangeAmount(changeAmount);
        accountLedger.setBalanceAfter(balanceAfterBigDecimal.doubleValue());
        return save(accountLedger);
    }

    public AccountLedgerVO getOne(Serializable id) {
        return accountLedgerConvert.toVo(super.getById(id));
    }

    public Boolean save(AccountLedgerAddDTO accountLedgerAddDTO) {
        return this.save(accountLedgerConvert.toEntity(accountLedgerAddDTO));
    }

    public Boolean updateById(AccountLedgerUpdateDTO accountLedgerUpdateDTO) {
        return this.updateById(accountLedgerConvert.toEntity(accountLedgerUpdateDTO));
    }


}

