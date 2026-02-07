package com.wulb2018.engine.model.dto;

import com.wulb2018.biz.enums.OrderSide;
import lombok.Data;

/**
 * @author wulubin
 * @date 2026/1/18
 * @description TODO
 */
@Data
public class OrderDTO {

    private Long id;

    private Long userId;

    private Long stockId;

    private OrderSide side;

    private Integer type;

    private Integer price;

    private Integer quantity;
}
