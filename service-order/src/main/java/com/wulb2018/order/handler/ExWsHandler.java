package com.wulb2018.order.handler;

import com.wulb2018.order.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

/**
 * @author wulubin
 * @date 2026/1/25
 * @description TODO
 */
//@Component
public class ExWsHandler extends TextWebSocketHandler {
    @Resource
    private OrderService orderService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        // 建立连接
        System.out.println("前端链接ws成功");
        //session.sendMessage(new TextMessage("ok"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        // 接收前端发来的 WebSocket 消息
        System.out.println("前端发来的消息：" + message.toString());
//        //订单簿数据
//        List<Order> buyOrderList = orderService.buyOrderList();
//        List<Order> sellOrderList = orderService.sellOrderList();
//
//        List<OrderBookVO> buyOrderBookList = orderService.buildOrderBookListVO(buyOrderList);
//        List<OrderBookVO> sellOrderBookList = orderService.buildOrderBookListVO(sellOrderList);
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("buyOrderBookList", buyOrderBookList);
//        map.put("sellOrderBookList", sellOrderBookList);
//        session.sendMessage(new TextMessage(JsonMapper.defaultMapper().toJson(map)));
    }
}
