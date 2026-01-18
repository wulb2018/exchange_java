package com.wulb2018.settlement.service;


import com.wulb2018.common.service.BaseService;
import com.wulb2018.settlement.mapper.FeeRuleMapper;
import com.wulb2018.settlement.model.dto.FeeRuleAddDTO;
import com.wulb2018.settlement.model.dto.FeeRuleUpdateDTO;
import com.wulb2018.settlement.model.entity.FeeRule;
import com.wulb2018.settlement.model.vo.FeeRuleVO;
import com.wulb2018.settlement.service.convert.FeeRuleConvert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 手续费规则(t_fee_rule)-业务处理类
 *
 * @author makejava
 * @since 2026-01-18 19:21:58
 */
@Service
@RequiredArgsConstructor
public class FeeRuleService extends BaseService<FeeRuleMapper, FeeRule> {

    private final FeeRuleConvert feeRuleConvert;

    public FeeRule getOneBySymbolId(Long symbolId) {
        return lambdaQuery().eq(FeeRule::getSymbolId, symbolId).one();
    }

    public FeeRuleVO getOne(Serializable id) {
        return feeRuleConvert.toVo(super.getById(id));
    }

    public Boolean save(FeeRuleAddDTO feeRuleAddDTO) {
        return this.save(feeRuleConvert.toEntity(feeRuleAddDTO));
    }

    public Boolean updateById(FeeRuleUpdateDTO feeRuleUpdateDTO) {
        return this.updateById(feeRuleConvert.toEntity(feeRuleUpdateDTO));
    }

}

