package cn.gjb151b.outline.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonByPositon {
    public String jsonUpdate(String data, String equipmentName, int[] nums, String keyName, String bulidAppend) {
        JSONObject jsonObject = JSON.parseObject(data);
        JSONArray index = jsonObject.getJSONArray(equipmentName);
        for (int i : nums) {
            index.getJSONObject(i).put(keyName, bulidAppend);
        }
        jsonObject.put(equipmentName, index);
        return jsonObject.toJSONString();
    }
}
