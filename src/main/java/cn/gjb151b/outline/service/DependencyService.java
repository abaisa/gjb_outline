package cn.gjb151b.outline.service;

import cn.gjb151b.outline.dao.ManageSysOutlineMapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 页面数据依赖于上一个系统的数据，或依赖当前页面之前页面数据，在这里进行特殊的监听和处理
 *
 * ManageSysOutlineMapper中有的方法可以直接用，否则新的查询写在DependencyMapper中
 */

@Service(value = "DependencyService")
public class DependencyService {

    @Resource
    private ManageSysOutlineMapper manageSysOutlineMapper;

    public String generateDependencyData(int outlineId, int pageNumber, String data) {

        System.out.println(pageNumber);
        JSONObject jsonObject = JSON.parseObject(data);

        switch (pageNumber) {
            case 3:
                if(jsonObject.size() == 0) {
                    String outlineName = manageSysOutlineMapper.selectCol(outlineId, "outline_name");
                    jsonObject.put("任务名称", outlineName);
                }

        }

        return JSON.toJSONString(jsonObject);
    }
}
