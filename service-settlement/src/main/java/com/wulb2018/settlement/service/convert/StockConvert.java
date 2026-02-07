package com.wulb2018.settlement.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wulb2018.settlement.model.dto.StockAddDTO;
import com.wulb2018.settlement.model.dto.StockDTO;
import com.wulb2018.settlement.model.dto.StockUpdateDTO;
import com.wulb2018.settlement.model.entity.Stock;
import com.wulb2018.settlement.model.vo.StockVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * (t_stock)-对象转换器接口
 *
 * @author makejava
 * @since 2026-02-07 14:53:28
 */
@Mapper(componentModel = "spring")
public interface StockConvert {

    StockVO toVo(Stock stock);

    List<StockVO> toListVo(List<Stock> stock);

    Page<StockVO> toPageVo(Page<Stock> stock);

    Stock toEntity(StockDTO stockDTO);

    Stock toEntity(StockAddDTO stockAddDTO);

    Stock toEntity(StockUpdateDTO stockUpdateDTO);
}

