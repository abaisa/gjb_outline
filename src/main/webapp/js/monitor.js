/*
页面中需要实现的一些监听函数
 */

function monitor() {
   if(outlineStatus == 1  ||  outlineStatus == 2  ||  outlineStatus == 3  ||  Status == 1){
       editor.disable();
   }
    if (page_number == '3') { //第三页隐藏上一页的按钮
        $("#pre_page").addClass("hidden");
    } else {  //其他页显示上一页的按钮
        $("#pre_page").removeClass("hidden");
    }

    if ((page_number == '5')||(page_number == '9')||(page_number == '11')) {  //显示分系统即设备信息
        $("#editor_head").removeClass("hidden");
    } else {
        $("#editor_head").addClass("hidden");
    }

        switch (page_number) {
            case '3':
                watchQuantity('root.参编单位', 5);
                break;
            case '4':
                editor.getEditor('root.任务名称').disable();
                editor.getEditor('root.分系统/设备').disable();
                editor.getEditor('root.分系统/设备名称').disable();
                editor.getEditor('root.型号').disable();
                editor.getEditor('root.串号').disable();
                editor.getEditor('root.承制单位').disable();
                editor.getEditor('root.预定使用平台').disable();
                break;
            case '5':
                editor.disable();
                break;
            case '7':
                watchQuantity('root.陪试设备清单', 20);
                break;
            case '8':
                editor.getEditor('root.电磁环境').disable();
                editor.getEditor('root.静电放电敏感度试验环境要求.温度').disable();
                editor.getEditor('root.静电放电敏感度试验环境要求.相对湿度').disable();
                editor.getEditor('root.静电放电敏感度试验环境要求.大气压力').disable();
                break;
            case '9':
                editor.getEditor('root.发射测试工作状态确定原则').disable();
                editor.getEditor('root.敏感度测试工作状态确定原则').disable();
                watchQuantity('root.发射测试工作状态', 5);
                watchQuantity('root.敏感度测试工作状态', 5);
                break;
            case '10':
                var load_properties = load_schema.properties;
                if($.isEmptyObject(load_properties)) {
                    loadTargetPage(1);
                }else {
                    watchQuantity('root.电源端口', 10);
                    watchQuantity('root.互联端口', 20);
                }
                editor.watch('root.电源端口', function () {
                    modifyPage10 = true;
                });
                editor.watch('root.互联端口', function () {
                    modifyPage10 = true;
                });
                break;
            case '11':
                watchQuantity('root.敏感度判据及检测方法', 10);
                var errors = editor.validate();
                if(errors.length) {
                    console.log(errors);
                }
                else {
                    console.log("valid");
                }
                break;
            case '12':
                $("#editor_holder button").addClass("hidden");
                for(var i=0 ; i<6; i++){
                    editor.getEditor('root.发射测试参数.'+i+'.频率范围').disable();
                    editor.getEditor('root.发射测试参数.'+i+'.6dB带宽(kHz)').disable();
                    editor.getEditor('root.发射测试参数.'+i+'.驻留时间(s)').disable();
                    editor.getEditor('root.发射测试参数.'+i+'.最小测量时间(模拟式测量接收机)').disable();
                }
                // editor.getEditor('root.发射测试参数.0.频率范围').disable();
                // $("table button").addClass("hidden");
                break;
            case '13':
                $("#editor_holder button").addClass("hidden");
                editor.getEditor('root').disable();
                break;
            case '14':
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.结果评定准则').disable();
                console.log(load_data);
                var testPortArray = load_data.试验端口及被试品工作状态;
                for(var i = 0; i < testPortArray.length; i++) {
                    var editorName = 'root.试验端口及被试品工作状态.'+i+'.试验端口';
                    editor.getEditor(editorName).disable();
                }
                $("#editor_holder button").addClass("hidden");
                //这里改成上面的隐藏按钮的方法，之后若是还有别的按钮，可以选择使用底下注释掉的disable方法（应该是有别的按钮的）
                // disableAddAndDelete('root.试验端口及被试品工作状态', true);

                break;
            case '15':
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.结果评定准则').disable();
                var testPortArray = load_data.试验端口及被试品工作状态;
                for(var i = 0; i < testPortArray.length; i++) {
                    var editorName = 'root.试验端口及被试品工作状态.'+i+'.试验端口';
                    editor.getEditor(editorName).disable();
                }
                $("#editor_holder button").addClass("hidden");
                break;
            case '16':
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                var testPortArray = load_data.试验端口及被试品工作状态;
                for(var i = 0; i < testPortArray.length; i++) {
                    var editorName = 'root.试验端口及被试品工作状态.'+i+'.天线端口';
                    editor.getEditor(editorName).disable();
                }
                $("#editor_holder button").addClass("hidden");
                break;
            case '17':
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                var testPortArray = load_data.试验端口及被试品工作状态;
                for(var i = 0; i < testPortArray.length; i++) {
                    var editorName = 'root.试验端口及被试品工作状态.'+i+'.试验电源端口';
                    editor.getEditor(editorName).disable();
                }
                $("#editor_holder button").addClass("hidden");
                break;

            case '18':
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测定结果评定准则').disable();
                var testPortArray = load_data.试验端口及被试品工作状态;
                for(var i = 0; i < testPortArray.length; i++) {
                    var editorName = 'root.试验端口及被试品工作状态.'+i+'.试验端口';
                    editor.getEditor(editorName).disable();
                }
                $("#editor_holder button").addClass("hidden");
                break;
            case '19':
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                var testPortArray = load_data.试验端口及被试品工作状态;
                for(var i = 0; i < testPortArray.length; i++) {
                    var editorName = 'root.试验端口及被试品工作状态.'+i+'.试验端口';
                    editor.getEditor(editorName).disable();
                }
                $("#editor_holder button").addClass("hidden");
                break;
            case '20':
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                var testPortArray = load_data.试验端口及被试品工作状态;
                for(var i = 0; i < testPortArray.length; i++) {
                    var editorName = 'root.试验端口及被试品工作状态.'+i+'.天线端口';
                    editor.getEditor(editorName).disable();
                }
                $("#editor_holder button").addClass("hidden");
                break;
            case '21':
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                var testPortArray = load_data.试验端口及被试品工作状态;
                for(var i = 0; i < testPortArray.length; i++) {
                    var editorName = 'root.试验端口及被试品工作状态.'+i+'.天线端口';
                    editor.getEditor(editorName).disable();
                }
                $("#editor_holder button").addClass("hidden");
                break;
            case '22':
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                var testPortArray = load_data.试验端口及被试品工作状态;
                for(var i = 0; i < testPortArray.length; i++) {
                    var editorName = 'root.试验端口及被试品工作状态.'+i+'.天线端口';
                    editor.getEditor(editorName).disable();
                }
                $("#editor_holder button").addClass("hidden");
                break;

            case '23':
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                var testPortArray = load_data.试验端口及被试品工作状态;
                for(var i = 0; i < testPortArray.length; i++) {
                    var editorName = 'root.试验端口及被试品工作状态.'+i+'.试验端口';
                    editor.getEditor(editorName).disable();
                }
                $("#editor_holder button").addClass("hidden");
                break;
            case '24':
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                // var testPortArray = load_data.试验位置及被试品工作状态;
                // for(var i = 0; i < testPortArray.length; i++) {
                //     var editorName = 'root.试验位置及被试品工作状态.'+i+'.试验位置';
                //     editor.getEditor(editorName).disable();
                // }
                $("#editor_holder button").addClass("hidden");
                break
            case '25':
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                $("#editor_holder button").addClass("hidden");
                break;
            case '26':
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                var testPortArray1 = load_data.试验端口及被试品工作状态.电源端口;
                var testPortArray2 = load_data.试验端口及被试品工作状态.互联端口;
                for(var i = 0; i < testPortArray1.length; i++) {
                    var editorName1 = 'root.试验端口及被试品工作状态.电源端口.'+i+'.电源端口';
                    editor.getEditor(editorName1).disable();
                }
                for(var i = 0; i < testPortArray2.length; i++) {
                    var editorName2 = 'root.试验端口及被试品工作状态.互联端口.'+i+'.互联端口';
                    editor.getEditor(editorName2).disable();
                }
                $("#editor_holder button").addClass("hidden");
                break;

            case '27':
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                var testPortArray1 = load_data.试验端口及被试品工作状态.电源端口;
                var testPortArray2 = load_data.试验端口及被试品工作状态.互联端口;
                for(var i = 0; i < testPortArray1.length; i++) {
                    var editorName1 = 'root.试验端口及被试品工作状态.电源端口.'+i+'.电源端口';
                    editor.getEditor(editorName1).disable();
                }
                for(var i = 0; i < testPortArray2.length; i++) {
                    var editorName2 = 'root.试验端口及被试品工作状态.互联端口.'+i+'.互联端口';
                    editor.getEditor(editorName2).disable();
                }
                $("#editor_holder button").addClass("hidden");
                break;
            case '28':
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                var testPortArray1 = load_data.试验端口及被试品工作状态.电源端口;
                var testPortArray2 = load_data.试验端口及被试品工作状态.互联端口;
                for(var i = 0; i < testPortArray1.length; i++) {
                    var editorName1 = 'root.试验端口及被试品工作状态.电源端口.'+i+'.电源端口';
                    editor.getEditor(editorName1).disable();
                }
                for(var i = 0; i < testPortArray2.length; i++) {
                    var editorName2 = 'root.试验端口及被试品工作状态.互联端口.'+i+'.互联端口';
                    editor.getEditor(editorName2).disable();
                }
                $("#editor_holder button").addClass("hidden");
                break;
            case '29':
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                $("#editor_holder button").addClass("hidden");
                break;
            case '30':
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                $("#editor_holder button").addClass("hidden");
                break;
            case '31':
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                $("#editor_holder button").addClass("hidden");
                break;
            case '32':
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                $("#editor_holder button").addClass("hidden");
                break;
            case '33':
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                $("#editor_holder button").addClass("hidden");
                break;
            case '34':
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                $("#editor_holder button").addClass("hidden");
                break;
            case '1001':case '1002':case '1003':case '1004':case '1005':
                $("#editor_holder button").addClass("hidden");
                editor.getEditor('root').disable();
                break;
            case '35':
                $("#editor_holder button").addClass("hidden");
                editor.disable();
                break;
            case '58':
                if(Status != 1) {
                    if (outlineStatus == 0 || outlineStatus == 4) {
                        $("#project_submit").removeClass("hidden");
                        console.log("removeClass");
                    }
                }
                break;
            default:
                break;

        }
}


