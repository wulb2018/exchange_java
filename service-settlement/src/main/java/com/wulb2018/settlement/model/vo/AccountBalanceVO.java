package com.wulb2018.settlement.model.vo;

import java.time.LocalDateTime;

import com.wulb2018.common.model.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 余额表(t_account_balance)-展现层实体类
 *
 * @author makejava
 * @since 2026-01-18 18:12:07
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@ApiModel("AccountBalanceVO")
public class AccountBalanceVO extends BaseVO<AccountBalanceVO> {

    @ApiModelProperty("账户ID")
    private Long accountId;

    @ApiModelProperty("可用余额")
    private Double available;

    @ApiModelProperty("冻结余额")
    private Double frozen;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedAt;


}

