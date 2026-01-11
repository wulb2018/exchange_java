package com.wulb2018.controller;

import com.wulb2018.client.MyNameFeignClient;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wulubin
 * @date 2026/1/11
 * @description TODO
 */
@RestController
public class EchoController {

    @Resource
    private MyNameFeignClient myNameFeignClient;


    @GetMapping("/echo/{name}")
    public String echo(@PathVariable String name) {
        return "Ok" + name;
    }

    @GetMapping("/name")
    public String getName(){
        String name = myNameFeignClient.getName();
        return name;
    }
}
