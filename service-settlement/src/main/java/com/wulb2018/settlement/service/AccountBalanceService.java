package com.wulb2018.settlement.service;


import com.wulb2018.common.service.BaseService;
import com.wulb2018.settlement.mapper.AccountBalanceMapper;
import com.wulb2018.settlement.model.dto.AccountBalanceAddDTO;
import com.wulb2018.settlement.model.dto.AccountBalanceUpdateDTO;
import com.wulb2018.settlement.model.entity.AccountBalance;
import com.wulb2018.settlement.model.vo.AccountBalanceVO;
import com.wulb2018.settlement.service.convert.AccountBalanceConvert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 余额表(t_account_balance)-业务处理类
 *
 * @author makejava
 * @since 2026-01-18 18:12:08
 */
@Service
@RequiredArgsConstructor
public class AccountBalanceService extends BaseService<AccountBalanceMapper, AccountBalance> {

    private final AccountBalanceConvert accountBalanceConvert;


    public AccountBalanceVO getOne(Serializable id) {
        return accountBalanceConvert.toVo(super.getById(id));
    }

    public Boolean save(AccountBalanceAddDTO accountBalanceAddDTO) {
        return this.save(accountBalanceConvert.toEntity(accountBalanceAddDTO));
    }

    public Boolean updateById(AccountBalanceUpdateDTO accountBalanceUpdateDTO) {
        return this.updateById(accountBalanceConvert.toEntity(accountBalanceUpdateDTO));
    }


}

