package com.wulb2018.settlement.model.vo;

import com.wulb2018.common.model.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 账户主表(t_account)-展现层实体类
 *
 * @author makejava
 * @since 2026-01-18 18:11:50
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@ApiModel("AccountVO")
public class AccountVO extends BaseVO<AccountVO> {

    @ApiModelProperty("用户ID")
    private Long userId;

//    @ApiModelProperty("资产币种")
//    private String asset;

    @ApiModelProperty("可用余额")
    private Double available;

    @ApiModelProperty("冻结余额")
    private Double frozen;

}

