package cn.gjb151b.outline.utils;

import cn.gjb151b.outline.Constants.ExceptionEnums;
import com.alibaba.fastjson.JSON;


/**
 * Created by ddgdd on 2018/11/29 0029 9:05
 */
public class CommonUtils {

    /**
     * 检查页面回传参数pageInfo，合法返回true
     */
    public static void checkParamLegal(String jsonData) throws Exception {

        try {
            Object parseData = JSON.parse(jsonData);
        } catch (Exception e) {
            throw new ServiceException(ExceptionEnums.PARAM_DATA_PARSE_ERR);
        }

    }
}
