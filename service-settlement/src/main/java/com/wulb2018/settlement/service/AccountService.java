package com.wulb2018.settlement.service;

import com.wulb2018.common.service.BaseService;
import com.wulb2018.settlement.mapper.AccountMapper;
import com.wulb2018.settlement.model.dto.AccountAddDTO;
import com.wulb2018.settlement.model.dto.AccountUpdateDTO;
import com.wulb2018.settlement.model.entity.Account;
import com.wulb2018.settlement.model.vo.AccountVO;
import com.wulb2018.settlement.service.convert.AccountConvert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 账户主表(t_account)-业务处理类
 *
 * @author makejava
 * @since 2026-01-18 18:11:50
 */
@Service
@RequiredArgsConstructor
public class AccountService extends BaseService<AccountMapper, Account> {

    private final AccountConvert accountConvert;



    public AccountVO getOne(Serializable id) {
        return accountConvert.toVo(super.getById(id));
    }

    public Boolean save(AccountAddDTO accountAddDTO) {
        return this.save(accountConvert.toEntity(accountAddDTO));
    }

    public Boolean updateById(AccountUpdateDTO accountUpdateDTO) {
        return this.updateById(accountConvert.toEntity(accountUpdateDTO));
    }


}

