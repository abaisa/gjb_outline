<%--
  Created by IntelliJ IDEA.
  User: chenzhehao
  Date: 2019/3/1
  Time: 10:27 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>上传图片</title>
    <base href="<%=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()%>/"/>

</head>
<body>

<form action="/outline/page_data/upload" method="post" enctype="multipart/form-data">
    <input  type="file" name="images">
    <button  type="submit"  name="upload">上传</button>
</form>
<img src="statics/imgs/1f4b078831eb4ac7b6325ad8d82679e8cover.png" >
<img src="statics/img/cover.png" >
</body>
</html>
