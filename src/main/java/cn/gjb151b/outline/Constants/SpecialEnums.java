package cn.gjb151b.outline.Constants;

/**
 * Created by ddgdd on 2018/11/26 0026 14:29
 */

/*
dev_install 安装方式 1代表台式 2代表机架安装 3代表甲板固定 4代表壁挂 5代表手持 6非手持
 */
public enum SpecialEnums {

    SHIP_AND_SUBMARINES(1, "（水面舰船和潜艇平台）工作频率低于100kHz，灵敏度优于1V"),
    ARMY_GROUND(2, "（陆军地面）扫雷和探雷的机动车辆"),
    NAVY_AIRCRAFT(3, "（海军飞机）反潜机"),
    NO_SPECIAL(0, "无");


    private int code;
    private String msg;

    SpecialEnums(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static SpecialEnums getMsgWithCode(int code) {
        for (SpecialEnums enums : SpecialEnums.values()) {
            if (enums.getCode() == code) {
                return enums;
            }
        }

        return null;
    }
}
