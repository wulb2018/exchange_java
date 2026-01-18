package com.wulb2018.settlement.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wulb2018.settlement.model.entity.TradeSymbol;
import com.wulb2018.settlement.model.vo.TradeSymbolVO;
import com.wulb2018.settlement.model.dto.TradeSymbolDTO;
import com.wulb2018.settlement.model.dto.TradeSymbolAddDTO;
import com.wulb2018.settlement.model.dto.TradeSymbolUpdateDTO;

import java.util.List;

import org.mapstruct.Mapper;

/**
 * 交易标的 / 交易对(t_trade_symbol)-对象转换器接口
 *
 * @author makejava
 * @since 2026-01-18 19:47:05
 */
@Mapper(componentModel = "spring")
public interface TradeSymbolConvert {

    TradeSymbolVO toVo(TradeSymbol tradeSymbol);

    List<TradeSymbolVO> toListVo(List<TradeSymbol> tradeSymbol);

    Page<TradeSymbolVO> toPageVo(Page<TradeSymbol> tradeSymbol);

    TradeSymbol toEntity(TradeSymbolDTO tradeSymbolDTO);

    TradeSymbol toEntity(TradeSymbolAddDTO tradeSymbolAddDTO);

    TradeSymbol toEntity(TradeSymbolUpdateDTO tradeSymbolUpdateDTO);
}

