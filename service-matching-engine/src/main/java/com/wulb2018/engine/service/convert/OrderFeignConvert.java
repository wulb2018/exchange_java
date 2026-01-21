package com.wulb2018.engine.service.convert;


import com.wulb2018.biz.enums.OrderSide;
import com.wulb2018.client.model.OrderFeign;

import com.wulb2018.engine.model.dto.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author wulubin
 * @date 2026/1/17
 * @description TODO
 */
@Mapper(componentModel = "spring")
public interface OrderFeignConvert {

    /**
     * 将int型转OrderSide枚举
     * @param side
     * @return
     */
    default OrderSide map(Integer side) {
        return OrderSide.fromCode(side);
    }

    OrderDTO toOrderDTO(OrderFeign orderFeign);

    List<OrderDTO> toListOrderDTO(List<OrderFeign> orderFeignList);

}
