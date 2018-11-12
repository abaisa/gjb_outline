package cn.gjb151b.outline.action;

import cn.gjb151b.outline.service.CoreService;
import cn.gjb151b.outline.utils.BaseResponse;
import com.google.common.base.Strings;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class OutlinePageSubmitAction extends ActionSupport {

    // todo 配一个日志  private final Logger
    private Integer outlineID;
    private Integer pageNumber;
    private String jsonData;
    private BaseResponse<String> response;

    private CoreService coreService;

    @Autowired
    OutlinePageSubmitAction(CoreService coreService) {
        this.coreService = coreService;
        response = new BaseResponse<>();
        response.setError("action error");
    }

    public String submit() {
        // 后端调试日志信息
        System.out.println("OutlinePageSubmitAction call");

        if (!paramCheck()) {
            response.setError("param error");
            return SUCCESS;
        }

        try {
            coreService.submitPageData(outlineID, pageNumber, jsonData);
        }catch (Exception e){
            System.out.println(e.getMessage());
            response.setError("service error");
        }

        response.setStatus("success");
        response.setMessage("");
        response.setData("data from server");
        return SUCCESS;
    }

    /**
     * 检查页面回传参数pageInfo，合法返回true
     * todo 需要校验一下data中的所有参数是否合法，可能需要写一个可配置的校验方法
     */
    private Boolean paramCheck() {
        if (Strings.isNullOrEmpty(this.jsonData)){
            return true;
        }
        return false;
    }

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
}
