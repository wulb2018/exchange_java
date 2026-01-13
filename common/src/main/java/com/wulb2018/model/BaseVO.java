package com.wulb2018.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

public class BaseVO<T extends BaseVO> implements Serializable {
    @ApiModelProperty("记录id")
    private Long id;

    @ApiModelProperty("创建时间")
    private LocalDateTime createDate;

    @ApiModelProperty("更新时间")
    private LocalDateTime modifyDate;


    public T setId(Long id) {
        this.id = id;
        return (T) this;
    }

    public T setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
        return (T) this;
    }

    public T setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
        return (T) this;
    }
}
