package cn.gjb151b.outline.service;

import cn.gjb151b.outline.Constants.*;
import cn.gjb151b.outline.dao.ManageSysDevelopMapper;
import cn.gjb151b.outline.outlineDao.ManageSysOutlineMapper;
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
        JSONArray jsonArray = new JSONArray();
        String resultData;

        ManageSysOutline outline = manageSysOutlineMapper.selectByPrimaryKey(outlineId);
        String devItemId = outline.getOutlineDevItemid();
        ManageSysDevelop devObject = manageSysDevelopMapper.selectByPrimaryKey(devItemId);


        switch (pageNumber) {
            case 3:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    jsonObject.put("任务名称", outline.getOutlineName());
                }
                resultData = JSON.toJSONString(jsonObject);
                break;
            case 4:
                jsonObject = JSON.parseObject(data);
                String name = devObject.getDevName();
                jsonObject.put("任务名称", name);
                String subEqp = SubSysEnums.getMsgWithCode(devObject.getDevSubsysEqp()).getMsg();
                String devPrimaryPlatform = PrimaryPlatformEnums.getMsgWithCode(devObject.getDevPrimaryPlatform()).getMsg();
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

                resultData = JSON.toJSONString(jsonObject);
                break;
            case 5:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    jsonObject = generateSubsysOrEqpAttrData(devObject);
                }
                resultData = JSON.toJSONString(jsonObject);
                break;

            case 1001:
                resultData = generateFreqDependency(devObject, 0);
                break;
            case 1002:
                resultData = generateFreqDependency(devObject, 1);
                break;
            case 1003:
                resultData = generateFreqDependency(devObject, 0);
                break;
            case 1004:
                resultData = generateFreqDependency(devObject,2);
                break;
            case 1005:
                resultData = generateFreqDependency(devObject, 1);
                break;
            case 14:
//                jsonObject = JSON.parseObject(data);
                String devCE101 = devObject.getDevCe101();
                resultData = generateLimitPic(data, devCE101);
//                JSONObject devCE101Object = JSON.parseObject(devCE101);
//                String limitValue = devCE101Object.getJSONObject("limit_value").getString("pic");
//                String limitValueCurrent = devCE101Object.getJSONObject("limit_value_current").getString("pic");
//                StringBuilder builder = new StringBuilder();
//                if(limitValue.equals(limitValueCurrent)) {
//                    builder.append("GJB151B-2013标准规定图形：");
//                    builder.append(limitValue);
//                    jsonObject.put("限值", builder.toString());
//                }else {
//                    builder.append("研制要求管理系统生成图形：");
//                    builder.append(limitValueCurrent);
//                    jsonObject.put("限值", builder.toString());
//                }
//
//                resultData = JSON.toJSONString(jsonObject);
                break;
            case 15:
                String devCE102 = devObject.getDevCe102();
                resultData = generateLimitPic(data, devCE102);
                break;
            case 16:
                String devCE106 = devObject.getDevCe106();
                resultData = generateAntennaData(devObject,data);
                resultData = generateLimitText(resultData, devCE106);
                break;
            case 17:
                String devCE107 = devObject.getDevCe107();
                resultData = generateLimitText(data, devCE107);
                break;
            case 18:
                String devCS101 = devObject.getDevCs101();
                resultData = generateLimitTwoPic(data, devCS101);
                break;
            case 19:
                String devCS102 = devObject.getDevCs102();
                resultData = generateLimitText(data, devCS102);
                break;
            case 20:
                resultData = generateAntennaData(devObject,data);
                break;
            case 21:
                resultData = generateAntennaData(devObject,data);
                break;
            case 22:
                resultData = generateAntennaData(devObject,data);
                break;
            case 23:
                String devCS106 = devObject.getDevCs106();
                resultData = generateLimitText(data, devCS106);
                break;
            case 24:
                String devCS109 = devObject.getDevCs109();
                resultData = generateLimitPic(data, devCS109);
                break;
            case 25:
                String devCS112 = devObject.getDevCs112();
                resultData = generateLimitText(data, devCS112);
                break;
            case 26:
                String devCS114 = devObject.getDevCs114();
                resultData = generateLimitPic(data, devCS114);
                break;
            case 27:
                String devCS115 = devObject.getDevCs115();
                resultData = generateLimitTextAndPic(data, devCS115);
                break;
            case 28:
                String devCS116 = devObject.getDevCs116();
                resultData = generateLimitTextAndPic(data, devCS116);
                break;
            case 29:
                String devRE101 = devObject.getDevRe101();
                resultData = generateLimitPic(data, devRE101);
                break;
            case 30:
                String devRE102 = devObject.getDevRe102();
                data = generateLimitPic(data, devRE102);
                resultData = data;
                break;
            case 31:
                String devRE103 = devObject.getDevRe103();
                resultData = generateLimitText(data, devRE103);
                break;
            case 32:
                String devRS101 = devObject.getDevRs101();
                resultData = generateLimitPic(data, devRS101);
                break;
            case 33:
                String devRS103 = devObject.getDevRs103();
                resultData = generateLimitPic(data, devRS103);
                break;
            case 34:
                String devRS105 = devObject.getDevRs105();
                resultData = generateLimitPic(data, devRS105);
