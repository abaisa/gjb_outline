package cn.gjb151b.outline.utils;

import cn.gjb151b.outline.model.UserLogin;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;


import java.util.Map;

/**
 * Created by ddgdd on 2018/8/27 0027 10:41
 */
public class LoginInterceptor extends AbstractInterceptor {
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        Map session = (Map) invocation.getInvocationContext().getSession();
        UserLogin user = (UserLogin) session.get("userLogin");
        if(user == null) {
            return Action.LOGIN;

        }

        return invocation.invoke();
    }
}
