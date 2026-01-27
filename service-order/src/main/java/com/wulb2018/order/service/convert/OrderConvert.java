package com.wulb2018.order.service.convert;


import com.wulb2018.order.model.entity.Order;
import com.wulb2018.order.model.vo.OrderVO;
import com.wulb2018.order.model.dto.OrderDTO;
import com.wulb2018.order.model.dto.OrderAddDTO;
import com.wulb2018.biz.model.dto.OrderUpdateDTO;

import java.util.List;

import org.mapstruct.Mapper;

/**
 * 委托订单表(t_order)-对象转换器接口
 *
 * @author makejava
 * @since 2026-01-12 22:30:54
 */
@Mapper(componentModel = "spring")
public interface OrderConvert {

    OrderVO toVo(Order order);

    List<OrderVO> toListVo(List<Order> order);


    Order toEntity(OrderDTO orderDTO);

    Order toEntity(OrderAddDTO orderAddDTO);

    Order toEntity(OrderUpdateDTO orderUpdateDTO);
}

