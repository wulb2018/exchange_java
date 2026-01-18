package com.wulb2018.client.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wulubin
 * @date 2026/1/18
 * @description TODO
 */
@Data
@Accessors(chain = true)
@ApiModel("AccountFeignDTO")
public class AccountFeignDTO {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("委托价格")
    private Double price;

    @ApiModelProperty("委托数量")
    private Double quantity;
    @ApiModelProperty("交易对ID")
    private Long symbolId;
    @ApiModelProperty("方向：1=买，2=卖")
    private Integer side;

}
