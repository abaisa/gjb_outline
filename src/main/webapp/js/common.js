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