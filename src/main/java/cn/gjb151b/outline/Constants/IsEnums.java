package cn.gjb151b.outline.Constants;

/**
 * Created by ddgdd on 2018/11/26 0026 15:16
 */
/*
对应0、1变量的是否概念
包括dev_key任务关键设备、dev_interport互联端口、dev_lowsensitive对低频信号敏感、dev_static静电威胁
 */
public enum IsEnums {

    IS(1, "是"),
    IE_NOT(0, "否");

    private int code;
    private String msg;
    IsEnums(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static IsEnums getMsgWithCode(int code) {
        for(IsEnums enums : IsEnums.values()) {
            if(enums.getCode() == code) {
                return enums;
            }
        }

        return null;
    }
}
