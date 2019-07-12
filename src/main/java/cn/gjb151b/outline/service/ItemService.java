package cn.gjb151b.outline.service;

import cn.gjb151b.outline.Constants.PathStoreEnum;
import cn.gjb151b.outline.action.ItemAction;
import cn.gjb151b.outline.dao.ManageSysDevelopMapper;
import cn.gjb151b.outline.model.*;
import cn.gjb151b.outline.outlineDao.ManageSysOutlineMapper;
import cn.gjb151b.outline.outlineDao.ManageSysSchemaMapper;
import cn.gjb151b.outline.outlineDao.OutlineUserInfoMapper;
import cn.gjb151b.outline.utils.BaseResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.tools.internal.xjc.outline.Outline;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

//添加事务注解
//1.使用 propagation 指定事务的传播行为, 即当前的事务方法被另外一个事务方法调用时
//如何使用事务, 默认取值为 REQUIRED, 即使用调用方法的事务
//REQUIRES_NEW: 事务自己的事务, 调用的事务方法的事务被挂起.
//2.使用 isolation 指定事务的隔离级别, 最常用的取值为 READ_COMMITTED
//3.默认情况下 Spring 的声明式事务对所有的运行时异常进行回滚. 也可以通过对应的
//属性进行设置. 通常情况下去默认值即可.
//4.使用 readOnly 指定事务是否为只读. 表示这个事务只读取数据但不更新数据,
//这样可以帮助数据库引擎优化事务. 若真的只是一个只读取数据库值的方法, 应设置 readOnly=true
//5.使用 timeout 指定强制回滚之前事务可以占用的时间.
@Transactional(propagation = Propagation.REQUIRED,
        isolation = Isolation.READ_COMMITTED,
        readOnly = false,
        timeout = 3)
@Service(value = "ItemService")
public class ItemService {
    private Logger logger = Logger.getLogger(ItemService.class);
    @Resource
    private ManageSysOutlineMapper manageSysOutlineMapper;
    @Resource
    private OutlineUserInfoMapper outlineUserInfoMapper;
    @Resource
    private ManageSysDevelopMapper manageSysDevelopMapper;


    @Autowired
    public ItemService(ManageSysOutlineMapper manageSysOutlineMapper, OutlineUserInfoMapper outlineUserInfoMapper, ManageSysDevelopMapper manageSysDevelopMapper) {
        this.outlineUserInfoMapper = outlineUserInfoMapper;
        this.manageSysOutlineMapper = manageSysOutlineMapper;
        this.manageSysDevelopMapper = manageSysDevelopMapper;
    }
    @Autowired
    private ManageSysSchemaMapper manageSysSchemaMapper;

    public BaseResponse findAllItem() {
        BaseResponse findAllItemResponse = new BaseResponse();
        List<ManageSysOutline> item = manageSysOutlineMapper.findAllItem();
        List<ItemOperater> itemList = new ArrayList<>();
        if (item == null) {
            findAllItemResponse.setStatus("error");
            findAllItemResponse.setMessage("目前没有项目");

        } else {
            for (ManageSysOutline manageSysOutline : item) {
                ItemOperater itemOperater = new ItemOperater();
                String userNewString = StringUtils.join(outlineUserInfoMapper.selectNewItemOperator(manageSysOutline.getOutlineDevItemid()), ",");
                String userProofreadString = StringUtils.join(outlineUserInfoMapper.selectProofreadItemOperator(manageSysOutline.getOutlineDevItemid()), ",");
                String userAuditString = StringUtils.join(outlineUserInfoMapper.selectAuditItemOperator(manageSysOutline.getOutlineDevItemid()), ",");
                String userAuthorize = StringUtils.join(outlineUserInfoMapper.selectAuthorizeItemOperator(manageSysOutline.getOutlineDevItemid()), ",");
                itemOperater.setUserNew(userNewString);
                itemOperater.setUserProofread(userProofreadString);
                itemOperater.setUserAudit(userAuditString);
                itemOperater.setUserAuthorize(userAuthorize);
                if (itemOperater.getUserNew() == null && itemOperater.getUserProofread() == null && itemOperater.getUserAudit() == null && itemOperater.getUserAuthorize() == null) {

                } else {
                    itemOperater.setItemId(manageSysOutline.getOutlineDevItemid());
                    itemOperater.setItemName(manageSysOutline.getOutlineName());
                    itemList.add(itemOperater);

                }

            }
            findAllItemResponse.setStatus("success");
            findAllItemResponse.setData(itemList);
            findAllItemResponse.setMessage("目前有项目");
        }
        return findAllItemResponse;
    }

