<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()%>/"/>
    <link rel="stylesheet" href="repack/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="repack/css/front.css">
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="statics/css/develop/new/new.css">
    <meta charset="utf-8"/>
    <title>试验大纲基础页面</title>
</head>
<body>
<div class="front-inner">
    <s:include value="../_nav.jsp?">
        <s:param name="userName">userName</s:param>
        <s:param name="act">${param.outlineStatus}</s:param>
    </s:include>
    <div class="container">
        <div id="editor_head" class="hidden"></div>
        <%--核心div--%>
        <div id='editor_holder'></div>
        <%--<div id="upload">--%>
            <%--&lt;%&ndash;<form id="uploadForm1" enctype="multipart/form-data" class="form-control">&ndash;%&gt;--%>
                <%--&lt;%&ndash;<input  type="file" name="images" id="images">&ndash;%&gt;--%>
            <%--&lt;%&ndash;</form>&ndash;%&gt;--%>
                <%--<input type="file" id="images" name="images">--%>
            <%--<button class="btn-primary" id="uploadpic1" onclick="uploadPic1()">上传分系统/设备照片</button>--%>
            <%--<button class="btn-primary" id="dowloadpic1" onclick="downloadPic1()">查看分系统/设备照片</button>--%>
        <%--</div>--%>
        <div id="page_3">
        <div id="upload1" style="display: none">
                <input type="file" id="images" name="images" class="input-lg">
                <button class="btn-primary" id="uploadpic1" onclick="uploadPic()">上传分系统/设备照片</button>
                <button data-toggle="modal" data-target="#myModal1"  class="btn-primary" onclick="downloadPic()">查看分系统/设备照片</button>
                <input name="button" type="button"  class="btn-primary" value="清空图片" onclick="deletePic1()"></button>
        </div>
        <div class="modal fade" id="myModal1" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="myModalLabel">分系统/设备照片</h4>
                    </div>
                    <div class="modal-body" id="showPic1">

                    </div>
                    <div class="modal-footer">
                        <a href="javascript:void(0)" class="btn btn-default" data-dismiss="modal">关闭</a>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
        </div>
        <br><br>
        <div id="upload2" style="display: none">
            <input type="file" id="images2" name="images" class="input-lg">
            <button class="btn-primary" id="uploadpic2" onclick="uploadPic2()">上传分系统/设备关系图</button>
            <button data-toggle="modal" data-target="#myModal2"  class="btn-primary" onclick="downloadPic2()">查看分系统/设备关系图</button>
            <input name="button" type="button"  class="btn-primary" value="清空图片" onclick="deletePic2()"></button>
        </div>
        <div class="modal fade" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="myModalLabel2">分系统/设备关系图</h4>
                    </div>
                    <div class="modal-body" id="showPic2">

                    </div>
                    <div class="modal-footer">
                        <a href="javascript:void(0)" class="btn btn-default" data-dismiss="modal">关闭</a>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
        </div>
        </div>
        <div id="page_14-34">
            <div id="upload3" style="display: none">
                <%--<input type="file" id="images3" name="images" class="input-lg">--%>
                <%--<button class="btn-primary" id="uploadpic3" onclick="uploadPic3()">上传试验图</button>--%>
                <button data-toggle="modal" data-target="#myModal3"  class="btn-primary" onclick="downloadPic3()">修改图形</button>
                    <button data-toggle="modal" data-target="#myModal4"  class="btn-primary" onclick="downloadPic3()">修改方法</button>

            </div>
        </div>
        <div class="modal fade" id="myModal3" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="myModalLabel3">项目试验图</h4>
                    </div>
                    <br><br>
                    <div class="row" style="margin: 20px; width: 95%" id="showPic3">

                    </div>
                    <input type="file" id="images3" name="images" class="input-lg">
                    <button class="btn-primary" id="uploadpic3" onclick="uploadPic3()">上传试验图</button>
                    <br><br>
                    <div class="row" style="margin:20px; width: 95%" id="changeText1" name="changeText">
                        <p style="font-weight: bold">修改图形理由</p>
                        <textarea id="textNew1" class="form-control radius" rows="5" style="width: 100%" name="textNew"></textarea><br>
                        <br><br><br><br><br><br>
                    </div>
                    <%--<div class="row" style="margin:20px; width: 95%" id="changeText2" name="changeText">--%>
                        <%--<p style="font-weight: bold">修改方法</p>--%>
                        <%--<textarea id="textNew2" class="form-control radius" rows="5" style="width: 100%" name="textNew"></textarea><br>--%>
                        <%--<br><br><br><br><br><br>--%>
                    <%--</div>--%>
                    <div class="modal-footer">
                        <button class="btn btn-primary" onclick="submitText()">确认提交</button>
                        <a href="javascript:void(0)" class="btn btn-default" data-dismiss="modal">关闭</a>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
        </div>
        <div class="modal fade" id="myModal4" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="myModalLabel4">试验方法</h4>
                    </div>
                    <div class="modal-body" id="showTextNew2">
                        <div class="row" style="margin:20px; width: 95%" id="changeText2" name="changeText">
                            <p style="font-weight: bold">修改方法</p>
                            <textarea id="textNew2" class="form-control radius" rows="5" style="width: 100%" name="textNew"></textarea><br>
                            <br><br><br><br><br><br>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-primary" onclick="submitText()">确认提交</button>
                        <a href="javascript:void(0)" class="btn btn-default" data-dismiss="modal">关闭</a>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
        </div>
        <button class="btn btn-primary" id="showEchartsPic1" onclick="showEchartsPic1()">查看试验实施网络图</button>
        <div id="echartsPic1" style="height: 600px; width: 600px" class="hidden"></div>
        <div class="text-right" id="page_button">
            <button class="btn btn-primary" id="pre_page" onclick="turnPage(2)">上一页</button>
            <button class="btn btn-primary" id="next_page" onclick="turnPage(1)">下一页</button>
        </div>
        <br>
        <div class="text-right hidden" id="project_submit" >
            <button type="button" class="btn btn-default" onclick="cancelProofread('${session.userLogin.userName}')">取消</button>
            |
            <button class="btn btn-primary" id="save" onclick="passResult(0, 2, '${session.userLogin.userName}')">保存草稿</button>
            <button class="btn btn-primary" id="submit" onclick="passResult(0, 1, '${session.userLogin.userName}')">提交报告</button>
        </div>

        <br><br><br>
        <%--<button id='submit' class="btn btn-primary">Submit</button>--%>
        <div class="panel panel-default front-panel">
            <div class="panel-heading" style="text-align: center;">
                <span class="panel-title">历史校对意见</span>
            </div>
            <div class="panel-body">
                <table class="table" id="advice_history_proofread">
                </table>
            </div>
            <div class="panel-heading" style="text-align: center;">
                <span class="panel-title">历史审核意见</span>
            </div>
            <div class="panel-body">
                <table class="table" id="advice_history_audit">
                </table>
            </div>
            <div class="panel-heading" style="text-align: center;">
                <span class="panel-title">历史批准意见</span>
            </div>
            <div class="panel-body">
                <table class="table" id="advice_history_authorize">
                </table>
            </div>
            <div class="panel-heading" style="text-align: center;" id="advice_title_div">
                <span class="panel-title" id = "advice_title">意见</span>
            </div>
            <div class="panel-body" id="advice_div" style="overflow-y: scroll">
                <textarea id="advice" type="text" class="form-control radius" placeholder='输入意见' style="height: 100px"></textarea>
                <br>
                <br>
                <form class="form-horizontal">
                    <div class="col-lg-offset-8 col-lg-4 text-right" style="padding-right: 12px;">
                        <button type="button" class="btn btn-default" onclick="cancelProofread('${session.userLogin.userName}')">取消</button>
                        |
                        <button type="button" id = "fail" class="btn btn-primary" onclick="passResult(${param.outlineStatus}, 2, '${session.userLogin.userName}')">不通过</button>
                        <button type="button" id = "pass" class="btn btn-primary" onclick="passResult(${param.outlineStatus}, 1, '${session.userLogin.userName}')">通过</button>
                    </div>
                </form>
            </div>
        </div>
        <br><br><br>

    </div>


    <s:include value="../../_footer.jsp"/>

