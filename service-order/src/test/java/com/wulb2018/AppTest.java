package com.wulb2018;

import com.wulb2018.order.model.dto.OrderAddDTO;
import com.wulb2018.order.service.OrderService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * Unit test for simple App.
 */
@SpringBootTest
public class AppTest
{
    @Autowired
    private OrderService orderService;

    @Test
    public void testOrder(){
        OrderAddDTO orderAddDTO = new OrderAddDTO();
        orderAddDTO.setUserId(1L);
        orderAddDTO.setSymbolId(1L);
        orderAddDTO.setSide(1);
        orderAddDTO.setType(1);
        orderAddDTO.setPrice(1000.01);
        orderAddDTO.setQuantity(2.);
        orderAddDTO.setStatus(0);
        orderAddDTO.setFrozenAmount(1001.02);
        orderAddDTO.setFilledQuantity(0.);
        orderService.save(orderAddDTO);
    }
}
