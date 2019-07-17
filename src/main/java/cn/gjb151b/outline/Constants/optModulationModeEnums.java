package cn.gjb151b.outline.Constants;

/**
 * Created by teng on 2019/07/12  14:32
 * opt_modulation_mode 系统一中的调制属性，1代表调幅，2代表调频
 */

public enum optModulationModeEnums {

    MODULATION_MODE_AM("1", "调幅"),
    MODULATION_MODE_FM("2", "调频");

    private String code;
    private String msg;
    optModulationModeEnums(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {return code;}

    public String getMsg() {return msg;}

    public static String getMsgWithCode(String code) {
        for(optModulationModeEnums enums : optModulationModeEnums.values()) {
            if(enums.getCode().equals(code)) {
                return enums.getMsg();
            }
        }

        return null;
    }


}
