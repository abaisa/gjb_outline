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
            outlineID: 1
        },

        success: function (data) {
            if (data.status == "success") {
                var all_data = JSON.parse(data.data);
                console.log("长度："+JSON.parse(all_data.outlineAdviceProofread.toString()).length);
                //填充意见框
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