function beforeSubmit() {
    switch (page_number) {
        case '10':
            if(modifyPage10) {
                pageAction = 3;
                modifyPage10 = false;
            }
            break;
        case '14':
            var errors = editor.validate();
            if(errors.length > 0) {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '15':
            var errors = editor.validate();
            if(errors.length > 0) {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '16':
            var errors = editor.validate();
            if(errors.length > 0) {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '17':
            var errors = editor.validate();
            if(errors.length > 0) {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '18':
            var errors = editor.validate();
            if(errors.length > 0) {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '19':
            var errors = editor.validate();
            if(errors.length > 0) {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '20':
            var errors = editor.validate();
            if(errors.length > 0) {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '21':
            var errors = editor.validate();
            if(errors.length > 0) {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '22':
            var errors = editor.validate();
            if(errors.length > 0) {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '23':
            var errors = editor.validate();
            if(errors.length > 0) {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '24':
            var errors = editor.validate();
            if(errors.length > 0) {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '25':
            var errors = editor.validate();
            if(errors.length > 0) {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '26':
            var errors = editor.validate();
            if(errors.length > 0) {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '27':
            var errors = editor.validate();
            if(errors.length > 0) {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '28':
            var errors = editor.validate();
            if(errors.length > 0) {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '29':
            var errors = editor.validate();
            if(errors.length > 0) {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '30':
            var errors = editor.validate();
            if(errors.length > 0) {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '31':
            var errors = editor.validate();
            if(errors.length > 0) {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '32':
            var errors = editor.validate();
            if(errors.length > 0) {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '33':
            var errors = editor.validate();
            if(errors.length > 0) {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '34':
            var errors = editor.validate();
            if(errors.length > 0) {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        default:
            break;
    }

    return true;


}


