package com.wulb2018.settlement.controller;


import com.wulb2018.biz.model.vo.CandlestickVO;
import com.wulb2018.common.controller.BaseRestController;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.settlement.model.dto.CandlestickAddDTO;
import com.wulb2018.settlement.model.dto.CandlestickUpdateDTO;
import com.wulb2018.settlement.service.CandlestickService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * (t_candlestick)-控制层
 *
 * @author makejava
 * @since 2026-01-26 20:41:12
 */

@Api(tags = "管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("candlestick")
public class CandlestickController extends BaseRestController {

    private final CandlestickService candlestickService;

    @ApiOperation("查询详情")
    @GetMapping("get/{id}")
    public ApiResponse<CandlestickVO> getById(@PathVariable @NotNull @ApiParam("主键id") Long id) {
        return ApiResponse.success(candlestickService.getOne(id));
    }

    @ApiOperation("添加")
    @PostMapping("add")
    public ApiResponse<Boolean> add(@Valid CandlestickAddDTO candlestickAddDTO) {
        return ApiResponse.success(candlestickService.save(candlestickAddDTO));
    }

    @ApiOperation("修改")
    @PostMapping("updateById")
    public ApiResponse<Boolean> updateById(@Valid CandlestickUpdateDTO candlestickUpdateDTO) {
        return ApiResponse.success(candlestickService.updateById(candlestickUpdateDTO));
    }


}

