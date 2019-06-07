<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<head>
    <base href="<%=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()%>/"/>
    <!-- 可直接使用框架提供的在线CSS文件 -->
    <!-- 若要使用本地的文件，请注释掉下面两行标记 -->
    <!--<link rel="stylesheet" href="http://newfront.free4inno.com/bootstrap/css/bootstrap.min.css">-->
    <!--<link rel="stylesheet" href="http://newfront.free4inno.com/css/front.css">-->

    <!-- 也可以使用下载到本地的CSS文件，请去掉下面两行标记的注释。其中localpath为本地路径。 -->
    <!-- 若要使用在线文件，请注释掉下面两行标记 -->
    <link rel="stylesheet" href="repack/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="repack/css/front.css">
    <link rel="stylesheet" href="statics/css/develop/new/new.css">
</head>
<body class="front-body"  onload="findAllItem();">
<div class="front-inner">
    <s:include value="_nav_admin.jsp">
    <s:param name="act">item</s:param>
    </s:include>
    <div class="container">
        <div style="margin-bottom:40px;">
            <a data-toggle="modal" data-target="#myModal"  class="pull-right btn btn-primary" onclick="findUserAndItem()">新建</a>
        </div>
        <div class="panel panel-default front-panel">
            <div class="panel-body front-no-padding">
                <table id="itemTable"
                       class="table table-striped" style="margin-bottom: 0px; text-align: center">
                </table>
            </div>
        </div>
    </div>
        <%--导入外部项目--%>
    <div id="importItem" align="right">
        <input type="file" id="sqlTxt" name="sqlTxt" class="input-lg">
        <button class="btn-primary" id="upload" onclick="importItem()">上传外部项目</button>
    </div>
    <s:include value="../../_footer.jsp"/>
</div>


<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">新建项目</h4>
            </div>
            <div class="modal-body" >
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-lg-3 col-md-2 control-label">项目名称</label>
                        <div class="col-lg-9 col-md-10">
                            <%--<input id="itemName" class="form-control" type="text" onkeyup="this.value=this.value.replace(/(^\s+)|(\s+$)/g,'');" placeholder="必填。"/>--%>
                            <select class="form-control" id="itemName">
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 col-md-2 control-label">编制人员</label>
                        <div class="col-lg-9 col-md-10" >
                            <select class="form-control" id="user_new">
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 col-md-2 control-label">校对人员</label>
                        <div class="col-lg-9 col-md-10">
                            <select class="form-control" id="user_proofread">
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 col-md-2 control-label">审核人员</label>
                        <div class="col-lg-9 col-md-10">
                            <select class="form-control" id="user_audit">
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 col-md-2 control-label">批准人员</label>
                        <div class="col-lg-9 col-md-10">
                            <select class="form-control" id="user_authorize">
                            </select>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <a href="javascript:void(0)" class="btn btn-default" data-dismiss="modal">取消</a>
                <a href="javascript:void(0)" class="btn btn-primary" onclick="addItem()">确定</a>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

<div class="panel panel-default front-panel">
    <div class="panel-body front-no-padding">
        <div class="userList">
            <table id="informationTable"
                   class="table table-striped" style="margin-bottom: 0px; text-align: center">
            </table>
        </div>
    </div>
</div>
<!-- 可直接使用框架提供的在线js文件 -->
<!-- 若要使用本地的文件，请注释掉下面三行标记 -->
<script type="text/javascript" src="repack/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="repack/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="repack/js/plugin/front.js"></script>
<script type="text/javascript" src="js/develop/admin.js"></script>
<script type="text/javascript" src="repack/js/jquery/ajaxfileupload.js"></script>
<script>
    function findUserAndItem(){
        $('#user_new').empty();//先清空避免重复查询，此段也可省略
        $('#user_proofread').empty();
        $('#user_audit').empty();
        $('#user_authorize').empty();
        $('#itemName').empty();
    $.ajax( {
        url : "admin/findAllUser",
        type : 'post',
        success : function(data) {
            var userList = data.data;
            for(var i = 0;i<userList.length;i++){
                if(userList[i].userLevel==3){
                    $('#user_new').append('<option>'+userList[i].userName+'</option>');
                    $('#user_proofread').append('<option>'+userList[i].userName+'</option>');
                }
                if(userList[i].userLevel==2){
                    $('#user_audit').append('<option>'+userList[i].userName+'</option>');
                    $('#user_authorize').append('<option>'+userList[i].userName+'</option>');
                }
            }
        }
    });
        $.ajax({
            url: "admin/getAllItem",
            type: 'post',
            success : function (data) {
                console.log(data);
                var itemList  =  data.data;
                for(var i = 0;i<itemList.length;i++){
                    $('#itemName').append('<option>'+itemList[i].outlineName+'</option>');

                }

            }

        });





}
    function importItem() {
        var fileInput = $('#sqlTxt').get(0).files[0];
        if(!fileInput){
            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '未选择任何图片'});

        } else {
            $.ajaxFileUpload({
                type: "post",
                url: "/outline/admin/importItem",
                fileElementId: "sqlTxt",
                dataType: "json",
                success: function(data){
                    console.log(data);
                    if (data.status === 'success') {
                        $.tipModal('confirm', 'info', data.message, function (result) {
                        })
                    } else {
                        $.tipModal('confirm', 'info', data.message, function (result) {
                        })
                    }

                },
                error: function(data){
                    console.log(data);
                    $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: "系统繁忙"});
                }

            })

        }
    }
</script>
</body>
</html>

