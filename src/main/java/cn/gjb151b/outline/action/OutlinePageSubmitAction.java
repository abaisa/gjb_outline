package cn.gjb151b.outline.action;

import cn.gjb151b.outline.service.CoreService;
import cn.gjb151b.outline.utils.BaseResponse;
import cn.gjb151b.outline.utils.ServiceException;
import com.alibaba.fastjson.JSON;
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
        if (!checkParamLegal()) {
            response.setError("param error");
            return SUCCESS;
        }

        try {
            coreService.submitPageData(outlineID, pageNumber, jsonData);
        } catch (ServiceException e){
          System.out.println(e.getExceptionEnums().getErrMsg());
          response.setError("service error");
        } catch (Exception e){
            System.out.println(e.getMessage());
            response.setError("other service error");
        }

        response.setResponse("data from server");
        return SUCCESS;
    }

    /**
     * 检查页面回传参数pageInfo，合法返回true
     * todo 需要校验一下data中的所有参数是否合法，当前只对json格式进行了校验
     */
    private Boolean checkParamLegal() {
        try {
            System.out.println(this.jsonData);
            Object parseRes = JSON.parseArray(this.jsonData);
            System.out.println(parseRes);
        }catch (Exception e){
            System.out.println("param check error");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
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
