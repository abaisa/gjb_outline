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
    if (page_number)

        switch (page_number) {
            case '3':
                watchQuantity('root.参编单位', 5);
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
                if($.isEmptyObject(load_properties)) {
                    loadTargetPage(1);
                }else {
                    watchQuantity('root.电源端口', 10);
                    watchQuantity('root.互联端口', 20);
                }
                break;
            case '12':
                // editor.getEditor('root.发射测试参数.频率范围').disable();
            default:
                break;
        }
}


function removeButton() {
      if(page_number){
          switch (page_number) {
              case '12':
                  console.log("到第12页啦！");
                  $("#editor_holder button").addClass("hidden");
                  for(var i=0 ; i<6; i++){
                      editor.getEditor('root.发射测试参数.'+i+'.频率范围').disable();
                      editor.getEditor('root.发射测试参数.'+i+'.6dB带宽(kHz)').disable();
                      editor.getEditor('root.发射测试参数.'+i+'.驻留时间(s)').disable();
                      editor.getEditor('root.发射测试参数.'+i+'.最小测量时间(模拟式测量接收机)').disable();
                  }
                  // editor.getEditor('root.发射测试参数.0.频率范围').disable();
                  // $("table button").addClass("hidden");
                  break;
              case '13':
                  console.log("到第13页啦！");
                  $("#editor_holder button").addClass("hidden");
                  editor.getEditor('root').disable();
                  break;
          }
      }

}