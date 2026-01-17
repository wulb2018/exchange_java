package com.wulb2018.controller;

import com.wulb2018.model.ApiResponse;
import com.wulb2018.model.Order;
import com.wulb2018.service.SimpleMatchingService;
import io.swagger.annotations.ApiParam;
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
public class OrderController extends BaseRestController{

    private final SimpleMatchingService simpleMatchingService;

    private long autoId = 0;

    @PostMapping("/create")
    public ApiResponse<String> create(@Valid @RequestBody Order order){
        autoId ++;
        order.setId(autoId);
        simpleMatchingService.addOrder(order);
        simpleMatchingService.testPrintTradeList();
        return ApiResponse.success(simpleMatchingService.getTradeListString());
    }

}
