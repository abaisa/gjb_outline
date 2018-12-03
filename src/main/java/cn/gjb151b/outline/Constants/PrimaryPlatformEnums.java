package cn.gjb151b.outline.Constants;

import cn.gjb151b.outline.utils.ServiceException;

public enum PrimaryPlatformEnums {
    SurfaceVessel(1, "水面舰船"),
    Submarine(2, "潜艇"),
    ArmyAircraft(3, "陆军飞机"),
    NavalAircraft(4, "海军飞机"),
    AirForceAircraft(5, "空军飞机"),
    SpaceSystem(6, "空间系统"),
    ArmyGround(7, "陆军地面"),
    NavyGround(8, "海军地面"),
    AirForceGround(9, "空军地面");

    private int code;
    private String msg;
    PrimaryPlatformEnums(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static PrimaryPlatformEnums getMsgWithCode(int code) throws ServiceException {
        for(PrimaryPlatformEnums enums : PrimaryPlatformEnums.values()) {
            if(enums.getCode() == code) {
                return enums;
            }
        }
        throw new ServiceException(ExceptionEnums.ENUM_CODE_MISS_ERR);
    }
}
