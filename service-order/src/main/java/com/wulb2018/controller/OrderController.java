package com.wulb2018.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wulb2018.model.ApiResponse;
import com.wulb2018.model.vo.OrderVO;
import com.wulb2018.model.dto.OrderDTO;
import com.wulb2018.model.dto.OrderAddDTO;
import com.wulb2018.model.dto.OrderUpdateDTO;
import com.wulb2018.service.OrderService;
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
    @PostMapping("add")
    public ApiResponse<Boolean> add(@Valid OrderAddDTO orderAddDTO) {
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

