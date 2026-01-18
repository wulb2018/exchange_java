package com.wulb2018.client.settlement;

import com.wulb2018.client.model.TradeFeign;
import com.wulb2018.common.model.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author wulubin
 * @date 2026/1/14
 * @description TODO
 */
@FeignClient(
        name = "service-settlement",
        contextId = "TradeFeignClient"
)
public interface TradeFeignClient {
    @PostMapping("/trade/settlement_trade")
    ApiResponse<String> create(@Valid @RequestBody TradeFeign tradeFeign);
}
