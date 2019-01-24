<%--
  Created by IntelliJ IDEA.
  User: HP
  Date: 2018/10/12
  Time: 14:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<div class="modal-body">
    <form class="form-horizontal">
        <div class="form-group">
            <label class="col-lg-3 col-md-2 control-label">用户名</label>
            <div class="col-lg-9 col-md-10">
                <input id="userName" name="userName" class="form-control" type="text" onkeyup="this.value=this.value.replace(/(^\s+)|(\s+$)/g,'');" placeholder="必填。"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-lg-3 col-md-2 control-label">密码</label>
            <div class="col-lg-9 col-md-10">
                <input id="userPassword" name="userPassword" class="form-control" type="password" placeholder="必填。"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-lg-3 col-md-2 control-label">确认密码</label>
            <div class="col-lg-9 col-md-10">
                <input id="userPasswordConfirm" name="userPasswordConfirm" class="form-control" type="password" placeholder="必填。"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-lg-3 col-md-2 control-label">层级</label>
            <label class="radio-inline">&nbsp;&nbsp;&nbsp;
                <input type="radio" name="type" value="1"/>管理员
            </label>
            <label class="radio-inline">
                <input type="radio" name="type" value="2"/>上级人员
            </label>
            <label class="radio-inline">
                <input type="radio" name="type" value="3"/>技术人员
            </label>
        </div>
        <div class="form-group">
            <label class="col-lg-3 col-md-2 control-label">备注</label>
            <div class="col-lg-9 col-md-10">
                <input id="userRemark" name="userRemark" class="form-control" type="text" />
            </div>
        </div>
    </form>
</div>

<div class="modal-footer">
    <a href="javascript:void(0)" class="btn btn-default" data-dismiss="modal">取消</a>
    <a href="javascript:void(0)" class="btn btn-primary" onclick="addUser()">确定</a>
</div>



