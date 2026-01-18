package com.wulb2018.order.controller;


import com.wulb2018.biz.enums.OrderSide;
import com.wulb2018.common.controller.BaseRestController;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.order.model.vo.OrderVO;
import com.wulb2018.order.model.dto.OrderAddDTO;
import com.wulb2018.order.model.dto.OrderUpdateDTO;
import com.wulb2018.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 委托订单表(t_order)-控制层
 *
 * @author makejava
 * @since 2026-01-12 22:30:53
 */

@Api(tags = "委托订单表管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("order")
public class OrderController extends BaseRestController {

    private final OrderService orderService;

    @ApiOperation("查询详情")
    @GetMapping("get/{id}")
    public ApiResponse<OrderVO> getById(@PathVariable @NotNull @ApiParam("主键id") Long id) {
        return ApiResponse.success(orderService.getOne(id));
    }

    @ApiOperation("添加委托订单表")
    @RequestMapping("add")
    public ApiResponse<Boolean> add(@Valid @RequestBody OrderAddDTO orderAddDTO) {//@Valid OrderAddDTO orderAddDTO
        System.out.println("wwwwwwwwwwwwwwwwwwwww");
        if (OrderSide.SELL.equals(orderAddDTO.getSide())) {
            orderAddDTO.setUserId(1L);
        } else {
            orderAddDTO.setUserId(2L);
        }
        orderAddDTO.setFilledQuantity(0.);
        orderAddDTO.setFrozenAmount(0.);
        orderAddDTO.setStatus(0);
        return ApiResponse.success(orderService.save(orderAddDTO));
    }

    @ApiOperation("修改委托订单表")
    @PostMapping("updateById")
    public ApiResponse<Boolean> updateById(@Valid OrderUpdateDTO orderUpdateDTO) {
        return ApiResponse.success(orderService.updateById(orderUpdateDTO));
    }

    @ApiOperation("删除委托订单表")
    @PostMapping("deleteByIds")
    public ApiResponse<Boolean> deleteByIds(
            @Size(min = 1, max = 20) @ApiParam("id列表（英文逗号分割）") @RequestParam List<Long> idList) {
        return ApiResponse.success(orderService.delete(idList));
    }

}

