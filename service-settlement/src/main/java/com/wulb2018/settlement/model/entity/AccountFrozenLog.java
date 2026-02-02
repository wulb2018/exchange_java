package com.wulb2018.settlement.model.entity;

import com.wulb2018.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 账户资金冻结记录(t_account_frozen_log)-数据表对应实体类
 *
 * @author makejava
 * @since 2026-02-01 18:31:55
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AccountFrozenLog extends BaseEntity<AccountFrozenLog> {
        //todo 本表暂时不用
    /**
     * 资金账户id
     */
    private Long accountId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 冻结资金数量
     */
    private Double frozen;


}

