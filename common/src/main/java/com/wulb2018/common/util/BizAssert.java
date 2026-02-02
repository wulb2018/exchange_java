package com.wulb2018.common.util;

import com.wulb2018.common.ICodeMessage;
import com.wulb2018.common.enums.SystemCodeMessage;
import com.wulb2018.common.exception.BizException;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Objects;

/**
 * @author chenkaihong
 * @since 2022/8/12 16:45
 */
public abstract class BizAssert {

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new BizException(SystemCodeMessage.BIZ_ERROR.getCode(), message);
        }
    }

    public static void notTrue(boolean expression, String message) {
        if (expression) {
            throw new BizException(SystemCodeMessage.BIZ_ERROR.getCode(), message);
        }
    }

    public static void isBlank(String value, String message) {
        if (StringUtils.isNotBlank(value)) {
            throw new BizException(SystemCodeMessage.BIZ_ERROR.getCode(), message);
        }
    }

    public static void isNotBlank(String value, String message) {
        if (StringUtils.isBlank(value)) {
            throw new BizException(SystemCodeMessage.BIZ_ERROR.getCode(), message);
        }
    }

    public static void isNull(Object object, String message) {
        if (!Objects.isNull(object)) {
            throw new BizException(SystemCodeMessage.BIZ_ERROR.getCode(), message);
        }
    }

    public static void notNull(Object object, String message) {
        if (Objects.isNull(object)) {
            throw new BizException(SystemCodeMessage.BIZ_ERROR.getCode(), message);
        }
    }

    public static void notNull(Object object, ICodeMessage codeMessage) {
        if (Objects.isNull(object)) {
            throw new BizException(codeMessage);
        }
    }

    public static void isTrue(boolean expression, ICodeMessage codeMessage) {
        if (!expression) {
            throw new BizException(codeMessage.getCode(), codeMessage.getMessage());
        }
    }

    public static void notTrue(boolean expression, ICodeMessage codeMessage) {
        if (expression) {
            throw new BizException(codeMessage.getCode(), codeMessage.getMessage());
        }
    }

    public static void isEmpty(Collection<?> collection, String message) {
        if (!Objects.isNull(collection) && !collection.isEmpty()) {
            throw new BizException(SystemCodeMessage.BIZ_ERROR.getCode(), message);
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (Objects.isNull(collection) || collection.isEmpty()) {
            throw new BizException(SystemCodeMessage.BIZ_ERROR.getCode(), message);
        }
    }

}
