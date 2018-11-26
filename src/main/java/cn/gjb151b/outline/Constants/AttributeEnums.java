package cn.gjb151b.outline.Constants;

/**
 * Created by ddgdd on 2018/11/26 0026 14:29
 */

/*
dev_attribute 设备属性 1代表用频 0代表非用频
 */
public enum AttributeEnums {

    FREQUENCY(1, "用频"),
    NO_USE_FREQUENCY(0, "非用频");

    private int code;
    private String msg;
    AttributeEnums(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static AttributeEnums getMsgWithCode(int code) {
        for(AttributeEnums enums : AttributeEnums.values()) {
            if(enums.getCode() == code) {
                return enums;
            }
        }

        return null;
    }
}
