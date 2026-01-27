package com.wulb2018;

import com.wulb2018.biz.enums.CandlestickType;
import com.wulb2018.biz.enums.OrderSide;
import com.wulb2018.biz.enums.OrderStatus;
import com.wulb2018.biz.model.vo.CandlestickVO;
import com.wulb2018.biz.util.PriceGenerator;
import com.wulb2018.client.settlement.TradeFeignClient;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.order.model.dto.OrderAddDTO;
import com.wulb2018.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;


/**
 * Unit test for simple App.
 */
@SpringBootTest
public class AppTest
{
    @Autowired
    private OrderService orderService;
    @Autowired
    private TradeFeignClient tradeFeignClient;

    private int controlPrice = 20000;

    @Test
    public void testClient() {
        ApiResponse<List<CandlestickVO>> candlestickInitDataRet = tradeFeignClient.getCandlestickInitData(CandlestickType.DAY1);
        System.out.println(candlestickInitDataRet);
    }

    @Test
    public void testOrder(){
        OrderAddDTO orderAddDTO = new OrderAddDTO();
        orderAddDTO.setUserId(1L);
        orderAddDTO.setSymbolId(1L);
        orderAddDTO.setSide(OrderSide.SELL);
        orderAddDTO.setType(1);
        orderAddDTO.setPrice(1000.01);
        orderAddDTO.setQuantity(2.);
        orderAddDTO.setStatus(OrderStatus.NEW);
        orderAddDTO.setFrozenAmount(1001.02);
        orderAddDTO.setFilledQuantity(0.);
        orderService.save(orderAddDTO);
    }

    @Test
    public void testBatchCreateOrder() {
        while (true) {
            List<Double> prices = PriceGenerator.generatePrices(200., 1000, 0.01, 0.2, 0.01);
            for (Double price: prices) {
                OrderAddDTO orderAddDTO = new OrderAddDTO();
                orderAddDTO.setUserId(getUserId());
                orderAddDTO.setSymbolId(1L);
                orderAddDTO.setSide(getOrderSide());
                orderAddDTO.setType(1);
                orderAddDTO.setPrice(price);
                orderAddDTO.setQuantity(getQuantity() );
                orderAddDTO.setStatus(OrderStatus.NEW);
                orderAddDTO.setFrozenAmount(0.);
                orderAddDTO.setFilledQuantity(0.);
                orderService.save(orderAddDTO);
            }
        }
    }

    private Long getUserId() {
        Random random = new Random();
        int min = 1;
        int max = 7;
        int userId = min + random.nextInt(max - min + 1);
        return (long) userId;
    }

    private OrderSide getOrderSide() {
        Random random = new Random();
        int min = 1;
        int max = 2;
        int site = min + random.nextInt(max - min + 1);
        return site == 1 ? OrderSide.BUY : OrderSide.SELL;
    }

    private Double getPrice() {
        PriceGenerator.generatePrices(200., 1000, 0.01, 0.2, 0.01);
        Random random = new Random();
        int min = randomInt(random, controlPrice - 5000, controlPrice - 1);
        int max = randomInt(random, controlPrice + 1, controlPrice + 10000);
        int intValue = min + random.nextInt(max - min + 1);
        //控制价格稳定
        if (intValue< 1000000 && intValue > 10000) {
            controlPrice = intValue / 2;
        }
        return intValue * 1.0 / 100;
    }

    private int randomInt(Random random, int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    private Double getQuantity() {
        Random random = new Random();
        int min = 100;
        int max = 100000;
        int intValue = min + random.nextInt(max - min + 1);
        return intValue * 1.0 / 100;
    }

}
