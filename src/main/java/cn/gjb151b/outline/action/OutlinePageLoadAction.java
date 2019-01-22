package cn.gjb151b.outline.action;

import cn.gjb151b.outline.service.CoreService;
import cn.gjb151b.outline.utils.BaseResponse;
import cn.gjb151b.outline.utils.ServiceException;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class OutlinePageLoadAction  extends ActionSupport {
    private Integer outlineID;
    private Integer currentPageNumber;
    private Integer pageAction;

    private BaseResponse<String> response;
    private CoreService coreService;

    @Autowired
    OutlinePageLoadAction(CoreService coreService) {
        this.coreService = coreService;
        response = new BaseResponse<>();
    }

    public String load() {
        String responseData;
        try {
            responseData = coreService.getResponseData(outlineID, currentPageNumber, pageAction);
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
}
