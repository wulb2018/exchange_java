package com.wulb2018.order.service.convert;


import com.wulb2018.client.model.OrderFeign;
import com.wulb2018.order.model.entity.Order;
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

    //@Mapping(target = "side", expression = "java(order.getSide().getCode())")
    OrderFeign toOrderFeign(Order order);

    //@Mapping(target = "side", expression = "java(order.getSide().getCode())")
    List<OrderFeign> toListOrderFeign(List<Order> orderList);

}
