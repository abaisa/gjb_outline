package cn.gjb151b.outline.service;

import cn.gjb151b.outline.Constants.ItemNamePrefix;
import cn.gjb151b.outline.Constants.PathStoreEnum;
import cn.gjb151b.outline.dao.ManageSysDevelopMapper;
import cn.gjb151b.outline.model.ManageSysDevelop;
import cn.gjb151b.outline.model.ManageSysOutline;
import cn.gjb151b.outline.outlineDao.ManageSysOutlineMapper;
import cn.gjb151b.outline.utils.BaseResponse;
import cn.gjb151b.outline.utils.Itext7.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

@Service
public class PdfService {
    @Resource
    private ManageSysOutlineMapper manageSysOutlineMapper;

    @Resource
    private ManageSysDevelopMapper manageSysDevelopMapper;

    //内容文件：report.pdf 目录文件：catalogReport.pdf 合成文件：newReport.pdf
    public static final String DEST="result/pdf/report.pdf";
    public static final String CATDEST = "result/pdf/catalogReport.pdf";
    public static final String NEWDEST = PathStoreEnum.WINDOWS_PDF_EXPORT_PATH.getValue();
    private static final int sizeRate = 2;

    /** pdf制作函数*/
    public void createPdf(String outlineDevItemId) throws Exception{
        String dest = DEST;
        String catdest = CATDEST;
        String newdest = NEWDEST;

        //    项目对应研制要求的所有信息
        ManageSysDevelop manageSysDevelop = manageSysDevelopMapper.selectByPrimaryKey(outlineDevItemId);

        //    项目对应试验大纲的所有信息
        ManageSysOutline manageSysOutline = manageSysOutlineMapper.selectProjectByDevItemId(outlineDevItemId);

        newdest += manageSysOutline.getOutlineName() + ".pdf";
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        // 内容文件设置
        PdfWriter writer = new PdfWriter(DEST);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // 设置中文字体
        PdfFont font = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H",true);
        // 设置字体大小
        //一级标题字体大小
        int bigSize = 14;
        //二级标题字体大小
        int midSize = 12;
        //三级标题字体大小
        int smallSize = 11;

        // 事件监听器，页眉页脚
        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new MyEventHandler());

        // Map：记录标题对应页数
        Map<String, Integer> catalogs = new TreeMap<String, Integer>(new Comparator<String>() {
            // 按put顺序排序
            public int compare(String o1, String o2) {
                return 1;
            }
        });

        /** 1 -- 5*/
        // 任务来源
        String workSource = manageSysDevelop.getDevSubsysSource();
        // 编制依据和引用文件 （红）
        String proof = manageSysDevelop.getDevSubsysComRef();
        // 试验性质
        JSONObject outlineData3 = JSON.parseObject(manageSysOutline.getOutlineData3());
        String checkProperty = outlineData3.getString("试验性质");
        // 试验目的
        String checkPurpose = outlineData3.getString("试验目的");
        // 试验时间  就是项目实施网络图第一个项目的起始时间~最后一个项目的结束时间
        String outlineData58 = manageSysOutline.getOutlineData58();
        JSONObject outlineDataObject = JSON.parseObject(outlineData58);
        String firstTime = outlineDataObject.getString("试验计划开始时间");
        String lastTime = outlineDataObject.getString("试验计划结束时间");
        String[] firstTimeArray = firstTime.split("-");
        String[] lastTimeArray = lastTime.split("-");
        String resFirstTime = firstTimeArray[0] + "年" + firstTimeArray[1] + "月" + firstTimeArray[2] + "日";
        String resLastTime = lastTimeArray[0] + "年" + lastTimeArray[1] + "月" + lastTimeArray[2] + "日";
        String checkTime = resFirstTime + "~" + resLastTime;
        // 试验地点
        String checkLocation = outlineData3.getString("试验地点");
        // 被试品组成 这里为了填写见图多少，所以需要把照片数量直接拿过来
        int currentPicNumber = 1;
        String outlineData4 = manageSysOutline.getOutlineData4();
        JSONObject outlineData4Object = JSON.parseObject(outlineData4);
        JSONArray img5_1PicList = outlineData4Object.getJSONArray("分系统/设备照片");
        String picNumberPrefix = "5-";
        String checkMakeup = manageSysDevelop.getDevSubsysEqpName() + "主要设备组成如表5-1所示，设备外部照片如" + getPicTitle(picNumberPrefix, currentPicNumber, img5_1PicList.size());
        // 表5-1标题
        String table0501Title = "表5-1\t" + manageSysDevelop.getDevSubsysEqpName() + "主要设备清单";

