package cn.gjb151b.outline.service;

import cn.gjb151b.outline.Constants.WorkStyleEnums;
import cn.gjb151b.outline.model.ManageSysDevelop;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理试验大纲系统中依赖研制要求系统的频率相关数据
 */
class FreqDependency {


    /**
     * devObject是第一个项目表的数据，后面参数分别指示工作模式
     * （规则详见石墨文档 "X1 X2 X3 说明）
     * 若固定频段和调频全部存在，使用固定频段的数据，不管跳频；由于邮件沟通中没有说直序列扩频，因此这里也先不管
     */
    static String grnerateData(ManageSysDevelop devObject, boolean _receive, boolean _send) {
        class outlineFreqModel {
            private outlineFreqModel(String _freq) {
                this.freq = _freq;
            }
            private outlineFreqModel(outlineFreqModel m) {
                this.order = m.order;
                this.modulation = m.modulation;
                this.workingStyle = m.workingStyle;
                this.freq = m.freq;
                this.extra = m.extra;
            }
            public outlineFreqModel clone() {
                outlineFreqModel cloneObj = new outlineFreqModel(this);
                return cloneObj;
            }
            private int order;
            private String modulation;
            private String freq;
            private String workingStyle;
            private String extra;
        }
        JSONArray freqResArray = new JSONArray();

        String devFreqOptionalStr = devObject.getDevFreqOptional();
        JSONArray devFreqOptionalArray = JSON.parseArray(devFreqOptionalStr);
        List<outlineFreqModel> outlineFreqModelList = new ArrayList<>();
        List<outlineFreqModel> outlineFreqModelListTemp;

        if (devFreqOptionalArray.size() > 0) {
            // 固定频段
            for (Object devFreqOptionalObject : devFreqOptionalArray) {
                JSONObject devFreqRow = (JSONObject) devFreqOptionalObject;
                outlineFreqModelList.add(new outlineFreqModel((String) devFreqRow.get("opt_freq_low")));
                outlineFreqModelList.add(new outlineFreqModel((String) devFreqRow.get("opt_freq_mid")));
                outlineFreqModelList.add(new outlineFreqModel((String) devFreqRow.get("opt_freq_high")));

                // 处理调制方式
                outlineFreqModelListTemp = new ArrayList<>();
                JSONObject modulationMode = (JSONObject)devFreqRow.get("opt_modulation_mode_num");
                String modulation1 = (String)modulationMode.get("opt_modulation_mode_1");
                String modulation2 = (String)modulationMode.get("opt_modulation_mode_2");
                for (outlineFreqModel model:outlineFreqModelList) {
                    model.modulation = modulation1;
                    outlineFreqModelListTemp.add(model.clone());
                    model.modulation = modulation2;
                    outlineFreqModelListTemp.add(model.clone());
                }
                outlineFreqModelList = outlineFreqModelListTemp;

                // 处理工作方式（收发）
                outlineFreqModelListTemp = new ArrayList<>();
                String devWorkingStyle = (String)modulationMode.get("opt_work_style");
                if (devWorkingStyle.equals("1") || devWorkingStyle.equals("2") || devWorkingStyle.equals("3")) {
                    for (outlineFreqModel model : outlineFreqModelList) {
                        if (_receive && (devWorkingStyle.equals("1") || devWorkingStyle.equals("3"))) {
                            model.workingStyle = "接收";
                            outlineFreqModelListTemp.add(model.clone());
                        }
                        if (_receive && (devWorkingStyle.equals("2") || devWorkingStyle.equals("3"))) {
                            model.workingStyle = "发送";
                            outlineFreqModelListTemp.add(model.clone());
                        }
                    }
                    outlineFreqModelList = outlineFreqModelListTemp;
                }
            }
        } else {
            // 调频


        }

        // 为生成的数据加上状态序号
        int order = 1;
        for (outlineFreqModel model:outlineFreqModelList) {
            model.order = order;
            order++;
        }
        // 转为JsonObject
        for (outlineFreqModel model:outlineFreqModelList) {
            JSONObject outlineOneLine = new JSONObject();
            outlineOneLine.put("状态序号", model.order);
            outlineOneLine.put("调制方式", model.modulation);
            outlineOneLine.put("频率", model.freq);
            outlineOneLine.put("工作方式", model.workingStyle);
            outlineOneLine.put("备注", model.extra);
            freqResArray.add(outlineOneLine);
        }

        return JSON.toJSONString(freqResArray);
    }

