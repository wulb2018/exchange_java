package com.wulb2018.settlement.controller;


import com.wulb2018.common.controller.BaseRestController;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.settlement.model.dto.AccountFrozenLogAddDTO;
import com.wulb2018.settlement.model.dto.AccountFrozenLogUpdateDTO;
import com.wulb2018.settlement.model.vo.AccountFrozenLogVO;
import com.wulb2018.settlement.service.AccountFrozenLogService;
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
 * 账户资金冻结记录(t_account_frozen_log)-控制层
 *
 * @author makejava
 * @since 2026-02-01 18:31:55
 */

@Api(tags = "账户资金冻结记录管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("accountFrozenLog")
public class AccountFrozenLogController extends BaseRestController {

    private final AccountFrozenLogService accountFrozenLogService;



    @ApiOperation("查询详情")
    @GetMapping("get/{id}")
    public ApiResponse<AccountFrozenLogVO> getById(@PathVariable @NotNull @ApiParam("主键id") Long id) {
        return ApiResponse.success(accountFrozenLogService.getOne(id));
    }

    @ApiOperation("添加账户资金冻结记录")
    @PostMapping("add")
    public ApiResponse<Boolean> add(@Valid AccountFrozenLogAddDTO accountFrozenLogAddDTO) {
        return ApiResponse.success(accountFrozenLogService.save(accountFrozenLogAddDTO));
    }

    @ApiOperation("修改账户资金冻结记录")
    @PostMapping("updateById")
    public ApiResponse<Boolean> updateById(@Valid AccountFrozenLogUpdateDTO accountFrozenLogUpdateDTO) {
        return ApiResponse.success(accountFrozenLogService.updateById(accountFrozenLogUpdateDTO));
    }

    @ApiOperation("删除账户资金冻结记录")
    @PostMapping("deleteByIds")
    public ApiResponse<Boolean> deleteByIds(
            @Size(min = 1, max = 20) @ApiParam("id列表（英文逗号分割）") @RequestParam List<Long> idList) {
        return ApiResponse.success(accountFrozenLogService.delete(idList));
    }

}

