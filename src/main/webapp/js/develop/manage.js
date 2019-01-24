function changePassword() {
    var userName = $('#userName').val();
    var userPassword = $('#userPassword').val();
    var newPassword1 = $('#newPassword1').val();
    var newPassword2 = $('#newPassword2').val();
    if(userName.length === 0) {
        $.fillTipBox({type:'warning', icon:'glyphicon-exclamation-sign', content:'用户名不能为空'});
    }else if(userPassword.length === 0) {
        $.fillTipBox({type:'warning', icon:'glyphicon-exclamation-sign', content:'用户密码不能为空'});
    }else if(newPassword1.length === 0) {
        $.fillTipBox({type:'warning', icon:'glyphicon-exclamation-sign', content:'用户新密码不能为空'});
    } else if(newPassword2.length === 0) {
        $.fillTipBox({type:'warning', icon:'glyphicon-exclamation-sign', content:'请再次输入新密码'});
    }else if(newPassword1 !== newPassword2) {
        $.fillTipBox({type:'warning', icon:'glyphicon-exclamation-sign', content:'两次新密码输入不同'});
    } else {
        $.post("manage/changePassword", {userName:userName, userPassword:userPassword, newPassword:newPassword1}, function (data) {
            if(data.status === 'error')  {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: data.message});
            }else {
                $.fillTipBox({type: 'success', icon: 'glyphicon-exclamation-sign', content: data.message});
               window.location.href = "views/login.jsp";
            }
        })
    }
}

function getParams() {
    var paramUrl = window.location.search.substr(1);
    var paramStrs = paramUrl.split('&');
    var params = {};
    for (var index = 0; index < paramStrs.length; index++) {
        params[paramStrs[index].split('=')[0]] = decodeURI(paramStrs[index].split('=')[1]);
    }
    return params;
}