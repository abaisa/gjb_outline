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

    DemoEnums(int status){
        this.setValue(status);
    }

    public int getValue() {
        return status;
    }

    private void setValue(int status) {
        this.status = status;
    }
}
