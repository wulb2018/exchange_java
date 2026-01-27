package com.wulb2018.biz.service;

import com.wulb2018.biz.mapper.TradeSymbolMapper;
import com.wulb2018.biz.model.dto.TradeSymbolAddDTO;
import com.wulb2018.biz.model.dto.TradeSymbolUpdateDTO;
import com.wulb2018.biz.model.entity.TradeSymbol;
import com.wulb2018.biz.model.vo.TradeSymbolVO;
import com.wulb2018.biz.service.convert.TradeSymbolConvert;
import com.wulb2018.common.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 交易标的 / 交易对(t_trade_symbol)-业务处理类
 *
 * @author makejava
 * @since 2026-01-18 19:47:05
 */
@Service
@RequiredArgsConstructor
public class TradeSymbolService extends BaseService<TradeSymbolMapper, TradeSymbol> {

    private final TradeSymbolConvert tradeSymbolConvert;


    public Map<Long, TradeSymbol> getSymbolIdAndTradeSymbolMap(List<Long> symbolIds) {
        List<TradeSymbol> tradeSymbols = listByIds(symbolIds);
        return tradeSymbols.stream().collect(Collectors.toMap(TradeSymbol::getId, o -> o));
    }

    public TradeSymbolVO getOne(Serializable id) {
        return tradeSymbolConvert.toVo(super.getById(id));
    }

    public Boolean save(TradeSymbolAddDTO tradeSymbolAddDTO) {
        return this.save(tradeSymbolConvert.toEntity(tradeSymbolAddDTO));
    }

    public Boolean updateById(TradeSymbolUpdateDTO tradeSymbolUpdateDTO) {
        return this.updateById(tradeSymbolConvert.toEntity(tradeSymbolUpdateDTO));
    }


}

