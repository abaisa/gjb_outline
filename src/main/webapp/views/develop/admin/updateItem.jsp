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
    String devItemId=request.getParameter("devItemId");
   // out.print(devItemId);
    String devName=request.getParameter("devName");
    String userNew=request.getParameter("userNew");
    String userProofread=request.getParameter("userProofread");
    String userAudit=request.getParameter("userAudit");
    String userAuthorize=request.getParameter("userAuthorize");
%>

<div class="modal-body" >
    <form class="form-horizontal">
        <div class="form-group">
            <label class="col-lg-3 col-md-2 control-label">项目名称</label>
            <div class="col-lg-9 col-md-10">
                <input id="item_name" class="form-control" type="text" value="<%=devName%>" onkeyup="this.value=this.value.replace(/(^\s+)|(\s+$)/g,'');" placeholder="必填"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-lg-3 col-md-2 control-label">编制人员</label>
            <div class="col-lg-9 col-md-10" >
                <select class="form-control" id="userNew">
                    <option select="selected" ><%=userNew%></option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-lg-3 col-md-2 control-label">校对人员</label>
            <div class="col-lg-9 col-md-10">
                <select class="form-control" id="userProofread">
                    <option select="selected" ><%=userProofread%></option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-lg-3 col-md-2 control-label">审核人员</label>
            <div class="col-lg-9 col-md-10">
                <select class="form-control" id="userAudit" >
                    <option select="selected" ><%=userAudit%></option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-lg-3 col-md-2 control-label">批准人员</label>
            <div class="col-lg-9 col-md-10">
                <select class="form-control" id="userAuthorize">
                    <option select="selected" ><%=userAuthorize%></option>
                </select>
            </div>
        </div>
    </form>
</div>

<div class="modal-footer">
    <a href="javascript:void(0)" class="btn btn-default" data-dismiss="modal">取消</a>
    <a href="javascript:void(0)" class="btn btn-primary" onclick="updateItem()">确定</a>
</div>

<script>
    $(function(){
        var user_new = $("#userNew").val();
        var user_proofread = $("#userProofread").val();
        var user_audit = $("#userAudit").val();
        var user_authorize = $("#userAuthorize").val();
        $.ajax( {
            url : "admin/findAllUser",
            type : 'post',
            success : function(data) {
                var userList = data.data;
                for(var i = 0;i<userList.length;i++){
                    if(userList[i].userLevel==3){
                        if(user_new!=userList[i].userName)
                            $("#userNew").append('<option>'+userList[i].userName+'</option>');
                        if(user_proofread!=userList[i].userName)
                            $('#userProofread').append('<option>'+userList[i].userName+'</option>');
                    }
                    if(userList[i].userLevel==2){
                        if(user_audit!=userList[i].userName)
                            $('#userAudit').append('<option>'+userList[i].userName+'</option>');
                        if(user_authorize!=userList[i].userName)
                            $('#userAuthorize').append('<option>'+userList[i].userName+'</option>');
                    }
                }
            }
        });
    })

    function updateItem() {
        var devItemId ="<%=devItemId%>";
        var devName="";
        devName+="<%=devName%>";
        var itemName = $("#item_name").val();
        var user_new = $("#userNew").val();
        var user_proofread = $("#userProofread").val();
        var user_audit = $("#userAudit").val();
        var user_authorize = $("#userAuthorize").val();
        if(itemName == null || itemName.trim() == "") {
            $.fillTipBox({type:'warning', icon:'glyphicon-exclamation-sign', content:'项目名称不能为空'});
        } else if((user_new==user_proofread)||(user_new==user_audit)||(user_new==user_authorize)
            ||(user_proofread==user_audit)||(user_proofread==user_authorize)||(user_audit==user_authorize)){
            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '需选择不同的用户进行操作'});
        } else{
            if(devName!=itemName){
                $.post("admin/updateItemName", {
                    devName: itemName,
                    devItemId: devItemId,
                }, function (data) {
                    if(data.status === 'success')  {
                        $.post("admin/updateOperator", {
                            devName: devName,
                            devItemId: devItemId,
                            user_new: user_new,
                            user_proofread: user_proofread,
                            user_audit: user_audit,
                            user_authorize: user_authorize
                        }, function (data) {
                            if(data.status === 'success')  {
                                $.tipModal('confirm', 'info', data.message, function (result) {
                                    window.location.reload();
                                })
                            }else{
                                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: data.message});
                            }
                        });
                    }else{
                        $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: data.message});
                    }
                });
            }
            else{
                $.post("admin/updateOperator", {
                    devName: devName,
                    devItemId: devItemId,
                    user_new: user_new,
                    user_proofread: user_proofread,
                    user_audit: user_audit,
                    user_authorize: user_authorize
                }, function (data) {
                    if(data.status === 'success')  {
                        $.tipModal('confirm', 'info', data.message, function (result) {
                            window.location.reload();
                        })
                    }else{
                        $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: data.message});
                    }
                });
            }

        }
    }
</script>


