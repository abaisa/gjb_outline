package cn.gjb151b.outline.Constants;

/**
 * Created by ddgdd on 2018/11/26 0026 14:29
 */

/*
dev_install 安装方式 1代表台式 2代表机架安装 3代表甲板固定 4代表壁挂 5代表手持 6非手持
 */
public enum InstallEnums {

    DESKTOP(1, "台式"),
    FRAME(2, "机架安装"),
    BOARD(3, "甲板固定"),
    HANG(4, "壁挂"),
    HOLD(5, "手持"),
    NO_HOLD(6, "非手持");

    private int code;
    private String msg;

    InstallEnums(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static InstallEnums getMsgWithCode(int code) {
        for (InstallEnums enums : InstallEnums.values()) {
            if (enums.getCode() == code) {
                return enums;
            }
        }

        return null;
    }
}
