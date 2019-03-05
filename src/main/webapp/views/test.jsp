<%--
  Created by IntelliJ IDEA.
  User: chenzhehao
  Date: 2019/3/4
  Time: 2:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <base href="<%=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()%>/"/>
    <!-- 也可以使用下载到本地的CSS文件，请去掉下面两行标记的注释。其中localpath为本地路径。 -->
    <!-- 若要使用在线文件，请注释掉下面两行标记 -->
    <link rel="stylesheet" href="repack/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="repack/css/front.css">
    <link rel="stylesheet" href="statics/css/develop/new/new.css">
</head>
<body>



<div class="container">
    <div id="upload">
        <input type="file" id="images" name="images">
        <button class="btn-primary" id="uploadpic1" onclick="uploadPic1()">上传分系统/设备照片</button>
            <button data-toggle="modal" data-target="#myModal"  class="btn-primary" onclick="findUserAndItem()">查看分系统/设备照片</button>
    </div>--%>
</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">新建项目</h4>
            </div>
            <div class="modal-body" >
                <%--<form class="form-horizontal">--%>
                    <%--<div class="form-group">--%>
                        <%--<label class="col-lg-3 col-md-2 control-label">项目名称</label>--%>
                        <%--<div class="col-lg-9 col-md-10">--%>
                            <%--&lt;%&ndash;<input id="itemName" class="form-control" type="text" onkeyup="this.value=this.value.replace(/(^\s+)|(\s+$)/g,'');" placeholder="必填。"/>&ndash;%&gt;--%>
                            <%--<select class="form-control" id="itemName">--%>
                            <%--</select>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="form-group">--%>
                        <%--<label class="col-lg-3 col-md-2 control-label">编制人员</label>--%>
                        <%--<div class="col-lg-9 col-md-10" >--%>
                            <%--<select class="form-control" id="user_new">--%>
                            <%--</select>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="form-group">--%>
                        <%--<label class="col-lg-3 col-md-2 control-label">校对人员</label>--%>
                        <%--<div class="col-lg-9 col-md-10">--%>
                            <%--<select class="form-control" id="user_proofread">--%>
                            <%--</select>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="form-group">--%>
                        <%--<label class="col-lg-3 col-md-2 control-label">审核人员</label>--%>
                        <%--<div class="col-lg-9 col-md-10">--%>
                            <%--<select class="form-control" id="user_audit">--%>
                            <%--</select>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="form-group">--%>
                        <%--<label class="col-lg-3 col-md-2 control-label">批准人员</label>--%>
                        <%--<div class="col-lg-9 col-md-10">--%>
                            <%--<select class="form-control" id="user_authorize">--%>
                            <%--</select>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</form>--%>
            </div>
            <div class="modal-footer">
                <a href="javascript:void(0)" class="btn btn-default" data-dismiss="modal">取消</a>
                <a href="javascript:void(0)" class="btn btn-primary" onclick="addItem()">确定</a>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

<!-- 可直接使用框架提供的在线js文件 -->
<!-- 若要使用本地的文件，请注释掉下面三行标记 -->
<script type="text/javascript" src="repack/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="repack/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="repack/js/plugin/front.js"></script>
<script type="text/javascript" src="js/develop/admin.js"></script>

</body>
</html>
