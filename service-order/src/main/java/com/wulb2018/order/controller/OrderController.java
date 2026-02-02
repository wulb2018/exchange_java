package com.wulb2018.order.controller;


import cn.hutool.core.bean.BeanUtil;
import com.wulb2018.biz.enums.OrderStatus;
import com.wulb2018.biz.model.dto.OrderBookCommonDTO;
import com.wulb2018.biz.model.dto.OrderCommonDTO;
import com.wulb2018.biz.model.dto.OrderUpdateDTO;
import com.wulb2018.common.controller.BaseRestController;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.order.model.dto.OrderAddDTO;
import com.wulb2018.order.model.entity.Order;
import com.wulb2018.order.model.vo.OrderBookVO;
import com.wulb2018.order.model.vo.OrderVO;
import com.wulb2018.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ApiResponse<Boolean> add(@Valid @RequestBody OrderAddDTO orderAddDTO) {
        orderAddDTO.setFilledQuantity(0.);
        orderAddDTO.setFrozenAmount(0.);
        orderAddDTO.setStatus(OrderStatus.NEW);
        return ApiResponse.success(orderService.save(orderAddDTO));
    }

    @ApiOperation("获取初始化订单列表")
    @RequestMapping("get_init_order_list")
    public ApiResponse<List<OrderCommonDTO>> getInitOrderList(){
        return ApiResponse.success(orderService.getInitOrderList());
    }
    @ApiOperation("更新订单")
    @RequestMapping("update_order_list")
    public ApiResponse<Boolean> updateOrderList(@Valid @RequestBody List<OrderUpdateDTO> orderUpdateDTOList) {

        return ApiResponse.success(orderService.updateOrderList(orderUpdateDTOList));
    }

    @ApiOperation("获取k线初始化数据")
    @RequestMapping("get_candlestick_init_data")
    public ApiResponse<String> getCandlestickInitData() {
        return ApiResponse.success(orderService.getCandlestickInitData());
    }

    @ApiOperation("撤销委托订单表")
    @PostMapping("cancel_order")
    public ApiResponse<Boolean> cancelOrder(@ApiParam("撤销订单id") @RequestParam Long id) {
        return ApiResponse.success(orderService.cancelOrder(id));
    }
    @ApiOperation("获取订单簿")
    @GetMapping("get_order_book")
    public ApiResponse<Map<String, List<OrderBookCommonDTO>>> getOrderBook() {
        //订单簿数据
        List<Order> buyOrderList = orderService.buyOrderList();
        List<Order> sellOrderList = orderService.sellOrderList();
        List<OrderBookVO> buyOrderBookList = orderService.buildOrderBookListVO(buyOrderList);
        List<OrderBookVO> sellOrderBookList = orderService.buildOrderBookListVO(sellOrderList);
        Map<String, List<OrderBookCommonDTO>> map = new HashMap<>();
        map.put("buyOrderBookList", BeanUtil.copyToList(buyOrderBookList, OrderBookCommonDTO.class));

        map.put("sellOrderBookList", BeanUtil.copyToList(sellOrderBookList, OrderBookCommonDTO.class));
        return ApiResponse.success(map);
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

