/**
 * 1.生成一个json editor页面，并保存
 * 2.恢复这个页面
 */
// json editor编辑器
// var editor = new JSONEditor(document.getElementById('editor_holder'), {
//         theme: 'bootstrap3',
//         disable_collapse: true, //default:false,remove all collapse buttons from objects and arrays.
//         disable_edit_json: true, //default:false,remove all Edit JSON buttons from objects.
//         disable_properties: true,  //default:false,remove all Edit Properties buttons from objects.
//         schema: {
//             "type": "array",
//             "title": "DEMO",
//             "items": {
//                 "type": "object",
//                 "title": "Child",
//                 "headerTemplate": "{{ i1 }} - {{ self.name }} (age {{ self.age }})",
//                 "properties": {
//                     "name": {"type": "string"},
//                     "age": {"type": "integer"}
//                 }
//             }
//
//         }
//     });

// document.getElementById('submit').addEventListener('click', turnPage);
//
// function turnPage(){
//     submitPageData();
//     loadTargetPage();
// }

// 控制翻页事件，提交本页数据，加载目标页 两个函数都是ajax请求
submitSuccess = false;

function turnPage(action) {
    pageAction = action;
    var isSubmit = beforeSubmit();
    if(isSubmit) {
        submitPageData(pageAction);
    }
    if (submitSuccess) {
        submitSuccess = false;
        pageAction = action;
        loadTargetPage(pageAction)
    }
}

function submitPageData(action) {
    console.log("call submitPageData");
    console.log(JSON.stringify(editor.getValue()));
    console.log("page_number:"+page_number);
    console.log("pageAction:"+action);
    $.ajax({
        type: "post",
        url: "/outline/page_data/submit",
        async: false, // 获取下一页页码在后端进行，避免数据不完整，提交试用同步方式
        data: {
            outlineID: 1,
            pageNumber: page_number,
            pageAction: action,       // 1 表示下一页，2 表示上一页，3表示其他依赖页触发的提交
            jsonData: JSON.stringify(editor.getValue())
        },
        success: function (data) {
            console.log("submitPageData ajax 请求成功");
            console.log(data);
            submitSuccess = true;
        },
        error: function (data) {
            console.log("ajax 请求失败");
            console.log(data);
            alert("internal error");
            submitSuccess = false;
        }
    });
}

function loadTargetPage(action) {
    console.log("call loadTargetPage");
    $.ajax({
        type: "post",
        url: "/outline/page_data/load",
        data: {
            currentPageNumber: page_number,
            outlineID: 1,
            pageAction: action       // 1 表示下一页，2 表示上一页
        },

        success: function (data) {
            console.log("loadTargetPage ajax 请求成功");
            if (data.status == "success") {
                console.log("data:"+data.data);
                var all_data = JSON.parse(data.data);

                load_data = all_data.data;
                load_schema = all_data.schema;
                var load_page_id = all_data.page_id;
                console.log("未json处理化的load_data"+load_data);
                load_schema = JSON.parse(load_schema);
                load_data = JSON.parse(load_data.replace(/\n/g,"\\\\n").replace(/\t/g,"\\\\t"));
                console.log("loadTargetPage data >>");
                console.log(load_schema);
                console.log(load_data);
                console.log(load_page_id);

                page_number = load_page_id;

                document.getElementById('editor_holder').innerHTML = "";
                editor = new JSONEditor(document.getElementById('editor_holder'), {
                    theme: 'bootstrap3',
                    disable_collapse: true,
                    disable_edit_json: true,
                    disable_properties: true,
                    required_by_default: true,
                    disable_array_reorder: true,
                    show_errors: true,
                    schema: load_schema
                });

                if (load_data != null && load_data.length != 0 && !$.isEmptyObject(load_data)) {
                    editor.setValue(load_data);
                }

                //设置当前页的监听事件
                monitor();
            } else {
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: data.message});
            }


            return true
        },
        error: function (data) {
            console.log("loadTargetPage ajax 请求失败");
            console.log(data);
            return false
        }
    });
}

// todo 如果要做页码跳转，请再独立写一个ajax方法，再写一个