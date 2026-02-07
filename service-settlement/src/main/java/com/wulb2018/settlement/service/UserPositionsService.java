package com.wulb2018.settlement.service;


import com.wulb2018.common.service.BaseService;
import com.wulb2018.settlement.mapper.UserPositionsMapper;
import com.wulb2018.settlement.model.dto.UserPositionsAddDTO;
import com.wulb2018.settlement.model.dto.UserPositionsUpdateDTO;
import com.wulb2018.settlement.model.entity.UserPositions;
import com.wulb2018.settlement.model.vo.UserPositionsVO;
import com.wulb2018.settlement.service.convert.UserPositionsConvert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 用户持仓表(t_user_positions)-业务处理类
 *
 * @author makejava
 * @since 2026-02-07 16:25:47
 */
@Service
@RequiredArgsConstructor
public class UserPositionsService extends BaseService<UserPositionsMapper, UserPositions> {

    private final UserPositionsConvert userPositionsConvert;


    public UserPositionsVO getOne(Serializable id) {
        return userPositionsConvert.toVo(super.getById(id));
    }

    public Boolean save(UserPositionsAddDTO userPositionsAddDTO) {
        return this.save(userPositionsConvert.toEntity(userPositionsAddDTO));
    }

    public Boolean updateById(UserPositionsUpdateDTO userPositionsUpdateDTO) {
        return this.updateById(userPositionsConvert.toEntity(userPositionsUpdateDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

