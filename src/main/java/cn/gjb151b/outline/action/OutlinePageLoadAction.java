package cn.gjb151b.outline.action;

import cn.gjb151b.outline.service.CoreService;
import cn.gjb151b.outline.utils.BaseResponse;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

// todo load少返回了一个下一页的页码
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
        response.setError("action error");
    }

    public String load() {
        String responseData;
        try {
            responseData = coreService.getResponseData(outlineID, currentPageNumber, pageAction);
        }catch (Exception e){
            System.out.println(e.getMessage());
            response.setError("service error");
            return SUCCESS;
        }

        response.setStatus("success");
        response.setMessage("");
        response.setData(responseData);
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