    public List<ItemFromFirstSys> getAllItem() {
        List<ManageSysDevelop> item = manageSysDevelopMapper.findAllItem();
        logger.info(item.get(0).getDevName());

        List<ItemFromFirstSys> itemList = new ArrayList<>();
        for(ManageSysDevelop manageSysDevelop : item){
            String devItemid = manageSysDevelop.getDevItemid();
            if(manageSysDevelop.getDevStatus() == 5 && manageSysOutlineMapper.selectProjectByDevItemId(devItemid) == null){
                ItemFromFirstSys itemFromFirstSys = new ItemFromFirstSys();
                itemFromFirstSys.setOutlineItemid(manageSysDevelop.getDevId());
                itemFromFirstSys.setOutlineDevItemid(manageSysDevelop.getDevItemid());
                itemFromFirstSys.setOutlineName(manageSysDevelop.getDevName());
                itemList.add(itemFromFirstSys);
            }
        }
        return itemList;
    }

//新建项目 还需要对字段进行初始化
    public void addItem(String devName, String userNew, String userProofread, String userAudit, String userAuthorize){
        String devItemId = manageSysDevelopMapper.selectColByName("dev_itemid", devName);
        ManageSysDevelop manageSysDevelop = manageSysDevelopMapper.selectByPrimaryKey(devItemId);
        Integer devId = manageSysDevelop.getDevId();
        Integer devSubsysEqp = manageSysDevelop.getDevSubsysEqp();
        String devSubsysEqpName = manageSysDevelop.getDevSubsysEqpName();
        String devSubsysEqpModel = manageSysDevelop.getDevSubsysEqpModel();
        String devSubsysEqpNum = manageSysDevelop.getDevSubsysEqpNum();
        String outlineItemid = String.valueOf(devId);
        ManageSysOutline newItem = new ManageSysOutline();
//        manageSysOutlineMapper.addItem("outline_dev_itemid", devItemId, "outline_name", devName, "outline_itemid", outlineItemid,"outline_dev_subsys_eqp", devSubsysEqp, "outline_status", 0, "outline_advice_proofread", "[]", "outline_advice_audit", "[]", "outline_advice_authorize", "[]");
        newItem.setOutlineDevItemid(devItemId);
        newItem.setOutlineName(devName);
        newItem.setOutlineItemid(outlineItemid);
        newItem.setOutlineDevSubsysEqp(devSubsysEqp);
        newItem.setOutlineStatus(0);
        newItem.setOutlineAdviceProofread("[]");
        newItem.setOutlineAdviceAudit("[]");
        newItem.setOutlineAdviceAuthorize("[]");
        newItem.setOutlineDevSubsysEqpName(devSubsysEqpName);
        newItem.setOutlineDevSubsysEqpModel(devSubsysEqpModel);
        newItem.setOutlineDevSubsysEqpNum(devSubsysEqpNum);
        manageSysOutlineMapper.insertItem(newItem);
//        manageSysOutlineMapper.updateItemCol("outline_dev_subsys_eqp_name", devSubsysEqpName, devItemId);
//        manageSysOutlineMapper.updateItemCol("outline_dev_subsys_eqp_model", devSubsysEqpModel, devItemId);
//        manageSysOutlineMapper.updateItemCol("outline_dev_subsys_eqp_num", devSubsysEqpNum, devItemId);
        for (int i = 3; i <= 59; i++) {
            String colName = "outline_data_"+i;
            manageSysOutlineMapper.updateItemCol(colName, "{}", devItemId);
        }
        //将outline_data12, outline_data13, outline_schema_subsys_eqp,  outline_data_subsys_eqp字段初始化
        manageSysOutlineMapper.updateItemCol("outline_schema_subsys_eqp", "{\"type\":\"object\",\"title\":\"分系统/设备信息\",\"properties\":{\"分系统或设备名称\":{\"type\":\"string\"},\"型号\":{\"type\":\"string\"},\"串号\":{\"type\":\"string\"},\"测试边界(m)\":{\"type\":\"string\"}}}", devItemId);
        manageSysOutlineMapper.updateItemCol("outline_data_12", "{\"发射测试参数\":[{\"频率范围\":\"25Hz~1kHz\",\"6dB带宽(kHz)\":\"0.01\",\"视频带宽(kHz)\":\"\",\"驻留时间(s)\":\"0.15\",\"最小测量时间(模拟式测量接收机)\":\"0.015s/Hz\"},{\"频率范围\":\"1kHz~10kHz\",\"6dB带宽(kHz)\":\"0.1\",\"视频带宽(kHz)\":\"\",\"驻留时间(s)\":\"0.02\",\"最小测量时间(模拟式测量接收机)\":\"0.2s/kHz\"},{\"频率范围\":\"10kHz~150kHz\",\"6dB带宽(kHz)\":\"1\",\"视频带宽(kHz)\":\"\",\"驻留时间(s)\":\"0.02\",\"最小测量时间(模拟式测量接收机)\":\"0.02s/kHz\"},{\"频率范围\":\"150kHz~30MHz\",\"6dB带宽(kHz)\":\"10\",\"视频带宽(kHz)\":\"\",\"驻留时间(s)\":\"0.02\",\"最小测量时间(模拟式测量接收机)\":\"2s/MHz\"},{\"频率范围\":\"30MHz~1GHz\",\"6dB带宽(kHz)\":\"100\",\"视频带宽(kHz)\":\"\",\"驻留时间(s)\":\"0.02\",\"最小测量时间(模拟式测量接收机)\":\"0.2s/MHz\"},{\"频率范围\":\">1GHz\",\"6dB带宽(kHz)\":\"1000\",\"视频带宽(kHz)\":\"\",\"驻留时间(s)\":\"0.02\",\"最小测量时间(模拟式测量接收机)\":\"20s/GHz\"}]}", devItemId);
        manageSysOutlineMapper.updateItemCol("outline_data_13", "{\"敏感度测试参数\":[{\"频率范围\":\"25Hz~1MHz\",\"模拟式扫描最大扫描速率(f0/s)\":\"0.0333\",\"步进式扫描最大步长(f0)\":\"0.05\",\"驻留时间\":\"\",\"调制类型(适用于CS114和RS103)\":\"脉冲\",\"重复频率(适用于CS114和RS103)\":\"1kHz\",\"占空比(适用于CS114和RS103)\":\"50%\"},{\"频率范围\":\"1MHz~30MHz\",\"模拟式扫描最大扫描速率(f0/s)\":\"0.00667\",\"步进式扫描最大步长(f0)\":\"0.01\",\"驻留时间\":\"\",\"调制类型(适用于CS114和RS103)\":\"脉冲\",\"重复频率(适用于CS114和RS103)\":\"1kHz\",\"占空比(适用于CS114和RS103)\":\"50%\"},{\"频率范围\":\"30MHz~1GHz\",\"模拟式扫描最大扫描速率(f0/s)\":\"0.00333\",\"步进式扫描最大步长(f0)\":\"0.005\",\"驻留时间\":\"\",\"调制类型(适用于CS114和RS103)\":\"脉冲\",\"重复频率(适用于CS114和RS103)\":\"1kHz\",\"占空比(适用于CS114和RS103)\":\"50%\"},{\"频率范围\":\"1GHz~40GHz\",\"模拟式扫描最大扫描速率(f0/s)\":\"0.00167\",\"步进式扫描最大步长(f0)\":\"0.0025\",\"驻留时间\":\"\",\"调制类型(适用于CS114和RS103)\":\"脉冲\",\"重复频率(适用于CS114和RS103)\":\"1kHz\",\"占空比(适用于CS114和RS103)\":\"50%\"}],\"敏感度试验波形参数\":{\"CS106\":[{\"上升时间(us)\":\"1.5us±0.5us\",\"下降时间(us)\":\"3.5us±0.5us\",\"脉冲宽度(us)\":\"5.5us（1±22%)us\"}],\"RS115\":[{\"上升时间(ns)\":\"1.8ns~2.8\",\"半脉宽(ns)\":\"23ns±5ns\"}]}}", devItemId);
        manageSysOutlineMapper.updateCol2ByOutlineDevItemId(devItemId, "current_page_number", 3);
        //outline_data_subsys_eqp 初始化
        JSONObject eqpJson = new JSONObject();
        eqpJson.put("分系统或设备名称", devSubsysEqpName);
        eqpJson.put("型号", devSubsysEqpModel);
        eqpJson.put("串号", devSubsysEqpNum);
        eqpJson.put("测试边界(m)", "");
        manageSysOutlineMapper.updateItemCol("outline_data_subsys_eqp", eqpJson.toJSONString(), devItemId);
        for (int i = 1001; i <= 1006; i++) {
            String colName = "outline_data_"+i;
            manageSysOutlineMapper.updateItemCol(colName, "{}", devItemId);
        }
        updateItem(devItemId, userNew, userProofread, userAudit, userAuthorize);
        //创建新项目上传图库文件夹
        File dir = new File(PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + devName);
        dir.mkdir();

    }

