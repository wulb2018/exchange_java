package com.wulb2018.settlement.controller;

import com.wulb2018.common.controller.BaseRestController;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.settlement.model.vo.AccountLedgerVO;
import com.wulb2018.settlement.model.dto.AccountLedgerAddDTO;
import com.wulb2018.settlement.model.dto.AccountLedgerUpdateDTO;
import com.wulb2018.settlement.service.AccountLedgerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 账务流水，最重要(t_account_ledger)-控制层
 *
 * @author makejava
 * @since 2026-01-18 18:12:44
 */

@Api(tags = "账务流水，最重要管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("accountLedger")
public class AccountLedgerController extends BaseRestController {

    private final AccountLedgerService accountLedgerService;

    @ApiOperation("查询详情")
    @GetMapping("get/{id}")
    public ApiResponse<AccountLedgerVO> getById(@PathVariable @NotNull @ApiParam("主键id") Long id) {
        return ApiResponse.success(accountLedgerService.getOne(id));
    }

    @ApiOperation("添加账务流水，最重要")
    @PostMapping("add")
    public ApiResponse<Boolean> add(@Valid AccountLedgerAddDTO accountLedgerAddDTO) {
        return ApiResponse.success(accountLedgerService.save(accountLedgerAddDTO));
    }

    @ApiOperation("修改账务流水，最重要")
    @PostMapping("updateById")
    public ApiResponse<Boolean> updateById(@Valid AccountLedgerUpdateDTO accountLedgerUpdateDTO) {
        return ApiResponse.success(accountLedgerService.updateById(accountLedgerUpdateDTO));
    }


}

