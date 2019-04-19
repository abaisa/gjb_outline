package cn.gjb151b.outline.service;

import cn.gjb151b.outline.utils.ConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class StandardLimitValueService {
    public double[] getImgDetails(List<String> imgNumList) {
        if (imgNumList == null) return new double[2];
        List<Double> limitValueRangeList = new ArrayList<>();
        double[] limitValueRangeRes = new double[2];
        for (String imgNum : imgNumList) {
            String imgMinX = ConfigUtils.getValueByKey("limitValues.properties", "MINX_", imgNum);
            String imgMaxX = ConfigUtils.getValueByKey("limitValues.properties", "MAXX_", imgNum);
            limitValueRangeList.add(Double.valueOf(imgMinX));
            limitValueRangeList.add(Double.valueOf(imgMaxX));
        }
        Collections.sort(limitValueRangeList);
        limitValueRangeRes[0] = limitValueRangeList.get(0);
        limitValueRangeRes[1] = limitValueRangeList.get(limitValueRangeList.size() - 1);
        return limitValueRangeRes;

    }
}