    public void updateItem(String devItemId, String userNew, String userProofread, String userAudit, String userAuthorize) {

        //项目的原来操作用户清空
        if (manageSysOutlineMapper.selectProjectByDevItemId(devItemId) != null) {
            List<String> userOldNew = outlineUserInfoMapper.selectNewItemOperator(devItemId);
            List<String> userOldProofread = outlineUserInfoMapper.selectProofreadItemOperator(devItemId);
            List<String> userOldAudit = outlineUserInfoMapper.selectAuditItemOperator(devItemId);
            List<String> userOldAuthorize = outlineUserInfoMapper.selectAuthorizeItemOperator(devItemId);
            if (userOldNew.size() > 0) {
                for (int i = 0; i < userOldNew.size(); i++) {
                    JSONArray userNewArray2 = new JSONArray();
                    OutlineUserInfo outlineUserInfoNew = outlineUserInfoMapper.selectUserByName(userOldNew.get(i));
                    String userNewStr = outlineUserInfoNew.getUserNew();
                    JSONArray userNewArray = JSON.parseArray(userNewStr);
                    for (Object str : userNewArray) {
                        if (str.equals(devItemId)) {

                        } else {
                            userNewArray2.add(str);
                        }


                    }
                    String userNewString2 = userNewArray2.toJSONString();
                    outlineUserInfoMapper.updateColByName("user_new", userNewString2, userOldNew.get(i));

                }
            }

            if(userOldProofread.size() > 0) {
                for(int i = 0; i < userOldProofread.size(); i++){
                    JSONArray userProofreadArray2 = new JSONArray();
                    OutlineUserInfo outlineUserInfoProofread = outlineUserInfoMapper.selectUserByName(userOldProofread.get(i));
                    String userProofreadStr = outlineUserInfoProofread.getUserProofread();
                    JSONArray userProofreadArray = JSON.parseArray(userProofreadStr);
                    for(Object str : userProofreadArray){
                        if(str.equals(devItemId)){

                        }else {
                            userProofreadArray2.add(str);
                        }
                    }
                    String userProofreadString2 = userProofreadArray2.toJSONString();
                    outlineUserInfoMapper.updateColByName("user_proofread", userProofreadString2, userOldProofread.get(i));

                }
            }

            if(userOldAudit.size() > 0){
                for(int i = 0; i < userOldAudit.size(); i++){
                    JSONArray userAuditArray2 = new JSONArray();
                    OutlineUserInfo outlineUserInfoAudit = outlineUserInfoMapper.selectUserByName(userOldAudit.get(i));
                    String userAuditStr = outlineUserInfoAudit.getUserAudit();
                    JSONArray userAuditArray = JSON.parseArray(userAuditStr);
                    for(Object str : userAuditArray){
                        if(str.equals(devItemId)){

                        }else {
                            userAuditArray2.add(str);
                        }
                    }
                    String userAuditString2 = userAuditArray2.toJSONString();
                    outlineUserInfoMapper.updateColByName("user_audit", userAuditString2, userOldAudit.get(i));
                }
            }

            if(userOldAuthorize.size() > 0){
                for(int i = 0; i < userOldAuthorize.size(); i++){
                    JSONArray userAthorizeArray2 = new JSONArray();
                    OutlineUserInfo outlineUserInfoAuthorize =outlineUserInfoMapper.selectUserByName(userOldAuthorize.get(i));
                    String userAuthorizeStr = outlineUserInfoAuthorize.getUserAuthorize();
                    JSONArray userAthorizeArray = JSON.parseArray(userAuthorizeStr);
                    for(Object str : userAthorizeArray){
                        if(str.equals(devItemId)){

                        }else {
                            userAthorizeArray2.add(str);
                        }
                    }
                    String userAuthorizeString2 = userAthorizeArray2.toJSONString();
                    outlineUserInfoMapper.updateColByName("user_authorize", userAuthorizeString2, userOldAuthorize.get(i));
                }
            }
        }

        //添加项目新的操作人员
        Object str = (Object)devItemId;
        OutlineUserInfo userInfoNew = outlineUserInfoMapper.selectUserByName(userNew);
        if(userNew != ""){
            String newStr1 = userInfoNew.getUserNew();
            JSONArray newJsonArray = JSON.parseArray(newStr1);
            newJsonArray.add(str);
            String newStr2 = newJsonArray.toJSONString();
            outlineUserInfoMapper.updateColByName("user_new", newStr2, userNew);
        }

        if(userProofread != ""){
            OutlineUserInfo userInfoProofread = outlineUserInfoMapper.selectUserByName(userProofread);
            String proofreadStr1 = userInfoProofread.getUserProofread();
            JSONArray proofreadJsonArray = JSON.parseArray(proofreadStr1);
            proofreadJsonArray.add(str);
            String proofreadStr2 = proofreadJsonArray.toJSONString();
            outlineUserInfoMapper.updateColByName("user_proofread", proofreadStr2, userProofread);

        }

        if(userAudit != ""){
            OutlineUserInfo userInfoAudit = outlineUserInfoMapper.selectUserByName(userAudit);
            String auditStr1 = userInfoAudit.getUserAudit();
            JSONArray auditJsonArray = JSON.parseArray(auditStr1);
            auditJsonArray.add(str);
            String auditStr2 = auditJsonArray.toJSONString();
            outlineUserInfoMapper.updateColByName("user_audit", auditStr2, userAudit);

        }

        if(userAuthorize != ""){
            OutlineUserInfo userInfoAuthorize = outlineUserInfoMapper.selectUserByName(userAuthorize);
            String authorizeStr1 = userInfoAuthorize.getUserAuthorize();
            JSONArray authorizeJsonArray = JSON.parseArray(authorizeStr1);
            authorizeJsonArray.add(str);
            String authorizeStr2 = authorizeJsonArray.toJSONString();
            outlineUserInfoMapper.updateColByName("user_authorize", authorizeStr2, userAuthorize);

        }

    }

