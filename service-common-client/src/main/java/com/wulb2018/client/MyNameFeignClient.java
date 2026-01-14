package com.wulb2018.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wulubin
 * @date 2026/1/11
 * @description TODO
 */
@FeignClient(
        name = "service-matching-engine",
        contextId = "MyNameFeignClient"
)
public interface MyNameFeignClient {
    @RequestMapping("/name")
    public String getName();
}
