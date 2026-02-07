package com.wulb2018.settlement.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wulb2018.common.controller.BaseRestController;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.settlement.model.dto.UserPositionsAddDTO;
import com.wulb2018.settlement.model.dto.UserPositionsDTO;
import com.wulb2018.settlement.model.dto.UserPositionsUpdateDTO;
import com.wulb2018.settlement.model.vo.UserPositionsVO;
import com.wulb2018.settlement.service.UserPositionsService;
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
 * 用户持仓表(t_user_positions)-控制层
 *
 * @author makejava
 * @since 2026-02-07 16:25:46
 */

@Api(tags = "用户持仓表管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("userPositions")
public class UserPositionsController extends BaseRestController {

    private final UserPositionsService userPositionsService;

    @ApiOperation("查询分页列表")
    @GetMapping("page")
    public ApiResponse<IPage<UserPositionsVO>> page(@Valid UserPositionsDTO userPositionsDTO) {
        return ApiResponse.success(userPositionsService.page(userPositionsDTO));
    }

    @ApiOperation("查询详情")
    @GetMapping("get/{id}")
    public ApiResponse<UserPositionsVO> getById(@PathVariable @NotNull @ApiParam("主键id") Long id) {
        return ApiResponse.success(userPositionsService.getOne(id));
    }

    @ApiOperation("添加用户持仓表")
    @PostMapping("add")
    public ApiResponse<Boolean> add(@Valid UserPositionsAddDTO userPositionsAddDTO) {
        return ApiResponse.success(userPositionsService.save(userPositionsAddDTO));
    }

    @ApiOperation("修改用户持仓表")
    @PostMapping("updateById")
    public ApiResponse<Boolean> updateById(@Valid UserPositionsUpdateDTO userPositionsUpdateDTO) {
        return ApiResponse.success(userPositionsService.updateById(userPositionsUpdateDTO));
    }

    @ApiOperation("删除用户持仓表")
    @PostMapping("deleteByIds")
    public ApiResponse<Boolean> deleteByIds(
            @Size(min = 1, max = 20) @ApiParam("id列表（英文逗号分割）") @RequestParam List<Long> idList) {
        return ApiResponse.success(userPositionsService.delete(idList));
    }

}

