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
            outlineDevItemId: outlineDevItemId,
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
            outlineDevItemId: outlineDevItemId
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
                outlineDevItemId: outlineDevItemId,
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
                outlineDevItemId: outlineDevItemId,
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
//新建14-34的上传实验图形
function uploadPic3() {
    var fileInput = $('#images3').get(0).files[0];
    if(!fileInput){
        $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '未选择任何图片'});

    } else {
        $.ajaxFileUpload({
            type: "post",
            url: "/outline/page_data/upload",
            fileElementId: "images3",
            data: {
                outlineDevItemId: outlineDevItemId,
                pageNumber: page_number,
            },
            success: function(data){
                console.log(data)
                $.fillTipBox({type: 'success', icon: 'glyphicon-exclamation-sign', content: '上传成功'});


            }

        });
        $("#myModal3").modal("hide");

    }

}
//新建页4的查看分系统/设备照片
function downloadPic(){
    $("#showPic1").empty();
    $.ajax({
        type: "post",
        url: "/outline/page_data/download",
        data: {
            outlineDevItemId: outlineDevItemId,
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
                // var url = "statics/imgs/"+pic1List[i];
                var url = "/image/"+pic1List[i];
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
            outlineDevItemId: outlineDevItemId,
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
                // var url = "statics/imgs/"+pic2List[i];
                var url = "/image/"+pic2List[i];
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

//新建页14-34查看试验图
function downloadPic3(){
    $("#showPic3").empty();
    $.ajax({
        type: "post",
        url: "/outline/page_data/download",
        data: {
            outlineDevItemId: outlineDevItemId,
            currentPageNumber: page_number,
            picNumber: -1,
        },
        success: function(data){
            console.log(data);
            var pic3List = data.data;
            for(var i = 0; i<pic3List.length; i++){
                var  img = document.createElement("img");
                var pictureNumber = document.createElement("span");
                pictureNumber.innerHTML = i+1;
                // var url = "statics/imgs/"+pic3List[i];
                var url = "/image/"+pic3List[i];
                img.src = url;
                img.className="image";
                img.width=500;
                var number = i + 1;

                $("#showPic3").append(img);
                $("#showPic3").append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                $("#showPic3").append("项目试验图" +": " +pic3List[i]);
            }

        }
    });
    downloadText();
}

//获取对应文本文字 根据textNumber
function downloadText() {
    $.ajax({
        type: "post",
        url: "/outline/page_data/loadText",
        data: {
            outlineDevItemId: outlineDevItemId,
            currentPageNumber: page_number,
        },
        success: function (data) {
            console.log(data);
            var textNew1AndTextNew2 = JSON.parse(data.data);
            var textNew1 = textNew1AndTextNew2["修改图形理由"];
            var textNew2 = textNew1AndTextNew2["修改方法"];
            $("#textNew1").val(textNew1);
            $("#textNew2").val(textNew2);
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
                outlineDevItemId: outlineDevItemId,
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
                outlineDevItemId: outlineDevItemId,
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

function submitText() {
    var textNew1 = $("#textNew1").val();
    var textNew2 = $("#textNew2").val();
    $.ajax({
        type: "post",
        url: "/outline/page_data/submitText",
        data: {
            outlineDevItemId: outlineDevItemId,
            PageNumber: page_number,
            textNew1: textNew1,
            textNew2: textNew2,
        },
        success: function (data) {
            console.log(data);
            $.fillTipBox({type: 'success', icon: 'glyphicon-exclamation-sign', content: '提交成功'});
        }
    })

}

function showEchartsPic1() {
    submitPageData(1);
    $.ajax({
        type: "post",
        url: "/outline/page_data/load",
        data: {
            currentPageNumber: 57,
            outlineDevItemId: outlineDevItemId,
            pageAction: 1       // 1 表示下一页，2 表示上一页
        },
        success: function (data) {
            console.log("loadTargetPage ajax 请求成功");
            if (data.status == 'success') {
                var myCharts = echarts.init(document.getElementById('echartsPic1'));
                var projectNameList = [];
                var projectStartTimeList = [];
                var projectEndTimeList = [];
                var all_data = JSON.parse(data.data);
                console.log('all_data'+":"+all_data);
                var load_data = all_data.data;
                console.log('load_data'+load_data);
                var load_dataObj = JSON.parse(load_data);
                var projectListObj = load_dataObj.试验项目;
                for (var i = 0; i < projectListObj.length - 1; i++) {
                    for (var j = 0; j < projectListObj.length - i - 1; j++) {
                        var switchObj;
                        var fStartTime = projectListObj[j].计划起始时间;
                        var sStartTime = projectListObj[j + 1].计划起始时间;
                        var farr = fStartTime.split("-");
                        var sarr = sStartTime.split("-");
                        var fyear = parseInt(farr[0]);
                        var fmonth = parseInt(farr[1]);
                        var fday = parseInt(farr[2]);
                        var syear = parseInt(sarr[0])
                        var smonth = parseInt(sarr[1]);
                        var sday = parseInt(sarr[2]);
                        if (fyear > syear) {
                            switchObj = projectListObj[j];
                            projectListObj[j] = projectListObj[j + 1];
                            projectListObj[j + 1] = switchObj;
                            continue;
                        }
                        if (fyear == syear && fmonth > smonth) {
                            switchObj = projectListObj[j];
                            projectListObj[j] = projectListObj[j + 1];
                            projectListObj[j + 1] = switchObj;
                            continue;
                        }
                        if (fyear == syear && fmonth == smonth && fday > sday) {
                            switchObj = projectListObj[j];
                            projectListObj[j] = projectListObj[j + 1];
                            projectListObj[j + 1] = switchObj;
                            continue;
                        }

                    }
                }
                // var fStartTime = projectListObj[0].计划起始时间;
                // var arr = fStartTime.split("-");
                // console.log("年份"+ " " + arr[0]);
                for (var i = 0; i < projectListObj.length; i++) {
                    var startTime = projectListObj[i].计划起始时间.replace(/-/g, '/');
                    var endTime = projectListObj[i].计划结束时间.replace(/-/g, '/');
                    projectNameList.push(projectListObj[i].试验项目名称);
                    projectStartTimeList.push(new Date(startTime));
                    projectEndTimeList.push(new Date(endTime));


                }
                // projectStartTimeList = [new Date('2019/05/12')];
                // projectEndTimeList = [new Date('2019/05/17')];
                console.log(projectNameList);
                console.log(projectStartTimeList);
                console.log(projectEndTimeList);
                option = {
                    title: {
                        text: '试验实施网络图',
                        left: 10
                    },
                    legend: {
                        data: ['计划实施时间', '实际实施时间']

                    },
                    grid: {
                        containLabel: true,
                        left: 20
                    },
                    xAxis: {
                        type: 'time'
                    },

                    yAxis: [{
                        data: projectNameList
                    }, {
                        position: 'left',
                        offset: 50,
                        type: 'category',
                        axisTick: {
                            lineStyle: {
                                color: '#000'
                            },
                            interval: function(index, value) {
                                return value !== '';
                            }
                        },
                        axisLabel: {
                            // height: 56,
                            // lineHeight:56,
                            color: '#000',
                            // show: 'middle',

                        },
                        // splitLine: {
                        //     interval: function(index, value) {
                        //         console.log(value)
                        //         return value !== '';
                        //     }
                        // },
                        // axisPointer: {
                        //     show: true
                        // },
                        // splitArea: {
                        //     show: true,
                        //     interval: function(index, value) {
                        //         return value !== '';
                        //     }
                        // },

                    }],
                    tooltip: {
                        trigger: 'axis',
                        formatter: function(params) {
                            // console.log('params', params)
                            var res = params[0].name + "</br>"
                            var date0 = params[0].data;
                            var date1 = params[1].data;
                            // var date2 = params[2].data;
                            // var date3 = params[3].data;
                            date0 = date0.getFullYear() + "-" + (date0.getMonth() + 1) + "-" + date0.getDate();
                            date1 = date1.getFullYear() + "-" + (date1.getMonth() + 1) + "-" + date1.getDate();
                            // date2 = date2.getFullYear() + "-" + (date2.getMonth() + 1) + "-" + date2.getDate();
                            // date3 = date3.getFullYear() + "-" + (date3.getMonth() + 1) + "-" + date3.getDate();
                            res += params[0].seriesName + "~" + params[1].seriesName + ":</br>" + date0 + "~" + date1 + "</br>"
                            // res += params[2].seriesName + "~" + params[3].seriesName + ":</br>" + date2 + "~" + date3 + "</br>"
                            return res;
                        }
                    },
                    series: [

                        {
                            name: '计划开始时间',
                            type: 'bar',
                            stack: 'test1',
                            itemStyle: {
                                normal: {
                                    color: 'rgba(0,0,0,0)'
                                }
                            },
                            // data: [
                            //     // new Date("2017/09/15"),
                            //     // new Date("2017/09/15"),
                            //     // new Date("2017/10/03"),
                            //     // new Date("2017/10/04"),
                            //     // new Date("2017/10/05"),
                            //     // new Date("2017/10/06")
                            //     new Date(time1)
                            // ]
                            data: projectStartTimeList
                        },
                        {
                            name: '计划完成时间',
                            type: 'bar',
                            stack: 'test1',
                            // data: [
                            //     // // new Date("2015/09/12"),
                            //     // new Date("2017/09/20"),
                            //     // new Date("2017/09/25"),
                            //     // new Date("2017/10/05"),
                            //     // new Date("2017/10/07"),
                            //     // new Date("2017/10/09"),
                            //     // new Date("2017/10/12")
                            //     new Date(time2)
                            // ]
                            data: projectEndTimeList
                        },
                    ]
                };
                myCharts.setOption(option);

            }

        }
    })


}