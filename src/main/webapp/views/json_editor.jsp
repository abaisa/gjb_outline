<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()%>/"/>
    <link rel="stylesheet" href="repack/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="repack/css/front.css">
    <link rel="stylesheet" href="css/common.css">
    <meta charset="utf-8"/>
    <title>试验大纲基础页面</title>
</head>
<body>
<div class="front-inner">
    <s:include value="_nav.jsp?">
    </s:include>
    <div class="container">
        <div id="editor_head" class="hidden"></div>
        <%--核心div--%>
        <div id='editor_holder'></div>
        <div class="text-right">
            <button class="btn btn-primary" id="pre_page" onclick="turnPage(2)">上一页</button>
            <button class="btn btn-primary" id="next_page" onclick="turnPage(1)">下一页</button>
        </div>

        <%--<button id='submit' class="btn btn-primary">Submit</button>--%>
    </div>

</div>

<%--通用js文件--%>
<script type="text/javascript" src="repack/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="repack/jsoneditor/jsoneditor.js"></script>

<%--页面方法--%>
<script type="text/javascript" src="js/base_json_editor.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/monitor.js"></script>
<script>
    // 页码初始化为2
    page_number = 12;

    //调用loadTargetPage(1)，后台设置了2的下一页为3，初始化此页面为第三页
    loadTargetPage(1);

    //加载分系统或设备的头部内容
    getSubsysOrEqpHead();
</script>
</body>
</html>
