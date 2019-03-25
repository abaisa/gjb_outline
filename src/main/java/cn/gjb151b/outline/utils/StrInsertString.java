package cn.gjb151b.outline.utils;

import java.util.ArrayList;
import java.util.List;

public class StrInsertString {
    public String strInsertString(String devObject, String str) {
        List<Integer> indexStr = new ArrayList<>();
        int length = 4;
        StringBuilder stringBuilder = new StringBuilder(devObject);
        for (int i = 0; i < devObject.length() - length; i++) {
            String curString = stringBuilder.substring(i, i + length);
            if (curString.equals("频率范围")) {
                indexStr.add(i);

            }
        }
        System.out.println(indexStr.size());

        for (int i = 0; i < indexStr.size(); i++) {
            stringBuilder.insert(indexStr.get(i) + i * 5 + 5, str);
        }
        return stringBuilder.toString();
    }
}