</div>

<%--通用js文件--%>
<script type="text/javascript" src="repack/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="repack/js/plugin/front.js"></script>
<script type="text/javascript" src="repack/jsoneditor/jsoneditor.js"></script>
<script type="text/javascript" src="js/develop/manage.js"></script>
<script type="text/javascript" src="js/develop/new.js"></script>
<script type="text/javascript" src="repack/js/jquery/ajaxfileupload.js"></script>
<script type="text/javascript" src="repack/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="repack/js/echarts/echarts.min.js"></script>

<%--页面方法--%>
<script type="text/javascript" src="js/base_json_editor.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/monitor.js"></script>
<script>
    outlineId = -1;
    outlineStatus = -1;
    outlineName = -1;
    Status = 0;
    page_number = -1;
    //获取url传递的参数
    var params = getParams();
    outlineName = params['outlineName'];
    outlineId = params['outlineId'];
    outlineStatus = params['outlineStatus'];
    Status = params['Status'];


    //调用loadTargetPage(1)，后台设置了2的下一页为3，初始化此页面为第三页
    $.ajax({
        type: "post",
        url: "/outline/page_data/getCurrentPageNumber",
        data: {
            outlineID: outlineId,
        },
        success: function(data){
            console.log(data);
            page_number = data.data;
            console.log('page_number'+' '+page_number);
            loadTargetPage(1);
        }

    }),







    // 页码初始化为2
    // page_number = 2;



    //第十页是否进行修改
    modifyPage10 = false;

    //第九页是否进行修改
    modifyPage9_launch = false;   //9页的发射参数
    modifyPage9_sensitive = false;  //9页的敏感参数

    //判断更改页的位置
    changeLocation = 0; //0代表没有位置更改，1代表从上至下第一个位置更改，以此类推


    // 填充页面意见框标题等内容
    adviceTitle(outlineStatus,Status);
    monitor();






    //加载分系统或设备的头部内容
    getSubsysOrEqpHead();










</script>
</body>
</html>
