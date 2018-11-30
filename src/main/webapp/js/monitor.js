/*
页面中需要实现的一些监听函数
 */

function monitor() {
    if (page_number == '3') { //第三页隐藏上一页的按钮
        $("#pre_page").addClass("hidden");
    } else {  //其他页显示上一页的按钮
        $("#pre_page").removeClass("hidden");
    }

    if (page_number == 5) {  //显示分系统即设备信息
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
        case '10':
            var load_properties = load_schema.properties;
            if ($.isEmptyObject(load_properties)) {
                loadTargetPage(1);
            } else {
                watchQuantity('root.电源端口', 10);
                watchQuantity('root.互联端口', 20);
            }
            break;
        case '1001':
            editor.disable();
            break;
        default:
            break;
    }
}

function beforeSubmit() {
    switch (page_number) {
        case '14':
            var errors = editor.validate();
            if(errors.length > 0) {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空'});
                return false;
            }
            break;
        default:
            break;
    }

    return true;
}