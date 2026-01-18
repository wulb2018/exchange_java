package com.wulb2018.settlement.controller;

import com.wulb2018.common.controller.BaseRestController;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.settlement.model.vo.FeeRuleVO;
import com.wulb2018.settlement.model.dto.FeeRuleAddDTO;
import com.wulb2018.settlement.model.dto.FeeRuleUpdateDTO;
import com.wulb2018.settlement.service.FeeRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 手续费规则(t_fee_rule)-控制层
 *
 * @author makejava
 * @since 2026-01-18 19:21:57
 */

@Api(tags = "手续费规则管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("feeRule")
public class FeeRuleController extends BaseRestController {

    private final FeeRuleService feeRuleService;


    @ApiOperation("查询详情")
    @GetMapping("get/{id}")
    public ApiResponse<FeeRuleVO> getById(@PathVariable @NotNull @ApiParam("主键id") Long id) {
        return ApiResponse.success(feeRuleService.getOne(id));
    }

    @ApiOperation("添加手续费规则")
    @PostMapping("add")
    public ApiResponse<Boolean> add(@Valid FeeRuleAddDTO feeRuleAddDTO) {
        return ApiResponse.success(feeRuleService.save(feeRuleAddDTO));
    }

    @ApiOperation("修改手续费规则")
    @PostMapping("updateById")
    public ApiResponse<Boolean> updateById(@Valid FeeRuleUpdateDTO feeRuleUpdateDTO) {
        return ApiResponse.success(feeRuleService.updateById(feeRuleUpdateDTO));
    }


}

