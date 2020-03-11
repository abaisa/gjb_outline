/*
页面中需要实现的一些监听函数
 */

function monitor() {
   if(outlineStatus == 1  ||  outlineStatus == 2  ||  outlineStatus == 3  ||  Status == 1){
       editor.disable();
   }
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
    if (page_number >= 14 && page_number <= 34) {
        $("#upload3").show();
        $("div[data-schemapath='root.项目试验图']").hide();
        $("div[data-schemapath='root.修改图形理由']").hide();
        $("div[data-schemapath='root.修改方法']").hide();
    }
    if (page_number > 34) {
        $("#upload3").hide();
    }
    if (page_number != 58) {
        $("#showEchartsPic1").addClass("hidden");
    } else {
        $("#showEchartsPic1").removeClass("hidden");
    }

        switch (page_number) {
            case '3':
                $("#upload1").hide();
                $("#upload2").hide();
                watchQuantity('root.参编单位', 5);
                break;
            case '4':
                $("#upload1").show();
                $("#upload2").show();
                editor.getEditor('root.任务名称').disable();
                editor.getEditor('root.分系统/设备').disable();
                editor.getEditor('root.分系统/设备名称').disable();
                editor.getEditor('root.型号').disable();
                editor.getEditor('root.串号').disable();
                editor.getEditor('root.承制单位').disable();
                editor.getEditor('root.预定使用平台').disable();
                $("div[data-schemapath='root.分系统/设备照片']").hide();
                $("div[data-schemapath='root.分系统/设备关系图']").hide();
                break;
            case '5':
                $("#upload1").hide();
                $("#upload2").hide();

                editor.disable();
                break;
            case '6':
                equipmentListNum = 0;
                configurationListNum = 0;
                $("input").removeAttr("style");
                $("table").removeAttr("style");
                $("input[name*='序号']").width(20);

                // $("input[name*='设备名称']").width(200);
                // $("input[name*='型号']").removeAttr("style");
                // console.log("设备名称长度："+$("input[name*='设备名称']").width());
                // $("input[name*='设备名称']").css({"width":"100px" });

                // $("input[name*='连接电缆ID']").width(50);
                // $("input[name*='名称']").width(20);
                // $("input[name*='型号']").width(20);
                // $("input[name*='生产厂']").width(50);
                // $("input[name*='长度']").width(50);
                // $("input[name*='连接端口ID']").width(50);
                // $("input[name*='备注']").width(50);
                $("div[data-schemapath*='root.分系统主要设备清单'] button:contains('添加')").on('click',
                    function () {
                        editor.getEditor('root.分系统主要设备清单.' + equipmentListNum + '.序号').setValue(equipmentListNum+1);
                        equipmentListNum++;
                    });
                $("div[data-schemapath*='root.分系统主要设备清单'] button:contains('最后一行')").on('click',
                    function () {
                        equipmentListNum--;
                    });
                $("div[data-schemapath*='root.分系统主要设备清单'] button:contains('所有')").on('click',
                    function () {
                        equipmentListNum = 0;
                    });
                $("div[data-schemapath*='root.产品软件配置清单'] button:contains('添加')").on('click',
                    function () {
                        editor.getEditor('root.产品软件配置清单.' + configurationListNum + '.序号').setValue(configurationListNum+1);
                        configurationListNum++;
                    });
                $("div[data-schemapath*='root.产品软件配置清单'] button:contains('最后一行')").on('click',
                    function () {
                        configurationListNum--;
                    });
                $("div[data-schemapath*='root.产品软件配置清单'] button:contains('所有')").on('click',
                    function () {
                        configurationListNum = 0;
                    });
                $("#editor_holder button").click(function() {
                    $("input[name*='序号']").width(20);
                    $("input[name*='连接电缆ID']").width(50);
                    $("input[name*='名称']").width(50);
                    $("input[name*='型号']").width(20);
                    $("input[name*='生产厂']").width(70);
                    $("input[name*='长度']").width(20);
                    $("input[name*='连接端口ID']").width(50);
                    $("input[name*='是否屏蔽']").width(20);
                    $("input[name*='是否双绞']").width(20);
                    $("input[name*='接地方式']").width(20);
                    $("input[name*='是否摸实']").width(20);
                    $("input[name*='备注']").width(50);

                });


            case '7':
                accompanyEquipmentListNum = 0;
                watchQuantity('root.陪试设备清单', 20);
                $("div[data-schemapath*='root.陪试设备清单'] button:contains('添加')").on('click',
                    function () {
                        editor.getEditor('root.陪试设备清单.' + accompanyEquipmentListNum + '.序号').setValue(accompanyEquipmentListNum+1);
                        accompanyEquipmentListNum++;
                    });
                $("div[data-schemapath*='root.陪试设备清单'] button:contains('最后一行')").on('click',
                    function () {
                        accompanyEquipmentListNum--;
                    });
                $("div[data-schemapath*='root.陪试设备清单'] button:contains('所有')").on('click',
                    function () {
                        accompanyEquipmentListNum = 0;
                    });
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
                editor.watch('root.发射测试工作状态', function(){
                    modifyPage9_launch = true;
                });
                editor.watch('root.敏感度测试工作状态', function(){
                    modifyPage9_sensitive = true;
                });
                break;
            case '10':
                var load_properties = load_schema.properties;
                if($.isEmptyObject(load_properties)) {
                    loadTargetPage(1);
                }else {
                    watchQuantity('root.电源端口', 10);
                    watchQuantity('root.互联端口', 20);
                }
                // editor.watch('root.电源端口', function () {
                    modifyPage10 = true;
                // });
                // editor.watch('root.互联端口', function () {
                    modifyPage10 = true;
                // });
                break;
            case '11':
                watchQuantity('root.敏感度判据及检测方法', 10);
                var errors = editor.validate();
                if(errors.length) {
                    console.log(errors);
                }
                else {
                    console.log("valid");
                }
                break;
            case '12':
                $("#editor_holder button").addClass("hidden");
                if(editor.getEditor('root.发射测试参数')) {
                    for (var i = 0; i < 6; i++) {
                        editor.getEditor('root.发射测试参数.' + i + '.频率范围').disable();
                        editor.getEditor('root.发射测试参数.' + i + '.6dB带宽(kHz)').disable();
                        editor.getEditor('root.发射测试参数.' + i + '.驻留时间(s)').disable();
                        editor.getEditor('root.发射测试参数.' + i + '.最小测量时间(模拟式测量接收机)').disable();
                    }
                }
                $("input[name*='视频带宽']").addClass("hidden");
                $("th:contains('视频带宽')").text("");
                // editor.getEditor('root.发射测试参数.0.频率范围').disable();
                // $("table button").addClass("hidden");
                break;
            case '13':
                $("#upload3").hide();
                $("#editor_holder button").addClass("hidden");
                editor.getEditor('root').disable();
                if(outlineStatus == 0 || outlineStatus == 4) {
                    var stayTime = load_data.敏感度测试参数;
                    for (var i = 0; i < stayTime.length; i++) {
                        var editorName = 'root.敏感度测试参数.' + i + '.驻留时间';
                        editor.getEditor(editorName).enable();
                    }
                }
                break;
            case '14':
                // $("select[name*='是否实施']").val("否");
                // $("input[name='root[试验端口及被试品工作状态][0][工作状态][工作状态1][不实施理由]']").val('hhhh');
                // var selectList = $("select[name*='是否实施']").children('option:selected').val();
                // console.log("selectList:"+selectList);
                page14 = false;

                $("select[name*='是否实施']").on('change',
                    function () {
                        var selectName = $(this).children('option:selected').val();
                        console.log("selectName:"+selectName);
                        if(selectName == "是") {
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('无');
                            page14 = false;
                        }else{
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('');
                            page14 = true;
                            $(this).parent().parent().parent().next("div").children().children().children("input").on('change',
                                function () {
                                    // if($(this).parent().parent().parent().next("div").children().children().children("input").val() != "") {
                                    //     page14 = false;
                                    // }else{
                                    //     page14 = true;
                                    // }
                                    page14 = false;
                                });
                            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '若不实施，则不实施理由必填'});
                        }
                    });
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
                $("input[name*='工作状态描述']").attr("readonly", "readonly");
                $("#editor_holder button").addClass("hidden");
                //这里改成上面的隐藏按钮的方法，之后若是还有别的按钮，可以选择使用底下注释掉的disable方法（应该是有别的按钮的）
                // disableAddAndDelete('root.试验端口及被试品工作状态', true);
                // var beforeMode = $("input[name='root[限值]']");
                // beforeMode.prop("disabled", false);

                break;
            case '15':
                page15 = false;
                $("select[name*='是否实施']").on('change',
                    function () {
                        var selectName = $(this).children('option:selected').val();
                        console.log("selectName:"+selectName);
                        if(selectName == "是") {
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('无');
                            page15 = false;
                        }else{
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('');
                            page15 = true;
                            $(this).parent().parent().parent().next("div").children().children().children("input").on('change',
                                function () {
                                    // if($(this).parent().parent().parent().next("div").children().children().children("input").val() != "") {
                                    //     page15 = false;
                                    // }else{
                                    //     page15 = true;
                                    // }
                                    page15 = false;
                                });
                            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '若不实施，则不实施理由必填'});
                        }
                    });
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.结果评定准则').disable();
                var testPortArray = load_data.试验端口及被试品工作状态;
                for(var i = 0; i < testPortArray.length; i++) {
                    var editorName = 'root.试验端口及被试品工作状态.'+i+'.试验端口';
                    editor.getEditor(editorName).disable();
                }
                $("input[name*='工作状态描述']").attr("readonly", "readonly");
                $("input[name*='']").width(200);

                $("#editor_holder button").addClass("hidden");
                break;
            case '16':
                page16 = false;
                $("button:contains('删除所有')").addClass("hidden");
                $("button:contains('删除最后一行')").addClass("hidden");
                $("input[name*='工作状态']").attr("readonly", "readonly");
                $("input[name*='不实施理由']").removeAttr("readonly");
                $("input[name*='备注']").removeAttr("readonly");
                $("select[name*='是否实施']").on('change',
                    function () {
                        var selectName = $(this).children('option:selected').val();
                        console.log("selectName:"+selectName);
                        if(selectName == "是") {
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('无');
                            page16 = false;
                        }else{
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('');
                            page16 = true;
                            $(this).parent().parent().parent().next("div").children().children().children("input").on('change',
                                function () {
                                    // if($(this).parent().parent().parent().next("div").children().children().children("input").val() != "") {
                                    //     page16 = false;
                                    // }else{
                                    //     page16 = true;
                                    // }
                                    page16 = false;
                                });
                            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '若不实施，则不实施理由必填'});
                        }
                    });
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                var testPortArray = load_data.试验端口及被试品工作状态;
                for(var i = 0; i < testPortArray.length; i++) {
                    var editorName = 'root.试验端口及被试品工作状态.'+i+'.天线端口';
                    editor.getEditor(editorName).disable();
                }
                // $("input[name*='工作状态描述']").attr("readonly", "readonly");
                editor.on('change',function() {
                    // $("input[name*='工作状态描述']").attr("readonly", "readonly");
                    $("input[name*='工作状态']").attr("readonly", "readonly");
                    $("input[name*='不实施理由']").removeAttr("readonly");
                    $("input[name*='备注']").removeAttr("readonly");
                });
                // $("#editor_holder button").addClass("hidden");
                break;
            case '17':
                page17 = false;
                $("select[name*='是否实施']").on('change',
                    function () {
                        var selectName = $(this).children('option:selected').val();
                        console.log("selectName:"+selectName);
                        if(selectName == "是") {
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('无');
                            page17 = false;
                        }else{
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('');
                            page17 = true;
                            $(this).parent().parent().parent().next("div").children().children().children("input").on('change',
                                function () {
                                    // if($(this).parent().parent().parent().next("div").children().children().children("input").val() != "") {
                                    //     page17 = false;
                                    // }else{
                                    //     page17 = true;
                                    // }
                                    page17 = false;
                                });
                            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '若不实施，则不实施理由必填'});
                        }
                    });
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                var testPortArray = load_data.试验端口及被试品工作状态;
                for(var i = 0; i < testPortArray.length; i++) {
                    var editorName = 'root.试验端口及被试品工作状态.'+i+'.试验电源端口';
                    editor.getEditor(editorName).disable();
                }

                if(devAttribute == 0){
                    $("input[name*='工作状态描述']").attr("readonly", "readonly");
                    editor.on('change',function() {
                        $("input[name*='工作状态描述']").attr("readonly", "readonly");
                    });
                    $("#editor_holder button").addClass("hidden");
                }
                // else if(devAttribute == 1){
                //     $("div[data-schemapath*='工作状态'] button").removeClass("hidden");
                // }

                break;

            case '18':
                page18 = false;
                $("select[name*='是否实施']").on('change',
                    function () {
                        var selectName = $(this).children('option:selected').val();
                        console.log("selectName:"+selectName);
                        if(selectName == "是") {
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('无');
                            page18 = false;
                        }else{
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('');
                            page18 = true;
                            $(this).parent().parent().parent().next("div").children().children().children("input").on('change',
                                function () {
                                    // if($(this).parent().parent().parent().next("div").children().children().children("input").val() != "") {
                                    //     page18 = false;
                                    // }else{
                                    //     page18 = true;
                                    // }
                                    page18 = false;
                                });
                            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '若不实施，则不实施理由必填'});
                        }
                    });
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测定结果评定准则').disable();
                var testPortArray = load_data.试验端口及被试品工作状态;
                for(var i = 0; i < testPortArray.length; i++) {
                    var editorName = 'root.试验端口及被试品工作状态.'+i+'.试验端口';
                    editor.getEditor(editorName).disable();
                }
                $("input[name*='工作状态描述']").attr("readonly", "readonly");
                editor.on('change',function() {
                    $("input[name*='工作状态描述']").attr("readonly", "readonly");
                });
                $("#editor_holder button").addClass("hidden");
                break;
            case '19':
                page19 = false;
                $("select[name*='是否实施']").on('change',
                    function () {
                        var selectName = $(this).children('option:selected').val();
                        console.log("selectName:"+selectName);
                        if(selectName == "是") {
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('无');
                            page19 = false;
                        }else{
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('');
                            page19 = true;
                            $(this).parent().parent().parent().next("div").children().children().children("input").on('change',
                                function () {
                                    // if($(this).parent().parent().parent().next("div").children().children().children("input").val() != "") {
                                    //     page19 = false;
                                    // }else{
                                    //     page19 = true;
                                    // }
                                    page19 = false;
                                });
                            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '若不实施，则不实施理由必填'});
                        }
                    });
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                var testPortArray = load_data.试验端口及被试品工作状态;
                if(testPortArray.length != 0) {
                    var workStatusNum = load_data.试验端口及被试品工作状态[0].工作状态;
                    if(workStatusNum != null) {
                        // for (var i = 0; i < testPortArray.length; i++) {
                        //     for(var j=0; j<workStatusNum.length; j++) {
                        //         var editorName = 'root.试验端口及被试品工作状态.' + i + '.工作状态.工作状态'+j+'.工作状态描述';
                        //         editor.getEditor(editorName).disable();
                        //     }
                        // }
                        $("input[name*='工作状态描述']").attr("readonly", "readonly");
                    }
                }
                editor.on('change',function() {
                    $("input[name*='工作状态描述']").attr("readonly", "readonly");
                });
                // $("#editor_holder button").addClass("hidden");
                break;
            case '20':
                page20 = false;
                $("button:contains('删除所有')").addClass("hidden");
                $("button:contains('删除最后一行')").addClass("hidden");
                $("input[name*='工作状态']").attr("readonly", "readonly");
                $("input[name*='不实施理由']").removeAttr("readonly");
                $("input[name*='备注']").removeAttr("readonly");
                $("select[name*='是否实施']").on('change',
                    function () {
                        var selectName = $(this).children('option:selected').val();
                        console.log("selectName:"+selectName);
                        if(selectName == "是") {
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('无');
                            page20 = false;
                        }else{
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('');
                            page20 = true;
                            $(this).parent().parent().parent().next("div").children().children().children("input").on('change',
                                function () {
                                    // if($(this).parent().parent().parent().next("div").children().children().children("input").val() != "") {
                                    //     page20 = false;
                                    // }else{
                                    //     page20 = true;
                                    // }
                                    page20 = false;
                                });
                            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '若不实施，则不实施理由必填'});
                        }
                    });
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                editor.on('change',function() {
                    $("input[name*='工作状态']").attr("readonly", "readonly");
                    $("input[name*='不实施理由']").removeAttr("readonly");
                    $("input[name*='备注']").removeAttr("readonly");
                });
                var testPortArray = load_data.试验端口及被试品工作状态;
                // if(testPortArray != null) {
                    for (var i = 0; i < testPortArray.length; i++) {
                        var editorName = 'root.试验端口及被试品工作状态.' + i + '.天线端口';
                        editor.getEditor(editorName).disable();
                    }
                // }

                // $("#editor_holder button").addClass("hidden");
                break;
            case '21':
                page21 = false;
                $("button:contains('删除所有')").addClass("hidden");
                $("button:contains('删除最后一行')").addClass("hidden");
                $("input[name*='工作状态']").attr("readonly", "readonly");
                $("input[name*='不实施理由']").removeAttr("readonly");
                $("input[name*='备注']").removeAttr("readonly");
                $("select[name*='是否实施']").on('change',
                    function () {
                        var selectName = $(this).children('option:selected').val();
                        console.log("selectName:"+selectName);
                        if(selectName == "是") {
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('无');
                            page21 = false;
                        }else{
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('');
                            page21 = true;
                            $(this).parent().parent().parent().next("div").children().children().children("input").on('change',
                                function () {
                                    // if($(this).parent().parent().parent().next("div").children().children().children("input").val() != "") {
                                    //     page21 = false;
                                    // }else{
                                    //     page21 = true;
                                    // }
                                    page21 = false;
                                });
                            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '若不实施，则不实施理由必填'});
                        }
                    });
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                var testPortArray = load_data.试验端口及被试品工作状态;
                for(var i = 0; i < testPortArray.length; i++) {
                    var editorName = 'root.试验端口及被试品工作状态.'+i+'.天线端口';
                    editor.getEditor(editorName).disable();
                }
                editor.on('change',function() {
                    $("input[name*='工作状态']").attr("readonly", "readonly");
                    $("input[name*='不实施理由']").removeAttr("readonly");
                    $("input[name*='备注']").removeAttr("readonly");
                });
                // $("#editor_holder button").addClass("hidden");
                break;
            case '22':
                page22 = false;
                $("button:contains('删除所有')").addClass("hidden");
                $("button:contains('删除最后一行')").addClass("hidden");
                $("input[name*='工作状态']").attr("readonly", "readonly");
                $("input[name*='不实施理由']").removeAttr("readonly");
                $("input[name*='备注']").removeAttr("readonly");
                $("select[name*='是否实施']").on('change',
                    function () {
                        var selectName = $(this).children('option:selected').val();
                        console.log("selectName:"+selectName);
                        if(selectName == "是") {
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('无');
                            page22 = false;
                        }else{
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('');
                            page22 = true;
                            $(this).parent().parent().parent().next("div").children().children().children("input").on('change',
                                function () {
                                    // if($(this).parent().parent().parent().next("div").children().children().children("input").val() != "") {
                                    //     page22 = false;
                                    // }else{
                                    //     page22 = true;
                                    // }
                                    page22 = false;
                                });
                            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '若不实施，则不实施理由必填'});
                        }
                    });
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                var testPortArray = load_data.试验端口及被试品工作状态;
                for(var i = 0; i < testPortArray.length; i++) {
                    var editorName = 'root.试验端口及被试品工作状态.'+i+'.天线端口';
                    editor.getEditor(editorName).disable();
                }
                editor.on('change',function() {
                    $("input[name*='工作状态']").attr("readonly", "readonly");
                    $("input[name*='不实施理由']").removeAttr("readonly");
                    $("input[name*='备注']").removeAttr("readonly");
                });
                // $("#editor_holder button").addClass("hidden");
                break;

            case '23':
                page23 = false;
                $("select[name*='是否实施']").on('change',
                    function () {
                        var selectName = $(this).children('option:selected').val();
                        console.log("selectName:"+selectName);
                        if(selectName == "是") {
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('无');
                            page23 = false;
                        }else{
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('');
                            page23 = true;
                            $(this).parent().parent().parent().next("div").children().children().children("input").on('change',
                                function () {
                                    // if($(this).parent().parent().parent().next("div").children().children().children("input").val() != "") {
                                    //     page23 = false;
                                    // }else{
                                    //     page23 = true;
                                    // }
                                    page23 = false;
                                });
                            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '若不实施，则不实施理由必填'});
                        }
                    });
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                var testPortArray = load_data.试验端口及被试品工作状态;
                for(var i = 0; i < testPortArray.length; i++) {
                    var editorName = 'root.试验端口及被试品工作状态.'+i+'.试验端口';
                    editor.getEditor(editorName).disable();
                }
                $("input[name*='工作状态描述']").attr("readonly", "readonly");
                editor.on('change',function() {
                    $("input[name*='工作状态描述']").attr("readonly", "readonly");
                });
                $("#editor_holder button").addClass("hidden");
                break;
            case '24':
                page24 = false;
                $("select[name*='是否实施']").on('change',
                    function () {
                        var selectName = $(this).children('option:selected').val();
                        console.log("selectName:"+selectName);
                        if(selectName == "是") {
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('无');
                            page24 = false;
                        }else{
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('');
                            page24 = true;
                            $(this).parent().parent().parent().next("div").children().children().children("input").on('change',
                                function () {
                                    // if($(this).parent().parent().parent().next("div").children().children().children("input").val() != "") {
                                    //     page24 = false;
                                    // }else{
                                    //     page24 = true;
                                    // }
                                    page24 = false;
                                });
                            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '若不实施，则不实施理由必填'});
                        }
                    });
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                var testPortArray = load_data.试验位置及被试品工作状态;
                for(var i = 0; i < testPortArray.length; i++) {
                    var editorName = 'root.试验位置及被试品工作状态.'+i+'.试验位置';
                    editor.getEditor(editorName).disable();
                }
                $("input[name*='工作状态描述']").attr("readonly", "readonly");
                editor.on('change',function() {
                    $("input[name*='工作状态描述']").attr("readonly", "readonly");
                });
                // $("#editor_holder button").addClass("hidden");
                break
            case '25':
                page25 = false;
                $("select[name*='是否实施']").on('change',
                    function () {
                        var selectName = $(this).children('option:selected').val();
                        console.log("selectName:"+selectName);
                        if(selectName == "是") {
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('无');
                            page25 = false;
                        }else{
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('');
                            page25 = true;
                            $(this).parent().parent().parent().next("div").children().children().children("input").on('change',
                                function () {
                                    // if($(this).parent().parent().parent().next("div").children().children().children("input").val() != "") {
                                    //     page25 = false;
                                    // }else{
                                    //     page25 = true;
                                    // }
                                    page25 = false;
                                });
                            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '若不实施，则不实施理由必填'});
                        }
                    });
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                $("input[name*='工作状态描述']").attr("readonly", "readonly");
                editor.on('change',function() {
                    $("input[name*='工作状态描述']").attr("readonly", "readonly");
                });
                // $("#editor_holder button").addClass("hidden");
                break;
            case '26':
                page26 = false;
                $("select[name*='是否实施']").on('change',
                    function () {
                        var selectName = $(this).children('option:selected').val();
                        console.log("selectName:"+selectName);
                        if(selectName == "是") {
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('无');
                            page26 = false;
                        }else{
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('');
                            page26 = true;
                            $(this).parent().parent().parent().next("div").children().children().children("input").on('change',
                                function () {
                                    // if($(this).parent().parent().parent().next("div").children().children().children("input").val() != "") {
                                    //     page26 = false;
                                    // }else{
                                    //     page26 = true;
                                    // }
                                    page26 = false;
                                });
                            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '若不实施，则不实施理由必填'});
                        }
                    });
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                if(load_data.试验端口及被试品工作状态 != null) {
                    var testPortArray1 = load_data.试验端口及被试品工作状态.电源端口;
                    var testPortArray2 = load_data.试验端口及被试品工作状态.互联端口;
                    for (var i = 0; i < testPortArray1.length; i++) {
                        var editorName1 = 'root.试验端口及被试品工作状态.电源端口.' + i + '.电源端口';
                        editor.getEditor(editorName1).disable();
                    }
                    for (var i = 0; i < testPortArray2.length; i++) {
                        var editorName2 = 'root.试验端口及被试品工作状态.互联端口.' + i + '.互联端口';
                        editor.getEditor(editorName2).disable();
                    }
                }
                $("input[name*='施加电缆束']").attr("readonly", "readonly");
                $("input[name*='工作状态描述']").attr("readonly", "readonly");
                editor.on('change',function() {
                    $("input[name*='工作状态描述']").attr("readonly", "readonly");
                    $("input[name*='施加电缆束']").attr("readonly", "readonly");
                });
                $("#editor_holder button").addClass("hidden");
                break;

            case '27':
                page27 = false;
                $("select[name*='是否实施']").on('change',
                    function () {
                        var selectName = $(this).children('option:selected').val();
                        console.log("selectName:"+selectName);
                        if(selectName == "是") {
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('无');
                            page27 = false;
                        }else{
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('');
                            page27 = true;
                            $(this).parent().parent().parent().next("div").children().children().children("input").on('change',
                                function () {
                                    // if($(this).parent().parent().parent().next("div").children().children().children("input").val() != "") {
                                    //     page27 = false;
                                    // }else{
                                    //     page27 = true;
                                    // }
                                    page27 = false;
                                });
                            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '若不实施，则不实施理由必填'});
                        }
                    });
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                if(load_data.试验端口及被试品工作状态 != null) {
                    var testPortArray1 = load_data.试验端口及被试品工作状态.电源端口;
                    var testPortArray2 = load_data.试验端口及被试品工作状态.互联端口;
                    for (var i = 0; i < testPortArray1.length; i++) {
                        var editorName1 = 'root.试验端口及被试品工作状态.电源端口.' + i + '.电源端口';
                        editor.getEditor(editorName1).disable();
                    }
                    for (var i = 0; i < testPortArray2.length; i++) {
                        var editorName2 = 'root.试验端口及被试品工作状态.互联端口.' + i + '.互联端口';
                        editor.getEditor(editorName2).disable();
                    }
                }
                $("input[name*='施加电缆束']").attr("readonly", "readonly");
                $("input[name*='工作状态描述']").attr("readonly", "readonly");
                editor.on('change',function() {
                    $("input[name*='工作状态描述']").attr("readonly", "readonly");
                    $("input[name*='施加电缆束']").attr("readonly", "readonly");
                });
                $("#editor_holder button").addClass("hidden");
                break;
            case '28':
                page28 = false;
                $("select[name*='是否实施']").on('change',
                    function () {
                        var selectName = $(this).children('option:selected').val();
                        console.log("selectName:"+selectName);
                        if(selectName == "是") {
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('无');
                            page28 = false;
                        }else{
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('');
                            page28 = true;
                            $(this).parent().parent().parent().next("div").children().children().children("input").on('change',
                                function () {
                                    // if($(this).parent().parent().parent().next("div").children().children().children("input").val() != "") {
                                    //     page28 = false;
                                    // }else{
                                    //     page28 = true;
                                    // }
                                    page28 = false;
                                });
                            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '若不实施，则不实施理由必填'});
                        }
                    });
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                if(load_data.试验端口及被试品工作状态 != null) {
                    var testPortArray1 = load_data.试验端口及被试品工作状态.电源端口;
                    var testPortArray2 = load_data.试验端口及被试品工作状态.互联端口;
                    for (var i = 0; i < testPortArray1.length; i++) {
                        var editorName1 = 'root.试验端口及被试品工作状态.电源端口.' + i + '.电源端口';
                        editor.getEditor(editorName1).disable();
                    }
                    for (var i = 0; i < testPortArray2.length; i++) {
                        var editorName2 = 'root.试验端口及被试品工作状态.互联端口.' + i + '.互联端口';
                        editor.getEditor(editorName2).disable();
                    }
                }
                $("input[name*='施加电缆束']").attr("readonly", "readonly");
                $("input[name*='工作状态描述']").attr("readonly", "readonly");
                editor.on('change',function() {
                    $("input[name*='工作状态描述']").attr("readonly", "readonly");
                    $("input[name*='施加电缆束']").attr("readonly", "readonly");
                });
                $("#editor_holder button").addClass("hidden");
                break;
            case '29':
                page29 = false;
                $("select[name*='是否实施']").on('change',
                    function () {
                        var selectName = $(this).children('option:selected').val();
                        console.log("selectName:"+selectName);
                        if(selectName == "是") {
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('无');
                            page29 = false;
                        }else{
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('');
                            page29 = true;
                            $(this).parent().parent().parent().next("div").children().children().children("input").on('change',
                                function () {
                                    // if($(this).parent().parent().parent().next("div").children().children().children("input").val() != "") {
                                    //     page29 = false;
                                    // }else{
                                    //     page29 = true;
                                    // }
                                    page29 = false;
                                });
                            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '若不实施，则不实施理由必填'});
                        }
                    });
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                $("input[name*='工作状态描述']").attr("readonly", "readonly");
                editor.on('change',function() {
                    $("input[name*='工作状态描述']").attr("readonly", "readonly");
                });
                // $("#editor_holder button").addClass("hidden");
                break;
            case '30':
                page30 = false;
                $("select[name*='是否实施']").on('change',
                    function () {
                        var selectName = $(this).children('option:selected').val();
                        console.log("selectName:"+selectName);
                        if(selectName == "是") {
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('无');
                            page30 = false;
                        }else{
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('');
                            page30 = true;
                            $(this).parent().parent().parent().next("div").children().children().children("input").on('change',
                                function () {
                                    // if($(this).parent().parent().parent().next("div").children().children().children("input").val() != "") {
                                    //     page30 = false;
                                    // }else{
                                    //     page30 = true;
                                    // }
                                    page30 = false;
                                });
                            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '若不实施，则不实施理由必填'});
                        }
                    });
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                $("input[name*='工作状态描述']").attr("readonly", "readonly");
                $("input[name*='频率（GHz）']").attr("readonly", "readonly");
                editor.on('change',function() {
                    $("input[name*='工作状态描述']").attr("readonly", "readonly");
                    $("input[name*='频率（GHz）']").attr("readonly", "readonly");
                });
                // $("#editor_holder button").addClass("hidden");
                break;
            case '31':
                page31 = false;
                $("select[name*='是否实施']").on('change',
                    function () {
                        var selectName = $(this).children('option:selected').val();
                        console.log("selectName:"+selectName);
                        if(selectName == "是") {
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('无');
                            page31 = false;
                        }else{
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('');
                            page31 = true;
                            $(this).parent().parent().parent().next("div").children().children().children("input").on('change',
                                function () {
                                    // if($(this).parent().parent().parent().next("div").children().children().children("input").val() != "") {
                                    //     page31 = false;
                                    // }else{
                                    //     page31 = true;
                                    // }
                                    page31 = false;
                                });
                            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '若不实施，则不实施理由必填'});
                        }
                    });
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                $("input[name*='工作状态描述']").attr("readonly", "readonly");
                editor.on('change',function() {
                    $("input[name*='工作状态描述']").attr("readonly", "readonly");
                });
                // $("#editor_holder button").addClass("hidden");
                break;
            case '32':
                page32 = false;
                $("select[name*='是否实施']").on('change',
                    function () {
                        var selectName = $(this).children('option:selected').val();
                        console.log("selectName:"+selectName);
                        if(selectName == "是") {
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('无');
                            page32 = false;
                        }else{
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('');
                            page32 = true;
                            $(this).parent().parent().parent().next("div").children().children().children("input").on('change',
                                function () {
                                    // if($(this).parent().parent().parent().next("div").children().children().children("input").val() != "") {
                                    //     page32 = false;
                                    // }else{
                                    //     page32 = true;
                                    // }
                                    page32 = false;
                                });
                            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '若不实施，则不实施理由必填'});
                        }
                    });
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                $("input[name*='工作状态描述']").attr("readonly", "readonly");
                editor.on('change',function() {
                    $("input[name*='工作状态描述']").attr("readonly", "readonly");
                });
                // $("#editor_holder button").addClass("hidden");
                break;
            case '33':
                page33 = false;
                $("select[name*='是否实施']").on('change',
                    function () {
                        var selectName = $(this).children('option:selected').val();
                        console.log("selectName:"+selectName);
                        if(selectName == "是") {
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('无');
                            page33 = false;
                        }else{
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('');
                            page33 = true;
                            $(this).parent().parent().parent().next("div").children().children().children("input").on('change',
                                function () {
                                    // if($(this).parent().parent().parent().next("div").children().children().children("input").val() != "") {
                                    //     page33 = false;
                                    // }else{
                                    //     page33 = true;
                                    // }
                                    page33 = false;
                                });
                            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '若不实施，则不实施理由必填'});
                        }
                    });
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                $("input[name*='工作状态描述']").attr("readonly", "readonly");
                $("input[name*='频率（GHz）']").attr("readonly", "readonly");
                editor.on('change',function() {
                    $("input[name*='工作状态描述']").attr("readonly", "readonly");
                    $("input[name*='频率（GHz）']").attr("readonly", "readonly");
                });
                // $("#editor_holder button").addClass("hidden");
                break;
            case '34':
                page34 = false;
                $("select[name*='是否实施']").on('change',
                    function () {
                        var selectName = $(this).children('option:selected').val();
                        console.log("selectName:"+selectName);
                        if(selectName == "是") {
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('无');
                            page34 = false;
                        }else{
                            $(this).parent().parent().parent().next("div").children().children().children("input").val('');
                            page34 = true;
                            $(this).parent().parent().parent().next("div").children().children().children("input").on('change',
                                function () {
                                    // if($(this).parent().parent().parent().next("div").children().children().children("input").val() != "") {
                                    //     page34 = false;
                                    // }else{
                                    //     page34 = true;
                                    // }
                                    page34 = false;
                                });
                            $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '若不实施，则不实施理由必填'});
                        }
                    });
                editor.getEditor('root.试验项目').disable();
                editor.getEditor('root.试验内容').disable();
                editor.getEditor('root.限值').disable();
                editor.getEditor('root.数据处理方法').disable();
                editor.getEditor('root.测试结果评定准则').disable();
                $("input[name*='工作状态描述']").attr("readonly", "readonly");
                editor.on('change',function() {
                    $("input[name*='工作状态描述']").attr("readonly", "readonly");
                });
                // $("#editor_holder button").addClass("hidden");
                break;
            case '35':
                editor.getEditor('root.CE101 测试设备').disable();
                $("div[data-schemapath='root.CE101 测试设备'] button").addClass("hidden");
                if(outlineStatus == 0 || outlineStatus == 4) {
                    $("textarea[name='root[CE101 测试设备][5][主要性能指标]']").prop('disabled', false);
                    $("textarea[name='root[CE101 测试设备][0][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE101 测试设备][1][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE101 测试设备][2][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE101 测试设备][3][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE101 测试设备][4][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE101 测试设备][5][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE101 测试设备][6][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE101 测试设备][7][备注]']").prop('disabled', false);
                }
                // for (var i = 0; i < 8; i++) {
                //     var limitName = 'root.CE101 测试设备.' + i + '.限值';
                //     editor.getEditor(limitName).hide();
                // }
                $("textarea[name*='限值']").addClass("hidden");
                $("th:contains('限值')").text("");
                break;
            case '36':
                editor.getEditor('root.CE102 测试设备').disable();
                $("div[data-schemapath='root.CE102 测试设备'] button").addClass("hidden");
                if(outlineStatus == 0 || outlineStatus == 4) {
                    $("textarea[name='root[CE102 测试设备][0][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE102 测试设备][1][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE102 测试设备][2][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE102 测试设备][3][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE102 测试设备][4][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE102 测试设备][5][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE102 测试设备][6][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE102 测试设备][7][备注]']").prop('disabled', false);
                }
                $("textarea[name*='限值']").addClass("hidden");
                $("th:contains('限值')").text("");
                break;
            case '37':
                editor.getEditor('root.CE106 测试设备').disable();
                $("div[data-schemapath='root.CE106 测试设备'] button").addClass("hidden");
                if(outlineStatus == 0 || outlineStatus == 4) {
                    $("textarea[name='root[CE106 测试设备][2][主要性能指标]']").prop('disabled', false);
                    $("textarea[name='root[CE106 测试设备][5][主要性能指标]']").prop('disabled', false);
                    $("textarea[name='root[CE106 测试设备][0][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE106 测试设备][1][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE106 测试设备][2][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE106 测试设备][3][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE106 测试设备][4][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE106 测试设备][5][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE106 测试设备][6][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE106 测试设备][7][备注]']").prop('disabled', false);
                }
                $("textarea[name*='限值']").addClass("hidden");
                $("th:contains('限值')").text("");
                break;
            case '38':
                editor.getEditor('root.CE107 测试设备').disable();
                $("div[data-schemapath='root.CE107 测试设备'] button").addClass("hidden");
                if(outlineStatus == 0 || outlineStatus == 4) {
                    $("textarea[name='root[CE107 测试设备][0][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE107 测试设备][1][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE107 测试设备][2][备注]']").prop('disabled', false);
                    $("textarea[name='root[CE107 测试设备][3][备注]']").prop('disabled', false);
                }
                $("textarea[name*='限值']").addClass("hidden");
                $("th:contains('限值')").text("");
                break;
            case '39':
                editor.getEditor('root.CS101 测试设备').disable();
                $("div[data-schemapath='root.CS101 测试设备'] button").addClass("hidden");
                if(outlineStatus == 0 || outlineStatus == 4) {
                    $("textarea[name='root[CS101 测试设备][8][主要性能指标]']").prop('disabled', false);
                    $("textarea[name='root[CS101 测试设备][0][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS101 测试设备][1][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS101 测试设备][2][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS101 测试设备][3][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS101 测试设备][4][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS101 测试设备][5][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS101 测试设备][6][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS101 测试设备][7][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS101 测试设备][8][备注]']").prop('disabled', false);
                }
                $("textarea[name*='限值']").addClass("hidden");
                $("th:contains('限值')").text("");
                break;
            case '40':
                editor.getEditor('root.CS102 测试设备').disable();
                $("div[data-schemapath='root.CS102 测试设备'] button").addClass("hidden");
                if(outlineStatus == 0 || outlineStatus == 4) {
                    $("textarea[name='root[CS102 测试设备][0][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS102 测试设备][1][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS102 测试设备][2][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS102 测试设备][3][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS102 测试设备][4][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS102 测试设备][5][备注]']").prop('disabled', false);
                }
                $("textarea[name*='限值']").addClass("hidden");
                $("th:contains('限值')").text("");
                break;
            case '41':
                editor.getEditor('root.CS103 测试设备').disable();
                $("div[data-schemapath='root.CS103 测试设备'] button").addClass("hidden");
                if(outlineStatus == 0 || outlineStatus == 4) {
                    $("textarea[name='root[CS103 测试设备][0][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS103 测试设备][1][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS103 测试设备][2][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS103 测试设备][3][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS103 测试设备][4][备注]']").prop('disabled', false);
                }
                $("textarea[name*='限值']").addClass("hidden");
                $("th:contains('限值')").text("");
                break;
            case '42':
                editor.getEditor('root.CS104 测试设备').disable();
                $("div[data-schemapath='root.CS104 测试设备'] button").addClass("hidden");
                if(outlineStatus == 0 || outlineStatus == 4) {
                    $("textarea[name='root[CS104 测试设备][0][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS104 测试设备][1][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS104 测试设备][2][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS104 测试设备][3][备注]']").prop('disabled', false);
                }
                $("textarea[name*='限值']").addClass("hidden");
                $("th:contains('限值')").text("");
                break;
            case '43':
                editor.getEditor('root.CS105 测试设备').disable();
                $("div[data-schemapath='root.CS105 测试设备'] button").addClass("hidden");
                if(outlineStatus == 0 || outlineStatus == 4) {
                    $("textarea[name='root[CS105 测试设备][0][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS105 测试设备][1][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS105 测试设备][2][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS105 测试设备][3][备注]']").prop('disabled', false);
                }
                $("textarea[name*='限值']").addClass("hidden");
                $("th:contains('限值')").text("");
                break;
            case '44':
                editor.getEditor('root.CS106 测试设备').disable();
                $("div[data-schemapath='root.CS106 测试设备'] button").addClass("hidden");
                if(outlineStatus == 0 || outlineStatus == 4) {
                    $("textarea[name='root[CS106 测试设备][6][主要性能指标]']").prop('disabled', false);
                    $("textarea[name='root[CS106 测试设备][0][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS106 测试设备][1][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS106 测试设备][2][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS106 测试设备][3][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS106 测试设备][4][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS106 测试设备][5][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS106 测试设备][6][备注]']").prop('disabled', false);
                }
                $("textarea[name*='限值']").addClass("hidden");
                $("th:contains('限值')").text("");
                break;
            case '45':
                editor.getEditor('root.CS109 测试设备').disable();
                $("div[data-schemapath='root.CS109 测试设备'] button").addClass("hidden");
                if(outlineStatus == 0 || outlineStatus == 4) {
                    $("textarea[name='root[CS109 测试设备][5][主要性能指标]']").prop('disabled', false);
                    $("textarea[name='root[CS109 测试设备][0][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS109 测试设备][1][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS109 测试设备][2][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS109 测试设备][3][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS109 测试设备][4][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS109 测试设备][5][备注]']").prop('disabled', false);
                }
                $("textarea[name*='限值']").addClass("hidden");
                $("th:contains('限值')").text("");
                break;
            case '46':
                editor.getEditor('root.CS112 测试设备').disable();
                $("div[data-schemapath='root.CS112 测试设备'] button").addClass("hidden");
                if(outlineStatus == 0 || outlineStatus == 4) {
                    $("textarea[name='root[CS112 测试设备][0][备注]']").prop('disabled', false);
                }
                $("textarea[name*='限值']").addClass("hidden");
                $("th:contains('限值')").text("");
                break;
            case '47':
                editor.getEditor('root.CS114 测试设备').disable();
                $("div[data-schemapath='root.CS114 测试设备'] button").addClass("hidden");
                if(outlineStatus == 0 || outlineStatus == 4) {
                    $("textarea[name='root[CS114 测试设备][0][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS114 测试设备][1][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS114 测试设备][2][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS114 测试设备][3][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS114 测试设备][4][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS114 测试设备][5][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS114 测试设备][6][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS114 测试设备][7][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS114 测试设备][8][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS114 测试设备][9][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS114 测试设备][10][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS114 测试设备][11][备注]']").prop('disabled', false);
                }
                $("textarea[name*='限值']").addClass("hidden");
                $("th:contains('限值')").text("");
                break;
            case '48':
                editor.getEditor('root.CS115 测试设备').disable();
                $("div[data-schemapath='root.CS115 测试设备'] button").addClass("hidden");
                if(outlineStatus == 0 || outlineStatus == 4) {
                    $("textarea[name='root[CS115 测试设备][0][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS115 测试设备][1][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS115 测试设备][2][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS115 测试设备][3][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS115 测试设备][4][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS115 测试设备][5][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS115 测试设备][6][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS115 测试设备][7][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS115 测试设备][8][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS115 测试设备][9][备注]']").prop('disabled', false);
                }
                $("textarea[name*='限值']").addClass("hidden");
                $("th:contains('限值')").text("");
                break;
            case '49':
                editor.getEditor('root.CS116 测试设备').disable();
                $("div[data-schemapath='root.CS116 测试设备'] button").addClass("hidden");
                if(outlineStatus == 0 || outlineStatus == 4) {
                    $("textarea[name='root[CS116 测试设备][0][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS116 测试设备][1][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS116 测试设备][2][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS116 测试设备][3][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS116 测试设备][4][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS116 测试设备][5][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS116 测试设备][6][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS116 测试设备][7][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS116 测试设备][8][备注]']").prop('disabled', false);
                    $("textarea[name='root[CS116 测试设备][9][备注]']").prop('disabled', false);
                }
                $("textarea[name*='限值']").addClass("hidden");
                $("th:contains('限值')").text("");
                break;
            case '50':
                editor.getEditor('root.RE101 测试设备').disable();
                $("div[data-schemapath='root.RE101 测试设备'] button").addClass("hidden");
                if(outlineStatus == 0 || outlineStatus == 4) {
                    $("textarea[name='root[RE101 测试设备][0][备注]']").prop('disabled', false);
                    $("textarea[name='root[RE101 测试设备][1][备注]']").prop('disabled', false);
                    $("textarea[name='root[RE101 测试设备][2][备注]']").prop('disabled', false);
                    $("textarea[name='root[RE101 测试设备][3][备注]']").prop('disabled', false);
                    $("textarea[name='root[RE101 测试设备][4][备注]']").prop('disabled', false);
                }
                $("textarea[name*='限值']").addClass("hidden");
                $("th:contains('限值')").text("");
                break;
            case '51':
                editor.getEditor('root.RE102 测试设备').disable();
                $("div[data-schemapath='root.RE102 测试设备'] button").addClass("hidden");
                if(outlineStatus == 0 || outlineStatus == 4) {
                    $("textarea[name='root[RE102 测试设备][0][备注]']").prop('disabled', false);
                    $("textarea[name='root[RE102 测试设备][1][备注]']").prop('disabled', false);
                    $("textarea[name='root[RE102 测试设备][2][备注]']").prop('disabled', false);
                    $("textarea[name='root[RE102 测试设备][3][备注]']").prop('disabled', false);
                    $("textarea[name='root[RE102 测试设备][4][备注]']").prop('disabled', false);
                    $("textarea[name='root[RE102 测试设备][5][备注]']").prop('disabled', false);
                    $("textarea[name='root[RE102 测试设备][6][备注]']").prop('disabled', false);
                    $("textarea[name='root[RE102 测试设备][7][备注]']").prop('disabled', false);
                }
                $("textarea[name*='限值']").addClass("hidden");
                $("th:contains('限值')").text("");
                break;
            case '52':
                editor.getEditor('root.RE103 测试设备').disable();
                $("div[data-schemapath='root.RE103 测试设备'] button").addClass("hidden");
                if(outlineStatus == 0 || outlineStatus == 4) {
                    $("textarea[name='root[RE103 测试设备][7][主要性能指标]']").prop('disabled', false);
                    $("textarea[name='root[RE103 测试设备][0][备注]']").prop('disabled', false);
                    $("textarea[name='root[RE103 测试设备][1][备注]']").prop('disabled', false);
                    $("textarea[name='root[RE103 测试设备][2][备注]']").prop('disabled', false);
                    $("textarea[name='root[RE103 测试设备][3][备注]']").prop('disabled', false);
                    $("textarea[name='root[RE103 测试设备][4][备注]']").prop('disabled', false);
                    $("textarea[name='root[RE103 测试设备][5][备注]']").prop('disabled', false);
                    $("textarea[name='root[RE103 测试设备][6][备注]']").prop('disabled', false);
                    $("textarea[name='root[RE103 测试设备][7][备注]']").prop('disabled', false);
                    $("textarea[name='root[RE103 测试设备][8][备注]']").prop('disabled', false);
                    $("textarea[name='root[RE103 测试设备][9][备注]']").prop('disabled', false);
                    $("textarea[name='root[RE103 测试设备][10][备注]']").prop('disabled', false);
                }
                $("textarea[name*='限值']").addClass("hidden");
                $("th:contains('限值')").text("");
                break;
            case '53':
                editor.getEditor('root.RS101 测试设备').disable();
                $("div[data-schemapath='root.RS101 测试设备'] button").addClass("hidden");
                if(outlineStatus == 0 || outlineStatus == 4) {
                    $("textarea[name='root[RS101 测试设备][0][备注]']").prop('disabled', false);
                    $("textarea[name='root[RS101 测试设备][1][备注]']").prop('disabled', false);
                    $("textarea[name='root[RS101 测试设备][2][备注]']").prop('disabled', false);
                    $("textarea[name='root[RS101 测试设备][3][备注]']").prop('disabled', false);
                    $("textarea[name='root[RS101 测试设备][4][备注]']").prop('disabled', false);
                    $("textarea[name='root[RS101 测试设备][5][备注]']").prop('disabled', false);
                    $("textarea[name='root[RS101 测试设备][6][备注]']").prop('disabled', false);
                    $("textarea[name='root[RS101 测试设备][7][备注]']").prop('disabled', false);
                }
                $("textarea[name*='限值']").addClass("hidden");
                $("th:contains('限值')").text("");
                break;
            case '54':
                editor.getEditor('root.RS103 测试设备').disable();
                $("div[data-schemapath='root.RS103 测试设备'] button").addClass("hidden");
                if(outlineStatus == 0 || outlineStatus == 4) {
                    $("textarea[name='root[RS103 测试设备][0][备注]']").prop('disabled', false);
                    $("textarea[name='root[RS103 测试设备][1][备注]']").prop('disabled', false);
                    $("textarea[name='root[RS103 测试设备][2][备注]']").prop('disabled', false);
                    $("textarea[name='root[RS103 测试设备][3][备注]']").prop('disabled', false);
                    $("textarea[name='root[RS103 测试设备][4][备注]']").prop('disabled', false);
                    $("textarea[name='root[RS103 测试设备][5][备注]']").prop('disabled', false);
                }
                $("textarea[name*='限值']").addClass("hidden");
                $("th:contains('限值')").text("");
                break;
            case '55':
                editor.getEditor('root.RS105 测试设备').disable();
                $("div[data-schemapath='root.RS105 测试设备'] button").addClass("hidden");
                if(outlineStatus == 0 || outlineStatus == 4) {
                    $("textarea[name='root[RS105 测试设备][0][备注]']").prop('disabled', false);
                    $("textarea[name='root[RS105 测试设备][1][备注]']").prop('disabled', false);
                    $("textarea[name='root[RS105 测试设备][2][备注]']").prop('disabled', false);
                    $("textarea[name='root[RS105 测试设备][3][备注]']").prop('disabled', false);
                    $("textarea[name='root[RS105 测试设备][4][备注]']").prop('disabled', false);
                    $("textarea[name='root[RS105 测试设备][5][备注]']").prop('disabled', false);
                    $("textarea[name='root[RS105 测试设备][6][备注]']").prop('disabled', false);
                }
                $("textarea[name*='限值']").addClass("hidden");
                $("th:contains('限值')").text("");
                break;
            // case '56':
            //     editor.getEditor('root').disable();
            case '57':
                $("#echartsPic1").addClass("hidden");
                // $("#project_submit").addClass("hidden");
                // $("#next_page").removeClass("hidden");
                break;




            case '1001':case '1002':case '1003':case '1004':case '1005':

                // editor.getEditor('root').disable();
                break;
            case '58':
                // editor.getEditor('root.试验项目').disable();
                    // $("#editor_holder button").addClass("hidden");
                // if (Status != 1) {
                //     if (outlineStatus == 0 || outlineStatus == 4) {
                //         $("#project_submit").removeClass("hidden");
                //         $("#next_page").addClass("hidden");
                //         console.log("removeClass");
                //     }
                // }
                // $("input[name='root[试验项目][0][试验项目序号]']").val(1);
                // $("input[name='root[试验项目][1][试验项目序号]']").val(2);


                $("#project_submit").addClass("hidden");
                $("#next_page").removeClass("hidden");
                $("#echartsPic1").removeClass("hidden");
                // var all_data = JSON.parse(pageData.data);
                // console.log('all_data'+":"+all_data);
                // var load_data_58 = all_data.data;+
                // console.log('load_data'+load_data_58);
                // var load_dataObj = JSON.parse(load_data_58);
                // var projectListObj = load_dataObj.试验项目;
                // var projectNumber = projectListObj.length;
                // $("div[data-schemapath*='root.试验项目'] button:contains('添加')").on('click',
                //     function () {
                //         editor.getEditor('root.试验项目.' + projectNumber + '.序号').setValue(projectNumber+1);
                //         projectNumber++;
                //     });
                // $("div[data-schemapath*='root.试验项目'] button:contains('最后一行')").on('click',
                //     function () {
                //         projectNumber-;
                //     });
                // $("div[data-schemapath*='root.试验项目'] button:contains('所有')").on('click',
                //     function () {
                //         projectNumber = 0;
                //     });
                // for (var i = 0; i < projectListObj.length - 1; i++) {
                //     for (var j = 0; j < projectListObj.length - i - 1; j++) {
                //         var switchObj;
                //         var fStartTime = projectListObj[j].计划起始时间;
                //         var sStartTime = projectListObj[j + 1].计划起始时间;
                //         var farr = fStartTime.split("-");
                //         var sarr = sStartTime.split("-");
                //         var fyear = parseInt(farr[0]);
                //         var fmonth = parseInt(farr[1]);
                //         var fday = parseInt(farr[2]);
                //         var syear = parseInt(sarr[0])
                //         var smonth = parseInt(sarr[1]);
                //         var sday = parseInt(sarr[2]);
                //         if (fyear > syear) {
                //             switchObj = projectListObj[j];
                //             projectListObj[j] = projectListObj[j + 1];
                //             projectListObj[j + 1] = switchObj;
                //             continue;
                //         }
                //         if (fyear == syear && fmonth > smonth) {
                //             switchObj = projectListObj[j];
                //             projectListObj[j] = projectListObj[j + 1];
                //             projectListObj[j + 1] = switchObj;
                //             continue;
                //         }
                //         if (fyear == syear && fmonth == smonth && fday > sday) {
                //             switchObj = projectListObj[j];
                //             projectListObj[j] = projectListObj[j + 1];
                //             projectListObj[j + 1] = switchObj;
                //             continue;
                //         }
                //
                //     }
                // }
                // // console.log("最早开始时间" + " " + projectListObj[0].计划起始时间);
                // // console.log("最晚结束时间" + " " + projectListObj[projectListObj.length  - 1].计划结束时间);
                // var mostEarlyTime = projectListObj[0].计划起始时间;
                // var mostLateTime = projectListObj[projectListObj.length - 1].计划结束时间;
                // console.log("最早开始时间" + " " + mostEarlyTime);
                // console.log("最晚结束时间" + " " + mostLateTime);
                // $("input[name='root[试验计划开始时间]']").val(mostEarlyTime);
                // $("input[name='root[试验计划结束时间]']").val(mostLateTime);
                // $("div[data-schemapath*='root.试验项目'] button:contains('添加')").on('click',
                //     function () {
                //         editor.getEditor('root.试验项目.' + projectNumber + '.序号').setValue(projectNumber+1);
                //         projectNumber++;
                //     });
                // $("div[data-schemapath*='root.试验项目'] button:contains('最后一行')").on('click',
                //     function () {
                //         projectNumber-;
                //     });
                // $("div[data-schemapath*='root.试验项目'] button:contains('所有')").on('click',
                //     function () {
                //         projectNumber = 0;
                //     });

                break;
            case '59':
                // $("#showEchartsPic1").addClass("hidden");
                $("#echartsPic1").addClass("hidden");
                if (Status != 1) {
                    if (outlineStatus == 0 || outlineStatus == 4) {
                        $("#project_submit").removeClass("hidden");
                        $("#next_page").addClass("hidden");
                        console.log("removeClass");
                    }
                }
                break;
            default:
                break;

        }
}


