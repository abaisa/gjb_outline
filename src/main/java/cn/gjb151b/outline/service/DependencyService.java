package cn.gjb151b.outline.service;

import cn.gjb151b.outline.Constants.ExceptionEnums;
import cn.gjb151b.outline.dao.ManageSysDevelopMapper;
import cn.gjb151b.outline.dao.ManageSysOutlineMapper;
import cn.gjb151b.outline.model.ManageSysDevelop;
import cn.gjb151b.outline.model.ManageSysOutline;
import cn.gjb151b.outline.utils.ServiceException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 页面数据依赖于上一个系统的数据，或依赖当前页面之前页面数据，在这里进行特殊的监听和处理
 * <p>
 * ManageSysOutlineMapper中有的方法可以直接用，否则新的查询写在DependencyMapper中
 */

@Service(value = "DependencyService")
public class DependencyService {

    @Resource
    private ManageSysOutlineMapper manageSysOutlineMapper;
    @Resource
    private ManageSysDevelopMapper manageSysDevelopMapper;

    public String generateDependencyData(int outlineId, int pageNumber, String data) {

        System.out.println(pageNumber);
        JSONObject jsonObject = JSON.parseObject(data);

        switch (pageNumber) {
            case 3:
                if (jsonObject.size() == 0) {
                    String outlineName = manageSysOutlineMapper.selectCol(outlineId, "outline_name");
                    jsonObject.put("任务名称", outlineName);
                }

        }

        return JSON.toJSONString(jsonObject);
    }

    public String getSubsysOrEqpHead(int outlineId) {
        ManageSysOutline manageSysOutline = manageSysOutlineMapper.selectByPrimaryKey(outlineId);
        String subsysEqpData = manageSysOutline.getOutlineDataSubsysEqp();
        JSONObject subsysEqpDataJson = JSON.parseObject(subsysEqpData);
        if (subsysEqpDataJson.size() == 0) {
            String subSysEqpName = manageSysOutline.getOutlineDevSubsysEqpName();
            String subSysEqpModel = manageSysOutline.getOutlineDevSubsysEqpModel();
            String subSysEqpNum = manageSysOutline.getOutlineDevSubsysEqpNum();
            if (Strings.isNullOrEmpty(subSysEqpName)) {
                subSysEqpName = "";
            }
            if (Strings.isNullOrEmpty(subSysEqpModel)) {
                subSysEqpModel = "";
            }
            if (Strings.isNullOrEmpty(subSysEqpNum)) {
                subSysEqpNum = "";
            }
            subsysEqpDataJson.put("分系统或设备名称", subSysEqpName);
            subsysEqpDataJson.put("型号", subSysEqpModel);
            subsysEqpDataJson.put("串号", subSysEqpNum);
        }

        Map<String, String> result = new HashMap<>();
        String schema = manageSysOutline.getOutlineSchemaSubsysEqp();
        if (Strings.isNullOrEmpty(schema)) {
            throw new ServiceException(ExceptionEnums.DB_EMPTY_ERR);
        }

        result.put("schema", schema);
        System.out.println(subsysEqpDataJson);
        System.out.println(JSON.toJSONString(subsysEqpDataJson));
        result.put("data", JSON.toJSONString(subsysEqpDataJson));

        return JSON.toJSONString(result);
    }

    public ManageSysDevelop getSysDevelopModelByDevItemId(String devItemId){
        return manageSysDevelopMapper.selectByPrimaryKey(devItemId);
    }
}
