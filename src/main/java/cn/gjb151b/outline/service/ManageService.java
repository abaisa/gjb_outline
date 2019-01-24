package cn.gjb151b.outline.service;

import cn.gjb151b.outline.model.OutlineUserInfo;
import cn.gjb151b.outline.outlineDao.OutlineUserInfoMapper;
import cn.gjb151b.outline.utils.BaseResponse;
import cn.gjb151b.outline.utils.MD5;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.dc.pr.PRError;

import javax.annotation.Resource;

@Service(value = "ManageService")
public class ManageService {
    private Logger logger = Logger.getLogger(ManageService.class);
    @Resource
    private OutlineUserInfoMapper outlineUserInfoMapper;
    @Autowired
    public ManageService(OutlineUserInfoMapper outlineUserInfoMapper){
        this.outlineUserInfoMapper = outlineUserInfoMapper;
    }

    public BaseResponse changePassword(String userName, String userPassword, String newPassword){
        BaseResponse changePasswordResponse = new BaseResponse();
        OutlineUserInfo outlineUserInfo = outlineUserInfoMapper.selectUserByName(userName);
        String userPasswordMd5 = MD5.md5(userPassword);
        if(outlineUserInfo == null){
            changePasswordResponse.setMessage("不存在此用户");
            return changePasswordResponse;

        }else {
            String Password = outlineUserInfo.getUserPassword();
            if(!Password.equals(userPasswordMd5)){
                changePasswordResponse.setMessage("用户原始密码输入有误");
                return changePasswordResponse;

            }else {
                outlineUserInfoMapper.updateColByName("user_password", MD5.md5(newPassword), userName);
                changePasswordResponse.setStatus("success");
                changePasswordResponse.setMessage("用户密码修改成功");
                return changePasswordResponse;
            }


        }

    }


}
