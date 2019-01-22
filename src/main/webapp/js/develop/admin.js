function findAllUser() {
    $.post( "admin/findAllUser", function(data) {
        if(data.status=="success") {
            var userList = data.data;
            $("#userTable").html('<TR><TD>用户名</TD><TD>层级</TD><TD>备注</TD>' +
                '<TD>重置密码</TD><TD>修改用户信息</TD>' +
                '<TD>删除用户</TD></TR>');
            for(var i = 0;i<userList.length;i++) {
                if(userList[i].userLevel==1){
                    var level="管理员";
                }else if(userList[i].userLevel==2){
                    var level="上级人员";
                }else{
                    var level="技术人员";
                }
                if(userList[i].userRemark==null){
                    var remark="";
                }
                else{
                    remark=userList[i].userRemark;
                }
                if((userList[i].userNew=="[]")&&(userList[i].userProofread=="[]")&&(userList[i].userAudit=="[]"&&(userList[i].userAuthorize=="[]"))){
                    $("#userTable").append('<TR><TD>'+ userList[i].userName +'</TD><TD>' + level
                        +'</TD><TD>'+remark+'</TD><TD>'
                        +'<a href="javascript:void(0);" data-toggle="front-modal" data-title="重置密码" ' +
                        'data-href="views/develop/admin/resetPassword.jsp?userId='+userList[i].userId+'">重置密码</a>'+ '</TD><TD>'
                        +'<a href="javascript:void(0);" data-toggle="front-modal" data-title="修改用户信息" ' +
                        'data-href="views/develop/admin/updateUser.jsp?' +
                        'userId='+userList[i].userId+'&userName='+userList[i].userName+'&userLevel='+userList[i].userLevel+'&userRemark='+userList[i].userRemark+'&user='+userList[i]+'">修改</a>'+ '</TD><TD>'
                        +'<a href="javascript:void(0);" onclick="deleteUser('+userList[i].userId+',this)">删除</a></TD><TR>');
                }
                else{
                    $("#userTable").append('<TR><TD>'+ userList[i].userName +'</TD><TD>' + level
                        +'</TD><TD>'+remark+'</TD><TD>'
                        +'<a href="javascript:void(0);" data-toggle="front-modal" data-title="重置密码" ' +
                        'data-href="views/develop/admin/resetPassword.jsp?userId='+userList[i].userId+'">重置密码</a>'+ '</TD><TD>'
                        +'<a href="javascript:void(0);" data-toggle="front-modal" data-title="修改用户信息" ' +
                        'data-href="views/develop/admin/updateUser.jsp?' +
                        'userId='+userList[i].userId+'&userName='+userList[i].userName+'&userLevel='+userList[i].userLevel+'&userRemark='+userList[i].userRemark+'&user='+userList[i]+'">修改</a>'+ '</TD><TD>'
                        +'<a>无法删除</a></TD><TR>');
                }
            }
        }

        else{
            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: data.message});
        }


    });
}

function addUser() {
    var userName = $("#userName").val();
    var userPassword = $("#userPassword").val();
    var userLevel = $("input[name='type']:checked").val();
    var userRemark = $("#userRemark").val();
    var userPasswordConfirm = $("#userPasswordConfirm").val();
    if(userName == null || userName.trim() == "") {
        $.fillTipBox({type:'warning', icon:'glyphicon-exclamation-sign', content:'用户名不能为空'});
    }else if(userPassword == null || userPassword.trim() == ""){
        $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '用户密码不能为空'});
    }else if(userPasswordConfirm == null || userPasswordConfirm.trim() == ""){
        $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '请确认密码'});
    }else if(userLevel == null || userLevel.trim() == ""){
        $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '请选择用户层级'});
    }else if(userPassword!= userPasswordConfirm){
        $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '两次密码输入不一致'});
    }
    else {
        $.post("admin/addUser", {
            userName:userName,
            userPassword:userPassword,
            userLevel:userLevel,
            userRemark: userRemark
        }, function (data) {
            if(data.status === 'error')  {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: data.message});
            }else {
                $.tipModal('confirm', 'info', data.message, function (result) {
                    window.location.reload();
                })
            }

        })
    }
}

function deleteUser(userId){
    var userId = userId;
        $.tipModal('confirm', 'danger', '您确定要删除这个用户信息吗？', function (result) {
            if (result) {
                $.post("admin/deleteUser", {
                userId: userId,
                }, function (data) {
                    $.tipModal('confirm', 'info', data.message, function (result) {
                        window.location.reload();
                    })
                })
            }
        });
}

