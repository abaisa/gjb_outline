package cn.gjb151b.outline.action;

import cn.gjb151b.outline.model.ItemOperater;
import cn.gjb151b.outline.model.ManageSysOutline;
import cn.gjb151b.outline.model.OutlineUserInfo;
import cn.gjb151b.outline.model.ViewProject;
import cn.gjb151b.outline.outlineDao.ManageSysOutlineMapper;
import cn.gjb151b.outline.service.ItemService;
import cn.gjb151b.outline.utils.BaseResponse;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemAction extends ActionSupport{
    private Logger logger =  Logger.getLogger(ItemAction.class);
    @Autowired
    private ItemService itemService;

    private String devItemId;
    private String devName;
    private String user_new;
    private String user_proofread;
    private String user_audit;
    private String user_authorize;
    private Map<String, List<String>> user_New = new HashMap<>();
    private BaseResponse findAllItemResponse = new BaseResponse<List<ViewProject>>();
    private BaseResponse addItemResponse = new BaseResponse<String>();
    private BaseResponse deleteItemResponse = new BaseResponse<String>();
    private BaseResponse updateItemResponse = new BaseResponse<String>();
    private BaseResponse updateItemNameResponse = new BaseResponse<String>();
    private BaseResponse findAllResponse = new BaseResponse<List<OutlineUserInfo>>();
    private List<String> list1;
    private BaseResponse getAllItemResponse;

    public String findAllItem(){
        findAllItemResponse = itemService.findAllItem();
        return "success";
    }

    public String getAllItem(){
        List<ItemOperater> itemList = itemService.getAllItem();
        logger.info(itemList.get(1).getItemName());
        getAllItemResponse = new BaseResponse(itemList);
        return "success";

    }

//    public String addItem(){
//        ManageSysDevelopEntity s = new ManageSysDevelopEntity ();
//        s.setDevItemid(UUIDUtils.getUUID());
//        s.setDevName(this.devName);
//        s.setDevStatus(0);
//        s.setDevAdviceProofread("[]");
//        s.setDevAdviceAudit("[]");
//        s.setDevAdviceAuthorize("[]");
//        addItemResponse = ItemService.addItem(s, user_new, user_proofread, user_audit, user_authorize);
//        return "success";
//    }
//
//    public String deleteItem(){
//        deleteItemResponse=ItemService.deleteItem(this.devName);
//        return "success";
//    }
//
//    public String updateItemName(){
//        updateItemNameResponse=ItemService.updateItemName(this.devItemId, this.devName);
//        return "success";
//    }
//
//    public String updateOperator(){
//        updateItemResponse=ItemService.updateItem(this.devItemId, this.user_new, this.user_proofread, this.user_audit, this.user_authorize);
//        return "success";
//    }

    public BaseResponse<List<OutlineUserInfo>> getFindAllResponse() {
        return findAllResponse;
    }

    public BaseResponse<List> getFindAllItemResponse() {
        return findAllItemResponse;
    }

    public BaseResponse<String> getAddItemResponse() {
        return addItemResponse;
    }

    public BaseResponse<String> getDeleteItemResponse() {
        return deleteItemResponse;
    }

    public BaseResponse<String> getUpdateItemResponse() {
        return updateItemResponse;
    }

    public BaseResponse<String> getUpdateItemNameResponse() {
        return updateItemNameResponse;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getUser_new() {
        return user_new;
    }

    public void setUser_new(String user_new) {
        this.user_new = user_new;
    }

    public String getUser_proofread() {
        return user_proofread;
    }

    public void setUser_proofread(String user_proofread) {
        this.user_proofread = user_proofread;
    }

    public String getUser_audit() {
        return user_audit;
    }

    public void setUser_audit(String user_audit) {
        this.user_audit = user_audit;
    }

    public String getUser_authorize() {
        return user_authorize;
    }

    public void setUser_authorize(String user_authorize) {
        this.user_authorize = user_authorize;
    }

    public List getList1() {
        return list1;
    }
    public void setList1(List list1) {
        this.list1 = list1;
    }

    public Map getUser_New(){return user_New;}
    public void setUser_New(Map user_New){this.user_New = user_New;}

    public String getDevItemId() {
        return devItemId;
    }

    public void setDevItemId(String devItemId) {
        this.devItemId = devItemId;
    }

    public BaseResponse getGetAllItemResponse() {
        return getAllItemResponse;
    }

    public void setGetAllItemResponse(BaseResponse getAllItemResponse) {
        this.getAllItemResponse = getAllItemResponse;
    }

}
