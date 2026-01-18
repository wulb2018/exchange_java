package com.wulb2018.settlement.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wulb2018.settlement.model.entity.FeeRule;
import com.wulb2018.settlement.model.vo.FeeRuleVO;
import com.wulb2018.settlement.model.dto.FeeRuleDTO;
import com.wulb2018.settlement.model.dto.FeeRuleAddDTO;
import com.wulb2018.settlement.model.dto.FeeRuleUpdateDTO;

import java.util.List;

import org.mapstruct.Mapper;

/**
 * 手续费规则(t_fee_rule)-对象转换器接口
 *
 * @author makejava
 * @since 2026-01-18 19:21:57
 */
@Mapper(componentModel = "spring")
public interface FeeRuleConvert {

    FeeRuleVO toVo(FeeRule feeRule);

    List<FeeRuleVO> toListVo(List<FeeRule> feeRule);

    Page<FeeRuleVO> toPageVo(Page<FeeRule> feeRule);

    FeeRule toEntity(FeeRuleDTO feeRuleDTO);

    FeeRule toEntity(FeeRuleAddDTO feeRuleAddDTO);

    FeeRule toEntity(FeeRuleUpdateDTO feeRuleUpdateDTO);
}

