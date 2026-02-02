package com.wulb2018.settlement.service;


import com.wulb2018.common.service.BaseService;
import com.wulb2018.settlement.mapper.FeeRecordMapper;
import com.wulb2018.settlement.model.dto.FeeRecordAddDTO;
import com.wulb2018.settlement.model.dto.FeeRecordUpdateDTO;
import com.wulb2018.settlement.model.entity.FeeRecord;
import com.wulb2018.settlement.model.vo.FeeRecordVO;
import com.wulb2018.settlement.service.convert.FeeRecordConvert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 手续费记录(t_fee_record)-业务处理类
 *
 * @author makejava
 * @since 2026-02-01 20:15:09
 */
@Service
@RequiredArgsConstructor
public class FeeRecordService extends BaseService<FeeRecordMapper, FeeRecord> {

    private final FeeRecordConvert feeRecordConvert;


    public FeeRecordVO getOne(Serializable id) {
        return feeRecordConvert.toVo(super.getById(id));
    }


    public Boolean save(FeeRecordAddDTO feeRecordAddDTO) {
        return this.save(feeRecordConvert.toEntity(feeRecordAddDTO));
    }

    public Boolean updateById(FeeRecordUpdateDTO feeRecordUpdateDTO) {
        return this.updateById(feeRecordConvert.toEntity(feeRecordUpdateDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

