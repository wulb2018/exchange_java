package com.wulb2018.engine.runner;

import com.wulb2018.biz.enums.OrderSide;
import com.wulb2018.client.model.OrderFeign;
import com.wulb2018.client.order.OrderFeignClient;
import com.wulb2018.common.model.ApiResponse;
import com.wulb2018.engine.model.dto.OrderDTO;
import com.wulb2018.engine.service.SimpleMatchingService;
import com.wulb2018.engine.service.convert.OrderFeignConvert;
import feign.FeignException;
import jakarta.annotation.Resource;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wulubin
 * @date 2026/1/19
 * @description TODO
 */
@Component
public class OrderListInitRunner implements ApplicationRunner {

    @Resource
    private SimpleMatchingService simpleMatchingService;


    @Override
    public void run(ApplicationArguments args) {
        //这个可以在提供服务前就进行加载数据
        System.out.println("系统启动，初始化数据");
        try {

            simpleMatchingService.loadInitOrderListMap();
            System.out.println("拉取数据成功，完成初始化");
        } catch (FeignException e) {
            System.out.println("拉取数据失败，未完成初始化");
        }
    }

}
