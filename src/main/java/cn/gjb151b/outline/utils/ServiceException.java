package cn.gjb151b.outline.utils;

import cn.gjb151b.outline.Constants.ExceptionEnums;

/**
 * 和异常枚举配套的异常类
 * 可以再增加一些相关错误方法
 */
public class ServiceException extends RuntimeException{
    private ExceptionEnums exceptionEnums;

    public ServiceException(ExceptionEnums exceptionEnums){
        this.exceptionEnums = exceptionEnums;
    }

    public ExceptionEnums getExceptionEnums(){
        return exceptionEnums;
    }
}