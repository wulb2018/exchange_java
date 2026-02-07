package com.wulb2018.biz.service;


import com.wulb2018.biz.mapper.StockMapper;
import com.wulb2018.biz.model.dto.StockAddDTO;
import com.wulb2018.biz.model.dto.StockUpdateDTO;
import com.wulb2018.biz.model.entity.Stock;
import com.wulb2018.biz.model.vo.StockVO;
import com.wulb2018.biz.service.convert.StockConvert;
import com.wulb2018.common.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * (t_stock)-业务处理类
 *
 * @author makejava
 * @since 2026-02-07 14:53:28
 */
@Service
@RequiredArgsConstructor
public class StockService extends BaseService<StockMapper, Stock> {

    private final StockConvert stockConvert;

    public StockVO getOne(Serializable id) {
        return stockConvert.toVo(super.getById(id));
    }

    public Boolean save(StockAddDTO stockAddDTO) {
        return this.save(stockConvert.toEntity(stockAddDTO));
    }

    public Boolean updateById(StockUpdateDTO stockUpdateDTO) {
        return this.updateById(stockConvert.toEntity(stockUpdateDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