function beforeSubmit() {
    switch (page_number) {
        case '8':
            pageAction = 3;
            break;
        case '9':
            if(modifyPage9_launch && modifyPage9_sensitive){
                pageAction = 3;
                modifyPage9_launch = false;
                modifyPage9_sensitive = false;
                changeLocation = 3;
            }else{
                if(modifyPage9_launch) {
                    pageAction = 3;
                    modifyPage9_launch = false;
                    changeLocation = 1;
                }
                if(modifyPage9_sensitive) {
                    pageAction = 3;
                    modifyPage9_sensitive = false;
                    changeLocation = 2;
                }
            }
            break;
        case '10':
            if(modifyPage10) {
                pageAction = 3;
                modifyPage10 = false;
            }
            break;
        case '14':
            // $("select[name*='是否实施']").on('change',
            //     function () {
            //         $(this).parent().parent().parent().next("div").children().children().children("input").val('无');
            //     });
            // $("label:contains('是否实施')").on('change',
            //     function () {
            //         $(this).parent().children("select").val('无');
            //     });


            // var errors = editor.validate();
            // if(errors.length > 0) {
            //     $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
            //     return false;
            // }
            if(page14){
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }

            break;
        case '15':
            if(page15){
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '16':
            if(page16){
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '17':
            // if($("input[name*='理由']").length==0){
            //     console.log("可以找到那个等于0的");
            // }
            // var errors = editor.validate();
            // if(errors.length > 0) {
            //     $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
            //     return false;
            // }
            if(page17){
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '18':
            if(page18){
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '19':
            if(page19){
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '20':
            if(page20){
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '21':
            if(page21){
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '22':
            if(page22){
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '23':
            if(page23){
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '24':
            if(page24){
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '25':
            if(page25){
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '26':
            if(page26){
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '27':
            if(page27){
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '28':
            if(page28){
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '29':
            if(page29){
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '30':
            if(page30){
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '31':
            if(page31){
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '32':
            if(page32){
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '33':
            if(page33){
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        case '34':
            if(page34){
                $.fillTipBox({type: 'warning', icon: 'glyphicon-exclamation-sign', content: '不实施理由项不能为空，若无理由请填无'});
                return false;
            }
            break;
        default:
            break;
    }

    return true;


}


