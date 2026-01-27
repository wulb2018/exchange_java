package com.wulb2018.client.order;

import com.wulb2018.biz.model.dto.OrderCommonDTO;
import com.wulb2018.biz.model.dto.OrderUpdateDTO;
import com.wulb2018.biz.model.dto.OrderBookCommonDTO;
import com.wulb2018.common.model.ApiResponse;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

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
    ApiResponse<List<OrderCommonDTO>> getInitOrderList();

    @RequestMapping("update_order_list")
    ApiResponse<Boolean> updateOrderList(@Valid @RequestBody List<OrderUpdateDTO> orderUpdateDTOList);

    @PostMapping("cancel_order")
    ApiResponse<Boolean> cancelOrder(@ApiParam("撤销订单id") @RequestParam Long id);

    @GetMapping("get_order_book")
    ApiResponse<Map<String, List<OrderBookCommonDTO>>> getOrderBook();
}
