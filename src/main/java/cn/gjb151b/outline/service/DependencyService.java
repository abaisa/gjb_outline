package cn.gjb151b.outline.service;

import cn.gjb151b.outline.Constants.*;
import cn.gjb151b.outline.dao.ManageSysDevelopMapper;
import cn.gjb151b.outline.dao.ManageSysOutlineMapper;
import cn.gjb151b.outline.model.ManageSysDevelop;
import cn.gjb151b.outline.model.ManageSysOutline;
import cn.gjb151b.outline.utils.ServiceException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
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

    public String generateDependencyData(int outlineId, int pageNumber, String data) throws Exception {

        System.out.println(pageNumber);
        JSONObject jsonData = JSON.parseObject(data);

        switch (pageNumber) {
            case 3:
                if (jsonData.size() == 0) {
                    String outlineName = manageSysOutlineMapper.selectCol(outlineId, "outline_name");
                    jsonData.put("任务名称", outlineName);
                }
                break;
            case 4:
                if (jsonData.size() == 0) {
                    // 这里是测试代码
                    ManageSysDevelop devObject = this.getSysDevelopModelByDevItemId("d51ef4b40e5049ccb64402fea308fd47");
//                    String name = devObject.getDevName();
//                    jsonObject.put("demoName", name);
//                    jsonData.put();

                }
                break;
            case 5:
                if (jsonData.size() == 0) {
                    ManageSysOutline outline = manageSysOutlineMapper.selectByPrimaryKey(outlineId);
                    String devItemId = outline.getOutlineDevItemid();
                    ManageSysDevelop develop = manageSysDevelopMapper.selectByPrimaryKey(devItemId);
                    jsonData = generateSubsysOrEqpAttrData(develop);
                }
                break;
            default:
                break;
        }

        return JSON.toJSONString(jsonData);
    }

    public String generateDependencySchema(int outlineId, int pageNumber, String schema) {
        JSONObject jsonSchema = JSON.parseObject(schema, Feature.OrderedField);

        switch (pageNumber) {
            case 10:
                ManageSysOutline outline = manageSysOutlineMapper.selectByPrimaryKey(outlineId);
                String devItemId = outline.getOutlineDevItemid();
                ManageSysDevelop develop = manageSysDevelopMapper.selectByPrimaryKey(devItemId);
                JSONObject jsonProperties = (JSONObject) jsonSchema.get("properties");
                if(develop.getDevPowerport() == 0) {
                    jsonProperties.remove("电源端口");
                }
                if(develop.getDevInterport() == 0) {
                    jsonProperties.remove("互联端口");
                }
                break;
            default:
                break;
        }

        return JSON.toJSONString(jsonSchema);
    }

    public JSONObject generateSubsysOrEqpAttrData(ManageSysDevelop develop) throws Exception {
        JSONObject jsonObject = new JSONObject();
        AttributeEnums attribute = AttributeEnums.getMsgWithCode(develop.getDevAttribute());
        if (attribute == null) {
            throw new ServiceException(ExceptionEnums.ENUM_CODE_MISS_ERR);
        } else {
            jsonObject.put("设备属性", attribute.getMsg());
        }

        IsEnums isKey = IsEnums.getMsgWithCode(develop.getDevKey());
        if (isKey == null) {
            throw new ServiceException(ExceptionEnums.ENUM_CODE_MISS_ERR);
        } else {
            jsonObject.put("任务关键设备", isKey.getMsg());
        }

        InstallEnums install = InstallEnums.getMsgWithCode(develop.getDevInstall());
        if (install == null) {
            throw new ServiceException(ExceptionEnums.ENUM_CODE_MISS_ERR);
        } else {
            jsonObject.put("安装方式", install.getMsg());
        }

        IsEnums haveGND = IsEnums.getMsgWithCode(develop.getDevGnd());
        if (haveGND == null) {
            throw new ServiceException(ExceptionEnums.ENUM_CODE_MISS_ERR);
        } else {
            jsonObject.put("带地线", haveGND.getMsg());
        }

        SpecialEnums special = SpecialEnums.getMsgWithCode(develop.getDevSpecial());
        if (special == null) {
            throw new ServiceException(ExceptionEnums.ENUM_CODE_MISS_ERR);
        } else {
            jsonObject.put("特殊设备", special.getMsg());
        }

        IsEnums isInterport = IsEnums.getMsgWithCode(develop.getDevInterport());
        if (isInterport == null) {
            throw new ServiceException(ExceptionEnums.ENUM_CODE_MISS_ERR);
        } else {
            jsonObject.put("互联端口", isInterport.getMsg());
        }

        IsEnums isLowsensitive = IsEnums.getMsgWithCode(develop.getDevLowsensitive());
        if (isInterport == null) {
            throw new ServiceException(ExceptionEnums.ENUM_CODE_MISS_ERR);
        } else {
            jsonObject.put("对低频信号敏感", isLowsensitive.getMsg());
        }

        JSONObject installPlatform = new JSONObject();

        HaveEnums haveEmp = HaveEnums.getMsgWithCode(develop.getDevEmp());
        if (haveEmp == null) {
            throw new ServiceException(ExceptionEnums.ENUM_CODE_MISS_ERR);
        } else {
            installPlatform.put("EMP加固措施", haveEmp.getMsg());
        }

        IsEnums isStatic = IsEnums.getMsgWithCode(develop.getDevStatic());
        if (isStatic == null) {
            throw new ServiceException(ExceptionEnums.ENUM_CODE_MISS_ERR);
        } else {
            installPlatform.put("静电威胁", isStatic.getMsg());
        }

        jsonObject.put("安装平台特性", installPlatform);

        return jsonObject;
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

    public ManageSysDevelop getSysDevelopModelByDevItemId(String devItemId) {
        try {
            ManageSysDevelop res = manageSysDevelopMapper.selectByPrimaryKey(devItemId);
            return res;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
