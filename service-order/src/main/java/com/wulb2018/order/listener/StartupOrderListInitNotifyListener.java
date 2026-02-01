package com.wulb2018.order.listener;

import com.wulb2018.client.matching.MatchingFeignClient;
import feign.FeignException;
import jakarta.annotation.Resource;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author wulubin
 * @date 2026/1/20
 * @description TODO
 */
@Component
public class StartupOrderListInitNotifyListener implements ApplicationListener<ApplicationReadyEvent> {
    @Resource
    private MatchingFeignClient matchingFeignClient;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        //这个通知初始化需要等到提供端口服务后再调用,所以在监听服务就绪这边
        System.out.println("服务就绪，通知撮合服务初始化");
        try {
            matchingFeignClient.init();
            System.out.println("通知撮合服务初始化成功");
        }catch (FeignException e) {
            System.out.println("通知撮合服务初始化失败");
        }

    }
}
