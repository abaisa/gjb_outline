import cn.gjb151b.outline.utils.CommonUtils;
import cn.gjb151b.outline.utils.ServiceException;
import org.junit.Test;

import static cn.gjb151b.outline.utils.CommonUtils.checkParamLegal;

/**
 * Created by ddgdd on 2018/11/29 0029 9:24
 */
public class ExceptionTest {

    @Test
    public void testServiceExcepion() {

        boolean result = false;
        try {
            checkParamLegal("{1");
        } catch (ServiceException e) {
            System.out.println(e.getExceptionEnums().getErrMsg());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(result);
    }
}
