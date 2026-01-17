package com.wulb2018.exception;


import com.wulb2018.ICodeMessage;
import com.wulb2018.enums.SystemCodeMessage;

public class BizException extends RuntimeException {
    private final String code;
    private final String msg;
    private Object data;

    public BizException(ICodeMessage codeMessage, Object... args) {
        super(codeMessage.getMessage());
        this.code = codeMessage.getCode();
        String message = codeMessage.getMessage();
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                message = message.replaceFirst("\\{}", arg.toString());
            }
        }
        this.msg = message;
    }

    public BizException(String code, String message) {
        this(code, message, null);
    }

    public BizException(String code, String message, Object data) {
        super(message);
        this.code = code;
        this.msg = message;
        this.data = data;
    }

    public BizException(String message) {
        super(message);
        this.code = SystemCodeMessage.BIZ_ERROR.getCode();
        this.msg = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }
}
