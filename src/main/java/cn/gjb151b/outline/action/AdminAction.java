package cn.gjb151b.outline.action;

import cn.gjb151b.outline.model.OutlineUserInfo;
import cn.gjb151b.outline.service.AdminService;
import cn.gjb151b.outline.utils.BaseResponse;
import cn.gjb151b.outline.utils.MD5;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.List;


public class
AdminAction extends ActionSupport{
    private Logger logger = Logger.getLogger(AdminAction.class);

    private int userId;
    private String userName;
    private String userPassword;
    private int userLevel;
    private String userNew;
    private String userProofread;
    private String userAudit;
    private String userAuthorize;
    private String userRemark;
    private Timestamp userCreateTime;
    private Timestamp userUpdateTime;
    private BaseResponse findAllUserResponse = new BaseResponse<List<OutlineUserInfo>>();
    private BaseResponse addUserResponse = new BaseResponse<String>();
    private BaseResponse deleteUserResponse = new BaseResponse<String>();
    private BaseResponse resetPasswordResponse = new BaseResponse<String>();
    private BaseResponse updateUserResponse = new BaseResponse<String>();
    private BaseResponse changePasswordResponse = new BaseResponse();
    @Autowired
    private AdminService adminService;

    public String findAllUser(){
        findAllUserResponse = adminService.findAllUser();
        logger.info(findAllUserResponse.getStatus());
        return "success";
    }

    public String addUser(){
        OutlineUserInfo s = new OutlineUserInfo();
        s.setUserName(this.userName);
        s.setUserPassword(MD5.md5(this.userPassword));
        s.setUserRemark(this.userRemark);
        s.setUserLevel(this.userLevel);
        s.setUserNew("[]");
        s.setUserProofread("[]");
        s.setUserAudit("[]");
        s.setUserAuthorize("[]");
        addUserResponse = adminService.addUser(s);
        return "success";
    }

    public String deleteUser(){
        deleteUserResponse =  adminService.deleteUser(userId);
        return "success";
    }

    public String updateUser(){
        updateUserResponse = adminService.updateUser(userId, userName, userLevel, userRemark);
        return "success";
    }

    public String resetPassword(){
        resetPasswordResponse = adminService.resetPassword(this.userId, this.userPassword);
        return "success";
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public BaseResponse<List> getFindAllUserResponse() {
        return findAllUserResponse;
    }

    public BaseResponse<String> getAddUserResponse() {
        return addUserResponse;
    }

    public BaseResponse<String> getDeleteUserResponse() {
        return deleteUserResponse;
    }

    public BaseResponse<String> getResetPasswordResponse() {
        return resetPasswordResponse;
    }

    public BaseResponse<String> getUpdateUserResponse() {
        return updateUserResponse;
    }

    public String getUserNew() {
        return userNew;
    }

    public void setUserNew(String userNew) {
        this.userNew = userNew;
    }


    public String getUserProofread() {
        return userProofread;
    }

    public void setUserProofread(String userProofread) {
        this.userProofread = userProofread;
    }


    public String getUserAudit() {
        return userAudit;
    }

    public void setUserAudit(String userAudit) {
        this.userAudit = userAudit;
    }


    public String getUserAuthorize() {
        return userAuthorize;
    }

    public void setUserAuthorize(String userAuthorize) {
        this.userAuthorize = userAuthorize;
    }


    public String getUserRemark() {
        return userRemark;
    }

    public void setUserRemark(String userRemark) {
        this.userRemark = userRemark;
    }


    public Timestamp getUserCreateTime() {
        return userCreateTime;
    }

    public void setUserCreateTime(Timestamp userCreateTime) {
        this.userCreateTime = userCreateTime;
    }


    public Timestamp getUserUpdateTime() {
        return userUpdateTime;
    }

    public void setUserUpdateTime(Timestamp userUpdateTime) {
        this.userUpdateTime = userUpdateTime;
    }
    public BaseResponse getChangePasswordResponse() {
        return changePasswordResponse;
    }

    public void setChangePasswordResponse(BaseResponse changePasswordResponse) {
        this.changePasswordResponse = changePasswordResponse;
    }




}
