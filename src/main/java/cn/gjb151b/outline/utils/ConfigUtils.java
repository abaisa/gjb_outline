package cn.gjb151b.outline.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by ddgdd on 2018/9/21 0021 9:51
 */

public class ConfigUtils {

    public static String getValueByKey(String filePath, String keyStr, String keyNum) {
        Properties properties = new Properties();
        try {
//            InputStream inputStream = new BufferedInputStream(new FileInputStream(filePath));
            InputStream inputStream = ConfigUtils.class.getClassLoader().getResourceAsStream(filePath);
            properties.load(new InputStreamReader(inputStream, "UTF-8"));

            StringBuilder stringBuilder = new StringBuilder(keyStr);
            stringBuilder.append(keyNum);

            String key = "" + stringBuilder;

            String value = properties.getProperty(key);

            return value;
        } catch (IOException e) {
            return null;
        }
    }
}
