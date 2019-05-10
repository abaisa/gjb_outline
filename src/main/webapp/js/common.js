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
    button_delete_row_title: "删除",
    error_notempty: "若无请填无"
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

function disableAddAndDelete(location, status) {
    var locStr = "[data-schemapath='" + location + "'] .json-editor-btn-add";
    $(locStr).filter(":contains('添加')")[0].disabled = status;
    locStr = "[data-schemapath='" + location + "'] .json-editor-btn-delete";
    var deleteButton = $(locStr).filter(":contains('删除')");
    for (var i = 0; i < deleteButton.length; i++) {
        deleteButton[i].disabled = true
    }
}

function submitSubsysOrEqpHead(subsysOrEqpData) {
    $.ajax({
        type: "post",
        url: "dependency/submitSubsysOrEqpHead",
        data: {
            outlineId: outlineId,
            subsysOrEqpData: JSON.stringify(subsysOrEqpData)
        },
        success: function (data) {
            if (data.status == 'error') {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: data.message});
            }

        },
        error: function (data) {
            return false
        }
    });
}

function getSubsysOrEqpHead() {
    $.ajax({
        type: "post",
        url: "dependency/getSubsysOrEqpHead",
        data: {
            outlineId: outlineId
        },
        success: function (data) {
            test = data;
            if (data.status == 'success') {
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
                submitSubsysOrEqpHead(editor_head.getValue());

                editor_head.watch('root.测试边界(m)', function () {
                    var subsysOrEqpData = editor_head.getValue();
                    var boundaryVal = editor_head.getEditor('root.测试边界(m)').getValue();
                    subsysOrEqpData['测试边界(m)'] = boundaryVal;
                    submitSubsysOrEqpHead(subsysOrEqpData);
                });

                return true
            } else {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: data.message});
            }

        },
        error: function (data) {
            return false
        }
    });

}
//新建页4的上传分系统/设备照片
function uploadPic() {
    var fileInput = $('#images').get(0).files[0];
    if(!fileInput){
        $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '未选择任何图片'});

    } else {
        $.ajaxFileUpload({
            type: "post",
            url: "/outline/page_data/upload",
            fileElementId: "images",
            data: {
                outlineID: outlineId,
                pageNumber: 4,
                picNumber: 1,
            },
            success: function(data){
                console.log(data)
                $.fillTipBox({type: 'success', icon: 'glyphicon-exclamation-sign', content: '上传成功'});


            }

        })

    }

}
//新建页4的上传分系统/设备关系图
function uploadPic2() {
    var fileInput = $('#images2').get(0).files[0];
    if(!fileInput){
        $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '未选择任何图片'});

    } else {
        $.ajaxFileUpload({
            type: "post",
            url: "/outline/page_data/upload",
            fileElementId: "images2",
            data: {
                outlineID: outlineId,
                pageNumber: 4,
                picNumber: 2,
            },
            success: function(data){
                console.log(data)
                $.fillTipBox({type: 'success', icon: 'glyphicon-exclamation-sign', content: '上传成功'});


            }

        })

    }

}
//新建页4的查看分系统/设备照片
function downloadPic(){
    $("#showPic1").empty();
    $.ajax({
        type: "post",
        url: "/outline/page_data/download",
        data: {
            outlineID: outlineId,
            currentPageNumber: 4,
            picNumber: 1,
        },
        success: function(data){
            console.log(data);
            var pic1List = data.data;
            for(var i = 0; i<pic1List.length; i++){
                var img = document.createElement("img");
                var pictureNumber = document.createElement("span");
                pictureNumber.innerHTML = i+1;
                var url = "statics/imgs/"+pic1List[i];
                // var url = "/image/"+pic1List[i];
                img.src = url;
                img.className="image";
                img.width=500;
                var number = i + 1;

                $("#showPic1").append(img);
                $("#showPic1").append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                $("#showPic1").append("分系统/设备照片"+number+": "+pic1List[i]);
            }

        }
    })
}
//新建页4的查看分系统/设备关系图
function downloadPic2(){
    $("#showPic2").empty();
    $.ajax({
        type: "post",
        url: "/outline/page_data/download",
        data: {
            outlineID: outlineId,
            currentPageNumber: 4,
            picNumber: 2,
        },
        success: function(data){
            console.log(data);
            var pic2List = data.data;
            for(var i = 0; i<pic2List.length; i++){
                var  img = document.createElement("img");
                var pictureNumber = document.createElement("span");
                pictureNumber.innerHTML = i+1;
                var url = "statics/imgs/"+pic2List[i];
                // var url = "/image/"+pic2List[i];
                img.src = url;
                img.className="image";
                img.width=500;
                var number = i + 1;

                $("#showPic2").append(img);
                $("#showPic2").append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                $("#showPic2").append("分系统/设备关系图"+number+": "+pic2List[i]);
            }

        }
    })
}
//清空新建页4上传的分系统/设备图片
function deletePic1() {
    var myMessage = confirm("确定要清空上传的分系统/设备图片吗？");
    if(myMessage == true){
        $("#showPic1").empty();
        $.ajax({
            type: "post",
            url: "/outline/page_data/deletePic",
            data: {
                outlineID: outlineId,
                PageNumber: 4,
                picNumber: 1,
            },
            success: function (data) {
                console.log(data);
                $.fillTipBox({type: 'success', icon: 'glyphicon-exclamation-sign', content: '清空图片成功'});


            }
        })

    }


}
//清空新建页4上传的分系统/设备关系图
function deletePic2() {
    var myMessage = confirm("确定要清空上传的分系统/设备关系图吗？");
    if(myMessage == true){
        $("#showPic2").empty();
        $.ajax({
            type: "post",
            url: "/outline/page_data/deletePic",
            data: {
                outlineID: outlineId,
                PageNumber: 4,
                picNumber: 2,
            },
            success: function (data) {
                console.log(data);
                $.fillTipBox({type: 'success', icon: 'glyphicon-exclamation-sign', content: '清空图片成功'});


            }
        })

    }


}