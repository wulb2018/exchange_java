package com.wulb2018.engine.service;


import com.wulb2018.biz.convert.TradeFeignConvert;
import com.wulb2018.biz.enums.OrderSide;
import com.wulb2018.biz.model.dto.OrderCommonDTO;
import com.wulb2018.biz.model.dto.TradeCommonDTO;
import com.wulb2018.biz.model.dto.TradeFeign;
import com.wulb2018.client.order.OrderFeignClient;
import com.wulb2018.client.settlement.TradeFeignClient;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.engine.enums.TradeType;
import com.wulb2018.engine.model.dto.OrderDTO;
import com.wulb2018.engine.service.convert.OrderFeignConvert;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author wulubin
 * @date 2026/1/14
 * @description TODO
 */
@Service
@RequiredArgsConstructor
public class SimpleMatchingService {
    private final TradeFeignClient tradeFeignClient;
    private final TradeFeignConvert tradeFeignConvert;
    @Resource
    private OrderFeignConvert orderFeignConvert;
    @Resource
    private OrderFeignClient orderFeignClient;
    //卖方需要先拿到最小的所以是拿第一个
    private final static TreeMap<Integer, TreeMap<Long, OrderDTO>> PRICE_AND_ORDER_ID_AND_SELL_ORDER_SORTED_MAP = new TreeMap<>();
    //买方需要先拿到最大的所以是拿最后一个
    private final static TreeMap<Integer, TreeMap<Long, OrderDTO>> PRICE_AND_ORDER_ID_AND_BUY_ORDER_SORTED_MAP = new TreeMap<>();

    //private final static List<TradeCommonDTO> TRADE_DTO_LIST = new ArrayList<>();

    private volatile boolean isLoadInit = false;

    private Integer lastTradePrice = 0;

    public void addOrder(OrderDTO orderDTO){
        if(OrderSide.SELL.equals(orderDTO.getSide())) {
            addSellOrder(orderDTO);
        } else {
            addBuyOrder(orderDTO);
        }
        matching();
    }

    private void addSellOrder(OrderDTO sellOrderDTO) {
        doAddOrder(sellOrderDTO, PRICE_AND_ORDER_ID_AND_SELL_ORDER_SORTED_MAP);
    }

    private void addBuyOrder(OrderDTO buyOrderDTO) {
        doAddOrder(buyOrderDTO, PRICE_AND_ORDER_ID_AND_BUY_ORDER_SORTED_MAP);
    }

    private void doAddOrder(OrderDTO orderDTO, TreeMap<Integer, TreeMap<Long, OrderDTO>> priceAndOrderIdAndOrderSortedMap){
        TreeMap<Long, OrderDTO> orderIdAndOrderSortedMap =  priceAndOrderIdAndOrderSortedMap.get(orderDTO.getPrice());
        if (orderIdAndOrderSortedMap == null) {
            orderIdAndOrderSortedMap = new TreeMap<>();
            orderIdAndOrderSortedMap.put(orderDTO.getId(), orderDTO);
        } else {
            orderIdAndOrderSortedMap.put(orderDTO.getId(), orderDTO);
        }
        priceAndOrderIdAndOrderSortedMap.put(orderDTO.getPrice(), orderIdAndOrderSortedMap);
    }