//                resultData = data;
                System.out.println("resultData:"+resultData);
                break;

            default:
                resultData = data;
                break;
        }

        return resultData;
    }

    public void generateDataAfterSubmit(int outlineId, int pageNumber, String data) {
        JSONObject jsonObject;
        JSONArray jsonArray;

        ManageSysOutline outline = manageSysOutlineMapper.selectByPrimaryKey(outlineId);
        switch (pageNumber) {
            case 10:
                //第10页的电源端口数据控制着第14页的试验端口即被试品工作状态的数据
                jsonObject = JSON.parseObject(data);
                JSONArray powerPortArray = jsonObject.getJSONArray("电源端口");
                JSONArray interPortArray = jsonObject.getJSONArray("互联端口");

                JSONArray testPortArray = new JSONArray(); //外部电源输入 试验端口的列表数组
                JSONArray allPowerArray = new JSONArray(); //电源输入端口，包括内部电源输入端口
                JSONObject allPortObject = new JSONObject(); //所有端口，包括电源端口和互联端口
                JSONArray powerArray = new JSONArray(); //所有电源端口名称
                JSONArray interArray = new JSONArray(); //所有互联端口名称
                String outlineData14 = outline.getOutlineData14();
                String outlineData15 = outline.getOutlineData15();
                String outlineData17 = outline.getOutlineData17();
                String outlineData18 = outline.getOutlineData18();
                String outlineData23 = outline.getOutlineData23();
                String outlineData26 = outline.getOutlineData26();
                String outlineData27 = outline.getOutlineData27();
                String outlineData28 = outline.getOutlineData28();
                JSONObject outlineData14Object = JSON.parseObject(outlineData14);
                JSONObject outlineData15Object = JSON.parseObject(outlineData15);
                JSONObject outlineData17Object = JSON.parseObject(outlineData17);
                JSONObject outlineData18Object = JSON.parseObject(outlineData18);
                JSONObject outlineData23Object = JSON.parseObject(outlineData23);
                JSONObject outlineData26Object = JSON.parseObject(outlineData26);
                JSONObject outlineData27Object = JSON.parseObject(outlineData27);
                JSONObject outlineData28Object = JSON.parseObject(outlineData28);
                for(int i = 0; i < powerPortArray.size(); i++) {
                    JSONObject powerPort = powerPortArray.getJSONObject(i);
                    if(powerPort.get("外部电源供电").equals("是") && powerPort.get("输入/输出").equals("输入")) {
                        JSONObject testPortObject = new JSONObject();
                        testPortObject.put("试验端口", powerPort.get("端口名称或代号"));
                        testPortArray.add(testPortObject);
                    }
                    if(powerPort.get("输入/输出").equals("输入")){
                        JSONObject allPowerObject = new JSONObject();
                        allPowerObject.put("试验电源端口", powerPort.get("端口名称或代号"));
                        allPowerArray.add(allPowerObject);
                    }
                    JSONObject singlePower = new JSONObject();
                    singlePower.put("电源端口",powerPort.get("端口名称或代号"));
                    powerArray.add(singlePower);

                }
                for(int i = 0; i < interPortArray.size(); i++){
                    JSONObject interPort = interPortArray.getJSONObject(i);
                    JSONObject singleInter = new JSONObject();
                    singleInter.put("互联端口", interPort.get("端口名称或代号"));
                    interArray.add(singleInter);
                }
                allPortObject.put("电源端口",powerArray);
                allPortObject.put("互联端口",interArray);
                outlineData14Object.put("试验端口及被试品工作状态", testPortArray);
                outlineData15Object.put("试验端口及被试品工作状态", testPortArray);
                outlineData17Object.put("试验端口及被试品工作状态", allPowerArray);
                outlineData18Object.put("试验端口及被试品工作状态", testPortArray);
                outlineData23Object.put("试验端口及被试品工作状态", testPortArray);
                outlineData26Object.put("试验端口及被试品工作状态", allPortObject);
                outlineData27Object.put("试验端口及被试品工作状态", allPortObject);
                outlineData28Object.put("试验端口及被试品工作状态", allPortObject);
                System.out.println("试验端口及被试品工作状态:"+JSON.toJSONString(outlineData26Object));
                manageSysOutlineMapper.updateCol(outlineId, "outline_data_14", JSON.toJSONString(outlineData14Object));
                manageSysOutlineMapper.updateCol(outlineId, "outline_data_15", JSON.toJSONString(outlineData15Object));
                manageSysOutlineMapper.updateCol(outlineId, "outline_data_17", JSON.toJSONString(outlineData17Object));
                manageSysOutlineMapper.updateCol(outlineId, "outline_data_18", JSON.toJSONString(outlineData18Object));
                manageSysOutlineMapper.updateCol(outlineId, "outline_data_23", JSON.toJSONString(outlineData23Object));
                manageSysOutlineMapper.updateCol(outlineId, "outline_data_26", JSON.toJSONString(outlineData26Object));
                manageSysOutlineMapper.updateCol(outlineId, "outline_data_27", JSON.toJSONString(outlineData27Object));
                manageSysOutlineMapper.updateCol(outlineId, "outline_data_28", JSON.toJSONString(outlineData28Object));

                break;
            default:
                break;
        }
    }

    public String generateAntennaData(ManageSysDevelop devObject, String data){
        JSONObject jsonObject;
        JSONArray jsonArray = new JSONArray();
        jsonObject = JSON.parseObject(data);
        JSONObject antennaObject = new JSONObject();
        antennaObject.put("天线端口", "天线端口1");
        Integer devAntenna = devObject.getDevAntenna();
        Integer devReceiveLaunch = devObject.getDevReceiveLaunch();
        Integer devModulation = devObject.getDevModulation();
        System.out.println("devAntenna:"+devAntenna);
        System.out.println("devReceiveLaunch:"+devReceiveLaunch);
        System.out.println("devModulation:"+devModulation);
        String devString = "";
        String antennaString = "";
        String receiveString = "";
        String modulationString = "";
        if(devAntenna != null) {
            if (devAntenna == 0) {
                antennaString = "无天线端口";
            } else if (devAntenna == 1) {
                antennaString = "天线端口可拆卸";
            } else if (devAntenna == 2) {
                antennaString = "天线端口不可拆卸";
            }
        }
        if(devReceiveLaunch != null) {
            if (devReceiveLaunch == 1) {
                receiveString = "发射";
            } else if (devReceiveLaunch == 2) {
                receiveString = "接收";
            } else if (devReceiveLaunch == 3) {
                receiveString = "收/发";
            }
        }
        if(devModulation != null) {
            if (devModulation == 1) {
                modulationString = "调幅";
            } else if (devModulation == 2) {
                modulationString = "非调幅";
            }
        }
        devString = "天线端口:"+antennaString+"; 天线端口模式:"+receiveString+"; 天线调制模式:"+modulationString;
        antennaObject.put("工作状态", devString);
        jsonArray.add(antennaObject);
        jsonObject.put("试验端口及被试品工作状态", jsonArray);
        String resultData = JSON.toJSONString(jsonObject);
        return resultData;

    }


    public String generateLimitPic(String data, String devProject){
        JSONObject jsonObject;
        jsonObject = JSON.parseObject(data);
        JSONObject devJsonProject = JSON.parseObject(devProject);
        String limitValue = devJsonProject.getJSONObject("limit_value").getString("pic");
        String limitValueCurrent = devJsonProject.getJSONObject("limit_value_current").getString("pic");
        StringBuilder builder = new StringBuilder();
        if(limitValue.equals(limitValueCurrent)) {
            builder.append("GJB151B-2013标准规定图形:standard");
            builder.append(limitValue);
            jsonObject.put("限值", builder.toString());
        }else {
            builder.append("研制要求管理系统生成图形:");
            builder.append(limitValueCurrent);
            jsonObject.put("限值", builder.toString());
        }

        String resultData = JSON.toJSONString(jsonObject);
        return resultData;
    }

    public String generateLimitTwoPic(String data, String devProject){
        JSONObject jsonObject;
        jsonObject = JSON.parseObject(data);
        JSONObject devJsonProject = JSON.parseObject(devProject);
        JSONObject limitValue = devJsonProject.getJSONObject("limit_value");
        JSONObject limitValueCurrent = devJsonProject.getJSONObject("limit_value_current");
        String pic1 = limitValue.getString("pic_one");
        String pic2 = limitValue.getString("pic_two");
        String picCurrent1 = limitValueCurrent.getString("pic_one");
        String picCurrent2 = limitValueCurrent.getString("pic_two");
        StringBuilder builder = new StringBuilder();
        if(pic1.equals(picCurrent1)) {
            builder.append("GJB151B-2013标准规定图形:standard");
            builder.append(pic1);
        }else{
            builder.append("研制要求管理系统生成图形:");
            builder.append(pic1);
        }
        if(pic2.equals(picCurrent2)){
            builder.append("; GJB151B-2013标准规定图形:standard");
            builder.append(pic2);
        }else{
            builder.append("研制要求管理系统生成图形:");
            builder.append(pic2);
        }
        jsonObject.put("限值", builder.toString());
        String resultData = JSON.toJSONString(jsonObject);
        return resultData;
    }


    public String generateLimitText(String data, String devProject){
        JSONObject jsonObject;
        jsonObject = JSON.parseObject(data);
        JSONObject devJsonProject = JSON.parseObject(devProject);
        jsonObject.put("限值", devJsonProject.getJSONObject("limit_value_current").getString("text"));
        String resultData = JSON.toJSONString(jsonObject);
        return resultData;
    }

    public String generateLimitTextAndPic(String data, String devProject){
        JSONObject jsonObject;
        jsonObject = JSON.parseObject(data);
        JSONObject devJsonProject = JSON.parseObject(devProject);
        String pic = devJsonProject.getJSONObject("limit_value").getString("pic");
        String picCurrent = devJsonProject.getJSONObject("limit_value_current").getString("pic");
        String textCurrent = devJsonProject.getJSONObject("limit_value_current").getString("text");
        StringBuilder builder = new StringBuilder();
        if(pic.equals(picCurrent)) {
            builder.append("GJB151B-2013标准规定图形:standard");
            builder.append(pic);
            builder.append("\n");
        }else {
            builder.append("研制要求管理系统生成图形:");
            builder.append(picCurrent);
            builder.append("\n");
        }
        builder.append(textCurrent);
        jsonObject.put("限值", builder.toString());
        String resultData = JSON.toJSONString(jsonObject);
        return resultData;
    }



    public String generateDependencySchema(int outlineId, int pageNumber, String schema) {
        JSONObject jsonSchema = JSON.parseObject(schema, Feature.OrderedField);

        switch (pageNumber) {
            case 10:
                ManageSysOutline outline = manageSysOutlineMapper.selectByPrimaryKey(outlineId);
                String devItemId = outline.getOutlineDevItemid();
                ManageSysDevelop develop = manageSysDevelopMapper.selectByPrimaryKey(devItemId);
                JSONArray powerPortList = (JSONArray) JSON.parse(develop.getDevPowerport());
                JSONObject jsonProperties = (JSONObject) jsonSchema.get("properties");
                if (powerPortList.size() == 0 || noPortSelect(powerPortList) == false) {
                    jsonProperties.remove("电源端口");
                }
                if (develop.getDevInterport() == 0) {
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

    public void submitSubsysOrEqpHead(int outlineId, String subSysOrEqpData) throws Exception {
        manageSysOutlineMapper.updateCol(outlineId, "outline_data_subsys_eqp", subSysOrEqpData);
    }

    /**
     * 这块现在还很迷 mode 0 为收发共用选发 1 为收发共用选收发共用 2 收发共用选收和发
     */
    private String generateFreqDependency(ManageSysDevelop devObject, int mode) {
        JSONArray freqResArray = new JSONArray();
        // 解析所有dev系统中的频段并存入 本项不填则用 - 表示
        int order = 0;
        JSONObject oneLineObject;

        String devFreqOptional = devObject.getDevFreqOptional();
        JSONArray devFreqOptionalArray = JSON.parseArray(devFreqOptional);

        String workstyle;
        for (Object devFreqOptionalArrayObject : devFreqOptionalArray) {
            JSONObject devFreqOptionalArrayJSONObject = (JSONObject) devFreqOptionalArrayObject;
            oneLineObject = new JSONObject();
            oneLineObject.put("状态序号", String.valueOf(order));
            oneLineObject.put("用频方式", "固定");
            oneLineObject.put("最低频率", devFreqOptionalArrayJSONObject.get("opt_freq_low"));
            oneLineObject.put("中间频率", devFreqOptionalArrayJSONObject.get("opt_freq_mid"));
            oneLineObject.put("最高频率", devFreqOptionalArrayJSONObject.get("opt_freq_high"));
            oneLineObject.put("最高传输速率", "--");
            oneLineObject.put("最大发射功率", devFreqOptionalArrayJSONObject.get("opt_ave_pow_transmit_max"));
            workstyle = (String)devFreqOptionalArrayJSONObject.get("opt_work_style");
            if (mode == 0 && workstyle.equals("3")) workstyle = "2";
            if (mode == 2 && workstyle.equals("3")) {
                workstyle = "1";
                oneLineObject.put("工作方式", WorkStyleEnums.getMsgWithCode(workstyle).getMsg());
                JSONObject modulationMode = (JSONObject)devFreqOptionalArrayJSONObject.get("opt_modulation_mode_num");
                String optModulationMode1 = (String)modulationMode.get("opt_modulation_mode_1");
                if(!Strings.isNullOrEmpty(optModulationMode1)){
                    oneLineObject.put("调制方式", optModulationMode1);
                    freqResArray.add(oneLineObject.clone());
                    order += 1;
                }
                String optModulationMode2 = (String)modulationMode.get("opt_modulation_mode_2");
                if(!Strings.isNullOrEmpty(optModulationMode2)){
                    oneLineObject.put("调制方式", optModulationMode2);
                    oneLineObject.put("状态序号", String.valueOf(order));
                    freqResArray.add(oneLineObject.clone());
                    order += 1;
                }
                workstyle = "2";
            }
            oneLineObject.put("工作方式", WorkStyleEnums.getMsgWithCode(workstyle).getMsg());
            // 调制方式，第一个系统如果有修改这里也要调整  oneLineObject.put("调制方式", "待定，第一个系统里可能要改");
            // 注意，这个第一个系统中可能是要改的，第一个系统整理好了之后再补后面的，这里先给固定频率的代码
            JSONObject modulationMode = (JSONObject)devFreqOptionalArrayJSONObject.get("opt_modulation_mode_num");
            String optModulationMode1 = (String)modulationMode.get("opt_modulation_mode_1");
            if(!Strings.isNullOrEmpty(optModulationMode1)){
                oneLineObject.put("调制方式", optModulationMode1);
                freqResArray.add(oneLineObject.clone());
                order += 1;
            }
            String optModulationMode2 = (String)modulationMode.get("opt_modulation_mode_2");
            if(!Strings.isNullOrEmpty(optModulationMode2)){
                oneLineObject.put("调制方式", optModulationMode2);
                oneLineObject.put("状态序号", String.valueOf(order));
                freqResArray.add(oneLineObject.clone());
                order += 1;
            }
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
        oneLineObject.put("最大发射功率", devFreqFhLowJSONObject.get("ave_pow_transmit_max"));
        oneLineObject.put("调制方式", "--");
        workstyle = (String)devFreqFhLowJSONObject.get("work_style");
        if (mode == 0 && workstyle.equals("3")) workstyle = "2";
        if (mode == 2 && workstyle.equals("3")) {
            workstyle = "1";
            oneLineObject.put("工作方式", WorkStyleEnums.getMsgWithCode(workstyle).getMsg());
            freqResArray.add(oneLineObject);
            order += 1;
            workstyle = "2";
        }
        oneLineObject.put("状态序号", String.valueOf(order));
        oneLineObject.put("工作方式", WorkStyleEnums.getMsgWithCode(workstyle).getMsg());
        freqResArray.add(oneLineObject);
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
        oneLineObject.put("最大发射功率", devFreqFhMidJSONObject.get("ave_pow_transmit_max"));
        oneLineObject.put("调制方式", "--");
        workstyle = (String)devFreqFhMidJSONObject.get("work_style");
        if (mode == 0 && workstyle.equals("3")) workstyle = "2";
        if (mode == 2 && workstyle.equals("3")) {
            workstyle = "1";
            oneLineObject.put("工作方式", WorkStyleEnums.getMsgWithCode(workstyle).getMsg());
            freqResArray.add(oneLineObject);
            order += 1;
            workstyle = "2";
        }
        oneLineObject.put("状态序号", String.valueOf(order));
        oneLineObject.put("工作方式", WorkStyleEnums.getMsgWithCode(workstyle).getMsg());
        freqResArray.add(oneLineObject);
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
        oneLineObject.put("最大发射功率", devFreqFhHighJSONObject.get("ave_pow_transmit_max"));
        oneLineObject.put("调制方式", "--");
        workstyle = (String)devFreqFhHighJSONObject.get("work_style");
        if (mode == 0 && workstyle.equals("3")) workstyle = "2";
        if (mode == 2 && workstyle.equals("3")) {
            workstyle = "1";
            oneLineObject.put("工作方式", WorkStyleEnums.getMsgWithCode(workstyle).getMsg());
            freqResArray.add(oneLineObject);
            order += 1;
            workstyle = "2";
        }
        oneLineObject.put("工作方式", WorkStyleEnums.getMsgWithCode(workstyle).getMsg());
        oneLineObject.put("状态序号", String.valueOf(order));
        freqResArray.add(oneLineObject);
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
        oneLineObject.put("最大发射功率", devFreqDsssJSONObject.get("ave_pow_transmit_max"));
        oneLineObject.put("调制方式", "--");
        workstyle = (String)devFreqDsssJSONObject.get("work_style");
        if (mode == 0 && workstyle.equals("3")) workstyle = "2";
        if (mode == 2 && workstyle.equals("3")) {
            workstyle = "1";
            oneLineObject.put("工作方式", WorkStyleEnums.getMsgWithCode(workstyle).getMsg());
            freqResArray.add(oneLineObject);
            order += 1;
            workstyle = "2";
        }
        oneLineObject.put("状态序号", String.valueOf(order));
        oneLineObject.put("工作方式", WorkStyleEnums.getMsgWithCode(workstyle).getMsg());
        freqResArray.add(oneLineObject);

        return JSON.toJSONString(freqResArray);
    }

    private  boolean noPortSelect(JSONArray jsonArray){
        boolean port = false;
        for(int i=0; i<jsonArray.size(); i++){
            if(jsonArray.getString(i).equals("0")){
                port = true;
            }
        }
        return port;
    }
}
