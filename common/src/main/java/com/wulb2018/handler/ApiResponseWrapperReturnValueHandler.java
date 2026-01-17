package com.wulb2018.handler;



import com.wulb2018.annotation.DisableApiResponseWrapper;
import com.wulb2018.model.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import jakarta.servlet.ServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
public class ApiResponseWrapperReturnValueHandler implements HandlerMethodReturnValueHandler, InitializingBean {

	private HandlerMethodReturnValueHandler delegate;

	private final RequestMappingHandlerAdapter adapter;

	@Override
	public void afterPropertiesSet() throws Exception {
		if(adapter == null) {
			return;
		}
		List<HandlerMethodReturnValueHandler> unmodifiableReturnValueHandlers = adapter.getReturnValueHandlers();
		List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>(unmodifiableReturnValueHandlers);
		for (HandlerMethodReturnValueHandler handler : handlers) {
			if(handler instanceof RequestResponseBodyMethodProcessor) {
				this.delegate = handler;
				int index = handlers.indexOf(handler);
				handlers.set(index, this);
				break;
			}
		}
		adapter.setReturnValueHandlers(handlers);
	}

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return delegate.supportsReturnType(returnType);
	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {
		//如果是下载文件跳过包装
		if(returnValue == null) {
			Optional<String> contentType = Optional.of(webRequest)
					.map(nativeWebRequest -> ((ServletWebRequest) webRequest))
					.map(ServletRequestAttributes::getResponse).map(ServletResponse::getContentType);
			if(contentType.isPresent() && contentType.get().contains("application/vnd.openxmlformats-officedocument")) {
				return;
			}
		}
		DisableApiResponseWrapper disableApiResponseWrapper = AnnotationUtils.findAnnotation(
				returnType.getAnnotatedElement(), DisableApiResponseWrapper.class);
		if(disableApiResponseWrapper != null) {
			delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
			return;
		}
		if(returnValue instanceof ApiResponse) {
			// 返回值是ApiResponse 直接返回
			delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);

		} else {
			// 封装ApiResponse 返回
			ApiResponse<Object> success = ApiResponse.success(returnValue);
			delegate.handleReturnValue(success, returnType, mavContainer, webRequest);
		}

	}
}
