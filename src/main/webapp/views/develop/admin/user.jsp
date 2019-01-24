<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<!DOCTYPE html>
<head>
    <base href="<%=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()%>/"/>
    <!-- 可直接使用框架提供的在线CSS文件 -->
    <!-- 若要使用本地的文件，请注释掉下面两行标记 -->
    <!--<link rel="stylesheet" href="http://newfront.free4inno.com/bootstrap/css/bootstrap.min.css">-->
    <!--<link rel="stylesheet" href="http://newfront.free4inno.com/css/front.css">-->

    <!-- 也可以使用下载到本地的CSS文件，请去掉下面两行标记的注释。其中localpath为本地路径。 -->
    <!-- 若要使用在线文件，请注释掉下面两行标记 -->
    <link rel="stylesheet" href="repack/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="repack/css/front.css">
    <link rel="stylesheet" href="statics/css/develop/new/new.css">
</head>
<body class="front-body" onload = "findAllUser();">
<div class="front-inner">
    <s:include value="_nav_admin.jsp">
        <s:param name="act">user</s:param>
    </s:include>
    <div class="container">
        <div style="margin-bottom:40px;">
            <a href="javascript:void(0);" data-toggle="front-modal" class="pull-right btn btn-primary" data-title="新增用户" data-href="views/develop/admin/addUser.jsp">新建</a>
        </div>
        <div class="panel panel-default front-panel">
            <div class="panel-body front-no-padding">
                <table id="userTable"
                       class="table table-striped" style="margin-bottom: 0px; text-align: center">
                </table>
            </div>
        </div>
    </div>
    <s:include value="../../_footer.jsp"/>
</div>

<!-- 可直接使用框架提供的在线js文件 -->
<!-- 若要使用本地的文件，请注释掉下面三行标记 -->
<script type="text/javascript" src="repack/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="repack/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="repack/js/plugin/front.js"></script>
<script type="text/javascript" src="js/develop/admin.js"></script>
</body>
</html>