    static String generateFreqDependency(ManageSysDevelop devObject, int mode) {
        JSONArray freqResArray = new JSONArray();
        int order = 0;
        JSONObject oneLineObject;

        String devFreqOptional = devObject.getDevFreqOptional();
        JSONArray devFreqOptionalArray = JSON.parseArray(devFreqOptional);

        String workstyle;
        for (Object devFreqOptionalArrayObject : devFreqOptionalArray) {
            JSONObject devFreqOptionalArrayJSONObject = (JSONObject) devFreqOptionalArrayObject;
            oneLineObject = new JSONObject();
            oneLineObject.put("状态序号", String.valueOf(order));
            oneLineObject.put("用频方式", "固定");
            oneLineObject.put("最低频率", devFreqOptionalArrayJSONObject.get("opt_freq_low"));
            oneLineObject.put("中间频率", devFreqOptionalArrayJSONObject.get("opt_freq_mid"));
            oneLineObject.put("最高频率", devFreqOptionalArrayJSONObject.get("opt_freq_high"));
            oneLineObject.put("最高传输速率", "--");
            oneLineObject.put("最大发射功率", devFreqOptionalArrayJSONObject.get("opt_ave_pow_transmit_max"));
            workstyle = (String)devFreqOptionalArrayJSONObject.get("opt_work_style");
            if (mode == 0 && workstyle.equals("3")) workstyle = "2";
            if (mode == 2 && workstyle.equals("3")) {
                workstyle = "1";
                oneLineObject.put("工作方式", WorkStyleEnums.getMsgWithCode(workstyle).getMsg());
                JSONObject modulationMode = (JSONObject)devFreqOptionalArrayJSONObject.get("opt_modulation_mode_num");
                String optModulationMode1 = (String)modulationMode.get("opt_modulation_mode_1");
                if(!Strings.isNullOrEmpty(optModulationMode1)){
                    oneLineObject.put("调制方式", optModulationMode1);
                    freqResArray.add(oneLineObject.clone());
                    order += 1;
                }
                String optModulationMode2 = (String)modulationMode.get("opt_modulation_mode_2");
                if(!Strings.isNullOrEmpty(optModulationMode2)){
                    oneLineObject.put("调制方式", optModulationMode2);
                    oneLineObject.put("状态序号", String.valueOf(order));
                    freqResArray.add(oneLineObject.clone());
                    order += 1;
                }
                workstyle = "2";
            }
            oneLineObject.put("工作方式", WorkStyleEnums.getMsgWithCode(workstyle).getMsg());
            // 调制方式，第一个系统如果有修改这里也要调整  oneLineObject.put("调制方式", "待定，第一个系统里可能要改");
            // 注意，这个第一个系统中可能是要改的，第一个系统整理好了之后再补后面的，这里先给固定频率的代码
            JSONObject modulationMode = (JSONObject)devFreqOptionalArrayJSONObject.get("opt_modulation_mode_num");
            String optModulationMode1 = (String)modulationMode.get("opt_modulation_mode_1");
            if(!Strings.isNullOrEmpty(optModulationMode1)){
                oneLineObject.put("调制方式", optModulationMode1);
                freqResArray.add(oneLineObject.clone());
                order += 1;
            }
            String optModulationMode2 = (String)modulationMode.get("opt_modulation_mode_2");
            if(!Strings.isNullOrEmpty(optModulationMode2)){
                oneLineObject.put("调制方式", optModulationMode2);
                oneLineObject.put("状态序号", String.valueOf(order));
                freqResArray.add(oneLineObject.clone());
                order += 1;
            }
        }


        String devFreqFhLow = devObject.getDevFreqFhLow();
        JSONObject devFreqFhLowJSONObject = JSON.parseObject(devFreqFhLow);
        oneLineObject = new JSONObject();
        oneLineObject.put("状态序号", String.valueOf(order));
        oneLineObject.put("用频方式", "跳频低频段");
        oneLineObject.put("最低频率", devFreqFhLowJSONObject.get("freq_low"));
        oneLineObject.put("中间频率", "--");
        oneLineObject.put("最高频率", devFreqFhLowJSONObject.get("freq_high"));
        oneLineObject.put("最高传输速率", "--");
        oneLineObject.put("最大发射功率", devFreqFhLowJSONObject.get("ave_pow_transmit_max"));
        oneLineObject.put("调制方式", "--");
        workstyle = (String)devFreqFhLowJSONObject.get("work_style");
        if (mode == 0 && workstyle.equals("3")) workstyle = "2";
        if (mode == 2 && workstyle.equals("3")) {
            workstyle = "1";
            oneLineObject.put("工作方式", WorkStyleEnums.getMsgWithCode(workstyle).getMsg());
            freqResArray.add(oneLineObject);
            order += 1;
            workstyle = "2";
        }
        oneLineObject.put("状态序号", String.valueOf(order));
        oneLineObject.put("工作方式", WorkStyleEnums.getMsgWithCode(workstyle).getMsg());
        freqResArray.add(oneLineObject);
        order += 1;

        String devFreqFhMid = devObject.getDevFreqFhMid();
        JSONObject devFreqFhMidJSONObject = JSON.parseObject(devFreqFhMid);
        oneLineObject = new JSONObject();
        oneLineObject.put("状态序号", String.valueOf(order));
        oneLineObject.put("用频方式", "跳频中频段");
        oneLineObject.put("最低频率", devFreqFhMidJSONObject.get("freq_low"));
        oneLineObject.put("中间频率", "--");
        oneLineObject.put("最高频率", devFreqFhMidJSONObject.get("freq_high"));
        oneLineObject.put("最高传输速率", "--");
        oneLineObject.put("最大发射功率", devFreqFhMidJSONObject.get("ave_pow_transmit_max"));
        oneLineObject.put("调制方式", "--");
        workstyle = (String)devFreqFhMidJSONObject.get("work_style");
        if (mode == 0 && workstyle.equals("3")) workstyle = "2";
        if (mode == 2 && workstyle.equals("3")) {
            workstyle = "1";
            oneLineObject.put("工作方式", WorkStyleEnums.getMsgWithCode(workstyle).getMsg());
            freqResArray.add(oneLineObject);
            order += 1;
            workstyle = "2";
        }
        oneLineObject.put("状态序号", String.valueOf(order));
        oneLineObject.put("工作方式", WorkStyleEnums.getMsgWithCode(workstyle).getMsg());
        freqResArray.add(oneLineObject);
        order += 1;

        String devFreqFhHigh = devObject.getDevFreqFhHigh();
        JSONObject devFreqFhHighJSONObject = JSON.parseObject(devFreqFhHigh);
        oneLineObject = new JSONObject();
        oneLineObject.put("状态序号", String.valueOf(order));
        oneLineObject.put("用频方式", "跳频高频段");
        oneLineObject.put("最低频率", devFreqFhHighJSONObject.get("freq_low"));
        oneLineObject.put("中间频率", "--");
        oneLineObject.put("最高频率", devFreqFhHighJSONObject.get("freq_high"));
        oneLineObject.put("最高传输速率", "--");
        oneLineObject.put("最大发射功率", devFreqFhHighJSONObject.get("ave_pow_transmit_max"));
        oneLineObject.put("调制方式", "--");
        workstyle = (String)devFreqFhHighJSONObject.get("work_style");
        if (mode == 0 && workstyle.equals("3")) workstyle = "2";
        if (mode == 2 && workstyle.equals("3")) {
            workstyle = "1";
            oneLineObject.put("工作方式", WorkStyleEnums.getMsgWithCode(workstyle).getMsg());
            freqResArray.add(oneLineObject);
            order += 1;
            workstyle = "2";
        }
        oneLineObject.put("工作方式", WorkStyleEnums.getMsgWithCode(workstyle).getMsg());
        oneLineObject.put("状态序号", String.valueOf(order));
        freqResArray.add(oneLineObject);
        order += 1;

        String devFreqDsss = devObject.getDevFreqDsss();
        JSONObject devFreqDsssJSONObject = JSON.parseObject(devFreqDsss);
        oneLineObject = new JSONObject();
        oneLineObject.put("状态序号", String.valueOf(order));
        oneLineObject.put("用频方式", "直序扩频");
        oneLineObject.put("最低频率", "--");
        oneLineObject.put("中间频率", "--");
        oneLineObject.put("最高频率", "--");
        oneLineObject.put("最高传输速率", devFreqDsssJSONObject.get("trans_rate_max"));
        oneLineObject.put("最大发射功率", devFreqDsssJSONObject.get("ave_pow_transmit_max"));
        oneLineObject.put("调制方式", "--");
        workstyle = (String)devFreqDsssJSONObject.get("work_style");
        if (mode == 0 && workstyle.equals("3")) workstyle = "2";
        if (mode == 2 && workstyle.equals("3")) {
            workstyle = "1";
            oneLineObject.put("工作方式", WorkStyleEnums.getMsgWithCode(workstyle).getMsg());
            freqResArray.add(oneLineObject);
            order += 1;
            workstyle = "2";
        }
        oneLineObject.put("状态序号", String.valueOf(order));
        oneLineObject.put("工作方式", WorkStyleEnums.getMsgWithCode(workstyle).getMsg());
        freqResArray.add(oneLineObject);

        return JSON.toJSONString(freqResArray);
    }
}
