package cn.gjb151b.outline.Constants;

import cn.gjb151b.outline.utils.ServiceException;

public enum SubSysEnums {

    SUBSYS(0, "分系统"),
    EQP(1, "设备");

    private int code;
    private String msg;
    SubSysEnums(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static SubSysEnums getMsgWithCode(int code) throws ServiceException {
        for(SubSysEnums enums : SubSysEnums.values()) {
            if(enums.getCode() == code) {
                return enums;
            }
        }
        throw new ServiceException(ExceptionEnums.ENUM_CODE_MISS_ERR);
    }
}
