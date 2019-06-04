<%--
  Created by IntelliJ IDEA.
  User: wangtengteng
  Date: 2018/10/13
  Time: 下午12:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.List"%>
<%@ page import="com.opensymphony.xwork2.ActionContext" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<head>
    <base href="<%=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()%>/"/>
    <link rel="stylesheet" href="repack/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="repack/css/front.css">
    <link rel="stylesheet" href="statics/css/develop/new/new.css">
    <script type="text/javascript" src="repack/js/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="js/develop/project.js"></script>
    <script type="text/javascript" src="repack/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="repack/js/plugin/front.js"></script>
    <script type="text/javascript" src="js/develop/manage.js"></script>


</head>
<body class="front-body">
<div id = "modelurl" class="front-inner">
    <%
       String userName = request.getParameter("userName");
        String url = "../_nav.jsp?act=project&userName="+userName;
    %>
    <%--<s:include value="../_nav.jsp?act=project"/>--%>
    <s:include value="../_nav.jsp?">
        <s:param name="userName">admin</s:param>
        <s:param name="act">${param.act}</s:param>
    </s:include>

    <div class="container">

        <%--这是带下拉菜单的表格      --%>
        <div class="row front-canvas" id="front-canvas">
            <div class="col-md-3 nav-left-offcanvas" id="nav-left-offcanvas">
                <div class="list-group nav-left" style="font-size: 15px">
                    <br>
                    <br>

                    <a type="button" onclick="userAll()" class="list-group-item" data-toggle="front-nav-left-sub" data-target="#slide-demo-xxx">全部<span class="glyphicon glyphicon-chevron-right"></span></a>
                    <a type="button" onclick="userNew()" class="list-group-item">待编制<span class="glyphicon glyphicon-chevron-right"></span></a>
                    <a type="button" onclick="userProofread()" class="list-group-item">待校对<span class="glyphicon glyphicon-chevron-right"></span></a>
                    <a type="button" onclick="userAudit()" class="list-group-item">待审核<span class="glyphicon glyphicon-chevron-right"></span></a>
                    <a type="button" onclick="userAuthorize()" class="list-group-item">待批准<span class="glyphicon glyphicon-chevron-right"></span></a>
                    <a type="button" onclick="userModify()" class="list-group-item">待修改<span class="glyphicon glyphicon-chevron-right"></span></a>
                </div>
            </div>

<%--<div class="panel panel-default front-panel">--%>
            <div class="col-md-9"  id="table">
                <table   class="table table-striped front-table"
                         style="margin-bottom: 0px;font-size: 15px; text-decoration:none; border:0px;">
                    <thead>
                    <tr id ="table_head">
                        <th style="width: 60%;border: 0px" id="project_name">
                            <div >待完成项目</div>
                        </th>
                        <th width="15%" style="text-align: center;border: 0px">
                            <div>项目状态</div>
                        </th>
                        <th width="40%"  style="text-align: center;border: 0px">
                            <div>操作</div>
                        </th>
                    </tr>
                    </thead>
                    <tbody id = "project_table">
                    </tbody>



                </table>
            </div>
        </div>
        <script type="text/javascript">
            var params = getParams();
            var userName = params['userName'];
            var act = params['act'];
            // $("#table").attr("style","margin-left:100px");
            $.ajax({
                type: "get", // 请求类型（get/post）
                url: "/outline/project/findproject",
                data: {"userName":userName},
                async: true, // 是否异步
                dataType: "json", // 设置数据类型
                success: function (data){
                    console.log("请求成功");
                    console.log(data);
                    if(data.status === 'error')  {
                        // alert(data.message);
                        // $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: data.message});
                        $("#table_head").hide();
                        $("#nav-left-offcanvas").hide();
                        document.getElementById("project_table").innerHTML = "<br><br>"+data.message;
                    }else {
                        var showtext = showText(data.data,act);
                        if(showtext == "")
                        {
                            document.getElementById("project_table").innerHTML = "<br><br>无可选项目";
                            $("#table_head").hide();
                        }else {
                            document.getElementById("project_table").innerHTML = showtext;
                            $("#table_head").show();
                        }
                    }
                },
                error: function (errorMsg){
                    // 请求失败
                    console.log("请求失败");
                    $("#table_head").hide();
                    $("#nav-left-offcanvas").hide();
                    document.getElementById("project_table").innerHTML = "<br><br>请求失败";
                }
            });
        </script>




    </div>
</div>


</body>
</html>
