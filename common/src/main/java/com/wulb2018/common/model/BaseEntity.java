package com.wulb2018.common.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@ToString
public abstract class BaseEntity<T extends BaseEntity> implements Serializable {

	@TableId(type = IdType.AUTO)
	private Long id;

	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createDate;

	@TableField(fill = FieldFill.INSERT_UPDATE)
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
