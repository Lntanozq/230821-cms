package com.briup.cms.exception;

import com.briup.cms.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
//@ControllerAdvice + @ResponseBody
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        Result result = null;
        if (e instanceof ServiceException) {
            log.error(e.getMessage());
            result = Result.failure(((ServiceException) e).getResultCode());
        }else {
            log.error(e.getMessage());
            result = Result.failure(500, "服务器意外错误：" + e.getMessage());
        }
        return result;
    }
}
