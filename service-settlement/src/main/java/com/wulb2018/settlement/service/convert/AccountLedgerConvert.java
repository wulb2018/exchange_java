package com.wulb2018.settlement.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wulb2018.settlement.model.entity.AccountLedger;
import com.wulb2018.settlement.model.vo.AccountLedgerVO;
import com.wulb2018.settlement.model.dto.AccountLedgerDTO;
import com.wulb2018.settlement.model.dto.AccountLedgerAddDTO;
import com.wulb2018.settlement.model.dto.AccountLedgerUpdateDTO;

import java.util.List;

import org.mapstruct.Mapper;

/**
 * 账务流水，最重要(t_account_ledger)-对象转换器接口
 *
 * @author makejava
 * @since 2026-01-18 18:12:44
 */
@Mapper(componentModel = "spring")
public interface AccountLedgerConvert {

    AccountLedgerVO toVo(AccountLedger accountLedger);

    List<AccountLedgerVO> toListVo(List<AccountLedger> accountLedger);

    Page<AccountLedgerVO> toPageVo(Page<AccountLedger> accountLedger);

    AccountLedger toEntity(AccountLedgerDTO accountLedgerDTO);

    AccountLedger toEntity(AccountLedgerAddDTO accountLedgerAddDTO);

    AccountLedger toEntity(AccountLedgerUpdateDTO accountLedgerUpdateDTO);
}

