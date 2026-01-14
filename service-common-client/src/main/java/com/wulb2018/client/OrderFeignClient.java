package com.wulb2018.client;

import com.wulb2018.model.ApiResponse;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wulubin
 * @date 2026/1/14
 * @description TODO
 */
@FeignClient(
        name = "service-matching-engine",
        contextId = "OrderFeignClient"
)
public interface OrderFeignClient {
    @PostMapping("/order/create")
    ApiResponse<String> create(
            @ApiParam("交易对ID") @RequestParam("symbolId") Long symbolId,
            @ApiParam("方向") @RequestParam("side") Integer side,
            @ApiParam("类型") @RequestParam("type") Integer type,
            @ApiParam("委托价格") @RequestParam("price") Integer price,
            @ApiParam("委托数量") @RequestParam("quantity") Integer quantity);
}