        // 正文第一页
        LeftParagraph t1 = new LeftParagraph("1\ua0a0\ua0a0任务依据", font, bigSize);
        LeftParagraph t1_1 = new LeftParagraph("1.1\ua0a0\ua0a0任务来源", font, midSize);
        bodyParagraph c1_1 = new bodyParagraph(workSource, font, smallSize);
        LeftParagraph t1_2 = new LeftParagraph("1.2\ua0a0\ua0a0编制依据和引用文件", font, midSize);
//        bodyParagraph t1_2p1 = new bodyParagraph("编制依据：", font, smallSize);
        bodyParagraph c1_2p1 = new bodyParagraph(proof, font, smallSize);
//        bodyParagraph t1_2p2 = new bodyParagraph("引用文件：", font, smallSize);
//        bodyParagraph []c1_2p2 = new bodyParagraph[useDoc.length];
//        for (int i = 0; i < useDoc.length; i++){
//            c1_2p2[i] = new bodyParagraph(useDoc[i], font, smallSize);
//        }
        LeftParagraph t2 = new LeftParagraph("2\ua0a0\ua0a0试验性质", font, bigSize);
        bodyParagraph c2 = new bodyParagraph(checkProperty, font, smallSize);
        LeftParagraph t3 = new LeftParagraph("3\ua0a0\ua0a0试验目的", font, bigSize);
        bodyParagraph c3 = new bodyParagraph(checkPurpose, font, smallSize);
        LeftParagraph t4 = new LeftParagraph("4\ua0a0\ua0a0试验时间和地点", font, bigSize);
        bodyParagraph c4p1 = new bodyParagraph("试验时间："+checkTime, font, smallSize);
        bodyParagraph c4p2 = new bodyParagraph("地      点："+checkLocation, font, smallSize);
        LeftParagraph t5 = new LeftParagraph("5\ua0a0\ua0a0被试品、陪试品的数量及技术状态", font, bigSize);
        LeftParagraph t5_1 = new LeftParagraph("5.1\ua0a0\ua0a0被试品组成和功能", font, midSize);
        LeftParagraph t5_1_1 = new LeftParagraph("5.1.1\ua0a0\ua0a0被试品组成", font, smallSize);
        bodyParagraph c5_1_1 = new bodyParagraph( manageSysDevelop.getDevSubsysEqpName() + checkMakeup, font, smallSize);
        MidParagraph tableTitle5_1 = new MidParagraph(table0501Title, font, smallSize);
        float[] table5_1Width = new float[]{44, 120, 80, 80, 150};
        // 表5-1 主要设备清单
        Table table5_1 = new Table(table5_1Width).setWidthPercent(100);
        table5_1.addCell(new Cell().add(new Paragraph("序号").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_1.addCell(new Cell().add(new Paragraph("设备名称").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_1.addCell(new Cell().add(new Paragraph("型号").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_1.addCell(new Cell().add(new Paragraph("编号/串号").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_1.addCell(new Cell().add(new Paragraph("安装位置").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        String outlineData6 = manageSysOutline.getOutlineData6();
        JSONObject outlineData6Object = JSON.parseObject(outlineData6);
        JSONArray mainEqpList = outlineData6Object.getJSONArray("分系统主要设备清单");
        for (int i = 0; i < mainEqpList.size(); i++) {
            table5_1.addCell(new Cell().add(new Paragraph(String.valueOf(i+1))
                    .setFont(font).setFontSize(smallSize)));
            table5_1.addCell(new Cell().add(new NotNullParagraph(mainEqpList.getJSONObject(i).getString("设备名称"), font, smallSize)));
            table5_1.addCell(new Cell().add(new NotNullParagraph(mainEqpList.getJSONObject(i).getString("型号"), font, smallSize)));
            table5_1.addCell(new Cell().add(new NotNullParagraph(mainEqpList.getJSONObject(i).getString("编号/串号"), font, smallSize)));
            table5_1.addCell(new Cell().add(new NotNullParagraph(mainEqpList.getJSONObject(i).getString("安装位置"), font, smallSize)));
        }
        // 表5-1注释
        String table5_1Text = "被试品软件配置项清单如表5-2，均已通过软件测评。";
        // 表5-2标题
        String table0502Title = "表5-2\t产品软件配置项清单";
        // 表5-2软件版本
        String[] softwareVersion = {"V2.02", "V2.02", "V2.02", "V2.03", "V2.03", "V2.01", "V2.01", "V2.03", "V2.03",
                "V2.03", "V2.02", "V2.02", "V2.03", "V2.03", "V2.01", "V2.01"};
        bodyParagraph table5_1t = new bodyParagraph(table5_1Text, font, smallSize);
        MidParagraph tableTitle5_2 = new MidParagraph(table0502Title, font, smallSize);
        float[] table5_2Width = new float[]{54, 100, 100, 100, 180};
        Table table5_2 = new Table(table5_2Width).setWidthPercent(100);
        table5_2.addCell(new Cell().add(new Paragraph("序号").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_2.addCell(new Cell().add(new Paragraph("软件名称").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_2.addCell(new Cell().add(new Paragraph("软件标识").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_2.addCell(new Cell().add(new Paragraph("软件版本号").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_2.addCell(new Cell().add(new Paragraph("备注").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
//        for (int i = 0; i < 16; i++){
//            for (int j = 0; j < 3; j++){
//                if (j == 0){
//                    table5_2.addCell(new Cell().add(new Paragraph(String.valueOf(i+1))
//                            .setFont(font).setFontSize(smallSize)));
//                }
//                if (j == 2){
//                    table5_2.addCell(new Cell().add(new Paragraph(softwareVersion[i])
//                            .setFont(font).setFontSize(smallSize)));
//                }
//                else{
//                    table5_2.addCell(new Cell().add(new Paragraph("\n").setFont(font).setFontSize(smallSize)));
//                }
//            }
//        }
        JSONArray softwareDeployList = outlineData6Object.getJSONArray("产品软件配置清单");
        for (int i = 0; i < softwareDeployList.size(); i++) {
            table5_2.addCell(new NotNullParagraph(String.valueOf(i + 1), font, smallSize));
            table5_2.addCell(new NotNullParagraph(softwareDeployList.getJSONObject(i).getString("软件名称"), font, smallSize));
            table5_2.addCell(new NotNullParagraph(softwareDeployList.getJSONObject(i).getString("软件标识"), font, smallSize));
            table5_2.addCell(new NotNullParagraph(softwareDeployList.getJSONObject(i).getString("软件版本号"), font, smallSize));
            table5_2.addCell(new NotNullParagraph(softwareDeployList.getJSONObject(i).getString("备注"), font, smallSize));
        }
        // 图5-1标题
        // 5-1分系统/设备照片
        currentPicNumber = 1;
        outlineData4 = manageSysOutline.getOutlineData4();
        outlineData4Object = JSON.parseObject(outlineData4);
        String img5_1UrlPrefix = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() +  "//" + manageSysOutline.getOutlineName() + "//";
        img5_1PicList = outlineData4Object.getJSONArray("分系统/设备照片");
        String img5_1TitleSuffix = manageSysDevelop.getDevSubsysEqpName() + "设备照片";
        // 5-1分系统设备关系图
        JSONArray img5_1RelationPicList = outlineData4Object.getJSONArray("分系统/设备关系图");
        // 5.1.2 主要功能  outline-4 产品功能性能
        String mainFunction = outlineData4Object.getString("产品功能性能");
        // 5.1.3 系统交联关系图 待完成 可以是多张图片 进行多张图片编号
        currentPicNumber += img5_1PicList.size();
        String imgRelationTitleRrefix = "5-";
        String systemRelationImg = manageSysDevelop.getDevName() + "系统交联关系如" + getPicTitle(imgRelationTitleRrefix, currentPicNumber, img5_1RelationPicList.size());
        //5.1.4 被试品电源端口
        String outlineData10 = manageSysOutline.getOutlineData10();
        JSONObject outlineData10Object = JSON.parseObject(outlineData10);
        JSONArray powerPortArray =  outlineData10Object.getJSONArray("电源端口");
        float[] table5_1_4Width = new float[]{60, 60, 60, 60, 60, 60, 60, 60, 60, 60};
        Table table5_1_4 = new Table(table5_1_4Width).setWidthPercent(100);
        table5_1_4.addCell(new Cell().add(new NotNullParagraph("序号", font, smallSize)))
                .setTextAlignment(TextAlignment.CENTER);
        table5_1_4.addCell(new Cell().add(new NotNullParagraph("端口名称或代号", font, smallSize)))
                .setTextAlignment(TextAlignment.CENTER);
        table5_1_4.addCell(new Cell().add(new NotNullParagraph("输入/输出", font, smallSize)))
                .setTextAlignment(TextAlignment.CENTER);
        table5_1_4.addCell(new Cell().add(new NotNullParagraph("外部电源供电", font, smallSize)))
                .setTextAlignment(TextAlignment.CENTER);
        table5_1_4.addCell(new Cell().add(new NotNullParagraph("交流/直流", font, smallSize)))
                .setTextAlignment(TextAlignment.CENTER);
        table5_1_4.addCell(new Cell().add(new NotNullParagraph("两相/三相", font, smallSize)))
                .setTextAlignment(TextAlignment.CENTER);
        table5_1_4.addCell(new Cell().add(new NotNullParagraph("功率（W）", font, smallSize)))
                .setTextAlignment(TextAlignment.CENTER);
        table5_1_4.addCell(new Cell().add(new NotNullParagraph("电压（V）", font, smallSize)))
                .setTextAlignment(TextAlignment.CENTER);
        table5_1_4.addCell(new Cell().add(new NotNullParagraph("电流（A）", font, smallSize)))
                .setTextAlignment(TextAlignment.CENTER);
        table5_1_4.addCell(new Cell().add(new NotNullParagraph("频率（Hz）", font, smallSize)))
                .setTextAlignment(TextAlignment.CENTER);
        for (int i = 0; i < powerPortArray.size(); i++) {
            JSONObject powerPortObject = powerPortArray.getJSONObject(i);
            table5_1_4.addCell(new Cell().add(new NotNullParagraph(String.valueOf(i + 1), font, smallSize)))
                    .setTextAlignment(TextAlignment.CENTER);
            table5_1_4.addCell(new Cell().add(new NotNullParagraph(powerPortObject.getString("端口名称或代号"), font, smallSize)))
                    .setTextAlignment(TextAlignment.CENTER);
            table5_1_4.addCell(new Cell().add(new NotNullParagraph(powerPortObject.getString("输入/输出"), font, smallSize)))
                    .setTextAlignment(TextAlignment.CENTER);
            table5_1_4.addCell(new Cell().add(new NotNullParagraph(powerPortObject.getString("外部电源供电"), font, smallSize)))
                    .setTextAlignment(TextAlignment.CENTER);
            table5_1_4.addCell(new Cell().add(new NotNullParagraph(powerPortObject.getString("交流/直流"), font, smallSize)))
                    .setTextAlignment(TextAlignment.CENTER);
            table5_1_4.addCell(new Cell().add(new NotNullParagraph(powerPortObject.getString("两相/三相"), font, smallSize)))
                    .setTextAlignment(TextAlignment.CENTER);
            table5_1_4.addCell(new Cell().add(new NotNullParagraph(powerPortObject.getString("功率(W)"), font, smallSize)))
                    .setTextAlignment(TextAlignment.CENTER);
            table5_1_4.addCell(new Cell().add(new NotNullParagraph(powerPortObject.getString("电压(V)"), font, smallSize)))
                    .setTextAlignment(TextAlignment.CENTER);
            table5_1_4.addCell(new Cell().add(new NotNullParagraph(powerPortObject.getString("电流(A)"), font, smallSize)))
                    .setTextAlignment(TextAlignment.CENTER);
            table5_1_4.addCell(new Cell().add(new NotNullParagraph(powerPortObject.getString("频率(Hz)"), font, smallSize)))
                    .setTextAlignment(TextAlignment.CENTER);

        }
        // 5.1.5 被试品互联端口
        float[] table5_1_5Width = new float[]{60, 120, 80, 60};
        Table table5_1_5 = new Table(table5_1_5Width).setWidthPercent(100);
        table5_1_5.addCell(new Cell().add(new NotNullParagraph("序号", font, smallSize)))
                .setTextAlignment(TextAlignment.CENTER);
        table5_1_5.addCell(new Cell().add(new NotNullParagraph("端口名称或代号", font, smallSize)))
                .setTextAlignment(TextAlignment.CENTER);
        table5_1_5.addCell(new Cell().add(new NotNullParagraph("端口类型", font, smallSize)))
                .setTextAlignment(TextAlignment.CENTER);
        table5_1_5.addCell(new Cell().add(new NotNullParagraph("高电位线数（根）", font, smallSize)))
                .setTextAlignment(TextAlignment.CENTER);
        JSONArray interconnectPortArray = outlineData10Object.getJSONArray("互联端口");
        for (int i = 0; i < interconnectPortArray.size(); i++) {
            JSONObject interconnectPort = interconnectPortArray.getJSONObject(i);
            table5_1_5.addCell(new Cell().add(new NotNullParagraph(String.valueOf(i + 1), font, smallSize)))
                    .setTextAlignment(TextAlignment.CENTER);
            table5_1_5.addCell(new Cell().add(new NotNullParagraph(interconnectPort.getString("端口名称或代号"), font, smallSize)))
                    .setTextAlignment(TextAlignment.CENTER);
            table5_1_5.addCell(new Cell().add(new NotNullParagraph(interconnectPort.getString("端口类型"), font, smallSize)))
                    .setTextAlignment(TextAlignment.CENTER);
            table5_1_5.addCell(new Cell().add(new NotNullParagraph(interconnectPort.getString("高电位线数（根）"), font, smallSize)))
                    .setTextAlignment(TextAlignment.CENTER);

        }
        // 图5-2标题
        String img5_2TitleSuffix = manageSysDevelop.getDevName() + "系统交联图";
        //添加5.1.6 被试品连接电缆信息
        MidParagraph table5_3Title = new MidParagraph("表5-5\t" + "被试品电缆信息", font, smallSize);
        float[] table5_3Width = new float[]{60, 80, 80, 80, 60, 60, 60, 60, 60, 60, 60, 120};
        Table table5_3 = new Table(table5_3Width).setWidthPercent(100);
        table5_3.addCell(new Cell().add(new NotNullParagraph("连接电缆ID", font, smallSize))
                .setTextAlignment(TextAlignment.CENTER));
        table5_3.addCell(new Cell().add(new NotNullParagraph("名称", font, smallSize))
                .setTextAlignment(TextAlignment.CENTER));
        table5_3.addCell(new Cell().add(new NotNullParagraph("型号", font, smallSize))
                .setTextAlignment(TextAlignment.CENTER));
        table5_3.addCell(new Cell().add(new NotNullParagraph("生产厂", font, smallSize))
                .setTextAlignment(TextAlignment.CENTER));
        table5_3.addCell(new Cell().add(new NotNullParagraph("长度", font, smallSize))
                .setTextAlignment(TextAlignment.CENTER));
        table5_3.addCell(new Cell().add(new NotNullParagraph("连接端口ID", font, smallSize))
                .setTextAlignment(TextAlignment.CENTER));
        table5_3.addCell(new Cell().add(new NotNullParagraph("类型", font, smallSize))
                .setTextAlignment(TextAlignment.CENTER));
        table5_3.addCell(new Cell().add(new NotNullParagraph("是否屏蔽", font, smallSize))
                .setTextAlignment(TextAlignment.CENTER));
        table5_3.addCell(new Cell().add(new NotNullParagraph("是否双绞", font, smallSize))
                .setTextAlignment(TextAlignment.CENTER));
        table5_3.addCell(new Cell().add(new NotNullParagraph("接地方式", font, smallSize))
                .setTextAlignment(TextAlignment.CENTER));
        table5_3.addCell(new Cell().add(new NotNullParagraph("是否摸实", font, smallSize))
                .setTextAlignment(TextAlignment.CENTER));
        table5_3.addCell(new Cell().add(new NotNullParagraph("备注", font, smallSize))
                .setTextAlignment(TextAlignment.CENTER));
        JSONArray testElecInfoArray = outlineData6Object.getJSONArray("受试分系统/设备连接电缆信息");
        for (int i = 0; i < testElecInfoArray.size(); i++) {
            JSONObject testElecObject = testElecInfoArray.getJSONObject(i);
            table5_3.addCell(new Cell().add(new NotNullParagraph(testElecObject.getString("连接电缆ID"), font, smallSize))
                    .setTextAlignment(TextAlignment.CENTER));
            table5_3.addCell(new Cell().add(new NotNullParagraph(testElecObject.getString("名称"), font, smallSize))
                    .setTextAlignment(TextAlignment.CENTER));
            table5_3.addCell(new Cell().add(new NotNullParagraph(testElecObject.getString("型号"), font, smallSize))
                    .setTextAlignment(TextAlignment.CENTER));
            table5_3.addCell(new Cell().add(new NotNullParagraph(testElecObject.getString("生产厂"), font, smallSize))
                    .setTextAlignment(TextAlignment.CENTER));
            table5_3.addCell(new Cell().add(new NotNullParagraph(testElecObject.getString("长度"), font, smallSize))
                    .setTextAlignment(TextAlignment.CENTER));
            table5_3.addCell(new Cell().add(new NotNullParagraph(testElecObject.getString("连接端口ID"), font, smallSize))
                    .setTextAlignment(TextAlignment.CENTER));
            table5_3.addCell(new Cell().add(new NotNullParagraph(testElecObject.getString("类型"), font, smallSize))
                    .setTextAlignment(TextAlignment.CENTER));
            table5_3.addCell(new Cell().add(new NotNullParagraph(testElecObject.getString("是否屏蔽"), font, smallSize))
                    .setTextAlignment(TextAlignment.CENTER));
            table5_3.addCell(new Cell().add(new NotNullParagraph(testElecObject.getString("是否双绞"), font, smallSize))
                    .setTextAlignment(TextAlignment.CENTER));
            table5_3.addCell(new Cell().add(new NotNullParagraph(testElecObject.getString("接地方式"), font, smallSize))
                    .setTextAlignment(TextAlignment.CENTER));
            table5_3.addCell(new Cell().add(new NotNullParagraph(testElecObject.getString("是否摸实"), font, smallSize))
                    .setTextAlignment(TextAlignment.CENTER));
            table5_3.addCell(new Cell().add(new NotNullParagraph(testElecObject.getString("备注"), font, smallSize))
                    .setTextAlignment(TextAlignment.CENTER));
        }
        // 5.1.7 被试品技术状态 outline-7 前两个状态
        String outlineData7 = manageSysOutline.getOutlineData7();
        JSONObject outlineData7Object = JSON.parseObject(outlineData7);
        //受试分系统/设备技术状态文字内容
        String techState = "受试分系统/设备技术状态:" + "\ua0a0" + outlineData7Object.getString("受试分系统/设备技术状态");
        //软件技术状态文字内容
        String softState = "软件技术状态:" + "\ua0a0" + outlineData7Object.getString("软件技术状态");
        // 5.2 被试品工作状态
        String install = "被试品工作状态详见第6章";
//        MidParagraph img5_1 = new MidParagraph(img5_1Title, font, smallSize);
        LeftParagraph t5_1_2 = new LeftParagraph("5.1.2 主要功能", font,smallSize);
        bodyParagraph c5_1_2 = new bodyParagraph(mainFunction, font, smallSize);
        LeftParagraph t5_1_3 = new LeftParagraph("5.1.3 系统交联关系图", font, smallSize);
        bodyParagraph c5_1_3 = new bodyParagraph(systemRelationImg, font, smallSize);
        LeftParagraph t5_1_4 = new LeftParagraph("5.1.4　被试品电源端口", font, smallSize);
        LeftParagraph t5_1_5 = new LeftParagraph("5.1.5　被试品互联端口", font, smallSize);
        LeftParagraph t5_1_6 = new LeftParagraph("5.1.6　被试品连接电缆信息", font, smallSize);
        LeftParagraph t5_1_7 = new LeftParagraph("5.1.7　被试品技术状态", font, smallSize);
        bodyParagraph c5_1_7p1 = new bodyParagraph(techState, font, smallSize);
        bodyParagraph softStatePara = new bodyParagraph(softState, font, smallSize);
        bodyParagraph c5_1_7p2 = new bodyParagraph("软件均已通过软件测评，被试品软件清单见表5-2。", font, smallSize);
        LeftParagraph t5_2 = new LeftParagraph("5.2　被试品工作状态", font, midSize);
        bodyParagraph c5_2 = new bodyParagraph(install, font, smallSize);
        LeftParagraph t5_3 = new LeftParagraph("5.3　陪试品的数量和技术状态", font, midSize);
        bodyParagraph c5_3 = new bodyParagraph("配试设备数量及编号见表5-6所示，配试设备参试应经检验合格或检定合格。",
                font, smallSize);

        //5_3 配试设备清单
        JSONArray accomponyTestEqpList = outlineData7Object.getJSONArray("陪试设备清单");
        MidParagraph table5_4Title = new MidParagraph("表5-6\t配试设备数量及编号汇总表", font, smallSize);
        float[] table5_4Width = new float[]{60, 80, 80, 120, 80, 120};
        Table table5_4 = new Table(table5_4Width).setWidthPercent(100);
        table5_4.addCell(new Cell().add(new Paragraph("序号").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_4.addCell(new Cell().add(new Paragraph("设备名称").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_4.addCell(new Cell().add(new Paragraph("参考型号").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_4.addCell(new Cell().add(new Paragraph("主要功能").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_4.addCell(new Cell().add(new Paragraph("检验/检定状态").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_4.addCell(new Cell().add(new Paragraph("备注").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        for (int i = 0; i < accomponyTestEqpList.size(); i++){
            table5_4.addCell(new Cell().add(new NotNullParagraph(accomponyTestEqpList.getJSONObject(i).getString("序号"), font, smallSize)));
            table5_4.addCell(new Cell().add(new NotNullParagraph(accomponyTestEqpList.getJSONObject(i).getString("设备名称"), font, smallSize)));
            table5_4.addCell(new Cell().add(new NotNullParagraph(accomponyTestEqpList.getJSONObject(i).getString("参考型号"), font, smallSize)));
            table5_4.addCell(new Cell().add(new NotNullParagraph(accomponyTestEqpList.getJSONObject(i).getString("主要功能"), font, smallSize)));
            table5_4.addCell(new Cell().add(new NotNullParagraph(accomponyTestEqpList.getJSONObject(i).getString("检验/检定状态"), font, smallSize)));
            table5_4.addCell(new Cell().add(new NotNullParagraph(accomponyTestEqpList.getJSONObject(i).getString("备注"), font, smallSize)));
        }

        document.add(t1);
        catalogs.put("1\ua0a0\ua0a0任务依据", pdf.getNumberOfPages());
        document.add(t1_1);
        catalogs.put("1.1\ua0a0\ua0a0任务来源", pdf.getNumberOfPages());
        document.add(c1_1);
        document.add(t1_2);
        catalogs.put("1.2\ua0a0\ua0a0编制依据和引用文件", pdf.getNumberOfPages());
        document.add(c1_2p1);
        document.add(t2);
        catalogs.put("2\ua0a0\ua0a0试验性质", pdf.getNumberOfPages());
        document.add(c2);
        document.add(t3);
        catalogs.put("3\ua0a0\ua0a0试验目的", pdf.getNumberOfPages());
        document.add(c3);
        document.add(t4);
        catalogs.put("4\ua0a0\ua0a0试验时间和地点", pdf.getNumberOfPages());
        document.add(c4p1);
        document.add(c4p2);
        document.add(t5);
        catalogs.put("5\ua0a0\ua0a0被试品、陪试品的数量及技术状态", pdf.getNumberOfPages());
        document.add(t5_1);
        catalogs.put("5.1\ua0a0\ua0a0被试品组成和功能", pdf.getNumberOfPages());
        document.add(t5_1_1);
        catalogs.put("5.1.1 \ua0a0\ua0a0被试品组成", pdf.getNumberOfPages());
        document.add(c5_1_1);
        document.add(tableTitle5_1);
        document.add(table5_1);
        document.add(table5_1t);
        document.add(tableTitle5_2);
        document.add(table5_2);
        //添加5-1相关图片 需要进行图片编号 并且将图片名称写在图片下方
        currentPicNumber = 1;
        for(int i = 0; i < img5_1PicList.size(); i++) {
            Image image5_1EqpPic = new Image(ImageDataFactory.create(img5_1UrlPrefix + img5_1PicList.getString(i))).
                    scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
            Paragraph image5_1 = new Paragraph().add(image5_1EqpPic).setMarginLeft(PageSize.A4.getWidth() / (16 / sizeRate));
            Paragraph image5_1EqpPicName = new MidParagraph( "图5-" + currentPicNumber + "\t" + img5_1TitleSuffix, font, smallSize);
            document.add(image5_1);
            document.add(image5_1EqpPicName);
            currentPicNumber++;
        }
        document.add(t5_1_2);
        catalogs.put("5.1.2\ua0a0\ua0a0主要功能", pdf.getNumberOfPages());
        document.add(c5_1_2);
        document.add(t5_1_3);
        catalogs.put("5.1.3\ua0a0\ua0a0系统交联关系图", pdf.getNumberOfPages());
        document.add(c5_1_3);
        breakline(document, 1);
        //添加5-2 分系统/设备关系图
        for (int i = 0; i < img5_1RelationPicList.size(); i++) {
            Image image5_1RelationPic = new Image(ImageDataFactory.create(img5_1UrlPrefix + img5_1RelationPicList.getString(i)));
            Paragraph image5_2EqpPicName = new MidParagraph("图5-" + currentPicNumber + "\t" + img5_2TitleSuffix, font, smallSize);
            document.add(image5_1RelationPic);
            document.add(image5_2EqpPicName);
        }
        document.add(t5_1_4);
        catalogs.put("5.1.4\ua0a0\ua0a0被试品电源端口", pdf.getNumberOfPages());
        document.add(new MidParagraph("表5-3\t" + "被试品电源端口", font, smallSize));
        document.add(table5_1_4);
        document.add(t5_1_5);
        catalogs.put("5.1.5\ua0a0\ua0a0被试品互联端口", pdf.getNumberOfPages());
        document.add(new MidParagraph("表5-4\t" + "被试品互联端口", font, smallSize));
        document.add(table5_1_5);
        document.add(t5_1_6);
        catalogs.put("5.1.6\ua0a0\ua0a0被试品电缆信息", pdf.getNumberOfPages());
        document.add(table5_3Title);
        document.add(table5_3);
        document.add(t5_1_7);
        catalogs.put("5.1.7\ua0a0\ua0a0被试品技术状态", pdf.getNumberOfPages());
        document.add(c5_1_7p1);
        document.add(softStatePara);
        document.add(c5_1_7p2);
        document.add(t5_2);
        catalogs.put("5.2\ua0a0\ua0a0被试品装机位置及方向", pdf.getNumberOfPages());
        document.add(c5_2);
        document.add(t5_3);
        catalogs.put("5.3\ua0a0\ua0a0陪试品的数量和技术状态", pdf.getNumberOfPages());
        document.add(c5_3);
        document.add(table5_4Title);
        document.add(table5_4);

        /** 6, 6.1*/
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        LeftParagraph t6 = new LeftParagraph("6　试验项目、方法及要求", font, smallSize);
        LeftParagraph t6_1 = new LeftParagraph("6.1　试验项目及限值要求", font, smallSize);
        bodyParagraph c6_1 = new bodyParagraph("本次设计鉴定电磁兼容性试验项目及限值要求见表6-1。", font, smallSize);
        MidParagraph table6_1Title = new MidParagraph("表6-1\t电磁兼容试验项目及限值要求", font, smallSize);
//         试验项目
//        String[] checkprojects = {"CE102", "CE106", "CS101", "CS114", "CS115", "CS116", "RE102", "RS103"};
//         试验内容
//        String[] checkCotents = {"10kHz～10MHz电源线传导发射", "10kHz～40GHz天线端子传导发射", "25Hz～50kHz电源线传导敏感度",
//                "10kHz～400MHz电缆束注入传导敏感度", "电缆束注入脉冲激励传导敏感度", "10kHz～100MHz电缆和电源线阻尼正弦瞬变传导敏感度",
//                "2MHz～18GHz电场辐射发射", "2MHz～18GHz电场辐射敏感度"};
//         限值要求
//        String[] limitReq = {"按GJB151A/152A-1997要求，限值采用AC220V供电设备限值要求。",
//                "按GJB151A/152A-1997要求，限值采用GJB151A-1997中的5.3.3.2条规定限值。",
//                "按GJB151A/152A-1997要求，极限值采用图CS101-1中的曲线1要求。",
//                "按GJB151A/152A-1997要求，极限值采用图CS114-1中的曲线2。",
//                "按GJB151A/152A-1997要求，注入电流幅度为5A。",
//                "按GJB151A/152A-1997要求，极限值采用图CS116-2中曲线要求，Imax=5A。",
//                "按GJB151A/152A-1997要求，限值采用图RE102-3中海军（固定的）和空军的限值要求。",
//                "按GJB151A/152A-1997要求，采用地面设备极限值要求中的频段2MHz～1GHz，10V/m；频段1GHz～18GHz，50V/m。"};
        float[] table6_1Width = new float[]{35, 70, 210, 280};
        Table table6_1 = new Table(table6_1Width).setWidthPercent(100);
        table6_1.addCell(new Cell().add(new Paragraph("序号").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        table6_1.addCell(new Cell().add(new Paragraph("试验项目").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        table6_1.addCell(new Cell().add(new Paragraph("试验内容").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        table6_1.addCell(new Cell().add(new Paragraph("限值要求").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        JSONArray projectArray = JSONArray.parseArray(manageSysDevelop.getProjectList());
        ArrayList<String> projectList = new ArrayList<>();
        for (int i = 0; i < projectArray.size(); i++) {
            projectList.add(projectArray.getString(i));
        }
        Collections.sort(projectList);
        //从第一个系统中取出适用项目
        currentPicNumber = 1;
        for (int i = 0; i < projectList.size(); i++) {
            String project = projectList.get(i);
            table6_1.addCell(new Cell().add(new NotNullParagraph(String.valueOf(i + 1), font, smallSize).
                    setTextAlignment(TextAlignment.CENTER)));
            switch (project) {
                case "CE101" :
                    table6_1.addCell(new Cell().add(new NotNullParagraph("CE101", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("25Hz~10kHz\ua0a0\ua0a0电源线传导发射", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("按GJB151B-2013要求，限值要求见图6-" + currentPicNumber, font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    currentPicNumber += 2;
                    break;
                case "CE102" :
                    table6_1.addCell(new Cell().add(new NotNullParagraph("CE102", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("10kHz~10MHz\ua0a0\ua0a0电源线传导发射", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("按GJB151B-2013要求，限值要求见图6-" + currentPicNumber, font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    currentPicNumber += 2;
                    break;
                case "CE106" :
                    table6_1.addCell(new Cell().add(new NotNullParagraph("CE106", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("10kHz~40GHz\ua0a0\ua0a0天线端口传导发射", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("按GJB151B-2013要求，限值要求见CE106试验项目限值要求", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    currentPicNumber++;
                    break;
                case "CE107" :
                    table6_1.addCell(new Cell().add(new NotNullParagraph("CE107", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("电源线尖峰信号（时域）传导发射", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("按GJB151B-2013要求，限值要求见CE107试验项目限值要求", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    currentPicNumber++;
                    break;
                case "CS101" :
                    table6_1.addCell(new Cell().add(new NotNullParagraph("CS101", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("25Hz~150kHz\ua0a0\ua0a0电源线传导敏感度", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    int nextPicNumber = currentPicNumber + 1;
                    table6_1.addCell(new Cell().add(new NotNullParagraph("按GJB151B-2013要求，限值要求见图6-" + currentPicNumber + "和图6-" + nextPicNumber, font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    currentPicNumber += 3;
                    break;
                case "CS102" :
                    table6_1.addCell(new Cell().add(new NotNullParagraph("CS102", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("25Hz~50kHz\ua0a0\ua0a0地线传导敏感度", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("按GJB151B-2013要求，限值要求见CS102试验项目限值要求", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    currentPicNumber++;
                    break;
                case "CS103" :
                    table6_1.addCell(new Cell().add(new NotNullParagraph("CS103", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("15kHz~10GHz\ua0a0\ua0a0天线端口互调传导敏感度", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("按GJB151B-2013要求，限值要求见CS103试验项目限值要求", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    currentPicNumber++;
                    break;
                case "CS104" :
                    table6_1.addCell(new Cell().add(new NotNullParagraph("CS104", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("25Hz~20GHz\ua0a0\ua0a0天线端口无用信号抑制传导敏感度", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("按GJB151B-2013要求，限值要求见CS104试验项目限值要求", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    currentPicNumber++;
                    break;
                case "CS105" :
                    table6_1.addCell(new Cell().add(new NotNullParagraph("CS105", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("25Hz~20GHz\ua0a0\ua0a0天线端口交调传导敏感度", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("按GJB151B-2013要求，限值要求见CS105试验项目限值要求", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    currentPicNumber++;
                    break;
                case "CS106" :
                    table6_1.addCell(new Cell().add(new NotNullParagraph("CS106", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("电源线尖峰信号传导敏感度", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("按GJB151B-2013要求，限值要求见CS106试验项目限值要求", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    currentPicNumber++;
                    break;
                case "CS109" :
                    table6_1.addCell(new Cell().add(new NotNullParagraph("CS109", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("50Hz~100kHz\ua0a0\ua0a0壳体电流传导敏感度", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("按GJB151B-2013要求，限值要求见图6-" + currentPicNumber, font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    currentPicNumber += 2;
                    break;
                case "CS112" :
                    table6_1.addCell(new Cell().add(new NotNullParagraph("CS112", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("静电放电敏感度", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("按GJB151B-2013要求，限值要求见CS112试验项目限值要求", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    currentPicNumber++;
                    break;
                case "CS114" :
                    table6_1.addCell(new Cell().add(new NotNullParagraph("CS114", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("4kHz~400MHz\ua0a0\ua0a0电缆束输入传导敏感度", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("按GJB151B-2013要求，限值要求见图6-" + currentPicNumber, font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    currentPicNumber += 2;
                    break;
                case "CS115" :
                    table6_1.addCell(new Cell().add(new NotNullParagraph("CS115", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("电缆束注入脉冲激励传导敏感度", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("按GJB151B-2013要求，限值要求见CS115试验项目限值要求和图6-" + currentPicNumber, font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    currentPicNumber += 2;
                    break;
                case "CS116" :
                    table6_1.addCell(new Cell().add(new NotNullParagraph("CS116", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("10kHz~100MHz\ua0a0\ua0a0电缆和电源线阻尼正弦瞬态传导敏感度", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("按GJB151B-2013要求，限值要求见CS116试验项目限值要求和图6-" + currentPicNumber, font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    currentPicNumber += 2;
                    break;
                case "RE101" :
                    table6_1.addCell(new Cell().add(new NotNullParagraph("RE101", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("25Hz~100kHz\ua0a0\ua0a0磁场辐射发射", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("按GJB151B-2013要求，限值要求见图6-" + currentPicNumber, font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    currentPicNumber += 2;
                    break;
                case "RE102" :
                    table6_1.addCell(new Cell().add(new NotNullParagraph("RE102", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("10kHz~18GHz\ua0a0\ua0a0电场辐射发射", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("按GJB151B-2013要求，限值要求见图6-" + currentPicNumber, font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    currentPicNumber += 2;
                    break;
                case "RE103" :
                    table6_1.addCell(new Cell().add(new NotNullParagraph("RE103", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("10kHz~40GHz\ua0a0\ua0a0天线谐波和乱真输出辐射发射", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("按GJB151B-2013要求，限值要求见RE103试验项目限值要求", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    currentPicNumber++;
                    break;
                case "RS101" :
                    table6_1.addCell(new Cell().add(new NotNullParagraph("RS101", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("25Hz~100kHz\ua0a0\ua0a0磁场辐射敏感度", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("按GJB151B-2013要求，限值要求见图6-" + currentPicNumber, font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    currentPicNumber += 2;
                    break;
                case "RS103" :
                    table6_1.addCell(new Cell().add(new NotNullParagraph("RS103", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("10kHz~40GHz\ua0a0\ua0a0电场辐射敏感度", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("按GJB151B-2013要求，限值要求见图6-" + currentPicNumber, font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    currentPicNumber += 2;
                    break;
                case "RS105" :
                    table6_1.addCell(new Cell().add(new NotNullParagraph("RS105", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("瞬态电磁场辐射敏感度", font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    table6_1.addCell(new Cell().add(new NotNullParagraph("按GJB151B-2013要求，限值要求见图6-" + currentPicNumber, font, smallSize).
                            setTextAlignment(TextAlignment.CENTER)));
                    currentPicNumber += 2;
                    break;
                    default :
                        break;
            }

        }

        document.add(t6);
        catalogs.put("6\ua0a0\ua0a0试验项目、方法及要求", pdf.getNumberOfPages());
        document.add(t6_1);
        catalogs.put("6.1\ua0a0\ua0a0试验项目及名称", pdf.getNumberOfPages());
        document.add(c6_1);
        document.add(table6_1Title);
        document.add(table6_1);

        /** 6.2 -- 6.5.1.4*/
        String outlineData8 = manageSysOutline.getOutlineData8();
        JSONObject outlineData8Object = JSON.parseObject(outlineData8);
        // 6.2 试验环境与条件要求
        String envAndCondition = "本项试验环境条件要求如下：\n" +
                "a)\t" + "温度(°C):\ua0a0" + outlineData8Object.getString("温度(°C)") + "\n" +
                "b)\t" + "相对湿度(%):\ua0a0" + outlineData8Object.getString("相对湿度(%)") + "\n" +
                "c)\t" + "大气压力(kPa):\ua0a0" + outlineData8Object.getString("大气压力(kPa)") + "\n" +
                "d)\t" + "电磁环境:\ua0a0" + outlineData8Object.getString("电磁环境") + "\n";
        // CS112试验环境与条件要求
        JSONObject CS112EnvAndConditionObject = outlineData8Object.getJSONObject("静电放电敏感度试验环境要求");
        String CS112EnvAndCondition = "CS112试验环境条件要求如下：\n" +
                "a)\t" + "温度(°C):\ua0a0" + CS112EnvAndConditionObject.getString("温度") + "\n" +
                "b)\t" + "相对湿度(%):\ua0a0" + CS112EnvAndConditionObject.getString("相对湿度") + "\n" +
                "c)\t" + "大气压力(kPa):\ua0a0" + CS112EnvAndConditionObject.getString("大气压力");
        // 监测点可能有多个，这里进行绘制表格
        float[] table6_2Width = new float[]{60, 80, 80, 120, 60};
        Table table6_2 = new Table(table6_2Width).setWidthPercent(100);
        table6_2.addCell(new Cell().add(new NotNullParagraph("序号", font, smallSize).
                setTextAlignment(TextAlignment.CENTER)));
        table6_2.addCell(new Cell().add(new NotNullParagraph("监测点描述", font, smallSize).
                setTextAlignment(TextAlignment.CENTER)));
        table6_2.addCell(new Cell().add(new NotNullParagraph("监测手段", font, smallSize).
                setTextAlignment(TextAlignment.CENTER)));
        table6_2.addCell(new Cell().add(new NotNullParagraph("评定准则", font, smallSize).
                setTextAlignment(TextAlignment.CENTER)));
        table6_2.addCell(new Cell().add(new NotNullParagraph("驻留时间（s）", font, smallSize).
                setTextAlignment(TextAlignment.CENTER)));
        String outlineData11 = manageSysOutline.getOutlineData11();
        JSONObject outlineData11Object = JSON.parseObject(outlineData11);
        JSONArray monitorPointArray = outlineData11Object.getJSONArray("敏感度判据及检测方法");
        for (int i = 0; i < monitorPointArray.size(); i++) {
            JSONObject monitorPointObject = monitorPointArray.getJSONObject(i);
            table6_2.addCell(new Cell().add(new NotNullParagraph(String.valueOf(i + 1), font, smallSize).
                    setTextAlignment(TextAlignment.CENTER)));
            table6_2.addCell(new Cell().add(new NotNullParagraph(monitorPointObject.getString("监测点描述"), font, smallSize).
                    setTextAlignment(TextAlignment.CENTER)));
            table6_2.addCell(new Cell().add(new NotNullParagraph(monitorPointObject.getString("监测手段"), font, smallSize).
                    setTextAlignment(TextAlignment.CENTER)));
            table6_2.addCell(new Cell().add(new NotNullParagraph(monitorPointObject.getString("评定准则"), font, smallSize).
                    setTextAlignment(TextAlignment.CENTER)));
            table6_2.addCell(new Cell().add(new NotNullParagraph(monitorPointObject.getString("驻留时间(s)"), font, smallSize).
                    setTextAlignment(TextAlignment.CENTER)));
        }

        LeftParagraph t6_2 = new LeftParagraph("6.2　试验环境与条件要求", font, midSize);
        bodyParagraph c6_2 = new bodyParagraph(envAndCondition, font, smallSize);
        bodyParagraph CS112envAndConditionPara = new bodyParagraph(CS112EnvAndCondition, font, smallSize);
        LeftParagraph t6_3 = new LeftParagraph("6.3　发射及敏感度测试参数设置", font, midSize);
        LeftParagraph t6_4 = new LeftParagraph("6.4　敏感度判据及监测方法", font, midSize);
        LeftParagraph t6_5 = new LeftParagraph("6.5　试验方法及要求", font, midSize);

        /** 红色部分*/
        document.add(t6_2);
        catalogs.put("6.2\ua0a0\ua0a0试验环境与条件要求", pdf.getNumberOfPages());
        document.add(c6_2);
        document.add(CS112envAndConditionPara);
        document.add(t6_3);
        catalogs.put("6.3\ua0a0\ua0a0发射及敏感度测试参数设置", pdf.getNumberOfPages());
        document.add(t6_4);
        catalogs.put("6.4\ua0a0\ua0a0敏感度判据及监测方法", pdf.getNumberOfPages());
        document.add(table6_2);
//        document.add(c6_4p1);
//        document.add(c6_4p2);
//        document.add(c6_4p3);
//        document.add(c6_4p4);
        document.add(t6_5);
        catalogs.put("6.5\ua0a0\ua0a0试验方法及要求", pdf.getNumberOfPages());

        //21个项目的试验方法与要求 currentPicNumber用于表的编号初始为1
        currentPicNumber = 1;
        for (int i = 0; i < projectList.size(); i++) {
            //目录前缀 比如第一个项目的目录前缀就是6.5.1 依次向后顺延
            String projectCatalogPrefix = "6.5." + String.valueOf(i + 1);
            String projectName = projectList.get(i);
            switch (projectName) {
                case "CE101" :
                    String outlineData14 = manageSysOutline.getOutlineData14();
                    JSONObject outlineData14Object = JSON.parseObject(outlineData14);
                    //试验项目名
                    String CE101ProjectName = projectName + "试验";
                    //CE101试验内容  限值要求: 本大纲限值要求图片编号 并添加限值图片
                    JSONArray CE101Array = new JSONArray();
                    CE101Array.add("CE101");
                    String CE101TestContent = "试验内容：\ua0a0\ua0a025Hz~10kHz\ua0a0\ua0a0电源线传导发射。" + "\n" +
                            "限值要求：见图6-" + currentPicNumber;
                    JSONObject CE101ValueContentObject = new GeneratePdfLimitService().getProjectLimit(CE101Array, currentPicNumber, manageSysDevelop);
                    String CE101ValuePicPath = CE101ValueContentObject.getString("valuePicPath");
                    String CE101ValuePicTitle = CE101ValueContentObject.getString("valuePicTitle");
                    Paragraph CE101ValuePicTitlePara = new MidParagraph(CE101ValuePicTitle, font, smallSize);
                    Image CE101ValuePic = new Image(ImageDataFactory.create(CE101ValuePicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    //CE101试验目的
                    String CE101TestTarget = outlineData14Object.getString("试验目的");
                    //CE101试验状态及测试位置
                    String CE101TestStateAndSite = "";
                    JSONArray CE101ValidTestPortArray = getTestPortAndWorkStatus(outlineData14, 14);
                    for (int j = 0; j < CE101ValidTestPortArray.size(); j++) {
                        JSONObject validTestPortObject = CE101ValidTestPortArray.getJSONObject(j);
                        CE101TestStateAndSite = CE101TestStateAndSite + "试验端口名称：" + validTestPortObject.getString("试验端口") + "\n";
                        CE101TestStateAndSite = CE101TestStateAndSite + "备注：" + validTestPortObject.getString("备注") + "\n";
                        if (validTestPortObject.containsKey("工作状态")) {
                            JSONObject allWorkStatusObject = validTestPortObject.getJSONObject("工作状态");
                            for (int k = 0; k < allWorkStatusObject.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject workStatusObject = allWorkStatusObject.getJSONObject("工作状态" + workStatusNumber);
                                if (workStatusObject.containsKey("状态是否实施")) {
                                    if ("是".equals(workStatusObject.getString("状态是否实施"))) {
                                        CE101TestStateAndSite = CE101TestStateAndSite + "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                        CE101TestStateAndSite = CE101TestStateAndSite + "工作状态描述：" + workStatusObject.getString("工作状态描述") + "\n";
                                    }
                                }
                            }
                        }
                        CE101TestStateAndSite = CE101TestStateAndSite + "\n\n";
                    }
                    //CE101试验方法  试验方法 + 试验配置图 这里如果没有修改方法 就用标准的按照。。。，和GJB151BA-1997方法对XXX开展实验，图形也放修改过的（如果修改）
                    String CE101TestMethod = "";
                    String CE101TestPicTitle = "图6-" + currentPicNumber + "\ua0a0\ua0a0" + "CE101试验配置图";
                    String CE101TestPicPath = "";
                    if (outlineData14Object.getString("项目试验图") != null && ! "".equals(outlineData14Object.getString("项目试验图"))) {
                        CE101TestPicPath = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + manageSysDevelop.getDevName() + "//" +
                                outlineData14Object.getString("项目试验图");
                    } else {
                        CE101TestPicPath = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "CE101StandardTestPic.png";
                    }
                    Image CE101TestPic = new Image(ImageDataFactory.create(CE101TestPicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    if (outlineData14Object.getString("修改方法") != null && ! "".equals(outlineData14Object.getString("修改方法"))) {
                        CE101TestMethod = outlineData14Object.getString("修改方法");
                    } else {
                        CE101TestMethod = "按照" + CE101TestPicTitle + "," + "\ua0a0\ua0a0" + "和GJB151B-2013方法对CE101开展试验";
                    }
                    Paragraph CE101TestPicTitlePara = new MidParagraph(CE101TestPicTitle, font, smallSize);

                    //CE101数据处理方法
                    String CE101DataWorkMethod = outlineData14Object.getString("数据处理方法");
                    //CE101测试结果评定准则
                    String CE101TestResultAssess = outlineData14Object.getString("结果评定准则");

                    Paragraph CE101ProjectNamePara = new LeftParagraph(projectCatalogPrefix + "\ua0a0\ua0a0" + CE101ProjectName,
                            font, smallSize);
                    Paragraph CE101TestContentPara = new LeftParagraph(projectCatalogPrefix + ".1" + "\ua0a0\ua0a0" + "CE101试验内容" +
                            "\n\n" + CE101TestContent, font, smallSize);
                    Paragraph CE101TestTargetPara = new LeftParagraph(projectCatalogPrefix + ".2" + "\ua0a0\ua0a0" + "CE101试验目的", font, smallSize);
                    Paragraph CE101TestTargetBodyPara = new bodyParagraph(CE101TestTarget, font, smallSize);
                    Paragraph CE101TestStateAndSitePara = new LeftParagraph(projectCatalogPrefix + ".3" + "\ua0a0\ua0a0" + "CE101试验状态及测试位置" +
                            "\n\n" + CE101TestStateAndSite, font, smallSize);
                    Paragraph CE101TestMethodPara = new LeftParagraph(projectCatalogPrefix + ".4" + "\ua0a0\ua0a0" + "CE101试验方法", font, smallSize);
                    Paragraph CE101TestMethodBodyPara = new bodyParagraph(CE101TestMethod, font, smallSize);
                    Paragraph CE101DataWorkMethodPara = new LeftParagraph(projectCatalogPrefix + ".5" + "\ua0a0\ua0a0" + "CE101数据处理方法", font, smallSize);
                    Paragraph CE101DataWorkMethodBodyPara = new bodyParagraph(CE101DataWorkMethod, font, smallSize);
                    Paragraph CE101TestResultAssessPara = new LeftParagraph(projectCatalogPrefix + ".6" + "\ua0a0\ua0a0" + "CE101测试结果评定准则", font, smallSize);
                    Paragraph CE101TestResultAssessBodyPara = new bodyParagraph(CE101TestResultAssess, font, smallSize);

                    //添加CE101试验项目到pdf中 并在目录中添加页码
                    document.add(CE101ProjectNamePara);
                    catalogs.put(projectCatalogPrefix  +"\ua0a0\ua0a0" + CE101ProjectName, pdf.getNumberOfPages());
                    document.add(CE101TestContentPara);
                    document.add(CE101ValuePic);
                    document.add(CE101ValuePicTitlePara);
                    document.add(CE101TestTargetPara);
                    document.add(CE101TestTargetBodyPara);
                    document.add(CE101TestStateAndSitePara);
                    document.add(CE101TestMethodPara);
                    document.add(CE101TestMethodBodyPara);
                    document.add(CE101TestPic);
                    document.add(CE101TestPicTitlePara);
                    document.add(CE101DataWorkMethodPara);
                    document.add(CE101DataWorkMethodBodyPara);
                    document.add(CE101TestResultAssessPara);
                    document.add(CE101TestResultAssessBodyPara);
                    break;
                case "CE102" :
                    String outlineData15 = manageSysOutline.getOutlineData15();
                    JSONObject outlineData15Object = JSON.parseObject(outlineData15);
                    //试验项目名
                    String CE102ProjectName = projectName + "试验";
                    //CE102试验内容
                    JSONArray CE102Array = new JSONArray();
                    CE102Array.add("CE102");
                    String CE102TestContent = "试验内容：\ua0a0\ua0a010kHz~10MHz\ua0a0\ua0a0电源线传导发射。" + "\n" +
                            "限值要求：见图6-" + currentPicNumber ;
                    JSONObject CE102ValueContentObject = new GeneratePdfLimitService().getProjectLimit(CE102Array, currentPicNumber, manageSysDevelop);
                    String CE102ValuePicPath = CE102ValueContentObject.getString("valuePicPath");
                    String CE102ValuePicTitle = CE102ValueContentObject.getString("valuePicTitle");
                    Paragraph CE102ValuePicTitlePara = new MidParagraph(CE102ValuePicTitle, font, smallSize);
                    Image CE102ValuePic = new Image(ImageDataFactory.create(CE102ValuePicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;

                    //CE102试验目的
                    String CE102TestTarget = outlineData15Object.getString("试验目的");
                    //CE102试验状态及测试位置
                    String CE102TestStateAndSite = "";
                    JSONArray CE102ValidTestPortArray = getTestPortAndWorkStatus(outlineData15, 15);
                    for (int j = 0; j < CE102ValidTestPortArray.size(); j++) {
                        JSONObject validTestPortObject = CE102ValidTestPortArray.getJSONObject(j);
                        CE102TestStateAndSite = CE102TestStateAndSite + "试验端口名称：" + validTestPortObject.getString("试验端口") + "\n";
                        CE102TestStateAndSite = CE102TestStateAndSite + "备注：" + validTestPortObject.getString("备注") + "\n";
                        if (validTestPortObject.containsKey("工作状态")) {
                            JSONObject allWorkStatusObject = validTestPortObject.getJSONObject("工作状态");
                            for (int k = 0; k < allWorkStatusObject.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject workStatusObject = allWorkStatusObject.getJSONObject("工作状态" + workStatusNumber);
                                if (workStatusObject.containsKey("状态是否实施")) {
                                    if ("是".equals(workStatusObject.getString("状态是否实施"))) {
                                        CE102TestStateAndSite = CE102TestStateAndSite + "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                        CE102TestStateAndSite = CE102TestStateAndSite + "工作状态描述：" + workStatusObject.getString("工作状态描述") + "\n";
                                    }
                                }
                            }
                        }
                        CE102TestStateAndSite = CE102TestStateAndSite + "\n\n";
                    }
                    //CE102试验方法
                    String CE102TestMethod = "";
                    String CE102TestPicTitle = "图6-" + currentPicNumber + "\ua0a0\ua0a0" + "CE102试验配置图";
                    String CE102TestPicPath = "";
                    if (outlineData15Object.getString("项目试验图") != null && ! "".equals(outlineData15Object.getString("项目试验图"))) {
                        CE102TestPicPath = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + manageSysDevelop.getDevName() + "//" +
                                outlineData15Object.getString("项目试验图");
                    } else {
                        CE102TestPicPath = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "CE102StandardTestPic.png";
                    }
                    Image CE102TestPic = new Image(ImageDataFactory.create(CE102TestPicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    if (outlineData15Object.getString("修改方法") != null && ! "".equals(outlineData15Object.getString("修改方法"))) {
                        CE102TestMethod = outlineData15Object.getString("修改方法");
                    } else {
                        CE102TestMethod = "按照" + CE102TestPicTitle + "," + "\ua0a0\ua0a0" + "和GJB151B-2013方法对CE102开展试验";
                    }
                    Paragraph CE102TestPicTitlePara = new MidParagraph(CE102TestPicTitle, font, smallSize);
                    //CE102数据处理方法
                    String CE102DataWorkMethod = outlineData15Object.getString("数据处理方法");
                    //CE102测试结果评定准则
                    String CE102TestResultAssess = outlineData15Object.getString("结果评定准则");

                    Paragraph CE102ProjectNamePara = new LeftParagraph(projectCatalogPrefix + "\ua0a0\ua0a0" + CE102ProjectName,
                            font, smallSize);
                    Paragraph CE102TestContentPara = new LeftParagraph(projectCatalogPrefix + ".1" + "\ua0a0\ua0a0" + "CE102试验内容" +
                            "\n\n" + CE102TestContent, font, smallSize);
                    Paragraph CE102TestTargetPara = new LeftParagraph(projectCatalogPrefix + ".2" + "\ua0a0\ua0a0" + "CE102试验目的", font, smallSize);
                    Paragraph CE102TestTargetBodyPara = new bodyParagraph(CE102TestTarget, font, smallSize);
                    Paragraph CE102TestStateAndSitePara = new LeftParagraph(projectCatalogPrefix + ".3" + "\ua0a0\ua0a0" + "CE102试验状态及测试位置" +
                            "\n\n" + CE102TestStateAndSite, font, smallSize);
                    Paragraph CE102TestMethodPara = new LeftParagraph(projectCatalogPrefix + ".4" + "\ua0a0\ua0a0" + "CE102试验方法", font, smallSize);
                    Paragraph CE102TestMethodBodyPara = new bodyParagraph(CE102TestMethod, font, smallSize);
                    Paragraph CE102DataWorkMethodPara = new LeftParagraph(projectCatalogPrefix + ".5" + "\ua0a0\ua0a0" + "CE102数据处理方法", font, smallSize);
                    Paragraph CE102DataWorkMethodBodyPara = new bodyParagraph(CE102DataWorkMethod, font, smallSize);
                    Paragraph CE102TestResultAssessPara = new LeftParagraph(projectCatalogPrefix + ".6" + "\ua0a0\ua0a0" + "CE102测试结果评定准则", font, smallSize);
                    Paragraph CE102TestResultAssessBodyPara = new bodyParagraph(CE102TestResultAssess, font, smallSize);

                    //添加CE102试验项目到pdf中 并在目录中添加页码
                    document.add(CE102ProjectNamePara);
                    catalogs.put(projectCatalogPrefix + "\ua0a0\ua0a0" + CE102ProjectName, pdf.getNumberOfPages());
                    document.add(CE102TestContentPara);
                    document.add(CE102ValuePic);
                    document.add(CE102ValuePicTitlePara);
                    document.add(CE102TestTargetPara);
                    document.add(CE102TestTargetBodyPara);
                    document.add(CE102TestStateAndSitePara);
                    document.add(CE102TestMethodPara);
                    document.add(CE102TestMethodBodyPara);
                    document.add(CE102TestPic);
                    document.add(CE102TestPicTitlePara);
                    document.add(CE102DataWorkMethodPara);
                    document.add(CE102DataWorkMethodBodyPara);
                    document.add(CE102TestResultAssessPara);
                    document.add(CE102TestResultAssessBodyPara);
                    break;
                case "CE106" :
                    String outlineData16 = manageSysOutline.getOutlineData16();
                    JSONObject outlineData16Object = JSON.parseObject(outlineData16);
                    //试验项目名
                    String CE106ProjectName = projectName + "试验";
                    //CE106试验内容
                    JSONArray CE106Array = new JSONArray();
                    CE106Array.add("CE106");
                    JSONObject CE106ValueContentObject = new GeneratePdfLimitService().getProjectLimit(CE106Array, currentPicNumber, manageSysDevelop);
                    String CE106TestContent = "试验内容：\ua0a0\ua0a010kHz~40GHz\ua0a0\ua0a0天线端口传导发射。" + "\n" +
                            "限值要求：如下内容" + "\n\n" + CE106ValueContentObject.getString("valueText");
                    //CE106试验目的
                    String CE106TestTarget = outlineData16Object.getString("试验目的");
                    //CE106试验状态及测试位置
                    String CE106TestStateAndSite = "";
                    JSONArray CE106ValidTestPortArray = getTestPortAndWorkStatus(outlineData16, 16);
                    for (int j = 0; j < CE106ValidTestPortArray.size(); j++) {
                        JSONObject validTestPortObject = CE106ValidTestPortArray.getJSONObject(j);
                        CE106TestStateAndSite = CE106TestStateAndSite + "天线端口名称：" + validTestPortObject.getString("天线端口") + "\n";
                        CE106TestStateAndSite = CE106TestStateAndSite + "备注：" + validTestPortObject.getString("备注") + "\n";
                        if (validTestPortObject.containsKey("工作状态")) {
                            JSONArray allWorkStatusArray = validTestPortObject.getJSONArray("工作状态");
                            for (int k = 0; k < allWorkStatusArray.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject workStatusObject = allWorkStatusArray.getJSONObject(k);
                                if (workStatusObject.containsKey("状态是否实施")) {
                                    if ("是".equals(workStatusObject.getString("状态是否实施"))) {
                                        CE106TestStateAndSite = CE106TestStateAndSite + "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                        CE106TestStateAndSite = CE106TestStateAndSite + "工作状态描述：" + workStatusObject.getString("工作状态") + "\n";
                                    }
                                }
                            }
                        }
                        CE106TestStateAndSite = CE106TestStateAndSite + "\n\n";
                    }
                    //CE106试验方法
                    String CE106TestMethod = "";
                    String CE106TestPicTitle = "";
                    String CE106TestPic1Path = "";
                    String CE106TestPic2Path = "";
                    String CE106TestPic1Title = "";
                    String CE106TestPic2Title = "";
                    ArrayList<String> CE106TestPicList = getTestPic("CE106", outlineData16);
                    if (outlineData16Object.getString("项目试验图") != null && ! "".equals(outlineData16Object.getString("项目试验图"))) {
                        CE106TestPic1Path = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + manageSysDevelop.getDevName() + "//" +
                                outlineData16Object.getString("项目试验图");
                    } else {
                        if (CE106TestPicList.contains("1")) {
                            CE106TestPic1Path = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "CE106StandardTestPic1.png";
                        }
                    }
                    if (CE106TestPicList.contains("2")) {
                        CE106TestPic2Path = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "CE106StandardTestPic2.png";
                    }
                    if (! "".equals(CE106TestPic1Path) && ! "".equals(CE106TestPic2Path)) {
                        int nextPicNumber = currentPicNumber + 1;
                        CE106TestPicTitle = "图6-" + currentPicNumber + "和图6-" + nextPicNumber + "CE106试验配置图";
                    } else {
                        CE106TestPicTitle = "图6-" + currentPicNumber + "\ua0a0\ua0a0" + "CE106试验配置图";
                    }
//                    Image CE106TestPic = new Image(ImageDataFactory.create(CE106TestPicPath)).
//                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
//                    currentPicNumber++;
                    if (outlineData16Object.getString("修改方法") != null && ! "".equals(outlineData16Object.getString("修改方法"))) {
                        CE106TestMethod = outlineData16Object.getString("修改方法");
                    } else {
                        CE106TestMethod = "按照" + CE106TestPicTitle + "," + "\ua0a0\ua0a0" + "和GJB151B-2013方法对CE106开展试验";
                    }
                    Paragraph CE106TestPicTitlePara = new MidParagraph(CE106TestPicTitle, font, smallSize);
                    //CE106数据处理方法
                    String CE106DataWorkMethod = outlineData16Object.getString("数据处理方法");
                    //CE106测试结果评定准则
                    String CE106TestResultAssess = outlineData16Object.getString("测试结果评定准则");

                    Paragraph CE106ProjectNamePara = new LeftParagraph(projectCatalogPrefix + "\ua0a0\ua0a0" + CE106ProjectName,
                            font, smallSize);
                    Paragraph CE106TestContentPara = new LeftParagraph(projectCatalogPrefix + ".1" + "\ua0a0\ua0a0" + "CE106试验内容" +
                            "\n\n" + CE106TestContent, font, smallSize);
                    Paragraph CE106TestTargetPara = new LeftParagraph(projectCatalogPrefix + ".2" + "\ua0a0\ua0a0" + "CE106试验目的", font, smallSize);
                    Paragraph CE106TestTargetBodyPara = new bodyParagraph(CE106TestTarget, font, smallSize);
                    Paragraph CE106TestStateAndSitePara = new LeftParagraph(projectCatalogPrefix + ".3" + "\ua0a0\ua0a0" + "CE106试验状态及测试位置" +
                            "\n\n" + CE106TestStateAndSite, font, smallSize);
                    Paragraph CE106TestMethodPara = new LeftParagraph(projectCatalogPrefix + ".4" + "\ua0a0\ua0a0" + "CE106试验方法", font, smallSize);
                    Paragraph CE106TestMethodBodyPara = new bodyParagraph(CE106TestMethod, font, smallSize);
                    Paragraph CE106DataWorkMethodPara = new LeftParagraph(projectCatalogPrefix + ".5" + "\ua0a0\ua0a0" + "CE106数据处理方法", font, smallSize);
                    Paragraph CE106DataWorkMethodBodyPara = new bodyParagraph(CE106DataWorkMethod, font, smallSize);
                    Paragraph CE106TestResultAssessPara = new LeftParagraph(projectCatalogPrefix + ".6" + "\ua0a0\ua0a0" + "CE106测试结果评定准则", font, smallSize);
                    Paragraph CE106TestResultAssessBodyPara = new bodyParagraph(CE106TestResultAssess, font, smallSize);

                    //添加CE106试验项目到pdf中 并在目录中添加页码
                    document.add(CE106ProjectNamePara);
                    catalogs.put(projectCatalogPrefix + "\ua0a0\ua0a0" + CE106ProjectName, pdf.getNumberOfPages());
                    document.add(CE106TestContentPara);
                    document.add(CE106TestTargetPara);
                    document.add(CE106TestTargetBodyPara);
                    document.add(CE106TestStateAndSitePara);
                    document.add(CE106TestMethodPara);
                    document.add(CE106TestMethodBodyPara);
                    if (! "".equals(CE106TestPic1Path)) {
                        CE106TestPic1Title = "图6-" + currentPicNumber + "\t" + "发射机和放大器的CE106测试配置";
                        Image CE106TestPic1 = new Image(ImageDataFactory.create(CE106TestPic1Path)).
                                scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                        currentPicNumber++;
                        Paragraph CE106TestPic1TitlePara = new MidParagraph(CE106TestPic1Title, font, smallSize);
                        document.add(CE106TestPic1);
                        document.add(CE106TestPic1TitlePara);
                    }
                    if (! "".equals(CE106TestPic2Path)) {
                        CE106TestPic2Title = "图6-" + currentPicNumber + "\t" + "接收机、处于待发状态下发射机和放大器的CE106测试配置";
                        Image CE106TestPic2 = new Image(ImageDataFactory.create(CE106TestPic2Path)).
                                scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                        currentPicNumber++;
                        Paragraph CE106TestPic2TitlePara = new MidParagraph(CE106TestPic2Title, font, smallSize);
                        document.add(CE106TestPic2);
                        document.add(CE106TestPic2TitlePara);
                    }
                    document.add(CE106DataWorkMethodPara);
                    document.add(CE106DataWorkMethodBodyPara);
                    document.add(CE106TestResultAssessPara);
                    document.add(CE106TestResultAssessBodyPara);
                    break;
                case "CE107" :
                    String outlineData17 = manageSysOutline.getOutlineData17();
                    JSONObject outlineData17Object = JSON.parseObject(outlineData17);
                    //试验项目名
                    String CE107ProjectName = projectName + "试验";
                    //CE107试验内容
                    JSONArray CE107Array = new JSONArray();
                    CE107Array.add("CE107");
                    JSONObject CE107ValueContentObject = new GeneratePdfLimitService().getProjectLimit(CE107Array, currentPicNumber, manageSysDevelop);
                    String CE107TestContent = "试验内容：\ua0a0\ua0a0电源线尖峰信号（时域）传导发射。" + "\n" +
                            "限值要求：如下内容" + "\n\n" + CE107ValueContentObject.getString("valueText");
                    //CE107试验目的
                    String CE107TestTarget = outlineData17Object.getString("试验目的");
                    //CE107试验状态及测试位置
                    String CE107TestStateAndSite = "";
                    JSONArray CE107ValidTestPortArray = getTestPortAndWorkStatus(outlineData17, 17);
                    for (int j = 0; j < CE107ValidTestPortArray.size(); j++) {
                        JSONObject validTestPortObject = CE107ValidTestPortArray.getJSONObject(j);
                        CE107TestStateAndSite = CE107TestStateAndSite + "试验电源端口名称：" + validTestPortObject.getString("试验电源端口") + "\n";
                        CE107TestStateAndSite = CE107TestStateAndSite + "开关状态：" + validTestPortObject.getString("开关状态") + "\n";
                        CE107TestStateAndSite = CE107TestStateAndSite + "备注：" + validTestPortObject.getString("备注") + "\n";
                        if (validTestPortObject.containsKey("工作状态")) {
                            JSONArray allWorkStatusArray = validTestPortObject.getJSONArray("工作状态");
                            for (int k = 0; k < allWorkStatusArray.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject workStatusObject = allWorkStatusArray.getJSONObject(k);
                                if (workStatusObject.containsKey("状态是否实施")) {
                                    if ("是".equals(workStatusObject.getString("状态是否实施"))) {
                                        CE107TestStateAndSite = CE107TestStateAndSite + "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                        CE107TestStateAndSite = CE107TestStateAndSite + "工作状态描述：" + workStatusObject.getString("工作状态") + "\n";
                                    }
                                }
                            }
                        }
                        CE107TestStateAndSite = CE107TestStateAndSite + "\n\n";
                    }
                    //CE107试验方法
                    String CE107TestMethod = "";
                    String CE107TestPicTitle = "图6-" + currentPicNumber + "\ua0a0\ua0a0" + "CE107试验配置图";
                    String CE107TestPicPath = "";
                    if (outlineData17Object.getString("项目试验图") != null && ! "".equals(outlineData17Object.getString("项目试验图"))) {
                        CE107TestPicPath = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + manageSysDevelop.getDevName() + "//" +
                                outlineData17Object.getString("项目试验图");
                    } else {
                        CE107TestPicPath = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "CE107StandardTestPic.png";
                    }
                    Image CE107TestPic = new Image(ImageDataFactory.create(CE107TestPicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    if (outlineData17Object.getString("修改方法") != null && ! "".equals(outlineData17Object.getString("修改方法"))) {
                        CE107TestMethod = outlineData17Object.getString("修改方法");
                    } else {
                        CE107TestMethod = "按照" + CE107TestPicTitle + "," + "\ua0a0\ua0a0" + "和GJB151B-2013方法对CE107开展试验";
                    }
                    Paragraph CE107TestPicTitlePara = new MidParagraph(CE107TestPicTitle, font, smallSize);

                    //CE107数据处理方法
                    String CE107DataWorkMethod = outlineData17Object.getString("数据处理方法");
                    //CE107测试结果评定准则
                    String CE107TestResultAssess = outlineData17Object.getString("测试结果评定准则");
                    Paragraph CE107ProjectNamePara = new LeftParagraph(projectCatalogPrefix + "\ua0a0\ua0a0" + CE107ProjectName,
                            font, smallSize);
                    Paragraph CE107TestContentPara = new LeftParagraph(projectCatalogPrefix + ".1" + "\ua0a0\ua0a0" + "CE107试验内容" +
                            "\n\n" + CE107TestContent, font, smallSize);
                    Paragraph CE107TestTargetPara = new LeftParagraph(projectCatalogPrefix + ".2" + "\ua0a0\ua0a0" + "CE107试验目的", font, smallSize);
                    Paragraph CE107TestTargetBodyPara = new bodyParagraph(CE107TestTarget, font, smallSize);
                    Paragraph CE107TestStateAndSitePara = new LeftParagraph(projectCatalogPrefix + ".3" + "\ua0a0\ua0a0" + "CE107试验状态及测试位置" +
                            "\n\n" + CE107TestStateAndSite, font, smallSize);
                    Paragraph CE107TestMethodPara = new LeftParagraph(projectCatalogPrefix + ".4" + "\ua0a0\ua0a0" + "CE107试验方法", font, smallSize);
                    Paragraph CE107TestMethodBodyPara = new bodyParagraph(CE107TestMethod, font, smallSize);
                    Paragraph CE107DataWorkMethodPara = new LeftParagraph(projectCatalogPrefix + ".5" + "\ua0a0\ua0a0" + "CE107数据处理方法", font, smallSize);
                    Paragraph CE107DataWorkMethodBodyPara = new bodyParagraph(CE107DataWorkMethod, font, smallSize);
                    Paragraph CE107TestResultAssessPara = new LeftParagraph(projectCatalogPrefix + ".6" + "\ua0a0\ua0a0" + "CE107测试结果评定准则", font, smallSize);
                    Paragraph CE107TestResultAssessBodyPara = new bodyParagraph(CE107TestResultAssess, font, smallSize);

                    //添加CE107试验项目到pdf中 并在目录中添加页码
                    document.add(CE107ProjectNamePara);
                    catalogs.put(projectCatalogPrefix + "\ua0a0\ua0a0" + CE107ProjectName, pdf.getNumberOfPages());
                    document.add(CE107TestContentPara);
                    document.add(CE107TestTargetPara);
                    document.add(CE107TestTargetBodyPara);
                    document.add(CE107TestStateAndSitePara);
                    document.add(CE107TestMethodPara);
                    document.add(CE107TestMethodBodyPara);
                    document.add(CE107TestPic);
                    document.add(CE107TestPicTitlePara);
                    document.add(CE107DataWorkMethodPara);
                    document.add(CE107DataWorkMethodBodyPara);
                    document.add(CE107TestResultAssessPara);
                    document.add(CE107TestResultAssessBodyPara);
                    break;
                case "CS101" :
                    String outlineData18 = manageSysOutline.getOutlineData18();
                    JSONObject outlineData18Object = JSON.parseObject(outlineData18);
                    //试验项目名
                    String CS101ProjectName = projectName + "试验";
                    //CS101试验内容
                    JSONArray CS101Array = new JSONArray();
                    CS101Array.add("CS101");
                    JSONObject CS101ValueContentObject = new GeneratePdfLimitService().getProjectLimit(CS101Array, currentPicNumber, manageSysDevelop);
                    String CS101TestContent = "试验内容：\ua0a0\ua0a025Hz~150kHz\ua0a0\ua0a0电源线传导敏感度。" + "\n" +
                            "限值要求：见图6-" + currentPicNumber + "和图6-" + (currentPicNumber + 1);
                    String CS101ValuePic1Path = CS101ValueContentObject.getString("valuePic1Path");
                    String CS101ValuePic1Title = CS101ValueContentObject.getString("valuePic1Title");
                    Paragraph CS101ValuePic1TitlePara = new MidParagraph(CS101ValuePic1Title, font, smallSize);
                    Image CS101ValuePic1 = new Image(ImageDataFactory.create(CS101ValuePic1Path)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    String CS101ValuePic2Path = CS101ValueContentObject.getString("valuePic2Path");
                    String CS101ValuePic2Title = CS101ValueContentObject.getString("valuePic2Title");
                    Paragraph CS101ValuePic2TitlePara = new MidParagraph(CS101ValuePic2Title, font, smallSize);
                    Image CS101ValuePic2 = new Image(ImageDataFactory.create(CS101ValuePic2Path)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    //CS101试验目的
                    String CS101TestTarget = outlineData18Object.getString("试验目的");
                    //CS101试验状态及测试位置
                    String CS101TestStateAndSite = "";
                    JSONArray CS101ValidTestPortArray = getTestPortAndWorkStatus(outlineData18, 18);
                    for (int j = 0; j < CS101ValidTestPortArray.size(); j++) {
                        JSONObject validTestPortObject = CS101ValidTestPortArray.getJSONObject(j);
                        CS101TestStateAndSite = CS101TestStateAndSite + "试验端口名称：" + validTestPortObject.getString("试验端口") + "\n";
                        CS101TestStateAndSite = CS101TestStateAndSite + "备注：" + validTestPortObject.getString("备注") + "\n";
                        if (validTestPortObject.containsKey("工作状态")) {
                            JSONObject allWorkStatusObject = validTestPortObject.getJSONObject("工作状态");
                            for (int k = 0; k < allWorkStatusObject.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject workStatusObject = allWorkStatusObject.getJSONObject("工作状态" + workStatusNumber);
                                if (workStatusObject.containsKey("状态是否实施")) {
                                    if ("是".equals(workStatusObject.getString("状态是否实施"))) {
                                        CS101TestStateAndSite = CS101TestStateAndSite + "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                        CS101TestStateAndSite = CS101TestStateAndSite + "工作状态描述：" + workStatusObject.getString("工作状态描述") + "\n";
                                    }
                                }
                            }
                        }
                        CS101TestStateAndSite = CS101TestStateAndSite + "\n\n";
                    }
//                    //CS101试验方法
//                    String CS101TestMethod = "";
//                    String CS101TestPicTitle = "图6-" + currentPicNumber + "\ua0a0\ua0a0" + "CS101试验配置图";
//                    String CS101TestPicPath = "";
////                    Image CS101TestPic = new Image(ImageDataFactory.create(CS101TestPicPath)).
////                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
////                    currentPicNumber++;
//                    if (outlineData18Object.getString("修改方法") != null && ! "".equals(outlineData18Object.getString("修改方法"))) {
//                        CS101TestMethod = outlineData18Object.getString("修改方法");
//                    } else {
//                        CS101TestMethod = "按照" + CS101TestPicTitle + "," + "\ua0a0\ua0a0" + "和GJB151B-2013方法对CS101开展试验";
//                    }
//                    Paragraph CS101TestPicTitlePara = new MidParagraph(CS101TestPicTitle, font, smallSize);
//                    //CS101数据处理方法
//                    String CS101DataWorkMethod = outlineData18Object.getString("数据处理方法");
//                    //CS101测试结果评定准则
//                    String CS101TestResultAssess = outlineData18Object.getString("测定结果评定准则");
//
//                    Paragraph CS101ProjectNamePara = new LeftParagraph(projectCatalogPrefix + "\ua0a0\ua0a0" + CS101ProjectName,
//                            font, smallSize);
//                    Paragraph CS101TestContentPara = new LeftParagraph(projectCatalogPrefix + ".1" + "\ua0a0\ua0a0" + "CS101试验内容" +
//                            "\n\n" + CS101TestContent, font, smallSize);
//                    Paragraph CS101TestTargetPara = new LeftParagraph(projectCatalogPrefix + ".2" + "\ua0a0\ua0a0" + "CS101试验目的", font, smallSize);
//                    Paragraph CS101TestTargetBodyPara = new bodyParagraph(CS101TestTarget, font, smallSize);
//                    Paragraph CS101TestStateAndSitePara = new LeftParagraph(projectCatalogPrefix + ".3" + "\ua0a0\ua0a0" + "CS101试验状态及测试位置" +
//                            "\n\n" + CS101TestStateAndSite, font, smallSize);
//                    Paragraph CS101TestMethodPara = new LeftParagraph(projectCatalogPrefix + ".4" + "\ua0a0\ua0a0" + "CS101试验方法", font, smallSize);
//                    Paragraph CS101TestMethodBodyPara = new bodyParagraph(CS101TestMethod, font, smallSize);
//                    Paragraph CS101DataWorkMethodPara = new LeftParagraph(projectCatalogPrefix + ".5" + "\ua0a0\ua0a0" + "CS101数据处理方法", font, smallSize);
//                    Paragraph CS101DataWorkMethodBodyPara = new bodyParagraph(CS101DataWorkMethod, font, smallSize);
//                    Paragraph CS101TestResultAssessPara = new LeftParagraph(projectCatalogPrefix + ".6" + "\ua0a0\ua0a0" + "CS101测试结果评定准则", font, smallSize);
//                    Paragraph CS101TestResultAssessBodyPara = new bodyParagraph(CS101TestResultAssess, font, smallSize);
//
//                    //添加CS101试验项目到pdf中 并在目录中添加页码
//                    document.add(CS101ProjectNamePara);
//                    catalogs.put(projectCatalogPrefix + "\ua0a0\ua0a0" + CS101ProjectName, pdf.getNumberOfPages());
//                    document.add(CS101TestContentPara);
//                    document.add(CS101ValuePic1);
//                    document.add(CS101ValuePic1TitlePara);
//                    document.add(CS101ValuePic2);
//                    document.add(CS101ValuePic2TitlePara);
//                    document.add(CS101TestTargetPara);
//                    document.add(CS101TestTargetBodyPara);
//                    document.add(CS101TestStateAndSitePara);
//                    document.add(CS101TestMethodPara);
//                    document.add(CS101TestMethodBodyPara);
//                    document.add(CS101DataWorkMethodPara);
//                    document.add(CS101DataWorkMethodBodyPara);
//                    document.add(CS101TestResultAssessPara);
//                    document.add(CS101TestResultAssessBodyPara);
//                    break;
                    String CS101TestMethod = "";
                    String CS101TestPicTitle = "图6-" + currentPicNumber + "\ua0a0\ua0a0" + "CS101试验配置图";
                    String CS101TestPicPath = "";
                    String CS101TestPic1Path = "";
                    String CS101TestPic2Path = "";
                    String CS101TestPic3Path = "";
                    String CS101TestPic1Title = "";
                    String CS101TestPic2Title = "";
                    String CS101TestPic3Title = "";
                    ArrayList<String> CS101TestPicList = getTestPic("CS101", outlineData10);
                    if (outlineData18Object.getString("项目试验图") != null && ! "".equals(outlineData18Object.getString("项目试验图"))) {
                        CS101TestPicPath = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + manageSysDevelop.getDevName() + "//" +
                                outlineData18Object.getString("项目试验图");
                    } else {
                        int picQuantity = 0;
                        if (CS101TestPicList.contains("1")) {
                            CS101TestPic1Path = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "CS101StandardTestPic1.png";
                            picQuantity++;
                        }
                        if (CS101TestPicList.contains("2")) {
                            CS101TestPic2Path = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "CS101StandardTestPic2.png";
                            picQuantity++;
                        }
                        if (CS101TestPicList.contains("3")) {
                            CS101TestPic3Path = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "CS101StandardTestPic3.png";
                            picQuantity++;
                        }
                        if (picQuantity == 2) {
                            int nextPicNumber = currentPicNumber + 1;
                            CS101TestPicTitle = "图6-" + currentPicNumber + "和图6-" + nextPicNumber + "\ua0a0\ua0a0" + "CS101试验配置图";
                        }
                        if (picQuantity == 3) {
                            int nextPicNumber = currentPicNumber + 1;
                            int nextNextPicNumber = currentPicNumber + 2;
                            CS101TestPicTitle = "图6-" + currentPicNumber + "和图6-" + nextPicNumber + "和图6-" + nextNextPicNumber + "\ua0a0\ua0a0" + "CS101试验配置图";
                        }
                    }
//                    Image CS101TestPic = new Image(ImageDataFactory.create(CS101TestPicPath)).
//                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
//                    currentPicNumber++;
                    if (outlineData18Object.getString("修改方法") != null && ! "".equals(outlineData18Object.getString("修改方法"))) {
                        CS101TestMethod = outlineData18Object.getString("修改方法");
                    } else {
                        CS101TestMethod = "按照" + CS101TestPicTitle + "," + "\ua0a0\ua0a0" + "和GJB151B-2013方法对CS101开展试验";
                    }
                    Paragraph CS101TestPicTitlePara = new MidParagraph(CS101TestPicTitle, font, smallSize);
                    //CS101数据处理方法
                    String CS101DataWorkMethod = outlineData18Object.getString("数据处理方法");
                    //CS101测试结果评定准则
                    String CS101TestResultAssess = outlineData18Object.getString("测定结果评定准则");

                    Paragraph CS101ProjectNamePara = new LeftParagraph(projectCatalogPrefix + "\ua0a0\ua0a0" + CS101ProjectName,
                            font, smallSize);
                    Paragraph CS101TestContentPara = new LeftParagraph(projectCatalogPrefix + ".1" + "\ua0a0\ua0a0" + "CS101试验内容" +
                            "\n\n" + CS101TestContent, font, smallSize);
                    Paragraph CS101TestTargetPara = new LeftParagraph(projectCatalogPrefix + ".2" + "\ua0a0\ua0a0" + "CS101试验目的", font, smallSize);
                    Paragraph CS101TestTargetBodyPara = new bodyParagraph(CS101TestTarget, font, smallSize);
                    Paragraph CS101TestStateAndSitePara = new LeftParagraph(projectCatalogPrefix + ".3" + "\ua0a0\ua0a0" + "CS101试验状态及测试位置" +
                            "\n\n" + CS101TestStateAndSite, font, smallSize);
                    Paragraph CS101TestMethodPara = new LeftParagraph(projectCatalogPrefix + ".4" + "\ua0a0\ua0a0" + "CS101试验方法", font, smallSize);
                    Paragraph CS101TestMethodBodyPara = new bodyParagraph(CS101TestMethod, font, smallSize);
                    Paragraph CS101DataWorkMethodPara = new LeftParagraph(projectCatalogPrefix + ".5" + "\ua0a0\ua0a0" + "CS101数据处理方法", font, smallSize);
                    Paragraph CS101DataWorkMethodBodyPara = new bodyParagraph(CS101DataWorkMethod, font, smallSize);
                    Paragraph CS101TestResultAssessPara = new LeftParagraph(projectCatalogPrefix + ".6" + "\ua0a0\ua0a0" + "CS101测试结果评定准则", font, smallSize);
                    Paragraph CS101TestResultAssessBodyPara = new bodyParagraph(CS101TestResultAssess, font, smallSize);

                    //添加CS101试验项目到pdf中 并在目录中添加页码
                    document.add(CS101ProjectNamePara);
                    catalogs.put(projectCatalogPrefix + "\ua0a0\ua0a0" + CS101ProjectName, pdf.getNumberOfPages());
                    document.add(CS101TestContentPara);
                    document.add(CS101ValuePic1);
                    document.add(CS101ValuePic1TitlePara);
                    document.add(CS101ValuePic2);
                    document.add(CS101ValuePic2TitlePara);
                    document.add(CS101TestTargetPara);
                    document.add(CS101TestTargetBodyPara);
                    document.add(CS101TestStateAndSitePara);
                    document.add(CS101TestMethodPara);
                    document.add(CS101TestMethodBodyPara);
                    if (! "".equals(CS101TestPicPath)) {
                        Image CS101TestPic = new Image(ImageDataFactory.create(CS101TestPicPath)).
                                scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                        currentPicNumber++;
                        document.add(CS101TestPic);
                        document.add(CS101TestPicTitlePara);
                    } else {
                        if (! "".equals(CS101TestPic1Path)) {
                            String CS101StandardTestPic1Title = "图6-" + currentPicNumber + "CS101测试配置（DC或单相AC线）";
                            Image CS101TestPic1 = new Image(ImageDataFactory.create(CS101TestPic1Path)).
                                    scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                            currentPicNumber++;
                            Paragraph CS101StandardTestPic1TitlePara = new MidParagraph(CS101StandardTestPic1Title, font, smallSize);
                            document.add(CS101TestPic1);
                            document.add(CS101StandardTestPic1TitlePara);
                        }
                        if (! "".equals(CS101TestPic2Path)) {
                            String CS101StandardTestPic2Title = "图6-" + currentPicNumber + "CS101测试配置（三相∆型电源线）";
                            Image CS101TestPic2 = new Image(ImageDataFactory.create(CS101TestPic2Path)).
                                    scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                            currentPicNumber++;
                            Paragraph CS101StandardTestPic2TitlePara = new MidParagraph(CS101StandardTestPic2Title, font, smallSize);
                            document.add(CS101TestPic2);
                            document.add(CS101StandardTestPic2TitlePara);
                        }
                        if (! "".equals(CS101TestPic3Path)) {
                            String CS101StandardTestPic3Title = "图6-" + currentPicNumber + "CS101测试配置（三相Y型电源线）";
                            Image CS101TestPic3 = new Image(ImageDataFactory.create(CS101TestPic3Path)).
                                    scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                            currentPicNumber++;
                            Paragraph CS101StandardTestPic3TitlePara = new MidParagraph(CS101StandardTestPic3Title, font, smallSize);
                            document.add(CS101TestPic3);
                            document.add(CS101StandardTestPic3TitlePara);
                        }

                    }
                    document.add(CS101DataWorkMethodPara);
                    document.add(CS101DataWorkMethodBodyPara);
                    document.add(CS101TestResultAssessPara);
                    document.add(CS101TestResultAssessBodyPara);
                    break;
                case "CS102" :
                    String outlineData19 = manageSysOutline.getOutlineData19();
                    JSONObject outlineData19Object = JSON.parseObject(outlineData19);
                    //试验项目名
                    String CS102ProjectName = projectName + "试验";
                    //CS102试验内容
                    JSONArray CS102Array = new JSONArray();
                    CS102Array.add("CS102");
                    JSONObject CS102ValueContentObject = new GeneratePdfLimitService().getProjectLimit(CS102Array, currentPicNumber, manageSysDevelop);
                    String CS102TestContent = "试验内容：\ua0a0\ua0a025Hz~50kHz\ua0a0\ua0a0地线传导敏感度。" + "\n" +
                            "限值要求：如下内容" + "\n\n" + CS102ValueContentObject.getString("valueText");
                    //CS102试验目的
                    String CS102TestTarget = outlineData19Object.getString("试验目的");
                    //CS102试验状态及测试位置
                    String CS102TestStateAndSite = "";
                    JSONArray CS102ValidTestPortArray = getTestPortAndWorkStatus(outlineData19, 19);
                    for (int j = 0; j < CS102ValidTestPortArray.size(); j++) {
                        JSONObject validTestPortObject = CS102ValidTestPortArray.getJSONObject(j);
                        CS102TestStateAndSite = CS102TestStateAndSite + "地线端口名称：" + validTestPortObject.getString("地线端口") + "\n";
                        CS102TestStateAndSite = CS102TestStateAndSite + "备注：" + validTestPortObject.getString("备注") + "\n";
                        if (validTestPortObject.containsKey("工作状态")) {
                            JSONObject allWorkStatusObject = validTestPortObject.getJSONObject("工作状态");
                            for (int k = 0; k < allWorkStatusObject.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject workStatusObject = allWorkStatusObject.getJSONObject("工作状态" + workStatusNumber);
                                if (workStatusObject.containsKey("状态是否实施")) {
                                    if ("是".equals(workStatusObject.getString("状态是否实施"))) {
                                        CS102TestStateAndSite = CS102TestStateAndSite + "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                        CS102TestStateAndSite = CS102TestStateAndSite + "工作状态描述：" + workStatusObject.getString("工作状态描述") + "\n";
                                    }
                                }
                            }
                        }
                        CS102TestStateAndSite = CS102TestStateAndSite + "\n\n";
                    }
                    //CS102试验方法
                    String CS102TestMethod = "";
                    String CS102TestPicTitle = "图6-" + currentPicNumber + "\ua0a0\ua0a0" + "CS102试验配置图";
                    String CS102TestPicPath = "";
                    if (outlineData19Object.getString("项目试验图") != null && ! "".equals(outlineData19Object.getString("项目试验图"))) {
                        CS102TestPicPath = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + manageSysDevelop.getDevName() + "//" +
                                outlineData19Object.getString("项目试验图");
                    } else {
                        CS102TestPicPath = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "CS102StandardTestPic.png";
                    }
                    Image CS102TestPic = new Image(ImageDataFactory.create(CS102TestPicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    if (outlineData19Object.getString("修改方法") != null && ! "".equals(outlineData19Object.getString("修改方法"))) {
                        CS102TestMethod = outlineData19Object.getString("修改方法");
                    } else {
                        CS102TestMethod = "按照" + CS102TestPicTitle + "," + "\ua0a0\ua0a0" + "和GJB151B-2013方法对CS102开展试验";
                    }
                    Paragraph CS102TestPicTitlePara = new MidParagraph(CS102TestPicTitle, font, smallSize);
                    //CS102数据处理方法
                    String CS102DataWorkMethod = outlineData19Object.getString("数据处理方法");
                    //CS102测试结果评定准则
                    String CS102TestResultAssess = outlineData19Object.getString("测试结果评定准则");

                    Paragraph CS102ProjectNamePara = new LeftParagraph(projectCatalogPrefix + "\ua0a0\ua0a0" + CS102ProjectName,
                            font, smallSize);
                    Paragraph CS102TestContentPara = new LeftParagraph(projectCatalogPrefix + ".1" + "\ua0a0\ua0a0" + "CS102试验内容" +
                            "\n\n" + CS102TestContent, font, smallSize);
                    Paragraph CS102TestTargetPara = new LeftParagraph(projectCatalogPrefix + ".2" + "\ua0a0\ua0a0" + "CS102试验目的", font, smallSize);
                    Paragraph CS102TestTargetBodyPara = new bodyParagraph(CS102TestTarget, font, smallSize);
                    Paragraph CS102TestStateAndSitePara = new LeftParagraph(projectCatalogPrefix + ".3" + "\ua0a0\ua0a0" + "CS102试验状态及测试位置" +
                            "\n\n" + CS102TestStateAndSite, font, smallSize);
                    Paragraph CS102TestMethodPara = new LeftParagraph(projectCatalogPrefix + ".4" + "\ua0a0\ua0a0" + "CS102试验方法", font, smallSize);
                    Paragraph CS102TestMethodBodyPara = new bodyParagraph(CS102TestMethod, font, smallSize);
                    Paragraph CS102DataWorkMethodPara = new LeftParagraph(projectCatalogPrefix + ".5" + "\ua0a0\ua0a0" + "CS102数据处理方法", font, smallSize);
                    Paragraph CS102DataWorkMethodBodyPara = new bodyParagraph(CS102DataWorkMethod, font, smallSize);
                    Paragraph CS102TestResultAssessPara = new LeftParagraph(projectCatalogPrefix + ".6" + "\ua0a0\ua0a0" + "CS102测试结果评定准则", font, smallSize);
                    Paragraph CS102TestResultAssessBodyPara = new bodyParagraph(CS102TestResultAssess, font, smallSize);

                    //添加CS102试验项目到pdf中 并在目录中添加页码
                    document.add(CS102ProjectNamePara);
                    catalogs.put(projectCatalogPrefix + "\ua0a0\ua0a0" + CS102ProjectName, pdf.getNumberOfPages());
                    document.add(CS102TestContentPara);
                    document.add(CS102TestTargetPara);
                    document.add(CS102TestTargetBodyPara);
                    document.add(CS102TestStateAndSitePara);
                    document.add(CS102TestMethodPara);
                    document.add(CS102TestMethodBodyPara);
                    document.add(CS102TestPic);
                    document.add(CS102TestPicTitlePara);
                    document.add(CS102DataWorkMethodPara);
                    document.add(CS102DataWorkMethodBodyPara);
                    document.add(CS102TestResultAssessPara);
                    document.add(CS102TestResultAssessBodyPara);
                    break;
                case "CS103" :
                    String outlineData20 = manageSysOutline.getOutlineData20();
                    JSONObject outlineData20Object = JSON.parseObject(outlineData20);
                    //试验项目名
                    String CS103ProjectName = projectName + "试验";
                    //CS103试验内容里的限值要求是文字内容 来源于第一个系统的手动填写
                    String CS103ValueText = JSON.parseObject(manageSysDevelop.getDevCs103()).getJSONObject("limit_value_current")
                            .getString("value");
                    String CS103TestContent = "试验内容：\ua0a0\ua0a015kHz~10GHz\ua0a0\ua0a0天线端口互调传导敏感度。" + "\n" +
                            "限值要求：" + "\n" + CS103ValueText;
                    //CS103试验目的
                    String CS103TestTarget = outlineData20Object.getString("试验目的");
                    //CS103试验状态及测试位置
                    String CS103TestStateAndSite = "";
                    JSONArray CS103ValidTestPortArray = getTestPortAndWorkStatus(outlineData20, 20);
                    for (int j = 0; j < CS103ValidTestPortArray.size(); j++) {
                        JSONObject validTestPortObject = CS103ValidTestPortArray.getJSONObject(j);
                        CS103TestStateAndSite = CS103TestStateAndSite + "天线端口名称：" + validTestPortObject.getString("天线端口") + "\n";
                        CS103TestStateAndSite = CS103TestStateAndSite + "备注：" + validTestPortObject.getString("备注") + "\n";
                        if (validTestPortObject.containsKey("工作状态")) {
                            JSONArray allWorkStatusArray = validTestPortObject.getJSONArray("工作状态");
                            for (int k = 0; k < allWorkStatusArray.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject workStatusObject = allWorkStatusArray.getJSONObject(k);
                                if (workStatusObject.containsKey("状态是否实施")) {
                                    if ("是".equals(workStatusObject.getString("状态是否实施"))) {
                                        CS103TestStateAndSite = CS103TestStateAndSite + "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                        CS103TestStateAndSite = CS103TestStateAndSite + "工作状态描述：" + workStatusObject.getString("工作状态") + "\n";
                                    }
                                }
                            }
                        }
                        CS103TestStateAndSite = CS103TestStateAndSite + "\n\n";
                    }
                    //CS103试验方法
                    String CS103TestMethod = "";
                    String CS103TestPicTitle = "图6-" + currentPicNumber + "\ua0a0\ua0a0" + "CS103试验配置图";
                    String CS103TestPicPath = "";
                    if (outlineData20Object.getString("项目试验图") != null && ! "".equals(outlineData20Object.getString("项目试验图"))) {
                        CS103TestPicPath = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + manageSysDevelop.getDevName() + "//" +
                                outlineData20Object.getString("项目试验图");
                    } else {
                        CS103TestPicPath = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "CS103StandardTestPic.png";
                    }
                    Image CS103TestPic = new Image(ImageDataFactory.create(CS103TestPicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    if (outlineData20Object.getString("修改方法") != null && ! "".equals(outlineData20Object.getString("修改方法"))) {
                        CS103TestMethod = outlineData20Object.getString("修改方法");
                    } else {
                        CS103TestMethod = "按照" + CS103TestPicTitle + "," + "\ua0a0\ua0a0" + "和GJB151B-2013方法对CS103开展试验";
                    }
                    Paragraph CS103TestPicTitlePara = new MidParagraph(CS103TestPicTitle, font, smallSize);
                    //CS103数据处理方法
                    String CS103DataWorkMethod = outlineData20Object.getString("数据处理方法");
                    //CS103测试结果评定准则
                    String CS103TestResultAssess = outlineData20Object.getString("测试结果评定准则");

                    Paragraph CS103ProjectNamePara = new LeftParagraph(projectCatalogPrefix + "\ua0a0\ua0a0" + CS103ProjectName,
                            font, smallSize);
                    Paragraph CS103TestContentPara = new LeftParagraph(projectCatalogPrefix + ".1" + "\ua0a0\ua0a0" + "CS103试验内容" +
                            "\n\n" + CS103TestContent, font, smallSize);
                    Paragraph CS103TestTargetPara = new LeftParagraph(projectCatalogPrefix + ".2" + "\ua0a0\ua0a0" + "CS103试验目的", font, smallSize);
                    Paragraph CS103TestTargetBodyPara = new bodyParagraph(CS103TestTarget, font, smallSize);
                    Paragraph CS103TestStateAndSitePara = new LeftParagraph(projectCatalogPrefix + ".3" + "\ua0a0\ua0a0" + "CS103试验状态及测试位置" +
                            "\n\n" + CS103TestStateAndSite, font, smallSize);
                    Paragraph CS103TestMethodPara = new LeftParagraph(projectCatalogPrefix + ".4" + "\ua0a0\ua0a0" + "CS103试验方法", font, smallSize);
                    Paragraph CS103TestMethodBodyPara = new bodyParagraph(CS103TestMethod, font, smallSize);
                    Paragraph CS103DataWorkMethodPara = new LeftParagraph(projectCatalogPrefix + ".5" + "\ua0a0\ua0a0" + "CS103数据处理方法", font, smallSize);
                    Paragraph CS103DataWorkMethodBodyPara = new bodyParagraph(CS103DataWorkMethod, font, smallSize);
                    Paragraph CS103TestResultAssessPara = new LeftParagraph(projectCatalogPrefix + ".6" + "\ua0a0\ua0a0" + "CS103测试结果评定准则", font, smallSize);
                    Paragraph CS103TestResultAssessBodyPara = new bodyParagraph(CS103TestResultAssess, font, smallSize);

                    //添加CS103试验项目到pdf中 并在目录中添加页码
                    document.add(CS103ProjectNamePara);
                    catalogs.put(projectCatalogPrefix + "\ua0a0\ua0a0" + CS103ProjectName, pdf.getNumberOfPages());
                    document.add(CS103TestContentPara);
                    document.add(CS103TestTargetPara);
                    document.add(CS103TestTargetBodyPara);
                    document.add(CS103TestStateAndSitePara);
                    document.add(CS103TestMethodPara);
                    document.add(CS103TestMethodBodyPara);
                    document.add(CS103TestPic);
                    document.add(CS103TestPicTitlePara);
                    document.add(CS103DataWorkMethodPara);
                    document.add(CS103DataWorkMethodBodyPara);
                    document.add(CS103TestResultAssessPara);
                    document.add(CS103TestResultAssessBodyPara);
                    break;
                case "CS104" :
                    String outlineData21 = manageSysOutline.getOutlineData21();
                    JSONObject outlineData21Object = JSON.parseObject(outlineData21);
                    //试验项目名
                    String CS104ProjectName = projectName + "试验";
                    //CS104试验内容 特殊项目限值填写
                    String CS104ValueText = JSON.parseObject(manageSysDevelop.getDevCs104()).getJSONObject("limit_value_current")
                            .getString("value");
                    String CS104TestContent = "试验内容：\ua0a0\ua0a025Hz~20GHz\ua0a0\ua0a0天线端口无用信号抑制传导敏感度。" + "\n" +
                            "限值要求：" + "\n" + CS104ValueText;
                    //CS104试验目的
                    String CS104TestTarget = outlineData21Object.getString("试验目的");
                    //CS104试验状态及测试位置
                    String CS104TestStateAndSite = "";
                    JSONArray CS104ValidTestPortArray = getTestPortAndWorkStatus(outlineData21, 21);
                    for (int j = 0; j < CS104ValidTestPortArray.size(); j++) {
                        JSONObject validTestPortObject = CS104ValidTestPortArray.getJSONObject(j);
                        CS104TestStateAndSite = CS104TestStateAndSite + "天线端口名称：" + validTestPortObject.getString("天线端口") + "\n";
                        CS104TestStateAndSite = CS104TestStateAndSite + "备注：" + validTestPortObject.getString("备注") + "\n";
                        if (validTestPortObject.containsKey("工作状态")) {
                            JSONArray allWorkStatusArray = validTestPortObject.getJSONArray("工作状态");
                            for (int k = 0; k < allWorkStatusArray.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject workStatusObject = allWorkStatusArray.getJSONObject(k);
                                if (workStatusObject.containsKey("状态是否实施")) {
                                    if ("是".equals(workStatusObject.getString("状态是否实施"))) {
                                        CS104TestStateAndSite = CS104TestStateAndSite + "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                        CS104TestStateAndSite = CS104TestStateAndSite + "工作状态描述：" + workStatusObject.getString("工作状态") + "\n";
                                    }
                                }
                            }
                        }
                        CS104TestStateAndSite = CS104TestStateAndSite + "\n\n";
                    }
                    //CS104试验方法
                    String CS104TestMethod = "";
                    String CS104TestPicTitle = "图6-" + currentPicNumber + "\ua0a0\ua0a0" + "CS104试验配置图";
                    String CS104TestPicPath = "";
                    if (outlineData21Object.getString("项目试验图") != null && ! "".equals(outlineData21Object.getString("项目试验图"))) {
                        CS104TestPicPath = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + manageSysDevelop.getDevName() + "//" +
                                outlineData21Object.getString("项目试验图");
                    } else {
                        CS104TestPicPath = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "CS104StandardTestPic.png";
                    }
                    Image CS104TestPic = new Image(ImageDataFactory.create(CS104TestPicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    if (outlineData21Object.getString("修改方法") != null && ! "".equals(outlineData21Object.getString("修改方法"))) {
                        CS104TestMethod = outlineData21Object.getString("修改方法");
                    } else {
                        CS104TestMethod = "按照" + CS104TestPicTitle + "," + "\ua0a0\ua0a0" + "和GJB151B-2013方法对CS104开展试验";
                    }
                    Paragraph CS104TestPicTitlePara = new MidParagraph(CS104TestPicTitle, font, smallSize);
                    //CS104数据处理方法
                    String CS104DataWorkMethod = outlineData21Object.getString("数据处理方法");
                    //CS104测试结果评定准则
                    String CS104TestResultAssess = outlineData21Object.getString("测试结果评定准则");

                    Paragraph CS104ProjectNamePara = new LeftParagraph(projectCatalogPrefix + "\ua0a0\ua0a0" + CS104ProjectName,
                            font, smallSize);
                    Paragraph CS104TestContentPara = new LeftParagraph(projectCatalogPrefix + ".1" + "\ua0a0\ua0a0" + "CS104试验内容" +
                            "\n\n" + CS104TestContent, font, smallSize);
                    Paragraph CS104TestTargetPara = new LeftParagraph(projectCatalogPrefix + ".2" + "\ua0a0\ua0a0" + "CS104试验目的", font, smallSize);
                    Paragraph CS104TestTargetBodyPara = new bodyParagraph(CS104TestTarget, font, smallSize);
                    Paragraph CS104TestStateAndSitePara = new LeftParagraph(projectCatalogPrefix + ".3" + "\ua0a0\ua0a0" + "CS104试验状态及测试位置" +
                            "\n\n" + CS104TestStateAndSite, font, smallSize);
                    Paragraph CS104TestMethodPara = new LeftParagraph(projectCatalogPrefix + ".4" + "\ua0a0\ua0a0" + "CS104试验方法", font, smallSize);
                    Paragraph CS104TestMethodBodyPara = new bodyParagraph(CS104TestMethod, font, smallSize);
                    Paragraph CS104DataWorkMethodPara = new LeftParagraph(projectCatalogPrefix + ".5" + "\ua0a0\ua0a0" + "CS104数据处理方法" , font, smallSize);
                    Paragraph CS104DataWorkMethodBodyPara = new bodyParagraph(CS104DataWorkMethod, font, smallSize);
                    Paragraph CS104TestResultAssessPara = new LeftParagraph(projectCatalogPrefix + ".6" + "\ua0a0\ua0a0" + "CS104测试结果评定准则", font, smallSize);
                    Paragraph CS104TestResultAssessBodyPara = new bodyParagraph(CS104TestResultAssess, font, smallSize);

                    //添加CS104试验项目到pdf中 并在目录中添加页码
                    document.add(CS104ProjectNamePara);
                    catalogs.put(projectCatalogPrefix + "\ua0a0\ua0a0" + CS104ProjectName, pdf.getNumberOfPages());
                    document.add(CS104TestContentPara);
                    document.add(CS104TestTargetPara);
                    document.add(CS104TestTargetBodyPara);
                    document.add(CS104TestStateAndSitePara);
                    document.add(CS104TestMethodPara);
                    document.add(CS104TestMethodBodyPara);
                    document.add(CS104TestPic);
                    document.add(CS104TestPicTitlePara);
                    document.add(CS104DataWorkMethodPara);
                    document.add(CS104DataWorkMethodBodyPara);
                    document.add(CS104TestResultAssessPara);
                    document.add(CS104TestResultAssessBodyPara);
                    break;
                case "CS105" :
                    String outlineData22 = manageSysOutline.getOutlineData22();
                    JSONObject outlineData22Object = JSON.parseObject(outlineData22);
                    //试验项目名
                    String CS105ProjectName = projectName + "试验";
                    //CS105试验内容 特殊项目限值填写
                    String CS105ValueText = JSON.parseObject(manageSysDevelop.getDevCs105()).getJSONObject("limit_value_current")
                            .getString("value");
                    String CS105TestContent = "试验内容：\ua0a0\ua0a025Hz~20GHz\ua0a0\ua0a0天线端口交调传导敏感度。" + "\n" +
                            "限值要求：" + "\n" + CS105ValueText;
                    //CS105试验目的
                    String CS105TestTarget = outlineData22Object.getString("试验目的");
                    //CS105试验状态及测试位置
                    String CS105TestStateAndSite = "";
                    JSONArray CS105ValidTestPortArray = getTestPortAndWorkStatus(outlineData22, 22);
                    for (int j = 0; j < CS105ValidTestPortArray.size(); j++) {
                        JSONObject validTestPortObject = CS105ValidTestPortArray.getJSONObject(j);
                        CS105TestStateAndSite = CS105TestStateAndSite + "天线端口名称：" + validTestPortObject.getString("天线端口") + "\n";
                        CS105TestStateAndSite = CS105TestStateAndSite + "备注：" + validTestPortObject.getString("备注") + "\n";
                        if (validTestPortObject.containsKey("工作状态")) {
                            JSONArray allWorkStatusArray = validTestPortObject.getJSONArray("工作状态");
                            for (int k = 0; k < allWorkStatusArray.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject workStatusObject = allWorkStatusArray.getJSONObject(k);
                                if (workStatusObject.containsKey("状态是否实施")) {
                                    if ("是".equals(workStatusObject.getString("状态是否实施"))) {
                                        CS105TestStateAndSite = CS105TestStateAndSite + "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                        CS105TestStateAndSite = CS105TestStateAndSite + "工作状态描述：" + workStatusObject.getString("工作状态") + "\n";
                                    }
                                }
                            }
                        }
                        CS105TestStateAndSite = CS105TestStateAndSite + "\n\n";
                    }
                    //CS105试验方法
                    String CS105TestMethod = "";
                    String CS105TestPicTitle = "图6-" + currentPicNumber + "\ua0a0\ua0a0" + "CS105试验配置图";
                    String CS105TestPicPath = "";
                    if (outlineData22Object.getString("项目试验图") != null && ! "".equals(outlineData22Object.getString("项目试验图"))) {
                        CS105TestPicPath = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + manageSysDevelop.getDevName() + "//" +
                                outlineData22Object.getString("项目试验图");
                    } else {
                        CS105TestPicPath = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "CS105StandardTestPic.png";
                    }
                    Image CS105TestPic = new Image(ImageDataFactory.create(CS105TestPicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    if (outlineData22Object.getString("修改方法") != null && ! "".equals(outlineData22Object.getString("修改方法"))) {
                        CS105TestMethod = outlineData22Object.getString("修改方法");
                    } else {
                        CS105TestMethod = "按照" + CS105TestPicTitle + "," + "\ua0a0\ua0a0" + "和GJB151B-2013方法对CS105开展试验";
                    }
                    Paragraph CS105TestPicTitlePara = new MidParagraph(CS105TestPicTitle, font, smallSize);
                    //CS105数据处理方法
                    String CS105DataWorkMethod = outlineData22Object.getString("数据处理方法");
                    //CS105测试结果评定准则
                    String CS105TestResultAssess = outlineData22Object.getString("测试结果评定准则");

                    Paragraph CS105ProjectNamePara = new LeftParagraph(projectCatalogPrefix + "\ua0a0\ua0a0" + CS105ProjectName,
                            font, smallSize);
                    Paragraph CS105TestContentPara = new LeftParagraph(projectCatalogPrefix + ".1" + "\ua0a0\ua0a0" + "CS105试验内容" +
                            "\n\n" + CS105TestContent, font, smallSize);
                    Paragraph CS105TestTargetPara = new LeftParagraph(projectCatalogPrefix + ".2" + "\ua0a0\ua0a0" + "CS105试验目的", font, smallSize);
                    Paragraph CS105TestTargetBodyPara = new bodyParagraph(CS105TestTarget, font, smallSize);
                    Paragraph CS105TestStateAndSitePara = new LeftParagraph(projectCatalogPrefix + ".3" + "\ua0a0\ua0a0" + "CS105试验状态及测试位置" +
                            "\n\n" + CS105TestStateAndSite, font, smallSize);
                    Paragraph CS105TestMethodPara = new LeftParagraph(projectCatalogPrefix + ".4" + "\ua0a0\ua0a0" + "CS105试验方法", font, smallSize);
                    Paragraph CS105TestMethodBodyPara = new bodyParagraph(CS105TestMethod, font, smallSize);
                    Paragraph CS105DataWorkMethodPara = new LeftParagraph(projectCatalogPrefix + ".5" + "\ua0a0\ua0a0" + "CS105数据处理方法", font, smallSize);
                    Paragraph CS105DataWorkMethodBodyPara = new bodyParagraph(CS105DataWorkMethod, font, smallSize);
                    Paragraph CS105TestResultAssessPara = new LeftParagraph(projectCatalogPrefix + ".6" + "\ua0a0\ua0a0" + "CS105测试结果评定准则", font, smallSize);
                    Paragraph CS105TestResultAssessBodyPara = new bodyParagraph(CS105TestResultAssess, font, smallSize);

                    //添加CS105试验项目到pdf中 并在目录中添加页码
                    document.add(CS105ProjectNamePara);
                    catalogs.put(projectCatalogPrefix + "\ua0a0\ua0a0" + CS105ProjectName, pdf.getNumberOfPages());
                    document.add(CS105TestContentPara);
                    document.add(CS105TestTargetPara);
                    document.add(CS105TestTargetBodyPara);
                    document.add(CS105TestStateAndSitePara);
                    document.add(CS105TestMethodPara);
                    document.add(CS105TestMethodBodyPara);
                    document.add(CS105TestPic);
                    document.add(CS105TestPicTitlePara);
                    document.add(CS105DataWorkMethodPara);
                    document.add(CS105DataWorkMethodBodyPara);
                    document.add(CS105TestResultAssessPara);
                    document.add(CS105TestResultAssessBodyPara);
                    break;
                case "CS106" :
                    String outlineData23 = manageSysOutline.getOutlineData23();
                    JSONObject outlineData23Object = JSON.parseObject(outlineData23);
                    //试验项目名
                    String CS106ProjectName = projectName + "试验";
                    //CS106试验内容
                    JSONArray CS106Array = new JSONArray();
                    CS106Array.add("CS106");
                    JSONObject CS106ValueContentObject = new GeneratePdfLimitService().getProjectLimit(CS106Array, currentPicNumber, manageSysDevelop);
                    String CS106TestContent = "试验内容：\ua0a0\ua0a0电源线尖峰信号传导敏感度。" + "\n" +
                            "限值要求：如下内容" + "\n\n" + CS106ValueContentObject.getString("valueText");
                    //CS106试验目的
                    String CS106TestTarget = outlineData23Object.getString("试验目的");
                    //CS106试验状态及测试位置
                    String CS106TestStateAndSite = "";
                    JSONArray CS106ValidTestPortArray = getTestPortAndWorkStatus(outlineData23, 23);
                    for (int j = 0; j < CS106ValidTestPortArray.size(); j++) {
                        JSONObject validTestPortObject = CS106ValidTestPortArray.getJSONObject(j);
                        CS106TestStateAndSite = CS106TestStateAndSite + "试验端口名称：" + validTestPortObject.getString("试验端口") + "\n";
                        CS106TestStateAndSite = CS106TestStateAndSite + "备注：" + validTestPortObject.getString("备注") + "\n";
                        if (validTestPortObject.containsKey("工作状态")) {
                            JSONObject allWorkStatusObject = validTestPortObject.getJSONObject("工作状态");
                            for (int k = 0; k < allWorkStatusObject.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject workStatusObject = allWorkStatusObject.getJSONObject("工作状态" + workStatusNumber);
                                if (workStatusObject.containsKey("状态是否实施")) {
                                    if ("是".equals(workStatusObject.getString("状态是否实施"))) {
                                        CS106TestStateAndSite = CS106TestStateAndSite + "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                        CS106TestStateAndSite = CS106TestStateAndSite + "工作状态描述：" + workStatusObject.getString("工作状态描述") + "\n";
                                    }
                                }
                            }
                        }
                        CS106TestStateAndSite = CS106TestStateAndSite + "\n\n";
                    }
                    //CS106试验方法 需确认 有多个试验配置图 如何确认
                    String CS106TestMethod = "";
                    String CS106TestPicTitle = "图6-" + currentPicNumber + "\ua0a0\ua0a0" + "CS106试验配置图";
                    String CS106TestPicPath = "";
                    if (outlineData23Object.getString("项目试验图") != null && ! "".equals(outlineData23Object.getString("项目试验图"))) {
                        CS106TestPicPath = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + manageSysDevelop.getDevName() + "//" +
                                outlineData23Object.getString("项目试验图");
                    } else {
                        CS106TestPicPath = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "CS106StandardTestPic.png";
                    }
                    Image CS106TestPic = new Image(ImageDataFactory.create(CS106TestPicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    if (outlineData23Object.getString("修改方法") != null && ! "".equals(outlineData23Object.getString("修改方法"))) {
                        CS106TestMethod = outlineData23Object.getString("修改方法");
                    } else {
                        CS106TestMethod = "按照" + CS106TestPicTitle + "," + "\ua0a0\ua0a0" + "和GJB151B-2013方法对CS106开展试验";
                    }
                    Paragraph CS106TestPicTitlePara = new MidParagraph(CS106TestPicTitle, font, smallSize);
                    //CS106数据处理方法
                    String CS106DataWorkMethod = outlineData23Object.getString("数据处理方法");
                    //CS106测试结果评定准则
                    String CS106TestResultAssess = outlineData23Object.getString("测试结果评定准则");

                    Paragraph CS106ProjectNamePara = new LeftParagraph(projectCatalogPrefix + "\ua0a0\ua0a0" + CS106ProjectName,
                            font, smallSize);
                    Paragraph CS106TestContentPara = new LeftParagraph(projectCatalogPrefix + ".1" + "\ua0a0\ua0a0" + "CS106试验内容" +
                            "\n\n" + CS106TestContent, font, smallSize);
                    Paragraph CS106TestTargetPara = new LeftParagraph(projectCatalogPrefix + ".2" + "\ua0a0\ua0a0" + "CS106试验目的", font, smallSize);
                    Paragraph CS106TestTargetBodyPara = new bodyParagraph(CS106TestTarget, font, smallSize);
                    Paragraph CS106TestStateAndSitePara = new LeftParagraph(projectCatalogPrefix + ".3" + "\ua0a0\ua0a0" + "CS106试验状态及测试位置" +
                            "\n\n" + CS106TestStateAndSite, font, smallSize);
                    Paragraph CS106TestMethodPara = new LeftParagraph(projectCatalogPrefix + ".4" + "\ua0a0\ua0a0" + "CS106试验方法", font, 10);
                    Paragraph CS106TestMethodBodyPara = new bodyParagraph(CS106TestMethod, font, smallSize);
                    Paragraph CS106DataWorkMethodPara = new LeftParagraph(projectCatalogPrefix + ".5" + "\ua0a0\ua0a0" + "CS106数据处理方法", font, smallSize);
                    Paragraph CS106DataWorkMethodBodyPara = new bodyParagraph(CS106DataWorkMethod, font, smallSize);
                    Paragraph CS106TestResultAssessPara = new LeftParagraph(projectCatalogPrefix + ".6" + "\ua0a0\ua0a0" + "CS106测试结果评定准则", font, smallSize);
                    Paragraph CS106TestResultAssessBodyPara = new bodyParagraph(CS106TestResultAssess, font, smallSize);

                    //添加CS106试验项目到pdf中 并在目录中添加页码
                    document.add(CS106ProjectNamePara);
                    catalogs.put(projectCatalogPrefix + "\ua0a0\ua0a0" + CS106ProjectName, pdf.getNumberOfPages());
                    document.add(CS106TestContentPara);
                    document.add(CS106TestTargetPara);
                    document.add(CS106TestTargetBodyPara);
                    document.add(CS106TestStateAndSitePara);
                    document.add(CS106TestMethodPara);
                    document.add(CS106TestMethodBodyPara);
                    document.add(CS106TestPic);
                    document.add(CS106TestPicTitlePara);
                    document.add(CS106DataWorkMethodPara);
                    document.add(CS106DataWorkMethodBodyPara);
                    document.add(CS106TestResultAssessPara);
                    document.add(CS106TestResultAssessBodyPara);
                    break;
                case "CS109" :
                    String outlineData24 = manageSysOutline.getOutlineData24();
                    JSONObject outlineData24Object = JSON.parseObject(outlineData24);
                    //试验项目名
                    String CS109ProjectName = projectName + "试验";
                    //CS109试验内容
                    JSONArray CS109Array = new JSONArray();
                    CS109Array.add("CS109");
                    JSONObject CS109ValueContentObject = new GeneratePdfLimitService().getProjectLimit(CS109Array, currentPicNumber, manageSysDevelop);
                    String CS109TestContent = "试验内容：\ua0a0\ua0a050Hz~100kHz\ua0a0\ua0a0壳体电流传导敏感度。" + "\n" +
                            "限值要求：见图6-" + currentPicNumber;
                    String CS109ValuePicPath = CS109ValueContentObject.getString("valuePicPath");
                    String CS109ValuePicTitle = CS109ValueContentObject.getString("valuePicTitle");
                    Paragraph CS109ValuePicTitlePara = new MidParagraph(CS109ValuePicTitle, font, smallSize);
                    Image CS109ValuePic = new Image(ImageDataFactory.create(CS109ValuePicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    //CS109试验目的
                    String CS109TestTarget = outlineData24Object.getString("试验目的");
                    //CS109试验状态及测试位置
                    String CS109TestStateAndSite = "";
                    JSONArray CS109ValidTestPortArray = getTestPortAndWorkStatus(outlineData24, 24);
                    for (int j = 0; j < CS109ValidTestPortArray.size(); j++) {
                        JSONObject validTestPortObject = CS109ValidTestPortArray.getJSONObject(j);
                        CS109TestStateAndSite = CS109TestStateAndSite + "试验位置名称：" + validTestPortObject.getString("试验位置") + "\n";
                        CS109TestStateAndSite = CS109TestStateAndSite + "备注：" + validTestPortObject.getString("备注") + "\n";
                        if (validTestPortObject.containsKey("工作状态")) {
                            JSONObject allWorkStatusObject = validTestPortObject.getJSONObject("工作状态");
                            for (int k = 0; k < allWorkStatusObject.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject workStatusObject = allWorkStatusObject.getJSONObject("工作状态" + workStatusNumber);
                                if (workStatusObject.containsKey("状态是否实施")) {
                                    if ("是".equals(workStatusObject.getString("状态是否实施"))) {
                                        CS109TestStateAndSite = CS109TestStateAndSite + "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                        CS109TestStateAndSite = CS109TestStateAndSite + "工作状态描述：" + workStatusObject.getString("工作状态描述") + "\n";
                                    }
                                }
                            }
                        }
                        CS109TestStateAndSite = CS109TestStateAndSite + "\n\n";
                    }
                    //CS109试验方法
                    String CS109TestMethod = "";
                    String CS109TestPicTitle = "图6-" + currentPicNumber + "\ua0a0\ua0a0" + "CS109试验配置图";
                    String CS109TestPicPath = "";
                    if (outlineData24Object.getString("项目试验图") != null && ! "".equals(outlineData24Object.getString("项目试验图"))) {
                        CS109TestPicPath = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + manageSysDevelop.getDevName() + "//" +
                                outlineData24Object.getString("项目试验图");
                    } else {
                        CS109TestPicPath = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "CS109StandardTestPic.png";
                    }
                    Image CS109TestPic = new Image(ImageDataFactory.create(CS109TestPicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    if (outlineData24Object.getString("修改方法") != null && ! "".equals(outlineData24Object.getString("修改方法"))) {
                        CS109TestMethod = outlineData24Object.getString("修改方法");
                    } else {
                        CS109TestMethod = "按照" + CS109TestPicTitle + "," + "\ua0a0\ua0a0" + "和GJB151B-2013方法对CS109开展试验";
                    }
                    Paragraph CS109TestPicTitlePara = new MidParagraph(CS109TestPicTitle, font, smallSize);
                    //CS109数据处理方法
                    String CS109DataWorkMethod = outlineData24Object.getString("数据处理方法");
                    //CS109测试结果评定准则
                    String CS109TestResultAssess = outlineData24Object.getString("测试结果评定准则");

                    Paragraph CS109ProjectNamePara = new LeftParagraph(projectCatalogPrefix + "\ua0a0\ua0a0" + CS109ProjectName,
                            font, smallSize);
                    Paragraph CS109TestContentPara = new LeftParagraph(projectCatalogPrefix + ".1" + "\ua0a0\ua0a0" + "CS109试验内容" +
                            "\n\n" + CS109TestContent, font, smallSize);
                    Paragraph CS109TestTargetPara = new LeftParagraph(projectCatalogPrefix + ".2" + "\ua0a0\ua0a0" + "CS109试验目的", font, smallSize);
                    Paragraph CS109TestTargetBodyPara = new bodyParagraph(CS109TestTarget, font, smallSize);
                    Paragraph CS109TestStateAndSitePara = new LeftParagraph(projectCatalogPrefix + ".3" + "\ua0a0\ua0a0" + "CS109试验状态及测试位置" +
                            "\n\n" + CS109TestStateAndSite, font, smallSize);
                    Paragraph CS109TestMethodPara = new LeftParagraph(projectCatalogPrefix + ".4" + "\ua0a0\ua0a0" + "CS109试验方法", font, smallSize);
                    Paragraph CS109TestMethodBodyPara = new bodyParagraph(CS109TestMethod, font, smallSize);
                    Paragraph CS109DataWorkMethodPara = new LeftParagraph(projectCatalogPrefix + ".5" + "\ua0a0\ua0a0" + "CS109数据处理方法", font, smallSize);
                    Paragraph CS109DataWorkMethodBodyPara = new bodyParagraph(CS109DataWorkMethod, font, smallSize);
                    Paragraph CS109TestResultAssessPara = new LeftParagraph(projectCatalogPrefix + ".6" + "\ua0a0\ua0a0" + "CS109测试结果评定准则", font, smallSize);
                    Paragraph CS109TestResultAssessBodyPara = new bodyParagraph(CS109TestResultAssess, font, smallSize);
                    //添加CS109试验项目到pdf中 并在目录中添加页码
                    document.add(CS109ProjectNamePara);
                    catalogs.put(projectCatalogPrefix + "\ua0a0\ua0a0" + CS109ProjectName, pdf.getNumberOfPages());
                    document.add(CS109TestContentPara);
                    document.add(CS109ValuePic);
                    document.add(CS109ValuePicTitlePara);
                    document.add(CS109TestTargetPara);
                    document.add(CS109TestTargetBodyPara);
                    document.add(CS109TestStateAndSitePara);
                    document.add(CS109TestMethodPara);
                    document.add(CS109TestMethodBodyPara);
                    document.add(CS109TestPic);
                    document.add(CS109TestPicTitlePara);
                    document.add(CS109DataWorkMethodPara);
                    document.add(CS109DataWorkMethodBodyPara);
                    document.add(CS109TestResultAssessPara);
                    document.add(CS109TestResultAssessBodyPara);
                    break;
                case "CS112" :
                    String outlineData25 = manageSysOutline.getOutlineData25();
                    JSONObject outlineData25Object = JSON.parseObject(outlineData25);
                    //试验项目名
                    String CS112ProjectName = projectName + "试验";
                    //CS112试验内容
                    JSONArray CS112Array = new JSONArray();
                    CS112Array.add("CS112");
                    JSONObject CS112ValueContentObject = new GeneratePdfLimitService().getProjectLimit(CS112Array, currentPicNumber, manageSysDevelop);
                    String CS112TestContent = "试验内容：\ua0a0\ua0a0静电放电敏感度。" + "\n" +
                            "限值要求：如下内容" + "\n\n" +CS112ValueContentObject.getString("valueText");
                    //CS112试验目的
                    String CS112TestTarget = outlineData25Object.getString("试验目的");
                    //CS112试验状态及测试位置
                    String CS112TestStateAndSite = "";
                    JSONArray CS112ValidTestPortArray = getTestPortAndWorkStatus(outlineData25, 25);
                    for (int j = 0; j < CS112ValidTestPortArray.size(); j++) {
                        JSONObject validTestPortObject = CS112ValidTestPortArray.getJSONObject(j);
                        CS112TestStateAndSite = CS112TestStateAndSite + "试验位置名称：" + validTestPortObject.getString("试验位置") + "\n";
                        CS112TestStateAndSite = CS112TestStateAndSite + "放电方式：" + validTestPortObject.getString("放电方式") + "\n";
                        CS112TestStateAndSite = CS112TestStateAndSite + "备注：" + validTestPortObject.getString("备注") + "\n";
                        if (validTestPortObject.containsKey("工作状态")) {
                            JSONObject allWorkStatusObject = validTestPortObject.getJSONObject("工作状态");
                            for (int k = 0; k < allWorkStatusObject.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject workStatusObject = allWorkStatusObject.getJSONObject("工作状态" + workStatusNumber);
                                if (workStatusObject.containsKey("状态是否实施")) {
                                    if ("是".equals(workStatusObject.getString("状态是否实施"))) {
                                        CS112TestStateAndSite = CS112TestStateAndSite + "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                        CS112TestStateAndSite = CS112TestStateAndSite + "工作状态描述：" + workStatusObject.getString("工作状态描述") + "\n";
                                    }
                                }
                            }
                        }
                        CS112TestStateAndSite = CS112TestStateAndSite + "\n\n";
                    }
                    //CS112试验方法
                    String CS112TestMethod = "";
                    String CS112TestPicTitle = "图6-" + currentPicNumber + "\ua0a0\ua0a0" + "CS112试验配置图";
                    String CS112TestPicPath = "";
                    if (outlineData25Object.getString("项目试验图") != null && ! "".equals(outlineData25Object.getString("项目试验图"))) {
                        CS112TestPicPath = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + manageSysDevelop.getDevName() + "//" +
                                outlineData25Object.getString("项目试验图");
                    } else {
                        CS112TestPicPath = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "CS112StandardTestPic.png";
                    }
                    Image CS112TestPic = new Image(ImageDataFactory.create(CS112TestPicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    if (outlineData25Object.getString("修改方法") != null && ! "".equals(outlineData25Object.getString("修改方法"))) {
                        CS112TestMethod = outlineData25Object.getString("修改方法");
                    } else {
                        CS112TestMethod = "按照" + CS112TestPicTitle + "," + "\ua0a0\ua0a0" + "和GJB151B-2013方法对CS112开展试验";
                    }
                    Paragraph CS112TestPicTitlePara = new MidParagraph(CS112TestPicTitle, font, smallSize);
                    //CS112数据处理方法
                    String CS112DataWorkMethod = outlineData25Object.getString("数据处理方法");
                    //CS112测试结果评定准则
                    String CS112TestResultAssess = outlineData25Object.getString("测试结果评定准则");

                    Paragraph CS112ProjectNamePara = new LeftParagraph(projectCatalogPrefix + "\ua0a0\ua0a0" + CS112ProjectName,
                            font, smallSize);
                    Paragraph CS112TestContentPara = new LeftParagraph(projectCatalogPrefix + ".1" + "\ua0a0\ua0a0" + "CS112试验内容" +
                            "\n\n" + CS112TestContent, font, smallSize);
                    Paragraph CS112TestTargetPara = new LeftParagraph(projectCatalogPrefix + ".2" + "\ua0a0\ua0a0" + "CS112试验目的", font, smallSize);
                    Paragraph CS112TestTargetBodyPara = new bodyParagraph(CS112TestTarget, font, smallSize);
                    Paragraph CS112TestStateAndSitePara = new LeftParagraph(projectCatalogPrefix + ".3" + "\ua0a0\ua0a0" + "CS112试验状态及测试位置" +
                            "\n\n" + CS112TestStateAndSite, font, smallSize);
                    Paragraph CS112TestMethodPara = new LeftParagraph(projectCatalogPrefix + ".4" + "\ua0a0\ua0a0" + "CS112试验方法", font, smallSize);
                    Paragraph CS112TestMethodBodyPara = new bodyParagraph(CS112TestMethod, font, smallSize);
                    Paragraph CS112DataWorkMethodPara = new LeftParagraph(projectCatalogPrefix + ".5" + "\ua0a0\ua0a0" + "CS112数据处理方法", font, smallSize);
                    Paragraph CS112DataWorkMethodBodyPara = new bodyParagraph(CS112DataWorkMethod, font, smallSize);
                    Paragraph CS112TestResultAssessPara = new LeftParagraph(projectCatalogPrefix + ".6" + "\ua0a0\ua0a0" + "CS112测试结果评定准则", font, smallSize);
                    Paragraph CS112TestResultAssessBodyPara = new bodyParagraph(CS112TestResultAssess, font, smallSize);

                    //添加CS112试验项目到pdf中 并在目录中添加页码
                    document.add(CS112ProjectNamePara);
                    catalogs.put(projectCatalogPrefix + "\ua0a0\ua0a0" + CS112ProjectName, pdf.getNumberOfPages());
                    document.add(CS112TestContentPara);
                    document.add(CS112TestTargetPara);
                    document.add(CS112TestTargetBodyPara);
                    document.add(CS112TestStateAndSitePara);
                    document.add(CS112TestMethodPara);
                    document.add(CS112TestMethodBodyPara);
                    document.add(CS112TestPic);
                    document.add(CS112TestPicTitlePara);
                    document.add(CS112DataWorkMethodPara);
                    document.add(CS112DataWorkMethodBodyPara);
                    document.add(CS112TestResultAssessPara);
                    document.add(CS112TestResultAssessBodyPara);
                    break;
                case "CS114" :
                    String outlineData26 = manageSysOutline.getOutlineData26();
                    JSONObject outlineData26Object = JSON.parseObject(outlineData26);
                    //试验项目名
                    String CS114ProjectName = projectName + "试验";
                    //CS114试验内容
                    JSONArray CS114Array = new JSONArray();
                    CS114Array.add("CS114");
                    JSONObject CS114ValueContentObject = new GeneratePdfLimitService().getProjectLimit(CS114Array, currentPicNumber, manageSysDevelop);
                    String CS114TestContent = "试验内容：\ua0a0\ua0a04kHz~400MHz\ua0a0\ua0a0电缆束注入传导敏感度。" + "\n" +
                            "限值要求：见图6-" + currentPicNumber;
                    String CS114ValuePicPath = CS114ValueContentObject.getString("valuePicPath");
                    String CS114ValuePicTitle = CS114ValueContentObject.getString("valuePicTitle");
                    Paragraph CS114ValuePicTitlePara = new MidParagraph(CS114ValuePicTitle, font, smallSize);
                    Image CS114ValuePic = new Image(ImageDataFactory.create(CS114ValuePicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    //CS114试验目的
                    String CS114TestTarget = outlineData26Object.getString("试验目的");
                    //CS114试验状态及测试位置
                    String CS114TestStateAndSite = "";
                    JSONObject CS114ValidTestPortObject = getTwoTestPort(outlineData26, 26);
                    JSONArray validElecTestPortArray = CS114ValidTestPortObject.getJSONArray("电源端口");
                    JSONArray validConnectTestPortArray = CS114ValidTestPortObject.getJSONArray("互联端口");
                    for (int j = 0; j < validElecTestPortArray.size(); j++) {
                        JSONObject elecTestPortObject = validElecTestPortArray.getJSONObject(j);
                        CS114TestStateAndSite += "电源端口名称：" + elecTestPortObject.getString("电源端口") + "\n";
                        if (elecTestPortObject.containsKey("工作状态")) {
                            JSONObject allElecWorkStatusObject = elecTestPortObject.getJSONObject("工作状态");
                            for (int k = 0; k < allElecWorkStatusObject.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject elecWorkStatusObject = allElecWorkStatusObject.getJSONObject("工作状态" + workStatusNumber);
                                if ("是".equals(elecWorkStatusObject.getString("状态是否实施"))) {
                                    CS114TestStateAndSite += "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                    CS114TestStateAndSite += "工作状态描述：" + elecWorkStatusObject.getString("工作状态描述") + "\n";
                                    CS114TestStateAndSite += "施加电缆束：" + elecWorkStatusObject.getString("施加电缆束") + "\n";
                                }
                            }
                        }
                        CS114TestStateAndSite += "\n\n";
                    }
                    for (int j = 0; j < validConnectTestPortArray.size(); j++) {
                        JSONObject connectTestPortObject = validConnectTestPortArray.getJSONObject(j);
                        CS114TestStateAndSite += "互联端口名称：" + connectTestPortObject.getString("互联端口") + "\n";
                        if (connectTestPortObject.containsKey("工作状态")) {
                            JSONObject allConnectWorkStatusObject = connectTestPortObject.getJSONObject("工作状态");
                            for (int k = 0; k < allConnectWorkStatusObject.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject connectWorkStatusObject = allConnectWorkStatusObject.getJSONObject("工作状态" + workStatusNumber);
                                if ("是".equals(connectWorkStatusObject.getString("状态是否实施"))) {
                                    CS114TestStateAndSite += "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                    CS114TestStateAndSite += "工作状态描述：" + connectWorkStatusObject.getString("工作状态描述") + "\n";
                                    CS114TestStateAndSite += "施加电缆束：" + connectWorkStatusObject.getString("施加电缆束") + "\n";
                                }
                            }
                        }
                        CS114TestStateAndSite += "\n\n";
                    }
                    //CS114试验方法
                    String CS114TestMethod = "";
                    String CS114TestPicTitle = "图6-" + currentPicNumber + "\ua0a0\ua0a0" + "CS114试验配置图";
                    String CS114TestPicPath = "";
                    if (outlineData26Object.getString("项目试验图") != null && ! "".equals(outlineData26Object.getString("项目试验图"))) {
                        CS114TestPicPath = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + manageSysDevelop.getDevName() + "//" +
                                outlineData26Object.getString("项目试验图");
                    } else {
                        CS114TestPicPath = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "CS114StandardTestPic.png";
                    }
                    Image CS114TestPic = new Image(ImageDataFactory.create(CS114TestPicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    if (outlineData26Object.getString("修改方法") != null && ! "".equals(outlineData26Object.getString("修改方法"))) {
                        CS114TestMethod = outlineData26Object.getString("修改方法");
                    } else {
                        CS114TestMethod = "按照" + CS114TestPicTitle + "," + "\ua0a0\ua0a0" + "和GJB151B-2013方法对CS114开展试验";
                    }
                    Paragraph CS114TestPicTitlePara = new MidParagraph(CS114TestPicTitle, font, smallSize);
                    //CS114数据处理方法
                    String CS114DataWorkMethod = outlineData26Object.getString("数据处理方法");
                    //CS114测试结果评定准则
                    String CS114TestResultAssess = outlineData26Object.getString("测试结果评定准则");

                    Paragraph CS114ProjectNamePara = new LeftParagraph(projectCatalogPrefix + "\ua0a0\ua0a0" + CS114ProjectName,
                            font, smallSize);
                    Paragraph CS114TestContentPara = new LeftParagraph(projectCatalogPrefix + ".1" + "\ua0a0\ua0a0" + "CS114试验内容" +
                            "\n\n" + CS114TestContent, font, smallSize);
                    Paragraph CS114TestTargetPara = new LeftParagraph(projectCatalogPrefix + ".2" + "\ua0a0\ua0a0" + "CS114试验目的", font, smallSize);
                    Paragraph CS114TestTargetBodyPara = new bodyParagraph(CS114TestTarget, font, smallSize);
                    Paragraph CS114TestStateAndSitePara = new LeftParagraph(projectCatalogPrefix + ".3" + "\ua0a0\ua0a0" + "CS114试验状态及测试位置" +
                            "\n\n" + CS114TestStateAndSite, font, smallSize);
                    Paragraph CS114TestMethodPara = new LeftParagraph(projectCatalogPrefix + ".4" + "\ua0a0\ua0a0" + "CS114试验方法", font, smallSize);
                    Paragraph CS114TestMethodBodyPara = new bodyParagraph(CS114TestMethod, font, smallSize);
                    Paragraph CS114DataWorkMethodPara = new LeftParagraph(projectCatalogPrefix + ".5" + "\ua0a0\ua0a0" + "CS114数据处理方法", font, smallSize);
                    Paragraph CS114DataWorkMethodBodyPara = new bodyParagraph(CS114DataWorkMethod, font, smallSize);
                    Paragraph CS114TestResultAssessPara = new LeftParagraph(projectCatalogPrefix + ".6" + "\ua0a0\ua0a0" + "CS114测试结果评定准则", font, smallSize);
                    Paragraph CS114TestResultAssessBodyPara = new bodyParagraph(CS114TestResultAssess, font, smallSize);

                    //添加CS114试验项目到pdf中 并在目录中添加页码
                    document.add(CS114ProjectNamePara);
                    catalogs.put(projectCatalogPrefix + "\ua0a0\ua0a0" + CS114ProjectName, pdf.getNumberOfPages());
                    document.add(CS114TestContentPara);
                    document.add(CS114ValuePic);
                    document.add(CS114ValuePicTitlePara);
                    document.add(CS114TestTargetPara);
                    document.add(CS114TestTargetBodyPara);
                    document.add(CS114TestStateAndSitePara);
                    document.add(CS114TestMethodPara);
                    document.add(CS114TestMethodBodyPara);
                    document.add(CS114TestPic);
                    document.add(CS114TestPicTitlePara);
                    document.add(CS114DataWorkMethodPara);
                    document.add(CS114DataWorkMethodBodyPara);
                    document.add(CS114TestResultAssessPara);
                    document.add(CS114TestResultAssessBodyPara);
                    break;
                case "CS115" :
                    String outlineData27 = manageSysOutline.getOutlineData27();
                    JSONObject outlineData27Object = JSON.parseObject(outlineData27);
                    //试验项目名
                    String CS115ProjectName = projectName + "试验";
                    //CS115试验内容
                    JSONArray CS115Array = new JSONArray();
                    CS115Array.add("CS115");
                    JSONObject CS115ValueContentObject = new GeneratePdfLimitService().getProjectLimit(CS115Array, currentPicNumber, manageSysDevelop);
                    String CS115TestContent = "试验内容：\ua0a0\ua0a0电缆束注入脉冲激励传导敏感度。" + "\n" +
                            "限值要求：如下文字内容\ua0a0并见图6-" + currentPicNumber + "\n\n" + CS115ValueContentObject.getString("valueText");
                    String CS115ValuePicPath = CS115ValueContentObject.getString("valuePicPath");
                    String CS115ValuePicTitle = CS115ValueContentObject.getString("valuePicTitle");
                    Paragraph CS115ValuePicTitlePara = new MidParagraph(CS115ValuePicTitle, font, smallSize);
                    Image CS115ValuePic = new Image(ImageDataFactory.create(CS115ValuePicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    //CS115试验目的
                    String CS115TestTarget = outlineData27Object.getString("试验目的");
                    //CS115试验状态及测试位置
                    String CS115TestStateAndSite = "";
                    JSONObject CS115ValidTestPortObject = getTwoTestPort(outlineData27, 27);
                    validElecTestPortArray = CS115ValidTestPortObject.getJSONArray("电源端口");
                    validConnectTestPortArray = CS115ValidTestPortObject.getJSONArray("互联端口");
                    for (int j = 0; j < validElecTestPortArray.size(); j++) {
                        JSONObject elecTestPortObject = validElecTestPortArray.getJSONObject(j);
                        CS115TestStateAndSite += "电源端口名称：" + elecTestPortObject.getString("电源端口") + "\n";
                        if (elecTestPortObject.containsKey("工作状态")) {
                            JSONObject allElecWorkStatusObject = elecTestPortObject.getJSONObject("工作状态");
                            for (int k = 0; k < allElecWorkStatusObject.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject elecWorkStatusObject = allElecWorkStatusObject.getJSONObject("工作状态" + workStatusNumber);
                                if ("是".equals(elecWorkStatusObject.getString("状态是否实施"))) {
                                    CS115TestStateAndSite += "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                    CS115TestStateAndSite += "工作状态描述：" + elecWorkStatusObject.getString("工作状态描述") + "\n";
                                    CS115TestStateAndSite += "施加电缆束：" + elecWorkStatusObject.getString("施加电缆束") + "\n";
                                }
                            }
                        }
                        CS115TestStateAndSite += "\n\n";
                    }
                    for (int j = 0; j < validConnectTestPortArray.size(); j++) {
                        JSONObject connectTestPortObject = validConnectTestPortArray.getJSONObject(j);
                        CS115TestStateAndSite += "互联端口名称：" + connectTestPortObject.getString("互联端口") + "\n";
                        if (connectTestPortObject.containsKey("工作状态")) {
                            JSONObject allConnectWorkStatusObject = connectTestPortObject.getJSONObject("工作状态");
                            for (int k = 0; k < allConnectWorkStatusObject.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject connectWorkStatusObject = allConnectWorkStatusObject.getJSONObject("工作状态" + workStatusNumber);
                                if ("是".equals(connectWorkStatusObject.getString("状态是否实施"))) {
                                    CS115TestStateAndSite += "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                    CS115TestStateAndSite += "工作状态描述：" + connectWorkStatusObject.getString("工作状态描述") + "\n";
                                    CS115TestStateAndSite += "施加电缆束：" + connectWorkStatusObject.getString("施加电缆束") + "\n";
                                }
                            }
                        }
                        CS115TestStateAndSite += "\n\n";
                    }
                    //CS115试验方法
                    String CS115TestMethod = "";
                    String CS115TestPicTitle = "图6-" + currentPicNumber + "\ua0a0\ua0a0" + "CS115试验配置图";
                    String CS115TestPicPath = "";
                    if (outlineData27Object.getString("项目试验图") != null && ! "".equals(outlineData27Object.getString("项目试验图"))) {
                        CS115TestPicPath = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + manageSysDevelop.getDevName() + "//" +
                                outlineData27Object.getString("项目试验图");
                    } else {
                        CS115TestPicPath = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "CS115StandardTestPic.png";
                    }
                    Image CS115TestPic = new Image(ImageDataFactory.create(CS115TestPicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    if (outlineData27Object.getString("修改方法") != null && ! "".equals(outlineData27Object.getString("修改方法"))) {
                        CS115TestMethod = outlineData27Object.getString("修改方法");
                    } else {
                        CS115TestMethod = "按照" + CS115TestPicTitle + "," + "\ua0a0\ua0a0" + "和GJB151B-2013方法对CS115开展试验";
                    }
                    Paragraph CS115TestPicTitlePara = new MidParagraph(CS115TestPicTitle, font, smallSize);
                    //CS115数据处理方法
                    String CS115DataWorkMethod = outlineData27Object.getString("数据处理方法");
                    //CS115测试结果评定准则
                    String CS115TestResultAssess = outlineData27Object.getString("测试结果评定准则");

                    Paragraph CS115ProjectNamePara = new LeftParagraph(projectCatalogPrefix + "\ua0a0\ua0a0" + CS115ProjectName,
                            font, smallSize);
                    Paragraph CS115TestContentPara = new LeftParagraph(projectCatalogPrefix + ".1" + "\ua0a0\ua0a0" + "CS115试验内容" +
                            "\n\n" + CS115TestContent, font, smallSize);
                    Paragraph CS115TestTargetPara = new LeftParagraph(projectCatalogPrefix + ".2" + "\ua0a0\ua0a0" + "CS115试验目的", font, smallSize);
                    Paragraph CS115TestTargetBodyPara = new bodyParagraph(CS115TestTarget, font, smallSize);
                    Paragraph CS115TestStateAndSitePara = new LeftParagraph(projectCatalogPrefix + ".3" + "\ua0a0\ua0a0" + "CS115试验状态及测试位置" +
                            "\n\n" + CS115TestStateAndSite, font, smallSize);
                    Paragraph CS115TestMethodPara = new LeftParagraph(projectCatalogPrefix + ".4" + "\ua0a0\ua0a0" + "CS115试验方法", font, smallSize);
                    Paragraph CS115TestMethodBodyPara = new bodyParagraph(CS115TestMethod, font, smallSize);
                    Paragraph CS115DataWorkMethodPara = new LeftParagraph(projectCatalogPrefix + ".5" + "\ua0a0\ua0a0" + "CS115数据处理方法", font, smallSize);
                    Paragraph CS115DataWorkMethodBodyPara = new bodyParagraph(CS115DataWorkMethod, font, smallSize);
                    Paragraph CS115TestResultAssessPara = new LeftParagraph(projectCatalogPrefix + ".6" + "\ua0a0\ua0a0" + "CS115测试结果评定准则", font, smallSize);
                    Paragraph CS115TestResultAssessBodyPara = new bodyParagraph(CS115TestResultAssess, font, smallSize);

                    //添加CS115试验项目到pdf中 并在目录中添加页码
                    document.add(CS115ProjectNamePara);
                    catalogs.put(projectCatalogPrefix + "\ua0a0\ua0a0" + CS115ProjectName, pdf.getNumberOfPages());
                    document.add(CS115TestContentPara);
                    document.add(CS115ValuePic);
                    document.add(CS115ValuePicTitlePara);
                    document.add(CS115TestTargetPara);
                    document.add(CS115TestTargetBodyPara);
                    document.add(CS115TestStateAndSitePara);
                    document.add(CS115TestMethodPara);
                    document.add(CS115TestMethodBodyPara);
                    document.add(CS115TestPic);
                    document.add(CS115TestPicTitlePara);
                    document.add(CS115DataWorkMethodPara);
                    document.add(CS115DataWorkMethodBodyPara);
                    document.add(CS115TestResultAssessPara);
                    document.add(CS115TestResultAssessBodyPara);
                    break;
                case "CS116" :
                    String outlineData28 = manageSysOutline.getOutlineData28();
                    JSONObject outlineData28Object = JSON.parseObject(outlineData28);
                    //试验项目名
                    String CS116ProjectName = projectName + "试验";
                    //CS116试验内容
                    JSONArray CS116Array = new JSONArray();
                    CS116Array.add("CS116");
                    JSONObject CS116ValueContentObject = new GeneratePdfLimitService().getProjectLimit(CS116Array, currentPicNumber, manageSysDevelop);
                    String CS116TestContent = "试验内容：\ua0a0\ua0a010kHz~100MHz\ua0a0\ua0a0电缆和电源线阻尼正弦瞬态传导敏感度。" + "\n" +
                            "限值要求：如下文字内容\ua0a0并见图6-" + currentPicNumber + "\n\n" + CS116ValueContentObject.getString("valueText");
                    String CS116ValuePicPath = CS116ValueContentObject.getString("valuePicPath");
                    String CS116ValuePicTitle = CS116ValueContentObject.getString("valuePicTitle");
                    Paragraph CS116ValuePicTitlePara = new MidParagraph(CS116ValuePicTitle, font, smallSize);
                    Image CS116ValuePic = new Image(ImageDataFactory.create(CS116ValuePicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    //CS116试验目的
                    String CS116TestTarget = outlineData28Object.getString("试验目的");
                    //CS116试验状态及测试位置
                    String CS116TestStateAndSite = "";
                    JSONObject CS116ValidTestPortObject = getTwoTestPort(outlineData28, 28);
                    validElecTestPortArray = CS116ValidTestPortObject.getJSONArray("电源端口");
                    validConnectTestPortArray = CS116ValidTestPortObject.getJSONArray("互联端口");
                    for (int j = 0; j < validElecTestPortArray.size(); j++) {
                        JSONObject elecTestPortObject = validElecTestPortArray.getJSONObject(j);
                        CS116TestStateAndSite += "电源端口名称：" + elecTestPortObject.getString("电源端口") + "\n";
                        if (elecTestPortObject.containsKey("工作状态")) {
                            JSONObject allElecWorkStatusObject = elecTestPortObject.getJSONObject("工作状态");
                            for (int k = 0; k < allElecWorkStatusObject.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject elecWorkStatusObject = allElecWorkStatusObject.getJSONObject("工作状态" + workStatusNumber);
                                if ("是".equals(elecWorkStatusObject.getString("状态是否实施"))) {
                                    CS116TestStateAndSite += "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                    CS116TestStateAndSite += "工作状态描述：" + elecWorkStatusObject.getString("工作状态描述") + "\n";
                                    CS116TestStateAndSite += "施加电缆束：" + elecWorkStatusObject.getString("施加电缆束") + "\n";
                                }
                            }
                        }
                        CS116TestStateAndSite += "\n\n";
                    }
                    for (int j = 0; j < validConnectTestPortArray.size(); j++) {
                        JSONObject connectTestPortObject = validConnectTestPortArray.getJSONObject(j);
                        CS116TestStateAndSite += "互联端口名称：" + connectTestPortObject.getString("互联端口") + "\n";
                        if (connectTestPortObject.containsKey("工作状态")) {
                            JSONObject allConnectWorkStatusObject = connectTestPortObject.getJSONObject("工作状态");
                            for (int k = 0; k < allConnectWorkStatusObject.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject connectWorkStatusObject = allConnectWorkStatusObject.getJSONObject("工作状态" + workStatusNumber);
                                if ("是".equals(connectWorkStatusObject.getString("状态是否实施"))) {
                                    CS116TestStateAndSite += "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                    CS116TestStateAndSite += "工作状态描述：" + connectWorkStatusObject.getString("工作状态描述") + "\n";
                                    CS116TestStateAndSite += "施加电缆束：" + connectWorkStatusObject.getString("施加电缆束") + "\n";
                                }
                            }
                        }
                        CS116TestStateAndSite += "\n\n";
                    }
                    //CS116试验方法
                    String CS116TestMethod = "";
                    String CS116TestPicTitle = "图6-" + currentPicNumber + "\ua0a0\ua0a0" + "CS116试验配置图";
                    String CS116TestPicPath = "";
                    if (outlineData28Object.getString("项目试验图") != null && ! "".equals(outlineData28Object.getString("项目试验图"))) {
                        CS116TestPicPath = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + manageSysDevelop.getDevName() + "//" +
                                outlineData28Object.getString("项目试验图");
                    } else {
                        CS116TestPicPath = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "CS116StandardTestPic.png";
                    }
                    Image CS116TestPic = new Image(ImageDataFactory.create(CS116TestPicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    if (outlineData28Object.getString("修改方法") != null && ! "".equals(outlineData28Object.getString("修改方法"))) {
                        CS116TestMethod = outlineData28Object.getString("修改方法");
                    } else {
                        CS116TestMethod = "按照" + CS116TestPicTitle + "," + "\ua0a0\ua0a0" + "和GJB151B-2013方法对CS116开展试验";
                    }
                    Paragraph CS116TestPicTitlePara = new MidParagraph(CS116TestPicTitle, font, smallSize);
                    //CS116数据处理方法
                    String CS116DataWorkMethod = outlineData28Object.getString("数据处理方法");
                    //CS116测试结果评定准则
                    String CS116TestResultAssess = outlineData28Object.getString("测试结果评定准则");

                    Paragraph CS116ProjectNamePara = new LeftParagraph(projectCatalogPrefix + "\ua0a0\ua0a0" + CS116ProjectName,
                            font, smallSize);
                    Paragraph CS116TestContentPara = new LeftParagraph(projectCatalogPrefix + ".1" + "\ua0a0\ua0a0" + "CS116试验内容" +
                            "\n\n" + CS116TestContent, font, smallSize);
                    Paragraph CS116TestTargetPara = new LeftParagraph(projectCatalogPrefix + ".2" + "\ua0a0\ua0a0" + "CS116试验目的", font, smallSize);
                    Paragraph CS116TestTargetBodyPara = new bodyParagraph(CS116TestTarget, font, smallSize);
                    Paragraph CS116TestStateAndSitePara = new LeftParagraph(projectCatalogPrefix + ".3" + "\ua0a0\ua0a0" + "CS116试验状态及测试位置" +
                            "\n\n" + CS116TestStateAndSite, font, smallSize);
                    Paragraph CS116TestMethodPara = new LeftParagraph(projectCatalogPrefix + ".4" + "\ua0a0\ua0a0" + "CS116试验方法", font, smallSize);
                    Paragraph CS116TestMethodBodyPara = new bodyParagraph(CS116TestMethod, font, smallSize);
                    Paragraph CS116DataWorkMethodPara = new LeftParagraph(projectCatalogPrefix + ".5" + "\ua0a0\ua0a0" + "CS116数据处理方法", font, smallSize);
                    Paragraph CS116DataWorkMethodBodyPara = new bodyParagraph(CS116DataWorkMethod, font, smallSize);
                    Paragraph CS116TestResultAssessPara = new LeftParagraph(projectCatalogPrefix + ".6" + "\ua0a0\ua0a0" + "CS116测试结果评定准则", font, smallSize);
                    Paragraph CS116TestResultAssessBodyPara = new bodyParagraph(CS116TestResultAssess, font, smallSize);

                    //添加CS116试验项目到pdf中 并在目录中添加页码
                    document.add(CS116ProjectNamePara);
                    catalogs.put(projectCatalogPrefix + "\ua0a0\ua0a0" + CS116ProjectName, pdf.getNumberOfPages());
                    document.add(CS116TestContentPara);
                    document.add(CS116ValuePic);
                    document.add(CS116ValuePicTitlePara);
                    document.add(CS116TestTargetPara);
                    document.add(CS116TestTargetBodyPara);
                    document.add(CS116TestStateAndSitePara);
                    document.add(CS116TestMethodPara);
                    document.add(CS116TestMethodBodyPara);
                    document.add(CS116TestPic);
                    document.add(CS116TestPicTitlePara);
                    document.add(CS116DataWorkMethodPara);
                    document.add(CS116DataWorkMethodBodyPara);
                    document.add(CS116TestResultAssessPara);
                    document.add(CS116TestResultAssessBodyPara);
                    break;
                case "RE101" :
                    String outlineData29 = manageSysOutline.getOutlineData29();
                    JSONObject outlineData29Object = JSON.parseObject(outlineData29);
                    //试验项目名
                    String RE101ProjectName = projectName + "试验";
                    //RE101试验内容
                    JSONArray RE101Array = new JSONArray();
                    RE101Array.add("RE101");
                    JSONObject RE101ValueContentObject = new GeneratePdfLimitService().getProjectLimit(RE101Array, currentPicNumber, manageSysDevelop);
                    String RE101TestContent = "试验内容：\ua0a0\ua0a025Hz~100kHz\ua0a0\ua0a0磁场辐射发射。" + "\n" +
                            "限值要求：见图6-" + currentPicNumber;
                    String RE101ValuePicPath = RE101ValueContentObject.getString("valuePicPath");
                    String RE101ValuePicTitle = RE101ValueContentObject.getString("valuePicTitle");
                    Paragraph RE101ValuePicTitlePara = new MidParagraph(RE101ValuePicTitle, font, smallSize);
                    Image RE101ValuePic = new Image(ImageDataFactory.create(RE101ValuePicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    //RE101试验目的
                    String RE101TestTarget = outlineData29Object.getString("试验目的");
                    //RE101试验状态及测试位置
                    String RE101TestStateAndSite = "";
                    JSONArray RE101ValidTestPortArray = getTestPortAndWorkStatus(outlineData29, 29);
                    for (int j = 0; j < RE101ValidTestPortArray.size(); j++) {
                        JSONObject validTestPortObject = RE101ValidTestPortArray.getJSONObject(j);
                        RE101TestStateAndSite = RE101TestStateAndSite + "试验部位名称：" + validTestPortObject.getString("试验部位") + "\n";
                        RE101TestStateAndSite = RE101TestStateAndSite + "备注：" + validTestPortObject.getString("备注") + "\n";
                        if (validTestPortObject.containsKey("工作状态")) {
                            JSONObject allWorkStatusObject = validTestPortObject.getJSONObject("工作状态");
                            for (int k = 0; k < allWorkStatusObject.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject workStatusObject = allWorkStatusObject.getJSONObject("工作状态" + workStatusNumber);
                                if (workStatusObject.containsKey("状态是否实施")) {
                                    if ("是".equals(workStatusObject.getString("状态是否实施"))) {
                                        RE101TestStateAndSite += "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                        RE101TestStateAndSite += "工作状态描述：" + workStatusObject.getString("工作状态描述") + "\n";
                                    }
                                }
                            }
                        }
                        RE101TestStateAndSite = RE101TestStateAndSite + "\n\n";
                    }
                    //RE101试验方法
                    String RE101TestMethod = "";
                    String RE101TestPicTitle = "图6-" + currentPicNumber + "\ua0a0\ua0a0" + "RE101试验配置图";
                    String RE101TestPicPath = "";
                    if (outlineData29Object.getString("项目试验图") != null && ! "".equals(outlineData29Object.getString("项目试验图"))) {
                        RE101TestPicPath = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + manageSysDevelop.getDevName() + "//" +
                                outlineData29Object.getString("项目试验图");
                    } else {
                        RE101TestPicPath = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "RE101StandardTestPic.png";
                    }
                    Image RE101TestPic = new Image(ImageDataFactory.create(RE101TestPicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    if (outlineData29Object.getString("修改方法") != null && ! "".equals(outlineData29Object.getString("修改方法"))) {
                        RE101TestMethod = outlineData29Object.getString("修改方法");
                    } else {
                        RE101TestMethod = "按照" + RE101TestPicTitle + "," + "\ua0a0\ua0a0" + "和GJB151B-2013方法对RE101开展试验";
                    }
                    Paragraph RE101TestPicTitlePara = new MidParagraph(RE101TestPicTitle, font, smallSize);
                    //RE101数据处理方法
                    String RE101DataWorkMethod = outlineData29Object.getString("数据处理方法");
                    //RE101测试结果评定准则
                    String RE101TestResultAssess = outlineData29Object.getString("测试结果评定准则");

                    Paragraph RE101ProjectNamePara = new LeftParagraph(projectCatalogPrefix + "\ua0a0\ua0a0" + RE101ProjectName,
                            font, smallSize);
                    Paragraph RE101TestContentPara = new LeftParagraph(projectCatalogPrefix + ".1" + "\ua0a0\ua0a0" + "RE101试验内容" +
                            "\n\n" + RE101TestContent, font, smallSize);
                    Paragraph RE101TestTargetPara = new LeftParagraph(projectCatalogPrefix + ".2" + "\ua0a0\ua0a0" + "RE101试验目的", font, smallSize);
                    Paragraph RE101TestTargetBodyPara = new bodyParagraph(RE101TestTarget, font, smallSize);
                    Paragraph RE101TestStateAndSitePara = new LeftParagraph(projectCatalogPrefix + ".3" + "\ua0a0\ua0a0" + "RE101试验状态及测试位置" +
                            "\n\n" + RE101TestStateAndSite, font, smallSize);
                    Paragraph RE101TestMethodPara = new LeftParagraph(projectCatalogPrefix + ".4" + "\ua0a0\ua0a0" + "RE101试验方法", font, smallSize);
                    Paragraph RE101TestMethodBodyPara = new bodyParagraph(RE101TestMethod, font, smallSize);
                    Paragraph RE101DataWorkMethodPara = new LeftParagraph(projectCatalogPrefix + ".5" + "\ua0a0\ua0a0" + "RE101数据处理方法", font, smallSize);
                    Paragraph RE101DataWorkMethodBodyPara = new bodyParagraph(RE101DataWorkMethod, font, smallSize);
                    Paragraph RE101TestResultAssessPara = new LeftParagraph(projectCatalogPrefix + ".6" + "\ua0a0\ua0a0" + "RE101测试结果评定准则", font, smallSize);
                    Paragraph RE101TestResultAssessBodyPara = new bodyParagraph(RE101TestResultAssess, font, smallSize);

                    //添加RE101试验项目到pdf中 并在目录中添加页码
                    document.add(RE101ProjectNamePara);
                    catalogs.put(projectCatalogPrefix + "\ua0a0\ua0a0" + RE101ProjectName, pdf.getNumberOfPages());
                    document.add(RE101TestContentPara);
                    document.add(RE101ValuePic);
                    document.add(RE101ValuePicTitlePara);
                    document.add(RE101TestTargetPara);
                    document.add(RE101TestTargetBodyPara);
                    document.add(RE101TestStateAndSitePara);
                    document.add(RE101TestMethodPara);
                    document.add(RE101TestMethodBodyPara);
                    document.add(RE101TestPic);
                    document.add(RE101TestPicTitlePara);
                    document.add(RE101DataWorkMethodPara);
                    document.add(RE101DataWorkMethodBodyPara);
                    document.add(RE101TestResultAssessPara);
                    document.add(RE101TestResultAssessBodyPara);
                    break;
                case "RE102" :
                    String outlineData30 = manageSysOutline.getOutlineData30();
                    JSONObject outlineData30Object = JSON.parseObject(outlineData30);
                    //试验项目名
                    String RE102ProjectName = projectName + "试验";
                    //RE102试验内容
                    JSONArray RE102Array = new JSONArray();
                    RE102Array.add("RE102");
                    JSONObject RE102ValueContentObject = new GeneratePdfLimitService().getProjectLimit(RE102Array, currentPicNumber, manageSysDevelop);
                    String RE102TestContent = "试验内容：\ua0a0\ua0a010kHz~18GHz\ua0a0\ua0a0电场辐射发射。" + "\n" +
                            "限值要求：见图6-" + currentPicNumber;
                    String RE102ValuePicPath = RE102ValueContentObject.getString("valuePicPath");
                    String RE102ValuePicTitle = RE102ValueContentObject.getString("valuePicTitle");
                    Paragraph RE102ValuePicTitlePara = new MidParagraph(RE102ValuePicTitle, font, smallSize);
                    Image RE102ValuePic = new Image(ImageDataFactory.create(RE102ValuePicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    //RE102试验目的
                    String RE102TestTarget = outlineData30Object.getString("试验目的");
                    //RE102试验状态及测试位置
                    String RE102TestStateAndSite = "";
                    JSONArray RE102ValidTestPortArray = getTestPortAndWorkStatus(outlineData30, 30);
                    for (int j = 0; j < RE102ValidTestPortArray.size(); j++) {
                        JSONObject validTestPortObject = RE102ValidTestPortArray.getJSONObject(j);
                        int testPortNumber = j + 1;
                        RE102TestStateAndSite = RE102TestStateAndSite + "试验端口名称：试验端口" + testPortNumber + "\n";
                        RE102TestStateAndSite = RE102TestStateAndSite + "备注：" + validTestPortObject.getString("备注") + "\n";
                        if (validTestPortObject.containsKey("天线位置")) {
                            RE102TestStateAndSite += "天线位置" + "\n";
                            JSONObject antennaPositionObject = validTestPortObject.getJSONObject("天线位置");
                            for(int k = 0; k < antennaPositionObject.size(); k++) {
                                int antennaNumber = k + 1;
                                JSONObject rateObject = antennaPositionObject.getJSONObject("频率" + antennaNumber);
                                RE102TestStateAndSite += "频率" + antennaNumber + "\n";
                                RE102TestStateAndSite += "频率：" + rateObject.getString("频率（GHz）") + "\n";
                                RE102TestStateAndSite += "位置数：" + rateObject.getString("位置数") + "\n";
                                RE102TestStateAndSite += "位置数计算过程：" + rateObject.getString("位置数计算过程") + "\n";
                            }
                        }
                        RE102TestStateAndSite += "\n";
                        if (validTestPortObject.containsKey("工作状态")) {
                            JSONObject allWorkStatusObject = validTestPortObject.getJSONObject("工作状态");
                            for (int k = 0; k < allWorkStatusObject.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject workStatusObject = allWorkStatusObject.getJSONObject("工作状态" + workStatusNumber);
                                if (workStatusObject.containsKey("状态是否实施")) {
                                    if ("是".equals(workStatusObject.getString("状态是否实施"))) {
                                        RE102TestStateAndSite += "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                        RE102TestStateAndSite += "工作状态描述：" + workStatusObject.getString("工作状态描述") + "\n";
                                    }
                                }
                            }
                        }
                        RE102TestStateAndSite = RE102TestStateAndSite + "\n\n";
                    }
                    //RE102试验方法  RE102测试配置图片有多个 如何确定
                    String RE102TestMethod = "";
                    String RE102TestPicTitle = "图6-" + currentPicNumber + "\ua0a0\ua0a0" + "RE102试验配置图";
                    String RE102TestPicPath = "";
                    String RE102TestPic1Path = "";
                    String RE102TestPic2Path = "";
                    String RE102TestPic1Title = "";
                    String RE102TestPic2Title = "";
                    if (outlineData30Object.getString("项目试验图") != null && ! "".equals(outlineData30Object.getString("项目试验图"))) {
                        RE102TestPicPath = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + manageSysDevelop.getDevName() + "//" +
                                outlineData30Object.getString("项目试验图");
                    } else {
                        RE102TestPic1Path = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "RE102StandardTestPic1.png";
                        RE102TestPic1Title = "图6-" + currentPicNumber + "\t" + "CE102试验配置图";
                        RE102TestPic2Path = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "RE102StandardTestPic2.png";
                        int nextPicNumber = currentPicNumber + 1;
                        RE102TestPic2Title = "图6-" + nextPicNumber + "\t" + "CE102试验配置图";
                        RE102TestPicTitle = "图6-" + currentPicNumber + "和图6-" + nextPicNumber + "\ua0a0\ua0a0" + "RE102试验配置图";
                    }
//                    Image RE102TestPic = new Image(ImageDataFactory.create(RE102TestPicPath)).
//                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    if (outlineData30Object.getString("修改方法") != null && ! "".equals(outlineData30Object.getString("修改方法"))) {
                        RE102TestMethod = outlineData30Object.getString("修改方法");
                    } else {
                        RE102TestMethod = "按照" + RE102TestPicTitle + "," + "\ua0a0\ua0a0" + "和GJB151B-2013方法对RE102开展试验";
                    }
                    Paragraph RE102TestPicTitlePara = new MidParagraph(RE102TestPicTitle, font, smallSize);
                    //RE102数据处理方法
                    String RE102DataWorkMethod = outlineData30Object.getString("数据处理方法");
                    //RE102测试结果评定准则
                    String RE102TestResultAssess = outlineData30Object.getString("测试结果评定准则");

                    Paragraph RE102ProjectNamePara = new LeftParagraph(projectCatalogPrefix + "\ua0a0\ua0a0" + RE102ProjectName,
                            font, smallSize);
                    Paragraph RE102TestContentPara = new LeftParagraph(projectCatalogPrefix + ".1" + "\ua0a0\ua0a0" + "RE102试验内容" +
                            "\n\n" + RE102TestContent, font, smallSize);
                    Paragraph RE102TestTargetPara = new LeftParagraph(projectCatalogPrefix + ".2" + "\ua0a0\ua0a0" + "RE102试验目的", font, smallSize);
                    Paragraph RE102TestTargetBodyPara =  new bodyParagraph(RE102TestTarget, font, smallSize);
                    Paragraph RE102TestStateAndSitePara = new LeftParagraph(projectCatalogPrefix + ".3" + "\ua0a0\ua0a0" + "RE102试验状态及测试位置" +
                            "\n\n" + RE102TestStateAndSite, font, smallSize);
                    Paragraph RE102TestMethodPara = new LeftParagraph(projectCatalogPrefix + ".4" + "\ua0a0\ua0a0" + "RE102试验方法", font, smallSize);
                    Paragraph RE102TestMethodBodyPara =  new bodyParagraph(RE102TestMethod, font, smallSize);
                    Paragraph RE102DataWorkMethodPara = new LeftParagraph(projectCatalogPrefix + ".5" + "\ua0a0\ua0a0" + "RE102数据处理方法", font, smallSize);
                    Paragraph RE102DataWorkMethodBodyPara =  new bodyParagraph(RE102DataWorkMethod, font, smallSize);
                    Paragraph RE102TestResultAssessPara = new LeftParagraph(projectCatalogPrefix + ".6" + "\ua0a0\ua0a0" + "RE102测试结果评定准则", font, smallSize);
                    Paragraph RE102TestResultAssessBodyPara =  new bodyParagraph(RE102TestResultAssess, font, smallSize);

                    //添加RE102试验项目到pdf中 并在目录中添加页码
                    document.add(RE102ProjectNamePara);
                    catalogs.put(projectCatalogPrefix + "\ua0a0\ua0a0" + RE102ProjectName, pdf.getNumberOfPages());
                    document.add(RE102TestContentPara);
                    document.add(RE102ValuePic);
                    document.add(RE102ValuePicTitlePara);
                    document.add(RE102TestTargetPara);
                    document.add(RE102TestTargetBodyPara);
                    document.add(RE102TestStateAndSitePara);
                    document.add(RE102TestMethodPara);
                    document.add(RE102TestMethodBodyPara);
                    if (! "".equals(RE102TestPicPath)) {
                        Image RE102TestPic = new Image(ImageDataFactory.create(RE102TestPicPath)).
                                scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                        currentPicNumber++;
                        document.add(RE102TestPic);
                        document.add(RE102TestPicTitlePara);
                    } else {
                        Image RE102TestPic1 = new Image(ImageDataFactory.create(RE102TestPic1Path)).
                                scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                        Paragraph RE102TestPic1TitlePara = new MidParagraph(RE102TestPic1Title, font, smallSize);
                        currentPicNumber++;
                        Image RE102TestPic2 = new Image(ImageDataFactory.create(RE102TestPic2Path)).
                                scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                        Paragraph RE102TestPic2TitlePara = new MidParagraph(RE102TestPic2Title, font, smallSize);
                        currentPicNumber++;
                        document.add(RE102TestPic1);
                        document.add(RE102TestPic1TitlePara);
                        document.add(RE102TestPic2);
                        document.add(RE102TestPic2TitlePara);
                    }
                    document.add(RE102DataWorkMethodPara);
                    document.add(RE102DataWorkMethodBodyPara);
                    document.add(RE102TestResultAssessPara);
                    document.add(RE102TestResultAssessBodyPara);
                    break;
                case "RE103" :
                    String outlineData31 = manageSysOutline.getOutlineData31();
                    JSONObject outlineData31Object = JSON.parseObject(outlineData31);
                    //试验项目名
                    String RE103ProjectName = projectName + "试验";
                    //RE103试验内容
                    JSONArray RE103Array = new JSONArray();
                    RE103Array.add("RE103");
                    JSONObject RE103ValueContentObject = new GeneratePdfLimitService().getProjectLimit(RE103Array, currentPicNumber, manageSysDevelop);
                    String RE103TestContent = "试验内容：\ua0a0\ua0a010kHz~40GHz\ua0a0\ua0a0天线谐波和乱真输出辐射发射。" + "\n" +
                            "限值要求：如下内容" + "\n\n" + RE103ValueContentObject.getString("valueText");
                    //RE103试验目的
                    String RE103TestTarget = outlineData31Object.getString("试验目的");
                    //RE103试验状态及测试位置
                    String RE103TestStateAndSite = "";
                    JSONArray RE103ValidTestPortArray = getTestPortAndWorkStatus(outlineData31, 31);
                    for (int j = 0; j < RE103ValidTestPortArray.size(); j++) {
                        JSONObject validTestPortObject = RE103ValidTestPortArray.getJSONObject(j);
                        int testPortNumber = j + 1;
                        RE103TestStateAndSite = RE103TestStateAndSite + "试验端口名称：试验端口" + testPortNumber + "\n";
                        RE103TestStateAndSite = RE103TestStateAndSite + "备注：" + validTestPortObject.getString("备注") + "\n";
                        if (validTestPortObject.containsKey("工作状态")) {
                            JSONObject allWorkStatusObject = validTestPortObject.getJSONObject("工作状态");
                            for (int k = 0; k < allWorkStatusObject.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject workStatusObject = allWorkStatusObject.getJSONObject("工作状态" + workStatusNumber);
                                if (workStatusObject.containsKey("状态是否实施")) {
                                    if ("是".equals(workStatusObject.getString("状态是否实施"))) {
                                        RE103TestStateAndSite += "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                        RE103TestStateAndSite += "工作状态描述：" + workStatusObject.getString("工作状态描述") + "\n";
                                    }
                                }
                            }
                        }
                        RE103TestStateAndSite = RE103TestStateAndSite + "\n\n";
                    }
                    //RE103试验方法
                    String RE103TestMethod = "";
                    String RE103TestPicTitle = "图6-" + currentPicNumber + "\ua0a0\ua0a0" + "RE103试验配置图";
                    String RE103TestPicPath = "";
                    String RE103TestPic1Path = "";
                    String RE103TestPic2Path = "";
                    String RE103TestPic1Title = "";
                    String RE103TestPic2Title = "";
                    if (outlineData31Object.getString("项目试验图") != null && ! "".equals(outlineData31Object.getString("项目试验图"))) {
                        RE103TestPicPath = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + manageSysDevelop.getDevName() + "//" +
                                outlineData31Object.getString("项目试验图");
                    } else {
                        int picNumber = currentPicNumber;
                        RE103TestPicTitle = "";
                        RE103TestPic1Path = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "RE103StandardTestPic1.png";
                        RE103TestPic1Title = "图6-" + picNumber + "\t" + "RE103谐波和乱真辐射发射校验和测试配置（10kHz~1GHz）";
                        RE103TestPicTitle += "图6-" + picNumber + "和";
                        picNumber++;
                        if (getTestPic("RE103", outlineData31).contains("1")) {
                            RE103TestPic2Path = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "RE103StandardTestPic2.png";
                            RE103TestPic2Title = "图6-" + picNumber + "\t" + "RE103谐波和乱真辐射发射校验和测试配置（1GHz~40GHz）";
                            RE103TestPicTitle += "图6-" + picNumber + "和";
                            picNumber++;
                        }
                        RE103TestPicTitle = RE103TestPicTitle.substring(0, RE103TestPicTitle.length() - 1);
                    }
//                    Image RE103TestPic = new Image(ImageDataFactory.create(RE103TestPicPath)).
//                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
//                    currentPicNumber++;
                    if (outlineData31Object.getString("修改方法") != null && ! "".equals(outlineData31Object.getString("修改方法"))) {
                        RE103TestMethod = outlineData31Object.getString("修改方法");
                    } else {
                        RE103TestMethod = "按照" + RE103TestPicTitle + "," + "\ua0a0\ua0a0" + "和GJB151B-2013方法对RE103开展试验";
                    }
                    Paragraph RE103TestPicTitlePara = new MidParagraph(RE103TestPicTitle, font, smallSize);
                    //RE103数据处理方法
                    String RE103DataWorkMethod = outlineData31Object.getString("数据处理方法");
                    //RE103测试结果评定准则
                    String RE103TestResultAssess = outlineData31Object.getString("测试结果评定准则");

                    Paragraph RE103ProjectNamePara = new LeftParagraph(projectCatalogPrefix + "\ua0a0\ua0a0" + RE103ProjectName,
                            font, smallSize);
                    Paragraph RE103TestContentPara = new LeftParagraph(projectCatalogPrefix + ".1" + "\ua0a0\ua0a0" + "RE103试验内容" +
                            "\n\n" + RE103TestContent, font, smallSize);
                    Paragraph RE103TestTargetPara = new LeftParagraph(projectCatalogPrefix + ".2" + "\ua0a0\ua0a0" + "RE103试验目的", font, smallSize);
                    Paragraph RE103TestTargetBodyPara = new bodyParagraph(RE103TestTarget, font, smallSize);
                    Paragraph RE103TestStateAndSitePara = new LeftParagraph(projectCatalogPrefix + ".3" + "\ua0a0\ua0a0" + "RE103试验状态及测试位置" +
                            "\n\n" + RE103TestStateAndSite, font, smallSize);
                    Paragraph RE103TestMethodPara = new LeftParagraph(projectCatalogPrefix + ".4" + "\ua0a0\ua0a0" + "RE103试验方法", font, smallSize);
                    Paragraph RE103TestMethodBodyPara = new bodyParagraph(RE103TestMethod, font, smallSize);
                    Paragraph RE103DataWorkMethodPara = new LeftParagraph(projectCatalogPrefix + ".5" + "\ua0a0\ua0a0" + "RE103数据处理方法", font, smallSize);
                    Paragraph RE103DataWorkMethodBodyPara = new bodyParagraph(RE103DataWorkMethod, font, smallSize);
                    Paragraph RE103TestResultAssessPara = new LeftParagraph(projectCatalogPrefix + ".6" + "\ua0a0\ua0a0" + "RE103测试结果评定准则", font, smallSize);
                    Paragraph RE103TestResultAssessBodyPara = new bodyParagraph(RE103TestResultAssess, font, smallSize);

                    //添加RE103试验项目到pdf中 并在目录中添加页码
                    document.add(RE103ProjectNamePara);
                    catalogs.put(projectCatalogPrefix + "\ua0a0\ua0a0" + RE103ProjectName, pdf.getNumberOfPages());
                    document.add(RE103TestContentPara);
                    document.add(RE103TestTargetPara);
                    document.add(RE103TestTargetBodyPara);
                    document.add(RE103TestStateAndSitePara);
                    document.add(RE103TestMethodPara);
                    document.add(RE103TestMethodBodyPara);
                    if (! "".equals(RE103TestPicPath)) {
                        Image RE103TestPic = new Image(ImageDataFactory.create(RE103TestPicPath)).
                                scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                        currentPicNumber++;
                        document.add(RE103TestPic);
                        document.add(RE103TestPicTitlePara);
                    } else {
                        Image RE103TestPic1 = new Image(ImageDataFactory.create(RE103TestPic1Path)).
                                scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                        currentPicNumber++;
                        Paragraph RE103TestPic1TitlePara = new MidParagraph(RE103TestPic1Title, font, smallSize);
                        document.add(RE103TestPic1);
                        document.add(RE103TestPic1TitlePara);
                        if (! "".equals(RE103TestPic2Path)) {
                            Image RE103TestPic2 = new Image(ImageDataFactory.create(RE103TestPic2Path)).
                                    scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                            currentPicNumber++;
                            Paragraph RE103TestPic2TitlePara = new MidParagraph(RE103TestPic2Title, font, smallSize);
                            document.add(RE103TestPic2);
                            document.add(RE103TestPic2TitlePara);
                        }
                    }
                    document.add(RE103DataWorkMethodPara);
                    document.add(RE103DataWorkMethodBodyPara);
                    document.add(RE103TestResultAssessPara);
                    document.add(RE103TestResultAssessBodyPara);
                    break;
                case "RS101" :
                    String outlineData32 = manageSysOutline.getOutlineData32();
                    JSONObject outlineData32Object = JSON.parseObject(outlineData32);
                    //试验项目名
                    String RS101ProjectName = projectName + "试验";
                    //RS101试验内容
                    JSONArray RS101Array = new JSONArray();
                    RS101Array.add("RS101");
                    JSONObject RS101ValueContentObject = new GeneratePdfLimitService().getProjectLimit(RS101Array, currentPicNumber, manageSysDevelop);
                    String RS101TestContent = "试验内容：\ua0a0\ua0a025Hz~100kHz\ua0a0\ua0a0磁场辐射敏感度" + "\n" +
                            "限值要求：见图6-" + currentPicNumber;
                    String RS101ValuePicPath = RS101ValueContentObject.getString("valuePicPath");
                    String RS101ValuePicTitle = RS101ValueContentObject.getString("valuePicTitle");
                    Paragraph RS101ValuePicTitlePara = new MidParagraph(RS101ValuePicTitle, font, smallSize);
                    Image RS101ValuePic = new Image(ImageDataFactory.create(RS101ValuePicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    //RS101试验目的
                    String RS101TestTarget = outlineData32Object.getString("试验目的");
                    //RS101试验状态及测试位置
                    String RS101TestStateAndSite = "";
                    JSONArray RS101ValidTestPortArray = getTestPortAndWorkStatus(outlineData32, 32);
                    for (int j = 0; j < RS101ValidTestPortArray.size(); j++) {
                        JSONObject validTestPortObject = RS101ValidTestPortArray.getJSONObject(j);
                        RS101TestStateAndSite = RS101TestStateAndSite + "试验部位名称：" + validTestPortObject.getString("试验部位") + "\n";
                        RS101TestStateAndSite = RS101TestStateAndSite + "备注：" + validTestPortObject.getString("备注") + "\n";
                        if (validTestPortObject.containsKey("工作状态")) {
                            JSONObject allWorkStatusObject = validTestPortObject.getJSONObject("工作状态");
                            for (int k = 0; k < allWorkStatusObject.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject workStatusObject = allWorkStatusObject.getJSONObject("工作状态" + workStatusNumber);
                                if (workStatusObject.containsKey("状态是否实施")) {
                                    if ("是".equals(workStatusObject.getString("状态是否实施"))) {
                                        RS101TestStateAndSite += "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                        RS101TestStateAndSite += "工作状态描述：" + workStatusObject.getString("工作状态描述") + "\n";
                                    }
                                }
                            }
                        }
                        RS101TestStateAndSite = RS101TestStateAndSite + "\n\n";
                    }
                    //RS101试验方法
                    String RS101TestMethod = "";
                    String RS101TestPicTitle = "图6-" + currentPicNumber + "\ua0a0\ua0a0" + "RS101试验配置图";
                    String RS101TestPicPath = "";
                    if (outlineData32Object.getString("项目试验图") != null && ! "".equals(outlineData32Object.getString("项目试验图"))) {
                        RS101TestPicPath = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + manageSysDevelop.getDevName() + "//" +
                                outlineData32Object.getString("项目试验图");
                    } else {
                        RS101TestPicPath = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "RS101StandardTestPic.png";
                    }
                    Image RS101TestPic = new Image(ImageDataFactory.create(RS101TestPicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    if (outlineData32Object.getString("修改方法") != null && ! "".equals(outlineData32Object.getString("修改方法"))) {
                        RS101TestMethod = outlineData32Object.getString("修改方法");
                    } else {
                        RS101TestMethod = "按照" + RS101TestPicTitle + "," + "\ua0a0\ua0a0" + "和GJB151B-2013方法对RS101开展试验";
                    }
                    Paragraph RS101TestPicTitlePara = new MidParagraph(RS101TestPicTitle, font, smallSize);
                    //RS101数据处理方法
                    String RS101DataWorkMethod = outlineData32Object.getString("数据处理方法");
                    //RS101测试结果评定准则
                    String RS101TestResultAssess = outlineData32Object.getString("测试结果评定准则");

                    Paragraph RS101ProjectNamePara = new LeftParagraph(projectCatalogPrefix + "\ua0a0\ua0a0" + RS101ProjectName,
                            font, smallSize);
                    Paragraph RS101TestContentPara = new LeftParagraph(projectCatalogPrefix + ".1" + "\ua0a0\ua0a0" + "RS101试验内容" +
                            "\n\n" + RS101TestContent, font, smallSize);
                    Paragraph RS101TestTargetPara = new LeftParagraph(projectCatalogPrefix + ".2" + "\ua0a0\ua0a0" + "RS101试验目的", font, smallSize);
                    Paragraph RS101TestTargetBodyPara = new bodyParagraph(RS101TestTarget, font, smallSize);
                    Paragraph RS101TestStateAndSitePara = new LeftParagraph(projectCatalogPrefix + ".3" + "\ua0a0\ua0a0" + "RS101试验状态及测试位置" +
                            "\n\n" + RS101TestStateAndSite, font, smallSize);
                    Paragraph RS101TestMethodPara = new LeftParagraph(projectCatalogPrefix + ".4" + "\ua0a0\ua0a0" + "RS101试验方法", font, smallSize);
                    Paragraph RS101TestMethodBodyPara = new bodyParagraph(RS101TestMethod, font, smallSize);
                    Paragraph RS101DataWorkMethodPara = new LeftParagraph(projectCatalogPrefix + ".5" + "\ua0a0\ua0a0" + "RS101数据处理方法", font, smallSize);
                    Paragraph RS101DataWorkMethodBodyPara = new bodyParagraph(RS101DataWorkMethod, font, smallSize);
                    Paragraph RS101TestResultAssessPara = new LeftParagraph(projectCatalogPrefix + ".6" + "\ua0a0\ua0a0" + "RS101测试结果评定准则", font, smallSize);
                    Paragraph RS101TestResultAssessBodyPara = new bodyParagraph(RS101TestResultAssess, font, smallSize);

                    //添加RS101试验项目到pdf中 并在目录中添加页码
                    document.add(RS101ProjectNamePara);
                    catalogs.put(projectCatalogPrefix + "\ua0a0\ua0a0" + RS101ProjectName, pdf.getNumberOfPages());
                    document.add(RS101TestContentPara);
                    document.add(RS101ValuePic);
                    document.add(RS101ValuePicTitlePara);
                    document.add(RS101TestTargetPara);
                    document.add(RS101TestTargetBodyPara);
                    document.add(RS101TestStateAndSitePara);
                    document.add(RS101TestMethodPara);
                    document.add(RS101TestMethodBodyPara);
                    document.add(RS101TestPic);
                    document.add(RS101TestPicTitlePara);
                    document.add(RS101DataWorkMethodPara);
                    document.add(RS101DataWorkMethodBodyPara);
                    document.add(RS101TestResultAssessPara);
                    document.add(RS101TestResultAssessBodyPara);
                    break;
                case "RS103" :
                    String outlineData33 = manageSysOutline.getOutlineData33();
                    JSONObject outlineData33Object = JSON.parseObject(outlineData33);
                    //试验项目名
                    String RS103ProjectName = projectName + "试验";
                    //RS103试验内容
                    JSONArray RS103Array = new JSONArray();
                    RS103Array.add("RS103");
                    JSONObject RS103ValueContentObject = new GeneratePdfLimitService().getProjectLimit(RS103Array, currentPicNumber, manageSysDevelop);
                    String RS103TestContent = "试验内容：\ua0a0\ua0a010kHz~40GHz\ua0a0\ua0a0电场辐射敏感度" + "\n" +
                            "限值要求：见图6-" + currentPicNumber;
                    String RS103ValuePicPath = RS103ValueContentObject.getString("valuePicPath");
                    String RS103ValuePicTitle = RS103ValueContentObject.getString("valuePicTitle");
                    Paragraph RS103ValuePicTitlePara = new MidParagraph(RS103ValuePicTitle, font, smallSize);
                    Image RS103ValuePic = new Image(ImageDataFactory.create(RS103ValuePicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    //RS103试验目的
                    String RS103TestTarget = outlineData33Object.getString("试验目的");
                    //RS103试验状态及测试位置
                    String RS103TestStateAndSite = "";
                    JSONArray RS103ValidTestPortArray = getTestPortAndWorkStatus(outlineData33, 33);
                    for (int j = 0; j < RS103ValidTestPortArray.size(); j++) {
                        JSONObject validTestPortObject = RS103ValidTestPortArray.getJSONObject(j);
                        int testPortNumber = j + 1;
                        RS103TestStateAndSite = RS103TestStateAndSite + "试验端口名称：试验端口" + testPortNumber + "\n";
                        RS103TestStateAndSite = RS103TestStateAndSite + "备注：" + validTestPortObject.getString("备注") + "\n";
                        if (validTestPortObject.containsKey("天线位置")) {
                            RS103TestStateAndSite += "天线位置" + "\n";
                            JSONObject antennaPositionObject = validTestPortObject.getJSONObject("天线位置");
                            for(int k = 0; k < antennaPositionObject.size(); k++) {
                                int antennaNumber = k + 1;
                                JSONObject rateObject = antennaPositionObject.getJSONObject("频率" + antennaNumber);
                                RS103TestStateAndSite += "频率" + antennaNumber + "\n";
                                RS103TestStateAndSite += "频率：" + rateObject.getString("频率（GHz）") + "\n";
                                RS103TestStateAndSite += "位置数：" + rateObject.getString("位置数") + "\n";
                                RS103TestStateAndSite += "位置数计算过程：" + rateObject.getString("位置数计算过程") + "\n";
                            }
                        }
                        RS103TestStateAndSite += "\n";
                        if (validTestPortObject.containsKey("工作状态")) {
                            JSONObject allWorkStatusObject = validTestPortObject.getJSONObject("工作状态");
                            for (int k = 0; k < allWorkStatusObject.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject workStatusObject = allWorkStatusObject.getJSONObject("工作状态" + workStatusNumber);
                                if (workStatusObject.containsKey("状态是否实施")) {
                                    if ("是".equals(workStatusObject.getString("状态是否实施"))) {
                                        RS103TestStateAndSite += "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                        RS103TestStateAndSite += "工作状态描述：" + workStatusObject.getString("工作状态描述") + "\n";
                                    }
                                }
                            }
                        }
                        RS103TestStateAndSite = RS103TestStateAndSite + "\n\n";
                    }
                    //RS103试验方法
                    String RS103TestMethod = "";
                    String RS103TestPicTitle = "图6-" + currentPicNumber + "\ua0a0\ua0a0" + "RS103试验配置图";
                    String RS103TestPicPath = "";
                    String RS103TestPic1Path = "";
                    String RS103TestPic2Path = "";
                    String RS103TestPic3Path = "";
                    String RS103TestPic4Path = "";
                    String RS103TestPic1Title = "";
                    String RS103TestPic2Title = "";
                    String RS103TestPic3Title = "";
                    String RS103TestPic4Title = "";
                    if (outlineData33Object.getString("项目试验图") != null && ! "".equals(outlineData33Object.getString("项目试验图"))) {
                        RS103TestPicPath = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + manageSysDevelop.getDevName() + "//" +
                                outlineData33Object.getString("项目试验图");
                    } else {
                        RS103TestPicTitle = "";
                        int currentNumber = currentPicNumber;
                        ArrayList<String> RS103TestPicList = getTestPic("RS103", outlineData33);
                        if (RS103TestPicList.contains("1")) {
                            RS103TestPic1Path = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "RS103StandardTestPic1.png";
                            RS103TestPic1Title = "图6-" + currentNumber + "\t" + "RS103试验配置图";
                            RS103TestPicTitle += "图6-" + currentNumber + "和";
                            currentNumber++;
                        }
                        if (RS103TestPicList.contains("2")) {

                            RS103TestPic2Path = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "RS103StandardTestPic2.png";
                            RS103TestPic2Title = "图6-" + currentPicNumber + currentNumber + "\t" + "RS103多天线布置（测试边界D>3m）";
                            RS103TestPicTitle += "图6-" + currentPicNumber + currentNumber + "和";
                            currentNumber++;
                        }
                        if (RS103TestPicList.contains("3")) {
                            RS103TestPic3Path = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "RS103StandardTestPic3.png";
                            RS103TestPic3Title = "图6-" + currentNumber + "\t" + "RS103多天线布置（>=200MHz）";
                            RS103TestPicTitle += "图6-" + currentNumber + "和";
                            currentNumber++;
                        }
                        if (RS103TestPicList.contains("4")) {
                            RS103TestPic4Path = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "RS103StandardTestPic4.png";
                            RS103TestPic4Title = "图6-" + currentNumber + "\t" + "RS103接收天线法布置（1GHz~40GHz）";
                            RS103TestPicTitle += "图6-" + currentNumber + "和";
                            currentNumber++;
                        }
                        RS103TestPicTitle = RS103TestPicTitle.substring(0, RS103TestPicTitle.length() - 1);
                    }
//                    Image RS103TestPic = new Image(ImageDataFactory.create(RS103TestPicPath)).
//                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
//                    currentPicNumber++;
                    if (outlineData33Object.getString("修改方法") != null && ! "".equals(outlineData33Object.getString("修改方法"))) {
                        RS103TestMethod = outlineData33Object.getString("修改方法");
                    } else {
                        RS103TestMethod = "按照" + RS103TestPicTitle + "," + "\ua0a0\ua0a0" + "和GJB151B-2013方法对RS103开展试验";
                    }
                    Paragraph RS103TestPicTitlePara = new MidParagraph(RS103TestPicTitle, font, smallSize);
                    //RS103数据处理方法
                    String RS103DataWorkMethod = outlineData33Object.getString("数据处理方法");
                    //RS103测试结果评定准则
                    String RS103TestResultAssess = outlineData33Object.getString("测试结果评定准则");

                    Paragraph RS103ProjectNamePara = new LeftParagraph(projectCatalogPrefix + "\ua0a0\ua0a0" + RS103ProjectName,
                            font, smallSize);
                    Paragraph RS103TestContentPara = new LeftParagraph(projectCatalogPrefix + ".1" + "\ua0a0\ua0a0" + "RS103试验内容" +
                            "\n\n" + RS103TestContent, font, smallSize);
                    Paragraph RS103TestTargetPara = new LeftParagraph(projectCatalogPrefix + ".2" + "\ua0a0\ua0a0" + "RS103试验目的", font, smallSize);
                    Paragraph RS103TestTargetBodyPara = new bodyParagraph(RS103TestTarget, font, smallSize);
                    Paragraph RS103TestStateAndSitePara = new LeftParagraph(projectCatalogPrefix + ".3" + "\ua0a0\ua0a0" + "RS103试验状态及测试位置" +
                            "\n\n" + RS103TestStateAndSite, font, smallSize);
                    Paragraph RS103TestMethodPara = new LeftParagraph(projectCatalogPrefix + ".4" + "\ua0a0\ua0a0" + "RS103试验方法", font, smallSize);
                    Paragraph RS103TestMethodBodyPara = new bodyParagraph(RS103TestMethod, font, smallSize);
                    Paragraph RS103DataWorkMethodPara = new LeftParagraph(projectCatalogPrefix + ".5" + "\ua0a0\ua0a0" + "RS103数据处理方法", font, smallSize);
                    Paragraph RS103DataWorkMethodBodyPara = new bodyParagraph(RS103DataWorkMethod, font, smallSize);
                    Paragraph RS103TestResultAssessPara = new LeftParagraph(projectCatalogPrefix + ".6" + "\ua0a0\ua0a0" + "RS103测试结果评定准则", font, smallSize);
                    Paragraph RS103TestResultAssessBodyPara = new bodyParagraph(RS103TestResultAssess, font, smallSize);

                    //添加RS103试验项目到pdf中 并在目录中添加页码
                    document.add(RS103ProjectNamePara);
                    catalogs.put(projectCatalogPrefix + "\ua0a0\ua0a0" + RS103ProjectName, pdf.getNumberOfPages());
                    document.add(RS103TestContentPara);
                    document.add(RS103ValuePic);
                    document.add(RS103ValuePicTitlePara);
                    document.add(RS103TestTargetPara);
                    document.add(RS103TestTargetBodyPara);
                    document.add(RS103TestStateAndSitePara);
                    document.add(RS103TestMethodPara);
                    document.add(RS103TestMethodBodyPara);
                    if (! "".equals(RS103TestPicPath)) {
                        Image RS103TestPic = new Image(ImageDataFactory.create(RS103TestPicPath)).
                                scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                        currentPicNumber++;
                        document.add(RS103TestPic);
                        document.add(RS103TestPicTitlePara);
                    } else {
                        if (! "".equals(RS103TestPic1Path)) {
                            Image RS103TestPic1 = new Image(ImageDataFactory.create(RS103TestPic1Path)).
                                    scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                            currentPicNumber++;
                            Paragraph RS103TestPic1TitlePara = new MidParagraph(RS103TestPic1Title, font, smallSize);
                            document.add(RS103TestPic1);
                            document.add(RS103TestPic1TitlePara);
                        }
                        if (! "".equals(RS103TestPic2Path)) {
                            Image RS103TestPic2 = new Image(ImageDataFactory.create(RS103TestPic2Path)).
                                    scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                            currentPicNumber++;
                            Paragraph RS103TestPic2TitlePara = new MidParagraph(RS103TestPic2Title, font, smallSize);
                            document.add(RS103TestPic2);
                            document.add(RS103TestPic2TitlePara);
                        }
                        if (! "".equals(RS103TestPic3Path)) {
                            Image RS103TestPic3 = new Image(ImageDataFactory.create(RS103TestPic3Path)).
                                    scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                            currentPicNumber++;
                            Paragraph RS103TestPic3TitlePara = new MidParagraph(RS103TestPic3Title, font, smallSize);
                            document.add(RS103TestPic3);
                            document.add(RS103TestPic3TitlePara);
                        }
                        if (! "".equals(RS103TestPic4Path)) {
                            Image RS103TestPic4 = new Image(ImageDataFactory.create(RS103TestPic4Path)).
                                    scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                            currentPicNumber++;
                            Paragraph RS103TestPic4TitlePara = new MidParagraph(RS103TestPic4Title, font, smallSize);
                            document.add(RS103TestPic4);
                            document.add(RS103TestPic4TitlePara);
                        }
                    }
                    document.add(RS103DataWorkMethodPara);
                    document.add(RS103DataWorkMethodBodyPara);
                    document.add(RS103TestResultAssessPara);
                    document.add(RS103TestResultAssessBodyPara);
                    break;
                case "RS105" :
                    String outlineData34 = manageSysOutline.getOutlineData34();
                    JSONObject outlineData34Object = JSON.parseObject(outlineData34);
                    //试验项目名
                    String RS105ProjectName = projectName + "试验";
                    //RS105试验内容 需确认限值图片
                    JSONArray RS105Array = new JSONArray();
                    RS105Array.add("RS105");
                    JSONObject RS105ValueContentObject = new GeneratePdfLimitService().getProjectLimit(RS105Array, currentPicNumber, manageSysDevelop);
                    String RS105TestContent = "试验内容：\ua0a0\ua0a0瞬态电磁场辐射敏感度" + "\n" +
                            "限值要求：见图6-" + currentPicNumber;
                    String RS105ValuePicPath = RS105ValueContentObject.getString("valuePicPath");
                    String RS105ValuePicTitle = RS105ValueContentObject.getString("valuePicTitle");
                    Paragraph RS105ValuePicTitlePara = new MidParagraph(RS105ValuePicTitle, font, smallSize);
                    Image RS105ValuePic = new Image(ImageDataFactory.create(RS105ValuePicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    //RS105试验目的
                    String RS105TestTarget = outlineData34Object.getString("试验目的");
                    //RS105试验状态及测试位置
                    String RS105TestStateAndSite = "";
                    JSONArray RS105ValidTestPortArray = getTestPortAndWorkStatus(outlineData34, 34);
                    for (int j = 0; j < RS105ValidTestPortArray.size(); j++) {
                        JSONObject validTestPortObject = RS105ValidTestPortArray.getJSONObject(j);
                        int testPortNumber = j + 1;
                        RS105TestStateAndSite = RS105TestStateAndSite + "试验端口名称：试验端口" + testPortNumber + "\n";
                        RS105TestStateAndSite = RS105TestStateAndSite + "备注：" + validTestPortObject.getString("备注") + "\n";
                        if (validTestPortObject.containsKey("工作状态")) {
                            JSONObject allWorkStatusObject = validTestPortObject.getJSONObject("工作状态");
                            for (int k = 0; k < allWorkStatusObject.size(); k++) {
                                int workStatusNumber = k + 1;
                                JSONObject workStatusObject = allWorkStatusObject.getJSONObject("工作状态" + workStatusNumber);
                                if (workStatusObject.containsKey("状态是否实施")) {
                                    if ("是".equals(workStatusObject.getString("状态是否实施"))) {
                                        RS105TestStateAndSite += "工作状态号：" + "工作状态" + workStatusNumber + "\n";
                                        RS105TestStateAndSite += "工作状态描述：" + workStatusObject.getString("工作状态描述") + "\n";
                                    }
                                }
                            }
                        }
                        RS105TestStateAndSite = RS105TestStateAndSite + "\n\n";
                    }
                    //RS105试验方法
                    String RS105TestMethod = "";
                    String RS105TestPicTitle = "图6-" + currentPicNumber + "\ua0a0\ua0a0" + "RS105试验配置图";
                    String RS105TestPicPath = "";
                    if (outlineData34Object.getString("项目试验图") != null && ! "".equals(outlineData34Object.getString("项目试验图"))) {
                        RS105TestPicPath = PathStoreEnum.WINDOWS_IMG_UPLOAD_DEST_PATH.getValue() + manageSysDevelop.getDevName() + "//" +
                                outlineData34Object.getString("项目试验图");
                    } else {
                        RS105TestPicPath = PathStoreEnum.WINDOWS_IMG_STANDARD_DEST_PATH.getValue() + "RS105StandardTestPic.png";
                    }
                    Image RS105TestPic = new Image(ImageDataFactory.create(RS105TestPicPath)).
                            scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
                    currentPicNumber++;
                    if (outlineData34Object.getString("修改方法") != null && ! "".equals(outlineData34Object.getString("修改方法"))) {
                        RS105TestMethod = outlineData34Object.getString("修改方法");
                    } else {
                        RS105TestMethod = "按照" + RS105TestPicTitle + "," + "\ua0a0\ua0a0" + "和GJB151B-2013方法对RS105开展试验";
                    }
                    Paragraph RS105TestPicTitlePara = new MidParagraph(RS105TestPicTitle, font, smallSize);
                    //RS105数据处理方法
                    String RS105DataWorkMethod = outlineData34Object.getString("数据处理方法");
                    //RS105测试结果评定准则
                    String RS105TestResultAssess = outlineData34Object.getString("测试结果评定准则");

                    Paragraph RS105ProjectNamePara = new LeftParagraph(projectCatalogPrefix + "\ua0a0\ua0a0" + RS105ProjectName,
                            font, smallSize);
                    Paragraph RS105TestContentPara = new LeftParagraph(projectCatalogPrefix + ".1" + "\ua0a0\ua0a0" + "RS105试验内容" +
                            "\n\n" + RS105TestContent, font, smallSize);
                    Paragraph RS105TestTargetPara = new LeftParagraph(projectCatalogPrefix + ".2" + "\ua0a0\ua0a0" + "RS105试验目的", font, smallSize);
                    Paragraph RS105TestTargetBodyPara = new bodyParagraph(RS105TestTarget, font, smallSize);
                    Paragraph RS105TestStateAndSitePara = new LeftParagraph(projectCatalogPrefix + ".3" + "\ua0a0\ua0a0" + "RS105试验状态及测试位置" +
                            "\n\n" + RS105TestStateAndSite, font, smallSize);
                    Paragraph RS105TestMethodPara = new LeftParagraph(projectCatalogPrefix + ".4" + "\ua0a0\ua0a0" + "RS105试验方法", font, smallSize);
                    Paragraph RS105TestMethodBodyPara = new bodyParagraph(RS105TestMethod, font, smallSize);
                    Paragraph RS105DataWorkMethodPara = new LeftParagraph(projectCatalogPrefix + ".5" + "\ua0a0\ua0a0" + "RS105数据处理方法", font, smallSize);
                    Paragraph RS105DataWorkMethodBodyPara = new bodyParagraph(RS105DataWorkMethod, font, smallSize);
                    Paragraph RS105TestResultAssessPara = new LeftParagraph(projectCatalogPrefix + ".6" + "\ua0a0\ua0a0" + "RS105测试结果评定准则", font, smallSize);
                    Paragraph RS105TestResultAssessBodyPara = new bodyParagraph(RS105TestResultAssess, font, smallSize);

                    //添加RS105试验项目到pdf中 并在目录中添加页码
                    document.add(RS105ProjectNamePara);
                    catalogs.put(projectCatalogPrefix + "\ua0a0\ua0a0" +  RS105ProjectName, pdf.getNumberOfPages());
                    document.add(RS105TestContentPara);
                    document.add(RS105ValuePic);
                    document.add(RS105ValuePicTitlePara);
                    document.add(RS105TestTargetPara);
                    document.add(RS105TestTargetBodyPara);
                    document.add(RS105TestStateAndSitePara);
                    document.add(RS105TestMethodPara);
                    document.add(RS105TestMethodBodyPara);
                    document.add(RS105TestPic);
                    document.add(RS105TestPicTitlePara);
                    document.add(RS105DataWorkMethodPara);
                    document.add(RS105DataWorkMethodBodyPara);
                    document.add(RS105TestResultAssessPara);
                    document.add(RS105TestResultAssessBodyPara);
                    break;
                default:
                    break;
            }
        }
        /** 图6-1 -- 7.1*/
        // 图6-1
//        String image6_1Name = "img6_1.png";
//        Image img6_1 = new Image(ImageDataFactory.create("src/main/resources/img/"+image6_1Name)).
//                scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
//        // 6.5.1.5 数据处理方法
//        String dataSolution = "待记录数据为被试品电源线传导发射幅度和频率的数据，该数据通过测试软件自动记录得出，并已完成所有线缆衰减、" +
//                "线路阻抗稳定网络电压分压系数、衰减器等回路设备系数的补偿，软件记录结果即为最终实测值。";
//        // 6.5.1.6 测试结果评定准则
//        String examResult = "被试品在10kHz～10MHz频率范围内传导发射实测值不超过GJB 151A-1997" +
//                "图CE102-1中AC220V基准限值的基础上整体放宽9dB，判定为合格，否则判定为不合格。";
        // CE102测量设备的名称
//        String []examMachineNames = {"频谱分析仪", "信号源", "频率计", "功率计100W", "误码仪", "直流稳压电源",
//                "可变衰减器", "固定衰减器", "数字万用表", "接地电阻表", "数字微欧计", "场强仪"};
//        Paragraph image6_1 = new Paragraph().add(img6_1).setMarginLeft(PageSize.A4.getWidth() / (16 / sizeRate));
//        MidParagraph image6_1Title = new MidParagraph("图6-1\tCE102试验配置图", font, smallSize);
//        LeftParagraph t6_5_1_5 = new LeftParagraph("6.5.1.5　数据处理方法", font, smallSize);
//        bodyParagraph c6_5_1_5 = new bodyParagraph(dataSolution, font, smallSize);
//        LeftParagraph t6_5_1_6 = new LeftParagraph("6.5.1.6　测试结果评定准则", font, smallSize);
//        bodyParagraph c6_5_1_6 = new bodyParagraph(examResult, font, smallSize);
        LeftParagraph t7 = new LeftParagraph("7　测试测量要求", font, smallSize);
//        电磁兼容性测试场地 待完成 outline-8 试验场地
        LeftParagraph t7_1 = new LeftParagraph("7.1　电磁兼容性测试场地", font, smallSize);
        float[] table7_0Width = new float[]{150, 150};
        Table table7_0 = new Table(table7_0Width).setWidthPercent(100).setMarginLeft(100).setMarginRight(100);
        table7_0.addCell(new Cell().add(new Paragraph("名称").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table7_0.addCell(new Cell().add(new Paragraph("主要性能").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 2; j++){
                table7_0.addCell(new Cell().add(new Paragraph("\n").setFont(font).setFontSize(smallSize)));
            }
        }
        LeftParagraph t7_2 = new LeftParagraph("7.2　电磁兼容性测试设备", font, smallSize);
        LeftParagraph t7_2_1 = new LeftParagraph("7.2.1　CE102测试设备", font, smallSize);
        MidParagraph table7_1Title = new MidParagraph("表7-1\tCE102测量设备", font, smallSize);
        float[] table7_1Width = new float[]{178, 119, 119, 178};
        Table table7_1 = new Table(table7_1Width).setWidthPercent(100).setMarginLeft(50).setMarginRight(50);
        table7_1.addCell(new Cell().add(new Paragraph("设备名称").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table7_1.addCell(new Cell().add(new Paragraph("主要性能指标").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table7_1.addCell(new Cell().add(new Paragraph("数量").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table7_1.addCell(new Cell().add(new Paragraph("备注").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);


//        document.add(image6_1);
//        document.add(image6_1Title);
//        document.add(t6_5_1_5);
//        catalogs.put("6.5.1.5 数据处理方法", pdf.getNumberOfPages());
//        document.add(c6_5_1_5);
//        document.add(t6_5_1_6);
//        catalogs.put("6.5.1.6 测试结果评定准则", pdf.getNumberOfPages());
//        document.add(c6_5_1_6);
        document.add(t7);
        catalogs.put("7\ua0a0\ua0a0测试测量要求", pdf.getNumberOfPages());
        document.add(t7_1);
        catalogs.put("7.1\ua0a0\ua0a0电磁兼容性测试场地", pdf.getNumberOfPages());
        document.add(table7_0);
        document.add(t7_2);
        catalogs.put("7.2\ua0a0\ua0a0电磁兼容性测试设备", pdf.getNumberOfPages());
//        document.add(t7_2_1);
//        catalogs.put("7.2.1 CE102测试设备", pdf.getNumberOfPages());
//        document.add(table7_1Title);
//        document.add(table7_1);
        //添加项目测试设备表
        for (int i = 0; i < projectList.size(); i++) {
            String catalogPrefix = "7.2.";
            String projectName = projectList.get(i);
            float[] tableTestEqpWidth = new float[]{178, 119, 119, 178};
            Table tableTestEqp = new Table(tableTestEqpWidth).setWidthPercent(100).setMarginLeft(50).setMarginRight(50);
            tableTestEqp.addCell(new Cell().add(new Paragraph("设备名称").setFont(font).
                    setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
            tableTestEqp.addCell(new Cell().add(new Paragraph("主要性能指标").setFont(font).
                    setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
            tableTestEqp.addCell(new Cell().add(new Paragraph("数量").setFont(font).
                    setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
            tableTestEqp.addCell(new Cell().add(new Paragraph("备注").setFont(font).
                    setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
            switch (projectName) {
//              CE101测试设备表
                case "CE101" :
                    String outlineData35 = manageSysOutline.getOutlineData35();
                    JSONObject outlineData35Object = JSON.parseObject(outlineData35);
                    JSONArray CE101EqpArray = outlineData35Object.getJSONArray("CE101 测试设备");
                    for (int j = 0; j < CE101EqpArray.size(); j++) {
                        JSONObject CE101Eqp = CE101EqpArray.getJSONObject(j);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CE101Eqp.getString("设备名称"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CE101Eqp.getString("主要性能指标"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CE101Eqp.getString("数量"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CE101Eqp.getString("备注"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                    }
                    Paragraph CE101Eqpcatalog = new LeftParagraph(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CE101\ua0a0\ua0a0测试设备", font, smallSize);
                    Paragraph CE101EqpTableTitle = new MidParagraph("表7-" + String.valueOf(i + 1) + "\t" + "CE101\ua0a0测试设备", font, smallSize);
                    document.add(CE101Eqpcatalog);
                    catalogs.put(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CE101\ua0a0测试设备", pdf.getNumberOfPages());
                    document.add(CE101EqpTableTitle);
                    document.add(tableTestEqp);
                    break;
//                CE102测试设备表
                case "CE102" :
                    String outlineData36 = manageSysOutline.getOutlineData36();
                    JSONObject outlineData36Object = JSON.parseObject(outlineData36);
                    JSONArray CE102EqpArray = outlineData36Object.getJSONArray("CE102 测试设备");
                    for (int j = 0; j < CE102EqpArray.size(); j++) {
                        JSONObject CE102Eqp = CE102EqpArray.getJSONObject(j);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CE102Eqp.getString("设备名称"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CE102Eqp.getString("主要性能指标"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CE102Eqp.getString("数量"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CE102Eqp.getString("备注"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                    }
                    Paragraph CE102Eqpcatalog = new LeftParagraph(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CE102\ua0a0\ua0a0测试设备", font, smallSize);
                    Paragraph CE102EqpTableTitle = new MidParagraph("表7-" + String.valueOf(i + 1) + "\t" + "CE102\ua0a0测试设备", font, smallSize);
                    document.add(CE102Eqpcatalog);
                    catalogs.put(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CE102\ua0a0测试设备", pdf.getNumberOfPages());
                    document.add(CE102EqpTableTitle);
                    document.add(tableTestEqp);
                    break;
//                CE106测试设备
                case "CE106" :
                    String outlineData37 = manageSysOutline.getOutlineData37();
                    JSONObject outlineData37Object = JSON.parseObject(outlineData37);
                    JSONArray CE106EqpArray = outlineData37Object.getJSONArray("CE106 测试设备");
                    for (int j = 0; j < CE106EqpArray.size(); j++) {
                        JSONObject CE106Eqp = CE106EqpArray.getJSONObject(j);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CE106Eqp.getString("设备名称"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CE106Eqp.getString("主要性能指标"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CE106Eqp.getString("数量"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CE106Eqp.getString("备注"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                    }
                    Paragraph CE106Eqpcatalog = new LeftParagraph(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CE106\ua0a0\ua0a0测试设备", font, smallSize);
                    Paragraph CE106EqpTableTitle = new MidParagraph("表7-" + String.valueOf(i + 1) + "\t" + "CE106\ua0a0测试设备", font, smallSize);
                    document.add(CE106Eqpcatalog);
                    catalogs.put(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CE106\ua0a0测试设备", pdf.getNumberOfPages());
                    document.add(CE106EqpTableTitle);
                    document.add(tableTestEqp);
                    break;
                //                    CE107测试设备
                case "CE107" :
                    String outlineData38 = manageSysOutline.getOutlineData38();
                    JSONObject outlineData38Object = JSON.parseObject(outlineData38);
                    JSONArray CE107EqpArray = outlineData38Object.getJSONArray("CE107 测试设备");
                    for (int j = 0; j < CE107EqpArray.size(); j++) {
                        JSONObject CE107Eqp = CE107EqpArray.getJSONObject(j);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CE107Eqp.getString("设备名称"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CE107Eqp.getString("主要性能指标"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CE107Eqp.getString("数量"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CE107Eqp.getString("备注"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                    }
                    Paragraph CE107Eqpcatalog = new LeftParagraph(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CE107\ua0a0\ua0a0测试设备", font, smallSize);
                    Paragraph CE107EqpTableTitle = new MidParagraph("表7-" + String.valueOf(i + 1) + "\t" + "CE107\ua0a0测试设备", font, smallSize);
                    document.add(CE107Eqpcatalog);
                    catalogs.put(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CE107\ua0a0测试设备", pdf.getNumberOfPages());
                    document.add(CE107EqpTableTitle);
                    document.add(tableTestEqp);
                    break;

//                    CS101测试设备
                case "CS101" :
                    String outlineData39 = manageSysOutline.getOutlineData39();
                    JSONObject outlineData39Object = JSON.parseObject(outlineData39);
                    JSONArray CS101EqpArray = outlineData39Object.getJSONArray("CS101 测试设备");
                    for (int j = 0; j < CS101EqpArray.size(); j++) {
                        JSONObject CS101Eqp = CS101EqpArray.getJSONObject(j);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS101Eqp.getString("设备名称"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS101Eqp.getString("主要性能指标"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS101Eqp.getString("数量"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS101Eqp.getString("备注"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                    }
                    Paragraph CS101Eqpcatalog = new LeftParagraph(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CS101\ua0a0\ua0a0测试设备", font, smallSize);
                    Paragraph CS101EqpTableTitle = new MidParagraph("表7-" + String.valueOf(i + 1) + "\t" + "CS101\ua0a0测试设备", font, smallSize);
                    document.add(CS101Eqpcatalog);
                    catalogs.put(catalogPrefix + String.valueOf(i + 1) +  "\ua0a0\ua0a0" + "CS101\ua0a0测试设备", pdf.getNumberOfPages());
                    document.add(CS101EqpTableTitle);
                    document.add(tableTestEqp);
                    break;
                //                    CS101测试设备
                case "CS102" :
                    String outlineData40 = manageSysOutline.getOutlineData40();
                    JSONObject outlineData40Object = JSON.parseObject(outlineData40);
                    JSONArray CS102EqpArray = outlineData40Object.getJSONArray("CS102 测试设备");
                    for (int j = 0; j < CS102EqpArray.size(); j++) {
                        JSONObject CS102Eqp = CS102EqpArray.getJSONObject(j);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS102Eqp.getString("设备名称"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS102Eqp.getString("主要性能指标"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS102Eqp.getString("数量"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS102Eqp.getString("备注"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                    }
                    Paragraph CS102Eqpcatalog = new LeftParagraph(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CS102\ua0a0\ua0a0测试设备", font, smallSize);
                    Paragraph CS102EqpTableTitle = new MidParagraph("表7-" + String.valueOf(i + 1) + "\t" + "CS102\ua0a0测试设备", font, smallSize);
                    document.add(CS102Eqpcatalog);
                    catalogs.put(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CS102\ua0a0测试设备", pdf.getNumberOfPages());
                    document.add(CS102EqpTableTitle);
                    document.add(tableTestEqp);
                    break;
                //                    CS103测试设备
                case "CS103" :
                    String outlineData41 = manageSysOutline.getOutlineData41();
                    JSONObject outlineData41Object = JSON.parseObject(outlineData41);
                    JSONArray CS103EqpArray = outlineData41Object.getJSONArray("CS103 测试设备");
                    for (int j = 0; j < CS103EqpArray.size(); j++) {
                        JSONObject CS103Eqp = CS103EqpArray.getJSONObject(j);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS103Eqp.getString("设备名称"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS103Eqp.getString("主要性能指标"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS103Eqp.getString("数量"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS103Eqp.getString("备注"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                    }
                    Paragraph CS103Eqpcatalog = new LeftParagraph(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CS103\ua0a0\ua0a0测试设备", font, smallSize);
                    Paragraph CS103EqpTableTitle = new MidParagraph("表7-" + String.valueOf(i + 1) + "\t" + "CS103\ua0a0测试设备", font, smallSize);
                    document.add(CS103Eqpcatalog);
                    catalogs.put(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CS103\ua0a0测试设备", pdf.getNumberOfPages());
                    document.add(CS103EqpTableTitle);
                    document.add(tableTestEqp);
                    break;
                //CS104测试设备
                case "CS104" :
                    String outlineData42 = manageSysOutline.getOutlineData42();
                    JSONObject outlineData42Object = JSON.parseObject(outlineData42);
                    JSONArray CS104EqpArray = outlineData42Object.getJSONArray("CS104 测试设备");
                    for (int j = 0; j < CS104EqpArray.size(); j++) {
                        JSONObject CS104Eqp = CS104EqpArray.getJSONObject(j);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS104Eqp.getString("设备名称"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS104Eqp.getString("主要性能指标"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS104Eqp.getString("数量"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS104Eqp.getString("备注"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                    }
                    Paragraph CS104Eqpcatalog = new LeftParagraph(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CS104\ua0a0\ua0a0测试设备", font, smallSize);
                    Paragraph CS104EqpTableTitle = new MidParagraph("表7-" + String.valueOf(i + 1) + "\t" + "CS104\ua0a0测试设备", font, smallSize);
                    document.add(CS104Eqpcatalog);
                    catalogs.put(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CS104\ua0a0测试设备", pdf.getNumberOfPages());
                    document.add(CS104EqpTableTitle);
                    document.add(tableTestEqp);
                    break;
                //                    CS101测试设备
                case "CS105" :
                    String outlineData43 = manageSysOutline.getOutlineData43();
                    JSONObject outlineData43Object = JSON.parseObject(outlineData43);
                    JSONArray CS105EqpArray = outlineData43Object.getJSONArray("CS105 测试设备");
                    for (int j = 0; j < CS105EqpArray.size(); j++) {
                        JSONObject CS105Eqp = CS105EqpArray.getJSONObject(j);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS105Eqp.getString("设备名称"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS105Eqp.getString("主要性能指标"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS105Eqp.getString("数量"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS105Eqp.getString("备注"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                    }
                    Paragraph CS105Eqpcatalog = new LeftParagraph(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CS105\ua0a0\ua0a0测试设备", font, smallSize);
                    Paragraph CS105EqpTableTitle = new MidParagraph("表7-" + String.valueOf(i + 1) + "\t" + "CS105\ua0a0测试设备", font, smallSize);
                    document.add(CS105Eqpcatalog);
                    catalogs.put(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CS105\ua0a0测试设备", pdf.getNumberOfPages());
                    document.add(CS105EqpTableTitle);
                    document.add(tableTestEqp);
                    break;
                //                    CS106测试设备
                case "CS106" :
                    String outlineData44 = manageSysOutline.getOutlineData44();
                    JSONObject outlineData44Object = JSON.parseObject(outlineData44);
                    JSONArray CS106EqpArray = outlineData44Object.getJSONArray("CS106 测试设备");
                    for (int j = 0; j < CS106EqpArray.size(); j++) {
                        JSONObject CS106Eqp = CS106EqpArray.getJSONObject(j);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS106Eqp.getString("设备名称"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS106Eqp.getString("主要性能指标"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS106Eqp.getString("数量"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS106Eqp.getString("备注"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                    }
                    Paragraph CS106Eqpcatalog = new LeftParagraph(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CS106\ua0a0\ua0a0测试设备", font, smallSize);
                    Paragraph CS106EqpTableTitle = new MidParagraph("表7-" + String.valueOf(i + 1) + "\t" + "CS106\ua0a0测试设备", font, smallSize);
                    document.add(CS106Eqpcatalog);
                    catalogs.put(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CS106\ua0a0测试设备", pdf.getNumberOfPages());
                    document.add(CS106EqpTableTitle);
                    document.add(tableTestEqp);
                    break;
                //                    CS109测试设备
                case "CS109" :
                    String outlineData45 = manageSysOutline.getOutlineData45();
                    JSONObject outlineData45Object = JSON.parseObject(outlineData45);
                    JSONArray CS109EqpArray = outlineData45Object.getJSONArray("CS109 测试设备");
                    for (int j = 0; j < CS109EqpArray.size(); j++) {
                        JSONObject CS109Eqp = CS109EqpArray.getJSONObject(j);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS109Eqp.getString("设备名称"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS109Eqp.getString("主要性能指标"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS109Eqp.getString("数量"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS109Eqp.getString("备注"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                    }
                    Paragraph CS109Eqpcatalog = new LeftParagraph(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CS109\ua0a0\ua0a0测试设备", font, smallSize);
                    Paragraph CS109EqpTableTitle = new MidParagraph("表7-" + String.valueOf(i + 1) + "\t" + "CS109\ua0a0测试设备", font, smallSize);
                    document.add(CS109Eqpcatalog);
                    catalogs.put(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CS109\ua0a0测试设备", pdf.getNumberOfPages());
                    document.add(CS109EqpTableTitle);
                    document.add(tableTestEqp);
                    break;
                //                    CS112测试设备
                case "CS112" :
                    String outlineData46 = manageSysOutline.getOutlineData46();
                    JSONObject outlineData46Object = JSON.parseObject(outlineData46);
                    JSONArray CS112EqpArray = outlineData46Object.getJSONArray("CS112 测试设备");
                    for (int j = 0; j < CS112EqpArray.size(); j++) {
                        JSONObject CS112Eqp = CS112EqpArray.getJSONObject(j);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS112Eqp.getString("设备名称"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS112Eqp.getString("主要性能指标"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS112Eqp.getString("数量"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS112Eqp.getString("备注"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                    }
                    Paragraph CS112Eqpcatalog = new LeftParagraph(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CS112\ua0a0\ua0a0测试设备", font, smallSize);
                    Paragraph CS112EqpTableTitle = new MidParagraph("表7-" + String.valueOf(i + 1) + "\t" + "CS112\ua0a0测试设备", font, smallSize);
                    document.add(CS112Eqpcatalog);
                    catalogs.put(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CS112\ua0a0测试设备", pdf.getNumberOfPages());
                    document.add(CS112EqpTableTitle);
                    document.add(tableTestEqp);
                    break;
                //                    CS112测试设备
                case "CS114" :
                    String outlineData47 = manageSysOutline.getOutlineData47();
                    JSONObject outlineData47Object = JSON.parseObject(outlineData47);
                    JSONArray CS114EqpArray = outlineData47Object.getJSONArray("CS114 测试设备");
                    for (int j = 0; j < CS114EqpArray.size(); j++) {
                        JSONObject CS114Eqp = CS114EqpArray.getJSONObject(j);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS114Eqp.getString("设备名称"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS114Eqp.getString("主要性能指标"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS114Eqp.getString("数量"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS114Eqp.getString("备注"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                    }
                    Paragraph CS114Eqpcatalog = new LeftParagraph(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CS114\ua0a0\ua0a0测试设备", font, smallSize);
                    Paragraph CS114EqpTableTitle = new MidParagraph("表7-" + String.valueOf(i + 1) + "\t" + "CS114\ua0a0测试设备", font, smallSize);
                    document.add(CS114Eqpcatalog);
                    catalogs.put(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CS114\ua0a0测试设备", pdf.getNumberOfPages());
                    document.add(CS114EqpTableTitle);
                    document.add(tableTestEqp);
                    break;
                //                    CS115测试设备
                case "CS115" :
                    String outlineData48 = manageSysOutline.getOutlineData48();
                    JSONObject outlineData48Object = JSON.parseObject(outlineData48);
                    JSONArray CS115EqpArray = outlineData48Object.getJSONArray("CS115 测试设备");
                    for (int j = 0; j < CS115EqpArray.size(); j++) {
                        JSONObject CS115Eqp = CS115EqpArray.getJSONObject(j);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS115Eqp.getString("设备名称"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS115Eqp.getString("主要性能指标"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS115Eqp.getString("数量"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS115Eqp.getString("备注"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                    }
                    Paragraph CS115Eqpcatalog = new LeftParagraph(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CS115\ua0a0\ua0a0测试设备", font, smallSize);
                    Paragraph CS115EqpTableTitle = new MidParagraph("表7-" + String.valueOf(i + 1) + "\t" + "CS115\ua0a0测试设备", font, smallSize);
                    document.add(CS115Eqpcatalog);
                    catalogs.put(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CS115\ua0a0测试设备", pdf.getNumberOfPages());
                    document.add(CS115EqpTableTitle);
                    document.add(tableTestEqp);
                    break;
                //                    CS116测试设备
                case "CS116" :
                    String outlineData49 = manageSysOutline.getOutlineData49();
                    JSONObject outlineData49Object = JSON.parseObject(outlineData49);
                    JSONArray CS116EqpArray = outlineData49Object.getJSONArray("CS116 测试设备");
                    for (int j = 0; j < CS116EqpArray.size(); j++) {
                        JSONObject CS116Eqp = CS116EqpArray.getJSONObject(j);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS116Eqp.getString("设备名称"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS116Eqp.getString("主要性能指标"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS116Eqp.getString("数量"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(CS116Eqp.getString("备注"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                    }
                    Paragraph CS116Eqpcatalog = new LeftParagraph(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CS116\ua0a0\ua0a0测试设备", font, smallSize);
                    Paragraph CS116EqpTableTitle = new MidParagraph("表7-" + String.valueOf(i + 1) + "\t" + "CS116\ua0a0测试设备", font, smallSize);
                    document.add(CS116Eqpcatalog);
                    catalogs.put(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "CS116\ua0a0测试设备", pdf.getNumberOfPages());
                    document.add(CS116EqpTableTitle);
                    document.add(tableTestEqp);
                    break;
                //                    RE101测试设备
                case "RE101" :
                    String outlineData50 = manageSysOutline.getOutlineData50();
                    JSONObject outlineData50Object = JSON.parseObject(outlineData50);
                    JSONArray RE101EqpArray = outlineData50Object.getJSONArray("RE101 测试设备");
                    for (int j = 0; j < RE101EqpArray.size(); j++) {
                        JSONObject RE101Eqp = RE101EqpArray.getJSONObject(j);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RE101Eqp.getString("设备名称"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RE101Eqp.getString("主要性能指标"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RE101Eqp.getString("数量"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RE101Eqp.getString("备注"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                    }
                    Paragraph RE101Eqpcatalog = new LeftParagraph(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "RE101\ua0a0\ua0a0测试设备", font, smallSize);
                    Paragraph RE101EqpTableTitle = new MidParagraph("表7-" + String.valueOf(i + 1) + "\t" + "RE101\ua0a0测试设备", font, smallSize);
                    document.add(RE101Eqpcatalog);
                    catalogs.put(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "RE101\ua0a0测试设备", pdf.getNumberOfPages());
                    document.add(RE101EqpTableTitle);
                    document.add(tableTestEqp);
                    break;
                //                    RE102测试设备
                case "RE102" :
                    String outlineData51 = manageSysOutline.getOutlineData51();
                    JSONObject outlineData51Object = JSON.parseObject(outlineData51);
                    JSONArray RE102EqpArray = outlineData51Object.getJSONArray("RE102 测试设备");
                    for (int j = 0; j < RE102EqpArray.size(); j++) {
                        JSONObject RE102Eqp = RE102EqpArray.getJSONObject(j);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RE102Eqp.getString("设备名称"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RE102Eqp.getString("主要性能指标"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RE102Eqp.getString("数量"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RE102Eqp.getString("备注"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                    }
                    Paragraph RE102Eqpcatalog = new LeftParagraph(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "RE102\ua0a0\ua0a0测试设备", font, smallSize);
                    Paragraph RE102EqpTableTitle = new MidParagraph("表7-" + String.valueOf(i + 1) + "\t" + "RE102\ua0a0测试设备", font, smallSize);
                    document.add(RE102Eqpcatalog);
                    catalogs.put(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "RE102\ua0a0测试设备", pdf.getNumberOfPages());
                    document.add(RE102EqpTableTitle);
                    document.add(tableTestEqp);
                    break;
                //                    RE103测试设备
                case "RE103" :
                    String outlineData52 = manageSysOutline.getOutlineData52();
                    JSONObject outlineData52Object = JSON.parseObject(outlineData52);
                    JSONArray RE103EqpArray = outlineData52Object.getJSONArray("RE103 测试设备");
                    for (int j = 0; j < RE103EqpArray.size(); j++) {
                        JSONObject RE103Eqp = RE103EqpArray.getJSONObject(j);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RE103Eqp.getString("设备名称"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RE103Eqp.getString("主要性能指标"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RE103Eqp.getString("数量"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RE103Eqp.getString("备注"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                    }
                    Paragraph RE103Eqpcatalog = new LeftParagraph(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "RE103\ua0a0\ua0a0测试设备", font, smallSize);
                    Paragraph RE103EqpTableTitle = new MidParagraph("表7-" + String.valueOf(i + 1) + "\t" + "RE103\ua0a0测试设备", font, smallSize);
                    document.add(RE103Eqpcatalog);
                    catalogs.put(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "RE103\ua0a0测试设备", pdf.getNumberOfPages());
                    document.add(RE103EqpTableTitle);
                    document.add(tableTestEqp);
                    break;
                //                    RS101测试设备
                case "RS101" :
                    String outlineData53 = manageSysOutline.getOutlineData53();
                    JSONObject outlineData53Object = JSON.parseObject(outlineData53);
                    JSONArray RS101EqpArray = outlineData53Object.getJSONArray("RS101 测试设备");
                    for (int j = 0; j < RS101EqpArray.size(); j++) {
                        JSONObject RS101Eqp = RS101EqpArray.getJSONObject(j);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RS101Eqp.getString("设备名称"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RS101Eqp.getString("主要性能指标"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RS101Eqp.getString("数量"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RS101Eqp.getString("备注"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                    }
                    Paragraph RS101Eqpcatalog = new LeftParagraph(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "RS101\ua0a0\ua0a0测试设备", font, smallSize);
                    Paragraph RS101EqpTableTitle = new MidParagraph("表7-" + String.valueOf(i + 1) + "\t" + "RS101\ua0a0测试设备", font, smallSize);
                    document.add(RS101Eqpcatalog);
                    catalogs.put(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "RS101\ua0a0测试设备", pdf.getNumberOfPages());
                    document.add(RS101EqpTableTitle);
                    document.add(tableTestEqp);
                    break;
                //                    RS103测试设备
                case "RS103" :
                    String outlineData54 = manageSysOutline.getOutlineData54();
                    JSONObject outlineData54Object = JSON.parseObject(outlineData54);
                    JSONArray RS103EqpArray = outlineData54Object.getJSONArray("RS103 测试设备");
                    for (int j = 0; j < RS103EqpArray.size(); j++) {
                        JSONObject RS103Eqp = RS103EqpArray.getJSONObject(j);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RS103Eqp.getString("设备名称"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RS103Eqp.getString("主要性能指标"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RS103Eqp.getString("数量"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RS103Eqp.getString("备注"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                    }
                    Paragraph RS103Eqpcatalog = new LeftParagraph(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "RS103\ua0a0\ua0a0测试设备", font, smallSize);
                    Paragraph RS103EqpTableTitle = new MidParagraph("表7-" + String.valueOf(i + 1) + "\t" + "RS103\ua0a0测试设备", font, smallSize);
                    document.add(RS103Eqpcatalog);
                    catalogs.put(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "RS103\ua0a0测试设备", pdf.getNumberOfPages());
                    document.add(RS103EqpTableTitle);
                    document.add(tableTestEqp);
                    break;
                //                    RS105测试设备
                case "RS105" :
                    String outlineData55 = manageSysOutline.getOutlineData55();
                    JSONObject outlineData55Object = JSON.parseObject(outlineData55);
                    JSONArray RS105EqpArray = outlineData55Object.getJSONArray("RS105 测试设备");
                    for (int j = 0; j < RS105EqpArray.size(); j++) {
                        JSONObject RS105Eqp = RS105EqpArray.getJSONObject(j);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RS105Eqp.getString("设备名称"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RS105Eqp.getString("主要性能指标"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RS105Eqp.getString("数量"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                        tableTestEqp.addCell(new Cell().add(new NotNullParagraph(RS105Eqp.getString("备注"), font, smallSize)))
                                .setTextAlignment(TextAlignment.CENTER);
                    }
                    Paragraph RS105Eqpcatalog = new LeftParagraph(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "RS105\ua0a0\ua0a0测试设备", font, smallSize);
                    Paragraph RS105EqpTableTitle = new MidParagraph("表7-" + String.valueOf(i + 1) + "\t" + "RS105\ua0a0测试设备", font, smallSize);
                    document.add(RS105Eqpcatalog);
                    catalogs.put(catalogPrefix + String.valueOf(i + 1) + "\ua0a0\ua0a0" + "RS105\ua0a0测试设备", pdf.getNumberOfPages());
                    document.add(RS105EqpTableTitle);
                    document.add(tableTestEqp);
                    break;
                    default:
                        break;
            }
        }

        /** 7.2 -- 14*/
        // 8 与标准的偏离说明
        String plusMinusParts = "试验增减项：方法偏离、布置偏离。";
        // 9.1.1 非责任故障判定
        String nonresponsErrorDisgust = "试验过程中，只有下列情况可判为非责任故障；\n" +
                "a)\t误操作引起的受试产品故障；\n" +
                "b)\t试验装置及测试仪表故障引起的受试产品故障；\n" +
                "c)\t试验设备引起的受试产品故障；\n" +
                "d)\t超出产品工作极限的环境条件和工作条件引起的受试产品故障；\n" +
                "e)\t修复过程中引入的故障。";
        // 责任故障判定
        String responseErrorDisgust = "除可判定为非责任的故障以外，其它所有故障判定为责任故障，如：\n" +
                "a)\t由于设计缺陷或制造工艺不良而造成的故障；\n" +
                "b)\t由于元器件潜在缺陷致使元器件失效而造成的故障；\n" +
                "c)\t间歇故障；\n" +
                "d)\t超出技术规范正常范围的调整；\n" +
                "e)\t试验期间所有非从属性故障原因引起的出现故障征兆（未超出性能极限）而引起的更换；\n" +
                "f)\t无法证实的异常情况。";
        // 试验中断处理
//        String examStopHandle = "试验过程中出现下列情形之一时，承试单位应中断试验，同时通知有关单位；\n" +
//                "a)\t出现安全、保密事故征兆；\n" +
//                "b)\t试验结果已判定关键战术技术指标达不到要求；\n" +
//                "c)\t出现影响性能和使用的重大技术问题；\n" +
//                "d)\t出现短期内难以排除的故障。";
        String outlineData56 = manageSysOutline.getOutlineData56();
        JSONObject outlineData56Object = JSON.parseObject(outlineData56);
        String examStopHandle = outlineData56Object.getString("试验中断条件");
        // 故障处理
        String errorHandle = "当确认受试产品发生故障时，应按相关规定将故障信息及时记入“故障报告表”、" +
                "“故障分析报告表”和“故障纠正措施报告表”。\n\t若经分析认为针对发生的故障采取的纠正措施不影响前期试验结果，" +
                "发生故障的试验项目应重做；若采取的纠正措施影响到前期试验结果，则发生故障的试验项目和受影响的试验项目均应重做。";
        // 试验恢复处理
//        String recoverHandle = "承研承制单位对试验中暴露的问题采取改进措施，经试验验证其他有关单位确认问题已解决，" +
//                "承试单位提出恢复或重新试验的申请，经批准后由原承试单位实施试验。";
        String recoverHandle = outlineData56Object.getString("试验回复条件");
        // 试验数据记录要求  就是试验报告编制要求
        String outlineData57 = manageSysOutline.getOutlineData57();
        JSONObject outlineData57Object = JSON.parseObject(outlineData57);
//        String dataLogReq = "应明确试验期间试验数据记录要求，试验数据主要包括试验样品信息和状态记录、" +
//                "试验设备和检测仪器信息及校准状态、样品连接、试验样品响应和功能性能测试数据、试验中断处理信息等。";
        String dataLogReq = outlineData57Object.getString("试验报告编制要求");
        // 试验组织及任务分工
//        String orgAndDivision = "为确保试验顺利实施，应成立试验工作组，对试验过程进行管理和监控。\n" +
//                "\t由工业和信息化部电子第四研究院（简称“电子四院”）等单位共同成立电磁兼容性试验工作组。\n" +
//                "\t组长单位：工业和信息化部电子第四研究院；\n" +
//                "\t副组长单位：XXXX；\n" +
//                "\t成员单位：XXXXXX；\n" +
//                "\t根据功能性能试验工作组结构，本次试验任务分工如下：\n" +
//                "\tXXXXXXX";
        String orgAndDivision = outlineData57Object.getString("试验组织及任务分工");
        // 试验保障
//        String examSafeguard = "a)\t环境条件符合试验要求；\n" +
//                "\tb)\t试验场地满足测试要求；\n" +
//                "\tc)\t所有试验及试验设备的技术保障工作，由工业和信息化部电子第四研究院统一负责；\n" +
//                "\td)\t试验期间地面设备间的通信保障工作由工业和信息化部电子第四研究院提供；\n" +
//                "\te)\t试验期间的录像、照相工作由工业和信息化部电子第四研究院承担。";
        String examSafeguard = outlineData57Object.getString("试验保障");
        // 试验安全
//        String examSafety = "所有参加试验的人员必须经过登记，试验中的操作按照试验分工要求由相应人员依据操作规范执行。" +
//                "试验安全保证要求如下：\n" +
//                "\ta)\t试验现场应规范、整洁，特别是用电设备及电缆使用、放置合理，不出现拉扯、绞缠的现象；\n" +
//                "\tb)\t试验设备由专人操作，试验过程中，无关人员不在试验现场来回走动、不随意操作试验设备；\n" +
//                "\tc)\t对试验过程进行监控，确保能够及时发现并处理问题；\n" +
//                "\td)\t试验现场应有防护措施。\n" +
//                "\te)\t试验前，严格按照试验配置图进行配置并检查确认；\n" +
//                "\tf)\t在试验过程中，各级人员按照职责分工进行相应操作。";
        String examSafety = outlineData57Object.getString("试验安全");
        //保密要求与措施
        String secretProtect = outlineData57Object.getString("保密措施与要求");
        // 试验的其它要求和有关问题说明
//        String otherReq = "无";
        String otherReq = outlineData57Object.getString("有关问题的说明");
//        LeftParagraph t7_2_2 = new LeftParagraph("7.2.2　RE102测试设备", font, smallSize);
        LeftParagraph t8 = new LeftParagraph("8\ua0a0\ua0a0与标准的偏离说明", font, smallSize);
        Paragraph table8_1Title = new MidParagraph("表8-1\t标准剪裁与偏离说明", font, smallSize);
        //标准剪裁与偏离说明表
        float[] table8_1Width = new float[]{80, 120, 120};
        Table table8_1 = new Table(table8_1Width).setWidthPercent(100);
        table8_1.addCell(new Cell().add(new NotNullParagraph("", font, smallSize))).setTextAlignment(TextAlignment.CENTER);
        table8_1.addCell(new Cell().add(new NotNullParagraph("内容", font, smallSize))).setTextAlignment(TextAlignment.CENTER);
        table8_1.addCell(new Cell().add(new NotNullParagraph("理由", font, smallSize))).setTextAlignment(TextAlignment.CENTER);
        String outlineData59 = manageSysOutline.getOutlineData59();
        JSONObject outlineData59Object = JSON.parseObject(outlineData59);
        JSONArray standardCutArray = outlineData59Object.getJSONArray("标准剪裁与偏离说明");
        if (standardCutArray != null) {
            for (int i = 0; i < standardCutArray.size(); i++) {
                JSONObject standardCurObject = standardCutArray.getJSONObject(i);
                table8_1.addCell(new Cell().add(new NotNullParagraph(standardCurObject.getString(""), font, smallSize)));
                table8_1.addCell(new Cell().add(new NotNullParagraph(standardCurObject.getString("内容"), font, smallSize)));
                table8_1.addCell(new Cell().add(new NotNullParagraph(standardCurObject.getString("理由"), font, smallSize)));

            }
        }

        LeftParagraph t9 = new LeftParagraph("9\ua0a0\ua0a0试验的中断处理与恢复", font, smallSize);
        /** 红色部分*/
        LeftParagraph t9_1 = new LeftParagraph("9.1\ua0a0\ua0a0故障分类", font, smallSize);
        LeftParagraph t9_1_1 = new LeftParagraph("9.1.1\ua0a0\ua0a0非责任故障的判定", font, smallSize);
        bodyParagraph c9_1_1 = new bodyParagraph(nonresponsErrorDisgust, font, smallSize);
        LeftParagraph t9_1_2 = new LeftParagraph("9.1.2\ua0a0\ua0a0责任故障的判定", font, smallSize);
        bodyParagraph c9_1_2 = new bodyParagraph(responseErrorDisgust, font, smallSize);
        LeftParagraph t9_2 = new LeftParagraph("9.2\ua0a0\ua0a0试验中断处理", font, smallSize);
        bodyParagraph c9_2 = new bodyParagraph(examStopHandle, font, smallSize);
        LeftParagraph t9_3 = new LeftParagraph("9.3\ua0a0\ua0a0故障处理", font, smallSize);
        bodyParagraph c9_3 = new bodyParagraph(errorHandle, font, smallSize);
        LeftParagraph t9_4 = new LeftParagraph("9.4\ua0a0\ua0a0试验恢复处理", font, smallSize);
        bodyParagraph c9_4 = new bodyParagraph(recoverHandle, font, smallSize);
        LeftParagraph t9_5 = new LeftParagraph("9.5\ua0a0\ua0a0试验报告编制要求", font, smallSize);
        bodyParagraph c9_5 = new bodyParagraph(dataLogReq, font, smallSize);
        LeftParagraph t10 = new LeftParagraph("10\ua0a0\ua0a0试验组织及任务分工", font, smallSize);
        bodyParagraph c10 = new bodyParagraph(orgAndDivision, font, smallSize);
        LeftParagraph t11 = new LeftParagraph("11\ua0a0\ua0a0试验保障", font, smallSize);
        bodyParagraph c11 = new bodyParagraph(examSafeguard, font, smallSize);
        LeftParagraph t12 = new LeftParagraph("12\ua0a0\ua0a0试验安全", font, smallSize);
        bodyParagraph c12 = new bodyParagraph(examSafety, font, smallSize);
        LeftParagraph t13 = new LeftParagraph("13\ua0a0\ua0a0保密要求与措施", font, smallSize);
        bodyParagraph c13 = new bodyParagraph(secretProtect, font, smallSize);
        LeftParagraph t14 = new LeftParagraph("14\ua0a0\ua0a0试验的其它要求和有关问题说明", font, smallSize);
        bodyParagraph c14 = new bodyParagraph(otherReq, font, smallSize);


        document.add(t8);
        catalogs.put("8\ua0a0\ua0a0与标准的偏离说明", pdf.getNumberOfPages());
//        document.add(c8);
        document.add(table8_1Title);
        document.add(table8_1);
        document.add(t9);
        catalogs.put("9\ua0a0\ua0a0试验的中断处理与恢复", pdf.getNumberOfPages());
        document.add(t9_1);
        catalogs.put("9.1\ua0a0\ua0a0故障分类", pdf.getNumberOfPages());
        document.add(t9_1_1);
        catalogs.put("9.1.1\ua0a0\ua0a0非责任故障的判定", pdf.getNumberOfPages());
        document.add(c9_1_1);
        document.add(t9_1_2);
        catalogs.put("9.1.2\ua0a0\ua0a0责任故障的判定", pdf.getNumberOfPages());
        document.add(c9_1_2);
        document.add(t9_2);
        catalogs.put("9.2\ua0a0\ua0a0试验中断处理", pdf.getNumberOfPages());
        document.add(c9_2);
        document.add(t9_3);
        catalogs.put("9.3\ua0a0\ua0a0故障处理", pdf.getNumberOfPages());
        document.add(c9_3);
        document.add(t9_4);
        catalogs.put("9.4\ua0a0\ua0a0试验恢复处理", pdf.getNumberOfPages());
        document.add(c9_4);
        document.add(t9_5);
        catalogs.put("9.5\ua0a0\ua0a0试验报告编制要求", pdf.getNumberOfPages());
        document.add(c9_5);
        document.add(t10);
        catalogs.put("10\ua0a0\ua0a0试验组织及任务分工", pdf.getNumberOfPages());
        document.add(c10);
        document.add(t11);
        catalogs.put("11\ua0a0\ua0a0试验保障", pdf.getNumberOfPages());
        document.add(c11);
        document.add(t12);
        catalogs.put("12\ua0a0\ua0a0试验安全", pdf.getNumberOfPages());
        document.add(c12);
        document.add(t13);
        catalogs.put("13\ua0a0\ua0a0保密要求与措施", pdf.getNumberOfPages());
        document.add(c13);
        document.add(t14);
        catalogs.put("14\ua0a0\ua0a0试验的其它要求和有关问题说明", pdf.getNumberOfPages());
        document.add(c14);
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        /** 15*/
        // 试验实施网络图
        String examNetwork = "试验计划自" + resFirstTime + "开始," + resLastTime + "结束" + "试验实施网络图见图13-1。";

        LeftParagraph t15 = new LeftParagraph("15\ua0a0\ua0a0试验实施网络图", font, smallSize);
        bodyParagraph c15 = new bodyParagraph(examNetwork, font, smallSize);
        MidParagraph image13_1Title = new MidParagraph("图13-1\t试验实施网络图", font, smallSize);

        document.add(t15);
        catalogs.put("15\ua0a0\ua0a0试验实施网络图", pdf.getNumberOfPages());
        document.add(c15);
        document.add(image13_1Title);
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        /** 附录部分 表格A1*/
        /** 红色部分*/
        MidParagraph appendixTitle = new MidParagraph("附录A", font, smallSize);
        MidParagraph subAppendixTitle = new MidParagraph("故障记录表", font, smallSize);
        MidParagraph tableA1Title = new MidParagraph("表A1  故障报告表", font, smallSize);
        // 附录表1
        float[] tableA1Width = new float[]{51, 136, 17, 51, 51, 68, 68, 136};
        Table tableA1 = new Table(tableA1Width).setWidthPercent(100);
        tableA1.addCell(new Cell(1, 2).add(new Paragraph("故障报告表编号").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA1.addCell(new Cell(1, 3).add(new Paragraph(" ").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA1.addCell(new Cell(1, 1).add(new Paragraph("填表日期").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA1.addCell(new Cell(1, 2).add(new Paragraph(" ").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA1.addCell(new Cell(1, 2).add(new Paragraph("故障件型号、名称、编号").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA1.addCell(new Cell(1, 6).add(new Paragraph(" ").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA1.addCell(new Cell(1, 2).add(new Paragraph("故障时间").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA1.addCell(new Cell(1, 6).add(new Paragraph(" ").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA1.addCell(new Cell(1, 2).add(new Paragraph("故障时试验应力").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA1.addCell(new Cell(1, 6).add(new Paragraph(" ").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA1.addCell(new Cell(30, 8).add(new Paragraph("故障现象：\n\n\n\n\n\n\n\n\n\n" +
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.LEFT));
        tableA1.addCell(new Cell(1, 1).add(new Paragraph("承制方").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA1.addCell(new Cell(1, 2).add(new Paragraph(" ").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA1.addCell(new Cell(1, 1).add(new Paragraph("军代表").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA1.addCell(new Cell(1, 2).add(new Paragraph(" ").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA1.addCell(new Cell(1, 1).add(new Paragraph("承试方").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA1.addCell(new Cell(1, 1).add(new Paragraph(" ").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));

        document.add(appendixTitle);
        catalogs.put("附录A", pdf.getNumberOfPages());
        document.add(subAppendixTitle);
        document.add(tableA1Title);
        document.add(tableA1);
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        /** 附录 表格A2*/
        MidParagraph tableA2Title = new MidParagraph("表A2  故障分析报告表", font, smallSize);
        float[] tableA2Width = new float[]{51, 136, 17, 51, 51, 68, 68, 136};
        Table tableA2 = new Table(tableA2Width).setWidthPercent(100);
        tableA2.addCell(new Cell(1, 2).add(new Paragraph("故障分析报告表编号").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA2.addCell(new Cell(1, 3).add(new Paragraph(" ").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA2.addCell(new Cell(1, 1).add(new Paragraph("填表日期").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA2.addCell(new Cell(1, 2).add(new Paragraph(" ").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA2.addCell(new Cell(1, 2).add(new Paragraph("故障件型号、名称、编号").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA2.addCell(new Cell(1, 6).add(new Paragraph(" ").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA2.addCell(new Cell(10, 8).add(new Paragraph("故障分析说明：\n\n\n\n\n\n\n\n\n\n").
                setFont(font).setFontSize(smallSize)).setTextAlignment(TextAlignment.LEFT));
        tableA2.addCell(new Cell(10, 8).add(new Paragraph("故障原因：\n\n\n\n\n\n\n\n\n\n").
                setFont(font).setFontSize(smallSize)).setTextAlignment(TextAlignment.LEFT));
        tableA2.addCell(new Cell(10, 8).add(new Paragraph("失效机理：\n\n\n\n\n\n\n\n\n\n").
                setFont(font).setFontSize(smallSize)).setTextAlignment(TextAlignment.LEFT));
        tableA2.addCell(new Cell(1, 1).add(new Paragraph("承制方").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA2.addCell(new Cell(1, 2).add(new Paragraph(" ").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA2.addCell(new Cell(1, 1).add(new Paragraph("军代表").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA2.addCell(new Cell(1, 2).add(new Paragraph(" ").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA2.addCell(new Cell(1, 1).add(new Paragraph("承试方").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA2.addCell(new Cell(1, 1).add(new Paragraph(" ").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));

        document.add(tableA2Title);
        document.add(tableA2);
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        /** 附录 表格A3*/
        MidParagraph tableA3Title = new MidParagraph("表A3  故障纠正措施报告表", font, smallSize);
        float[] tableA3Width = new float[]{51, 136, 17, 51, 51, 68, 68, 136};
        Table tableA3 = new Table(tableA3Width).setWidthPercent(100);
        tableA3.addCell(new Cell(1, 2).add(new Paragraph("故障纠正措施报告表编号").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA3.addCell(new Cell(1, 3).add(new Paragraph(" ").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA3.addCell(new Cell(1, 1).add(new Paragraph("填表日期").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA3.addCell(new Cell(1, 2).add(new Paragraph(" ").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA3.addCell(new Cell(1, 2).add(new Paragraph("故障件型号、名称、编号").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA3.addCell(new Cell(1, 6).add(new Paragraph(" ").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA3.addCell(new Cell(1, 2).add(new Paragraph("实施单位").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA3.addCell(new Cell(1, 3).add(new Paragraph(" ").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA3.addCell(new Cell(1, 1).add(new Paragraph("实施日期").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA3.addCell(new Cell(1, 2).add(new Paragraph(" ").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA3.addCell(new Cell(10, 8).add(new Paragraph("纠正措施：\n\n\n\n\n\n\n\n\n\n").
                setFont(font).setFontSize(smallSize)).setTextAlignment(TextAlignment.LEFT));
        tableA3.addCell(new Cell(10, 8).add(new Paragraph("验证方法及纠正效果：\n\n\n\n\n\n\n\n\n\n").
                setFont(font).setFontSize(smallSize)).setTextAlignment(TextAlignment.LEFT));
        tableA3.addCell(new Cell(10, 8).add(new Paragraph("遗留问题及处理意见：\n\n\n\n\n\n\n\n\n\n").
                setFont(font).setFontSize(smallSize)).setTextAlignment(TextAlignment.LEFT));
        tableA3.addCell(new Cell(1, 1).add(new Paragraph("承制方").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA3.addCell(new Cell(1, 2).add(new Paragraph(" ").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA3.addCell(new Cell(1, 1).add(new Paragraph("军代表").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA3.addCell(new Cell(1, 2).add(new Paragraph(" ").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA3.addCell(new Cell(1, 1).add(new Paragraph("承试方").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
        tableA3.addCell(new Cell(1, 1).add(new Paragraph(" ").setFont(font).
                setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));

        document.add(tableA3Title);
        document.add(tableA3);
        int totalNumberPages = pdf.getNumberOfPages();
        //Close document
        document.close();

        /** 封面和目录部分*/
        // 设置字体
        PdfFont font2 = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H",true);
        // 输出含有目录的pdf
        PdfWriter catalogWriter = new PdfWriter(catdest);
        PdfDocument catalogPdf = new PdfDocument(catalogWriter);
        Document catalogDocument = new Document(catalogPdf);

        // 首页右上角的密级
        String degreeOfPassword = "";
        // 型号+名称
        String sizeAndName = manageSysOutline.getOutlineDevSubsysEqpModel() + "__" + manageSysOutline.getOutlineDevSubsysEqpName();
        // 首页标注的页数
        String totalPage = String.valueOf(totalNumberPages);
        // 编制单位
        String itemName = ItemNamePrefix.getValue();
        if ("_".equals(itemName.substring(itemName.length() - 1, itemName.length()))) {
            itemName = itemName.substring(0, itemName.length() - 1);
        }
        String Unit = itemName;

        // 首页年份 + 首页月份
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH) + 1;
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);


        // 编制日期
        Date outlineNewTime = manageSysOutline.getOutlineNewTime();
        Calendar newCal = Calendar.getInstance();
        newCal.setTime(outlineNewTime);
        String newYear = String.valueOf(newCal.get(Calendar.YEAR));
        String newMonth = String.valueOf(newCal.get(Calendar.MONTH) + 1);
        String newDay = String.valueOf(newCal.get(Calendar.DAY_OF_MONTH));
        String orgDate = newYear + "年" + newMonth + "月" + newDay + "日";
        // 校对日期
        Date outlineProofreadTime = manageSysOutline.getOutlineProofreadTime();
        Calendar proofreadCal = Calendar.getInstance();
        newCal.setTime(outlineProofreadTime);
        String proofreadYear = String.valueOf(proofreadCal.get(Calendar.YEAR));
        String proofreadMonth = String.valueOf(proofreadCal.get(Calendar.MONTH) + 1);
        String proofreadDay = String.valueOf(proofreadCal.get(Calendar.DAY_OF_MONTH));
        String checkDate = proofreadYear + "年" + proofreadMonth + "月" + proofreadDay + "日";
        // 审核日期
        Date outlineAuditTime = manageSysOutline.getOutlineAuditTime();
        Calendar auditCal = Calendar.getInstance();
        auditCal.setTime(outlineAuditTime);
        String auditYear = String.valueOf(auditCal.get(Calendar.YEAR));
        String auditMonth = String.valueOf(auditCal.get(Calendar.MONTH) + 1);
        String auditDay = String.valueOf(auditCal.get(Calendar.DAY_OF_MONTH));
        String examDate = auditYear + "年" + auditMonth + "月" + auditDay + "日";
        // 批准日期
        Date outlineAuthorizeTime = manageSysOutline.getOutlineAuthorizeTime();
        Calendar authorizeCal = Calendar.getInstance();
        newCal.setTime(outlineAuthorizeTime);
        String authorizeYear = String.valueOf(authorizeCal.get(Calendar.YEAR));
        String authorizeMonth = String.valueOf(authorizeCal.get(Calendar.MONTH) + 1);
        String authorizeDay = String.valueOf(authorizeCal.get(Calendar.DAY_OF_MONTH));
        String apprvDate = authorizeYear + "年" + authorizeMonth + "月" + authorizeDay + "日";

        // 首页
        LeftParagraph p01l01p1 = new LeftParagraph("定型文件"+multispace(120)+"密级："+
                degreeOfPassword, font2, midSize);
        MidParagraph p01l02 = new MidParagraph(sizeAndName, font2, bigSize);
        MidParagraph p01l03 = new MidParagraph("设计鉴定电磁兼容性试验大纲", font2, bigSize);
        MidParagraph p01l04 = new MidParagraph("共"+totalPage+"页", font2, midSize);
        MidParagraph p01l05 = new MidParagraph(Unit, font2, midSize);
        MidParagraph p01l06 = new MidParagraph(year+"年"+month+"月", font2, midSize);

        breakline(catalogDocument, 2);
        catalogDocument.add(p01l01p1);
        breakline(catalogDocument, 15);
        catalogDocument.add(p01l02);
        catalogDocument.add(p01l03);
        catalogDocument.add(p01l04);
        breakline(catalogDocument, 40);
        catalogDocument.add(p01l05);
        catalogDocument.add(p01l06);

        // 第二页
        catalogDocument.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        MidParagraph p02l01 = new MidParagraph(sizeAndName, font2, bigSize);
        MidParagraph p02l02 = new MidParagraph("设计鉴定电磁兼容性试验大纲", font2, bigSize);
        MidParagraph p02l03 = new MidParagraph("签署页", font2, midSize);
        MidParagraph p02l04 = new MidParagraph("编制：______________ 日期："+orgDate, font2, midSize);
        MidParagraph p02l05 = new MidParagraph("校对：______________ 日期："+checkDate, font2, midSize);
        MidParagraph p02l06 = new MidParagraph("审核：______________ 日期："+examDate, font2, midSize);
        MidParagraph p02l07 = new MidParagraph("批准：______________ 日期："+apprvDate, font2, midSize);

        breakline(catalogDocument, 20);
        catalogDocument.add(p02l01);
        catalogDocument.add(p02l02);
        catalogDocument.add(p02l03);
        breakline(catalogDocument, 30);
        catalogDocument.add(p02l04);
        catalogDocument.add(p02l05);
        catalogDocument.add(p02l06);
        catalogDocument.add(p02l07);

        // 第三页
        catalogDocument.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        MidParagraph p03l01 = new MidParagraph("文件修改记录", font2, midSize);
        float[] reviseTableWidth = new float[]{70, 105, 140, 210, 70};
        // 文件修改表格
        Table reviseTable = new Table(reviseTableWidth).setWidthPercent(100);
        reviseTable.addCell(new Cell().add(new Paragraph("版本号").setFont(font2).
                setFontSize(midSize))).setTextAlignment(TextAlignment.CENTER);
        reviseTable.addCell(new Cell().add(new Paragraph("修改内容").setFont(font2).
                setFontSize(midSize))).setTextAlignment(TextAlignment.CENTER);
        reviseTable.addCell(new Cell().add(new Paragraph("修改人").setFont(font2).
                setFontSize(midSize))).setTextAlignment(TextAlignment.CENTER);
        reviseTable.addCell(new Cell().add(new Paragraph("修改日期").setFont(font2).
                setFontSize(midSize))).setTextAlignment(TextAlignment.CENTER);
        reviseTable.addCell(new Cell().add(new Paragraph("备注").setFont(font2).
                setFontSize(midSize))).setTextAlignment(TextAlignment.CENTER);
        for (int i = 0; i < 15; i++){
            for (int j = 0; j < 5; j++){
                reviseTable.addCell(new Cell().add(new Paragraph("\n").setFont(font2).setFontSize(midSize)));
            }
        }
        catalogDocument.add(p03l01);
        catalogDocument.add(reviseTable);
        catalogDocument.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        MidParagraph catalogSet = new MidParagraph("目 次", font2, midSize);
        catalogDocument.add(catalogSet);
        for (Map.Entry<String, Integer> entry: catalogs.entrySet()){
            new catalogText().createPdf(catalogDocument, CATDEST, entry.getKey(), entry.getValue(), font2, smallSize);
        }

        catalogDocument.close();

        // 合并pdf 新pdf
        PdfDocument newPdf = new PdfDocument(new PdfWriter(newdest));
        PdfMerger merger = new PdfMerger(newPdf);

        catalogPdf = new PdfDocument(new PdfReader(CATDEST));
        merger.merge(catalogPdf, 1, catalogPdf.getNumberOfPages());
        pdf = new PdfDocument(new PdfReader(DEST));
        merger.merge(pdf, 1, pdf.getNumberOfPages());

        catalogPdf.close();
        pdf.close();
        newPdf.close();
    }

    /** 多个空格*/
    public String multispace(int n){
        String space = "";
        for (int i = 0; i < n; i++){
            space = space.concat(" ");
        }
        return space;
    }

    /** 空行*/
    public void breakline(Document document, int n){
        for (int i = 0; i < n; i++){
            document.add(new Paragraph(" "));
        }
    }

    /** 页眉页脚监听器*/
    protected class MyEventHandler implements IEventHandler {
        public void handleEvent(Event event) {
            try{
                PdfFont font = PdfFontFactory.createFont("STSong-Light",
                        "UniGB-UCS2-H",true);
                PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
                PdfDocument pdfDoc = docEvent.getDocument();
                PdfPage page = docEvent.getPage();
                int pageNumber = pdfDoc.getPageNumber(page);
                Rectangle pageSize = page.getPageSize();
                PdfCanvas pdfCanvas = new PdfCanvas(
                        page.newContentStreamBefore(), page.getResources(), pdfDoc);

                pdfCanvas.beginText()
                        .setFontAndSize(font, 10)
                        .moveText(pageSize.getWidth() - 150, pageSize.getTop() - 20)
                        .showText("电磁兼容性试验大纲")
                        .moveText(-pageSize.getWidth() / 2 + 150, -pageSize.getTop() + 30)
                        .showText(String.valueOf(pageNumber))
                        .endText();

                pdfCanvas.release();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //得到需要实施的试验端口和相关工作状态 i指的是试验项目序号 如CE101序号是14
    private JSONArray getTestPortAndWorkStatus(String outlineData14To34, int i) {
        JSONArray validTestPortArray = new JSONArray();
        JSONObject outlineData14To34Object = JSON.parseObject(outlineData14To34);
        if (outlineData14To34Object.containsKey("试验端口及被试品工作状态")) {
            JSONArray testPortAndWorkStatusArray;
            testPortAndWorkStatusArray = outlineData14To34Object.getJSONArray("试验端口及被试品工作状态");
            for (int j = 0; j < testPortAndWorkStatusArray.size(); j++) {
                JSONObject testPortAndWorkStatusObject = testPortAndWorkStatusArray.getJSONObject(j);
                if (testPortAndWorkStatusObject.containsKey("端口是否实施")) {
                    if ("是".equals(testPortAndWorkStatusObject.getString("端口是否实施"))) {
                        validTestPortArray.add(testPortAndWorkStatusObject);
                    }
                }
            }
        } else if (outlineData14To34Object.containsKey("试验位置及被试品工作状态")) {
            JSONArray testPortAndWorkStatusArray;
            testPortAndWorkStatusArray = outlineData14To34Object.getJSONArray("试验位置及被试品工作状态");
            for (int j = 0; j < testPortAndWorkStatusArray.size(); j++) {
                JSONObject testPortAndWorkStatusObject = testPortAndWorkStatusArray.getJSONObject(j);
                if (testPortAndWorkStatusObject.containsKey("端口是否实施")) {
                    if ("是".equals(testPortAndWorkStatusObject.getString("端口是否实施"))) {
                        validTestPortArray.add(testPortAndWorkStatusObject);
                    }
                }
            }
        } else if (outlineData14To34Object.containsKey("试验部位及被试品工作状态")) {
            JSONArray testPortAndWorkStatusArray;
            testPortAndWorkStatusArray = outlineData14To34Object.getJSONArray("试验部位及被试品工作状态");
            for (int j = 0; j < testPortAndWorkStatusArray.size(); j++) {
                JSONObject testPortAndWorkStatusObject = testPortAndWorkStatusArray.getJSONObject(j);
                if (testPortAndWorkStatusObject.containsKey("端口是否实施")) {
                    if ("是".equals(testPortAndWorkStatusObject.getString("端口是否实施"))) {
                        validTestPortArray.add(testPortAndWorkStatusObject);
                    }
                }
            }
        } else if (outlineData14To34Object.containsKey("被试品工作状态")) {
            JSONArray testPortAndWorkStatusArray;
            testPortAndWorkStatusArray = outlineData14To34Object.getJSONArray("被试品工作状态");
            for (int j = 0; j < testPortAndWorkStatusArray.size(); j++) {
                JSONObject testPortAndWorkStatusObject = testPortAndWorkStatusArray.getJSONObject(j);
                if (testPortAndWorkStatusObject.containsKey("端口是否实施")) {
                    if ("是".equals(testPortAndWorkStatusObject.getString("端口是否实施"))) {
                        validTestPortArray.add(testPortAndWorkStatusObject);
                    }
                }
            }
        }
        return validTestPortArray;
    }

    //电源端口 + 互联端口 （CS114, CS115, CS116） 这里再另外写一个函数
    private JSONObject getTwoTestPort(String outlineData26To34, int i) {
        JSONObject outlineData26To34Object = JSON.parseObject(outlineData26To34);
        JSONObject resObject = new JSONObject();
        JSONObject testPortAndWorkStatusObject;
        JSONArray elecTestPortArray = new JSONArray();
        JSONArray validElecTestPortArray = new JSONArray();
        JSONArray connectTestPortArray = new JSONArray();
        JSONArray validConnectTestportArray = new JSONArray();
        if (outlineData26To34Object.containsKey("试验端口及被试品工作状态")) {
            elecTestPortArray = outlineData26To34Object.getJSONObject("试验端口及被试品工作状态").getJSONArray("电源端口");
            connectTestPortArray = outlineData26To34Object.getJSONObject("试验端口及被试品工作状态").getJSONArray("互联端口");
            for (int j = 0; j < elecTestPortArray.size(); j++) {
                testPortAndWorkStatusObject = elecTestPortArray.getJSONObject(j);
                if (testPortAndWorkStatusObject.containsKey("端口是否实施")) {
                    if ("是".equals(testPortAndWorkStatusObject.getString("端口是否实施"))) {
                        validElecTestPortArray.add(testPortAndWorkStatusObject);
                    }
                }
            }
            for (int j = 0; j < connectTestPortArray.size(); j++) {
                testPortAndWorkStatusObject = connectTestPortArray.getJSONObject(j);
                if (testPortAndWorkStatusObject.containsKey("是否实施")) {
                    if ("是".equals(testPortAndWorkStatusObject.getString("是否实施"))) {
                        validConnectTestportArray.add(testPortAndWorkStatusObject);
                    }
                }
            }
            resObject.put("电源端口", validElecTestPortArray);
            resObject.put("互联端口", validConnectTestportArray);
        }
        return resObject;
    }

    //这个函数是为了针对有可能有多个图片的标题来写的返回的句子样式为 如图5-1和图5-2
    private String getPicTitle(String picNamePrefix, int currentPicNumber, int picQuantity) {
        String result = "";
        if (picQuantity == 1)
            return "图" + picNamePrefix + currentPicNumber + "所示";
        for (int i = 0; i < picQuantity; i++) {
            result += "图" + picNamePrefix + currentPicNumber + "和";
            currentPicNumber++;
        }
        return result.substring(0, result.length() - 1) + "所示";
    }

    //这个函数是为了确认试验配置图 写的一个函数
    private ArrayList<String> getTestPic(String projectName, String outlineData) {
        ArrayList<String> resPicList = new ArrayList<>();
        if ("CE106".equals(projectName)) {
            JSONObject outlineData16Object = JSON.parseObject(outlineData);
            JSONArray testPortAndWorkStateArray = outlineData16Object.getJSONArray("试验端口及被试品工作状态");
            for (int i = 0; i < testPortAndWorkStateArray.size(); i++) {
                JSONObject testPortAndWorkStateObject = testPortAndWorkStateArray.getJSONObject(i);
                if ("是".equals(testPortAndWorkStateObject.getString("端口是否实施"))) {
                    JSONArray workStateArray = testPortAndWorkStateObject.getJSONArray("工作状态");
                    for (int j = 0; j < workStateArray.size(); j++) {
                        JSONObject workStateObject = workStateArray.getJSONObject(j);
                        if ("是".equals(workStateObject.getString("状态是否实施"))) {
                            String workState = workStateObject.getString("工作状态");
                            if ("工作方式：发".equals(workState.substring(0, 6))) {
                                resPicList.add("1");
                            } else if ("工作方式：收".equals(workState.substring(0, 6))) {
                                resPicList.add("2");
                            } else if ("工作方式：待发".equals(workState.substring(0, 7))) {
                                resPicList.add("2");
                            }
                        }
                    }
                }
            }
        }
        if ("CS101".equals(projectName)) {
            JSONObject outlineData10Object = JSON.parseObject(outlineData);
            JSONArray elecPortArray = outlineData10Object.getJSONArray("电源端口");
            for (int i = 0 ; i < elecPortArray.size(); i++) {
                JSONObject elecPortObject = elecPortArray.getJSONObject(i);
                String position = elecPortObject.getString("两相/三相");
                if ("两相".equals(position)) {
                    resPicList.add("1");
                } else if ("三相∆".equals(position)) {
                    resPicList.add("2");
                } else if ("三相Y".equals(position)) {
                    resPicList.add("3");
                }

            }
        }

        if ("RS103".equals(projectName)) {
            JSONObject outlineData33Object = JSON.parseObject(outlineData);
            JSONArray CS103TestPortAndWorkStateArray = outlineData33Object.getJSONArray("试验端口及被试品工作状态");
            for (int i = 0; i < CS103TestPortAndWorkStateArray.size(); i++) {
                JSONObject TestPortAndWorkStateObject = CS103TestPortAndWorkStateArray.getJSONObject(i);
                JSONObject antennaPositionObject = TestPortAndWorkStateObject.getJSONObject("天线位置");
                for (int j = 0; j < antennaPositionObject.size(); j++) {
                    int rateNumber = j + 1;
                    int positionNumber = Integer.valueOf((String) antennaPositionObject.getJSONObject("频率" + rateNumber).get("位置数"));
                    if (j == 0) {
                        if (positionNumber == 1) {
                            resPicList.add("1");
                        } else {
                            resPicList.add("2");
                        }
                    }

                    if (j == 1) {
                        if (positionNumber == 1) {
                            resPicList.add("1");
                        } else {
                            resPicList.add("3");
                        }
                    }

                    if (j == 2) {
                        if (positionNumber == 1) {
                            resPicList.add("1");
                        } else {
                            resPicList.add("4");
                        }
                    }
                }
            }
        }

        if ("RE103".equals(projectName)) {
            JSONObject outlineData31Object = JSON.parseObject(outlineData);
            JSONArray testPortAndWorkStateArray = outlineData31Object.getJSONArray("被试品工作状态");
            for (int i = 0; i < testPortAndWorkStateArray.size(); i++) {
                JSONObject testPortAndWorkStateObject = testPortAndWorkStateArray.getJSONObject(i);
                if ("是".equals(testPortAndWorkStateObject.getString("端口是否实施"))) {
                    JSONObject allWorkStateObject = testPortAndWorkStateObject.getJSONObject("工作状态");
                    for (int j = 0; j < allWorkStateObject.size(); j++) {
                        int number = j + 1;
                        String key = "工作状态" + number;
                        JSONObject workStateObject = allWorkStateObject.getJSONObject(key);
                        if ("是".equals(workStateObject.getString("状态是否实施"))) {
                            String workStateDescribe = workStateObject.getString("工作状态描述");
                            int startIndex = workStateDescribe.indexOf("频率：") + 3;
                            int endIndex = workStateDescribe.indexOf("M");
                            int rate = Integer.valueOf(workStateDescribe.substring(startIndex, endIndex));
                            if (rate > 50) {
                                resPicList.add("1");
                            }
                        }
                    }
                }
            }

        }
        return resPicList;
    }
}

