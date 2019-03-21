package cn.gjb151b.outline.action;

import cn.gjb151b.outline.service.CoreService;
import cn.gjb151b.outline.service.DBService;
import cn.gjb151b.outline.utils.BaseResponse;
import cn.gjb151b.outline.utils.ServiceException;
import cn.gjb151b.outline.utils.UUIDUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static cn.gjb151b.outline.utils.CommonUtils.checkParamLegal;

@Controller
public class OutlinePageSubmitAction extends ActionSupport {
    private static Logger logger = Logger.getLogger(OutlinePageLoadAction.class);

    private Integer outlineID;
    private Integer pageNumber;
    private Integer pageAction;
    private String jsonData;
    private String outlineAdvice;
    private Integer outlineStatus;
    private Integer outlineStatusOriginal;
    private BaseResponse<String> response;
    private Integer changeLocation;

    private Integer picNumber;

    private File images;

    private String imagesContentType;
    private String imagesFileName;

    private CoreService coreService;
    @Autowired
    private DBService dbService;

    @Autowired
    OutlinePageSubmitAction(CoreService coreService) {
        this.coreService = coreService;
        response = new BaseResponse<>();
        response.setError("action error");
    }

    public String submit() {
        try {
            checkParamLegal(jsonData);
        } catch (ServiceException e) {
            logger.error(String.format("param parse error, outlineID:%d pageNumber:%d errInfo:%s", outlineID, pageNumber,
                    e.getExceptionEnums().getErrMsg()));

            response.setError(e.getExceptionEnums().getErrMsg());
            return SUCCESS;
        } catch (Exception e) {
            logger.error(String.format("param parse error, outlineID:%d pageNumber:%d errInfo:%s", outlineID, pageNumber,
                    e.getMessage()));
            response.setError(e.getMessage());
            return SUCCESS;
        }
//        if (!checkParamLegal(jsonData)) {
//            response.setError("param error");
//            return SUCCESS;
//        }

        try {
            coreService.submitPageData(outlineID, pageNumber, pageAction, jsonData, changeLocation);
        } catch (ServiceException e) {
            logger.info(String.format("service error, outlineID:%d pageNumber:%d errInfo:%s", outlineID, pageNumber,
                    e.getExceptionEnums().getErrMsg()));
            response.setError("service error");

            return SUCCESS;
        } catch (Exception e) {
            logger.info(String.format("unknown error, outlineID:%d pageNumber:%d errInfo:%s", outlineID, pageNumber,
                    e.getMessage()));
            response.setError("other service error");
            return SUCCESS;
        }

        response.setResponse("data from server");
        return SUCCESS;
    }

