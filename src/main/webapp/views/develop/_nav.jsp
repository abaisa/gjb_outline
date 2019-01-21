<%--
  Created by IntelliJ IDEA.
  User: ddgdd
  Date: 2018/8/20 0020
  Time: 14:56
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
            <div class="nav-brand" style="font-size: 22px"><a href="#">GJB151B试验大纲管理系统</a></div>
        </div>
        <div class="nav-collapse collapse" id="nav-collapse-demo">
            <ul class="nav navbar-nav">
                <li class="${param.act == "project"? "front-active":""}"><a id="project" href="/outline/views/develop/project/project.jsp?act=project&userName=${session.userLogin.userName}">项目</a></li>
                <li class="${param.act == "0" || param.act == "4" ? "front-active" : ""}"><a href="/outline/views/develop/project/project.jsp?act=0&userName=${session.userLogin.userName}">编制</a></li>
                <li class="${param.act == "1" ? "front-active" : ""}"><a href="/outline/views/develop/project/project.jsp?act=1&userName=${session.userLogin.userName}">校对</a></li>
                <li class="${param.act == "2" ? "front-active" : ""}"><a href="/outline/views/develop/project/project.jsp?act=2&userName=${session.userLogin.userName}">审核</a></li>
                <li class="${param.act == "3" ? "front-active" : ""}"><a href="/outline/views/develop/project/project.jsp?act=3&userName=${session.userLogin.userName}">批准</a></li>
            </ul>
        </div>
        <div class="nav-right">
            <ul class="nav navbar-nav">
                <li><a id="userName">${session.userLogin.userName}</a></li>
                <li><a href="login/logout">注销</a></li>
            </ul>
        </div>
    </div>
</nav>
<script type="text/javascript" src="repack/js/jquery/jquery.min.js"></script>
<script>
    var userName = "${session.userLogin.userName}";
    console.log("userName"+userName);
    var url = "/outline/views/develop/project/project.jsp?act=project&userName="+userName;
    $("#userName").text(userName);
    $("#project").attr("href",url);
</script>
