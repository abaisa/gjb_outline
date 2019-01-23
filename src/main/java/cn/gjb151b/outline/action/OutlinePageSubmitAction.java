package cn.gjb151b.outline.action;

import cn.gjb151b.outline.service.CoreService;
import cn.gjb151b.outline.utils.BaseResponse;
import cn.gjb151b.outline.utils.ServiceException;
import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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

    private CoreService coreService;

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
            coreService.submitPageData(outlineID, pageNumber, pageAction, jsonData);
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

    public String getOutlineAdvice(){ return  outlineAdvice;}

    public void setOutlineAdvice(String outlineAdvice){ this.outlineAdvice = outlineAdvice;}

    public Integer getOutlineStatus(){ return outlineStatus;}

    public void setOutlineStatus(Integer outlineStatus){ this.outlineStatus = outlineStatus;}

    public Integer getOutlineStatusOriginal(){ return outlineStatusOriginal;}

    public void setOutlineStatusOriginal(Integer outlineStatusOriginal){ this.outlineStatusOriginal = outlineStatusOriginal;}

}
