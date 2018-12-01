import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

/**
 * Created by ddgdd on 2018/12/1 0001 14:57
 */
public class JsonTest {

    @Test
    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", "value1");
        jsonObject.put("key", "value2");

        System.out.println(JSON.toJSONString(jsonObject));
    }
}
