package com.wulb2018.settlement.controller;


import com.wulb2018.biz.enums.OrderSide;
import com.wulb2018.biz.model.dto.AccountCommonDTO;
import com.wulb2018.common.controller.BaseRestController;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.settlement.model.dto.AccountAddDTO;
import com.wulb2018.settlement.model.dto.AccountUpdateDTO;
import com.wulb2018.settlement.model.vo.AccountVO;
import com.wulb2018.settlement.service.AccountService;
import com.wulb2018.settlement.service.UserPositionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 账户主表(t_account)-控制层
 *
 * @author makejava
 * @since 2026-01-18 18:11:49
 */

@Api(tags = "账户主表管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("account")
public class AccountController extends BaseRestController {

    private final AccountService accountService;
    private final UserPositionsService userPositionsService;


    @ApiOperation("添加账户主表")
    @PostMapping("frozen_asset")
    public ApiResponse<Boolean> frozenAsset(@Valid @RequestBody AccountCommonDTO accountCommonDTO) {
        boolean ret;
        if (OrderSide.BUY.equals(accountCommonDTO.getSide())) {
            ret = accountService.frozenBuy(accountCommonDTO);
        } else {
            ret = userPositionsService.frozenSell(accountCommonDTO);
        }
        return ApiResponse.success(ret);
    }

    @ApiOperation("查询详情")
    @GetMapping("get/{id}")
    public ApiResponse<AccountVO> getById(@PathVariable @NotNull @ApiParam("主键id") Long id) {
        return ApiResponse.success(accountService.getOne(id));
    }

    @ApiOperation("添加账户主表")
    @PostMapping("add")
    public ApiResponse<Boolean> add(@Valid AccountAddDTO accountAddDTO) {
        return ApiResponse.success(accountService.save(accountAddDTO));
    }

    @ApiOperation("修改账户主表")
    @PostMapping("updateById")
    public ApiResponse<Boolean> updateById(@Valid AccountUpdateDTO accountUpdateDTO) {
        return ApiResponse.success(accountService.updateById(accountUpdateDTO));
    }


}

