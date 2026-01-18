package com.wulb2018.settlement.controller;


import com.wulb2018.biz.model.dto.TradeCommonDTO;
import com.wulb2018.client.model.TradeFeign;
import com.wulb2018.common.controller.BaseRestController;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.settlement.model.vo.TradeVO;
import com.wulb2018.settlement.model.dto.TradeAddDTO;
import com.wulb2018.settlement.model.dto.TradeUpdateDTO;
import com.wulb2018.settlement.service.TradeService;
import com.wulb2018.client.convert.TradeFeignConvert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 成交记录(t_trade)-控制层
 *
 * @author makejava
 * @since 2026-01-18 14:48:59
 */

@Api(tags = "成交记录管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("trade")
public class TradeController extends BaseRestController {

    private final TradeService tradeService;
    private final TradeFeignConvert tradeFeignConvert;

    @ApiOperation("结算交易")
    @PostMapping("/settlement_trade")
    public ApiResponse<String> settlementTrade(@Valid @RequestBody TradeFeign tradeFeign) {
        TradeCommonDTO tradeCommonDTO = tradeFeignConvert.toOrderCommonDTO(tradeFeign);
        tradeService.settlementTrade(tradeCommonDTO);
        return ApiResponse.success();
    }



    @ApiOperation("查询详情")
    @GetMapping("get/{id}")
    public ApiResponse<TradeVO> getById(@PathVariable @NotNull @ApiParam("主键id") Long id) {
        return ApiResponse.success(tradeService.getOne(id));
    }

    @ApiOperation("添加成交记录")
    @PostMapping("add")
    public ApiResponse<Boolean> add(@Valid TradeAddDTO tradeAddDTO) {
        return ApiResponse.success(tradeService.save(tradeAddDTO));
    }

    @ApiOperation("修改成交记录")
    @PostMapping("updateById")
    public ApiResponse<Boolean> updateById(@Valid TradeUpdateDTO tradeUpdateDTO) {
        return ApiResponse.success(tradeService.updateById(tradeUpdateDTO));
    }

}

