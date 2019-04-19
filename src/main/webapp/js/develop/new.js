
function adviceTitle(devStatus,Status) {
    if(Status == 1){
        $("#advice_title_div").hide();
        $("#advice_div").hide();
    }else {
        if (outlineStatus == 1) {
            $("#advice_title_div").show();
            $("#advice_div").show();
            $("#advice").attr("placeholder", "输入校对意见");
            document.getElementById("advice_title").innerHTML = "校对意见";
            document.getElementById("pass").innerHTML = "校对通过";
            document.getElementById("fail").innerHTML = "校对不通过";
        } else if (outlineStatus == 2) {
            $("#advice_title_div").show();
            $("#advice_div").show();
            $("#advice").attr("placeholder", "输入审核意见");
            document.getElementById("advice_title").innerHTML = "审核意见";
            document.getElementById("pass").innerHTML = "审核通过";
            document.getElementById("fail").innerHTML = "审核不通过";
        } else if (outlineStatus == 3) {
            $("#advice_title_div").show();
            $("#advice_div").show();
            $("#advice").attr("placeholder", "输入批准意见");
            document.getElementById("advice_title").innerHTML = "批准意见";
            document.getElementById("pass").innerHTML = "批准通过";
            document.getElementById("fail").innerHTML = "批准不通过";
        } else {
            $("#advice_title_div").hide();
            $("#advice_div").hide();
        }
    }
    $.ajax({
        type: "post",
        url: "/outline/page_data/loadAdvice",
        data: {
            outlineID: outlineId
        },

        success: function (data) {
            if (data.status == "success") {
                var all_data = JSON.parse(data.data);
                console.log("长度："+JSON.parse(all_data.outlineAdviceProofread.toString()).length);
                //填充意见框
                outlineAdviceProofread = JSON.parse(all_data.outlineAdviceProofread.toString());
                outlineAdviceAudit = JSON.parse(all_data.outlineAdviceAudit.toString());
                outlineAdviceAuthorize = JSON.parse(all_data.outlineAdviceAuthorize.toString());
                historyAdviceProofread(JSON.parse(all_data.outlineAdviceProofread.toString()));
                historyAdviceAudit(JSON.parse(all_data.outlineAdviceAudit.toString()));
                historyAdviceAuthorize(JSON.parse(all_data.outlineAdviceAuthorize.toString()));

            }else {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: data.message});
            }
        },
        error: function (data) {
            console.log("loadTargetPage ajax 请求失败");
            console.log(data);
            return false
        }
    });

}


/*
校对/审核/批准通过
outlineStatus为项目自身状态同数据库同名含义
审核通过时，项目状态发生改变：
校对/审核通过时，outlineStatus均加1；批准通过时，outlineStatus由3变为5
*/
function  passResult(outlineStatus, result, userName){
    if(outlineStatus == 0){
        turnPage(1);
    }
    outlineStatusNow = -1;
    if(result == 1){
        //批准通过即项目完成，项目最后状态为5
        if(outlineStatus == 3){
            outlineStatusNow = 5;
        }else{
            outlineStatusNow = outlineStatus+1;
        }
    }else{
        outlineStatusNow = 4;
    }
    //devAdviceString获取意见框输入
    var outlineAdviceString = $("#advice").val().trim();
    console.log("outlineAdviceString:"+outlineAdviceString);
    var data = {"outlineID": outlineId,"outlineStatus":outlineStatusNow, "outlineStatusOriginal":outlineStatus};
    if(outlineAdviceString != ""){
        var myDate = new Date();
        var adviceDate = myDate.toLocaleString();
        outlineAdviceString = adviceDate+" "+outlineAdviceString;
        var adviceJson = "";
        if(outlineStatus == 1){
            adviceJson = outlineAdviceProofread;
        }else if(outlineStatus == 2){
            adviceJson = outlineAdviceAudit;
        }else if(outlineStatus == 3){
            adviceJson = outlineAdviceAuthorize;
        }
        adviceJson.push(outlineAdviceString);
        outlineAdviceString = JSON.stringify(adviceJson);
        data.outlineAdvice = outlineAdviceString;
        // if(outlineStatus == 1){
        //     data.outlineAdviceProofread=outlineAdviceString;
        // }else if(outlineStatus == 2){
        //     data.outlineAdviceAudit=outlineAdviceString;
        // }else if(outlineStatus == 4){
        //     data.outlineAdviceAuthorize=outlineAdviceString;
        // }
    }else{
        if(outlineStatus == 1){
            data.outlineAdvice = JSON.stringify(outlineAdviceProofread);
        }else if(outlineStatus == 2){
            data.outlineAdvice = JSON.stringify(outlineAdviceAudit);
        }else if(outlineStatus == 3){
            data.outlineAdvice = JSON.stringify(outlineAdviceAuthorize);
        }
    }
    console.log(data);
    $.ajax({
        type: "POST", // 请求类型（get/post）
        url:"/outline/page_data/submitAdvice",
        data:data,
        async: true, // 是否异步
        dataType: "json", // 设置数据类型
        success: function (data){
            console.log("请求成功");
            if(data.status === 'success')  {
                // if(outlineStatus == 1){
                //     alert("校对结果提交成功！");
                // }else if(outlineStatus == 2){
                //     alert("审核结果提交成功！");
                // }else if(outlineStatus == 3){
                //     alert("批准结果提交成功！");
                // }
                alert("提交成功！");
                window.onbeforeunload = undefined;
                // $.fillTipBox({type: 'warning',icon: 'glyphicon-exclamation-sign', content: "校对结果提交成功"});
                window.location.href = "views/develop/project/project.jsp?act=project&userName="+userName;
            }else {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: data.message});

            }
        },
        error: function (errorMsg){
            // 请求失败
            console.log("请求失败");
            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: "请求失败"});
        }
    });
}


//取消操作
function cancelProofread(userName) {
    $("#advice_proofread").val("");
    window.onbeforeunload = undefined;
    window.location.href = "views/develop/project/project.jsp?act=project&userName="+userName;
}


//实时输入校对意见框展示
function historyAdviceProofread(json) {
    var len = json.length;
    var text = "";
    // var draft = "";
    if(len === 0){
        text = "无历史校对意见";
    }else {
        for (var i = 0; i < len; i++) {
            var index = i+1;
            text += "<tr><td>第" + index + "条校对意见: " + json[i] + "</td></tr>";
        }
    }
    document.getElementById("advice_history_proofread").innerHTML=text;
}

//实时输入审核意见框展示
function historyAdviceAudit(json) {
    var len = json.length;
    var text = "";
    if(len === 0){
        text = "无历史审核意见";
    }else {
        for (var i = 0; i < len; i++) {
            var index = i+1;
            text += "<tr><td>第" + index + "条审核意见: " + json[i] + "</td></tr>";
        }
    }
    document.getElementById("advice_history_audit").innerHTML=text;
}

//实时输入批准意见框展示
function historyAdviceAuthorize(json) {
    var len = json.length;
    var text = "";
    if(len === 0){
        text = "无历史批准意见";
    }else {
        for (var i = 0; i < len; i++) {
            var index = i+1;
            text += "<tr><td>第" + index + "条批准意见: " + json[i] + "</td></tr>";
        }
    }
    document.getElementById("advice_history_authorize").innerHTML=text;
}


