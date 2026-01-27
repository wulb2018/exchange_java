package com.wulb2018.engine.listener;

import com.wulb2018.client.order.OrderFeignClient;
import com.wulb2018.engine.service.SimpleMatchingService;
import com.wulb2018.engine.service.convert.OrderFeignConvert;
import jakarta.annotation.Resource;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author wulubin
 * @date 2026/1/19
 * @description TODO
 */
//@Component
public class StartupOrderListListener implements ApplicationListener<ApplicationReadyEvent> {
    @Resource
    private OrderFeignClient orderFeignClient;
    @Resource
    private SimpleMatchingService simpleMatchingService;
    @Resource
    private OrderFeignConvert orderFeignConvert;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
//        System.out.println("系统启动，初始化数据");
//        ApiResponse<List<OrderFeign>> orderFeignListRet = orderFeignClient.getInitOrderList();
//        List<OrderDTO> orderList = orderFeignConvert.toListOrderDTO(orderFeignListRet.getData());
//        simpleMatchingService.loadInitOrderListMap(orderList);
//        System.out.println("拉取数据成功，完成初始化");
    }


}
