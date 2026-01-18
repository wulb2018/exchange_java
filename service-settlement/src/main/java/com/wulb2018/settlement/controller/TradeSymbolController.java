package com.wulb2018.settlement.controller;


import com.wulb2018.common.controller.BaseRestController;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.settlement.model.vo.TradeSymbolVO;
import com.wulb2018.settlement.model.dto.TradeSymbolAddDTO;
import com.wulb2018.settlement.model.dto.TradeSymbolUpdateDTO;
import com.wulb2018.settlement.service.TradeSymbolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 交易标的 / 交易对(t_trade_symbol)-控制层
 *
 * @author makejava
 * @since 2026-01-18 19:47:05
 */

@Api(tags = "交易标的 / 交易对管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("tradeSymbol")
public class TradeSymbolController extends BaseRestController {

    private final TradeSymbolService tradeSymbolService;



    @ApiOperation("查询详情")
    @GetMapping("get/{id}")
    public ApiResponse<TradeSymbolVO> getById(@PathVariable @NotNull @ApiParam("主键id") Long id) {
        return ApiResponse.success(tradeSymbolService.getOne(id));
    }

    @ApiOperation("添加交易标的 / 交易对")
    @PostMapping("add")
    public ApiResponse<Boolean> add(@Valid TradeSymbolAddDTO tradeSymbolAddDTO) {
        return ApiResponse.success(tradeSymbolService.save(tradeSymbolAddDTO));
    }

    @ApiOperation("修改交易标的 / 交易对")
    @PostMapping("updateById")
    public ApiResponse<Boolean> updateById(@Valid TradeSymbolUpdateDTO tradeSymbolUpdateDTO) {
        return ApiResponse.success(tradeSymbolService.updateById(tradeSymbolUpdateDTO));
    }


}