    public void deleteItem(String outlineName){
        String outlineItemId = manageSysOutlineMapper.selectItemidByName(outlineName);
        this.updateItem(outlineItemId,"","","","");
        manageSysOutlineMapper.deleteItemByName(outlineName);
        String targetPath = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + outlineName;
        File dir = new File(targetPath);
        if (dir.exists()) {
            try {
                FileUtils.deleteDirectory(dir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public BaseResponse importItem(File sqlTxt, String fileName) throws Exception {
        BaseResponse importItemResponse = new BaseResponse();
        //导入项目相关图片
        String oldPath =  PathStoreEnum.WINDOWS_SQLDATA_AND_IMG_SOURTH_PATH.getValue() + fileName.substring(0, fileName.length() - 5) + "_" + "图片";
        String oldPath2 = PathStoreEnum.WINDOWS_SQLDATA_AND_IMG_SOURTH_PATH.getValue() + fileName;
        // newPath1 将图片文件夹放至changed目录下
        String newPath1 = PathStoreEnum.WINDOWS_IMG_CHANGED_DEST_PATH.getValue() + fileName.substring(0, fileName.length() - 5) + "_" + "图片";
        // newPath2 文件导入相关工作完成后，将相关内容移动到归档目录下，然后删除相关文件，保持导入目录的整洁性
        String newPath2 = PathStoreEnum.WINDOWS_ARCHIVED_FILR_DEST_PATH.getValue() + fileName.substring(0, fileName.length() - 5) + "_" + "图片";
        String newPath3 = PathStoreEnum.WINDOWS_ARCHIVED_FILR_DEST_PATH.getValue();

        File oldFile = new File(oldPath);
        if (! oldFile.exists()) {
            importItemResponse.setStatus("error");
            importItemResponse.setMessage("请先将项目相关图片放置桌面导入文件目录下，再上传json文件！");
            return importItemResponse;
        }

        //导入项目数据库相关数据
        String importSqlDataFinalPath = PathStoreEnum.WINDOWS_SQLDATA_AND_IMG_SOURTH_PATH.getValue() + fileName;
        File fileVerify = new File(importSqlDataFinalPath);
        if (! fileName.substring(fileName.length() - 5).equals(".json")) {
            importItemResponse.setStatus("error");
            importItemResponse.setMessage("请确认导入文件格式是否为json！");
            return importItemResponse;
        } else if (! fileVerify.exists()) {
            importItemResponse.setStatus("error");
            importItemResponse.setMessage("请先将项目json文件放置桌面导入文件目录下，再上传json文件！");
            return importItemResponse;
        }
        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            br = new BufferedReader(new InputStreamReader((new FileInputStream(importSqlDataFinalPath))));
            sb = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String sqlDataStr = sb.toString();
        JSONObject sqlDataObject = JSON.parseObject(sqlDataStr);
        ManageSysDevelop manageSysDevelop= new ManageSysDevelop();
        manageSysDevelop.setDevItemid(sqlDataObject.getString("devItemId"));
        manageSysDevelop.setDevName(sqlDataObject.getString("devName"));
        manageSysDevelop.setDevSubsysEqp(sqlDataObject.getInteger("devSubsysEqp"));
        manageSysDevelop.setDevSubsysEqpName(sqlDataObject.getString("devSubsysEqpName"));
        manageSysDevelop.setDevSubsysEqpModel(sqlDataObject.getString("devSubsysEqpModel"));
        manageSysDevelop.setDevSubsysEqpNum(sqlDataObject.getString("devSubsysEqpNum"));
        manageSysDevelop.setDevSupplier(sqlDataObject.getString("devSupplier"));
        manageSysDevelop.setDevPrimaryPlatform(sqlDataObject.getInteger("devPrimaryPlatform"));
        manageSysDevelop.setDevSecondaryPlatform(sqlDataObject.getInteger("devSecondaryPlatform"));
        manageSysDevelop.setDevAttribute(sqlDataObject.getInteger("devAttribute"));
        manageSysDevelop.setDevKey(sqlDataObject.getInteger("devKey"));
        manageSysDevelop.setDevInstall(sqlDataObject.getInteger("devInstall"));
        manageSysDevelop.setDevGnd(sqlDataObject.getInteger("devGnd"));
        manageSysDevelop.setDevSpecial(sqlDataObject.getInteger("devSpecial"));
        manageSysDevelop.setDevInterport(sqlDataObject.getInteger("devInterport"));
        manageSysDevelop.setDevLowsensitive(sqlDataObject.getInteger("devLowsensitive"));
        manageSysDevelop.setDevEmp(sqlDataObject.getInteger("devEmp"));
        manageSysDevelop.setDevStatic(sqlDataObject.getInteger("devStatic"));
        manageSysDevelop.setDevPowerport(sqlDataObject.getString("devPowerPort"));
        manageSysDevelop.setDevPowersupply(sqlDataObject.getString("devPowersupply"));
        manageSysDevelop.setDevVoltage(sqlDataObject.getString("devVoltage"));
        manageSysDevelop.setDevVoltagenum(sqlDataObject.getString("devVoltagenum"));
        manageSysDevelop.setDevAntenna(sqlDataObject.getInteger("devAntenna"));
        manageSysDevelop.setDevReceiveLaunch(sqlDataObject.getInteger("devReceiveLaunch"));
        manageSysDevelop.setDevModulation(sqlDataObject.getInteger("devModulation"));
        manageSysDevelop.setDevFreqOptional(sqlDataObject.getString("devFreqOptional"));
        manageSysDevelop.setDevFreqFhLow(sqlDataObject.getString("devFreqFHLow"));
        manageSysDevelop.setDevFreqFhMid(sqlDataObject.getString("devFreqFHMid"));
        manageSysDevelop.setDevFreqFhHigh(sqlDataObject.getString("devFreqFHHigh"));
        manageSysDevelop.setDevFreqDsss(sqlDataObject.getString("devFreqDSSS"));
        manageSysDevelop.setDevCe101(sqlDataObject.getString("devCe101"));
        manageSysDevelop.setDevCe102(sqlDataObject.getString("devCe102"));
        manageSysDevelop.setDevCe106(sqlDataObject.getString("devCe106"));
        manageSysDevelop.setDevCe107(sqlDataObject.getString("devCe107"));
        manageSysDevelop.setDevCs101(sqlDataObject.getString("devCs101"));
        manageSysDevelop.setDevCs102(sqlDataObject.getString("devCs102"));
        manageSysDevelop.setDevCs103(sqlDataObject.getString("devCs103"));
        manageSysDevelop.setDevCs104(sqlDataObject.getString("devCs104"));
        manageSysDevelop.setDevCs105(sqlDataObject.getString("devCs105"));
        manageSysDevelop.setDevCs106(sqlDataObject.getString("devCs106"));
        manageSysDevelop.setDevCs109(sqlDataObject.getString("devCs109"));
        manageSysDevelop.setDevCs112(sqlDataObject.getString("devCs112"));
        manageSysDevelop.setDevCs114(sqlDataObject.getString("devCs114"));
        manageSysDevelop.setDevCs115(sqlDataObject.getString("devCs115"));
        manageSysDevelop.setDevCs116(sqlDataObject.getString("devCs116"));
        manageSysDevelop.setDevRe101(sqlDataObject.getString("devRe101"));
        manageSysDevelop.setDevRe102(sqlDataObject.getString("devRe102"));
        manageSysDevelop.setDevRe103(sqlDataObject.getString("devRe103"));
        manageSysDevelop.setDevRs101(sqlDataObject.getString("devRs101"));
        manageSysDevelop.setDevRs103(sqlDataObject.getString("devRs103"));
        manageSysDevelop.setDevRs105(sqlDataObject.getString("devCs105"));
        manageSysDevelop.setDevAdviceProofread(sqlDataObject.getString("devAdviceProofread"));
        manageSysDevelop.setDevAdviceAudit(sqlDataObject.getString("devAdviceAudit"));
        manageSysDevelop.setDevAdviceAuthorize(sqlDataObject.getString("devAdviceAuthorize"));
        manageSysDevelop.setDevCreateTime(sqlDataObject.getDate("devCreateTime"));
        manageSysDevelop.setDevNewTime(sqlDataObject.getDate("devNewTime"));
        manageSysDevelop.setDevModifyTime(sqlDataObject.getDate("devModifyTime"));
        manageSysDevelop.setDevProofreadTime(sqlDataObject.getDate("devProofreadTime"));
        manageSysDevelop.setDevAuditTime(sqlDataObject.getDate("devAuditTime"));
        manageSysDevelop.setDevAuthorizeTime(sqlDataObject.getDate("devAuthorizeTime"));
        manageSysDevelop.setDevOpeator(sqlDataObject.getInteger("devOperator"));
        manageSysDevelop.setDevStatus(sqlDataObject.getInteger("devStatus"));
        manageSysDevelop.setProjectList(sqlDataObject.getString("projectList"));
        manageSysDevelop.setDevFreSelect(sqlDataObject.getInteger("devFreSelect"));
        manageSysDevelop.setDevSubsysSource(sqlDataObject.getString("devSubsysSource"));
        manageSysDevelop.setDevSubsysComRef(sqlDataObject.getString("devSubsysComRef"));
        manageSysDevelop.setDevSubsysQuantity(sqlDataObject.getString("devSubsysQuantity"));
        manageSysDevelop.setDevSubsysEnvironment(sqlDataObject.getString("devSubsysEnvironment"));
        manageSysDevelop.setDevPowername(sqlDataObject.getString("devPowername"));
        if (this.manageSysDevelopMapper.selectByPrimaryKey(manageSysDevelop.getDevItemid()) == null &&
                this.manageSysDevelopMapper.getProjectByDevName(manageSysDevelop.getDevName()) == null) {
            this.manageSysDevelopMapper.addItem(manageSysDevelop);
            copyFolder(oldPath, newPath1);
            copyFolder(oldPath, newPath2);
            FileUtils.copyFileToDirectory(new File(oldPath2), new File(newPath3));
            FileUtils.deleteDirectory(new File(oldPath));
            FileUtils.deleteQuietly(new File(oldPath2));
            importItemResponse.setStatus("success");
            importItemResponse.setMessage("项目导入成功 可选择新建此项目");
        } else if (this.manageSysDevelopMapper.selectByPrimaryKey(manageSysDevelop.getDevItemid()) != null) {
            importItemResponse.setStatus("error");
            importItemResponse.setMessage("该项目已存在 请勿重复导入！");
        } else if (this.manageSysDevelopMapper.getProjectByDevName(manageSysDevelop.getDevName()) != null) {
            importItemResponse.setStatus("error");
            importItemResponse.setMessage("系统中存在同名项目，请修改项目名称后再次上传！");
        }

        return importItemResponse;
    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public static void copyFolder(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();

        }
    }
}




