package cn.gjb151b.outline.service;

import cn.gjb151b.outline.Constants.*;
import cn.gjb151b.outline.dao.ManageSysDevelopMapper;
import cn.gjb151b.outline.model.ManageSysSchema;
import cn.gjb151b.outline.outlineDao.ManageSysOutlineMapper;
import cn.gjb151b.outline.model.ManageSysDevelop;
import cn.gjb151b.outline.model.ManageSysOutline;
import cn.gjb151b.outline.outlineDao.ManageSysSchemaMapper;
import cn.gjb151b.outline.utils.DoubleIntoInteger;
import cn.gjb151b.outline.utils.JsonByPositon;
import cn.gjb151b.outline.utils.ServiceException;
import cn.gjb151b.outline.utils.StrInsertString;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.google.common.base.Strings;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static cn.gjb151b.outline.service.FreqDependency.grnerateData;

/**
 * 页面数据依赖于上一个系统的数据，或依赖当前页面之前页面数据，在这里进行特殊的监听和处理
 * <p>
 * ManageSysOutlineMapper中有的方法可以直接用，否则新的查询写在DependencyMapper中
 */

@Service(value = "DependencyService")
public class DependencyService {
    private int sensitiveNum =0;
    private JSONArray array1001 = new JSONArray();
    private JSONArray array1002 = new JSONArray();
    private JSONArray array1003 = new JSONArray();
    private JSONArray array1004 = new JSONArray();
    private JSONArray array1005 = new JSONArray();
    private JSONArray array1006 = new JSONArray();
    private JSONArray antennaCE106 = new JSONArray(); //CE106天线端口
    private JSONArray antennaCS = new JSONArray(); //CS103、CS104天线端口
    private JSONArray antennaCS105 = new JSONArray(); //CS105天线端口
    private JSONArray arrayRE103 = new JSONArray(); //RE103工作状态（只保留不可拆卸状态）
    @Autowired
    private DBService dbService;
    @Autowired
    private StandardLimitValueService standardLimitValueService;

    @Resource
    private ManageSysOutlineMapper manageSysOutlineMapper;
    @Resource
    private ManageSysDevelopMapper manageSysDevelopMapper;
    @Resource
    private ManageSysSchemaMapper manageSysSchemaMapper;
    Logger logger = Logger.getLogger(DependencyService.class);


    public String generateDependencyData(String outlineDevItemId, int pageNumber, String data) throws Exception {

        System.out.println("generateDependencyData Page ID >> " + pageNumber);

        JSONObject jsonObject;
        String resultData = new String();

        ManageSysOutline outline = manageSysOutlineMapper.selectProjectByDevItemId(outlineDevItemId);
        String devItemId = outline.getOutlineDevItemid();
        ManageSysDevelop devObject = manageSysDevelopMapper.selectByPrimaryKey(devItemId);
        String outlineData10 = outline.getOutlineData10();
        JSONObject outlineData1Object = JSONObject.parseObject(outlineData10);
        String phasePosition = "两相";
        if (outlineData1Object.size() != 0) {
            List<String> phasePositionList = new ArrayList<>();
            for (int i = 0; i < outlineData1Object.getJSONArray("电源端口").size(); i++) {
                phasePositionList.add((String)outlineData1Object.getJSONArray("电源端口").getJSONObject(i).get("两相/三相"));
            }
            if (phasePositionList.contains("三相Y")) {
                phasePosition = "三相Y";
            } else if (phasePositionList.contains("三相∆")) {
                phasePosition = "三相∆";
            } else if (phasePositionList.contains("两相")) {
                phasePosition = "两相";
            }
        }


        switch (pageNumber) {
            case 3:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    jsonObject.put("任务名称", outline.getOutlineName());
                }
                resultData = JSON.toJSONString(jsonObject);
                break;
            case 4:
                List<String> pic1List = new ArrayList<>();
                jsonObject = JSON.parseObject(data);
                logger.info("-----------------");
                logger.info(data);
                String name = devObject.getDevName();
                if(jsonObject.size() == 0){
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
                    jsonObject.put("分系统/设备照片", pic1List);
                    jsonObject.put("分系统/设备关系图", pic1List);
                }
//                jsonObject.put("任务名称", name);
//                String subEqp = SubSysEnums.getMsgWithCode(devObject.getDevSubsysEqp()).getMsg();
//                String devPrimaryPlatform = PrimaryPlatformEnums.getMsgWithCode(devObject.getDevPrimaryPlatform()).getMsg();
//                jsonObject.put("预定使用平台", devPrimaryPlatform);
//                jsonObject.put("分系统/设备", subEqp);
//                String subsysEqpName = devObject.getDevSubsysEqpName();
//                jsonObject.put("分系统/设备名称", subsysEqpName);
//                String subsysEqpModel = devObject.getDevSubsysEqpModel();
//                jsonObject.put("型号", subsysEqpModel);
//                String subsysEqpNum = devObject.getDevSubsysEqpNum();
//                jsonObject.put("串号", subsysEqpNum);
//                String supplier = devObject.getDevSupplier();
//                jsonObject.put("承制单位", supplier);
//                jsonObject.put("分系统/设备照片", pic1List);

                resultData = JSON.toJSONString(jsonObject);
                break;
            case 5:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    jsonObject = generateSubsysOrEqpAttrData(devObject);
                }
                resultData = JSON.toJSONString(jsonObject);
                break;
            case 6:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    resultData = manageSysSchemaMapper.selectCol(1, "outline_data_6");
                } else {
                    resultData = data;
                }
                break;

            case 1001:
                resultData = grnerateData(devObject, true, true);
                break;
            case 1002:
                resultData = grnerateData(devObject, true, true);
                break;
            case 1003:
                resultData = grnerateData(devObject, true, true);
                break;
            case 1004:
                resultData = grnerateData(devObject, true, true);
                break;
            case 1005:
                resultData = grnerateData(devObject, true, false);
                break;

            case 10:
                JSONArray powerArray = new JSONArray();
                if(outline.getOutlineStatus() == 0){
                    JSONArray powerPortArray = JSON.parseArray(devObject.getDevPowerport());
                    JSONArray powerSupplyArray = JSON.parseArray(devObject.getDevPowersupply());
                    JSONArray voltageArray = JSON.parseArray(devObject.getDevVoltage());
                    JSONArray voltageNumArray = JSON.parseArray(devObject.getDevVoltagenum());
                    JSONArray powerName = JSON.parseArray(devObject.getDevPowername());
                    for(int i=0; i<powerPortArray.size(); i++){
                        JSONObject singlePower = new JSONObject();
                        singlePower.put("端口名称或代号", powerName.getString(i));
                        if(powerPortArray.getString(i).equals("1")){
                            singlePower.put("输入/输出", "输入");
                            singlePower.put("外部电源供电", "是");
                            if(voltageArray.getString(i).equals("1")){
                                singlePower.put("交流/直流", "直流");
                            }else{
                                singlePower.put("交流/直流", "交流");
                                if(voltageArray.getString(i).equals("2")){
                                    singlePower.put("频率(Hz)", "50");
                                }else if (voltageArray.getString(i).equals("3")){
                                    singlePower.put("频率(Hz)", "400");
                                }
                            }
                            if(powerSupplyArray.getString(i).equals("1")){
                                if(voltageNumArray.getString(i).equals("1")){
                                    singlePower.put("电压(V)", "<=28V");
                                }else if(voltageNumArray.getString(i).equals("2")){
                                    singlePower.put("电压(V)", ">28V");
                                }
                            }else{
                                singlePower.put("电压(V)", "<=28V 或 >28V");
                            }
                        }else if(powerPortArray.getString(i).equals("2")){
                            singlePower.put("外部电源供电", "否");
                        }else{
                            continue;
                        }
                        powerArray.add(singlePower);
                    }
                    JSONObject page10JsonObject = JSON.parseObject(data);
                    page10JsonObject.put("电源端口", powerArray);
                    resultData = JSON.toJSONString(page10JsonObject);
                }else{
                    resultData = data;
                }
                break;
            case 13:
                String outlineData11 = manageSysOutlineMapper.selectColByOutlineDevItemId(outlineDevItemId, "outline_data_11");
                JSONArray outlineData11JsonArray = JSON.parseObject(outlineData11).getJSONArray("敏感度判据及检测方法");
                jsonObject = JSON.parseObject(data);
                if (outlineData11JsonArray != null && outlineData11JsonArray.size() > 0) {
                    String stopTime = "";
                    for (int i = 0; i < outlineData11JsonArray.size(); i++) {
                        stopTime = stopTime + String.valueOf((int)outlineData11JsonArray.getJSONObject(i).get("驻留时间(s)")) + "s" +  "、";
                    }
                    stopTime = stopTime.substring(0, stopTime.length() - 1);
                    JSONArray outlineData13JsonArray = jsonObject.getJSONArray("敏感度测试参数");
                    if (outlineData13JsonArray != null && outlineData13JsonArray.size() > 0) {
                        for (int i = 0; i < outlineData13JsonArray.size(); i++) {
                            outlineData13JsonArray.getJSONObject(i).put("驻留时间", stopTime);
                        }
                        jsonObject.put("敏感度测试参数", outlineData13JsonArray);
                    }
                }
                resultData = JSON.toJSONString(jsonObject);
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
//                resultData = generateAntennaData(devObject,data);
                resultData = generateLimitText(data, devCE106);
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
                resultData = data;
//                resultData = generateAntennaData(devObject,data);
                break;
            case 21:
//                resultData = generateAntennaData(devObject,data);
                resultData = data;
                break;
            case 22:
