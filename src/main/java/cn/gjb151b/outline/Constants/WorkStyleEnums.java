package cn.gjb151b.outline.Constants;

import cn.gjb151b.outline.utils.ServiceException;

public enum WorkStyleEnums {
    RECEIVE("1", "接收"),
    SEND("2", "发送"),
    RECEIVE_AND_SEND("3", "收发共用");

    private String code;
    private String msg;
    WorkStyleEnums(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static WorkStyleEnums getMsgWithCode(String code) throws ServiceException {
        for(WorkStyleEnums enums : WorkStyleEnums.values()) {
            if(enums.getCode().equals(code)) {
                return enums;
            }
        }
        throw new ServiceException(ExceptionEnums.ENUM_CODE_MISS_ERR);
    }
}
