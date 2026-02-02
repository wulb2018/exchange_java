package com.wulb2018.settlement.controller;


import com.wulb2018.common.controller.BaseRestController;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.settlement.model.dto.FeeRecordAddDTO;
import com.wulb2018.settlement.model.dto.FeeRecordUpdateDTO;
import com.wulb2018.settlement.model.vo.FeeRecordVO;
import com.wulb2018.settlement.service.FeeRecordService;
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
 * 手续费记录(t_fee_record)-控制层
 *
 * @author makejava
 * @since 2026-02-01 20:15:08
 */

@Api(tags = "手续费记录管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("feeRecord")
public class FeeRecordController extends BaseRestController {

    private final FeeRecordService feeRecordService;


    @ApiOperation("查询详情")
    @GetMapping("get/{id}")
    public ApiResponse<FeeRecordVO> getById(@PathVariable @NotNull @ApiParam("主键id") Long id) {
        return ApiResponse.success(feeRecordService.getOne(id));
    }

    @ApiOperation("添加手续费记录")
    @PostMapping("add")
    public ApiResponse<Boolean> add(@Valid FeeRecordAddDTO feeRecordAddDTO) {
        return ApiResponse.success(feeRecordService.save(feeRecordAddDTO));
    }

    @ApiOperation("修改手续费记录")
    @PostMapping("updateById")
    public ApiResponse<Boolean> updateById(@Valid FeeRecordUpdateDTO feeRecordUpdateDTO) {
        return ApiResponse.success(feeRecordService.updateById(feeRecordUpdateDTO));
    }

    @ApiOperation("删除手续费记录")
    @PostMapping("deleteByIds")
    public ApiResponse<Boolean> deleteByIds(
            @Size(min = 1, max = 20) @ApiParam("id列表（英文逗号分割）") @RequestParam List<Long> idList) {
        return ApiResponse.success(feeRecordService.delete(idList));
    }

}

