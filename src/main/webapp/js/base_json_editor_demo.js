/**
 * 1.生成一个json editor页面，并保存
 * 2.恢复这个页面
 */

// 页码
var page_number = 1;

// json editor编辑器
var editor = new JSONEditor(document.getElementById('editor_holder'), {
        theme: 'bootstrap3',
        disable_collapse: true, //default:false,remove all collapse buttons from objects and arrays.
        disable_edit_json: true, //default:false,remove all Edit JSON buttons from objects.
        disable_properties: true,  //default:false,remove all Edit Properties buttons from objects.
        schema: {
            "type": "array",
            "title": "Children",
            "items": {
                "type": "object",
                "title": "Child",
                "headerTemplate": "{{ i1 }} - {{ self.name }} (age {{ self.age }})",
                "properties": {
                    "name": {"type": "string"},
                    "age": {"type": "integer"}
                }
            }

        }
    });

document.getElementById('submit').addEventListener('click', turnPage);

// 控制翻页事件，提交本页数据，加载目标页
function turnPage(){
    submitPageData();
    loadTargetPage();
}

function submitPageData() {
    $.ajax({
        type: "post",
        url: "/outline/page_data/submit",
        data: {
            outlineID: 711,
            pageNumber: page_number,
            jsonData: JSON.stringify(editor.getValue())
        },
        success: function (data) {
            console.log("submitPageData ajax 请求成功");
            console.log(data);
            return true
        },
        error: function (data) {
            console.log("ajax 请求失败");
            console.log(data);
            alert("internal error");
            return false
        }
    });
}

function loadTargetPage() {
    console.log("call loadTargetPage");
    $.ajax({
        type: "post",
        url: "/outline/page_data/load",
        data: {
            currentPageNumber: page_number,
            outlineID: 711,
            pageAction: 1       // 1 表示下一页，2 表示上一页
        },
        success: function (data) {
            console.log("loadTargetPage ajax 请求成功");
            console.log(data);
            var load_data = JSON.parse(data.data).data;
            var load_schema = JSON.parse(data.data).schema;
            load_schema = JSON.parse(load_schema);
            load_data = JSON.parse(load_data);

            console.log(load_schema);
            console.log(load_data);

            document.getElementById('editor_holder').innerHTML = "";
            editor = new JSONEditor(document.getElementById('editor_holder'), {
                theme: 'bootstrap3',
                disable_collapse: true,
                disable_edit_json: true,
                disable_properties: true,
                schema: load_schema
            });
            editor.setValue(load_data);
            return true
        },
        error: function (data) {
            console.log("loadTargetPage ajax 请求失败");
            console.log(data);
            return false
        }
    });
}

// todo 如果要做页码跳转，请再独立写一个ajax方法，再写一个a