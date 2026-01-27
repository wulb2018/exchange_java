package com.wulb2018.engine.controller;

import com.wulb2018.common.controller.BaseRestController;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.biz.model.dto.OrderCommonDTO;
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

    @PostMapping("/create")
    public ApiResponse<String> create(@Valid @RequestBody OrderCommonDTO orderCommonDTO){
        OrderDTO orderDTO = orderFeignConvert.toOrderDTO(orderCommonDTO);
        simpleMatchingService.addOrder(orderDTO);
        //simpleMatchingService.testPrintTradeList();
        return ApiResponse.success(simpleMatchingService.getTradeListString());
    }
    @PostMapping("/init")
    public ApiResponse<Boolean> init() {
        simpleMatchingService.initOrderListMap();
        return ApiResponse.success(true);
    }

}
