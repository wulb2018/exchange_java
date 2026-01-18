package com.wulb2018.controller;

import com.wulb2018.common.controller.BaseRestController;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.biz.model.Trade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wulubin
 * @date 2026/1/17
 * @description TODO
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/trade")
public class TradeController extends BaseRestController {

    @PostMapping("/settlement_trade")
    public ApiResponse<String> settlementTrade(@Valid @RequestBody Trade trade) {
        return ApiResponse.success();
    }

}
