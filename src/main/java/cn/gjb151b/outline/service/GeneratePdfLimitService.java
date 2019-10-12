package cn.gjb151b.outline.service;

import cn.gjb151b.outline.Constants.PathStoreEnum2;
import cn.gjb151b.outline.dao.ManageSysDevelopMapper;
import cn.gjb151b.outline.model.ManageSysDevelop;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>
 * 为pdf传递限值
 * 类别标识：1 文字 2 双图 3 单图 4 单图+文字 5 特殊处理的项目
 * </p>
 *
 * @Author: teng 870006096@qq.com
 * @Data: Created on 5:48 PM 2019/7/23
 * @Modified By:
 */

public class GeneratePdfLimitService {
    private static int devId;
    private static HashMap<String, String> limitNameMap = new HashMap<String, String>(){
        {
            put("8","适用于水面舰船和潜艇的CE101限值（DC）" );
            put("9","适用于水面舰船和潜艇的CE101限值（50Hz）");
            put("10","适用于水面舰船和潜艇的CE101限值（400Hz）");
            put("11_1","适用于海军ASW飞机，陆军飞机（包括机场维护工作区）和空间系统的CE101限值（受试设备额定电源电压（AC和DC）>28V )");
            put("11_2","适用于海军ASW飞机，陆军飞机（包括机场维护工作区）和空间系统的CE101限值（受试设备额定电源电压（AC和DC）<=28V )");
            put("14","CE102限值（AC和DC）");
            put("CE106_a","EUT天线端口传导发射不应超过以下限值（接收）");
            put("CE106_b_c","EUT天线端口传导发射不应超过以下限值（发射）");
            put("CE106_a_b_c","EUT天线端口传导发射不应超过以下限值（收/发）");
            put("CE107_1","随手动或自动开关操作而产生的开关瞬态传导发射不应超过下列值（交流电源线）");
            put("CE107_2","随手动或自动开关操作而产生的开关瞬态传导发射不应超过下列值（直流电源线）");
            put("21_1","CS101电压限值（受试设备额定电源电压 >28V）");
            put("21_2","CS101电压限值（受试设备额定电源电压 <=28V）");
            put("22","CS101功率限值");
            put("CS102","CS102限值");
            put("CS106","CS106限值");
            put("37","CS109限值");
            put("CS112_10_11_A","限值（A类EUT）");
            put("CS112_10_11_B","限值（B类EUT）");
            put("39_1","CS114校验限值");
            put("39_2","CS114校验限值");
            put("39_3","CS114校验限值");
            put("39_4","CS114校验限值");
            put("39_5","CS114校验限值");
            put("39_6","CS114校验限值");
            put("39_7","CS114校验限值");
            put("39_8","CS114校验限值");
            put("39_9","CS114校验限值");
            put("39_10","CS114校验限值");
            put("39_11","CS114校验限值");
            put("39_12","CS114校验限值");
            put("39_13","CS114校验限值");
            put("44","CS115波形");
            put("CS115","CS115限值");
            put("48","CS116波形");
            put("CS116","CS116限值");
            put("51","适用于陆军的RE101限值");
            put("52","适用于海军的RE101限值");
            put("55_1","适用于水面舰船的RE102限值（甲板下）");
            put("55_2","适用于水面舰船的RE102限值（甲板上）");
            put("56_1","适用于潜艇的RE102限值（压力舱内）");
            put("56_2","适用于潜艇的RE102限值（压力舱外）");
            put("57_1","适用于飞机和空间系统的RE102限值（固定翼飞机内部（首尾间距≧25m））");
            put("57_2","适用于飞机和空间系统的RE102限值（固定翼飞机内部（首尾间距<25m））");
            put("57_3","适用于飞机和空间系统的RE102限值（固定翼飞机外部（2MHz～18GHz）和直升机）");
            put("58_1","适用于地面的RE102限值（海军（固定的）和空军）");
            put("58_2","适用于地面的RE102限值（海军（移动的）和陆军）");
            put("RE103","RE103限值");
            put("65","适用于海军的RS101限值");
            put("66","适用于陆军的RS101限值");
            put("RS103_1","RS103限值");
            put("RS103_2","RS103限值");
            put("RS103_3","RS103限值");
            put("RS103_4","RS103限值");
            put("RS103_5","RS103限值");
            put("RS103_6","RS103限值");
            put("RS103_7","RS103限值");
            put("RS103_8","RS103限值");
            put("RS103_9","RS103限值");
            put("RS103_10","RS103限值");
            put("73","RS105限值");
        }
    };


