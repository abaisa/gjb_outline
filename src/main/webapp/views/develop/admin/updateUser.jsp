<%--
  Created by IntelliJ IDEA.
  User: HP
  Date: 2018/10/12
  Time: 14:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String userId=request.getParameter("userId");
    String userName=request.getParameter("userName");
    String userLevel=request.getParameter("userLevel");
    String userRemark=request.getParameter("userRemark");
    if(userRemark.equals("null"))
        userRemark="";
%>
<div class="modal-body">
    <form class="form-horizontal">
        <div class="form-group">
            <label class="col-lg-3 col-md-2 control-label">用户</label>
            <div class="col-lg-9 col-md-10">
                <input id="name" name="userName" class="form-control" type="text" value="<%=userName%>" onkeyup="this.value=this.value.replace(/(^\s+)|(\s+$)/g,'');" placeholder="必填"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-lg-3 col-md-2 control-label">层级</label>
            <label class="radio-inline">&nbsp;&nbsp;&nbsp;
                <input type="radio" name="level" value="1" <%= ("1".equals(userLevel))?"Checked":"" %>/>管理员
            </label>
            <label class="radio-inline">
                <input type="radio" name="level" value="2" <%= ("2".equals(userLevel))?"Checked":"" %>/>上级人员
            </label>
            <label class="radio-inline">
                <input type="radio" name="level" value="3" <%= ("3".equals(userLevel))?"Checked":"" %> />技术人员
            </label>
        </div>
        <div class="form-group">
            <label class="col-lg-3 col-md-2 control-label">备注</label>
            <div class="col-lg-9 col-md-10">
                <input id="remark" name="userRemark" class="form-control" type="text" value="<%=userRemark%>" />
            </div>
        </div>
    </form>
</div>

<div class="modal-footer">
    <a href="javascript:void(0)" class="btn btn-default" data-dismiss="modal">取消</a>
    <a href="javascript:void(0)" class="btn btn-primary" onclick="updateUser(<%=userId%>)">确定</a>
</div>


