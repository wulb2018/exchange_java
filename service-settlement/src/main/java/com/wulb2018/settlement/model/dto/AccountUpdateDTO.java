package com.wulb2018.settlement.model.dto;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 账户主表(t_account)-修改实体参数类
 *
 * @author makejava
 * @since 2026-01-18 18:11:50
 */
@Data
@Accessors(chain = true)
@ApiModel("AccountUpdateDTO")
public class AccountUpdateDTO {

    @ApiModelProperty("账户ID")
    private Long id;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("资产币种")
    private String asset;

    @ApiModelProperty("可用余额")
    private Double available;

    @ApiModelProperty("冻结余额")
    private Double frozen;


}

