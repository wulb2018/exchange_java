package com.wulb2018.client.order;

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
        contextId = "OrderFeignClient"
)
public interface OrderFeignClient {
    @PostMapping("/order/create")
    ApiResponse<String> create( @Valid @RequestBody OrderFeign orderFeign);
}
