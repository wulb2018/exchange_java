package com.wulb2018.settlement.model.dto;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 账户主表(t_account)-实体类
 *
 * @author makejava
 * @since 2026-01-18 18:11:50
 */
@Data
@Accessors(chain = true)
@ApiModel("AccountDTO")
public class AccountDTO {


    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("资产币种")
    private String asset;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedAt;

}

