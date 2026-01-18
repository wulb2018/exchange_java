package com.wulb2018.settlement.model.entity;

import java.time.LocalDateTime;

import com.wulb2018.common.model.BaseEntity;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 账户主表(t_account)-数据表对应实体类
 *
 * @author makejava
 * @since 2026-01-18 18:11:50
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Account extends BaseEntity<Account> {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 资产币种
     */
    private String asset;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

}

