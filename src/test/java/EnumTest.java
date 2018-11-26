import cn.gjb151b.outline.Constants.ExceptionEnums;
import cn.gjb151b.outline.utils.ServiceException;
import org.junit.Test;

public class EnumTest {
    @Test
    public void testManageSysDevelop() throws ServiceException{
        System.out.println(">> test start");
        demoEnum e = demoEnum.getMsgWithCode(1);
        if (null == e) throw new ServiceException(ExceptionEnums.ENUM_CODE_MISS_ERR);
        String msg = e.getMsg();
        System.out.println(">> code is:  " + msg);
        System.out.println(">> test finish");
    }
}

enum demoEnum{
    NO(0, "不！"), YES(1, "可以！");
    private int code;
    private String msg;
    demoEnum(int _status, String _msg){
        this.code = _status;
        this.msg = _msg;
    }
    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public static demoEnum getMsgWithCode(int _code) {
        for (demoEnum e: demoEnum.values()) {
            if (e.getCode() == _code) {
                return e;
            }
        }
        return null;
    }
};

