package com.wulb2018.engine.controller;

import com.wulb2018.client.MyNameFeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wulubin
 * @date 2026/1/11
 * @description TODO
 */
@RestController
public class MyNameController {

    @RequestMapping("/name")
    public String getName() {
        return "test666";
    }
}
