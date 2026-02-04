package com.wulb2018.settlement.interceptor;

import com.wulb2018.biz.enums.CandlestickType;
import com.wulb2018.common.model.BaseEnum;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

/**
 * @author wulubin
 * @date 2026/2/1
 * @description TODO
 */
@Component
public class WsInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        URI uri = request.getURI();
        //String query = uri.getQuery(); // token=xxx&roomId=1001
        Map<String, String> params = UriComponentsBuilder
                .fromUri(uri)
                .build()
                .getQueryParams()
                .toSingleValueMap();

        String candlestickType = params.get("candlestickType");
        // 存到 session attributes
        attributes.put("candlestickType", BaseEnum.getByCode(CandlestickType.class, candlestickType));
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
