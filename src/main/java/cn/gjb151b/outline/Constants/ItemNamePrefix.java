package cn.gjb151b.outline.Constants;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>
 *
 * </p>
 *
 * @Author: fcupup 875894948@qq.com
 * @Data: Created on 9:26 AM 2019/7/12
 * @Modified By:
 */
public class ItemNamePrefix {
    private static String itemNamePrefix;
    static {
        Properties properties = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(PathStoreEnum.WINDOWS_CONFIG_PATH.getValue()));
            properties.load(in);
            itemNamePrefix = new String(properties.getProperty("itemNamePrefix").getBytes("ISO8859-1"),"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getValue() {
        return itemNamePrefix;
    }
}
