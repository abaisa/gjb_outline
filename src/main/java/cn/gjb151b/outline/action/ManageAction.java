package cn.gjb151b.outline.action;

import cn.gjb151b.outline.service.ManageService;
import cn.gjb151b.outline.utils.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;

public class ManageAction {
    private String userName;
    private String userPassword;
    private String newPassword;

    private BaseResponse changePasswordResponse;
    @Autowired
    private ManageService  manageService;

    public String changePassword(){

        changePasswordResponse =  manageService.changePassword(userName, userPassword, newPassword);
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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    public BaseResponse getChangePasswordResponse() {
        return changePasswordResponse;
    }

    public void setChangePasswordResponse(BaseResponse changePasswordResponse) {
        this.changePasswordResponse = changePasswordResponse;
    }


}