//                resultData = generateAntennaData(devObject,data);
                resultData = data;

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
                System.out.println("resultData:"+resultData);
                break;
            case 35:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try{
                        resultData = dbService.fetchDefaultData(1, "outline_data_35");
                        devCE101 = devObject.getDevCe101();
                        String equipment = "CE101 测试设备";
                        int[] nums = {0, 1, 2, 3};
                        String keyName = "限值";
//                        resultData = generateLimitPic2(resultData, devCE101, equipment, nums, keyName );
//                        根据新建10 单相、三相拉选所填，确定相应测试设备数量
                        JSONObject outlineData35Object = JSONObject.parseObject(resultData);
                        if (phasePosition.equals("两相")) {
                            outlineData35Object.getJSONArray("CE101 测试设备").getJSONObject(6).put("数量", 2);
                            outlineData35Object.getJSONArray("CE101 测试设备").getJSONObject(7).put("数量", 2);
                        } else if (phasePosition.equals("三相∆")) {
                            outlineData35Object.getJSONArray("CE101 测试设备").getJSONObject(6).put("数量", 3);
                            outlineData35Object.getJSONArray("CE101 测试设备").getJSONObject(7).put("数量", 3);
                        } else if (phasePosition.equals("三相Y")) {
                            outlineData35Object.getJSONArray("CE101 测试设备").getJSONObject(6).put("数量", 4);
                            outlineData35Object.getJSONArray("CE101 测试设备").getJSONObject(7).put("数量", 4);
                        }
                        //根据manage_sys_develop中项目对应测试项目所填写的图片，确定限值的当前范围
//                        JSONObject devCE101JsonObject = JSONObject.parseObject(devCE101);
//                        JSONArray limitValueJsonArray = devCE101JsonObject.getJSONArray("limit_value");
//                        List<String> imgNumList = new ArrayList<>();
//                        for (int i = 0; i < limitValueJsonArray.size(); i++) {
//                            imgNumList.add((String)limitValueJsonArray.getJSONObject(i).get("pic"));
//                        }
//                        int[] positionNums = {0, 1, 2, 3, 6};
//                        outlineData35Object = putLimitRangeValue(outlineData35Object, positionNums, imgNumList, "CE101 测试设备");
                        resultData = JSON.toJSONString(outlineData35Object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                break;
            case 36:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        resultData = dbService.fetchDefaultData(1, "outline_data_36");
                        devCE102 = devObject.getDevCe102();
                        String equipment = "CE102 测试设备";
                        int[] nums = {0, 1, 3};
                        String keyName = "限值";
                        resultData = generateLimitPic2(resultData, devCE102, equipment, nums, keyName);
                        //根据新建10 单相、三相拉选所填，确定相应测试设备数量
                        JSONObject outlineData36Object = JSONObject.parseObject(resultData);
                        if (phasePosition.equals("两相")) {
                            outlineData36Object.getJSONArray("CE102 测试设备").getJSONObject(4).put("数量", 2);
                            outlineData36Object.getJSONArray("CE102 测试设备").getJSONObject(5).put("数量", 1);
                        } else if (phasePosition.equals("三相∆")) {
                            outlineData36Object.getJSONArray("CE102 测试设备").getJSONObject(4).put("数量", 3);
                            outlineData36Object.getJSONArray("CE102 测试设备").getJSONObject(5).put("数量", 2);
                        } else if (phasePosition.equals("三相Y")) {
                            outlineData36Object.getJSONArray("CE102 测试设备").getJSONObject(4).put("数量", 4);
                            outlineData36Object.getJSONArray("CE102 测试设备").getJSONObject(5).put("数量", 3);
                        }
                        //根据manage_sys_develop中项目对应测试项目所填写的图片，确定限值的当前范围
                        JSONObject devCE102JsonObject = JSONObject.parseObject(devCE102);
                        JSONObject limitValueJsonObject = devCE102JsonObject.getJSONObject("limit_value");
                        List<String> imgNumList = new ArrayList<>();
                        imgNumList.add((String)limitValueJsonObject.get("pic"));
                        int[] positionNums = {0, 1, 3};
                        outlineData36Object = putLimitRangeValue(outlineData36Object, positionNums, imgNumList, "CE102 测试设备");
                        String Dimmer20db = (String)outlineData36Object.getJSONArray("CE102 测试设备").getJSONObject(3).get("主要性能指标");
                        Dimmer20db = Dimmer20db + "\n" + "阻抗：" + "50Ω";
                        outlineData36Object.getJSONArray("CE102 测试设备").getJSONObject(3).put("主要性能指标", Dimmer20db);
                        resultData = JSON.toJSONString(outlineData36Object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                break;
            case 37:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        resultData = dbService.fetchDefaultData(1, "outline_data_37");
                        devCE106 = devObject.getDevCe106();
                        String equipment = "CE106 测试设备";
                        int nums[] = {0, 1, 3, 5};
                        String keyName = "限值";
                        resultData = generateLimitText2(resultData, devCE106, equipment, nums, keyName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                break;
            case 38:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        resultData = dbService.fetchDefaultData(1, "outline_data_38");
                        //根据新建10 单相、三相拉选所填，确定相应测试设备数量
                        JSONObject outlineData38Object = JSONObject.parseObject(resultData);
                        if (phasePosition.equals("两相")) {
                            outlineData38Object.getJSONArray("CE107 测试设备").getJSONObject(2).put("数量", 2);
                            outlineData38Object.getJSONArray("CE107 测试设备").getJSONObject(3).put("数量", 2);
                        } else if (phasePosition.equals("三相∆")) {
                            outlineData38Object.getJSONArray("CE107 测试设备").getJSONObject(2).put("数量", 3);
                            outlineData38Object.getJSONArray("CE107 测试设备").getJSONObject(3).put("数量", 3);
                        } else if (phasePosition.equals("三相Y")) {
                            outlineData38Object.getJSONArray("CE107 测试设备").getJSONObject(2).put("数量", 4);
                            outlineData38Object.getJSONArray("CE107 测试设备").getJSONObject(3).put("数量", 4);
                        }
                        resultData = JSON.toJSONString(outlineData38Object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                break;
            case 39:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        devCS101 = devObject.getDevCs101();
                        String equipment = "CS101 测试设备";
                        int nums[] = {0, 1, 3};
                        String keyName = "限值";
                        resultData = dbService.fetchDefaultData(1, "outline_data_39");
                        resultData = generateLimitTwoPic2(resultData, devCS101, equipment, nums, keyName);
                        //根据新建10 单相、三相拉选所填，确定相应测试设备数量
                        JSONObject outlineData39Object = JSONObject.parseObject(resultData);
                        if (phasePosition.equals("两相")) {
                            outlineData39Object.getJSONArray("CS101 测试设备").getJSONObject(4).put("数量", 2);
                            outlineData39Object.getJSONArray("CS101 测试设备").getJSONObject(5).put("数量", 2);
                            outlineData39Object.getJSONArray("CS101 测试设备").getJSONObject(6).put("数量", 1);
                        } else if (phasePosition.equals("三相∆")) {
                            outlineData39Object.getJSONArray("CS101 测试设备").getJSONObject(4).put("数量", 3);
                            outlineData39Object.getJSONArray("CS101 测试设备").getJSONObject(5).put("数量", 3);
                            outlineData39Object.getJSONArray("CS101 测试设备").getJSONObject(6).put("数量", 3);
                        } else if (phasePosition.equals("三相Y")) {
                            outlineData39Object.getJSONArray("CS101 测试设备").getJSONObject(4).put("数量", 4);
                            outlineData39Object.getJSONArray("CS101 测试设备").getJSONObject(5).put("数量", 4);
                            outlineData39Object.getJSONArray("CS101 测试设备").getJSONObject(6).put("数量", 3);
                        }
                        JSONObject devCS101JsonObject = JSONObject.parseObject(devCS101);
                        JSONArray devCS101JsonArray = devCS101JsonObject.getJSONArray("limit_value");
                        List<String> imgNumList = new ArrayList<>();
                        for (int i = 0; i < devCS101JsonArray.size(); i++) {
                            if (devCS101JsonArray.getJSONObject(i).containsKey("pic_one")) {
                                imgNumList.add((String)devCS101JsonArray.getJSONObject(i).get("pic_one"));
                            }
                            if (devCS101JsonArray.getJSONObject(i).containsKey("pic_two")) {
                                imgNumList.add((String)devCS101JsonArray.getJSONObject(i).get("pic_two"));
                            }
                            if (devCS101JsonArray.getJSONObject(i).containsKey("pic_three")) {
                                imgNumList.add((String)devCS101JsonArray.getJSONObject(i).get("pic_three"));
                            }

                        }
                        int[] positionNums = {0, 1, 3};
                        outlineData39Object = putLimitRangeValue(outlineData39Object, positionNums, imgNumList, "CS101 测试设备");
                        resultData = JSON.toJSONString(outlineData39Object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                break;
            case 40:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        devCS102 = devObject.getDevCs102();
                        String equipment = "CS102 测试设备";
                        int nums[] = {0, 1, 3};
                        String keyName = "限值";
                        resultData = dbService.fetchDefaultData(1, "outline_data_40");
                        resultData = generateLimitText2(resultData, devCS102, equipment, nums, keyName);
                        //根据新建10 单相、三相拉选所填，确定相应测试设备数量
                        JSONObject outlineData40Object = JSONObject.parseObject(resultData);
                        if (phasePosition.equals("两相")) {
                            outlineData40Object.getJSONArray("CS102 测试设备").getJSONObject(4).put("数量", 2);
                            outlineData40Object.getJSONArray("CS102 测试设备").getJSONObject(5).put("数量", 2);
                        } else if (phasePosition.equals("三相∆")) {
                            outlineData40Object.getJSONArray("CS102 测试设备").getJSONObject(4).put("数量", 3);
                            outlineData40Object.getJSONArray("CS102 测试设备").getJSONObject(5).put("数量", 3);
                        } else if (phasePosition.equals("三相Y")) {
                            outlineData40Object.getJSONArray("CS102 测试设备").getJSONObject(4).put("数量", 4);
                            outlineData40Object.getJSONArray("CS102 测试设备").getJSONObject(5).put("数量", 4);
                        }
                        resultData = JSON.toJSONString(outlineData40Object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                break;
            case 41:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    jsonObject = JSON.parseObject(data);
                    if (jsonObject.size() == 0) {
                        try {
                            resultData = dbService.fetchDefaultData(1, "outline_data_41");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        resultData = data;
                    }
                } else {
                    resultData = data;
                }
                break;
            case 42:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        resultData = dbService.fetchDefaultData(1, "outline_data_42");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                break;
            case 43:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        resultData = dbService.fetchDefaultData(1, "outline_data_43");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                break;
            case 44:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        resultData = dbService.fetchDefaultData(1, "outline_data_44");
                        //根据新建10 单相、三相拉选所填，确定相应测试设备数量
                        JSONObject outlineData44Object = JSONObject.parseObject(resultData);
                        if (phasePosition.equals("两相")) {
                            outlineData44Object.getJSONArray("CS106 测试设备").getJSONObject(2).put("数量", 2);
                            outlineData44Object.getJSONArray("CS106 测试设备").getJSONObject(3).put("数量", 2);
                            outlineData44Object.getJSONArray("CS106 测试设备").getJSONObject(4).put("数量", 1);
                        } else if (phasePosition.equals("三相∆")) {
                            outlineData44Object.getJSONArray("CS106 测试设备").getJSONObject(2).put("数量", 3);
                            outlineData44Object.getJSONArray("CS106 测试设备").getJSONObject(3).put("数量", 3);
                            outlineData44Object.getJSONArray("CS106 测试设备").getJSONObject(4).put("数量", 3);
                        } else if (phasePosition.equals("三相Y")) {
                            outlineData44Object.getJSONArray("CS106 测试设备").getJSONObject(2).put("数量", 4);
                            outlineData44Object.getJSONArray("CS106 测试设备").getJSONObject(3).put("数量", 4);
                            outlineData44Object.getJSONArray("CS106 测试设备").getJSONObject(4).put("数量", 3);
                        }
                        resultData = JSON.toJSONString(outlineData44Object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                break;
            case 45:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        devCS109 = devObject.getDevCs109();
                        String equipment = "CS109 测试设备";
                        int nums[] = {0, 1, 2, 3};
                        String keyName = "限值";
                        resultData = dbService.fetchDefaultData(1, "outline_data_45");
                        resultData = generateLimitPic2(resultData, devCS109, equipment, nums, keyName);
                        JSONObject outlineData45Object = JSONObject.parseObject(resultData);
                        JSONObject devCS109JsonObject = JSONObject.parseObject(devCS109);
                        JSONObject limitValueJsonObject = devCS109JsonObject.getJSONObject("limit_value");
                        List<String> imgNumList = new ArrayList<>();
                        imgNumList.add((String)limitValueJsonObject.get("pic"));
                        int[] positionNums = {0, 1, 2, 3};
                        outlineData45Object = putLimitRangeValue(outlineData45Object, positionNums, imgNumList, "CS109 测试设备");
                        resultData = JSON.toJSONString(outlineData45Object);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                break;
            case 46:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        resultData = dbService.fetchDefaultData(1, "outline_data_46");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                break;
            case 47:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        devCS114 = devObject.getDevCs114();
                        String equipment = "CS114 测试设备";
                        int nums[] = {0, 1, 2, 3, 5, 6, 7, 8, 9};
                        String keyName = "限值";
                        resultData = dbService.fetchDefaultData(1, "outline_data_47");
                        resultData = generateLimitPic2(resultData, devCS114, equipment, nums, keyName);
                        //根据新建10 单相、三相拉选所填，确定相应测试设备数量
                        JSONObject outlineData47Object = JSONObject.parseObject(resultData);
                        if (phasePosition.equals("两相")) {
                            outlineData47Object.getJSONArray("CS114 测试设备").getJSONObject(10).put("数量", 2);
                            outlineData47Object.getJSONArray("CS114 测试设备").getJSONObject(11).put("数量", 2);
                        } else if (phasePosition.equals("三相∆")) {
                            outlineData47Object.getJSONArray("CS114 测试设备").getJSONObject(10).put("数量", 3);
                            outlineData47Object.getJSONArray("CS114 测试设备").getJSONObject(11).put("数量", 3);
                        } else if (phasePosition.equals("三相Y")) {
                            outlineData47Object.getJSONArray("CS114 测试设备").getJSONObject(10).put("数量", 4);
                            outlineData47Object.getJSONArray("CS114 测试设备").getJSONObject(11).put("数量", 4);
                        }
                        JSONObject devCS114JsonObject = JSONObject.parseObject(devCS114);
                        JSONObject limitValueJsonObject = devCS114JsonObject.getJSONObject("limit_value");
                        List<String> imgNumList = new ArrayList<>();
                        imgNumList.add((String)limitValueJsonObject.get("pic"));
                        int[] positionNums = {0, 1, 2, 3, 5, 6, 7, 8, 9};
                        outlineData47Object = putLimitRangeValue(outlineData47Object, positionNums, imgNumList, "CS114 测试设备");
                        resultData = JSON.toJSONString(outlineData47Object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                break;
            case 48:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        resultData = dbService.fetchDefaultData(1, "outline_data_48");
                        //根据新建10 单相、三相拉选所填，确定相应测试设备数量
                        JSONObject outlineData48Object = JSONObject.parseObject(resultData);
                        if (phasePosition.equals("两相")) {
                            outlineData48Object.getJSONArray("CS115 测试设备").getJSONObject(7).put("数量", 2);
                            outlineData48Object.getJSONArray("CS115 测试设备").getJSONObject(8).put("数量", 2);
                        } else if (phasePosition.equals("三相∆")) {
                            outlineData48Object.getJSONArray("CS115 测试设备").getJSONObject(7).put("数量", 3);
                            outlineData48Object.getJSONArray("CS115 测试设备").getJSONObject(8).put("数量", 3);
                        } else if (phasePosition.equals("三相Y")) {
                            outlineData48Object.getJSONArray("CS115 测试设备").getJSONObject(7).put("数量", 4);
                            outlineData48Object.getJSONArray("CS115 测试设备").getJSONObject(8).put("数量", 4);
                        }
//                        devCS115 = devObject.getDevCs115();
//                        JSONObject devCS115JsonObject = JSONObject.parseObject(devCS115);
//                        JSONObject limitValueJsonObject = devCS115JsonObject.getJSONObject("limit_value");
//                        List<String> imgNumList = new ArrayList<>();
//                        imgNumList.add((String)limitValueJsonObject.get("pic"));
//                        int[] positionNums = {1, 2, 5};
//                        outlineData48Object = putLimitRangeValue(outlineData48Object, positionNums, imgNumList, "CS115 测试设备");
                        resultData = JSON.toJSONString(outlineData48Object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                break;
            case 49:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        resultData = dbService.fetchDefaultData(1, "outline_data_49");
                        //根据新建10 单相、三相拉选所填，确定相应测试设备数量
                        JSONObject outlineData49Object = JSONObject.parseObject(resultData);
                        if (phasePosition.equals("两相")) {
                            outlineData49Object.getJSONArray("CS116 测试设备").getJSONObject(8).put("数量", 2);
                            outlineData49Object.getJSONArray("CS116 测试设备").getJSONObject(9).put("数量", 2);
                        } else if (phasePosition.equals("三相∆")) {
                            outlineData49Object.getJSONArray("CS116 测试设备").getJSONObject(8).put("数量", 3);
                            outlineData49Object.getJSONArray("CS116 测试设备").getJSONObject(9).put("数量", 3);
                        } else if (phasePosition.equals("三相Y")) {
                            outlineData49Object.getJSONArray("CS116 测试设备").getJSONObject(8).put("数量", 4);
                            outlineData49Object.getJSONArray("CS116 测试设备").getJSONObject(9).put("数量", 4);
                        }
                        resultData = JSON.toJSONString(outlineData49Object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                    break;
            case 50:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        devRE101 = devObject.getDevRe101();
                        String equipment = "RE101 测试设备";
                        int nums[] = {0, 1, 2};
                        String keyName = "限值";
                        resultData = dbService.fetchDefaultData(1, "outline_data_50");
                        resultData = generateLimitPic2(resultData, devRE101, equipment, nums, keyName);
                        //根据新建10 单相、三相拉选所填，确定相应测试设备数量
                        JSONObject outlineData50Object = JSONObject.parseObject(resultData);
                        if (phasePosition.equals("两相")) {
                            outlineData50Object.getJSONArray("RE101 测试设备").getJSONObject(3).put("数量", 2);
                            outlineData50Object.getJSONArray("RE101 测试设备").getJSONObject(4).put("数量", 2);
                        } else if (phasePosition.equals("三相∆")) {
                            outlineData50Object.getJSONArray("RE101 测试设备").getJSONObject(3).put("数量", 3);
                            outlineData50Object.getJSONArray("RE101 测试设备").getJSONObject(4).put("数量", 3);
                        } else if (phasePosition.equals("三相Y")) {
                            outlineData50Object.getJSONArray("RE101 测试设备").getJSONObject(3).put("数量", 4);
                            outlineData50Object.getJSONArray("RE101 测试设备").getJSONObject(4).put("数量", 4);
                        }
                        devRE101 = devObject.getDevRe101();
                        JSONObject devCS115JsonObject = JSONObject.parseObject(devRE101);
                        JSONObject limitValueJsonObject = devCS115JsonObject.getJSONObject("limit_value");
                        List<String> imgNumList = new ArrayList<>();
                        imgNumList.add((String)limitValueJsonObject.get("pic"));
                        int[] positionNums = {0, 1, 2};
                        outlineData50Object = putLimitRangeValue(outlineData50Object, positionNums, imgNumList, "RE101 测试设备");
                        resultData = JSON.toJSONString(outlineData50Object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                break;
            case 51:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        devRE102 = devObject.getDevRe102();
                        String equipment = "RE102 测试设备";
                        int nums[] = {0, 5};
                        String keyName = "限值";
                        resultData = dbService.fetchDefaultData(1, "outline_data_51");
                        resultData = generateLimitPic2(resultData, devRE102, equipment, nums, keyName);
                        //根据新建10 单相、三相拉选所填，确定相应测试设备数量
                        JSONObject outlineData51Object = JSONObject.parseObject(resultData);
                        if (phasePosition.equals("两相")) {
                            outlineData51Object.getJSONArray("RE102 测试设备").getJSONObject(6).put("数量", 2);
                            outlineData51Object.getJSONArray("RE102 测试设备").getJSONObject(7).put("数量", 2);
                        } else if (phasePosition.equals("三相∆")) {
                            outlineData51Object.getJSONArray("RE102 测试设备").getJSONObject(6).put("数量", 3);
                            outlineData51Object.getJSONArray("RE102 测试设备").getJSONObject(7).put("数量", 3);
                        } else if (phasePosition.equals("三相Y")) {
                            outlineData51Object.getJSONArray("RE102 测试设备").getJSONObject(6).put("数量", 4);
                            outlineData51Object.getJSONArray("RE102 测试设备").getJSONObject(7).put("数量", 4);
                        }
                        devRE102 = devObject.getDevRe102();
                        JSONObject devRE102JsonObject = JSONObject.parseObject(devRE102);
                        JSONObject limitValueJsonObject = devRE102JsonObject.getJSONObject("limit_value");
                        List<String> imgNumList = new ArrayList<>();
                        imgNumList.add((String)limitValueJsonObject.get("pic"));
                        int[] positionNums = {0, 5};
                        outlineData51Object = putLimitRangeValue(outlineData51Object, positionNums, imgNumList, "RE102 测试设备");
                        resultData = JSON.toJSONString(outlineData51Object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                break;
            case 52:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        devRE103 = devObject.getDevRe103();
                        String equipment = "RE103 测试设备";
                        int nums[] = {0, 5, 8};
                        String keyName = "限值";
                        resultData = dbService.fetchDefaultData(1, "outline_data_52");
                        resultData = generateLimitText2(resultData, devRE103, equipment, nums, keyName);
                        //根据新建10 单相、三相拉选所填，确定相应测试设备数量
                        JSONObject outlineData52Object = JSONObject.parseObject(resultData);
                        if (phasePosition.equals("两相")) {
                            outlineData52Object.getJSONArray("RE103 测试设备").getJSONObject(9).put("数量", 2);
                            outlineData52Object.getJSONArray("RE103 测试设备").getJSONObject(10).put("数量", 2);
                        } else if (phasePosition.equals("三相∆")) {
                            outlineData52Object.getJSONArray("RE103 测试设备").getJSONObject(9).put("数量", 3);
                            outlineData52Object.getJSONArray("RE103 测试设备").getJSONObject(10).put("数量", 3);
                        } else if (phasePosition.equals("三相Y")) {
                            outlineData52Object.getJSONArray("RE103 测试设备").getJSONObject(9).put("数量", 4);
                            outlineData52Object.getJSONArray("RE103 测试设备").getJSONObject(10).put("数量", 4);
                        }
                        resultData = JSON.toJSONString(outlineData52Object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                break;
            case 53:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        devRS101 = devObject.getDevRs101();
                        String equipment = "RS101 测试设备";
                        int nums[] = {0, 1, 2, 3, 4, 7};
                        String keyName = "限值";
                        resultData = dbService.fetchDefaultData(1, "outline_data_53");
                        resultData = generateLimitPic2(resultData, devRS101, equipment, nums, keyName);
                        //根据新建10 单相、三相拉选所填，确定相应测试设备数量
                        JSONObject outlineData53Object = JSONObject.parseObject(resultData);
                        if (phasePosition.equals("两相")) {
                            outlineData53Object.getJSONArray("RS101 测试设备").getJSONObject(5).put("数量", 2);
                            outlineData53Object.getJSONArray("RS101 测试设备").getJSONObject(6).put("数量", 2);
                        } else if (phasePosition.equals("三相∆")) {
                            outlineData53Object.getJSONArray("RS101 测试设备").getJSONObject(5).put("数量", 3);
                            outlineData53Object.getJSONArray("RS101 测试设备").getJSONObject(6).put("数量", 3);
                        } else if (phasePosition.equals("三相Y")) {
                            outlineData53Object.getJSONArray("RS101 测试设备").getJSONObject(5).put("数量", 4);
                            outlineData53Object.getJSONArray("RS101 测试设备").getJSONObject(6).put("数量", 4);
                        }
                        devRS101 = devObject.getDevRs101();
                        JSONObject devRS101JsonObject = JSONObject.parseObject(devRS101);
                        JSONObject limitValueJsonObject = devRS101JsonObject.getJSONObject("limit_value");
                        List<String> imgNumList = new ArrayList<>();
                        imgNumList.add((String)limitValueJsonObject.get("pic"));
                        int[] positionNums = {0, 1, 2, 3, 4, 7};
                        outlineData53Object = putLimitRangeValue(outlineData53Object, positionNums, imgNumList, "RS101 测试设备");
                        resultData = JSON.toJSONString(outlineData53Object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                break;
            case 54:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        devRS103 = devObject.getDevRs103();
                        String equipment = "RS103 测试设备";
                        int nums[] = {0, 1, 2, 3};
                        String keyName = "限值";
                        resultData = dbService.fetchDefaultData(1, "outline_data_54");
                        resultData = generateLimitPic2(resultData, devRS103, equipment, nums, keyName);
                        //根据新建10 单相、三相拉选所填，确定相应测试设备数量
                        JSONObject outlineData54Object = JSONObject.parseObject(resultData);
                        if (phasePosition.equals("两相")) {
                            outlineData54Object.getJSONArray("RS103 测试设备").getJSONObject(4).put("数量", 2);
                            outlineData54Object.getJSONArray("RS103 测试设备").getJSONObject(5).put("数量", 2);
                        } else if (phasePosition.equals("三相∆")) {
                            outlineData54Object.getJSONArray("RS103 测试设备").getJSONObject(4).put("数量", 3);
                            outlineData54Object.getJSONArray("RS103 测试设备").getJSONObject(5).put("数量", 3);
                        } else if (phasePosition.equals("三相Y")) {
                            outlineData54Object.getJSONArray("RS103 测试设备").getJSONObject(4).put("数量", 4);
                            outlineData54Object.getJSONArray("RS103 测试设备").getJSONObject(5).put("数量", 4);
                        }
                        devRS103 = devObject.getDevRs103();
                        JSONObject devRE103JsonObject = JSONObject.parseObject(devRS103);
                        JSONObject limitValueJsonObject = devRE103JsonObject.getJSONObject("limit_value");
                        List<String> imgNumList = new ArrayList<>();
                        imgNumList.add((String)limitValueJsonObject.get("pic"));
                        int[] positionNums = {0, 1, 2, 3};
                        outlineData54Object = putLimitRangeValue(outlineData54Object, positionNums, imgNumList, "RS103 测试设备");
                        resultData = JSON.toJSONString(outlineData54Object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                break;
            case 55:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        resultData = dbService.fetchDefaultData(1, "outline_data_55");
                        //根据新建10 单相、三相拉选所填，确定相应测试设备数量
                        JSONObject outlineData55Object = JSONObject.parseObject(resultData);
                        if (phasePosition.equals("两相")) {
                            outlineData55Object.getJSONArray("RS105 测试设备").getJSONObject(5).put("数量", 2);
                            outlineData55Object.getJSONArray("RS105 测试设备").getJSONObject(6).put("数量", 2);
                        } else if (phasePosition.equals("三相∆")) {
                            outlineData55Object.getJSONArray("RS105 测试设备").getJSONObject(5).put("数量", 3);
                            outlineData55Object.getJSONArray("RS105 测试设备").getJSONObject(6).put("数量", 3);
                        } else if (phasePosition.equals("三相Y")) {
                            outlineData55Object.getJSONArray("RS105 测试设备").getJSONObject(5).put("数量", 4);
                            outlineData55Object.getJSONArray("RS105 测试设备").getJSONObject(6).put("数量", 4);
                        }
                        resultData = JSON.toJSONString(outlineData55Object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                break;
            case 56:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        String outlineData56 = dbService.fetchDefaultData(1, "outline_data_56");
                        String outlineName = dbService.fetchData(outlineDevItemId, "outline_name");
                        jsonObject = JSON.parseObject(outlineData56);
                        jsonObject.put("任务名称", outlineName);
                        resultData = jsonObject.toJSONString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                break;
            case 57:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        String outlineData57 = dbService.fetchDefaultData(1, "outline_data_57");
                        String outlineName = dbService.fetchData(outlineDevItemId, "outline_name");
                        jsonObject = JSON.parseObject(outlineData57);
                        jsonObject.put("任务名称", outlineName);
                        resultData = jsonObject.toJSONString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                break;
            case 58:
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        String outlineData58 = dbService.fetchDefaultData(1, "outline_data_58");
                        String outlineName = dbService.fetchData(outlineDevItemId, "outline_name");
                        jsonObject = JSON.parseObject(outlineData58);
                        jsonObject.put("任务名称", outlineName);
                        String projectList = devObject.getProjectList();
                        JSONArray projectArray = JSON.parseArray(projectList);
                        JSONArray testProjectArray = new JSONArray();
                        for (int i = 0; i < projectArray.size(); i++) {
                            JSONObject projectObject = new JSONObject();
                            projectObject.put("试验项目序号", (i + 1));
                            projectObject.put("试验项目名称", projectArray.getString(i));
                            projectObject.put("计划起始时间", "");
                            projectObject.put("计划结束时间", "");
                            testProjectArray.add(projectObject);
                        }
                        jsonObject.put("试验项目", testProjectArray);
                        resultData = jsonObject.toJSONString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                break;
            case 59:
                String outlineStandardData59 = manageSysSchemaMapper.selectCol(1, "outline_data_59");
                JSONObject outlineStandardData59Object = JSON.parseObject(outlineStandardData59);
                jsonObject = JSON.parseObject(data);
                if (jsonObject.size() == 0) {
                    try {
                        String outlineData59 = dbService.fetchDefaultData(1, "outline_data_59");
                        String outlineName = dbService.fetchData(outlineDevItemId, "outline_name");
                        jsonObject = JSON.parseObject(outlineData59);
                        jsonObject.put("任务名称", outlineName);
                        resultData = jsonObject.toJSONString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultData = data;
                }
                jsonObject = JSON.parseObject(resultData);
                JSONArray cutAndDeflectArray = new JSONArray();
                JSONObject standardCutObject = outlineStandardData59Object.getJSONArray("标准剪裁与偏离说明").getJSONObject(0);
                JSONObject standardDeflectObject = outlineStandardData59Object.getJSONArray("标准剪裁与偏离说明").getJSONObject(1);
                String outlineData14To34 = "";
                String standardDeflect = "";
                String standardDeflectReason = "";
                String standardCut = "";
                String standardCutReason = "";
                ArrayList<String> projectList = new ArrayList<>();
                projectList.add("CE101");
                projectList.add("CE102");
                projectList.add("CE106");
                projectList.add("CE107");
                projectList.add("CS101");
                projectList.add("CS102");
                projectList.add("CS103");
                projectList.add("CS104");
                projectList.add("CS105");
                projectList.add("CS106");
                projectList.add("CS109");
                projectList.add("CS112");
                projectList.add("CS114");
                projectList.add("CS115");
                projectList.add("CS116");
                projectList.add("RE101");
                projectList.add("RE102");
                projectList.add("RE103");
                projectList.add("RS101");
                projectList.add("RS103");
                projectList.add("RS105");
                //添加每个项目的标准剪裁
                if (! JSON.parseObject(devObject.getDevCe101()).getString("remark").equals("")) {
                    standardCut = "试验项目CE101经剪裁";
                    standardCutReason = JSON.parseObject(devObject.getDevCe101()).getString("remark");
                    JSONObject newJsonObject = (JSONObject) standardCutObject.clone();
                    newJsonObject.put("内容", standardCut);
                    newJsonObject.put("理由", standardCutReason);
                    cutAndDeflectArray.add(newJsonObject);
                }
                if (! JSON.parseObject(devObject.getDevCe102()).getString("remark").equals("")) {
                    standardCut = "试验项目CE102经剪裁";
                    standardCutReason = JSON.parseObject(devObject.getDevCe102()).getString("remark");
                    JSONObject newJsonObject = (JSONObject) standardCutObject.clone();
                    newJsonObject.put("内容", standardCut);
                    newJsonObject.put("理由", standardCutReason);
                    cutAndDeflectArray.add(newJsonObject);
                }
                if (! JSON.parseObject(devObject.getDevCe106()).getString("remark").equals("")) {
                    standardCut = "试验项目CE106经剪裁";
                    standardCutReason = JSON.parseObject(devObject.getDevCe106()).getString("remark");
                    JSONObject newJsonObject = (JSONObject) standardCutObject.clone();
                    newJsonObject.put("内容", standardCut);
                    newJsonObject.put("理由", standardCutReason);
                    cutAndDeflectArray.add(newJsonObject);
                }
                if (! JSON.parseObject(devObject.getDevCe107()).getString("remark").equals("")) {
                    standardCut = "试验项目CE107经剪裁";
                    standardCutReason = JSON.parseObject(devObject.getDevCe107()).getString("remark");
                    JSONObject newJsonObject = (JSONObject) standardCutObject.clone();
                    newJsonObject.put("内容", standardCut);
                    newJsonObject.put("理由", standardCutReason);
                    cutAndDeflectArray.add(newJsonObject);
                }
                if (! JSON.parseObject(devObject.getDevCs101()).getString("remark").equals("")) {
                    standardCut = "试验项目CS101经剪裁";
                    standardCutReason = JSON.parseObject(devObject.getDevCs101()).getString("remark");
                    JSONObject newJsonObject = (JSONObject) standardCutObject.clone();
                    newJsonObject.put("内容", standardCut);
                    newJsonObject.put("理由", standardCutReason);
                    cutAndDeflectArray.add(newJsonObject);
                }
                if (! JSON.parseObject(devObject.getDevCs102()).getString("remark").equals("")) {
                    standardCut = "试验项目CS102经剪裁";
                    standardCutReason = JSON.parseObject(devObject.getDevCs102()).getString("remark");
                    JSONObject newJsonObject = (JSONObject) standardCutObject.clone();
                    newJsonObject.put("内容", standardCut);
                    newJsonObject.put("理由", standardCutReason);
                    cutAndDeflectArray.add(newJsonObject);
                }
                if (! JSON.parseObject(devObject.getDevCs103()).getString("remark").equals("")) {
                    standardCut = "试验项目CS103经剪裁";
                    standardCutReason = JSON.parseObject(devObject.getDevCs103()).getString("remark");
                    JSONObject newJsonObject = (JSONObject) standardCutObject.clone();
                    newJsonObject.put("内容", standardCut);
                    newJsonObject.put("理由", standardCutReason);
                    cutAndDeflectArray.add(newJsonObject);
                }
                if (! JSON.parseObject(devObject.getDevCs104()).getString("remark").equals("")) {
                    standardCut = "试验项目CS104经剪裁";
                    standardCutReason = JSON.parseObject(devObject.getDevCs104()).getString("remark");
                    JSONObject newJsonObject = (JSONObject) standardCutObject.clone();
                    newJsonObject.put("内容", standardCut);
                    newJsonObject.put("理由", standardCutReason);
                    cutAndDeflectArray.add(newJsonObject);
                }
                if (! JSON.parseObject(devObject.getDevCs105()).getString("remark").equals("")) {
                    standardCut = "试验项目CS105经剪裁";
                    standardCutReason = JSON.parseObject(devObject.getDevCs105()).getString("remark");
                    JSONObject newJsonObject = (JSONObject) standardCutObject.clone();
                    newJsonObject.put("内容", standardCut);
                    newJsonObject.put("理由", standardCutReason);
                    cutAndDeflectArray.add(newJsonObject);
                }
                if (! JSON.parseObject(devObject.getDevCs106()).getString("remark").equals("")) {
                    standardCut = "试验项目CS106经剪裁";
                    standardCutReason = JSON.parseObject(devObject.getDevCs106()).getString("remark");
                    JSONObject newJsonObject = (JSONObject) standardCutObject.clone();
                    newJsonObject.put("内容", standardCut);
                    newJsonObject.put("理由", standardCutReason);
                    cutAndDeflectArray.add(newJsonObject);
                }
                if (! JSON.parseObject(devObject.getDevCs109()).getString("remark").equals("")) {
                    standardCut = "试验项目CS109经剪裁";
                    standardCutReason = JSON.parseObject(devObject.getDevCs109()).getString("remark");
                    JSONObject newJsonObject = (JSONObject) standardCutObject.clone();
                    newJsonObject.put("内容", standardCut);
                    newJsonObject.put("理由", standardCutReason);
                    cutAndDeflectArray.add(newJsonObject);
                }
                if (! JSON.parseObject(devObject.getDevCs112()).getString("remark").equals("")) {
                    standardCut = "试验项目CS112经剪裁";
                    standardCutReason = JSON.parseObject(devObject.getDevCs112()).getString("remark");
                    JSONObject newJsonObject = (JSONObject) standardCutObject.clone();
                    newJsonObject.put("内容", standardCut);
                    newJsonObject.put("理由", standardCutReason);
                    cutAndDeflectArray.add(newJsonObject);
                }
                if (! JSON.parseObject(devObject.getDevCs114()).getString("remark").equals("")) {
                    standardCut = "试验项目CS114经剪裁";
                    standardCutReason = JSON.parseObject(devObject.getDevCs114()).getString("remark");
                    JSONObject newJsonObject = (JSONObject) standardCutObject.clone();
                    newJsonObject.put("内容", standardCut);
                    newJsonObject.put("理由", standardCutReason);
                    cutAndDeflectArray.add(newJsonObject);
                }
                if (! JSON.parseObject(devObject.getDevCs115()).getString("remark").equals("")) {
                    standardCut = "试验项目CS115经剪裁";
                    standardCutReason = JSON.parseObject(devObject.getDevCs115()).getString("remark");
                    JSONObject newJsonObject = (JSONObject) standardCutObject.clone();
                    newJsonObject.put("内容", standardCut);
                    newJsonObject.put("理由", standardCutReason);
                    cutAndDeflectArray.add(newJsonObject);
                }
                if (! JSON.parseObject(devObject.getDevCs116()).getString("remark").equals("")) {
                    standardCut = "试验项目CS116经剪裁";
                    standardCutReason = JSON.parseObject(devObject.getDevCs116()).getString("remark");
                    JSONObject newJsonObject = (JSONObject) standardCutObject.clone();
                    newJsonObject.put("内容", standardCut);
                    newJsonObject.put("理由", standardCutReason);
                    cutAndDeflectArray.add(newJsonObject);
                }
                if (! JSON.parseObject(devObject.getDevRe101()).getString("remark").equals("")) {
                    standardCut = "试验项目RE101经剪裁";
                    standardCutReason = JSON.parseObject(devObject.getDevRe101()).getString("remark");
                    JSONObject newJsonObject = (JSONObject) standardCutObject.clone();
                    newJsonObject.put("内容", standardCut);
                    newJsonObject.put("理由", standardCutReason);
                    cutAndDeflectArray.add(newJsonObject);
                }
                if (! JSON.parseObject(devObject.getDevRe102()).getString("remark").equals("")) {
                    standardCut = "试验项目RE102经剪裁";
                    standardCutReason = JSON.parseObject(devObject.getDevRe102()).getString("remark");
                    JSONObject newJsonObject = (JSONObject) standardCutObject.clone();
                    newJsonObject.put("内容", standardCut);
                    newJsonObject.put("理由", standardCutReason);
                    cutAndDeflectArray.add(newJsonObject);
                }
                if (! JSON.parseObject(devObject.getDevRe103()).getString("remark").equals("")) {
                    standardCut = "试验项目RE103经剪裁";
                    standardCutReason = JSON.parseObject(devObject.getDevRe103()).getString("remark");
                    JSONObject newJsonObject = (JSONObject) standardCutObject.clone();
                    newJsonObject.put("内容", standardCut);
                    newJsonObject.put("理由", standardCutReason);
                    cutAndDeflectArray.add(newJsonObject);
                }
                if (! JSON.parseObject(devObject.getDevRs101()).getString("remark").equals("")) {
                    standardCut = "试验项目RS101经剪裁";
                    standardCutReason = JSON.parseObject(devObject.getDevRs101()).getString("remark");
                    JSONObject newJsonObject = (JSONObject) standardCutObject.clone();
                    newJsonObject.put("内容", standardCut);
                    newJsonObject.put("理由", standardCutReason);
                    cutAndDeflectArray.add(newJsonObject);
                }
                if (! JSON.parseObject(devObject.getDevRs103()).getString("remark").equals("")) {
                    standardCut = "试验项目RS103经剪裁";
                    standardCutReason = JSON.parseObject(devObject.getDevRs103()).getString("remark");
                    JSONObject newJsonObject = (JSONObject) standardCutObject.clone();
                    newJsonObject.put("内容", standardCut);
                    newJsonObject.put("理由", standardCutReason);
                    cutAndDeflectArray.add(newJsonObject);
                }
                if (! JSON.parseObject(devObject.getDevRs105()).getString("remark").equals("")) {
                    standardCut = "试验项目RS105经剪裁";
                    standardCutReason = JSON.parseObject(devObject.getDevRs105()).getString("remark");
                    JSONObject newJsonObject = (JSONObject) standardCutObject.clone();
                    newJsonObject.put("内容", standardCut);
                    newJsonObject.put("理由", standardCutReason);
                    cutAndDeflectArray.add(newJsonObject);
                }
                //添加每个项目的标准偏离
                for (int i = 14; i <= 34; i++) {
                    String projectName = projectList.get(i - 14);
                    String colName = "outline_data_" + i;
                    outlineData14To34 = manageSysOutlineMapper.selectColByOutlineDevItemId(outlineDevItemId, colName);
                    JSONObject outlineData14To34Object = JSON.parseObject(outlineData14To34);
                    //项目试验图和试验方法相关修改
                    if (outlineData14To34Object.containsKey("项目试验图")) {
                        standardDeflect = projectName + "项目试验图修改";
                        standardDeflectReason = outlineData14To34Object.getString("修改图形理由");
                        JSONObject newJsonObject = (JSONObject) standardDeflectObject.clone();
                        newJsonObject.put("内容", standardDeflect);
                        newJsonObject.put("理由", standardDeflectReason);
                        cutAndDeflectArray.add(newJsonObject);
                    }
                    if (outlineData14To34Object.containsKey("修改方法")) {
                        standardDeflect = projectName + "试验方法修改";
                        standardDeflectReason = "无";
                        JSONObject newJsonObject = (JSONObject) standardDeflectObject.clone();
                        newJsonObject.put("内容", standardDeflect);
                        newJsonObject.put("理由", standardDeflectReason);
                        cutAndDeflectArray.add(newJsonObject);
                    }
                    //试验端口及被试品工作状态偏离
                    if (outlineData14To34Object.containsKey("试验端口及被试品工作状态")) {
                        JSONArray testPortAndWorkStatusArray;
                        if (i == 16 || i == 20 || i == 21 || i == 22) {
                            testPortAndWorkStatusArray = outlineData14To34Object.getJSONArray("试验端口及被试品工作状态");
                            for (int j = 0; j < testPortAndWorkStatusArray.size(); j++) {
                                JSONObject testPortAndWorkStatusObject = testPortAndWorkStatusArray.getJSONObject(j);
                                if (testPortAndWorkStatusObject.containsKey("端口是否实施")) {
                                    if (testPortAndWorkStatusObject.getString("端口是否实施").equals("否")) {
                                        int number = j + 1;
                                        standardDeflect = projectName + "试验端口" + number + "不实施";
                                        standardDeflectReason = testPortAndWorkStatusObject.getString("不实施理由");
                                        JSONObject newJsonObject = (JSONObject) standardDeflectObject.clone();
                                        newJsonObject.put("内容", standardDeflect);
                                        newJsonObject.put("理由", standardDeflectReason);
                                        cutAndDeflectArray.add(newJsonObject);
                                        continue;
                                    }
                                }
                                if (testPortAndWorkStatusObject.containsKey("工作状态")) {
                                    JSONArray workStatusArray = testPortAndWorkStatusObject.getJSONArray("工作状态");
                                    for (int k = 0; k < workStatusArray.size(); k++) {
                                        JSONObject workStatusObjectK = workStatusArray.getJSONObject(k);
                                        if ("否".equals(workStatusArray.getJSONObject(k).getString("状态是否实施"))) {
                                            int portNumber = j + 1;
                                            int workStatusNumber = k + 1;
                                            standardDeflect = projectName + "试验端口" + portNumber + "的工作状态" + workStatusNumber + "不实施";
                                            standardDeflectReason = workStatusObjectK.getString("不实施理由");
                                            JSONObject newJsonObject = (JSONObject) standardDeflectObject.clone();
                                            newJsonObject.put("内容", standardDeflect);
                                            newJsonObject.put("理由", standardDeflectReason);
                                            cutAndDeflectArray.add(newJsonObject);
                                        }
                                    }
                                }
                            }
                            continue;
                        }
                        if (i == 17) {
                            testPortAndWorkStatusArray = outlineData14To34Object.getJSONArray("试验端口及被试品工作状态");
                            for (int j = 0; j < testPortAndWorkStatusArray.size(); j++) {
                                JSONObject testPortAndWorkStatusObject = testPortAndWorkStatusArray.getJSONObject(j);
                                if (testPortAndWorkStatusObject.containsKey("端口是否实施")) {
                                    if (testPortAndWorkStatusObject.getString("端口是否实施").equals("否")) {
                                        int number = j + 1;
                                        standardDeflect = projectName + "试验端口" + number + "不实施";
                                        standardDeflectReason = testPortAndWorkStatusObject.getString("不实施理由");
                                        JSONObject newJsonObject = (JSONObject) standardDeflectObject.clone();
                                        newJsonObject.put("内容", standardDeflect);
                                        newJsonObject.put("理由", standardDeflectReason);
                                        cutAndDeflectArray.add(newJsonObject);
                                    }
                                }
                            }
                            continue;
                        }
                        if (i >= 26 && i <= 28) {
                            testPortAndWorkStatusArray = outlineData14To34Object.getJSONObject("试验端口及被试品工作状态").getJSONArray("电源端口");
                        } else {
                            testPortAndWorkStatusArray = outlineData14To34Object.getJSONArray("试验端口及被试品工作状态");
                        }
                        for (int j = 0; j < testPortAndWorkStatusArray.size(); j++) {
                            JSONObject testPortAndWorkStatusObject = testPortAndWorkStatusArray.getJSONObject(j);
                            if (testPortAndWorkStatusObject.containsKey("端口是否实施")) {
                                if (testPortAndWorkStatusObject.getString("端口是否实施").equals("否")) {
                                    int number = j + 1;
                                    standardDeflect = projectName + "试验端口" + number + "不实施";
                                    standardDeflectReason = testPortAndWorkStatusObject.getString("不实施理由");
                                    JSONObject newJsonObject = (JSONObject) standardDeflectObject.clone();
                                    newJsonObject.put("内容", standardDeflect);
                                    newJsonObject.put("理由", standardDeflectReason);
                                    cutAndDeflectArray.add(newJsonObject);
                                    continue;
                                }
                            }
                            if (testPortAndWorkStatusObject.containsKey("工作状态")) {
                                JSONObject workStatusObject = testPortAndWorkStatusObject.getJSONObject("工作状态");
                                for (int k = 0; k < workStatusObject.size(); k++) {
                                    String workStatusKey = "工作状态" + (k + 1);
                                    JSONObject workStatusObjectK = workStatusObject.getJSONObject(workStatusKey);
                                    if (workStatusObjectK.containsKey("状态是否实施")) {
                                        if (workStatusObjectK.getString("状态是否实施").equals("否")) {
                                            int portNumber = j + 1;
                                            int workStatusNumber = k + 1;
                                            standardDeflect = projectName + "试验端口" + portNumber + "的工作状态" + workStatusNumber + "不实施";
                                            standardDeflectReason = workStatusObjectK.getString("不实施理由");
                                            JSONObject newJsonObject = (JSONObject) standardDeflectObject.clone();
                                            newJsonObject.put("内容", standardDeflect);
                                            newJsonObject.put("理由", standardDeflectReason);
                                            cutAndDeflectArray.add(newJsonObject);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                jsonObject.put("标准剪裁与偏离说明", cutAndDeflectArray);
                resultData = jsonObject.toJSONString();
                break;

            default:
                resultData = data;
                break;
        }
        String colName = "outline_data_"+pageNumber;
        dbService.submitData(outlineDevItemId, colName, resultData);


        return resultData;
    }

    public void generateDataAfterSubmit(String outlineDevItemId, int pageNumber, String data, int changeLocation) {
        JSONObject jsonObject;
        JSONArray jsonArray;
//        int sensitiveNum = 0;

        ManageSysOutline outline = manageSysOutlineMapper.selectProjectByDevItemId(outlineDevItemId);
        String devItemId = outline.getOutlineDevItemid();
        ManageSysDevelop devObject = manageSysDevelopMapper.selectByPrimaryKey(devItemId);
        switch (pageNumber) {
            case 8:
                if(devObject.getDevAttribute() == 1){
                    String outlineData14 = outline.getOutlineData14();
                    String outlineData15 = outline.getOutlineData15();
                    String outlineData16 = outline.getOutlineData16();
                    String outlineData17 = outline.getOutlineData17();
                    String outlineData29 = outline.getOutlineData29();
                    String outlineData30 = outline.getOutlineData30();
                    String outlineData31 = outline.getOutlineData31();
                    String outlineData18 = outline.getOutlineData18();
                    String outlineData19 = outline.getOutlineData19();
                    String outlineData20 = outline.getOutlineData20();
                    String outlineData21 = outline.getOutlineData21();
                    String outlineData22 = outline.getOutlineData22();
                    String outlineData23 = outline.getOutlineData23();
                    String outlineData24 = outline.getOutlineData24();
                    String outlineData25 = outline.getOutlineData25();
                    String outlineData26 = outline.getOutlineData26();
                    String outlineData27 = outline.getOutlineData27();
                    String outlineData28 = outline.getOutlineData28();
                    String outlineData32 = outline.getOutlineData32();
                    String outlineData33 = outline.getOutlineData33();
                    String outlineData34 = outline.getOutlineData34();
                    getFreqWorkStatus(devObject);
                    fillLaunchWorkStatus(array1001, outlineDevItemId, outlineData14, "试验端口及被试品工作状态", "outline_schema_14", "outline_data_14");
                    fillLaunchWorkStatus(array1001, outlineDevItemId, outlineData15, "试验端口及被试品工作状态", "outline_schema_15", "outline_data_15");
                    fillAntennaWorkStatus(antennaCE106, outlineDevItemId, outlineData16, "试验端口及被试品工作状态", "outline_data_16");
//                    fillLaunchWorkStatus(array1004, outlineDevItemId, outlineData16, "试验端口及被试品工作状态", "outline_schema_16", "outline_data_16");
//                    fillLaunchWorkStatus(array1006, outlineId, outlineData17, "试验端口及被试品工作状态", "outline_schema_17", "outline_data_17");
                    //更新17页项目信息
                    JSONObject outlineData17Object = JSON.parseObject(outlineData17, Feature.OrderedField);
                    outlineData17Object.remove("试验端口及被试品工作状态");
                    manageSysSchemaMapper.updateCol(1, "outline_schema_17", array1006.getString(0));
                    manageSysOutlineMapper.updateColByOutlineDevItemId(outlineDevItemId, "outline_data_17", JSON.toJSONString(outlineData17Object));

                    fillLaunchWorkStatus(array1001, outlineDevItemId, outlineData29, "试验部位及被试品工作状态", "outline_schema_29", "outline_data_29");
                    fillLaunchWorkStatus(array1003, outlineDevItemId, outlineData30, "被试品工作状态", "outline_schema_30", "outline_data_30");
                    fillLaunchWorkStatus(arrayRE103, outlineDevItemId, outlineData31, "被试品工作状态", "outline_schema_31", "outline_data_31");
                    fillLaunchWorkStatus(array1002, outlineDevItemId, outlineData18, "试验端口及被试品工作状态", "outline_schema_18", "outline_data_18");
                    fillLaunchWorkStatus(array1002, outlineDevItemId, outlineData19, "试验端口及被试品工作状态", "outline_schema_19", "outline_data_19");
                    fillAntennaWorkStatus(antennaCS, outlineDevItemId, outlineData20, "试验端口及被试品工作状态", "outline_data_20");
                    fillAntennaWorkStatus(antennaCS, outlineDevItemId, outlineData21, "试验端口及被试品工作状态", "outline_data_21");
                    fillAntennaWorkStatus(antennaCS105, outlineDevItemId, outlineData22, "试验端口及被试品工作状态", "outline_data_22");
//                    fillLaunchWorkStatus(array1005, outlineDevItemId, outlineData20, "试验端口及被试品工作状态", "outline_schema_20", "outline_data_20");
//                    fillLaunchWorkStatus(array1005, outlineDevItemId, outlineData21, "试验端口及被试品工作状态", "outline_schema_21", "outline_data_21");
//                    fillLaunchWorkStatus(array1005, outlineDevItemId, outlineData22, "试验端口及被试品工作状态", "outline_schema_22", "outline_data_22");
                    fillLaunchWorkStatus(array1002, outlineDevItemId, outlineData23, "试验端口及被试品工作状态", "outline_schema_23", "outline_data_23");
                    fillLaunchWorkStatus(array1002, outlineDevItemId, outlineData24, "试验位置及被试品工作状态", "outline_schema_24", "outline_data_24");
                    fillLaunchWorkStatus(array1002, outlineDevItemId, outlineData25, "试验位置及被试品工作状态", "outline_schema_25", "outline_data_25");
                    fillLaunchWorkStatus(array1002, outlineDevItemId, outlineData26, "试验端口及被试品工作状态", "outline_schema_26", "outline_data_26");
                    fillLaunchWorkStatus(array1002, outlineDevItemId, outlineData27, "试验端口及被试品工作状态", "outline_schema_27", "outline_data_27");
                    fillLaunchWorkStatus(array1002, outlineDevItemId, outlineData28, "试验端口及被试品工作状态", "outline_schema_28", "outline_data_28");
                    fillLaunchWorkStatus(array1002, outlineDevItemId, outlineData32, "试验端口及被试品工作状态", "outline_schema_32", "outline_data_32");
                    fillLaunchWorkStatus(array1004, outlineDevItemId, outlineData33, "试验端口及被试品工作状态", "outline_schema_33", "outline_data_33");
                    fillLaunchWorkStatus(array1002, outlineDevItemId, outlineData34, "试验端口及被试品工作状态", "outline_schema_34", "outline_data_34");
                }
                break;
            case 9:
                jsonObject = JSON.parseObject(data);
                if (changeLocation ==1 || changeLocation == 3) {
                    JSONArray launchArray = jsonObject.getJSONArray("发射测试工作状态");
                    String outlineData14 = outline.getOutlineData14();
                    String outlineData15 = outline.getOutlineData15();
                    String outlineData16 = outline.getOutlineData16();
                    String outlineData17 = outline.getOutlineData17();
                    String outlineData29 = outline.getOutlineData29();
                    String outlineData30 = outline.getOutlineData30();
                    String outlineData31 = outline.getOutlineData31();
                    fillLaunchWorkStatus(launchArray, outlineDevItemId, outlineData14, "试验端口及被试品工作状态", "outline_schema_14", "outline_data_14");
                    fillLaunchWorkStatus(launchArray, outlineDevItemId, outlineData15, "试验端口及被试品工作状态", "outline_schema_15", "outline_data_15");
                    fillLaunchWorkStatus(launchArray, outlineDevItemId, outlineData16, "试验端口及被试品工作状态", "outline_schema_16", "outline_data_16");
                    fillLaunchWorkStatus(launchArray, outlineDevItemId, outlineData17, "试验端口及被试品工作状态", "outline_schema_17", "outline_data_17");
                    fillLaunchWorkStatus(launchArray, outlineDevItemId, outlineData29, "试验部位及被试品工作状态", "outline_schema_29", "outline_data_29");
                    fillLaunchWorkStatus(launchArray, outlineDevItemId, outlineData30, "被试品工作状态", "outline_schema_30", "outline_data_30");
                    fillLaunchWorkStatus(launchArray, outlineDevItemId, outlineData31, "被试品工作状态", "outline_schema_31", "outline_data_31");
//                       System.out.println("outlineSchema29"+JSON.toJSONString(outlineSchema29Object));
                    }
                if(changeLocation ==2 || changeLocation ==3) {
                        JSONArray sensitiveArray = jsonObject.getJSONArray("敏感度测试工作状态");
//                        sensitiveNum = sensitiveArray.size();
                        String outlineData18 = outline.getOutlineData18();
                        String outlineData19 = outline.getOutlineData19();
                        String outlineData20 = outline.getOutlineData20();
                        String outlineData21 = outline.getOutlineData21();
                        String outlineData22 = outline.getOutlineData22();
                        String outlineData23 = outline.getOutlineData23();
                        String outlineData24 = outline.getOutlineData24();
                        String outlineData25 = outline.getOutlineData25();
                        String outlineData26 = outline.getOutlineData26();
                        String outlineData27 = outline.getOutlineData27();
                        String outlineData28 = outline.getOutlineData28();
                        String outlineData32 = outline.getOutlineData32();
                        String outlineData33 = outline.getOutlineData33();
                        String outlineData34 = outline.getOutlineData34();
                        fillLaunchWorkStatus(sensitiveArray, outlineDevItemId, outlineData18, "试验端口及被试品工作状态", "outline_schema_18", "outline_data_18");
                        fillLaunchWorkStatus(sensitiveArray, outlineDevItemId, outlineData19, "试验端口及被试品工作状态", "outline_schema_19", "outline_data_19");
                        fillLaunchWorkStatus(sensitiveArray, outlineDevItemId, outlineData20, "试验端口及被试品工作状态", "outline_schema_20", "outline_data_20");
                        fillLaunchWorkStatus(sensitiveArray, outlineDevItemId, outlineData21, "试验端口及被试品工作状态", "outline_schema_21", "outline_data_21");
                        fillLaunchWorkStatus(sensitiveArray, outlineDevItemId, outlineData22, "试验端口及被试品工作状态", "outline_schema_22", "outline_data_22");
                        fillLaunchWorkStatus(sensitiveArray, outlineDevItemId, outlineData23, "试验端口及被试品工作状态", "outline_schema_23", "outline_data_23");
                        fillLaunchWorkStatus(sensitiveArray, outlineDevItemId, outlineData24, "试验位置及被试品工作状态", "outline_schema_24", "outline_data_24");
                        fillLaunchWorkStatus(sensitiveArray, outlineDevItemId, outlineData25, "试验位置及被试品工作状态", "outline_schema_25", "outline_data_25");
                        fillLaunchWorkStatus(sensitiveArray, outlineDevItemId, outlineData26, "试验端口及被试品工作状态", "outline_schema_26", "outline_data_26");
                        fillLaunchWorkStatus(sensitiveArray, outlineDevItemId, outlineData27, "试验端口及被试品工作状态", "outline_schema_27", "outline_data_27");
                        fillLaunchWorkStatus(sensitiveArray, outlineDevItemId, outlineData28, "试验端口及被试品工作状态", "outline_schema_28", "outline_data_28");
                        fillLaunchWorkStatus(sensitiveArray, outlineDevItemId, outlineData32, "试验端口及被试品工作状态", "outline_schema_32", "outline_data_32");
                        fillLaunchWorkStatus(sensitiveArray, outlineDevItemId, outlineData33, "试验端口及被试品工作状态", "outline_schema_33", "outline_data_33");
                        fillLaunchWorkStatus(sensitiveArray, outlineDevItemId, outlineData34, "试验端口及被试品工作状态", "outline_schema_34", "outline_data_34");
                    }
                break;
            case 10:
                //第10页的电源端口数据控制着第14页的试验端口即被试品工作状态的数据
                jsonObject = JSON.parseObject(data);
                JSONArray powerPortArray = jsonObject.getJSONArray("电源端口");
                JSONArray interPortArray = jsonObject.getJSONArray("互联端口");
                if(devObject.getDevAttribute() == 0) {
                    sensitiveNum = JSON.parseObject(manageSysOutlineMapper.selectColByOutlineDevItemId(outlineDevItemId, "outline_data_9")).getJSONArray("敏感度测试工作状态").size();
                }else{
                    sensitiveNum = array1002.size();
                }
                JSONArray testPortArray = new JSONArray(); //外部电源输入 试验端口的列表数组
                JSONArray allPowerArray = new JSONArray(); //电源输入端口，包括内部电源输入端口
                JSONObject allPortObjectCS114 = new JSONObject(); //所有端口，包括电源端口和互联端口
                JSONArray powerArrayCS114 = new JSONArray(); //所有电源端口名称
                JSONObject allPortObjectCS116 = new JSONObject(); //所有端口，包括电源端口和互联端口
                JSONArray powerArrayCS116 = new JSONArray(); //所有电源端口名称
                JSONArray interArrayCS114 = new JSONArray(); //所有互联端口名称
                JSONArray interArrayCS116 = new JSONArray();
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
//                for(int i=1; i<sensitiveNum+1; i++){
//                    JSONObject CS114Power = new JSONObject();
//                    JSONObject CS116Power = new JSONObject();
//                    CS114Power.put("施加电缆束", "完整电源线，所有高电位线");
//                    CS116Power.put("施加电缆束", "完整电源线，每根高电位线");
//                    CS114Status.put("工作状态"+i, CS114Power);
//                    CS116Status.put("工作状态"+i, CS116Power);
//                }
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
                    //添加电源电缆束
                    JSONObject CS114Status = new JSONObject();
                    JSONObject CS116Status = new JSONObject();
                    for(int j=1; j<sensitiveNum+1; j++){
                        JSONObject CS114Power = new JSONObject();
                        JSONObject CS116Power = new JSONObject();
                        CS114Power.put("施加电缆束", "完整电源线，所有高电位线");
                        CS116Power.put("施加电缆束", "完整电源线，每根高电位线");
                        CS114Status.put("工作状态"+j, CS114Power);
                        CS116Status.put("工作状态"+j, CS116Power);
                    }
                    //所有电源端口
                    JSONObject singlePowerCS114 = new JSONObject();
                    JSONObject singlePowerCS116 = new JSONObject();
                    singlePowerCS114.put("电源端口",powerPort.get("端口名称或代号"));
                    singlePowerCS114.put("工作状态", CS114Status );
                    singlePowerCS116.put("电源端口",powerPort.get("端口名称或代号"));
                    singlePowerCS116.put("工作状态", CS116Status );
                    powerArrayCS114.add(singlePowerCS114);
                    powerArrayCS116.add(singlePowerCS116);

                }
                for(int i = 0; i < interPortArray.size(); i++){
                    JSONObject interPort = interPortArray.getJSONObject(i);
                    JSONObject singleInterCS114 = new JSONObject();
                    JSONObject singleInterCS116 = new JSONObject();
                    JSONObject CS114ConnectStatus = new JSONObject();
                    JSONObject CS116ConnectStatus = new JSONObject();
                    for(int j=1; j<sensitiveNum+1; j++){
                        JSONObject CS114Connect = new JSONObject();
                        JSONObject CS116Connect = new JSONObject();
                        if(interPort.get("端口类型").equals("互联")){
                            CS114Connect.put("施加电缆束", "完整互联线");
                            CS116Connect.put("施加电缆束", "完整互联线");
                        }else {
                            CS114Connect.put("施加电缆束", "互联线，所有电源线，所有高电位线");
                            CS116Connect.put("施加电缆束", "互联线，所有电源线，每根高电位线");
                        }
                        CS114ConnectStatus.put("工作状态"+j, CS114Connect);
                        CS116ConnectStatus.put("工作状态"+j, CS116Connect);
                    }
                    singleInterCS114.put("互联端口", interPort.get("端口名称或代号"));
                    singleInterCS114.put("工作状态", CS114ConnectStatus);
                    interArrayCS114.add(singleInterCS114);
                    singleInterCS116.put("互联端口", interPort.get("端口名称或代号"));
                    singleInterCS116.put("工作状态", CS116ConnectStatus);
                    interArrayCS116.add(singleInterCS116);
                }
                allPortObjectCS114.put("电源端口",powerArrayCS114);
                allPortObjectCS114.put("互联端口",interArrayCS114);
                allPortObjectCS116.put("电源端口",powerArrayCS116);
                allPortObjectCS116.put("互联端口",interArrayCS116);
                outlineData14Object.put("试验端口及被试品工作状态", testPortArray);
                outlineData15Object.put("试验端口及被试品工作状态", testPortArray);
                outlineData17Object.put("试验端口及被试品工作状态", allPowerArray);
                outlineData18Object.put("试验端口及被试品工作状态", testPortArray);
                outlineData23Object.put("试验端口及被试品工作状态", testPortArray);
                outlineData26Object.put("试验端口及被试品工作状态", allPortObjectCS114);
                outlineData27Object.put("试验端口及被试品工作状态", allPortObjectCS114);
                outlineData28Object.put("试验端口及被试品工作状态", allPortObjectCS116);
                System.out.println("试验端口及被试品工作状态:"+JSON.toJSONString(outlineData26Object));
                manageSysOutlineMapper.updateColByOutlineDevItemId(outlineDevItemId, "outline_data_14", JSON.toJSONString(outlineData14Object));
                manageSysOutlineMapper.updateColByOutlineDevItemId(outlineDevItemId, "outline_data_15", JSON.toJSONString(outlineData15Object));
                manageSysOutlineMapper.updateColByOutlineDevItemId(outlineDevItemId, "outline_data_17", JSON.toJSONString(outlineData17Object));
                manageSysOutlineMapper.updateColByOutlineDevItemId(outlineDevItemId, "outline_data_18", JSON.toJSONString(outlineData18Object));
                manageSysOutlineMapper.updateColByOutlineDevItemId(outlineDevItemId, "outline_data_23", JSON.toJSONString(outlineData23Object));
                manageSysOutlineMapper.updateColByOutlineDevItemId(outlineDevItemId, "outline_data_26", JSON.toJSONString(outlineData26Object));
                manageSysOutlineMapper.updateColByOutlineDevItemId(outlineDevItemId, "outline_data_27", JSON.toJSONString(outlineData27Object));
                manageSysOutlineMapper.updateColByOutlineDevItemId(outlineDevItemId, "outline_data_28", JSON.toJSONString(outlineData28Object));

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
        JSONArray statusList = jsonObject.getJSONArray("试验端口及被试品工作状态");
        if(statusList == null || statusList.getJSONObject(0).getString("工作状态").equals(devString) == false){
            jsonArray.add(antennaObject);
            jsonObject.put("试验端口及被试品工作状态", jsonArray);
        }
//        jsonArray.add(antennaObject);
//        jsonObject.put("试验端口及被试品工作状态", jsonArray);
        String resultData = JSON.toJSONString(jsonObject);
        return resultData;

    }


    public String generateLimitPic(String data, String devProject){
        JSONObject jsonObject;
        jsonObject = JSON.parseObject(data);
        JSONObject devJsonProject = (JSONObject) JSON.parse(devProject);
        StringBuilder builder = new StringBuilder();
        int CE101Num = 0;
        if(devJsonProject.getString("project_id").equals("CE101")) {
            if(devJsonProject.getJSONArray("limit_value").size() != 0) {
                for (int i = 0; i < devJsonProject.getJSONArray("limit_value").size(); i++) {
                    if (devJsonProject.getJSONArray("limit_value").getJSONObject(i).isEmpty()) {
                        CE101Num++;
                        continue;
                    }
                    String limitValue = devJsonProject.getJSONArray("limit_value").getJSONObject(i).getString("pic");
                    String limitValueCurrent = devJsonProject.getJSONArray("limit_value_current").getJSONObject(i).getString("pic");
                    if(limitValue != null && limitValueCurrent != null) {
                        builder.append("第");
                        builder.append(i + 1);
                        builder.append("个限值： ");
                        if (limitValue.equals(limitValueCurrent)) {
                            builder.append("GJB151B-2013标准规定图形:standard");
                            builder.append(limitValue);
                            builder.append(" 。");
                        } else {
                            builder.append("研制要求管理系统生成图形:");
                            builder.append(limitValueCurrent);
                            builder.append(" 。");
                        }
                    }else{
                        CE101Num++;
                    }
                }
                if(CE101Num == 3){
                    builder.append("无限值");
                }
            }else{
                builder.append("无限值");
            }
            jsonObject.put("限值", builder.toString());
        }else{
            String limitValue = devJsonProject.getJSONObject("limit_value").getString("pic");
            String limitValueCurrent = devJsonProject.getJSONObject("limit_value_current").getString("pic");
            if(limitValue == null || limitValueCurrent == null) {
                builder.append("无限值");
                jsonObject.put("限值", builder.toString());
            }else{
                if (limitValue.equals(limitValueCurrent)) {
                    builder.append("GJB151B-2013标准规定图形:standard");
                    builder.append(limitValue);
                    jsonObject.put("限值", builder.toString());
                } else {
                    builder.append("研制要求管理系统生成图形:");
                    builder.append(limitValueCurrent);
                    jsonObject.put("限值", builder.toString());
                }
            }
        }

        String resultData = JSON.toJSONString(jsonObject);
        return resultData;
    }
    public String generateLimitPic2(String data, String devProject, String equipment, int[] nums, String keyName){
        JSONObject jsonObject;
        jsonObject = JSON.parseObject(data);
        JSONObject devJsonProject = (JSONObject) JSON.parse(devProject);
        StringBuilder builder = new StringBuilder();
        JsonByPositon jsonByPositon = new JsonByPositon();
        if(devJsonProject.getString("project_id").equals("CE101")) {
            for (int i = 0; i < devJsonProject.getJSONArray("limit_value").size(); i++) {
                if(devJsonProject.getJSONArray("limit_value").getJSONObject(i).isEmpty()){
                    continue;
                }
                String limitValue = devJsonProject.getJSONArray("limit_value").getJSONObject(i).getString("pic");
                String limitValueCurrent = devJsonProject.getJSONArray("limit_value_current").getJSONObject(i).getString("pic");
                builder.append("第");
                builder.append(i+1);
                builder.append("个限值： ");
                if (limitValue.equals(limitValueCurrent)) {
                    builder.append("GJB151B-2013标准规定图形:standard");
                    builder.append(limitValue);
                    builder.append(" 。");
                } else {
                    builder.append("研制要求管理系统生成图形:");
                    builder.append(limitValueCurrent);
                    builder.append(" 。");
                }
            }
//            jsonObject.put("", builder.toString());
            return jsonByPositon.jsonUpdate(data, equipment, nums, keyName, builder.toString());
        }else{
            String limitValue = devJsonProject.getJSONObject("limit_value").getString("pic");
            String limitValueCurrent = devJsonProject.getJSONObject("limit_value_current").getString("pic");
            if (limitValue.equals(limitValueCurrent)) {
                builder.append("GJB151B-2013标准规定图形:standard");
                builder.append(limitValue);
//                jsonObject.put("限值", builder.toString());
                return jsonByPositon.jsonUpdate(data, equipment, nums, keyName, builder.toString());

            } else {
                builder.append("研制要求管理系统生成图形:");
                builder.append(limitValueCurrent);
//                jsonObject.put("限值", builder.toString());
                return jsonByPositon.jsonUpdate(data, equipment, nums, keyName, builder.toString());

            }
        }

//        String resultData = JSON.toJSONString(jsonObject);
//
    }

    public String generateLimitTwoPic(String data, String devProject){
        JSONObject jsonObject;
        jsonObject = JSON.parseObject(data);
        JSONObject devJsonProject = JSON.parseObject(devProject);
        int CS101Num = 0;
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<devJsonProject.getJSONArray("limit_value").size(); i++) {
            if(devJsonProject.getJSONArray("limit_value").getJSONObject(i).isEmpty()){
                CS101Num++;
                continue;
            }
            builder.append("第");
            builder.append(i+1);
            builder.append("个限值： ");
            JSONObject limitValue = devJsonProject.getJSONArray("limit_value").getJSONObject(i);
            JSONObject limitValueCurrent = devJsonProject.getJSONArray("limit_value_current").getJSONObject(i);
            String pic1 = limitValue.getString("pic_one");
            String pic2 = limitValue.getString("pic_two");
            String picCurrent1 = limitValueCurrent.getString("pic_one");
            String picCurrent2 = limitValueCurrent.getString("pic_two");
            if (pic1.equals(picCurrent1)) {
                builder.append("GJB151B-2013标准规定图形:standard");
                builder.append(pic1);
            } else {
                builder.append("研制要求管理系统生成图形:");
                builder.append(pic1);
            }
            if (pic2.equals(picCurrent2)) {
                builder.append("; GJB151B-2013标准规定图形:standard");
                builder.append(pic2);
                builder.append(" 。");
            } else {
                builder.append("研制要求管理系统生成图形:");
                builder.append(pic2);
                builder.append(" 。");
            }
        }
        if(CS101Num == 2){
            jsonObject.put("限值", "无限值");
        }else {
            jsonObject.put("限值", builder.toString());
        }
        String resultData = JSON.toJSONString(jsonObject);
        return resultData;
    }

    public String generateLimitTwoPic2(String data, String devProject, String equipment, int[] nums, String keyName){
        JSONObject jsonObject;
        jsonObject = JSON.parseObject(data);
        JSONObject devJsonProject = JSON.parseObject(devProject);
        int CS101Num = 0;
        StringBuilder builder = new StringBuilder();
        JsonByPositon jsonByPositon = new JsonByPositon();
        for(int i=0; i<devJsonProject.getJSONArray("limit_value").size(); i++) {
            if(devJsonProject.getJSONArray("limit_value").getJSONObject(i).isEmpty()){
                CS101Num++;
                continue;
            }
            builder.append("第");
            builder.append(i+1);
            builder.append("个限值： ");
            JSONObject limitValue = devJsonProject.getJSONArray("limit_value").getJSONObject(i);
            JSONObject limitValueCurrent = devJsonProject.getJSONArray("limit_value_current").getJSONObject(i);
            String pic1 = limitValue.getString("pic_one");
            String pic2 = limitValue.getString("pic_two");
            String picCurrent1 = limitValueCurrent.getString("pic_one");
            String picCurrent2 = limitValueCurrent.getString("pic_two");
            if (pic1.equals(picCurrent1)) {
                builder.append("GJB151B-2013标准规定图形:standard");
                builder.append(pic1);
            } else {
                builder.append("研制要求管理系统生成图形:");
                builder.append(pic1);
            }
            if (pic2.equals(picCurrent2)) {
                builder.append("; GJB151B-2013标准规定图形:standard");
                builder.append(pic2);
                builder.append(" 。");
            } else {
                builder.append("研制要求管理系统生成图形:");
                builder.append(pic2);
                builder.append(" 。");
            }
        }
        if(CS101Num == 2){
//            jsonObject.put("限值", "无限值");
            return jsonByPositon.jsonUpdate(data, equipment, nums, keyName, "无限值");
        }else {
//            jsonObject.put("限值", builder.toString());
            return jsonByPositon.jsonUpdate(data, equipment, nums, keyName, builder.toString());
        }
//        String resultData = JSON.toJSONString(jsonObject);
//        return resultData;
    }


    public String generateLimitText(String data, String devProject){
        JSONObject jsonObject;
        jsonObject = JSON.parseObject(data);
        JSONObject devJsonProject = JSON.parseObject(devProject);
        int CE107Num = 0;
        if (devJsonProject.getString("project_id").equals("CE107")) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < devJsonProject.getJSONArray("limit_value_current").size(); i++) {
                if (devJsonProject.getJSONArray("limit_value_current").getJSONObject(i).isEmpty()) {
                    CE107Num++;
                    continue;
                }
                builder.append("第");
                builder.append(i + 1);
                builder.append("个限值： ");
                builder.append(devJsonProject.getJSONArray("limit_value_current").getJSONObject(i).getString("text"));
                builder.append(" 。");
            }
            if(CE107Num == 2){
                jsonObject.put("限值","无限值");
            }else {
                jsonObject.put("限值", builder.toString());
            }
        } else {
            if(devJsonProject.getJSONObject("limit_value_current").getString("text") != null) {
                jsonObject.put("限值", devJsonProject.getJSONObject("limit_value_current").getString("text"));
            }else{
                jsonObject.put("限值","无限值");
            }
        }

        String resultData = JSON.toJSONString(jsonObject);
        return resultData;
    }

    public String generateLimitText2(String data, String devProject, String equipment, int[] nums, String keyName){
        JSONObject jsonObject;
        jsonObject = JSON.parseObject(data);
        JSONObject devJsonProject = JSON.parseObject(devProject);
        JsonByPositon jsonByPositon = new JsonByPositon();
        int CE107Num = 0;
        if (devJsonProject.getString("project_id").equals("CE107")) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < devJsonProject.getJSONArray("limit_value_current").size(); i++) {
                if (devJsonProject.getJSONArray("limit_value_current").getJSONObject(i).isEmpty()) {
                    CE107Num++;
                    continue;
                }
                builder.append("第");
                builder.append(i + 1);
                builder.append("个限值： ");
                builder.append(devJsonProject.getJSONArray("limit_value_current").getJSONObject(i).getString("text"));
                builder.append(" 。");
            }
            if(CE107Num == 2){
//                jsonObject.put("限值","无限值");
                return jsonByPositon.jsonUpdate(data, equipment, nums, keyName, "无限值");

            }else {
//                jsonObject.put("限值", builder.toString());
                return jsonByPositon.jsonUpdate(data, equipment, nums, keyName, builder.toString());
            }
        } else {
            if(devJsonProject.getJSONObject("limit_value_current").getString("text") != null) {
//                jsonObject.put("限值", devJsonProject.getJSONObject("limit_value_current").getString("text"));
                return jsonByPositon.jsonUpdate(data, equipment, nums, keyName, devJsonProject.getJSONObject("limit_value_current").getString("text"));
            }else{
//                jsonObject.put("限值","无限值");
                return jsonByPositon.jsonUpdate(data, equipment, nums, keyName, "无限值");
            }
        }

//        String resultData = JSON.toJSONString(jsonObject);
//        return resultData;
    }

    public String generateLimitTextAndPic(String data, String devProject){
        JSONObject jsonObject;
        jsonObject = JSON.parseObject(data);
        JSONObject devJsonProject = JSON.parseObject(devProject);
        String pic = devJsonProject.getJSONObject("limit_value").getString("pic");
        String picCurrent = devJsonProject.getJSONObject("limit_value_current").getString("pic");
        String textCurrent = devJsonProject.getJSONObject("limit_value_current").getString("text");
        StringBuilder builder = new StringBuilder();
        if(pic == null || picCurrent == null){
            if(textCurrent == null){
                builder.append("无限值");
            }else{
                builder.append(textCurrent);
            }
        }else {
            if (pic.equals(picCurrent)) {
                builder.append("GJB151B-2013标准规定图形:standard");
                builder.append(pic);
                builder.append("\n");
            } else {
                builder.append("研制要求管理系统生成图形:");
                builder.append(picCurrent);
                builder.append("\n");
            }
            builder.append(textCurrent);
        }
        jsonObject.put("限值", builder.toString());
        String resultData = JSON.toJSONString(jsonObject);
        return resultData;
    }



    public String generateDependencySchema(String outlineDevItemId, int pageNumber, String schema) {
        JSONObject jsonSchema = JSON.parseObject(schema, Feature.OrderedField);

        switch (pageNumber) {
            case 10:
                ManageSysOutline outline = manageSysOutlineMapper.selectProjectByDevItemId(outlineDevItemId);
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
            case 58:
                ManageSysOutline outline58 = manageSysOutlineMapper.selectProjectByDevItemId(outlineDevItemId);
                String devItemId58 = outline58.getOutlineDevItemid();
                ManageSysDevelop develop58  = manageSysDevelopMapper.selectByPrimaryKey(devItemId58);
                String projectList = develop58.getProjectList();
                String outlineSchema58 = manageSysSchemaMapper.selectCol(1, "outline_schema_58");
                JSONObject outlineSchema58Object = JSON.parseObject(outlineSchema58, Feature.OrderedField);
                outlineSchema58Object.getJSONObject("properties").getJSONObject("试验项目").getJSONObject("items").getJSONObject("properties").getJSONObject("试验项目名称").put("enum", (JSONArray)JSON.parse(projectList));
                jsonSchema = outlineSchema58Object;
                break;
            default:
                break;
        }

        return JSON.toJSONString(jsonSchema);
    }

    public void fillAntennaWorkStatus(JSONArray antennaArray, String outlineDevItemId, String outlineData, String title, String data){
        JSONObject outlineDataObject = JSON.parseObject(outlineData, Feature.OrderedField);
        outlineDataObject.remove(title);
        outlineDataObject.put(title, antennaArray);
        manageSysOutlineMapper.updateColByOutlineDevItemId(outlineDevItemId, data, JSON.toJSONString(outlineDataObject));

    }

    public void fillLaunchWorkStatus( JSONArray launchArray, String outlineDevItemId, String outlineData, String title, String schema, String data){
        JSONObject outlineData29Object = JSON.parseObject(outlineData, Feature.OrderedField);
        outlineData29Object.remove(title);
        String outlineSchema = manageSysSchemaMapper.selectCol(1, schema);
        JSONObject outlineSchemaObject = JSON.parseObject(outlineSchema, Feature.OrderedField);
//        JSONObject exWorkLaunch;
//        if(schema.equals("outline_schema_26") || schema.equals("outline_schema_27") || schema.equals("outline_schema_28")){
//            exWorkLaunch = outlineSchemaObject.getJSONObject("properties").getJSONObject(title).getJSONObject("properties").getJSONObject("电源端口").getJSONObject("items").getJSONObject("properties").getJSONObject("工作状态").getJSONObject("properties");
//        }else {
//            exWorkLaunch = outlineSchemaObject.getJSONObject("properties").getJSONObject(title).getJSONObject("items").getJSONObject("properties").getJSONObject("工作状态").getJSONObject("properties");
//        }
//        JSONObject oneExLaunch = exWorkLaunch.getJSONObject("工作状态1");
        JSONObject oneExLaunch = (JSONObject)JSON.parse("{\"type\":\"object\",\"title\":\"工作状态1\",\"properties\":{\"工作状态描述\":{\"default\":\"受试设备处于接收状态，调制方式为调频；工作频率：1MHz。\",\"type\":\"string\"},\"状态是否实施\":{\"default\":\"是\",\"type\":\"string\",\"enum\":[\"是\",\"否\"]},\"不实施理由\":{\"default\":\"无\",\"type\":\"string\"}}}");
        JSONObject allWorkLaunch = new JSONObject(true);
        for(int i=0; i<launchArray.size(); i++){
            int num = i+1;
            JSONObject workLaunch = new JSONObject();
            JSONObject workProperties = new JSONObject(true);
            JSONObject workDefault = new JSONObject();
            JSONObject ifAction = new JSONObject();
            JSONObject actionReason = new JSONObject();
            workDefault.put("type", "string");
            workDefault.put("default",launchArray.getJSONObject(i).getString("工作状态") );
            workLaunch.put("type", oneExLaunch.getString("type"));
            workLaunch.put("title","工作状态"+num);
            ifAction.put("type", "string");
            ifAction.put("enum", oneExLaunch.getJSONObject("properties").getJSONObject("状态是否实施").get("enum"));
            ifAction.put("default","是");
            actionReason.put("type", "string");
            actionReason.put("default", "无");
            workProperties.put("工作状态描述", workDefault);
            if(schema.equals("outline_schema_26") || schema.equals("outline_schema_27") || schema.equals("outline_schema_28")){
                JSONObject cableBundle = new JSONObject();
                cableBundle.put("type", "string");
                workProperties.put("施加电缆束", cableBundle);
            }
            workProperties.put("状态是否实施", ifAction);
            workProperties.put("不实施理由", actionReason);
            workLaunch.put("properties", workProperties);
            allWorkLaunch.put("工作状态"+num, workLaunch);
        }
        if(schema.equals("outline_schema_26") || schema.equals("outline_schema_27") || schema.equals("outline_schema_28")){
//            JSONObject allWorkLaunchConnected = (JSONObject) allWorkLaunch.clone();
            JSONObject allWorkLaunchConnected = new JSONObject(true);
            for(int i=0; i<launchArray.size(); i++){
                int numConnected = i+1;
                JSONObject workLaunchConnected = new JSONObject();
                JSONObject workPropertiesConnected = new JSONObject(true);
                JSONObject workDefaultConnected = new JSONObject();
                JSONObject cableBundleConnected = new JSONObject();
                JSONObject ifActionConnected = new JSONObject();
                JSONObject actionReasonConnected = new JSONObject();
                workDefaultConnected.put("type", "string");
                workDefaultConnected.put("default",launchArray.getJSONObject(i).getString("工作状态") );
                workLaunchConnected.put("type", oneExLaunch.getString("type"));
                workLaunchConnected.put("title","工作状态"+numConnected);
                cableBundleConnected.put("type", "string");
                cableBundleConnected.put("default", "");
                ifActionConnected.put("type", "string");
                ifActionConnected.put("enum", oneExLaunch.getJSONObject("properties").getJSONObject("状态是否实施").get("enum"));
                ifActionConnected.put("default","是");
                actionReasonConnected.put("type", "string");
                actionReasonConnected.put("default", "无");
                workPropertiesConnected.put("工作状态描述", workDefaultConnected);
                workPropertiesConnected.put("施加电缆束", cableBundleConnected);
                workPropertiesConnected.put("状态是否实施", ifActionConnected);
                workPropertiesConnected.put("不实施理由", actionReasonConnected);
                workLaunchConnected.put("properties", workPropertiesConnected);
                allWorkLaunchConnected.put("工作状态"+numConnected, workLaunchConnected);
            }
            outlineSchemaObject.getJSONObject("properties").getJSONObject(title).getJSONObject("properties").getJSONObject("电源端口").getJSONObject("items").getJSONObject("properties").getJSONObject("工作状态").put("properties", allWorkLaunch);
            outlineSchemaObject.getJSONObject("properties").getJSONObject(title).getJSONObject("properties").getJSONObject("互联端口").getJSONObject("items").getJSONObject("properties").getJSONObject("工作状态").put("properties", allWorkLaunchConnected);
        }else {
            outlineSchemaObject.getJSONObject("properties").getJSONObject(title).getJSONObject("items").getJSONObject("properties").getJSONObject("工作状态").put("properties", allWorkLaunch);
        }
        manageSysSchemaMapper.updateCol(1, schema, JSON.toJSONString(outlineSchemaObject));
        manageSysOutlineMapper.updateColByOutlineDevItemId(outlineDevItemId, data, JSON.toJSONString(outlineData29Object));
        System.out.println(schema+JSON.toJSONString(outlineSchemaObject));
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

    public String getSubsysOrEqpHead(String outlineDevItemId) {
        ManageSysOutline manageSysOutline = manageSysOutlineMapper.selectProjectByDevItemId(outlineDevItemId);
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

    public void submitSubsysOrEqpHead(String outlineDevItemId, String subSysOrEqpData) throws Exception {
        manageSysOutlineMapper.updateColByOutlineDevItemId(outlineDevItemId, "outline_data_subsys_eqp", subSysOrEqpData);
    }

    private  boolean noPortSelect(JSONArray jsonArray){
        boolean port = true;
        int portNum = 0;
        for(int i=0; i<jsonArray.size(); i++){
            System.out.println("电源端口："+jsonArray.getString(i));
            if(jsonArray.getString(i).equals("0")){
                portNum++;
            }
        }
        if(portNum == jsonArray.size()){
            port = false;
        }
        System.out.println("port:"+port);
        return port;
    }
    //设置新建35-58页的限值范围值
    public JSONObject putLimitRangeValue(JSONObject jsonObject, int[] positionNums, List<String> imgNumList, String equipName) {
        double[] limitValueRange;
        limitValueRange = standardLimitValueService.getImgDetails(imgNumList);
        String[] limitValue = DoubleIntoInteger.doubleIntoInteger(limitValueRange);
        String putLimitValueRes = "频率范围：" + "\n" + limitValue[0] + "Hz" + "~" +  limitValue[1] + "Hz";
        for (int position : positionNums) {
            jsonObject.getJSONArray(equipName).getJSONObject(position).put("主要性能指标", putLimitValueRes);
        }
        return jsonObject;
    }


    //获取14-34页用频设备工作状态描述
    public void getFreqWorkStatus(ManageSysDevelop manageSysDevelop) {
        String string1 = "1";
        String string2 = "2";
        array1001 = new JSONArray();
        array1002 = new JSONArray();
        array1003 = new JSONArray();
        array1004 = new JSONArray();
        array1005 = new JSONArray();
        array1006 = new JSONArray();
        arrayRE103 = new JSONArray();
        antennaCE106 = new JSONArray();
        antennaCS = new JSONArray();
        antennaCS105 = new JSONArray();
        array1006.add("{\"type\":\"object\",\"title\":\"CE107-17\",\"properties\":{\"试验项目\":{\"type\":\"string\",\"default\":\"CE107\"},\"试验目的\":{\"type\":\"string\",\"default\":\"考核被试品因开关操作在输入电源线上产生的上的传导发射是否符合GJB151B规定。\"},\"试验内容\":{\"type\":\"string\",\"default\":\"电源线尖峰信号传导发射\"},\"限值\":{\"type\":\"string\",\"format\":\"textarea\"},\"数据处理方法\":{\"type\":\"string\",\"format\":\"textarea\",\"default\":\"测试数据为尖峰信号的电压幅度、极性、半峰值脉冲宽度，并提供波形图。\"},\"测试结果评定准则\":{\"type\":\"string\",\"default\":\"被试品传导发射实测值不超过限值要求，则判为合格，否则为不合格。\"},\"试验端口及被试品工作状态\":{\"type\":\"array\",\"format\":\"tab\",\"items\":{\"type\":\"object\",\"headerTemplate\":\"试验电源端口{{ i1 }}\",\"properties\":{\"试验电源端口\":{\"type\":\"string\"},\"工作状态\":{\"type\":\"array\",\"format\":\"tab\",\"items\":{\"type\":\"object\",\"headerTemplate\":\"工作状态{{ i1 }}\",\"properties\":{\"\":{\"type\":\"object\",\"properties\":{\"工作状态描述\":{\"type\":\"string\"},\"状态是否实施\":{\"type\":\"string\",\"enum\":[\"是\",\"否\"],\"default\":\"是\"},\"不实施理由\":{\"type\":\"string\",\"default\":\"无\"}}}}}},\"开关状态\":{\"type\":\"string\"},\"端口是否实施\":{\"type\":\"string\",\"enum\":[\"是\",\"否\"],\"default\":\"是\"},\"不实施理由\":{\"type\":\"string\",\"default\":\"无\"},\"备注\":{\"type\":\"string\"}}}}}}");
//        JSONArray devFreqOptional = (JSONArray) JSON.parse(manageSysDevelop.getDevFreqOptional());
        JSONObject devFreqFHLow = (JSONObject) JSON.parse(manageSysDevelop.getDevFreqFhLow());
        JSONObject devFreqFHMid = (JSONObject) JSON.parse(manageSysDevelop.getDevFreqFhMid());
        JSONObject devFreqFHHigh = (JSONObject) JSON.parse(manageSysDevelop.getDevFreqFhHigh());
        JSONObject devFreqDSSS = (JSONObject) JSON.parse(manageSysDevelop.getDevFreqDsss());
        JSONArray optList = (JSONArray) JSON.parse(manageSysDevelop.getDevFreqOptional());
        int optListLength = optList.size();
        for (int len = 0; len < optListLength; len++) {
            JSONArray antennaArrayCE106 = new JSONArray();
            JSONArray antennaArrayCS = new JSONArray();
            JSONObject freqOptional = optList.getJSONObject(len);
            int devFreSelect = Integer.parseInt(optList.getJSONObject(len).getString("opt_fre_select_option")); //用频方式选择
            int devReceiveLaunch = Integer.parseInt(optList.getJSONObject(len).getString("opt_work_style")); //接收方式读取
            String devAntennaName = optList.getJSONObject(len).getString("opt_port_name"); //天线端口名称
            String optInstallMode = freqOptional.getString("opt_install_mode");
//        if(!devFreqOptional.isEmpty()){
            if (devFreSelect == 1) {
                if (devReceiveLaunch == 1 || devReceiveLaunch == 3) {
                        StringBuilder string1001 = new StringBuilder();
                        StringBuilder string1003_low = new StringBuilder();
                        StringBuilder string1003_mid = new StringBuilder();
                        StringBuilder string1003_high = new StringBuilder();
                        StringBuilder string1005_low = new StringBuilder();
                        StringBuilder string1005_mid = new StringBuilder();
                        StringBuilder string1005_high = new StringBuilder();
                        StringBuilder string1003_low_CE106 = new StringBuilder();
                        StringBuilder string1003_mid_CE106 = new StringBuilder();
                        StringBuilder string1003_high_CE106 = new StringBuilder();
                        string1001.append("工作方式：发；最大发射功率（W）=").append(freqOptional.getString("opt_ave_pow_transmit_max")).append("(dBW)；调制方式：").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；中频率：").append(freqOptional.getString("opt_freq_mid")).append("MHz。");
                        string1003_low.append("工作方式：发；最大发射功率（W）=").append(freqOptional.getString("opt_ave_pow_transmit_max")).append("(dBW)；调制方式：").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；低频率：").append(freqOptional.getString("opt_freq_low")).append("MHz。");
                        string1003_mid.append("工作方式：发；最大发射功率（W）=").append(freqOptional.getString("opt_ave_pow_transmit_max")).append("(dBW)；调制方式：").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；中频率：").append(freqOptional.getString("opt_freq_mid")).append("MHz。");
                        string1003_high.append("工作方式：发；最大发射功率（W）=").append(freqOptional.getString("opt_ave_pow_transmit_max")).append("(dBW)；调制方式：").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；高频率：").append(freqOptional.getString("opt_freq_high")).append("MHz。");
                        string1003_low_CE106.append("工作方式：待发；调制方式：").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；低频率：").append(freqOptional.getString("opt_freq_low")).append("MHz。");
                        string1003_mid_CE106.append("工作方式：待发；调制方式：").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；中频率：").append(freqOptional.getString("opt_freq_mid")).append("MHz。");
                        string1003_high_CE106.append("工作方式：待发；调制方式：").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；高频率：").append(freqOptional.getString("opt_freq_high")).append("MHz。");
//                    string1005_low.append("受试设备处于接收状态，最大发射平均功率为").append(freqOptional.getString("opt_ave_pow_transmit_max")).append("(dBW)；调制方式为").append(freqOptional.getJSONObject("opt_modulation_mode_num").getString("opt_modulation_mode_1")).append("；工作频率：").append(freqOptional.getString("opt_freq_low")).append("MHz。");
//                    string1005_mid.append("受试设备处于接收状态，最大发射平均功率为").append(freqOptional.getString("opt_ave_pow_transmit_max")).append("(dBW)；调制方式为").append(freqOptional.getJSONObject("opt_modulation_mode_num").getString("opt_modulation_mode_1")).append("；工作频率：").append(freqOptional.getString("opt_freq_mid")).append("MHz。");
//                    string1005_high.append("受试设备处于接收状态，最大发射平均功率为").append(freqOptional.getString("opt_ave_pow_transmit_max")).append("(dBW)；调制方式为").append(freqOptional.getJSONObject("opt_modulation_mode_num").getString("opt_modulation_mode_1")).append("；工作频率：").append(freqOptional.getString("opt_freq_high")).append("MHz。");
                        string1005_low.append("工作方式：收；调制方式为").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；低频率：").append(freqOptional.getString("opt_freq_low")).append("MHz。");
                        string1005_mid.append("工作方式：收；调制方式为").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；中频率：").append(freqOptional.getString("opt_freq_mid")).append("MHz。");
                        string1005_high.append("工作方式：收；调制方式为").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；高频率：").append(freqOptional.getString("opt_freq_high")).append("MHz。");
                        JSONObject json1001 = new JSONObject();
                        JSONObject json1003_low = new JSONObject();
                        JSONObject json1003_mid = new JSONObject();
                        JSONObject json1003_high = new JSONObject();
                        JSONObject json1005_low = new JSONObject();
                        JSONObject json1005_mid = new JSONObject();
                        JSONObject json1005_high = new JSONObject();
                        JSONObject json1003_low_CE106 = new JSONObject();
                        JSONObject json1003_mid_CE106 = new JSONObject();
                        JSONObject json1003_high_CE106 = new JSONObject();
                        json1001.put("工作状态", string1001.toString());
                        json1003_low.put("工作状态", string1003_low.toString());
                        json1003_mid.put("工作状态", string1003_mid.toString());
                        json1003_high.put("工作状态", string1003_high.toString());
                        json1005_low.put("工作状态", string1005_low.toString());
                        json1005_mid.put("工作状态", string1005_mid.toString());
                        json1005_high.put("工作状态", string1005_high.toString());
                        json1003_low_CE106.put("工作状态", string1003_low_CE106.toString());
                        json1003_mid_CE106.put("工作状态", string1003_mid_CE106.toString());
                        json1003_high_CE106.put("工作状态", string1003_high_CE106.toString());
                        array1001.add(json1001);   //累加一样的代表此种情况下工作状态相同
                        array1002.add(json1001);
                        array1003.add(json1003_low);
                        array1003.add(json1003_mid);
                        array1003.add(json1003_high);
                        array1004.add(json1003_low);
                        array1004.add(json1003_mid);
                        array1004.add(json1003_high);
                        antennaArrayCE106.add(json1003_low_CE106);
                        antennaArrayCE106.add(json1003_low);
                        antennaArrayCE106.add(json1003_mid_CE106);
                        antennaArrayCE106.add(json1003_mid);
                        antennaArrayCE106.add(json1003_high_CE106);
                        antennaArrayCE106.add(json1003_high);
                        if(devReceiveLaunch == 3) {
                            array1005.add(json1005_low);
                            array1005.add(json1005_mid);
                            array1005.add(json1005_high);
                            antennaArrayCS.add(json1005_low);
                            antennaArrayCS.add(json1005_mid);
                            antennaArrayCS.add(json1005_high);
                        }
                        if(string2.equals(optInstallMode)){
                            arrayRE103.add(json1003_low);
                            arrayRE103.add(json1003_mid);
                            arrayRE103.add(json1003_high);
                        }

                }
                if (devReceiveLaunch == 2 || devReceiveLaunch == 3) {
                        StringBuilder string1001 = new StringBuilder();
                        StringBuilder string1003_low = new StringBuilder();
                        StringBuilder string1003_mid = new StringBuilder();
                        StringBuilder string1003_high = new StringBuilder();

//                        String testString2 = optModulationModeEnums.MODULATION_MODE_AM.getMsg();
//                        String testString = optModulationModeEnums.getMsgWithCode("1");
//                        String testString3 = freqOptional.getString("opt_modulation_mode");
//                        String testString1 = optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"));
//                        String testString4 = optModulationModeEnums.getMsgWithCode(testString3);

//                    string1001.append("受试设备处于接收状态，最大发射平均功率为").append(freqOptional.getString("opt_ave_pow_transmit_max")).append("(dBW)；调制方式为").append(freqOptional.getJSONObject("opt_modulation_mode_num").getString("opt_modulation_mode_1")).append("；工作频率：").append(freqOptional.getString("opt_freq_mid")).append("MHz。");
//                    string1003_low.append("受试设备处于接收状态，最大发射平均功率为").append(freqOptional.getString("opt_ave_pow_transmit_max")).append("(dBW)；调制方式为").append(freqOptional.getJSONObject("opt_modulation_mode_num").getString("opt_modulation_mode_1")).append("；工作频率：").append(freqOptional.getString("opt_freq_low")).append("MHz。");
//                    string1003_mid.append("受试设备处于接收状态，最大发射平均功率为").append(freqOptional.getString("opt_ave_pow_transmit_max")).append("(dBW)；调制方式为").append(freqOptional.getJSONObject("opt_modulation_mode_num").getString("opt_modulation_mode_1")).append("；工作频率：").append(freqOptional.getString("opt_freq_mid")).append("MHz。");
//                    string1003_high.append("受试设备处于接收状态，最大发射平均功率为").append(freqOptional.getString("opt_ave_pow_transmit_max")).append("(dBW)；调制方式为").append(freqOptional.getJSONObject("opt_modulation_mode_num").getString("opt_modulation_mode_1")).append("；工作频率：").append(freqOptional.getString("opt_freq_high")).append("MHz。");
                        string1001.append("工作方式：收；调制方式为").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；中频率：").append(freqOptional.getString("opt_freq_mid")).append("MHz。");
                        string1003_low.append("工作方式：收；调制方式为").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；低频率：").append(freqOptional.getString("opt_freq_low")).append("MHz。");
                        string1003_mid.append("工作方式：收；调制方式为").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；中频率：").append(freqOptional.getString("opt_freq_mid")).append("MHz。");
                        string1003_high.append("工作方式：收；调制方式为").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；高频率：").append(freqOptional.getString("opt_freq_high")).append("MHz。");
                        JSONObject json1001 = new JSONObject();
                        JSONObject json1003_low = new JSONObject();
                        JSONObject json1003_mid = new JSONObject();
                        JSONObject json1003_high = new JSONObject();
                        json1001.put("工作状态", string1001.toString());
                        json1003_low.put("工作状态", string1003_low.toString());
                        json1003_mid.put("工作状态", string1003_mid.toString());
                        json1003_high.put("工作状态", string1003_high.toString());
                        array1002.add(json1001);
                        array1004.add(json1003_low);
                        array1004.add(json1003_mid);
                        array1004.add(json1003_high);
                        antennaArrayCE106.add(json1003_low);
                        antennaArrayCE106.add(json1003_mid);
                        antennaArrayCE106.add(json1003_high);
                        if (devReceiveLaunch == 2) {
                            array1001.add(json1001);   //累加一样的代表此种情况下工作状态相同
                            array1003.add(json1003_low);
                            array1003.add(json1003_mid);
                            array1003.add(json1003_high);
                            array1005.add(json1003_low);
                            array1005.add(json1003_mid);
                            array1005.add(json1003_high);
                            antennaArrayCS.add(json1003_low);
                            antennaArrayCS.add(json1003_mid);
                            antennaArrayCS.add(json1003_high);
                            if(string2.equals(optInstallMode)){
                                arrayRE103.add(json1003_low);
                                arrayRE103.add(json1003_mid);
                                arrayRE103.add(json1003_high);
                            }
                        }
                }
//        }else if(!devFreqFHLow.isEmpty()){
            } else if (devFreSelect == 2) {
                if (devReceiveLaunch == 1 || devReceiveLaunch == 3) {
                    StringBuilder string1001_low = new StringBuilder();
                    StringBuilder string1001_mid = new StringBuilder();
                    StringBuilder string1001_high = new StringBuilder();
                    StringBuilder string1005_low = new StringBuilder();
                    StringBuilder string1005_mid = new StringBuilder();
                    StringBuilder string1005_high = new StringBuilder();
                    //跳频中无固定频点
                    string1001_low.append("工作方式：发；调制方式：").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；最大发射功率（W）=").append(freqOptional.getString("opt_ave_pow_transmit_max")).append("(dBW）").append("；低频率：").append(freqOptional.getString("opt_freq_low")).append("MHz～高频率：").append(freqOptional.getString("opt_freq_high")).append("MHz，至少覆盖30%可用频率组。");
                    //调频中有固定频点
                    string1001_mid.append("工作方式：发；调制方式：").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；最大发射功率（W）=").append(freqOptional.getString("opt_ave_pow_transmit_max")).append("(dBW) ").append("；固定频点（MHz）=").append(freqOptional.getString("opt_fix_trans_point"));
                    //CE106的待发状态
                    string1001_high.append("工作方式：待发；调制方式：").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；固定频点（MHz）=").append(freqOptional.getString("opt_fix_trans_point"));

//                string1005_low.append("受试设备处于接收状态，最大发射平均功率为").append(devFreqFHLow.getString("ave_pow_transmit_max")).append("(dBW)").append("；工作频率范围：").append(devFreqFHLow.getString("freq_low")).append("～").append(devFreqFHLow.getString("freq_high")).append("MHz，至少覆盖30%可用频率组。");
//                string1005_mid.append("受试设备处于接收状态，最大发射平均功率为").append(devFreqFHMid.getString("ave_pow_transmit_max")).append("(dBW)").append("；工作频率范围：").append(devFreqFHMid.getString("freq_low")).append("～").append(devFreqFHMid.getString("freq_high")).append("MHz，至少覆盖30%可用频率组。");
//                string1005_high.append("受试设备处于接收状态，最大发射平均功率为").append(devFreqFHHigh.getString("ave_pow_transmit_max")).append("(dBW)").append("；工作频率范围：").append(devFreqFHHigh.getString("freq_low")).append("～").append(devFreqFHHigh.getString("freq_high")).append("MHz，至少覆盖30%可用频率组。");
                    //跳频中无固定频点
                    string1005_low.append("工作方式：收；调制方式：").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；低频率：").append(freqOptional.getString("opt_freq_low")).append("MHz～高频率：").append(freqOptional.getString("opt_freq_high")).append("MHz，至少覆盖30%可用频率组。");
                    //调频中有固定频点
                    string1005_mid.append("工作方式：收；调制方式：").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；固定频点（MHz）=").append(freqOptional.getString("opt_fix_trans_point"));

//                    string1005_high.append("工作方式：收；工作频率范围：").append(devFreqFHHigh.getString("freq_low")).append("～").append(devFreqFHHigh.getString("freq_high")).append("MHz，至少覆盖30%可用频率组。");
                    JSONObject json1001_low = new JSONObject();
                    JSONObject json1001_mid = new JSONObject();
                    JSONObject json1001_high = new JSONObject();
                    JSONObject json1005_low = new JSONObject();
                    JSONObject json1005_mid = new JSONObject();
                    JSONObject json1005_high = new JSONObject();
                    json1001_low.put("工作状态", string1001_low.toString());
                    json1001_mid.put("工作状态", string1001_mid.toString());
                    json1001_high.put("工作状态", string1001_high.toString());
                    json1005_low.put("工作状态", string1005_low.toString());
                    json1005_mid.put("工作状态", string1005_mid.toString());
                    json1005_high.put("工作状态", string1005_high.toString());
                    array1001.add(json1001_low);
//                    array1001.add(json1001_mid);
//                    array1001.add(json1001_high);
                    array1002.add(json1001_low);
//                    array1002.add(json1001_mid);
//                    array1002.add(json1001_high);
//                    array1003.add(json1001_low);
                    array1003.add(json1001_mid);
//                    array1003.add(json1001_high);
                    array1004.add(json1001_low);
//                    array1004.add(json1001_mid);
//                    array1004.add(json1001_high);
                    if(devReceiveLaunch == 3) {
//                        array1005.add(json1005_low);
                        array1005.add(json1005_mid);
                        antennaArrayCS.add(json1005_mid);
//                        array1005.add(json1005_high);
                    }
//                    antennaArrayCE106.add(json1001_low);
                    //CE106先待发工作状态，再发射状态
                    antennaArrayCE106.add(json1001_high);
                    antennaArrayCE106.add(json1001_mid);
                    if(string2.equals(optInstallMode)){
                        arrayRE103.add(json1001_mid);
                    }
                }
                if (devReceiveLaunch == 2 || devReceiveLaunch == 3) {
                    StringBuilder string1001_low = new StringBuilder();
                    StringBuilder string1001_mid = new StringBuilder();
                    StringBuilder string1001_high = new StringBuilder();
//                string1001_low.append("受试设备处于接收状态，最大发射平均功率为").append(devFreqFHLow.getString("ave_pow_transmit_max")).append("(dBW)").append("；工作频率范围：").append(devFreqFHLow.getString("freq_low")).append("～").append(devFreqFHLow.getString("freq_high")).append("MHz，至少覆盖30%可用频率组。");
//                string1001_mid.append("受试设备处于接收状态，最大发射平均功率为").append(devFreqFHMid.getString("ave_pow_transmit_max")).append("(dBW)").append("；工作频率范围：").append(devFreqFHMid.getString("freq_low")).append("～").append(devFreqFHMid.getString("freq_high")).append("MHz，至少覆盖30%可用频率组。");
//                string1001_high.append("受试设备处于接收状态，最大发射平均功率为").append(devFreqFHHigh.getString("ave_pow_transmit_max")).append("(dBW)").append("；工作频率范围：").append(devFreqFHHigh.getString("freq_low")).append("～").append(devFreqFHHigh.getString("freq_high")).append("MHz，至少覆盖30%可用频率组。");
                    //跳频中无固定频点
                    string1001_low.append("工作方式：收；调制方式：").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；低频率：").append(freqOptional.getString("opt_freq_low")).append("MHz～高频率：").append(freqOptional.getString("opt_freq_high")).append("MHz，至少覆盖30%可用频率组。");
                    //调频中有固定频点
                    string1001_mid.append("工作方式：收；调制方式：").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；固定频点（MHz）=").append(freqOptional.getString("opt_fix_trans_point"));

//                    string1001_high.append("受试设备处于接收状态，工作频率范围：").append(devFreqFHHigh.getString("freq_low")).append("～").append(devFreqFHHigh.getString("freq_high")).append("MHz，至少覆盖30%可用频率组。");
                    JSONObject json1001_low = new JSONObject();
                    JSONObject json1001_mid = new JSONObject();
                    JSONObject json1001_high = new JSONObject();
                    json1001_low.put("工作状态", string1001_low.toString());
                    json1001_mid.put("工作状态", string1001_mid.toString());
                    json1001_high.put("工作状态", string1001_high.toString());
                    array1002.add(json1001_low);
//                    array1002.add(json1001_mid);
//                    array1002.add(json1001_high);
                    array1004.add(json1001_low);
//                    array1004.add(json1001_mid);
//                    array1004.add(json1001_high);
//                    antennaArrayCE106.add(json1001_low);
                    antennaArrayCE106.add(json1001_mid);
//                    antennaArrayCE106.add(json1001_high);
                    if (devReceiveLaunch == 2) {
                        array1001.add(json1001_low);
//                        array1001.add(json1001_mid);
//                        array1001.add(json1001_high);
//                        array1003.add(json1001_low);
                        array1003.add(json1001_mid);
//                        array1003.add(json1001_high);
//                        array1005.add(json1001_low);
                        array1005.add(json1001_mid);
                        antennaArrayCS.add(json1001_mid);
//                        array1005.add(json1001_high);
                        if(string2.equals(optInstallMode)){
                            arrayRE103.add(json1001_mid);
                        }
                    }
                }
            } else {
                if (devReceiveLaunch == 1 || devReceiveLaunch == 3) {
                    StringBuilder string1001 = new StringBuilder();
                    StringBuilder string1003_low = new StringBuilder();
                    StringBuilder string1003_mid = new StringBuilder();
                    StringBuilder string1003_high = new StringBuilder();
                    StringBuilder string1005_low = new StringBuilder();
                    StringBuilder string1005_mid = new StringBuilder();
                    StringBuilder string1005_high = new StringBuilder();
                    StringBuilder string1003_low_CE106 = new StringBuilder();
                    StringBuilder string1003_mid_CE106 = new StringBuilder();
                    StringBuilder string1003_high_CE106 = new StringBuilder();
                    string1001.append("工作方式：发；最大发射功率（W）=").append(freqOptional.getString("opt_ave_pow_transmit_max")).append("(dBW)；调制方式：").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；中频率：").append(freqOptional.getString("opt_freq_mid")).append("MHz；最高传输速率：").append(freqOptional.getString("opt_trans_speed")).append("bit/s。");
                    string1003_low.append("工作方式：发；最大发射功率（W）=").append(freqOptional.getString("opt_ave_pow_transmit_max")).append("(dBW)；调制方式：").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；低频率：").append(freqOptional.getString("opt_freq_low")).append("MHz；最高传输速率：").append(freqOptional.getString("opt_trans_speed")).append("bit/s。");
                    string1003_mid.append("工作方式：发；最大发射功率（W）=").append(freqOptional.getString("opt_ave_pow_transmit_max")).append("(dBW)；调制方式：").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；中频率：").append(freqOptional.getString("opt_freq_mid")).append("MHz；最高传输速率：").append(freqOptional.getString("opt_trans_speed")).append("bit/s。");
                    string1003_high.append("工作方式：发；最大发射功率（W）=").append(freqOptional.getString("opt_ave_pow_transmit_max")).append("(dBW)；调制方式：").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；高频率：").append(freqOptional.getString("opt_freq_high")).append("MHz；最高传输速率：").append(freqOptional.getString("opt_trans_speed")).append("bit/s。");
                    string1003_low_CE106.append("工作方式：待发；调制方式：").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；低频率：").append(freqOptional.getString("opt_freq_low")).append("MHz；最高传输速率：").append(freqOptional.getString("opt_trans_speed")).append("bit/s。");
                    string1003_mid_CE106.append("工作方式：待发；调制方式：").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；中频率：").append(freqOptional.getString("opt_freq_mid")).append("MHz；最高传输速率：").append(freqOptional.getString("opt_trans_speed")).append("bit/s。");
                    string1003_high_CE106.append("工作方式：待发；调制方式：").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；高频率：").append(freqOptional.getString("opt_freq_high")).append("MHz；最高传输速率：").append(freqOptional.getString("opt_trans_speed")).append("bit/s。");

//                   string1001.append("受试设备处于发射状态，最大发射平均功率为").append(devFreqDSSS.getString("ave_pow_transmit_max")).append("(dBW)").append("；最高传输速率：").append(devFreqDSSS.getString("trans_rate_max")).append("bit/s。");

//                string1005.append("受试设备处于接收状态，最大发射平均功率为").append(devFreqDSSS.getString("ave_pow_transmit_max")).append("(dBW)").append("；最高传输速率：").append(devFreqDSSS.getString("trans_rate_max")).append("bit/s。");
//                string1005.append("受试设备处于接收状态，最高传输速率：").append(devFreqDSSS.getString("trans_rate_max")).append("bit/s。");
                    string1005_low.append("工作方式：收；调制方式为").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；低频率：").append(freqOptional.getString("opt_freq_low")).append("MHz；最高传输速率：").append(freqOptional.getString("opt_trans_speed")).append("bit/s。");
                    string1005_mid.append("工作方式：收；调制方式为").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；中频率：").append(freqOptional.getString("opt_freq_mid")).append("MHz；最高传输速率：").append(freqOptional.getString("opt_trans_speed")).append("bit/s。");
                    string1005_high.append("工作方式：收；调制方式为").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；高频率：").append(freqOptional.getString("opt_freq_high")).append("MHz；最高传输速率：").append(freqOptional.getString("opt_trans_speed")).append("bit/s。");
                    JSONObject json1001 = new JSONObject();
                    JSONObject json1003_low = new JSONObject();
                    JSONObject json1003_mid = new JSONObject();
                    JSONObject json1003_high = new JSONObject();
                    JSONObject json1005_low = new JSONObject();
                    JSONObject json1005_mid = new JSONObject();
                    JSONObject json1005_high = new JSONObject();
                    JSONObject json1003_low_CE106 = new JSONObject();
                    JSONObject json1003_mid_CE106 = new JSONObject();
                    JSONObject json1003_high_CE106 = new JSONObject();
                    json1001.put("工作状态", string1001.toString());
                    json1003_low.put("工作状态", string1003_low.toString());
                    json1003_mid.put("工作状态", string1003_mid.toString());
                    json1003_high.put("工作状态", string1003_high.toString());
                    json1005_low.put("工作状态", string1005_low.toString());
                    json1005_mid.put("工作状态", string1005_mid.toString());
                    json1005_high.put("工作状态", string1005_high.toString());
                    json1003_low_CE106.put("工作状态", string1003_low_CE106.toString());
                    json1003_mid_CE106.put("工作状态", string1003_mid_CE106.toString());
                    json1003_high_CE106.put("工作状态", string1003_high_CE106.toString());
                    array1001.add(json1001);
                    array1002.add(json1001);
                    array1003.add(json1003_low);
                    array1003.add(json1003_mid);
                    array1003.add(json1003_high);
                    array1004.add(json1003_low);
                    array1004.add(json1003_mid);
                    array1004.add(json1003_high);
                    antennaArrayCE106.add(json1003_low_CE106);
                    antennaArrayCE106.add(json1003_low);
                    antennaArrayCE106.add(json1003_mid_CE106);
                    antennaArrayCE106.add(json1003_mid);
                    antennaArrayCE106.add(json1003_high_CE106);
                    antennaArrayCE106.add(json1003_high);
                    if(devReceiveLaunch == 3) {
                        array1005.add(json1005_low);
                        array1005.add(json1005_mid);
                        array1005.add(json1005_high);
                        antennaArrayCS.add(json1005_low);
                        antennaArrayCS.add(json1005_mid);
                        antennaArrayCS.add(json1005_high);
                    }
                    if(string2.equals(optInstallMode)){
                        arrayRE103.add(json1003_low);
                        arrayRE103.add(json1003_mid);
                        arrayRE103.add(json1003_high);
                    }
                }
                if (devReceiveLaunch == 2 || devReceiveLaunch == 3) {
                    StringBuilder string1001 = new StringBuilder();
                    StringBuilder string1003_low = new StringBuilder();
                    StringBuilder string1003_mid = new StringBuilder();
                    StringBuilder string1003_high = new StringBuilder();
//                string1001.append("受试设备处于接收状态，最大发射平均功率为").append(devFreqDSSS.getString("ave_pow_transmit_max")).append("(dBW)").append("；最高传输速率：").append(devFreqDSSS.getString("trans_rate_max")).append("bit/s。");
//                    string1001.append("受试设备处于接收状态，最高传输速率：").append(devFreqDSSS.getString("trans_rate_max")).append("bit/s。");
                    string1001.append("工作方式：收；调制方式为").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；中频率：").append(freqOptional.getString("opt_freq_mid")).append("MHz；最高传输速率：").append(freqOptional.getString("opt_trans_speed")).append("bit/s。");
                    string1003_low.append("工作方式：收；调制方式为").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；低频率：").append(freqOptional.getString("opt_freq_low")).append("MHz；最高传输速率：").append(freqOptional.getString("opt_trans_speed")).append("bit/s。");
                    string1003_mid.append("工作方式：收；调制方式为").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；中频率：").append(freqOptional.getString("opt_freq_mid")).append("MHz；最高传输速率：").append(freqOptional.getString("opt_trans_speed")).append("bit/s。");
                    string1003_high.append("工作方式：收；调制方式为").append(optModulationModeEnums.getMsgWithCode(freqOptional.getString("opt_modulation_mode"))).append("；高频率：").append(freqOptional.getString("opt_freq_high")).append("MHz；最高传输速率：").append(freqOptional.getString("opt_trans_speed")).append("bit/s。");
                    JSONObject json1001 = new JSONObject();
                    JSONObject json1003_low = new JSONObject();
                    JSONObject json1003_mid = new JSONObject();
                    JSONObject json1003_high = new JSONObject();
                    json1001.put("工作状态", string1001.toString());
                    json1003_low.put("工作状态", string1003_low.toString());
                    json1003_mid.put("工作状态", string1003_mid.toString());
                    json1003_high.put("工作状态", string1003_high.toString());
                    array1002.add(json1001);
                    array1004.add(json1003_low);
                    array1004.add(json1003_mid);
                    array1004.add(json1003_high);
                    antennaArrayCE106.add(json1003_low);
                    antennaArrayCE106.add(json1003_mid);
                    antennaArrayCE106.add(json1003_high);
                    if (devReceiveLaunch == 2) {
                        array1001.add(json1001);
                        array1003.add(json1003_low);
                        array1003.add(json1003_mid);
                        array1003.add(json1003_high);
                        array1005.add(json1003_low);
                        array1005.add(json1003_mid);
                        array1005.add(json1003_high);
                        antennaArrayCS.add(json1003_low);
                        antennaArrayCS.add(json1003_mid);
                        antennaArrayCS.add(json1003_high);
                        if(string2.equals(optInstallMode)){
                            arrayRE103.add(json1003_low);
                            arrayRE103.add(json1003_mid);
                            arrayRE103.add(json1003_high);
                        }
                    }
                }
            }
            //CE106天线端口拼接
            if(string1.equals(optInstallMode)) {
                JSONObject singleAntennaCE106 = new JSONObject();
                singleAntennaCE106.put("天线端口", devAntennaName);
                singleAntennaCE106.put("工作状态", antennaArrayCE106);
                antennaCE106.add(singleAntennaCE106);
            }
            //CS103、CS104、CS105天线端口拼接
            if(devReceiveLaunch != 1) {
                //CS103、CS104天线端口拼接
                JSONObject singleAntennaCS = new JSONObject();
                singleAntennaCS.put("天线端口", devAntennaName);
                singleAntennaCS.put("工作状态", antennaArrayCS);
                antennaCS.add(singleAntennaCS);
                //CS105天线端口
                if (string1.equals(freqOptional.getString("opt_modulation_mode"))) {
                    JSONObject singleAntennaCS105 = new JSONObject();
                    singleAntennaCS105.put("天线端口", devAntennaName);
                    singleAntennaCS105.put("工作状态", antennaArrayCS);
                    antennaCS105.add(singleAntennaCS105);
                }
            }
        }
    }
}
