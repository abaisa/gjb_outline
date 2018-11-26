package cn.gjb151b.outline.Constants;

/**
 * Created by ddgdd on 2018/11/26 0026 15:16
 */
/*
对应0、1变量的有无概念
包括dev_GND地线、dev_empEMP加固措施
 */
public enum HaveEnums {

    HAVA(1, "有"),
    HAVE_NOT(0, "无");

    private int code;
    private String msg;
    HaveEnums(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static HaveEnums getMsgWithCode(int code) {
        for(HaveEnums enums : HaveEnums.values()) {
            if(enums.getCode() == code) {
                return enums;
            }
        }

        return null;
    }
}
