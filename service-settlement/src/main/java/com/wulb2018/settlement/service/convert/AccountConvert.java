package com.wulb2018.settlement.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wulb2018.settlement.model.entity.Account;
import com.wulb2018.settlement.model.vo.AccountVO;
import com.wulb2018.settlement.model.dto.AccountDTO;
import com.wulb2018.settlement.model.dto.AccountAddDTO;
import com.wulb2018.settlement.model.dto.AccountUpdateDTO;

import java.util.List;

import org.mapstruct.Mapper;

/**
 * 账户主表(t_account)-对象转换器接口
 *
 * @author makejava
 * @since 2026-01-18 18:11:50
 */
@Mapper(componentModel = "spring")
public interface AccountConvert {

    AccountVO toVo(Account account);

    List<AccountVO> toListVo(List<Account> account);

    Page<AccountVO> toPageVo(Page<Account> account);

    Account toEntity(AccountDTO accountDTO);

    Account toEntity(AccountAddDTO accountAddDTO);

    Account toEntity(AccountUpdateDTO accountUpdateDTO);
}

