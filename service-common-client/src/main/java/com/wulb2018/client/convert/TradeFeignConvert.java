package com.wulb2018.client.convert;

import com.wulb2018.biz.model.dto.TradeCommonDTO;
import com.wulb2018.client.model.TradeFeign;
import org.mapstruct.Mapper;

/**
 * @author wulubin
 * @date 2026/1/18
 * @description TODO
 */
@Mapper(componentModel = "spring")
public interface TradeFeignConvert {

    TradeCommonDTO toOrderCommonDTO(TradeFeign tradeFeign);


    TradeFeign toTradeFeign(TradeCommonDTO tradeCommonDTO);
}
