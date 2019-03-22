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
    <%--coverDevelopment.png是研制要求系统图片，coverTest.png是试验大纲系统图片--%>
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
    <%--<style>--%>
    <%--.picClass{width: 100%;height: 300px;background:url("../statics/imgs/coverDevelopment.png") no-repeat left top;}--%>
    <%--</style>--%>
        <div class="container" style="padding-top: 20px">
            <div class="row">
                <%--<form>--%>
                <br>
                <br>
                <div class="col-md-offset-4 col-md-4" >
                    <div class="form-group">
                        <input type="text" name="userName" id="userName" class="form-control input-lg" placeholder="账户"/>
                    </div>
                    <div class="form-group">
                        <input type="password" name="userPassword" id="userPassword" class="form-control input-lg" placeholder="密码"/>
                    </div>
                    <div class="form-group">
                        <button id="submit" type="submit" class="btn btn-info btn-block btn-lg" onclick="checkUser()">登录</button>
                    </div>
                    <div class="front-login-hr"></div>
                    <div class="form-group">
                        <input type="button" class="btn btn-default btn-block btn-lg free_reg" style="border: 1px solid #5bc0de;color: #5bc0de"
                               value="修改账户密码" onclick="toChangePassword()">
                    </div>
                </div>
                <%--</form>--%>
            </div>

        </div>

    <s:include value="_footer.jsp"/>
</div>
<!-- 可直接使用框架提供的在线js文件 -->
<script type="text/javascript" src="repack/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="repack/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="repack/js/plugin/front.js"></script>
<script type="text/javascript" src="js/login.js"></script>
</body>
</html>