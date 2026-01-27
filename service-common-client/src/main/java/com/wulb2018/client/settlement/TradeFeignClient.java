package com.wulb2018.client.settlement;

import com.wulb2018.biz.enums.CandlestickType;
import com.wulb2018.biz.model.vo.CandlestickVO;
import com.wulb2018.biz.model.dto.TradeFeign;
import com.wulb2018.common.model.ApiResponse;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author wulubin
 * @date 2026/1/14
 * @description TODO
 */
@FeignClient(
        name = "service-settlement",
        contextId = "TradeFeignClient")
public interface TradeFeignClient {
    @PostMapping("/trade/settlement_trade")
    ApiResponse<String> settlementTrade(@Valid @RequestBody TradeFeign tradeFeign);

    @PostMapping("/trade/get_candlestick_init_data")
    ApiResponse<List<CandlestickVO>> getCandlestickInitData(@RequestParam("candlestickType") @NotNull @ApiParam("蜡烛类型") CandlestickType candlestickType);
}
