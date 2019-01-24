package cn.gjb151b.outline.service;


import cn.gjb151b.outline.model.OutlineUserInfo;
import cn.gjb151b.outline.outlineDao.OutlineUserInfoMapper;
import cn.gjb151b.outline.utils.BaseResponse;
import cn.gjb151b.outline.utils.MD5;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service(value="AdminService")
public class AdminService{
    private static Logger logger = Logger.getLogger(AdminService.class);
    @Resource
    private OutlineUserInfoMapper outlineUserInfoMapper;

    @Autowired
    public AdminService(OutlineUserInfoMapper outlineUserInfoMapper){
        this.outlineUserInfoMapper = outlineUserInfoMapper;
    }
    public BaseResponse<List> findAllUser(){
        BaseResponse<List> findAllUserResponse = new BaseResponse<>();
        List<OutlineUserInfo> conts = outlineUserInfoMapper.findAllUser();
        logger.info("11111111111111");
        if(conts == null) {
            findAllUserResponse.setStatus("error");
            findAllUserResponse.setMessage("目前没有用户");
        }else {
            findAllUserResponse.setStatus("success");
            findAllUserResponse.setData(conts);
            findAllUserResponse.setMessage("目前有用户");
        }
        return findAllUserResponse;


    }

    public BaseResponse resetPassword(int userId,String userPassword ){
        outlineUserInfoMapper.updateCol(userId,"user_password", MD5.md5(userPassword));
        BaseResponse resetPasswordResponse = new BaseResponse();
        String userName = outlineUserInfoMapper.selectUserById(userId).getUserName();
        if(userName == null){
            resetPasswordResponse.setStatus("error");
            resetPasswordResponse.setData(userName);
            resetPasswordResponse.setMessage("用户名为'"+userName+"'的密码重置失败");
        }
        else{
            resetPasswordResponse.setStatus("success");
            resetPasswordResponse.setData(userName);
            resetPasswordResponse.setMessage("用户名为'"+userName+"'的密码重置成功");
        }
        return resetPasswordResponse;
    }

    public BaseResponse deleteUser(int userId){
        BaseResponse deleteUserResponse = new BaseResponse();
        outlineUserInfoMapper.deleteUserById(userId);
        String userName = outlineUserInfoMapper.selectUserById(userId).getUserName();
        if(userName == null){
            deleteUserResponse.setStatus("error");
            deleteUserResponse.setData(userName);
            deleteUserResponse.setMessage("用户名为'"+userName+"'的用户信息删除失败");
        }
        else{
            deleteUserResponse.setStatus("success");
            deleteUserResponse.setData(userName);
            deleteUserResponse.setMessage("用户名为'"+userName+"'的用户信息删除成功");
        }
        return deleteUserResponse;
    }

    public BaseResponse updateUser(int userId, String userName, int userLevel, String userReamrk){
        BaseResponse updateUserResponse = new BaseResponse();
        outlineUserInfoMapper.updateCol(userId,"user_name", userName);
        outlineUserInfoMapper.updateUser(userId,"user_level", userLevel);
        outlineUserInfoMapper.updateCol(userId,"user_remark", userReamrk);
        userName = outlineUserInfoMapper.selectUserById(userId).getUserName();
        if(userName == null){
            updateUserResponse.setStatus("error");
            updateUserResponse.setData(userName);
            updateUserResponse.setMessage("用户名为'"+userName+"'的信息修改失败");
        }
        else{
            updateUserResponse.setStatus("success");
            updateUserResponse.setData(userName);
            updateUserResponse.setMessage("用户名为'"+userName+"'的信息修改成功");
        }
        return updateUserResponse;
    }

    public  BaseResponse addUser(OutlineUserInfo s){
        BaseResponse addUserResponse = new BaseResponse();
      OutlineUserInfo outlineUserInfo = outlineUserInfoMapper.selectUserByName(s.getUserName());
        if(outlineUserInfo == null) {
            outlineUserInfoMapper.addUser(s);
            String userName = s.getUserName();
            if(userName == null){
                addUserResponse.setStatus("error");
                addUserResponse.setData(userName);
                addUserResponse.setMessage("添加失败");
            }
            else{
                addUserResponse.setStatus("success");
                addUserResponse.setData(userName);
                addUserResponse.setMessage("成功添加用户'"+userName+"'的信息");
            }
        }else{
            addUserResponse.setStatus("error");
            addUserResponse.setData(s.getUserName());
            addUserResponse.setMessage("用户名'"+s.getUserName()+"'已存在");
        }
        return addUserResponse;
    }







}

