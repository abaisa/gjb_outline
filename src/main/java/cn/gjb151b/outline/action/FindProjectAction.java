package cn.gjb151b.outline.action;

import cn.gjb151b.outline.model.ViewProject;
import cn.gjb151b.outline.service.ViewProjectService;
import cn.gjb151b.outline.utils.BaseResponse;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.Map;

public class FindProjectAction extends ActionSupport {
    @Autowired
    private ViewProjectService viewProjectService;

    private static Logger logger = Logger.getLogger(FindProjectAction.class);
    private String userName;
    private BaseResponse<Map<String, List<ViewProject>>> projectResultResponse = new BaseResponse<Map<String, List<ViewProject>>>();

    public String showProject() {

        Map<String, List<ViewProject>> projectResults =viewProjectService.allProject(userName);
        logger.info(projectResults.get("showNew"));
        if (projectResults == null) {
            projectResultResponse.setStatus("error");
            projectResultResponse.setMessage("用户项目列表更新失败");
        } else {
            projectResultResponse.setData(projectResults);
            projectResultResponse.setStatus("success");
        }
        return "success";


    }

    public String getUserName(){return userName;}
    public void setUserName(String userName){this.userName = userName;}
    public BaseResponse<Map<String,List<ViewProject>>> getProjectResultResponse(){return projectResultResponse;}
    public void setProjectResultResponsesult(BaseResponse<Map<String,List<ViewProject>>> projectResultResponse){this.projectResultResponse = projectResultResponse;}
}
