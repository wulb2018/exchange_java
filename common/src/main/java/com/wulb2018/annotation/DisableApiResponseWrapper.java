package com.wulb2018.annotation;

import java.lang.annotation.*;

/**
 * 禁用Controller返回值自动封装成ApiResponse
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DisableApiResponseWrapper {
}
