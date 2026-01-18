package com.wulb2018.settlement.service;


import com.wulb2018.biz.model.dto.TradeCommonDTO;
import com.wulb2018.common.service.BaseService;
import com.wulb2018.settlement.mapper.TradeMapper;
import com.wulb2018.settlement.model.dto.TradeAddDTO;
import com.wulb2018.settlement.model.dto.TradeUpdateDTO;
import com.wulb2018.settlement.model.entity.Trade;
import com.wulb2018.settlement.model.vo.TradeVO;
import com.wulb2018.settlement.service.convert.TradeConvert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 成交记录(t_trade)-业务处理类
 *
 * @author makejava
 * @since 2026-01-18 14:49:00
 */
@Service
@RequiredArgsConstructor
public class TradeService extends BaseService<TradeMapper, Trade> {

    private final TradeConvert tradeConvert;

    public void settlementTrade(TradeCommonDTO tradeCommonDTO){
        Trade entity = tradeConvert.toEntity(tradeCommonDTO);
        save(entity);
    }

    public TradeVO getOne(Serializable id) {
        return tradeConvert.toVo(super.getById(id));
    }

    public Boolean save(TradeAddDTO tradeAddDTO) {
        return this.save(tradeConvert.toEntity(tradeAddDTO));
    }

    public Boolean updateById(TradeUpdateDTO tradeUpdateDTO) {
        return this.updateById(tradeConvert.toEntity(tradeUpdateDTO));
    }

}