    //todo 撮合这边的单位转换好像有问题，运行测试时在数据库中的保存的价格是2元左右，但是测试停止后重启撮合就变成200左右
    public void matching(){
        //卖方需要拿到最小的所以是拿第一个
        Map.Entry<Integer, TreeMap<Long, OrderDTO>> priceAndOrderIdAndMinSellOrderMapEntry = PRICE_AND_ORDER_ID_AND_SELL_ORDER_SORTED_MAP.firstEntry();
        if (priceAndOrderIdAndMinSellOrderMapEntry == null) {
            return;
        }
        Integer minSellPrice = priceAndOrderIdAndMinSellOrderMapEntry.getKey();
        System.out.println("卖方最小价格 = " + minSellPrice);
        //买方需要先拿到最大的所以是拿最后一个
        Map.Entry<Integer, TreeMap<Long, OrderDTO>> priceAndOrderIdAndMaxBuyOrderMapEntry = PRICE_AND_ORDER_ID_AND_BUY_ORDER_SORTED_MAP.lastEntry();
        if (priceAndOrderIdAndMaxBuyOrderMapEntry == null) {
            System.out.println("找不到买方最大价格订单");
            return;
        }
        Integer maxBuyPrice = priceAndOrderIdAndMaxBuyOrderMapEntry.getKey();
        System.out.println("买方最大价格 = " + maxBuyPrice);

        if (maxBuyPrice >= minSellPrice) {
            //买方价格比卖方大于等于，那么就一定成交，否则不成交
            System.out.println("买方价格大于等于卖方价格");
            //获取卖方最早的（最小订单id）一个订单
            Map.Entry<Long, OrderDTO> earliestSellOrderEntry = priceAndOrderIdAndMinSellOrderMapEntry.getValue().firstEntry();
            if (earliestSellOrderEntry == null) {
                System.out.println("找不到卖方最小价格的第一个订单");
                return;
            }
            OrderDTO earliestSellOrder = earliestSellOrderEntry.getValue();
            //获取买方最早的（最小订单id）一个订单
            Map.Entry<Long, OrderDTO> earliestBuyOrderEntry = priceAndOrderIdAndMaxBuyOrderMapEntry.getValue().firstEntry();
            if (earliestBuyOrderEntry == null) {
                System.out.println("找不到买方最大价格的第一个订单");
                return;
            }
            OrderDTO earliestBuyOrder = earliestBuyOrderEntry.getValue();

            if (earliestSellOrder.getUserId().equals(earliestBuyOrder.getUserId())) {
                //用户id相同则不进行交易
                System.out.println("用户id相同则不进行交易");
                //做撤销处理
                if (earliestSellOrder.getId() > earliestBuyOrder.getId()) {
                    //earliestBuyOrder
                    orderFeignClient.cancelOrder(earliestBuyOrder.getId());
                    removeBuyOrderFromMap(priceAndOrderIdAndMaxBuyOrderMapEntry, earliestBuyOrder, maxBuyPrice);
                    System.out.println("撤销相同用户id的较早的订单，买方单");
                } else {
                    //earliestSellOrder
                    orderFeignClient.cancelOrder(earliestSellOrder.getId());
                    removeSellOrderFromMap(priceAndOrderIdAndMinSellOrderMapEntry, earliestSellOrder, minSellPrice);
                    System.out.println("用户id相同则不进行交易");
                    System.out.println("撤销相同用户id的较早的订单，卖方单");
                }
                return;
            }
            if (!earliestSellOrder.getSymbolId().equals(earliestBuyOrder.getSymbolId())) {
                //交易对不同不进行交易
                System.out.println("交易对不同不进行交易");
                return;
            }
            lastTradePrice = getLastTradePrice(earliestSellOrder, earliestBuyOrder);
            if (earliestSellOrder.getQuantity().equals(earliestBuyOrder.getQuantity())) {
                //买方价格大于等于卖方价格，且数量相等，直接成交
                System.out.println("数量相等，直接全部成交");
                //添加成交记录
                TradeCommonDTO tradeCommonDTO = createTrade(earliestSellOrder, earliestBuyOrder, lastTradePrice, TradeType.COMPLETE);
                settlementTrade(tradeCommonDTO);
                //分别在有序map中移除两个订单
                //删除卖方订单
                removeSellOrderFromMap(priceAndOrderIdAndMinSellOrderMapEntry, earliestSellOrder, minSellPrice);
                //删除买方订单
                removeBuyOrderFromMap(priceAndOrderIdAndMaxBuyOrderMapEntry, earliestBuyOrder, maxBuyPrice);
            } else if (earliestSellOrder.getQuantity() > earliestBuyOrder.getQuantity()) {
                //卖方数量比买方大 卖方部分成交
                System.out.println("卖方数量比买方大 卖方部分成交");
                TradeCommonDTO tradeCommonDTO = createTrade(earliestSellOrder, earliestBuyOrder, lastTradePrice, TradeType.SELL_PART);
                settlementTrade(tradeCommonDTO);
                //删除买方订单
                removeBuyOrderFromMap(priceAndOrderIdAndMaxBuyOrderMapEntry, earliestBuyOrder, maxBuyPrice);
            } else {
                //买方数量比卖方大 买方部分成交
                System.out.println("买方数量比卖方大 买方部分成交");
                TradeCommonDTO tradeCommonDTO = createTrade(earliestSellOrder, earliestBuyOrder, lastTradePrice, TradeType.BUY_PART);
                settlementTrade(tradeCommonDTO);
                //删除卖方订单
                removeSellOrderFromMap(priceAndOrderIdAndMinSellOrderMapEntry, earliestSellOrder, minSellPrice);
            }
        }
    }

    /**
     * 删除卖方订单
     * @param priceAndOrderIdAndMinSellOrderMapEntry
     * @param earliestSellOrderDTO
     * @param minSellPrice
     */
    private void removeSellOrderFromMap(Map.Entry<Integer, TreeMap<Long, OrderDTO>> priceAndOrderIdAndMinSellOrderMapEntry,
                                        OrderDTO earliestSellOrderDTO, Integer minSellPrice) {
        TreeMap<Long, OrderDTO> orderIdAndMinSellOrderMap = priceAndOrderIdAndMinSellOrderMapEntry.getValue();
        orderIdAndMinSellOrderMap.remove(earliestSellOrderDTO.getId());
        if (orderIdAndMinSellOrderMap.isEmpty()) {
            //该价位的订单数量为0时删除该价位
            PRICE_AND_ORDER_ID_AND_SELL_ORDER_SORTED_MAP.remove(minSellPrice);
        }
    }

