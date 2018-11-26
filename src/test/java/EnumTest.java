import org.junit.Test;

public class EnumTest {
    @Test
    public void testManageSysDevelop() {
        System.out.println(">> test start");
        int code = DemoEnums.valueOf("YES").getValue();
        System.out.println(">> code is:  " + code);
        System.out.println(">> test finish");
    }
}

enum DemoEnums {
    NO(0),
    YES(1);

    private int status;
    DemoEnums(int _status){this.status = _status;}
    public int getValue() {return status;}
}
