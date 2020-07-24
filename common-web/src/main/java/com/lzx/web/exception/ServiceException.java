package com.lzx.web.exception;

/**
 * 业务异常
 *
 * @author lzx
 * @since 2018/12/19
 */
public class ServiceException extends RuntimeException {

    /**
     * 必要时使用错误码区分错误原因
     */
    private int code = -1;


    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getCode() {
        return code;
    }

    public ServiceException setCode(int code) {
        this.code = code;
        return this;
    }
}
