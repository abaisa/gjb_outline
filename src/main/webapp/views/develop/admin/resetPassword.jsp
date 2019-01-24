<%--
  Created by IntelliJ IDEA.
  User: HP
  Date: 2018/10/12
  Time: 14:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String userId=request.getParameter("userId");
%>
<div class="modal-body">
    <form class="form-horizontal">
        <div class="form-group">
            <label class="col-lg-3 col-md-2 control-label">新密码</label>
            <div class="col-lg-9 col-md-10">
                <input id="resetPassword" class="form-control"  type="password" placeholder="必填。"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-lg-3 col-md-2 control-label">确认新密码</label>
            <div class="col-lg-9 col-md-10">
                <input id="resetPasswordConfirm" class="form-control" type="password" placeholder="必填。"/>
            </div>
        </div>
    </form>
</div>

<div class="modal-footer">
    <a href="javascript:void(0)" class="btn btn-default" data-dismiss="modal">取消</a>
    <a href="javascript:void(0)" class="btn btn-primary" onclick="resetPassword(<%=userId%>)">确定</a>
</div>



