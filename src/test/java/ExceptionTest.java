import cn.gjb151b.outline.utils.CommonUtils;
import cn.gjb151b.outline.utils.ServiceException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

    @Test
    public void testFastJson() {
        JSONArray jsonArray;
        jsonArray = JSON.parseArray("[{\"状态序号\":0,\"工作频率\":\"\",\"直序序列最高传输速率\":\"\",\"设备所处状态\":\"\",\"最大发射平均功率\":\"\",\"调制方式\":\"\"},{\"状态序号\":1,\"工作频率\":\"\",\"直序序列最高传输速率\":\"\",\"设备所处状态\":\"\",\"最大发射平均功率\":\"\",\"调制方式\":\"\"}]");
        System.out.println(jsonArray.size());
        for(Object o: jsonArray) {
            JSONObject jo = (JSONObject)o;
        }
        JSONObject jo = (JSONObject) jsonArray.get(0);
        System.out.println(jo);
        jo.put("最大发射平均功率", "1212121");
        System.out.println(jo);

        String s = JSON.toJSONString(jsonArray);
        System.out.println(">>> " + s);
    }
}
