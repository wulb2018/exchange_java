package com.wulb2018.settlement.controller;


import cn.hutool.core.bean.BeanUtil;
import com.wulb2018.biz.enums.CandlestickType;
import com.wulb2018.biz.model.dto.TradeCommonDTO;
import com.wulb2018.biz.model.entity.TradeSymbol;
import com.wulb2018.biz.model.vo.CandlestickVO;
import com.wulb2018.biz.service.TradeSymbolService;
import com.wulb2018.biz.util.DataPrecisionConvert;
import com.wulb2018.biz.convert.TradeFeignConvert;
import com.wulb2018.biz.model.dto.TradeFeign;
import com.wulb2018.common.controller.BaseRestController;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.settlement.model.dto.TradeAddDTO;
import com.wulb2018.settlement.model.dto.TradeDTO;
import com.wulb2018.settlement.model.dto.TradeUpdateDTO;
import com.wulb2018.settlement.model.vo.TradeVO;
import com.wulb2018.settlement.service.TradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final TradeSymbolService tradeSymbolService;

    @ApiOperation("结算交易")
    @PostMapping("/settlement_trade")
    public ApiResponse<String> settlementTrade(@Valid @RequestBody TradeFeign tradeFeign) {
        TradeCommonDTO tradeCommonDTO = tradeFeignConvert.toOrderCommonDTO(tradeFeign);
        TradeSymbol tradeSymbol = tradeSymbolService.getById(tradeCommonDTO.getSymbolId());
        TradeDTO  tradeDTO = tradeCommonDTO2TradeDTO(tradeCommonDTO, tradeSymbol);
        tradeService.settlementTrade(tradeDTO);
        return ApiResponse.success();
    }

    private TradeDTO tradeCommonDTO2TradeDTO(TradeCommonDTO tradeCommonDTO, TradeSymbol tradeSymbol) {
        TradeDTO tradeDTO = new TradeDTO();
        BeanUtil.copyProperties(tradeCommonDTO, tradeDTO);
        tradeDTO.setPrice(DataPrecisionConvert.intToDecimal(tradeCommonDTO.getPrice(), tradeSymbol.getPricePrecision()));
        tradeDTO.setAmount(DataPrecisionConvert.intToDecimal(tradeCommonDTO.getAmount(), tradeSymbol.getPricePrecision()));
        return tradeDTO;
    }

    @ApiOperation("获取k线初始化数据")
    @PostMapping("/get_candlestick_init_data")
    public ApiResponse<List<CandlestickVO>> getCandlestickInitData(@RequestParam("candlestickType") @NotNull @ApiParam("蜡烛类型") CandlestickType candlestickType) {
        return ApiResponse.success(tradeService.getCandlestickInitData(candlestickType));
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

