package com.wulb2018.common.exception;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.wulb2018.common.enums.SystemCodeMessage;
import com.wulb2018.common.model.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@Component
public class GlobalExceptionHandler {


    /**
     * 全局异常
     *
     * @param exception
     * @return
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ApiResponse<Object> exception(Exception exception) {
        //log.error(StrUtil.format("系统异常[{}]，参数信息：[{}]", RequestUtil.getRequestUri(), RequestUtil.getParameters()), exception);
        log.error("系统异常", exception);
        return ApiResponse.fail(SystemCodeMessage.SYSTEM_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(BizException.class)
    public ApiResponse<Object> bizException(BizException bizException) {
        //log.warn("业务异常[{}] {}，参数信息：[{}]", RequestUtil.getRequestUri(), bizException.getMsg(), RequestUtil.getParameters());
        return ApiResponse.fail(bizException.getCode(), bizException.getMsg(), bizException.getData());
    }

    /**
     * 针对@RequestParam(required = true) 时参数为空报的异常
     */
    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResponse<Object> missingServletRequestParameterException(
            MissingServletRequestParameterException missingServletRequestParameterException) {
        //log.warn("参数异常[{}] {}", RequestUtil.getRequestUri(), missingServletRequestParameterException.getMessage());
        log.error("参数异常{}", missingServletRequestParameterException.getMessage());
        return ApiResponse.fail(SystemCodeMessage.ILLEGAL_ARGUMENT);
    }

    /**
     * validate 参数校验错误异常
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Object> methodArgumentNotValidException(
            MethodArgumentNotValidException methodArgumentNotValidException) {
        List<ObjectError> allErrors = methodArgumentNotValidException.getBindingResult().getAllErrors();
        //log.warn("参数异常[{}] {}", RequestUtil.getRequestUri(), methodArgumentNotValidException.getMessage() + " " + this.getParamErrorMsg(allErrors));
        log.warn("参数异常 {}", methodArgumentNotValidException.getMessage() + " " + this.getParamErrorMsg(allErrors));
        return ApiResponse.fail(SystemCodeMessage.ILLEGAL_ARGUMENT);
    }


    /**
     * validate 参数校验错误异常
     */
    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public ApiResponse<Object> bindException(BindException e) {
        List<ObjectError> allErrors = e.getAllErrors();
        //log.warn("参数异常[{}] {} ", RequestUtil.getRequestUri(), this.getParamErrorMsg(allErrors));
        log.warn("参数异常 {} ", this.getParamErrorMsg(allErrors));
        return ApiResponse.fail(SystemCodeMessage.ILLEGAL_ARGUMENT);
    }

    /**
     * validate 参数校验错误异常
     */
    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ApiResponse<Object> constraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        List<String> errorArr = violations.stream().map(violation -> {
            Iterator<Path.Node> propertyPath = violation.getPropertyPath().iterator();
            Path.MethodNode methodNode = propertyPath.next().as(Path.MethodNode.class);
            Path.ParameterNode parameterNode = propertyPath.next().as(Path.ParameterNode.class);
            return parameterNode.getName() + " " + violation.getMessage();
        }).collect(Collectors.toList());
        String errMsg = String.join(" ", errorArr.toArray(new String[]{}));
        //log.warn("参数异常[{}] {}", RequestUtil.getRequestUri(), errMsg);
        log.warn("参数异常 {}", errMsg);
        return ApiResponse.fail(SystemCodeMessage.ILLEGAL_ARGUMENT);
    }


    /**
     * 参数异常，举例 String cast to java.lang.integer
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ApiResponse<Object> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        //log.warn("参数类型异常[{}] {}", RequestUtil.getRequestUri(), e.getMessage());
        log.warn("参数类型异常 {}", e.getMessage());
        return ApiResponse.fail(SystemCodeMessage.ILLEGAL_ARGUMENT);
    }

    /**
     * 请求方式错误 get接口， post方式请求
     */
    @ResponseBody
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ApiResponse<Object> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        //log.warn("请求异常[{}] {}", RequestUtil.getRequestUri(), e.getMessage());
        log.warn("请求异常 {}", e.getMessage());
        return ApiResponse.fail(SystemCodeMessage.ILLEGAL_REQUEST);
    }

    private String getParamErrorMsg(List<ObjectError> allErrors) {
        List<String> errorArr = Lists.newArrayList();
        for (ObjectError error : allErrors) {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                errorArr.add(fieldError.getField() + " " + fieldError.getDefaultMessage());
            } else {
                errorArr.add(error.getDefaultMessage());
            }
        }
        return String.join(" ", errorArr.toArray(new String[]{}));
    }


}
