package com.wulb2018.settlement.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wulb2018.biz.model.dto.TradeCommonDTO;
import com.wulb2018.biz.model.entity.TradeSymbol;
import com.wulb2018.biz.service.TradeSymbolService;
import com.wulb2018.biz.util.DataPrecisionConvert;
import com.wulb2018.settlement.model.entity.Trade;
import com.wulb2018.settlement.model.vo.TradeVO;
import com.wulb2018.settlement.model.dto.TradeDTO;
import com.wulb2018.settlement.model.dto.TradeAddDTO;
import com.wulb2018.settlement.model.dto.TradeUpdateDTO;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * 成交记录(t_trade)-对象转换器接口
 *
 * @author makejava
 * @since 2026-01-18 14:48:59
 */
@Mapper(
        componentModel = "spring",
        uses = TradeSymbolService.class
)
public interface TradeConvert {

    TradeVO toVo(Trade trade);

    List<TradeVO> toListVo(List<Trade> trade);

    Page<TradeVO> toPageVo(Page<Trade> trade);

    Trade toEntity(TradeDTO tradeDTO);

    Trade toEntity(TradeCommonDTO tradeCommonDTO);

    Trade toEntity(TradeAddDTO tradeAddDTO);

    Trade toEntity(TradeUpdateDTO tradeUpdateDTO);

    @AfterMapping
    default void tradeCommonDTOToEntityPrecisionConvert(TradeCommonDTO tradeCommonDTO, @MappingTarget Trade trade, TradeSymbolService tradeSymbolService) {
        TradeSymbol tradeSymbol = tradeSymbolService.getById(tradeCommonDTO.getSymbolId());
        if (tradeSymbol == null) {
            return;
        }
        trade.setQuantity(DataPrecisionConvert.intToDecimal(tradeCommonDTO.getQuantity(), tradeSymbol.getQuantityPrecision()));
        trade.setPrice(DataPrecisionConvert.intToDecimal(tradeCommonDTO.getPrice(), tradeSymbol.getPricePrecision()));
        trade.setAmount(DataPrecisionConvert.intToDecimal(tradeCommonDTO.getAmount(), tradeSymbol.getPricePrecision()));
    }
}

