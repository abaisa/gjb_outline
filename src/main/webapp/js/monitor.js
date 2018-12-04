/*
页面中需要实现的一些监听函数
 */

function monitor() {
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
                break;
            case '11':
                watchQuantity('root.敏感度判据及检测方法', 10);
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
                disableAddAndDelete('root.试验端口及被试品工作状态', true);

                break;
            case '1001':
                $("#editor_holder button").addClass("hidden");
                editor.getEditor('root').disable();
                break;
            case '35':
                $("#editor_holder button").addClass("hidden");
                editor.disable();
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
        default:
            break;
    }

    return true;


}