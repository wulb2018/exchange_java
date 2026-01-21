package com.wulb2018.client.matching;

import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.client.model.OrderFeign;
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
        name = "service-matching-engine",
        contextId = "MatchingOrderFeignClient",
        path = "/order"
)
public interface MatchingOrderFeignClient {
    @PostMapping("/create")
    ApiResponse<String> create( @Valid @RequestBody OrderFeign orderFeign);

    @PostMapping("/init")
    ApiResponse<Boolean> init();
}
