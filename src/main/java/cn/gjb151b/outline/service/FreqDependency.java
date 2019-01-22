package cn.gjb151b.outline.service;

import cn.gjb151b.outline.Constants.ExceptionEnums;
import cn.gjb151b.outline.Constants.WorkStyleEnums;
import cn.gjb151b.outline.model.ManageSysDevelop;
import cn.gjb151b.outline.utils.ServiceException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理试验大纲系统中依赖研制要求系统的频率相关数据
 */
class FreqDependency {
    private static Logger logger =Logger.getLogger(FreqDependency.class);

    private static double twoDicimal(double d) {
        return (double) Math.round(d * 100) / 100;
    }

    /**
     * devObject是第一个项目表的数据，后面参数分别指示工作模式
     * （规则详见石墨文档 "X1 X2 X3 说明）
     * 若固定频段和调频全部存在，使用固定频段的数据，不管跳频；由于邮件沟通中没有说直序列扩频，因此这里也先不管
     */
    static String grnerateData(ManageSysDevelop devObject, boolean _receive, boolean _send) throws Exception {
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
                return new outlineFreqModel(this);
            }
            private int order;
            private String modulation;
            private String freq;
            private String workingStyle;
            private String extra;
        }
        JSONArray freqResArray = new JSONArray();

        String devFreqOptionalStr = "[{\"opt_freq_mode\":\"2\",\"opt_freq_range\":\"1\",\"opt_freq_low\":\"10\",\"opt_freq_mid\":\"20\",\"opt_freq_high\":\"30\",\"opt_freq_points\":\"1\",\"opt_work_style\":\"3\",\"opt_install_mode\":\"1\",\"opt_ave_pow_transmit_max\":\"50\",\"opt_port_name\":\"1\",\"opt_modulation_mode_num\":{\"opt_modulation_mode_1\":\"No1 连续可调 收发共用 100db 10-20-30  调制方式1\",\"opt_modulation_mode_2\":\"No1 连续可调 收发共用 100db 10-20-30  调制方式2\"}},{\"opt_freq_mode\":\"1\",\"opt_freq_range\":\"2\",\"opt_freq_low\":\"100\",\"opt_freq_mid\":\"200\",\"opt_freq_high\":\"300\",\"opt_freq_points\":\"\",\"opt_work_style\":\"2\",\"opt_install_mode\":\"1\",\"opt_ave_pow_transmit_max\":\"200\",\"opt_port_name\":\"2\",\"opt_modulation_mode_num\":{\"opt_modulation_mode_1\":\"No2 固定 收 200db 100-200-300  调制方式1\",\"opt_modulation_mode_2\":\"No2 固定 收 200db 100-200-300  调制方式2\"}},{\"opt_freq_mode\":\"1\",\"opt_freq_range\":\"3\",\"opt_freq_low\":\"1\",\"opt_freq_mid\":\"2\",\"opt_freq_high\":\"3\",\"opt_freq_points\":\"\",\"opt_work_style\":\"1\",\"opt_install_mode\":\"1\",\"opt_ave_pow_transmit_max\":\"1000\",\"opt_port_name\":\"3\",\"opt_modulation_mode_num\":{\"opt_modulation_mode_1\":\"No3 固定 收 1000db 1-2-3  调制方式1\",\"opt_modulation_mode_2\":\"\"}}]";
        JSONArray devFreqOptionalArray = JSON.parseArray(devFreqOptionalStr);
        List<outlineFreqModel> outlineFreqModelListResult = new ArrayList<>();
        List<outlineFreqModel> outlineFreqModelList, outlineFreqModelListTemp;

        if (devFreqOptionalArray.size() > 0) {
            // 固定频段
            for (Object devFreqOptionalObject : devFreqOptionalArray) {
                outlineFreqModelList = new ArrayList<>();
                JSONObject devFreqRow = (JSONObject) devFreqOptionalObject;

                try {
                    double freq_low = Double.parseDouble((String) devFreqRow.get("opt_freq_low"));
                    double freq_high = Double.parseDouble((String) devFreqRow.get("opt_freq_high"));
                    outlineFreqModelList.add(new outlineFreqModel(String.valueOf(twoDicimal(freq_low * 1.05))));
                    outlineFreqModelList.add(new outlineFreqModel(String.valueOf(twoDicimal(freq_low + freq_high) / 2)));
                    outlineFreqModelList.add(new outlineFreqModel(String.valueOf(twoDicimal(freq_high * 0.95))));
                } catch (Exception e) {
                    logger.error("opt_freq_low opt_freq_high error");
                    throw new ServiceException(ExceptionEnums.DEV_SYS_INPUT_DATA_ERR);
                }

                // 处理调制方式
                outlineFreqModelListTemp = new ArrayList<>();
                JSONObject modulationMode = (JSONObject)devFreqRow.get("opt_modulation_mode_num");
                String modulation1 = (String)modulationMode.get("opt_modulation_mode_1");
                String modulation2 = (String)modulationMode.get("opt_modulation_mode_2");
                if (Strings.isNullOrEmpty(modulation1) && Strings.isNullOrEmpty(modulation2)){
                    logger.error("opt_modulation_mode_num error");
                    throw new ServiceException(ExceptionEnums.DEV_SYS_INPUT_DATA_ERR);
                }
                for (outlineFreqModel model:outlineFreqModelList) {
                    if (!Strings.isNullOrEmpty(modulation1)){
                        model.modulation = modulation1;
                        outlineFreqModelListTemp.add(model.clone());
                    }
                    if (!Strings.isNullOrEmpty(modulation2)){
                        model.modulation = modulation2;
                        outlineFreqModelListTemp.add(model.clone());
                    }
                }
                if (outlineFreqModelListTemp.size() > 0)
                    outlineFreqModelList = outlineFreqModelListTemp;

                // 处理工作方式（收发）
                outlineFreqModelListTemp = new ArrayList<>();
                String devWorkingStyle = (String)devFreqRow.get("opt_work_style");
                if (devWorkingStyle.equals("1") || devWorkingStyle.equals("2") || devWorkingStyle.equals("3")){
                    for (outlineFreqModel model : outlineFreqModelList) {
                        if (_send && (devWorkingStyle.equals("2") || devWorkingStyle.equals("3"))) {
                            model.workingStyle = "发送";
                            outlineFreqModelListTemp.add(model.clone());
                        }
                        if (_receive && (devWorkingStyle.equals("1") || devWorkingStyle.equals("3"))) {
                            model.workingStyle = "接收";
                            outlineFreqModelListTemp.add(model.clone());
                        }
                    }
                } else {
                    logger.error("opt_work_style error");
                    throw new ServiceException(ExceptionEnums.DEV_SYS_INPUT_DATA_ERR);
                }
                outlineFreqModelList = outlineFreqModelListTemp;
                outlineFreqModelListResult.addAll(outlineFreqModelList);
            }
        } else {
            // 调频低频段
            String devFreqFhLowStr = "{\"freq_range\":\"低频段\",\"freq_low\":\"100\",\"freq_high\":\"200\",\"freq_points\":\"\",\"work_style\":\"2\",\"install_mode\":\"1\",\"ave_pow_transmit_max\":\"100\",\"port_name\":\"4\"}";
            String devFreqFhMidStr = "{\"freq_range\":\"中频段\",\"freq_low\":\"200\",\"freq_high\":\"300\",\"freq_points\":\"\",\"work_style\":\"2\",\"install_mode\":\"1\",\"ave_pow_transmit_max\":\"200\",\"port_name\":\"5\"}";
            String devFreqFhHighStr = "{\"freq_range\":\"高频段\",\"freq_low\":\"300\",\"freq_high\":\"400\",\"freq_points\":\"\",\"work_style\":\"2\",\"install_mode\":\"1\",\"ave_pow_transmit_max\":\"300\",\"port_name\":\"6\"}";
            JSONObject[] devFreqFhObjects = {JSON.parseObject(devFreqFhLowStr), JSON.parseObject(devFreqFhMidStr), JSON.parseObject(devFreqFhHighStr)};

            for (JSONObject object: devFreqFhObjects) {

                // 调频频率取高频和低频的平均数
                outlineFreqModelList = new ArrayList<>();
                try {
                    double freq_low = Double.parseDouble((String) object.get("freq_low"));
                    double freq_high = Double.parseDouble((String) object.get("freq_high"));
                    outlineFreqModelList.add(new outlineFreqModel(String.valueOf(twoDicimal(freq_low + freq_high) / 2)));
                } catch (Exception e) {
                    logger.error("fh freq_low freq_high error");
                    throw new ServiceException(ExceptionEnums.DEV_SYS_INPUT_DATA_ERR);
                }

                // 把跳频所属低中高写在extra中
                for (outlineFreqModel model : outlineFreqModelList) {
                    model.extra = (String) object.get("freq_range");
                }

                // 处理工作方式（收发）
                outlineFreqModelListTemp = new ArrayList<>();
                String devWorkingStyle = (String)object.get("work_style");
                if (devWorkingStyle.equals("1") || devWorkingStyle.equals("2") || devWorkingStyle.equals("3")){
                    for (outlineFreqModel model : outlineFreqModelList) {
                        if (_receive && (devWorkingStyle.equals("1") || devWorkingStyle.equals("3"))) {
                            model.workingStyle = "接收";
                            outlineFreqModelListTemp.add(model.clone());
                        }
                        if (_send && (devWorkingStyle.equals("2") || devWorkingStyle.equals("3"))) {
                            model.workingStyle = "发送";
                            outlineFreqModelListTemp.add(model.clone());
                        }
                    }
                } else {
                    logger.error("fh work_style error");
                    throw new ServiceException(ExceptionEnums.DEV_SYS_INPUT_DATA_ERR);
                }

                // 处理调制方式 当前没有，所以全都一样
                for (outlineFreqModel model : outlineFreqModelList) {
                    model.modulation = "-";
                }

                outlineFreqModelListResult.addAll(outlineFreqModelList);
            }
        }

        // 为生成的数据加上状态序号
        int order = 1;
        for (outlineFreqModel model:outlineFreqModelListResult) {
            model.order = order;
            order++;
        }
        // 转为JsonObject
        for (outlineFreqModel model:outlineFreqModelListResult) {
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
