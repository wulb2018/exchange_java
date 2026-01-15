package com.wulb2018.controller;

import com.wulb2018.model.ApiResponse;
import com.wulb2018.model.Order;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author wulubin
 * @date 2026/1/14
 * @description TODO
 */
@RestController
@RequestMapping("/order")
public class OrderController extends BaseRestController{

    @PostMapping("/create")
    public ApiResponse<String> create(@Valid @RequestBody Order order){

        return ApiResponse.success();
    }

}
