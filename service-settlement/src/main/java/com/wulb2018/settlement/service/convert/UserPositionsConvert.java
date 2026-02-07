package com.wulb2018.settlement.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wulb2018.settlement.model.entity.UserPositions;
import com.wulb2018.settlement.model.vo.UserPositionsVO;
import com.wulb2018.settlement.model.dto.UserPositionsDTO;
import com.wulb2018.settlement.model.dto.UserPositionsAddDTO;
import com.wulb2018.settlement.model.dto.UserPositionsUpdateDTO;

import java.util.List;

import org.mapstruct.Mapper;

/**
 * 用户持仓表(t_user_positions)-对象转换器接口
 *
 * @author makejava
 * @since 2026-02-07 16:25:47
 */
@Mapper(componentModel = "spring")
public interface UserPositionsConvert {

    UserPositionsVO toVo(UserPositions userPositions);

    List<UserPositionsVO> toListVo(List<UserPositions> userPositions);

    Page<UserPositionsVO> toPageVo(Page<UserPositions> userPositions);

    UserPositions toEntity(UserPositionsDTO userPositionsDTO);

    UserPositions toEntity(UserPositionsAddDTO userPositionsAddDTO);

    UserPositions toEntity(UserPositionsUpdateDTO userPositionsUpdateDTO);
}

