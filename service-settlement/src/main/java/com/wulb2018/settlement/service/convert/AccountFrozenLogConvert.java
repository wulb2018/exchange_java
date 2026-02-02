package com.wulb2018.settlement.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wulb2018.settlement.model.dto.AccountFrozenLogAddDTO;
import com.wulb2018.settlement.model.dto.AccountFrozenLogDTO;
import com.wulb2018.settlement.model.dto.AccountFrozenLogUpdateDTO;
import com.wulb2018.settlement.model.entity.AccountFrozenLog;
import com.wulb2018.settlement.model.vo.AccountFrozenLogVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 账户资金冻结记录(t_account_frozen_log)-对象转换器接口
 *
 * @author makejava
 * @since 2026-02-01 18:31:56
 */
@Mapper(componentModel = "spring")
public interface AccountFrozenLogConvert {

    AccountFrozenLogVO toVo(AccountFrozenLog accountFrozenLog);

    List<AccountFrozenLogVO> toListVo(List<AccountFrozenLog> accountFrozenLog);

    Page<AccountFrozenLogVO> toPageVo(Page<AccountFrozenLog> accountFrozenLog);

    AccountFrozenLog toEntity(AccountFrozenLogDTO accountFrozenLogDTO);

    AccountFrozenLog toEntity(AccountFrozenLogAddDTO accountFrozenLogAddDTO);

    AccountFrozenLog toEntity(AccountFrozenLogUpdateDTO accountFrozenLogUpdateDTO);
}

