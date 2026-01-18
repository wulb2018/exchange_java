package com.wulb2018.settlement.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wulb2018.settlement.model.entity.AccountBalance;
import com.wulb2018.settlement.model.vo.AccountBalanceVO;
import com.wulb2018.settlement.model.dto.AccountBalanceDTO;
import com.wulb2018.settlement.model.dto.AccountBalanceAddDTO;
import com.wulb2018.settlement.model.dto.AccountBalanceUpdateDTO;

import java.util.List;

import org.mapstruct.Mapper;

/**
 * 余额表(t_account_balance)-对象转换器接口
 *
 * @author makejava
 * @since 2026-01-18 18:12:07
 */
@Mapper(componentModel = "spring")
public interface AccountBalanceConvert {

    AccountBalanceVO toVo(AccountBalance accountBalance);

    List<AccountBalanceVO> toListVo(List<AccountBalance> accountBalance);

    Page<AccountBalanceVO> toPageVo(Page<AccountBalance> accountBalance);

    AccountBalance toEntity(AccountBalanceDTO accountBalanceDTO);

    AccountBalance toEntity(AccountBalanceAddDTO accountBalanceAddDTO);

    AccountBalance toEntity(AccountBalanceUpdateDTO accountBalanceUpdateDTO);
}

