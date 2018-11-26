package cn.gjb151b.outline.action;

import cn.gjb151b.outline.service.DependencyService;
import cn.gjb151b.outline.utils.BaseResponse;
import cn.gjb151b.outline.utils.ServiceException;
import com.sun.xml.internal.rngom.parse.host.Base;
import org.springframework.beans.factory.annotation.Autowired;

import static com.opensymphony.xwork2.Action.SUCCESS;

/**
 * Created by ddgdd on 2018/11/20 0020 16:52
 */
public class DependencyAction {

    private int outlineId;

    private BaseResponse<String> response = new BaseResponse<>();

    @Autowired
    private DependencyService dependencyService;

    public String getSubsysOrEqpHead() {
        String responseData;
        try {
            responseData = dependencyService.getSubsysOrEqpHead(outlineId);
        } catch (ServiceException e) {
            response.setError(e);
            return SUCCESS;
        } catch (Exception e) {
            response.setError("other service error");
            return SUCCESS;
        }

        response.setResponse(responseData);

        return SUCCESS;
    }

    //TODO 保存头部信息

    public int getOutlineId() {
        return outlineId;
    }

    public void setOutlineId(int outlineId) {
        this.outlineId = outlineId;
    }

    public BaseResponse<String> getResponse() {
        return response;
    }

    public void setResponse(BaseResponse<String> response) {
        this.response = response;
    }
}
