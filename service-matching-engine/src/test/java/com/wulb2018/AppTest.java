package com.wulb2018;

import com.wulb2018.enums.OrderSide;
import com.wulb2018.model.Order;
import com.wulb2018.service.SimpleMatchingService;
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

    @Test
    public void testSimpleMatching() {
        Order sellOrder1 = new Order();
        sellOrder1.setId(1L);
        sellOrder1.setUserId(1L);
        sellOrder1.setType(1);
        sellOrder1.setSide(OrderSide.SELL);
        sellOrder1.setPrice(80);
        sellOrder1.setQuantity(1);
        sellOrder1.setSymbolId(1L);
        simpleMatchingService.addOrder(sellOrder1);

        //        Order sellOrder2 = new Order();
//        sellOrder2.setId(3L);
//        sellOrder2.setUserId(3L);
//        sellOrder2.setType(1);
//        sellOrder2.setSide(OrderSide.SELL);
//        sellOrder2.setPrice(99);
//        sellOrder2.setQuantity(1);
//        sellOrder2.setSymbolId(1L);
//        simpleMatchingService.addOrder(sellOrder2);

        Order buyOrder1 = new Order();
        buyOrder1.setId(2L);
        buyOrder1.setUserId(2L);
        buyOrder1.setType(1);
        buyOrder1.setSide(OrderSide.BUY);
        buyOrder1.setPrice(100);
        buyOrder1.setQuantity(1);
        buyOrder1.setSymbolId(1L);
        simpleMatchingService.addOrder(buyOrder1);

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
}
