function checkUser() {
    var userName = $("#userName").val();
    var userPassword = $("#userPassword").val();
    if(userName.length === 0) {
        $.fillTipBox({type:'warning', icon:'glyphicon-exclamation-sign', content:'用户名不能为空'});
    }else if(userPassword.length === 0){
        $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '用户密码不能为空'});
    }else {
        $.post("login/checkUser", {userName:userName, userPassword:userPassword}, function (data) {
            console.log(data.data);
            if(data.status === 'error')  {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: data.message});
            }else {
                if(data.data.userLevel === 1) {
                    window.location.href = "views/develop/admin/user.jsp";
                }else {
                    window.location.href = "views/develop/project/project.jsp?act=project&userName="+data.data.userName;
                }
            }
        })
    }


        // alert($("#userAdd").serialize());
        // $.ajax({
        //     url:"login/checkUser",
        //     data: {
        //         userName:"test"
        //     },
        //
        //     type:"post",
        //     success:function(data){
        //         console.log(data);
        //     }
        // });
}

function toChangePassword() {
    var userName = $("#userName").val();
    window.location.href = "views/develop/manage/change_password.jsp?username="+userName;
    $.post("view/checkUser",{userName:userName})
}


