package cn.gjb151b.outline.service;

import cn.gjb151b.outline.Constants.*;
import cn.gjb151b.outline.dao.ManageSysDevelopMapper;
import cn.gjb151b.outline.dao.ManageSysOutlineMapper;
import cn.gjb151b.outline.model.ManageSysDevelop;
import cn.gjb151b.outline.model.ManageSysOutline;
import cn.gjb151b.outline.utils.ServiceException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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

        System.out.println("generateDependencyData Page ID >> " + pageNumber);

        JSONObject jsonObject;
        JSONArray jsonArray;
        String res;

        ManageSysOutline outline = manageSysOutlineMapper.selectByPrimaryKey(outlineId);
        String devItemId = outline.getOutlineDevItemid();
        ManageSysDevelop devObject = manageSysDevelopMapper.selectByPrimaryKey(devItemId);


        switch (pageNumber) {
            case 3:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    jsonObject.put("任务名称", outline.getOutlineName());
                }
                res = JSON.toJSONString(jsonObject);
                break;
            case 4:
                jsonObject = JSON.parseObject(data);
                String name = devObject.getDevName();
                jsonObject.put("任务名称", name);
                String subEqp = devObject.getDevSubsysEqp() == 1 ? "分系统": "设备";
                String devPrimaryPlatform = devObject.getDevPrimaryPlatform() == 1?"水面舰船":"其他的一级平台";
                jsonObject.put("预定使用平台", devPrimaryPlatform);
                jsonObject.put("分系统/设备", subEqp);
                String subsysEqpName = devObject.getDevSubsysEqpName();
                jsonObject.put("分系统/设备名称", subsysEqpName);
                String subsysEqpModel = devObject.getDevSubsysEqpModel();
                jsonObject.put("型号", subsysEqpModel);
                String subsysEqpNum = devObject.getDevSubsysEqpNum();
                jsonObject.put("串号", subsysEqpNum);
                String supplier = devObject.getDevSupplier();
                jsonObject.put("承制单位", supplier);

                res = JSON.toJSONString(jsonObject);
                break;
            case 5:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    jsonObject = generateSubsysOrEqpAttrData(devObject);
                }
                res = JSON.toJSONString(jsonObject);
                break;
            case 1001:
                jsonArray = new JSONArray();
                // 解析所有dev系统中的频段并存入 本项不填则用 - 表示
                int order = 0;
                JSONObject oneLineObject;

                String devFreqOptional = devObject.getDevFreqOptional();
                JSONArray devFreqOptionalArray = JSON.parseArray(devFreqOptional);
                for(Object devFreqOptionalArrayObject: devFreqOptionalArray) {
                    JSONObject devFreqOptionalArrayJSONObject = (JSONObject)devFreqOptionalArrayObject;
                    oneLineObject = new JSONObject();
                    oneLineObject.put("状态序号", String.valueOf(order));
                    oneLineObject.put("用频方式", "固定");
                    oneLineObject.put("最低频率", devFreqOptionalArrayJSONObject.get("opt_freq_low"));
                    oneLineObject.put("中间频率", devFreqOptionalArrayJSONObject.get("opt_freq_mid"));
                    oneLineObject.put("最高频率", devFreqOptionalArrayJSONObject.get("opt_freq_high"));
                    oneLineObject.put("最高传输速率", "--");
                    oneLineObject.put("工作方式", devFreqOptionalArrayJSONObject.get("opt_work_style"));
                    oneLineObject.put("最大发射功率", devFreqOptionalArrayJSONObject.get("opt_ave_pow_transmit_max"));
                    oneLineObject.put("调制方式", "待定，第一个系统里可能要改");
                    jsonArray.add(oneLineObject);
                    order += 1;
                }


                String devFreqFhLow = devObject.getDevFreqFhLow();
                JSONObject devFreqFhLowJSONObject = JSON.parseObject(devFreqFhLow);
                oneLineObject = new JSONObject();
                oneLineObject.put("状态序号", String.valueOf(order));
                oneLineObject.put("用频方式", "跳频低频段");
                oneLineObject.put("最低频率", devFreqFhLowJSONObject.get("freq_low"));
                oneLineObject.put("中间频率", "--");
                oneLineObject.put("最高频率", devFreqFhLowJSONObject.get("freq_high"));
                oneLineObject.put("最高传输速率", "--");
                oneLineObject.put("工作方式", devFreqFhLowJSONObject.get("work_style"));
                oneLineObject.put("最大发射功率", devFreqFhLowJSONObject.get("ave_pow_transmit_max"));
                oneLineObject.put("调制方式", "--");
                jsonArray.add(oneLineObject);
                order += 1;

                String devFreqFhMid = devObject.getDevFreqFhMid();
                JSONObject devFreqFhMidJSONObject = JSON.parseObject(devFreqFhMid);
                oneLineObject = new JSONObject();
                oneLineObject.put("状态序号", String.valueOf(order));
                oneLineObject.put("用频方式", "跳频中频段");
                oneLineObject.put("最低频率", devFreqFhMidJSONObject.get("freq_low"));
                oneLineObject.put("中间频率", "--");
                oneLineObject.put("最高频率", devFreqFhMidJSONObject.get("freq_high"));
                oneLineObject.put("最高传输速率", "--");
                oneLineObject.put("工作方式", devFreqFhMidJSONObject.get("work_style"));
                oneLineObject.put("最大发射功率", devFreqFhMidJSONObject.get("ave_pow_transmit_max"));
                oneLineObject.put("调制方式", "--");
                jsonArray.add(oneLineObject);
                order += 1;

                String devFreqFhHigh = devObject.getDevFreqFhHigh();
                JSONObject devFreqFhHighJSONObject = JSON.parseObject(devFreqFhHigh);
                oneLineObject = new JSONObject();
                oneLineObject.put("状态序号", String.valueOf(order));
                oneLineObject.put("用频方式", "跳频高频段");
                oneLineObject.put("最低频率", devFreqFhHighJSONObject.get("freq_low"));
                oneLineObject.put("中间频率", "--");
                oneLineObject.put("最高频率", devFreqFhHighJSONObject.get("freq_high"));
                oneLineObject.put("最高传输速率", "--");
                oneLineObject.put("工作方式", devFreqFhHighJSONObject.get("work_style"));
                oneLineObject.put("最大发射功率", devFreqFhHighJSONObject.get("ave_pow_transmit_max"));
                oneLineObject.put("调制方式", "--");
                jsonArray.add(oneLineObject);
                order += 1;

                String devFreqDsss = devObject.getDevFreqDsss();
                JSONObject devFreqDsssJSONObject = JSON.parseObject(devFreqDsss);
                oneLineObject = new JSONObject();
                oneLineObject.put("状态序号", String.valueOf(order));
                oneLineObject.put("用频方式", "直序扩频");
                oneLineObject.put("最低频率", "--");
                oneLineObject.put("中间频率", "--");
                oneLineObject.put("最高频率", "--");
                oneLineObject.put("最高传输速率", devFreqDsssJSONObject.get("trans_rate_max"));
                oneLineObject.put("工作方式", devFreqDsssJSONObject.get("work_style"));
                oneLineObject.put("最大发射功率", devFreqDsssJSONObject.get("ave_pow_transmit_max"));
                oneLineObject.put("调制方式", "--");
                jsonArray.add(oneLineObject);
                order += 1;

                res = JSON.toJSONString(jsonArray);
                break;
            default:
                res = data;
                break;
        }

        return res;
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

    public void submitSubsysOrEqpHead(int outlineId, String subSysOrEqpData) throws Exception{
        manageSysOutlineMapper.updateCol(outlineId, "outline_data_subsys_eqp", subSysOrEqpData);
    }

}
