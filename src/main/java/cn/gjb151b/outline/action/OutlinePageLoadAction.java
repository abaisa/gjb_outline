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
import java.util.List;

@Controller
public class OutlinePageLoadAction  extends ActionSupport {
    private Integer outlineID;
    private Integer currentPageNumber;
    private Integer pageAction;

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
            responseData = coreService.getResponseData(outlineID, currentPageNumber, pageAction);
            logger.info(outlineID+" "+currentPageNumber+" "+pageAction);
        } catch (ServiceException e) {
            System.out.println(e.getExceptionEnums().getErrMsg());
            response.setError(e);
            return SUCCESS;
        } catch (Exception e){
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
            responseData = coreService.getAdvice(outlineID);
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
                String outlineData4 = dbService.fetchData(outlineID, "outline_data_4");
                JSONObject jsonObject = JSON.parseObject(outlineData4);
                picList = (List<String>) jsonObject.get("分系统/设备照片");
            } catch (Exception e){
                e.printStackTrace();
                downloadResponse.setError("other service error");
            }

        }else if(picNumber == 2){
            try{
                String outlineData4 = dbService.fetchData(outlineID, "outline_data_4");
                JSONObject jsonObject = JSON.parseObject(outlineData4);
                picList = (List<String>) jsonObject.get("分系统/设备关系图");
            } catch (Exception e){
                e.printStackTrace();
                downloadResponse.setError("other service error");
            }

        }
        downloadResponse = new BaseResponse<>();
        downloadResponse.setResponse(picList);
        return SUCCESS;

    }

    public String getPageNumber(){
        Integer pageNumber;
        getCurrentPageNumberResponse = new BaseResponse<>();
        try{
            pageNumber =  dbService.fetehPageNumber(outlineID, "current_page_number");
            getCurrentPageNumberResponse.setData(pageNumber-1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  SUCCESS;


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
