package cn.gjb151b.outline.service;

import cn.gjb151b.outline.model.OutlineUserInfo;
import cn.gjb151b.outline.outlineDao.OutlineUserInfoMapper;
import cn.gjb151b.outline.utils.BaseResponse;
import cn.gjb151b.outline.utils.MD5;
import com.opensymphony.xwork2.ActionContext;
import cn.gjb151b.outline.model.UserLogin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.Map;
@Service(value = "LoginService")
public class LoginService {

    @Resource
    private OutlineUserInfoMapper outlineUserInfoMapper;

    @Autowired
    public LoginService(OutlineUserInfoMapper outlineUserInfoMapper){
        this.outlineUserInfoMapper = outlineUserInfoMapper;

    }




    public BaseResponse<UserLogin> checkUser(String userName, String userPassword) {

        BaseResponse<UserLogin> checkUserResponse = new BaseResponse<UserLogin>();
        OutlineUserInfo outlineUserInfo = outlineUserInfoMapper.selectUserByName(userName);
//        userInfo= loginDao.checkUser(userName);
//        UserLogin userLogin = new UserLogin(userInfo.getUserName(),userInfo.getUserPassword(),userInfo.getUserLevel(),userInfo.getUserId());
        if(outlineUserInfo == null) {
            checkUserResponse.setMessage("不存在此用户信息");
        }else {
            UserLogin userLogin = new UserLogin(outlineUserInfo.getUserName(),outlineUserInfo.getUserPassword(),outlineUserInfo.getUserLevel(),outlineUserInfo.getUserId());
            String passwordMd5 = MD5.md5(userPassword);
            if(StringUtils.equals(userPassword, userLogin.getUserPassword())) {
                checkUserResponse.setStatus("success");
                checkUserResponse.setData(userLogin);
                checkUserResponse.setMessage("用户登录成功");
                Map session = ActionContext.getContext().getSession();
                session.put("userLogin", userLogin);
            } else {
                checkUserResponse.setMessage("用户密码错误");
            }
        }

        return checkUserResponse;
    }


}
