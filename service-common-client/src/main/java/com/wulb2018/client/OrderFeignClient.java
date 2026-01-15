package com.wulb2018.client;

import com.wulb2018.model.ApiResponse;
import com.wulb2018.model.Order;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    ApiResponse<String> create( @Valid @RequestBody Order order);
}
