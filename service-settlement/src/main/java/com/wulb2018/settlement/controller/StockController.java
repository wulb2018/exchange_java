package com.wulb2018.settlement.controller;


import com.wulb2018.common.controller.BaseRestController;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.biz.model.dto.StockAddDTO;
import com.wulb2018.biz.model.dto.StockUpdateDTO;
import com.wulb2018.biz.model.vo.StockVO;
import com.wulb2018.biz.service.StockService;
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
 * (t_stock)-控制层
 *
 * @author makejava
 * @since 2026-02-07 14:53:28
 */

@Api(tags = "管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("stock")
public class StockController extends BaseRestController {

    private final StockService stockService;


    @ApiOperation("查询详情")
    @GetMapping("get/{id}")
    public ApiResponse<StockVO> getById(@PathVariable @NotNull @ApiParam("主键id") Long id) {
        return ApiResponse.success(stockService.getOne(id));
    }

    @ApiOperation("添加")
    @PostMapping("add")
    public ApiResponse<Boolean> add(@Valid StockAddDTO stockAddDTO) {
        return ApiResponse.success(stockService.save(stockAddDTO));
    }

    @ApiOperation("修改")
    @PostMapping("updateById")
    public ApiResponse<Boolean> updateById(@Valid StockUpdateDTO stockUpdateDTO) {
        return ApiResponse.success(stockService.updateById(stockUpdateDTO));
    }

    @ApiOperation("删除")
    @PostMapping("deleteByIds")
    public ApiResponse<Boolean> deleteByIds(
            @Size(min = 1, max = 20) @ApiParam("id列表（英文逗号分割）") @RequestParam List<Long> idList) {
        return ApiResponse.success(stockService.delete(idList));
    }

}

