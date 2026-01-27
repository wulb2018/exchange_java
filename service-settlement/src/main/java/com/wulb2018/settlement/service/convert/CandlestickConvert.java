package com.wulb2018.settlement.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wulb2018.biz.model.vo.CandlestickVO;
import com.wulb2018.settlement.model.dto.CandlestickAddDTO;
import com.wulb2018.settlement.model.dto.CandlestickDTO;
import com.wulb2018.settlement.model.dto.CandlestickUpdateDTO;
import com.wulb2018.settlement.model.entity.Candlestick;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * (t_candlestick)-对象转换器接口
 *
 * @author makejava
 * @since 2026-01-26 20:41:12
 */
@Mapper(componentModel = "spring")
public interface CandlestickConvert {

    CandlestickVO toVo(Candlestick candlestick);

    List<CandlestickVO> toListVo(List<Candlestick> candlestick);

    Page<CandlestickVO> toPageVo(Page<Candlestick> candlestick);

    Candlestick toEntity(CandlestickDTO candlestickDTO);

    Candlestick toEntity(CandlestickAddDTO candlestickAddDTO);

    Candlestick toEntity(CandlestickUpdateDTO candlestickUpdateDTO);
}

