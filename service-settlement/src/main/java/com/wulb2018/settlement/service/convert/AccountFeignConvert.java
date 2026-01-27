package com.wulb2018.settlement.service.convert;

import com.wulb2018.biz.model.dto.AccountCommonDTO;
import com.wulb2018.settlement.model.dto.AccountUpdateDTO;
import org.mapstruct.Mapper;

/**
 * @author wulubin
 * @date 2026/1/18
 * @description TODO
 */
@Mapper(componentModel = "spring")
public interface AccountFeignConvert {
    AccountUpdateDTO toAccountUpdateDTO(AccountCommonDTO accountCommonDTO);
}
