package cn.gjb151b.outline.action;

import cn.gjb151b.outline.Constants.PathStoreEnum;
import cn.gjb151b.outline.model.*;
import cn.gjb151b.outline.outlineDao.ManageSysOutlineMapper;
import cn.gjb151b.outline.service.ItemService;
import cn.gjb151b.outline.utils.BaseResponse;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
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
    private BaseResponse importItemResponse = new BaseResponse();
    private List<String> list1;
    private BaseResponse getAllItemResponse;
    private File sqlTxt;
    private String sqlTxtContentType;
    private String sqlTxtFileName;

    public String findAllItem(){
        findAllItemResponse = itemService.findAllItem();
        return "success";
    }

    public String getAllItem(){
        List<ItemFromFirstSys> itemList = itemService.getAllItem();
        getAllItemResponse = new BaseResponse(itemList);
        return "success";

    }

    public String deleteItem(){
        itemService.deleteItem(devName);
        deleteItemResponse.setMessage("删除成功！");
        deleteItemResponse.setStatus("success");
        return "success";

    }

    public String addItem(){
        itemService.addItem(devName,user_new, user_proofread, user_audit, user_authorize);
        addItemResponse.setStatus("success");
        addItemResponse.setData(devName);
        addItemResponse.setMessage("成功添加项目'"+devName+"'");
        return "success";

    }


    public String updateItem(){
        logger.info(user_authorize);
        itemService.updateItem(devItemId,user_new,user_proofread,user_audit,user_authorize);
        updateItemResponse.setStatus("success");
        updateItemResponse.setData(devItemId);
        updateItemResponse.setMessage("更新成功");

        return "success";
    }

    public String importItem() {
        try {
            this.importItemResponse = this.itemService.importItem(this.sqlTxt, this.sqlTxtFileName);
        } catch (SQLException e) {
            e.printStackTrace();
            this.importItemResponse.setStatus("error");
            this.importItemResponse.setMessage("数据库访问异常！");
        } catch (RuntimeException e) {
            e.printStackTrace();
            this.importItemResponse.setStatus("error");
            this.importItemResponse.setMessage("运行时发生错误！");
        } catch (Exception e) {
            e.printStackTrace();
            this.importItemResponse.setStatus("error");
            this.importItemResponse.setMessage("系统发生了其它未知错误！");
        }
        return "success";

    }

    public File getSqlTxt() {
        return sqlTxt;
    }

    public void setSqlTxt(File sqlTxt) {
        this.sqlTxt = sqlTxt;
    }


    public String getSqlTxtContentType() {
        return sqlTxtContentType;
    }

    public void setSqlTxtContentType(String sqlTxtContentType) {
        this.sqlTxtContentType = sqlTxtContentType;
    }

    public String getSqlTxtFileName() {
        return sqlTxtFileName;
    }

    public void setSqlTxtFileName(String sqlTxtFileName) {
        this.sqlTxtFileName = sqlTxtFileName;
    }




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

    public BaseResponse getImportItemResponse() {
        return importItemResponse;
    }

    public void setImportItemResponse(BaseResponse importItemResponse) {
        this.importItemResponse = importItemResponse;
    }

}
