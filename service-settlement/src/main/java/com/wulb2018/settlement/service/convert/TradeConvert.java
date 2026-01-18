package com.wulb2018.settlement.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wulb2018.biz.model.dto.TradeCommonDTO;
import com.wulb2018.settlement.model.entity.Trade;
import com.wulb2018.settlement.model.vo.TradeVO;
import com.wulb2018.settlement.model.dto.TradeDTO;
import com.wulb2018.settlement.model.dto.TradeAddDTO;
import com.wulb2018.settlement.model.dto.TradeUpdateDTO;

import java.util.List;

import org.mapstruct.Mapper;

/**
 * 成交记录(t_trade)-对象转换器接口
 *
 * @author makejava
 * @since 2026-01-18 14:48:59
 */
@Mapper(componentModel = "spring")
public interface TradeConvert {

    TradeVO toVo(Trade trade);

    List<TradeVO> toListVo(List<Trade> trade);

    Page<TradeVO> toPageVo(Page<Trade> trade);

    Trade toEntity(TradeDTO tradeDTO);

    Trade toEntity(TradeCommonDTO tradeCommonDTO);

    Trade toEntity(TradeAddDTO tradeAddDTO);

    Trade toEntity(TradeUpdateDTO tradeUpdateDTO);
}