    public String upload()  {
        //获得文件类型（可以判断如果不是图片，禁止上传）
        String contentType = imagesContentType;
        //获得文件后缀名
        String suffixName=contentType.substring(contentType.indexOf("/")+1);
        //使用UUID使得图片名唯一
        String uuid = UUIDUtils.getUUID();
        //得到组合的filename
        String filename = uuid+imagesFileName;
        //图片存储的相对路径
        String localPath = "src/main/webapp/statics/imgs";
        //将照片copy到指定的相对路径下
        try{
            FileUtils.copyFile(images, new File(localPath, filename));

        } catch (Exception e){
            e.printStackTrace();
        }
        //mysql中存储相应的图片路径
        String sql = "statics/imgs"+"/"+filename;
        logger.info(pageNumber);

        if(pageNumber == 4){
            if(picNumber == 1){
                try{
                    String outlineData4 = dbService.fetchData(outlineID, "outline_data_4");
                    JSONObject jsonObject;
                    jsonObject = JSON.parseObject(outlineData4);
                    List<String> pic1List = (List<String>)jsonObject.get("分系统/设备照片");
                    pic1List.add(sql);
                    jsonObject.put("分系统/设备照片", pic1List);
                    outlineData4 = jsonObject.toJSONString();
                    dbService.submitData(outlineID, "outline_data_4", outlineData4);
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else if(picNumber == 2){
                try{
                    String outlineData4 = dbService.fetchData(outlineID, "outline_data_4");
                    JSONObject jsonObject;
                    jsonObject = JSON.parseObject(outlineData4);
                    List<String> pic2List = (List<String>)jsonObject.get("分系统/设备关系图");
                    pic2List.add(sql);
                    jsonObject.put("分系统/设备关系图", pic2List);
                    outlineData4 = jsonObject.toJSONString();
                    dbService.submitData(outlineID, "outline_data_4", outlineData4);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            }
        response.setMessage("success");
        return SUCCESS;
    }

    public String deletePic(){
        if(pageNumber == 4){
            if(picNumber == 1){
                    try{
                        String outlineData4 = dbService.fetchData(outlineID, "outline_data_4");
                        JSONObject jsonObject;
                        jsonObject = JSON.parseObject(outlineData4);
                        List<String> picList = new ArrayList<>();
                        jsonObject.put("分系统/设备照片", picList);
                        dbService.submitData(outlineID, "outline_data_4", jsonObject.toJSONString());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

            } else if(picNumber == 2) {
                    try{
                        String outlineData4 = dbService.fetchData(outlineID, "outline_data_4");
                        JSONObject jsonObject;
                        jsonObject = JSON.parseObject(outlineData4);
                        List<String> picList = new ArrayList<>();
                        jsonObject.put("分系统/设备关系图", picList);
                        dbService.submitData(outlineID, "outline_data_4", jsonObject.toJSONString());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

        }
        response.setMessage("success");
        return SUCCESS;
    }



    public String submitAdvice(){
        try {
            if(outlineStatusOriginal == 1){
                coreService.submitAdvice(outlineID, "outline_advice_proofread", outlineAdvice, "outline_status", outlineStatus);
            }else if(outlineStatusOriginal == 2){
                coreService.submitAdvice(outlineID, "outline_advice_audit", outlineAdvice, "outline_status", outlineStatus);
            }else if(outlineStatusOriginal == 3){
                coreService.submitAdvice(outlineID, "outline_advice_authorize", outlineAdvice, "outline_status", outlineStatus);
            }else{
                coreService.submitAdvice(outlineID, "outline_advice_authorize", outlineAdvice, "outline_status", outlineStatus);
            }

        } catch (ServiceException e) {
            response.setError("service error");

            return SUCCESS;
        } catch (Exception e) {
            response.setError("other service error");
            return SUCCESS;
        }

        response.setResponse("data from server");
        return SUCCESS;
    }
//    /**
//     * 检查页面回传参数pageInfo，合法返回true
//     */
//    private Boolean checkParamLegal() {
//        try {
//            Object parseRes = JSON.parse(this.jsonData);
//        }catch (Exception e){
//            logger.error(String.format("param error, outlineID:%d pageNumber:%d errInfo:%s", outlineID, pageNumber,
//                    e.getMessage()));
//
//            logger.warn(String.format("param error, outlineID:%d pageNumber:%d errInfo:%s", outlineID, pageNumber,
//                    e.getMessage()));
//            return false;
//        }
//        return true;
//    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public BaseResponse<String> getResponse() {
        return response;
    }

    public void setResponse(BaseResponse<String> response) {
        this.response = response;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getOutlineID() {
        return outlineID;
    }

    public void setOutlineID(Integer outlineID) {
        this.outlineID = outlineID;
    }

    public Integer getPageAction() {
        return pageAction;
    }

    public void setPageAction(Integer pageAction) {
        this.pageAction = pageAction;
    }

    public File getImages() {
        return images;
    }

    public void setImages(File images) {
        this.images = images;
    }

    public String getImagesContentType() {
        return imagesContentType;
    }

    public void setImagesContentType(String imagesContentType) {
        this.imagesContentType = imagesContentType;
    }

    public String getImagesFileName() {
        return imagesFileName;
    }

    public void setImagesFileName(String imagesFileName) {
        this.imagesFileName = imagesFileName;
    }


    public Integer getPicNumber() {
        return picNumber;
    }

    public void setPicNumber(Integer picNumber) {
        this.picNumber = picNumber;
    }

    public String getOutlineAdvice(){ return  outlineAdvice;}

    public void setOutlineAdvice(String outlineAdvice){ this.outlineAdvice = outlineAdvice;}

    public Integer getOutlineStatus(){ return outlineStatus;}

    public void setOutlineStatus(Integer outlineStatus){ this.outlineStatus = outlineStatus;}

    public Integer getOutlineStatusOriginal(){ return outlineStatusOriginal;}

    public void setOutlineStatusOriginal(Integer outlineStatusOriginal){ this.outlineStatusOriginal = outlineStatusOriginal;}

    public Integer getChangeLocation(){ return  changeLocation;}

    public void  setChangeLocation( Integer changeLocation){ this.changeLocation = changeLocation;}

}
