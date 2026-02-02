package com.wulb2018.settlement.service;


import com.wulb2018.common.service.BaseService;
import com.wulb2018.settlement.mapper.AccountFrozenLogMapper;
import com.wulb2018.settlement.model.dto.AccountFrozenLogAddDTO;
import com.wulb2018.settlement.model.dto.AccountFrozenLogUpdateDTO;
import com.wulb2018.settlement.model.entity.AccountFrozenLog;
import com.wulb2018.settlement.model.vo.AccountFrozenLogVO;
import com.wulb2018.settlement.service.convert.AccountFrozenLogConvert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 账户资金冻结记录(t_account_frozen_log)-业务处理类
 *
 * @author makejava
 * @since 2026-02-01 18:31:56
 */
@Service
@RequiredArgsConstructor
public class AccountFrozenLogService extends BaseService<AccountFrozenLogMapper, AccountFrozenLog> {

    private final AccountFrozenLogConvert accountFrozenLogConvert;


    public AccountFrozenLogVO getOne(Serializable id) {
        return accountFrozenLogConvert.toVo(super.getById(id));
    }


    public boolean save(Long accountId, Long orderId, Double frozen) {
        AccountFrozenLog accountFrozenLog = new AccountFrozenLog();
        accountFrozenLog.setAccountId(accountId);
        accountFrozenLog.setOrderId(orderId);
        accountFrozenLog.setFrozen(frozen);
        return this.save(accountFrozenLog);
    }

    public Boolean save(AccountFrozenLogAddDTO accountFrozenLogAddDTO) {
        return this.save(accountFrozenLogConvert.toEntity(accountFrozenLogAddDTO));
    }

    public Boolean updateById(AccountFrozenLogUpdateDTO accountFrozenLogUpdateDTO) {
        return this.updateById(accountFrozenLogConvert.toEntity(accountFrozenLogUpdateDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

