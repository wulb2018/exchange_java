package com.wulb2018.settlement.handler;

import com.wulb2018.biz.enums.CandlestickType;
import com.wulb2018.biz.model.vo.CandlestickVO;
import com.wulb2018.biz.model.dto.OrderBookCommonDTO;
import com.wulb2018.client.order.OrderFeignClient;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.common.util.JsonMapper;
import com.wulb2018.settlement.service.CandlestickService;
import jakarta.annotation.Resource;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wulubin
 * @date 2026/1/25
 * @description TODO
 */
//@Component
public class ExWsHandler extends TextWebSocketHandler {
//    @Resource
//    private TradeService tradeService;
    @Resource
    private CandlestickService candlestickService;

    @Resource
    private OrderFeignClient orderFeignClient;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        Map<String, Object> attrs = session.getAttributes();
        CandlestickType candlestickType = (CandlestickType) attrs.get("candlestickType");

        System.out.println(candlestickType);
        // 建立连接
        System.out.println("前端链接ws成功");
        //session.sendMessage(new TextMessage("ok"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        // 接收前端发来的 WebSocket 消息
        System.out.println("前端发来的消息：" + message.toString());
        Map<String, Object> map = new HashMap<>();
        List<CandlestickVO>  lastSomeCandlestickList = candlestickService.getLastSomeCandlestickList(CandlestickType.SECOND5);
        map.put("candlestickList", lastSomeCandlestickList);

        ApiResponse<Map<String, List<OrderBookCommonDTO>>> orderBook = orderFeignClient.getOrderBook();
        if (!orderBook.getData().isEmpty()) {
            Map<String, List<OrderBookCommonDTO>> orderBookMap= orderBook.getData();
            map.putAll(orderBookMap);
        }
        session.sendMessage(new TextMessage(JsonMapper.defaultMapper().toJson(map)));
    }
}
