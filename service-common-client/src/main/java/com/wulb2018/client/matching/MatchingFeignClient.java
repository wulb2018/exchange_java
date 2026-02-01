package com.wulb2018.client.matching;

import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.biz.model.dto.OrderCommonDTO;
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
        contextId = "MatchingFeignClient",
        path = "/matching"
)
public interface MatchingFeignClient {
    @PostMapping("/add_order")
    ApiResponse<String> addOrder(@Valid @RequestBody OrderCommonDTO orderCommonDTO);

    @PostMapping("/init")
    ApiResponse<Boolean> init();
}
