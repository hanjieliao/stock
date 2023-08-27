package com.org.stock.constant;

/**
 * 结果码
 */
public interface ResultCode {
    /**成功*/
    short SUCCESS = 0;
    /**登录已失效请重新登录*/
    short UNAUTHORIZED = 401;
    /**许可证已过期*/
    short LICENSE_FAIL = 402;
    /**操作失败*/
    short FAILED = 403;
    /**参数异常*/
    short PARAMS_ERROR =404;
    /**未知业务异常*/
    short UNKNOWN_EXCEPTION = 500;
    /**临时认证失效**/
    short TEMP_TOKEN = 405;
}