    public JSONObject getProjectLimit(JSONArray projectList, Integer currentPicNumber, ManageSysDevelop manageSysDevelop) throws Exception {
        setDevId(devId);
        JSONObject projectLimit = new JSONObject();
        ManageSysDevelop manageSysDevelopEntity = manageSysDevelop;
        int projectLength = projectList.size();
        JSONObject resObject = new JSONObject();
        for(int i=0; i<projectLength; i++){
          switch(projectList.getString(i)){
              case "CE101":
                  JSONArray limitCE101 = new JSONArray();
                  JSONObject devCE101 = (JSONObject) JSON.parse(manageSysDevelopEntity.getDevCe101());
                  JSONArray CE101Limit = devCE101.getJSONArray("limit_value");
                  JSONArray CE101LimitCurrent = devCE101.getJSONArray("limit_value_current");
                  for(int j=0; j<3 ;j++){
                      if(!CE101Limit.getJSONObject(j).isEmpty()){
                          String picNum = CE101Limit.getJSONObject(j).getString("pic");
//                          String picCurrent = CE101LimitCurrent.getJSONObject(j).getString("pic");
                          String pic = getLimitPic("CE101", CE101Limit.getJSONObject(j).getString("pic"), CE101LimitCurrent.getJSONObject(j).getString("pic"), j+1);
                          JSONObject singleLimit = new JSONObject();
                          singleLimit.put("category", 3);
                          singleLimit.put("pic", pic);
                          singleLimit.put("title", limitNameMap.get(picNum));
                          limitCE101.add(singleLimit);
                      }
                  }
                  String CE101ValuePicPath = getCurrentPicPath(manageSysDevelopEntity.getDevName(), limitCE101.getJSONObject(0).getString("pic"));
                  String CE101ValuePicTitle = "图6-" + currentPicNumber + "\t" + limitCE101.getJSONObject(0).getString("title");
                  resObject = limitCE101.getJSONObject(0);
                  resObject.put("valuePicPath", CE101ValuePicPath);
                  resObject.put("valuePicTitle", CE101ValuePicTitle);
                  resObject.put("nextPicNumber", currentPicNumber + 1);
                  return resObject;
              case "CE102":
                  JSONArray limitCE102 = new JSONArray();
                  JSONObject devCE102 = (JSONObject)JSON.parse(manageSysDevelopEntity.getDevCe102());
                  JSONObject CE102Limit = devCE102.getJSONObject("limit_value");
                  JSONObject CE102LimitCurrent = devCE102.getJSONObject("limit_value_current");
                  String picNum = CE102Limit.getString("pic");
                  String pic = getLimitPic("CE102", picNum, CE102LimitCurrent.getString("pic"), 0);
                  JSONObject singleLimit = new JSONObject();
                  singleLimit.put("category", 3);
                  singleLimit.put("pic", pic);
                  singleLimit.put("title", limitNameMap.get(picNum));
                  limitCE102.add(singleLimit);
                  projectLimit.put("CE102", limitCE102);
                  String CE102ValuePicPath = getCurrentPicPath(manageSysDevelopEntity.getDevName(), limitCE102.getJSONObject(0).getString("pic"));
                  String CE102ValuePicTitle = "图6-" + currentPicNumber + "\t" + limitCE102.getJSONObject(0).getString("title");
                  resObject = limitCE102.getJSONObject(0);
                  resObject.put("valuePicPath", CE102ValuePicPath);
                  resObject.put("valuePicTitle", CE102ValuePicTitle);
                  resObject.put("nextPicNumber", currentPicNumber + 1);
                  return resObject;
              case "CE106":
                  JSONArray limitCE106 = new JSONArray();
                  JSONObject devCE106 = (JSONObject)JSON.parse(manageSysDevelopEntity.getDevCe106());
                  String CE106LimitCurrent = devCE106.getJSONObject("limit_value_current").getString("text");
                  JSONObject singleLimitCE106 = new JSONObject();
                  singleLimitCE106.put("category", 1);
                  singleLimitCE106.put("text", CE106LimitCurrent);
                  limitCE106.add(singleLimitCE106);
                  projectLimit.put("CE106", limitCE106);
                  resObject = limitCE106.getJSONObject(0);
                  resObject.put("valueText", CE106LimitCurrent);
                  return resObject;
              case "CE107":
                  JSONArray limitCE107 = new JSONArray();
                  JSONObject devCE107 = (JSONObject)JSON.parse(manageSysDevelopEntity.getDevCe107());
                  JSONArray CE107LimitCurrent = devCE107.getJSONArray("limit_value_current");
                  for(int j=0; j<2; j++){
                      if(!CE107LimitCurrent.getJSONObject(j).isEmpty()){
                          JSONObject singleLimitCE107 = new JSONObject();
                          singleLimitCE107.put("category", 1);
                          singleLimitCE107.put("text", CE107LimitCurrent.getJSONObject(j).getString("text"));
                          limitCE107.add(singleLimitCE107);
                      }
                  }
                  projectLimit.put("CE107", limitCE107);
                  resObject = limitCE107.getJSONObject(0);
                  resObject.put("valueText", CE107LimitCurrent);
                  return resObject;
              case "CS101":
                  JSONArray limitCS101 = new JSONArray();
                  JSONObject devCS101 = (JSONObject) JSON.parse(manageSysDevelopEntity.getDevCs101());
                  JSONArray CS101Limit = devCS101.getJSONArray("limit_value");
                  JSONArray CS101LimitCurrent = devCS101.getJSONArray("limit_value_current");
                  for(int j=0; j<2; j++){
                      if(!CS101Limit.getJSONObject(j).isEmpty()){
                          String picNum1 = CS101Limit.getJSONObject(j).getString("pic_one");
                          String picNum2 = CS101Limit.getJSONObject(j).getString("pic_two");
                          String pic1 = getLimitBiPic("CS101", picNum1, CS101LimitCurrent.getJSONObject(j).getString("pic_one"), j+1, 1);
                          String pic2 = getLimitBiPic("CS101", picNum2, CS101LimitCurrent.getJSONObject(j).getString("pic_two"), j+1, 2);
                          JSONObject singleLimitCS101 = new JSONObject();
                          singleLimitCS101.put("category", 2);
                          singleLimitCS101.put("pic1", pic1);
                          singleLimitCS101.put("title1", limitNameMap.get(picNum1));
                          singleLimitCS101.put("pic2", pic2);
                          singleLimitCS101.put("title2", limitNameMap.get(picNum2));
                          limitCS101.add(singleLimitCS101);
                      }
                  }
                  projectLimit.put("CS101", limitCS101);
                  String CS101ValuePic1Path = getCurrentPicPath(manageSysDevelopEntity.getDevName(), limitCS101.getJSONObject(0).getString("pic1"));
                  String CS101ValuePic1Title = "图6-" + currentPicNumber + "\t" + limitCS101.getJSONObject(0).getString("title1");
                  currentPicNumber++;
                  String CS101ValuePic2Path = getCurrentPicPath(manageSysDevelopEntity.getDevName(), limitCS101.getJSONObject(0).getString("pic2"));
                  String CS101ValuePic12Title = "图6-" + currentPicNumber + "\t" + limitCS101.getJSONObject(0).getString("title2");
                  resObject = limitCS101.getJSONObject(0);
                  resObject.put("valuePic1Path", CS101ValuePic1Path);
                  resObject.put("valuePic1Title", CS101ValuePic1Title);
                  resObject.put("valuePic2Path", CS101ValuePic2Path);
                  resObject.put("valuePic2Title", CS101ValuePic12Title);
                  return resObject;
              case "CS102":
                  JSONArray limitCS102 = new JSONArray();
                  JSONObject devCS102 = (JSONObject)JSON.parse(manageSysDevelopEntity.getDevCs102());
                  String CS102LimitCurrent = devCS102.getJSONObject("limit_value_current").getString("text");
                  JSONObject singleLimitCS102 = new JSONObject();
                  singleLimitCS102.put("category", 1);
                  singleLimitCS102.put("text", CS102LimitCurrent);
                  limitCS102.add(singleLimitCS102);
                  projectLimit.put("CS102", limitCS102);
                  resObject.put("valueText", CS102LimitCurrent);
                  return resObject;
              case "CS103":
                  //?特殊项目暂时不知道显示什么
                  JSONArray limitCS103 = new JSONArray();
                  JSONObject devCS103 = (JSONObject)JSON.parse(manageSysDevelopEntity.getDevCs103());
                  JSONObject singleLimitCS103 = new JSONObject();
                  singleLimitCS103.put("category", 5);
                  limitCS103.add(singleLimitCS103);
                  projectLimit.put("CS103", limitCS103);
                  break;
              case "CS104":
                  //?特殊项目暂时不知道显示什么
                  JSONArray limitCS104 = new JSONArray();
                  JSONObject devCS104 = (JSONObject)JSON.parse(manageSysDevelopEntity.getDevCs104());
                  JSONObject singleLimitCS104 = new JSONObject();
                  singleLimitCS104.put("category", 5);
                  limitCS104.add(singleLimitCS104);
                  projectLimit.put("CS104", limitCS104);
                  break;
              case "CS105":
                  //?特殊项目暂时不知道显示什么
                  JSONArray limitCS105 = new JSONArray();
                  JSONObject devCS105 = (JSONObject)JSON.parse(manageSysDevelopEntity.getDevCs105());
                  JSONObject singleLimitCS105 = new JSONObject();
                  singleLimitCS105.put("category", 5);
                  limitCS105.add(singleLimitCS105);
                  projectLimit.put("CS105", limitCS105);
                  break;
              case "CS106":
                  JSONArray limitCS106 = new JSONArray();
                  JSONObject devCS106 = (JSONObject)JSON.parse(manageSysDevelopEntity.getDevCs106());
                  String CS106LimitCurrent = devCS106.getJSONObject("limit_value_current").getString("text");
                  JSONObject singleLimitCS106 = new JSONObject();
                  singleLimitCS106.put("category", 1);
                  singleLimitCS106.put("text", CS106LimitCurrent);
                  limitCS106.add(singleLimitCS106);
                  projectLimit.put("CS106", limitCS106);
                  resObject.put("valueText", CS106LimitCurrent);
                  return resObject;
              case "CS109":
                  JSONArray limitCS109 = new JSONArray();
                  JSONObject devCS109 = (JSONObject)JSON.parse(manageSysDevelopEntity.getDevCs109());
                  JSONObject CS109Limit = devCS109.getJSONObject("limit_value");
                  JSONObject CS109LimitCurrent = devCS109.getJSONObject("limit_value_current");
                  String picNumCS109 =CS109Limit.getString("pic");
                  String picCS109 = getLimitPic("CS109", picNumCS109, CS109LimitCurrent.getString("pic"), 0);
                  JSONObject singleLimitCS109 = new JSONObject();
                  singleLimitCS109.put("category", 3);
                  singleLimitCS109.put("pic", picCS109);
                  singleLimitCS109.put("title", limitNameMap.get(picNumCS109));
                  limitCS109.add(singleLimitCS109);
                  projectLimit.put("CS109", limitCS109);
                  String CS109ValuePicPath = getCurrentPicPath(manageSysDevelopEntity.getDevName(), limitCS109.getJSONObject(0).getString("pic"));
                  String CS109ValuePicTitle = "图6-" + currentPicNumber + "\t" + limitCS109.getJSONObject(0).getString("title");
                  resObject.put("valuePicPath", CS109ValuePicPath);
                  resObject.put("valuePicTitle", CS109ValuePicTitle);
                  return resObject;
              case "CS112":
                  JSONArray limitCS112 = new JSONArray();
                  JSONObject devCS112 = (JSONObject)JSON.parse(manageSysDevelopEntity.getDevCs112());
                  String CS112LimitCurrent = devCS112.getJSONObject("limit_value_current").getString("text");
                  JSONObject singleLimitCS112 = new JSONObject();
                  singleLimitCS112.put("category", 1);
                  singleLimitCS112.put("text", CS112LimitCurrent);
                  limitCS112.add(singleLimitCS112);
                  projectLimit.put("CS112", limitCS112);
                  resObject.put("valueText", CS112LimitCurrent);
                  return resObject;
              case "CS114":
                  JSONArray limitCS114 = new JSONArray();
                  JSONObject devCS114 = (JSONObject)JSON.parse(manageSysDevelopEntity.getDevCs114());
                  JSONObject CS114Limit = devCS114.getJSONObject("limit_value");
                  JSONObject CS114LimitCurrent = devCS114.getJSONObject("limit_value_current");
                  String picNumCS114 =CS114Limit.getString("pic");
                  String picCS114 = getLimitPic("CS114", picNumCS114, CS114LimitCurrent.getString("pic"), 0);
                  JSONObject singleLimitCS114 = new JSONObject();
                  singleLimitCS114.put("category", 3);
                  singleLimitCS114.put("pic", picCS114);
                  singleLimitCS114.put("title", limitNameMap.get(picNumCS114));
                  limitCS114.add(singleLimitCS114);
                  projectLimit.put("CS114", limitCS114);
                  String CS114ValuePicPath = getCurrentPicPath(manageSysDevelopEntity.getDevName(), limitCS114.getJSONObject(0).getString("pic"));
                  String CS114ValuePicTitle = "图6-" + currentPicNumber + "\t" + limitCS114.getJSONObject(0).getString("title");
                  resObject.put("valuePicPath", CS114ValuePicPath);
                  resObject.put("valuePicTitle", CS114ValuePicTitle);
                  return resObject;
              case "CS115":
                  JSONArray limitCS115 = new JSONArray();
                  JSONObject devCS115 = (JSONObject)JSON.parse(manageSysDevelopEntity.getDevCs115());
                  JSONObject CS115Limit = devCS115.getJSONObject("limit_value");
                  JSONObject CS115LimitCurrent = devCS115.getJSONObject("limit_value_current");
                  String picNumCS115 =CS115Limit.getString("pic");
                  String picCS115 = getLimitPic("CS115", picNumCS115, CS115LimitCurrent.getString("pic"), 0);
                  JSONObject singleLimitCS115 = new JSONObject();
                  singleLimitCS115.put("category", 4);
                  singleLimitCS115.put("pic", picCS115);
                  singleLimitCS115.put("title", limitNameMap.get(picNumCS115));
                  singleLimitCS115.put("text", CS115LimitCurrent.getString("text"));
                  limitCS115.add(singleLimitCS115);
                  projectLimit.put("CS115", limitCS115);
                  resObject.put("valueText", CS115LimitCurrent.getString("text"));
                  String CS115ValuePicPath = getCurrentPicPath(manageSysDevelopEntity.getDevName(), limitCS115.getJSONObject(0).getString("pic"));
                  String CS115ValuePicTitle = "图6-" + currentPicNumber + "\t" + limitCS115.getJSONObject(0).getString("title");
                  resObject.put("valuePicPath", CS115ValuePicPath);
                  resObject.put("valuePicTitle", CS115ValuePicTitle);
                  return resObject;
              case "CS116":
                  JSONArray limitCS116 = new JSONArray();
                  JSONObject devCS116 = (JSONObject)JSON.parse(manageSysDevelopEntity.getDevCs116());
                  JSONObject CS116Limit = devCS116.getJSONObject("limit_value");
                  JSONObject CS116LimitCurrent = devCS116.getJSONObject("limit_value_current");
                  String picNumCS116 =CS116Limit.getString("pic");
                  String picCS116 = getLimitPic("CS116", picNumCS116, CS116LimitCurrent.getString("pic"), 0);
                  JSONObject singleLimitCS116 = new JSONObject();
                  singleLimitCS116.put("category", 4);
                  singleLimitCS116.put("pic", picCS116);
                  singleLimitCS116.put("title", limitNameMap.get(picNumCS116));
                  singleLimitCS116.put("text", CS116LimitCurrent.getString("text"));
                  limitCS116.add(singleLimitCS116);
                  projectLimit.put("CS116", limitCS116);
                  resObject.put("valueText", CS116LimitCurrent.getString("text"));
                  String CS116ValuePicPath = getCurrentPicPath(manageSysDevelopEntity.getDevName(), limitCS116.getJSONObject(0).getString("pic"));
                  String CS116ValuePicTitle = "图6-" + currentPicNumber + "\t" + limitCS116.getJSONObject(0).getString("title");
                  resObject.put("valuePicPath", CS116ValuePicPath);
                  resObject.put("valuePicTitle", CS116ValuePicTitle);
                  return resObject;
              case "RE101":
                  JSONArray limitRE101 = new JSONArray();
                  JSONObject devRE101 = (JSONObject)JSON.parse(manageSysDevelopEntity.getDevRe101());
                  JSONObject RE101Limit = devRE101.getJSONObject("limit_value");
                  JSONObject RE101LimitCurrent = devRE101.getJSONObject("limit_value_current");
                  String picNumRE101 =RE101Limit.getString("pic");
                  String picRE101 = getLimitPic("RE101", picNumRE101, RE101LimitCurrent.getString("pic"), 0);
                  JSONObject singleLimitRE101 = new JSONObject();
                  singleLimitRE101.put("category", 3);
                  singleLimitRE101.put("pic", picRE101);
                  singleLimitRE101.put("title", limitNameMap.get(picNumRE101));
                  limitRE101.add(singleLimitRE101);
                  projectLimit.put("RE101", limitRE101);
                  String RE101ValuePicPath = getCurrentPicPath(manageSysDevelopEntity.getDevName(), limitRE101.getJSONObject(0).getString("pic"));
                  String RE101ValuePicTitle = "图6-" + currentPicNumber + "\t" + limitRE101.getJSONObject(0).getString("title");
                  resObject.put("valuePicPath", RE101ValuePicPath);
                  resObject.put("valuePicTitle", RE101ValuePicTitle);
                  return resObject;
              case "RE102":
                  JSONArray limitRE102 = new JSONArray();
                  JSONObject devRE102 = (JSONObject)JSON.parse(manageSysDevelopEntity.getDevRe102());
                  JSONObject RE102Limit = devRE102.getJSONObject("limit_value");
                  JSONObject RE102LimitCurrent = devRE102.getJSONObject("limit_value_current");
                  String picNumRE102 =RE102Limit.getString("pic");
                  String picRE102 = getLimitPic("RE102", picNumRE102, RE102LimitCurrent.getString("pic"), 0);
                  JSONObject singleLimitRE102 = new JSONObject();
                  singleLimitRE102.put("category", 3);
                  singleLimitRE102.put("pic", picRE102);
                  singleLimitRE102.put("title", limitNameMap.get(picNumRE102));
                  limitRE102.add(singleLimitRE102);
                  projectLimit.put("RE102", limitRE102);
                  String RE102ValuePicPath = getCurrentPicPath(manageSysDevelopEntity.getDevName(), limitRE102.getJSONObject(0).getString("pic"));
                  String RE102ValuePicTitle = "图6-" + currentPicNumber + "\t" + limitRE102.getJSONObject(0).getString("title");
                  resObject.put("valuePicPath", RE102ValuePicPath);
                  resObject.put("valuePicTitle", RE102ValuePicTitle);
                  return resObject;
              case "RE103":
                  JSONArray limitRE103 = new JSONArray();
                  JSONObject devRE103 = (JSONObject)JSON.parse(manageSysDevelopEntity.getDevRe103());
                  String RE103LimitCurrent = devRE103.getJSONObject("limit_value_current").getString("text");
                  JSONObject singleLimitRE103 = new JSONObject();
                  singleLimitRE103.put("category", 1);
                  singleLimitRE103.put("text", RE103LimitCurrent);
                  limitRE103.add(singleLimitRE103);
                  projectLimit.put("RE103", limitRE103);
                  resObject.put("valueText", RE103LimitCurrent);
                  return resObject;
              case "RS101":
                  JSONArray limitRS101 = new JSONArray();
                  JSONObject devRS101 = (JSONObject)JSON.parse(manageSysDevelopEntity.getDevRs101());
                  JSONObject RS101Limit = devRS101.getJSONObject("limit_value");
                  JSONObject RS101LimitCurrent = devRS101.getJSONObject("limit_value_current");
                  String picNumRS101 =RS101Limit.getString("pic");
                  String picRS101 = getLimitPic("RS101", picNumRS101, RS101LimitCurrent.getString("pic"), 0);
                  JSONObject singleLimitRS101 = new JSONObject();
                  singleLimitRS101.put("category", 3);
                  singleLimitRS101.put("pic", picRS101);
                  singleLimitRS101.put("title", limitNameMap.get(picNumRS101));
                  limitRS101.add(singleLimitRS101);
                  projectLimit.put("RS101", limitRS101);
                  String RS101ValuePicPath = getCurrentPicPath(manageSysDevelopEntity.getDevName(), limitRS101.getJSONObject(0).getString("pic"));
                  String RS101ValuePicTitle = "图6-" + currentPicNumber + "\t" + limitRS101.getJSONObject(0).getString("title");
                  resObject.put("valuePicPath", RS101ValuePicPath);
                  resObject.put("valuePicTitle", RS101ValuePicTitle);
                  return resObject;
              case "RS103":
                  JSONArray limitRS103 = new JSONArray();
                  JSONObject devRS103 = (JSONObject)JSON.parse(manageSysDevelopEntity.getDevRs103());
                  JSONObject RS103Limit = devRS103.getJSONObject("limit_value");
                  JSONObject RS103LimitCurrent = devRS103.getJSONObject("limit_value_current");
                  String picNumRS103 =RS103Limit.getString("pic");
                  String picRS103 = getLimitPic("RS103", picNumRS103, RS103LimitCurrent.getString("pic"), 0);
                  JSONObject singleLimitRS103 = new JSONObject();
                  singleLimitRS103.put("category", 3);
                  singleLimitRS103.put("pic", picRS103);
                  singleLimitRS103.put("title", limitNameMap.get(picNumRS103));
                  limitRS103.add(singleLimitRS103);
                  projectLimit.put("RS103", limitRS103);
                  String RS103ValuePicPath = getCurrentPicPath(manageSysDevelopEntity.getDevName(), limitRS103.getJSONObject(0).getString("pic"));
                  String RS103ValuePicTitle = "图6-" + currentPicNumber + "\t" + limitRS103.getJSONObject(0).getString("title");
                  resObject.put("valuePicPath", RS103ValuePicPath);
                  resObject.put("valuePicTitle", RS103ValuePicTitle);
                  return resObject;
              case "RS105":
                  JSONArray limitRS105 = new JSONArray();
                  JSONObject devRS105 = (JSONObject)JSON.parse(manageSysDevelopEntity.getDevRs105());
                  JSONObject RS105Limit = devRS105.getJSONObject("limit_value");
                  JSONObject RS105LimitCurrent = devRS105.getJSONObject("limit_value_current");
                  String picNumRS105 =RS105Limit.getString("pic");
                  String picRS105 = getLimitPic("RS105", picNumRS105, RS105LimitCurrent.getString("pic"), 0);
                  JSONObject singleLimitRS105 = new JSONObject();
                  singleLimitRS105.put("category", 3);
                  singleLimitRS105.put("pic", picRS105);
                  singleLimitRS105.put("title", limitNameMap.get(picNumRS105));
                  limitRS105.add(singleLimitRS105);
                  projectLimit.put("RS105", limitRS105);
                  String RS105ValuePicPath = getCurrentPicPath(manageSysDevelopEntity.getDevName(), limitRS105.getJSONObject(0).getString("pic"));
                  String RS105ValuePicTitle = "图6-" + currentPicNumber + "\t" + limitRS105.getJSONObject(0).getString("title");
                  resObject.put("valuePicPath", RS105ValuePicPath);
                  resObject.put("valuePicTitle", RS105ValuePicTitle);
                  return resObject;
              default:

          }

        }

        return null;
    }