function resetPassword(userId)
{
    var userId = userId;
    var userPassword = $("#resetPassword").val();
    var userPasswordConfirm = $("#resetPasswordConfirm").val();

    if(userPassword == null || userPassword.trim() == "") {
        $.fillTipBox({type:'warning', icon:'glyphicon-exclamation-sign', content:'新密码不能为空'});
    }else if(userPasswordConfirm == null || userPasswordConfirm.trim() == ""){
        $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '请确认密码'});
    }else if(userPassword != userPasswordConfirm){
        $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '两次密码输入不一致'});
    }else {
        $.post("admin/resetPassword", {
            userId:userId,
            userPassword:userPassword,
        }, function (data) {
            $.tipModal('confirm', 'info', data.message, function (result) {
                window.location.reload();
            })
        })
    }
}


function updateUser(userId) {
    var userId = userId;
    var userName = $("#name").val();
    var userLevel = $("input[name='level']:checked").val();
    var userRemark = $("#remark").val();
    if(userName == null || userName.trim() == "") {
        $.fillTipBox({type:'warning', icon:'glyphicon-exclamation-sign', content:'用户名不能为空'});
    }else if(userLevel == null || userLevel.trim() == ""){
        $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '请选择用户层级'});
    }else {
        $.post("admin/updateUser", {
            userId:userId,
            userName:userName,
            userLevel:userLevel,
            userRemark: userRemark
        }, function (data) {
            $.tipModal('confirm', 'info', data.message, function (result) {
                window.location.reload();
            })
        })
    }
}

function findAllItem(){
    $.post( "admin/findAllItem", function(data) {
        if(data.status=="success") {
            var operaterList = data.data;
            $("#itemTable").html('<TR><TD>项目名称</TD><TD>编制</TD><TD>校对</TD>' +
                '<TD>审核</TD><TD>批准</TD><TD>修改项目信息</TD>' +
                '<TD>删除项目</TD></TR>');
            for(var i = 0;i<operaterList.length;i++){
                $("#itemTable").append('<TR><TD>'+ operaterList[i].itemName +'</TD><TD>' + operaterList[i].userNew
                    +'</TD><TD>'+operaterList[i].userProofread+'</TD><TD>'+operaterList[i].userAudit+'</TD><TD>'+operaterList[i].userAuthorize+'</TD><TD>'
                    +'<a href="javascript:void(0);" data-toggle="front-modal" data-title="修改项目信息" ' +
                    'data-href="/GJB151BSys/views/develop/admin/updateItem.jsp?' +
                    'devName='+operaterList[i].itemName+'&devItemId='+operaterList[i].itemId+'&userNew='+operaterList[i].userNew+'&userProofread='+operaterList[i].userProofread+
                    '&userAudit='+operaterList[i].userAudit+'&userAuthorize='+operaterList[i].userAuthorize+'">修改</a>'+ '</TD><TD>'
                    +'<a href="javascript:void(0);" onclick="deleteItem(\''+operaterList[i].itemName+'\')">删除</a></TD><TR>');
            }
        }
        else{
            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: data.message});
        }
    });
}

function addItem() {
    var devName = $("#itemName").val();
    var user_new = $("#user_new").val();
    var user_proofread = $("#user_proofread").val();
    var user_audit = $("#user_audit").val();
    var user_authorize = $("#user_authorize").val();
    if(devName == null || devName.trim() == "") {
        $.fillTipBox({type:'warning', icon:'glyphicon-exclamation-sign', content:'项目名称不能为空'});
    } else if((user_new==user_proofread)||(user_new==user_audit)||(user_new==user_authorize)
    ||(user_proofread==user_audit)||(user_proofread==user_authorize)||(user_audit==user_authorize)){
        $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '需选择不同的用户进行操作'});
    } else{
        $.post("admin/addItem", {
            devName: devName,
            user_new: user_new,
            user_proofread: user_proofread,
            user_audit: user_audit,
            user_authorize: user_authorize,
        }, function (data) {
            if(data.status === 'error')  {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: data.message});
            }else {
                $.tipModal('confirm', 'info', data.message, function (result) {
                    window.location.reload();
                })
            }

        })
    }
}

function deleteItem(devName){
    $.tipModal('confirm', 'danger', '您确定要删除这个项目吗？', function (result) {
        if (result) {
            $.post("admin/deleteItem", {
                devName: devName,
            }, function (data) {
                $.tipModal('confirm', 'info', data.message, function (result) {
                    window.location.reload();
                })
            })
        }
    });
}



