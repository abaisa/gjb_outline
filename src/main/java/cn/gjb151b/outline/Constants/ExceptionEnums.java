package cn.gjb151b.outline.Constants;

/**
 * 错误枚举
 * （错误编号，错误信息）
 * 抛出异常使用错误类传递消息
 */
public enum ExceptionEnums {
    DB_FETCH_ERR(2, "fetch data from database error"),
    DB_EMPTY_ERR(3, "fetch db is empty");

    private int errCode;
    private String errMsg;

    ExceptionEnums(int code, String msg){
        this.setValue(code, msg);
    }

    public String getErrMsg() {
        return errMsg;
    }

    public int getErrCode() {
        return errCode;
    }

    private void setValue(int code, String msg) {
        this.errCode = code;
        this.errMsg = msg;
    }

}
