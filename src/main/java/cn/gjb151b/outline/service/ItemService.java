package cn.gjb151b.outline.service;

import cn.gjb151b.outline.action.ItemAction;
import cn.gjb151b.outline.dao.ManageSysDevelopMapper;
import cn.gjb151b.outline.model.*;
import cn.gjb151b.outline.outlineDao.ManageSysOutlineMapper;
import cn.gjb151b.outline.outlineDao.OutlineUserInfoMapper;
import cn.gjb151b.outline.utils.BaseResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sun.tools.internal.xjc.outline.Outline;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
            if(manageSysDevelop.getDevStatus() == 5 && manageSysOutlineMapper.selectProjectByItemId(devItemid) == null){
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
        manageSysOutlineMapper.addItem("outline_dev_itemid", devItemId, "outline_name", devName, "outline_itemid", outlineItemid,"outline_dev_subsys_eqp", devSubsysEqp, "outline_status", 0, "outline_advice_proofread", "[]", "outline_advice_audit", "[]", "outline_advice_authorize", "[]");
        manageSysOutlineMapper.updateItemCol("outline_dev_subsys_eqp_name", devSubsysEqpName, devItemId);
        manageSysOutlineMapper.updateItemCol("outline_dev_subsys_eqp_model", devSubsysEqpModel, devItemId);
        manageSysOutlineMapper.updateItemCol("outline_dev_subsys_eqp_num", devSubsysEqpNum, devItemId);
        for (int i = 3; i < 59; i++) {
            String colName = "outline_data_"+i;
            manageSysOutlineMapper.updateItemCol(colName, "{}", devItemId);
        }
        for (int i = 1001; i < 1006; i++) {
            String colName = "outline_data_"+i;
            manageSysOutlineMapper.updateItemCol(colName, "{}", devItemId);
        }
        updateItem(devItemId, userNew, userProofread, userAudit, userAuthorize);


    }

    public void updateItem(String devItemId, String userNew, String userProofread, String userAudit, String userAuthorize) {

        //项目的原来操作用户清空
        if (manageSysOutlineMapper.selectProjectByItemId(devItemId) != null) {
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

    }
}




