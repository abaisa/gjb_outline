package cn.gjb151b.outline.Constants;

/**
 * 错误枚举
 * （错误编号，错误信息）
 * 抛出异常使用错误类传递消息
 */
public enum ExceptionEnums {
    // DB error 1000
    DB_FETCH_ERR(1001, "fetch data from database error"),
    DB_EMPTY_ERR(1002, "fetch db is empty"),

    // 参数错误 2000
    PARAM_PAGE_ID_ERR(2001, "param error, page id illegal"),
    ENUM_CODE_MISS_ERR(2002, "enum code not found"),
    PARAM_DATA_PARSE_ERR(2003, "param data parse fail"),
    DEV_SYS_INPUT_DATA_ERR(2004, "data from pre system error"),
    UPLOAD_IMG_HAVE_EXISTED(2005,"图片名重复，请修改图片名称后再次上传！");

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
