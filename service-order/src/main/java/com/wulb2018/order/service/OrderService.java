package com.wulb2018.order.service;


import com.wulb2018.biz.enums.OrderSide;
import com.wulb2018.biz.enums.OrderStatus;
import com.wulb2018.client.model.AccountFeignDTO;
import com.wulb2018.client.matching.MatchingOrderFeignClient;
import com.wulb2018.client.settlement.AccountFeignClient;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.common.service.BaseService;
import com.wulb2018.order.mapper.OrderMapper;
import com.wulb2018.client.model.OrderFeign;
import com.wulb2018.order.model.dto.OrderAddDTO;
import com.wulb2018.order.model.dto.OrderUpdateDTO;
import com.wulb2018.order.model.entity.Order;
import com.wulb2018.order.model.vo.OrderVO;
import com.wulb2018.order.service.convert.OrderConvert;

import com.wulb2018.order.service.convert.OrderFeignConvert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
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
    private final OrderFeignConvert orderFeignConvert;
    private final MatchingOrderFeignClient matchingOrderFeignClient;
    private final AccountFeignClient accountFeignClient;


    public OrderVO getOne(Serializable id) {
        return orderConvert.toVo(super.getById(id));
    }

    public Boolean save(OrderAddDTO orderAddDTO) {
        AccountFeignDTO accountFeignDTO = new AccountFeignDTO();
        accountFeignDTO.setUserId(orderAddDTO.getUserId());
        accountFeignDTO.setPrice(orderAddDTO.getPrice());
        accountFeignDTO.setQuantity(orderAddDTO.getQuantity());
        accountFeignDTO.setSymbolId(orderAddDTO.getSymbolId());
        accountFeignDTO.setSide(orderAddDTO.getSide().getCode());
        ApiResponse<Boolean> response = accountFeignClient.frozenAsset(accountFeignDTO);
        if (!response.getData()) {
            return false;
        }
        Order entity = orderConvert.toEntity(orderAddDTO);
        boolean ret = this.save(entity);
        OrderFeign orderFeign = orderFeignConvert.toOrderFeign(entity);
        matchingOrderFeignClient.create(orderFeign);
        return ret;
        //todo 有空统一改一下索引名称
    }

    public List<OrderFeign> getInitOrderList() {
        List<Order> buyOrderList = lambdaQuery()
                .in(Order::getStatus, List.of(OrderStatus.NEW, OrderStatus.PART_TRADE))
                .eq(Order::getSide, OrderSide.BUY)
                .orderByDesc(Order::getPrice)
                //todo 加载多少条先待定
                .list();


        List<Order> sellOrderList = lambdaQuery()
                .in(Order::getStatus, List.of(OrderStatus.NEW, OrderStatus.PART_TRADE))
                .eq(Order::getSide, OrderSide.SELL)
                .orderByDesc(Order::getPrice)
                //todo 加载多少条先待定
                .list();
        List<OrderFeign> orderFeignList = new ArrayList<>();
        orderFeignList.addAll(orderFeignConvert.toListOrderFeign(buyOrderList));
        orderFeignList.addAll(orderFeignConvert.toListOrderFeign(sellOrderList));
        return orderFeignList;
    }

    public Boolean updateById(OrderUpdateDTO orderUpdateDTO) {
        return this.updateById(orderConvert.toEntity(orderUpdateDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

