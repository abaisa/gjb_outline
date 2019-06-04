package cn.gjb151b.outline.action;

import cn.gjb151b.outline.service.CoreService;
import cn.gjb151b.outline.service.DBService;
import cn.gjb151b.outline.utils.BaseResponse;
import cn.gjb151b.outline.utils.ServiceException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OutlinePageLoadAction  extends ActionSupport {
    private Integer outlineID;
    private Integer currentPageNumber;
    private Integer pageAction;
    private String outlineDevItemId;

    private BaseResponse<String> response;
    private CoreService coreService;

    private BaseResponse<List<String>> downloadResponse;
    private Logger logger = Logger.getLogger(OutlinePageLoadAction.class);

    private Integer picNumber;

    private BaseResponse<Integer> getCurrentPageNumberResponse;

    @Autowired
    private DBService dbService;

    @Autowired
    OutlinePageLoadAction(CoreService coreService) {
        this.coreService = coreService;
        response = new BaseResponse<>();
    }

    public String load() {
        String responseData;
        try {
            responseData = coreService.getResponseData(outlineDevItemId, currentPageNumber, pageAction);
            logger.info(outlineDevItemId+" "+currentPageNumber+" "+pageAction);
        } catch (ServiceException e) {
            e.printStackTrace();
            System.out.println(e.getExceptionEnums().getErrMsg());
            response.setError(e);
            return SUCCESS;
        } catch (Exception e){
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setError("other service error");
            return SUCCESS;
        }

        response.setResponse(responseData);
        System.out.println("response成功！！");
        return SUCCESS;
    }

//    public String load2(){
//        String responseData;
//        responseData =
//    }

    public String loadAdvice(){
        String responseData;
        try{
            responseData = coreService.getAdvice(outlineDevItemId);
        } catch (ServiceException e) {
            System.out.println(e.getExceptionEnums().getErrMsg());
            response.setError(e);
            return SUCCESS;
        } catch (Exception e){
            System.out.println(e.getMessage());
            response.setError("other service error");
            return SUCCESS;
        }
        response.setResponse(responseData);
        return SUCCESS;

    }

    public String download(){
        List<String> picList = new ArrayList<>();
        if(picNumber == 1){
            try{
                String outlineData4 = dbService.fetchData(outlineDevItemId, "outline_data_4");
                JSONObject jsonObject = JSON.parseObject(outlineData4);
                picList = (List<String>) jsonObject.get("分系统/设备照片");
            } catch (Exception e){
                e.printStackTrace();
                downloadResponse.setError("other service error");
            }

        } else if (picNumber == 2) {
            try{
                String outlineData4 = dbService.fetchData(outlineDevItemId, "outline_data_4");
                JSONObject jsonObject = JSON.parseObject(outlineData4);
                picList = (List<String>) jsonObject.get("分系统/设备关系图");
            } catch (Exception e){
                e.printStackTrace();
                downloadResponse.setError("other service error");
            }

        }
        if (currentPageNumber >= 14 && currentPageNumber <= 34) {
            try {
                String colName = "outline_data_" + currentPageNumber;
                String outlineData14To34 = dbService.fetchData(outlineDevItemId, colName);
                if (JSON.parseObject(outlineData14To34).getString("项目试验图") != null) {
                    String picName = JSON.parseObject(outlineData14To34).getString("项目试验图");
                    picList.add(picName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        downloadResponse = new BaseResponse<>();
        downloadResponse.setResponse(picList);
        return SUCCESS;

    }

    public String loadText() {
        String responseData = "";
        String colName = "outline_data_" + currentPageNumber;
        try {
            String outlineData14To34 = dbService.fetchData(outlineDevItemId, colName);
            String textNew1 = JSON.parseObject(outlineData14To34).getString("修改图形理由");
            String textNew2 = JSON.parseObject(outlineData14To34).getString("修改方法");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("修改图形理由", textNew1);
            jsonObject.put("修改方法", textNew2);
            responseData = jsonObject.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setData(responseData);
        return SUCCESS;
    }

    public String getPageNumber(){
        Integer pageNumber;
        getCurrentPageNumberResponse = new BaseResponse<>();
        try{
            pageNumber =  dbService.fetehPageNumber(outlineDevItemId, "current_page_number");
            getCurrentPageNumberResponse.setData(pageNumber-1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  SUCCESS;


    }

    public String getOutlineDevItemId() {
        return outlineDevItemId;
    }

    public void setOutlineDevItemId(String outlineDevItemId) {
        this.outlineDevItemId = outlineDevItemId;
    }


    public Integer getOutlineID() {
        return outlineID;
    }

    public void setOutlineID(Integer outlineID) {
        this.outlineID = outlineID;
    }

    public Integer getCurrentPageNumber() {
        return currentPageNumber;
    }

    public void setCurrentPageNumber(Integer currentPageNumber) {
        this.currentPageNumber = currentPageNumber;
    }

    public BaseResponse<String> getResponse() {
        return response;
    }

    public void setResponse(BaseResponse<String> response) {
        this.response = response;
    }

    public Integer getPageAction() {
        return pageAction;
    }

    public void setPageAction(Integer pageAction) {
        this.pageAction = pageAction;
    }
    public BaseResponse<List<String>> getDownloadResponse() {
        return downloadResponse;
    }

    public void setDownloadResponse(BaseResponse<List<String>> downloadResponse) {
        this.downloadResponse = downloadResponse;
    }

    public Integer getPicNumber() {
        return picNumber;
    }

    public void setPicNumber(Integer picNumber) {
        this.picNumber = picNumber;
    }


    public BaseResponse<Integer> getGetCurrentPageNumberResponse() {
        return getCurrentPageNumberResponse;
    }

    public void setGetCurrentPageNumberResponse(BaseResponse<Integer> getCurrentPageNumberResponse) {
        this.getCurrentPageNumberResponse = getCurrentPageNumberResponse;
    }

}
