package cn.gjb151b.outline.action;

import cn.gjb151b.outline.service.DependencyService;
import cn.gjb151b.outline.utils.BaseResponse;
import cn.gjb151b.outline.utils.ServiceException;
import com.sun.xml.internal.rngom.parse.host.Base;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import static cn.gjb151b.outline.utils.CommonUtils.checkParamLegal;
import static com.opensymphony.xwork2.Action.SUCCESS;

/**
 * Created by ddgdd on 2018/11/20 0020 16:52
 */
public class DependencyAction {

    private static Logger logger = Logger.getLogger(DependencyAction.class);

    private int outlineId;

    private String subsysOrEqpData;

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

    public String submitSubsysOrEqpHead() {
        try {
            checkParamLegal(subsysOrEqpData);
        } catch (ServiceException e) {
            logger.error(String.format("subsysOrEqpData param parse error, outlineID:%d errInfo:%s", outlineId, e.getExceptionEnums().getErrMsg()));

            response.setError(e.getExceptionEnums().getErrMsg());
            return SUCCESS;
        } catch (Exception e) {
            logger.error(String.format("subsysOrEqpData param parse error, outlineID:%d errInfo:%s", outlineId, e.getMessage()));

            response.setError(e.getMessage());
            return SUCCESS;
        }

        try {
            dependencyService.submitSubsysOrEqpHead(outlineId, subsysOrEqpData);
        } catch (Exception e) {
            logger.info(String.format("submitSubsysOrEqpData error, outlineID:%d subsysOrEqpData:%s errInfo:%s", outlineId, subsysOrEqpData,
                    e.getMessage()));
            response.setError("submitSubsysOrEqpData error");

            return SUCCESS;
        }

        response.setResponse("data from server");

        return SUCCESS;
    }

    public int getOutlineId() {
        return outlineId;
    }

    public void setOutlineId(int outlineId) {
        this.outlineId = outlineId;
    }

    public String getSubsysOrEqpData() {
        return subsysOrEqpData;
    }

    public void setSubsysOrEqpData(String subsysOrEqpData) {
        this.subsysOrEqpData = subsysOrEqpData;
    }

    public BaseResponse<String> getResponse() {
        return response;
    }

    public void setResponse(BaseResponse<String> response) {
        this.response = response;
    }
}
