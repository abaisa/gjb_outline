<%--
  Created by IntelliJ IDEA.
  User: ddgdd
  Date: 2018/8/20 0020
  Time: 11:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<head>
    <base href="<%=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()%>/"/>
    <link rel="stylesheet" href="repack/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="repack/css/front.css">
</head>
<body>
<div>
    <div class="picClass" style="background-color: #74BEDA; width: 100%;height: 300px;">
        <%--<a><img src="statics/imgs/coverDevelopment.png" alt="" style="width: 100%;height: 300px;">--%>
        <%--</a>--%>
        <br><br><br>
        <h1 style="text-align:center; font-size: 30px; color: white; font-family:微软雅黑">
            <%--<font size="6" color="white">GJB151B实施管理系统</font>--%>
            GJB151B实施管理系统
        </h1>
        <br>
        <h1 style="text-align:center;font-size: 60px; color: white; font-family:微软雅黑">
            试验大纲管理系统
        </h1>

    </div>
    <div class="container" style="padding-top: 20px">
        <div class="row">
            <%--<form>--%>
                <div class="col-md-offset-4 col-md-4" >
                    <div class="form-group">
                        <input type="text" name="userName" id="userName" class="form-control input-lg" placeholder="账户"/>
                    </div>
                    <div class="form-group">
                        <input type="password" name="userPassword" id="userPassword" class="form-control input-lg" placeholder="原密码"/>
                    </div>
                    <div class="form-group">
                        <input type="password" name="newPassword1" id="newPassword1" class="form-control input-lg" placeholder="新密码"/>
                    </div>
                    <div class="form-group">
                        <input type="password" name="newPassword2" id="newPassword2" class="form-control input-lg" placeholder="确认新密码"/>
                    </div>
                    <div class="form-group">
                        <button id="submit" type="submit" class="btn btn-info btn-block btn-lg" onclick="changePassword()">提交修改</button>
                    </div>
                    <div class="front-login-hr"></div>
                    <div class="form-group">
                        <input type="button" class="btn btn-default btn-block btn-lg free_reg" style="border: 1px solid #5bc0de;color: #5bc0de"
                               value="放弃修改密码" onclick="window.location.href='/outline/login'">
                    </div>
                </div>
            <%--</form>--%>
        </div>

    </div>

    <s:include value="../../_footer.jsp"/>
</div>

<!-- 可直接使用框架提供的在线js文件 -->
<script type="text/javascript" src="repack/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="repack/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="repack/js/plugin/front.js"></script>
<script type="text/javascript" src="js/develop/manage.js"></script>
<script type="text/javascript">
    var params = getParams();
    var userName = params['username'];
    if(userName.length !== 0) {
        $('#userName').val(userName)
    }
</script>
</body>
</html>
