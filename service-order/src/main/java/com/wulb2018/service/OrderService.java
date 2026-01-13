package com.wulb2018.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wulb2018.mapper.OrderMapper;
import com.wulb2018.model.dto.OrderDTO;
import com.wulb2018.model.dto.OrderAddDTO;
import com.wulb2018.model.dto.OrderUpdateDTO;
import com.wulb2018.model.entity.Order;
import com.wulb2018.model.vo.OrderVO;
import com.wulb2018.service.convert.OrderConvert;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 委托订单表(t_order)-业务处理类
 *
 * @author makejava
 * @since 2026-01-12 22:30:54
 */
@Service
@RequiredArgsConstructor
public class OrderService extends BaseService<OrderMapper, Order> {

    private final OrderConvert orderConvert;


    public OrderVO getOne(Serializable id) {
        return orderConvert.toVo(super.getById(id));
    }

    public Boolean save(OrderAddDTO orderAddDTO) {
        return this.save(orderConvert.toEntity(orderAddDTO));
    }

    public Boolean updateById(OrderUpdateDTO orderUpdateDTO) {
        return this.updateById(orderConvert.toEntity(orderUpdateDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

