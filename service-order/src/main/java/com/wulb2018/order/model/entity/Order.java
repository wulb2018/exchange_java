package com.wulb2018.order.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wulb2018.biz.enums.OrderSide;
import com.wulb2018.biz.enums.OrderStatus;
import com.wulb2018.common.model.BaseEntity;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 委托订单表(t_order)-数据表对应实体类
 *
 * @author makejava
 * @since 2026-01-12 22:30:53
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseEntity<Order> {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 股票ID
     */
    private Long stockId;

    /**
     * 方向：1=买，2=卖
     */
    private OrderSide side;

    /**
     * 类型：1=限价
     */
    private Integer type;

    /**
     * 委托价格
     */
    private Double price;

    /**
     * 委托数量
     */
    private Integer quantity;

    /**
     * 已成交数量
     */
    private Integer filledQuantity;

    /**
     * 冻结资金/资产
     */
    private Double frozenAmount;

    /**
     * 0=新建，1=部分成交，2=全部成交，3=已取消
     */
    @TableField("`status`")
    private OrderStatus status;

}

