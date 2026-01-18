package com.wulb2018.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;


public class AutoFillMetaObjectHandler implements MetaObjectHandler {

	private static final String CREATE_DATE = "createDate";
	private static final String MODIFY_DATE = "modifyDate";
	//private static final String DELETED = "deleted";

	@Override
	public void insertFill(MetaObject metaObject) {
		this.strictInsertFill(metaObject, CREATE_DATE, LocalDateTime::now, LocalDateTime.class);
		this.strictInsertFill(metaObject, MODIFY_DATE, LocalDateTime::now, LocalDateTime.class);
		//this.strictInsertFill(metaObject, DELETED, () -> false, Boolean.class);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		this.strictUpdateFill(metaObject, MODIFY_DATE, LocalDateTime::now, LocalDateTime.class);
	}
}
