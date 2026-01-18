package com.wulb2018;

import com.wulb2018.biz.enums.OrderSide;
import com.wulb2018.client.model.OrderFeign;
import com.wulb2018.engine.model.dto.OrderDTO;
import com.wulb2018.engine.service.SimpleMatchingService;
import com.wulb2018.engine.service.convert.OrderFeignConvert;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Scanner;

/**
 * Unit test for simple App.
 */

@SpringBootTest
public class AppTest
{

    @Resource
    private SimpleMatchingService simpleMatchingService;
    @Resource
    private OrderFeignConvert orderFeignConvert;

    @Test
    public void testSimpleMatching() {
        OrderDTO sellOrderDTO1 = new OrderDTO();
        sellOrderDTO1.setId(1L);
        sellOrderDTO1.setUserId(1L);
        sellOrderDTO1.setType(1);
        sellOrderDTO1.setSide(OrderSide.SELL);
        sellOrderDTO1.setPrice(80);
        sellOrderDTO1.setQuantity(1);
        sellOrderDTO1.setSymbolId(1L);
        simpleMatchingService.addOrder(sellOrderDTO1);

        //        Order sellOrder2 = new Order();
//        sellOrder2.setId(3L);
//        sellOrder2.setUserId(3L);
//        sellOrder2.setType(1);
//        sellOrder2.setSide(OrderSide.SELL);
//        sellOrder2.setPrice(99);
//        sellOrder2.setQuantity(1);
//        sellOrder2.setSymbolId(1L);
//        simpleMatchingService.addOrder(sellOrder2);

        OrderDTO buyOrderDTO1 = new OrderDTO();
        buyOrderDTO1.setId(2L);
        buyOrderDTO1.setUserId(2L);
        buyOrderDTO1.setType(1);
        buyOrderDTO1.setSide(OrderSide.BUY);
        buyOrderDTO1.setPrice(100);
        buyOrderDTO1.setQuantity(1);
        buyOrderDTO1.setSymbolId(1L);
        simpleMatchingService.addOrder(buyOrderDTO1);

        simpleMatchingService.testPrintTradeList();
    }
    @Test
    public void testScanner(){
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (!scanner.hasNextLine()) {
                break; // 输入流结束（Ctrl+D / Ctrl+Z）
            }
            String line = scanner.nextLine();
            System.out.println("收到：" + line);
        }
    }
    @Test
    public void testOrderFeignConvert() {
        OrderFeign orderFeign = new OrderFeign();
        orderFeign.setId(1L);
        orderFeign.setQuantity(1);
        orderFeign.setSide(1);

        OrderDTO orderDTO = orderFeignConvert.toOrderDTO(orderFeign);
        System.out.println(orderDTO);
    }
}
