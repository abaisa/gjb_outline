/*
抽象出的一些页面函数
 */

JSONEditor.defaults.languages.cn = {
    button_add_row_title: "添加",
    button_delete_last_title: "删除最后一行",
    button_delete_all_title: "删除所有",
    button_delete_row_title_short: "删除",
    button_move_down_title: "下移",
    button_move_up_title: "上移",
    button_delete_row_title: "删除"
};

JSONEditor.defaults.language = "cn";

function changeBtnStatus(location, status) {
    var locStr = "[data-schemapath='" + location + "'] .json-editor-btn-add";
    $(locStr).filter(":contains('添加')")[0].disabled = status;
}

function watchQuantity(location, limitNum) {
    return editor.watch(location, function () {
        var participantNum = editor.getEditor(location).getValue().length;
        changeBtnStatus(location, participantNum >= limitNum)
    });
}

function getSubsysOrEqpHead() {
    $.ajax({
        type: "post",
        url: "dependency/getSubsysOrEqpHead",
        data: {
            outlineId: 1
        },
        success: function (data) {
            test = data;
            var all_data = JSON.parse(data.data);
            var head_data_str = all_data.data;
            var head_schema_str = all_data.schema;
            var head_schema = JSON.parse(head_schema_str);
            var head_data = JSON.parse(head_data_str);

            var editor_head = new JSONEditor(document.getElementById('editor_head'), {
                theme: 'bootstrap3',
                disable_collapse: true, //default:false,remove all collapse buttons from objects and arrays.
                disable_edit_json: true, //default:false,remove all Edit JSON buttons from objects.
                disable_properties: true,  //default:false,remove all Edit Properties buttons from objects.
                required_by_default: true,
                schema: head_schema
            });

            that = head_data;
            editor_head.setValue(head_data);
            editor_head.disable();
            editor_head.getEditor('root.测试边界(m)').enable();


            return true
        },
        error: function (data) {
            return false
        }
    });
}