package com.wulb2018.client.order;

import com.wulb2018.biz.enums.OrderSide;
import com.wulb2018.client.model.OrderFeign;
import com.wulb2018.common.model.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @author wulubin
 * @date 2026/1/14
 * @description TODO
 */
@FeignClient(
        name = "service-order",
        contextId = "OrderFeignClient",
        path = "/order"
)
public interface OrderFeignClient {

    @RequestMapping("/get_init_order_list")
    ApiResponse<List<OrderFeign>> getInitOrderList();
}
