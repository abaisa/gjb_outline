<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()%>/"/>
    <link rel="stylesheet" href="repack/bootstrap/css/bootstrap.min.css">
    <meta charset="utf-8"/>
    <title>Basic JSON Editor Example</title>
</head>
<body>
    <%--<div>--%>
        <%--<s:include value="_nav.jsp?">--%>
            <%--<s:param name="userName">teng</s:param>--%>
            <%--<s:param name="act">1</s:param>--%>
        <%--</s:include>--%>
    <%--</div>--%>
    <h1>Basic JSON Editor Example</h1>
    <%--核心div--%>
    <div id='editor_holder'></div>
    <button id='submit' class="btn btn-primary">Submit</button>
<script type="text/javascript" src="repack/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="repack/jsoneditor/jsoneditor.js"></script>
<script type="text/javascript" src="js/base_json_editor_demo.js"></script>
</body>
</html>