    /**
     * 判断限值单图片是否进行更改
     *
     * @Author: teng 870006096@qq.com
     * @Data: Created on 22:48 PM 2019/7/23
     * @Modified By:
     */

    public static String getLimitPic(String project, String pic, String picCurrent, int location){
        StringBuilder limit = new StringBuilder();
//        if(pic != null){
            if(pic.equals(picCurrent)){
                //标准图库
                limit.append("standard");
                limit.append(pic);
                limit.append(".png");
            }else{
                //更改后的图库前缀
                limit.append("change");
                if(location != 0) {
                    //CE101多限值情况
                    limit.append(devId);
                    limit.append("_");
                    limit.append(location);
                    limit.append("_");
                    limit.append(project);
                    limit.append(".png");
                }else{
                    limit.append(devId);
                    limit.append("_");
                    limit.append(project);
                    limit.append(".png");
                }

            }
//        }
        return limit.toString();
    }

    /**
     * 判断限值双图片是否进行更改
     *
     * @Author: teng 870006096@qq.com
     * @Data: Created on 01:48 PM 2019/7/24
     * @Modified By:
     */
    public static String getLimitBiPic(String project, String pic, String picCurrent, int location, int picLocation){
        StringBuilder limit = new StringBuilder();
//        if(pic != null){
            if(pic.equals(picCurrent)){
                //标准图库
                limit.append("standard");
                limit.append(pic);
                limit.append(".png");
            }else{
                //更改后的图库前缀
                limit.append("change");
                limit.append(devId);
                limit.append("_");
                limit.append(location);
                limit.append("_");
                limit.append(project);
                limit.append("_");
                limit.append(picLocation);
                limit.append(".png");
            }
//        }
        return limit.toString();
    }

    private static String getCurrentPicPath(String devName, String picName) {
        File standardPic = new File(PathStoreEnum2.WINDOWS_STANDADIMG_PATH.getValue() + picName);
        File changedPic = new File(PathStoreEnum2.WINDOWS_CHANGEDIMG_SOURTHPATH.getValue() + devName + "//" + picName);
        if (standardPic.isFile() && standardPic.exists()) {
            return PathStoreEnum2.WINDOWS_STANDADIMG_PATH.getValue() + picName;
        } else if (changedPic.isFile() && changedPic.exists()) {
            return PathStoreEnum2.WINDOWS_CHANGEDIMG_SOURTHPATH.getValue() + devName + "//" + picName;
        } else {
            return null;
        }
    }


    public HashMap<String, String> getLimitNameMap() {return limitNameMap;}

    public void setLimitNameMap(HashMap<String, String> limitNameMap) {this.limitNameMap = limitNameMap;}

    public static int getDevId() {return devId;}

    public static void setDevId(int devId1) {devId = devId1;}
}
