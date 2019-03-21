package cn.gjb151b.outline.action;


import cn.gjb151b.outline.service.LoginService;
import cn.gjb151b.outline.utils.BaseResponse;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;


/**
 * Created by ddgdd on 2018/8/23 0023 11:40
 */
public class LoginAction extends ActionSupport{

    private String userName;
    private String userPassword;

    private BaseResponse checkUserResponse = new BaseResponse<Integer>();
    @Autowired
    private LoginService loginService;


    public String checkUser() {

        if(StringUtils.isNotBlank(userName)) {
            if(StringUtils.isNotBlank(userPassword)) {
                checkUserResponse = loginService.checkUser(userName, userPassword);
            }else {
                checkUserResponse.setMessage("密码不能为空");
            }
        }else {
            checkUserResponse.setMessage("用户名不能为空");
        }


        return "success";
    }

    public String logout() {
        ActionContext.getContext().getSession().clear();

        return "success";
    }





    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public BaseResponse<Integer> getCheckUserResponse() {
        return checkUserResponse;
    }

    public void setCheckUserResponse(BaseResponse<Integer> checkUserResponse) {
        this.checkUserResponse = checkUserResponse;
    }
}
