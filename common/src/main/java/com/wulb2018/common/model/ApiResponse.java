package com.wulb2018.common.model;


import com.wulb2018.common.ICodeMessage;
import com.wulb2018.common.enums.SystemCodeMessage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ApiResponse<T> {

	@ApiModelProperty("响应状态码")
	private String code;
	@ApiModelProperty("响应消息提示")
	private String message;
	@ApiModelProperty("链路id")
	private String traceId;
	@ApiModelProperty("响应数据")
	private T data;

	public ApiResponse() {
		this(SystemCodeMessage.SUCCESS.getCode(), SystemCodeMessage.SUCCESS.getMessage());
	}

	public ApiResponse(String code, String message) {
		this.code = code;
		this.message = message;
		this.initTraceId();
	}

	public ApiResponse(ICodeMessage codeMessage) {
		this(codeMessage.getCode(), codeMessage.getMessage());
	}

	public ApiResponse(ICodeMessage codeMessage, T data) {
		this(codeMessage);
		this.data = data;
	}

	public ApiResponse(String code, String message, T data) {
		this(code, message);
		this.data = data;
	}

	public static <T> ApiResponse<T> success(T data) {
		ApiResponse<T> objectDataResult = new ApiResponse<>();
		objectDataResult.setData(data);
		return objectDataResult;
	}

	public static <T> ApiResponse<T> success() {
		ApiResponse<T> objectDataResult = new ApiResponse<>();
		objectDataResult.setData(null);
		return objectDataResult;
	}

	public static <T> ApiResponse<T> fail(ICodeMessage codeMessage, T t) {
		return new ApiResponse<>(codeMessage.getCode(), codeMessage.getMessage(), t);
	}

	public static <T> ApiResponse<T> fail(String code, String message, T t) {
		return new ApiResponse<>(code, message, t);
	}

	public static <T> ApiResponse<T> fail(ICodeMessage codeMessage) {
		return new ApiResponse<>(codeMessage.getCode(), codeMessage.getMessage(), null);
	}

	public static <T> ApiResponse<T> fail(String code, String message) {
		return new ApiResponse<>(code, message, null);
	}

	public void initTraceId() {
		this.traceId = UUID.randomUUID().toString();
	}


}