    /**
     * 删除买方订单
     * @param priceAndOrderIdAndMaxBuyOrderMapEntry
     * @param earliestBuyOrderDTO
     * @param maxBuyPrice
     */
    private void removeBuyOrderFromMap(Map.Entry<Integer, TreeMap<Long, OrderDTO>> priceAndOrderIdAndMaxBuyOrderMapEntry,
                                       OrderDTO earliestBuyOrderDTO, Integer maxBuyPrice) {
        TreeMap<Long, OrderDTO> orderIdAndMaxBuyOrderMap = priceAndOrderIdAndMaxBuyOrderMapEntry.getValue();
        orderIdAndMaxBuyOrderMap.remove(earliestBuyOrderDTO.getId());
        if(orderIdAndMaxBuyOrderMap.isEmpty()) {
            //该价位的订单数量为0时删除该价位
            PRICE_AND_ORDER_ID_AND_BUY_ORDER_SORTED_MAP.remove(maxBuyPrice);
        }
    }


    private TradeCommonDTO createTrade(OrderDTO earliestSellOrderDTO, OrderDTO earliestBuyOrderDTO, Integer lastTradePrice, TradeType tradeType) {
        TradeCommonDTO tradeCommonDTO = new TradeCommonDTO();
        tradeCommonDTO.setSymbolId(earliestSellOrderDTO.getSymbolId());
        tradeCommonDTO.setBuyOrderId(earliestBuyOrderDTO.getId());
        tradeCommonDTO.setSellOrderId(earliestSellOrderDTO.getId());
        tradeCommonDTO.setBuyUserId(earliestBuyOrderDTO.getUserId());
        tradeCommonDTO.setSellUserId(earliestSellOrderDTO.getUserId());
        tradeCommonDTO.setPrice(lastTradePrice);
        if (TradeType.SELL_PART.equals(tradeType)) {
            //成交数量取较小值
            tradeCommonDTO.setQuantity(earliestBuyOrderDTO.getQuantity());
            //卖单 较大值减去 买单较小值，重置卖单数量
            earliestSellOrderDTO.setQuantity(earliestSellOrderDTO.getQuantity() - earliestBuyOrderDTO.getQuantity());
        } else if(TradeType.BUY_PART.equals(tradeType)) {
            //成交数量取较小值
            tradeCommonDTO.setQuantity(earliestSellOrderDTO.getQuantity());
            //买单 较大值减去 卖单较小值，重置买单数量
            earliestBuyOrderDTO.setQuantity(earliestBuyOrderDTO.getQuantity() - earliestSellOrderDTO.getQuantity());
        } else {
            //全部成交 就取 卖方订单数量
            tradeCommonDTO.setQuantity(earliestSellOrderDTO.getQuantity());
        }
        tradeCommonDTO.setAmount(tradeCommonDTO.getPrice() * tradeCommonDTO.getQuantity());
        tradeCommonDTO.setMakerSide(getMakerSide(earliestSellOrderDTO.getId(), earliestBuyOrderDTO.getId()));
        return tradeCommonDTO;
    }


    private Integer getLastTradePrice(OrderDTO earliestSellOrderDTO, OrderDTO earliestBuyOrderDTO) {
        //比较谁先挂单
        if (earliestSellOrderDTO.getId() < earliestBuyOrderDTO.getId()) {
            System.out.println("成交价格 = " + earliestSellOrderDTO.getPrice());
            return earliestSellOrderDTO.getPrice();
        } else {
            System.out.println("成交价格 = " + earliestBuyOrderDTO.getPrice());
            return earliestBuyOrderDTO.getPrice();
        }
    }

    private OrderSide getMakerSide(Long earliestSellOrderId, Long earliestBuyOrderId) {
        if (earliestSellOrderId < earliestBuyOrderId) {
            return OrderSide.SELL;
        } else {
            return OrderSide.BUY;
        }
    }

    private void settlementTrade(TradeCommonDTO tradeCommonDTO) {
        TradeFeign tradeFeign = tradeFeignConvert.toTradeFeign(tradeCommonDTO);
        tradeFeignClient.settlementTrade(tradeFeign);
    }

    /**
     * 在本服务启动时加载可以不考虑并发问题
     */
    public void loadInitOrderListMap() {
        ApiResponse<List<OrderCommonDTO>> orderFeignListRet = orderFeignClient.getInitOrderList();
        List<OrderDTO> orderList = orderFeignConvert.toListOrderDTO(orderFeignListRet.getData());
        for (OrderDTO orderDTO: orderList) {
            addOrder(orderDTO);
        }
        isLoadInit = true;
    }

    public void initOrderListMap() {
        //todo 这里的反序列化问题，值得在细细研究一下
        System.out.println("接收到订单服务通知初始化数据");
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("被订单服务通知初始化数据");
            if (isLoadInit) {
                System.out.println("数据已经被初始化1");
                return;
            }
            synchronized(this) {
                if (isLoadInit) {
                    System.out.println("数据已经被初始化2");
                    return;
                }
                //todo 这个有空看一下那个加两个 synchronized 的是为什么要加两个  看看我这边需不需要，就是看那个教程视频或者网上查一下
                loadInitOrderListMap();
            }
            System.out.println("数据初始化完成");
        });
        thread.start();
    }

//    public void testPrintTradeList() {
//        System.out.println(TRADE_DTO_LIST);
//    }
//
//    public String getTradeListString() {
//        return TRADE_DTO_LIST.toString();
//    }
}
