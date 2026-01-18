package com.wulb2018.common.enums;

import com.wulb2018.common.ICodeMessage;

public enum SystemCodeMessage implements ICodeMessage {

	SUCCESS("0", "SUCCESS"),

	SYSTEM_ERROR("1", "系统繁忙，请稍后再试"),

	REMOTE_SERVER_BUSY("2","服务器繁忙"),

	BIZ_ERROR("1000", "业务处理异常"),
	LOGIN_ERROR("1001", "您尚未登录或登录超时"),
	NO_PERMISSION("1002", "无操作权限"),

	ILLEGAL_ARGUMENT("2000", "参数不合法"),
	ILLEGAL_REQUEST("2001", "非法请求");


	private final String code;

	private final String message;

	SystemCodeMessage(String code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
