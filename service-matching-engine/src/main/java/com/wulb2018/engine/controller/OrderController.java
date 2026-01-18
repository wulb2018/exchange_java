package com.wulb2018.engine.controller;

import com.wulb2018.common.controller.BaseRestController;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.client.model.OrderFeign;
import com.wulb2018.engine.model.dto.OrderDTO;
import com.wulb2018.engine.service.SimpleMatchingService;
import com.wulb2018.engine.service.convert.OrderFeignConvert;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author wulubin
 * @date 2026/1/14
 * @description TODO
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController extends BaseRestController {

    private final SimpleMatchingService simpleMatchingService;
    private final OrderFeignConvert orderFeignConvert;

    private long autoId = 0;

    @PostMapping("/create")
    public ApiResponse<String> create(@Valid @RequestBody OrderFeign orderFeign){
//        autoId ++;
//        order.setId(autoId);
        OrderDTO orderDTO = orderFeignConvert.toOrderDTO(orderFeign);
        simpleMatchingService.addOrder(orderDTO);
        simpleMatchingService.testPrintTradeList();
        return ApiResponse.success(simpleMatchingService.getTradeListString());
    }

}
