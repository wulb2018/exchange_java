package com.wulb2018.settlement.controller;

import com.wulb2018.common.controller.BaseRestController;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.settlement.model.vo.AccountBalanceVO;
import com.wulb2018.settlement.model.dto.AccountBalanceAddDTO;
import com.wulb2018.settlement.model.dto.AccountBalanceUpdateDTO;
import com.wulb2018.settlement.service.AccountBalanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 余额表(t_account_balance)-控制层
 *
 * @author makejava
 * @since 2026-01-18 18:12:07
 */

@Api(tags = "余额表管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("accountBalance")
public class AccountBalanceController extends BaseRestController {

    private final AccountBalanceService accountBalanceService;


    @ApiOperation("查询详情")
    @GetMapping("get/{id}")
    public ApiResponse<AccountBalanceVO> getById(@PathVariable @NotNull @ApiParam("主键id") Long id) {
        return ApiResponse.success(accountBalanceService.getOne(id));
    }

    @ApiOperation("添加余额表")
    @PostMapping("add")
    public ApiResponse<Boolean> add(@Valid AccountBalanceAddDTO accountBalanceAddDTO) {
        return ApiResponse.success(accountBalanceService.save(accountBalanceAddDTO));
    }

    @ApiOperation("修改余额表")
    @PostMapping("updateById")
    public ApiResponse<Boolean> updateById(@Valid AccountBalanceUpdateDTO accountBalanceUpdateDTO) {
        return ApiResponse.success(accountBalanceService.updateById(accountBalanceUpdateDTO));
    }


}

