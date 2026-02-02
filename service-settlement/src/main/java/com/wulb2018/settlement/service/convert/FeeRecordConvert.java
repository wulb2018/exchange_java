package com.wulb2018.settlement.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wulb2018.settlement.model.dto.FeeRecordAddDTO;
import com.wulb2018.settlement.model.dto.FeeRecordDTO;
import com.wulb2018.settlement.model.dto.FeeRecordUpdateDTO;
import com.wulb2018.settlement.model.entity.FeeRecord;
import com.wulb2018.settlement.model.vo.FeeRecordVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 手续费记录(t_fee_record)-对象转换器接口
 *
 * @author makejava
 * @since 2026-02-01 20:15:09
 */
@Mapper(componentModel = "spring")
public interface FeeRecordConvert {

    FeeRecordVO toVo(FeeRecord feeRecord);

    List<FeeRecordVO> toListVo(List<FeeRecord> feeRecord);

    Page<FeeRecordVO> toPageVo(Page<FeeRecord> feeRecord);

    FeeRecord toEntity(FeeRecordDTO feeRecordDTO);

    FeeRecord toEntity(FeeRecordAddDTO feeRecordAddDTO);

    FeeRecord toEntity(FeeRecordUpdateDTO feeRecordUpdateDTO);
}

