package cn.gjb151b.outline.utils;

import java.util.UUID;

/**
 * Created by ddgdd on 2018/10/23 0023 15:43
 */
public class UUIDUtils {

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr = str.replace("-", "");

        return uuidStr;
    }

}
