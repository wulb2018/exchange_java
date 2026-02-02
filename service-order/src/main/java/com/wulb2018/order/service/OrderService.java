package com.wulb2018.order.service;


import com.wulb2018.biz.enums.CandlestickType;
import com.wulb2018.biz.enums.OrderSide;
import com.wulb2018.biz.enums.OrderStatus;
import com.wulb2018.biz.model.dto.AccountCommonDTO;
import com.wulb2018.biz.model.dto.OrderCommonDTO;
import com.wulb2018.biz.model.dto.OrderUpdateDTO;
import com.wulb2018.biz.model.entity.TradeSymbol;
import com.wulb2018.biz.model.vo.CandlestickVO;
import com.wulb2018.biz.service.TradeSymbolService;
import com.wulb2018.biz.util.DataPrecisionConvert;
import com.wulb2018.client.matching.MatchingFeignClient;
import com.wulb2018.client.settlement.AccountFeignClient;
import com.wulb2018.client.settlement.TradeFeignClient;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.common.service.BaseService;
import com.wulb2018.common.util.JsonMapper;
import com.wulb2018.order.mapper.OrderMapper;
import com.wulb2018.order.model.dto.OrderAddDTO;
import com.wulb2018.order.model.entity.Order;
import com.wulb2018.order.model.vo.OrderBookVO;
import com.wulb2018.order.model.vo.OrderVO;
import com.wulb2018.order.service.convert.OrderConvert;
import com.wulb2018.order.service.convert.OrderFeignConvert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 委托订单表(t_order)-业务处理类
 *
 * @author makejava
 * @since 2026-01-12 22:30:54
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService extends BaseService<OrderMapper, Order> {

    private final OrderConvert orderConvert;
    private final OrderFeignConvert orderFeignConvert;
    private final MatchingFeignClient matchingFeignClient;
    private final AccountFeignClient accountFeignClient;
    private final TradeSymbolService tradeSymbolService;
    private final TradeFeignClient tradeFeignClient;


    public OrderVO getOne(Serializable id) {
        return orderConvert.toVo(super.getById(id));
    }

    public Boolean save(OrderAddDTO orderAddDTO) {

        AccountCommonDTO accountCommonDTO = new AccountCommonDTO();
        accountCommonDTO.setUserId(orderAddDTO.getUserId());
        accountCommonDTO.setPrice(orderAddDTO.getPrice());
        accountCommonDTO.setQuantity(orderAddDTO.getQuantity());
        accountCommonDTO.setSymbolId(orderAddDTO.getSymbolId());
        accountCommonDTO.setSide(orderAddDTO.getSide());

        TradeSymbol symbol = tradeSymbolService.getById(accountCommonDTO.getSymbolId());
        if (symbol == null) {
            return false;
        }
        ApiResponse<Boolean> response = accountFeignClient.frozenAsset(accountCommonDTO);
        if (!response.getData()) {
            log.info("可用余额不足以冻结");
            return false;
        }
        Order entity = orderConvert.toEntity(orderAddDTO);
        boolean ret = this.save(entity);
        OrderCommonDTO orderCommonDTO = orderFeignConvert.toOrderFeign(entity);
        //做价格精度转换
        orderCommonDTO.setPrice(DataPrecisionConvert.decimalToInt(entity.getPrice(), symbol.getPricePrecision()));
        orderCommonDTO.setQuantity(DataPrecisionConvert.decimalToInt(entity.getQuantity(), symbol.getQuantityPrecision()));
        matchingFeignClient.addOrder(orderCommonDTO);
        return ret;
        //todo 有空统一改一下索引名称
    }

    public boolean cancelOrder(Long id) {
        return lambdaUpdate().set(Order::getStatus, OrderStatus.CANCEL).eq(Order::getId, id).update();
    }

    public List<OrderCommonDTO> getInitOrderList() {
        List<Order> buyOrderList = buyOrderList();
        List<Long> symbolIds = new ArrayList<>();
        if (!buyOrderList.isEmpty()) {
            symbolIds.addAll(buyOrderList.stream().map(Order::getSymbolId).distinct().toList());
        }

        List<Order> sellOrderList = sellOrderList();
        if (!sellOrderList.isEmpty()) {
            symbolIds.addAll(sellOrderList.stream().map(Order::getSymbolId).distinct().toList());
        }
        List<OrderCommonDTO> orderCommonDTOList = new ArrayList<>();
        if (symbolIds.isEmpty()) {
            return orderCommonDTOList;
        }
        Map<Long, TradeSymbol> symbolIdAndTradeSymbolMap = tradeSymbolService.getSymbolIdAndTradeSymbolMap(symbolIds);
        if (!buyOrderList.isEmpty()) {
            for (Order order: buyOrderList) {
                TradeSymbol tradeSymbol = symbolIdAndTradeSymbolMap.get(order.getSymbolId());
                orderCommonDTOList.add(order2OrderFeign(order, tradeSymbol.getPricePrecision(), tradeSymbol.getQuantityPrecision()));
            }
        }
        if (!sellOrderList.isEmpty()) {
            for (Order order: sellOrderList) {
                TradeSymbol tradeSymbol = symbolIdAndTradeSymbolMap.get(order.getSymbolId());
                orderCommonDTOList.add(order2OrderFeign(order, tradeSymbol.getPricePrecision(), tradeSymbol.getQuantityPrecision()));
            }
        }
        return orderCommonDTOList;
    }

    public List<Order> buyOrderList() {
        //买方 从大到小
        return lambdaQuery()
                .in(Order::getStatus, List.of(OrderStatus.NEW, OrderStatus.PART_TRADE))
                .eq(Order::getSide, OrderSide.BUY)
                .orderByDesc(Order::getPrice)
                //todo 加载多少条先待定
                .list();
    }

    public List<Order> sellOrderList() {
        //卖方 从小到大
        return lambdaQuery()
                .in(Order::getStatus, List.of(OrderStatus.NEW, OrderStatus.PART_TRADE))
                .eq(Order::getSide, OrderSide.SELL)
                .orderByAsc(Order::getPrice)
                //todo 加载多少条先待定
                .list();
    }

    private OrderCommonDTO order2OrderFeign(Order order, Integer pricePrecision, Integer quantityPrecision) {
        OrderCommonDTO orderCommonDTO = new OrderCommonDTO();
        orderCommonDTO.setId(order.getId());
        orderCommonDTO.setUserId(order.getUserId());
        orderCommonDTO.setSymbolId(order.getSymbolId());
        orderCommonDTO.setSide(order.getSide());
        orderCommonDTO.setType(order.getType());
        orderCommonDTO.setPrice(DataPrecisionConvert.decimalToInt(order.getPrice(), pricePrecision));
        orderCommonDTO.setQuantity(DataPrecisionConvert.decimalToInt(order.getQuantity(), quantityPrecision));
        return orderCommonDTO;
    }

    public boolean updateOrderList(List<OrderUpdateDTO> orderUpdateDTOList) {
        //todo 这里可能需要锁行，防止并发
        for (OrderUpdateDTO orderUpdateDTO : orderUpdateDTOList) {
            Order order = getById(orderUpdateDTO.getId());
            if (order.getQuantity() == (orderUpdateDTO.getFilledQuantity() + order.getFilledQuantity())) {
                order.setFilledQuantity(order.getQuantity());
                order.setStatus(OrderStatus.COMPLETE);
            } else if (order.getQuantity() > (orderUpdateDTO.getFilledQuantity() + order.getFilledQuantity())) {
                order.setFilledQuantity(orderUpdateDTO.getFilledQuantity() + order.getFilledQuantity());
                order.setStatus(OrderStatus.PART_TRADE);
            } else {
                //todo 这里处理细节需要再思考
                return false;
            }
            updateById(order);
        }
        return true;
    }

    public String getCandlestickInitData() {
        ApiResponse<List<CandlestickVO>> candlestickInitDataRet = tradeFeignClient.getCandlestickInitData(CandlestickType.SECOND5);
        if (candlestickInitDataRet.getData() == null) {
            return "{}";
        }
        //k线数据
        List<CandlestickVO> candlestickVOS = candlestickInitDataRet.getData();
        //订单簿数据
        List<Order> buyOrderList = buyOrderList();
        List<Order> sellOrderList = sellOrderList();

        List<OrderBookVO> buyOrderBookList = buildOrderBookListVO(buyOrderList);
        List<OrderBookVO> sellOrderBookList = buildOrderBookListVO(sellOrderList);

        Map<String, Object> map = new HashMap<>();
        map.put("candlestickList", candlestickVOS);
        map.put("buyOrderBookList", buyOrderBookList);
        map.put("sellOrderBookList", sellOrderBookList);

        return JsonMapper.defaultMapper().toJson(map);
    }

    public List<OrderBookVO> buildOrderBookListVO(List<Order> orderList) {
        //先限制只返回10个
        int limit = 10;
        List<OrderBookVO> orderBookList = new ArrayList<>();
        OrderBookVO orderBookVO = null;
        Double lastBuyPrice = null;
        for (Order order: orderList) {
            if (lastBuyPrice == null || !lastBuyPrice.equals(order.getPrice())) {
                orderBookVO = new OrderBookVO();
                if (orderBookList.size() >= limit) {
                    break;
                }
                orderBookVO.setOrderPrice(order.getPrice());
                orderBookVO.setAmount(order.getFilledQuantity() * order.getPrice());
                orderBookVO.setCumulativeQuantity(order.getQuantity() - order.getFilledQuantity());
                orderBookVO.setSide(OrderSide.BUY);
            } else {
                orderBookVO.setAmount(orderBookVO.getAmount() + (order.getFilledQuantity() * order.getPrice()));
                orderBookVO.setCumulativeQuantity(orderBookVO.getCumulativeQuantity() + (order.getQuantity() - order.getFilledQuantity()));
            }
            if (orderBookVO.getAmount() == null || orderBookVO.getAmount() == 0) {
                continue;
            }
            orderBookList.add(orderBookVO);
            lastBuyPrice = order.getPrice();
        }
        return orderBookList;
    }


    public Boolean updateById(OrderUpdateDTO orderUpdateDTO) {
        return this.updateById(orderConvert.toEntity(orderUpdateDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

