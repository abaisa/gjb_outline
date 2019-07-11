<%--
  Created by IntelliJ IDEA.
  User: ddgdd
  Date: 2018/8/29 0029
  Time: 9:49
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-default navbar-fixed-top front-nav">
    <div class="container">
        <div>
            <!-- 品牌图片大小为150 * 30：宽度不定，高度固定30px -->
            <!--<div class="nav-brand"><a href="#"><img src="/bootstrap-front/dev/img/logo.png" alt="brand" class="img-responsive"/></a></div>-->
            <!-- -->
            <div class="nav-brand" style="font-size: 22px"><a href="#">试验大纲.管理系统</a></div>
        </div>
        <div class="nav-collapse collapse" id="nav-collapse-demo">
            <ul class="nav navbar-nav">
                <li class="${param.act == "item" ? "front-active" : ""}"><a href="views/develop/admin/item.jsp" >项目管理</a></li>
                <li class="${param.act == "user" ? "front-active" : ""}"><a href="views/develop/admin/user.jsp" >用户管理</a></li>
            </ul>
        </div>
        <div class="nav-right">
            <ul class="nav navbar-nav">
                <li><a href="#">${session.userLogin.userName}</a></li>
                <li><a href="login/logout">注销</a></li>
            </ul>
        </div>
    </div>
</nav>

