package com.wulb2018.order.service.convert;


import com.wulb2018.biz.model.dto.OrderCommonDTO;
import com.wulb2018.order.model.entity.Order;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author wulubin
 * @date 2026/1/17
 * @description TODO
 */
@Mapper(
        componentModel = "spring"
)
public interface OrderFeignConvert {

    //@Mapping(target = "side", expression = "java(order.getSide().getCode())")
    OrderCommonDTO toOrderFeign(Order order);

    //@Mapping(target = "side", expression = "java(order.getSide().getCode())")
    List<OrderCommonDTO> toListOrderFeign(List<Order> orderList);

}
