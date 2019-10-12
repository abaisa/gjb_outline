package cn.gjb151b.outline.utils.Itext7;

import cn.gjb151b.outline.utils.Itext7.*;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class report {
    //内容文件：report.pdf 目录文件：catalogReport.pdf 合成文件：newReport.pdf
    public static final String DEST="result/pdf/report.pdf";
    public static final String CATDEST = "result/pdf/catalogReport.pdf";
    public static final String NEWDEST = "result/pdf/newReport.pdf";

    /** main*/
    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new report().createPdf(DEST, CATDEST, NEWDEST);
    }

    /** pdf制作函数*/
    public void createPdf(String dest, String catdest, String newdest) throws Exception{
        // 内容文件设置
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // 设置中文字体
        PdfFont font = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H",true);
        // 设置字体大小
        int midSize = 15;
        int bigSize = 30;
        int smallSize = 10;

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
        String workSource = "按照XXXX承担XXXXXXXXXX设计鉴定电磁兼容性试验，依据《XXXXX》和相关文件、标准编制了" +
                "《XXXXXXXXXX设计鉴定电磁兼容性试验大纲》。";
        // 编制依据 （红）
        String proof = "XXXXX";
        // 引用文件 （红）
        String[] useDoc = {"GJB1362A-2007\t 军工产品定型程序和要求\n",
                "GJB/Z 170-2013\t 军工产品设计定型文件编制指南\n",
                "GJB 151A-1997\t 军用设备和分系统电磁发射和敏感度要求\n",
                "GJB 152A-1997\t 军用设备和分系统电磁发射和敏感度测量\n",
                "GJB 6785-2009\t 军用电子设备方舱屏蔽效能测试方法\n",
                "GJB 1389A-2005\t 系统电磁兼容性要求\n",
                "GJB151B-2013\t 军用设备和分系统电磁发射和敏感度要求与测量\n",
                "GJB5313-2004\t 电磁辐射暴露限值和测量方法"};
        // 试验性质
        String checkProperty = "设计鉴定试验";
        // 试验目的
        String checkPurpose = "考核XXXXXXXXXX在规定的电磁兼容性是否满足《XXXXX》的要求，为其设计鉴定提供依据。";
        // 试验时间
        String checkTime = "201X年X月（暂定）";
        // 试验地点
        String checkLocation = "工业和信息化部电子第四研究院军用实验室。";
        // 被试品组成
        String checkMakeup = "主要设备组成如表5-1所示，设备外部照片";
        // 表5-1标题
        String table0501Title = "表5-1 XXXXXX主要设备清单";

        // 正文第一页
        LeftParagraph t1 = new LeftParagraph("1 任务依据", font, smallSize);
        LeftParagraph t1_1 = new LeftParagraph("1.1 任务来源", font, smallSize);
        bodyParagraph c1_1 = new bodyParagraph(workSource, font, smallSize);
        LeftParagraph t1_2 = new LeftParagraph("1.2 编制依据和引用文件", font, smallSize);
        bodyParagraph t1_2p1 = new bodyParagraph("编制依据：", font, smallSize);
        bodyParagraph c1_2p1 = new bodyParagraph(proof, font, smallSize);
        bodyParagraph t1_2p2 = new bodyParagraph("引用文件：", font, smallSize);
        bodyParagraph []c1_2p2 = new bodyParagraph[useDoc.length];
        for (int i = 0; i < useDoc.length; i++){
            c1_2p2[i] = new bodyParagraph(useDoc[i], font, smallSize);
        }
        LeftParagraph t2 = new LeftParagraph("2 试验性质", font, smallSize);
        bodyParagraph c2 = new bodyParagraph(checkProperty, font, smallSize);
        LeftParagraph t3 = new LeftParagraph("3 试验的目的", font, smallSize);
        bodyParagraph c3 = new bodyParagraph(checkPurpose, font, smallSize);
        LeftParagraph t4 = new LeftParagraph("4 试验时间和地点", font, smallSize);
        bodyParagraph c4p1 = new bodyParagraph("试验时间："+checkTime, font, smallSize);
        bodyParagraph c4p2 = new bodyParagraph("地      点："+checkLocation, font, smallSize);
        LeftParagraph t5 = new LeftParagraph("5 被试品、陪试品的数量及技术状态", font, smallSize);
        LeftParagraph t5_1 = new LeftParagraph("5.1 被试品组成和功能", font, smallSize);
        LeftParagraph t5_1_1 = new LeftParagraph("5.1.1 被试品组成", font, smallSize);
        bodyParagraph c5_1_1 = new bodyParagraph("（名称）"+checkMakeup+"如图5-1所示。", font, smallSize);
        MidParagraph tableTitle5_1 = new MidParagraph(table0501Title, font, smallSize);
        float[] table5_1Width = new float[]{44, 154, 176, 44, 132, 44};
        // 表5-1 主要设备清单
        Table table5_1 = new Table(table5_1Width).setWidthPercent(100);
        table5_1.addCell(new Cell().add(new Paragraph("序号").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_1.addCell(new Cell().add(new Paragraph("设备名称").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_1.addCell(new Cell().add(new Paragraph("型号/编号").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_1.addCell(new Cell().add(new Paragraph("数量").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_1.addCell(new Cell().add(new Paragraph("安装位置").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_1.addCell(new Cell().add(new Paragraph("备注").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        for (int i = 0; i < 54; i++){
            for (int j = 0; j < 5; j++){
                if (j == 0){
                    table5_1.addCell(new Cell().add(new Paragraph(String.valueOf(i+1))
                            .setFont(font).setFontSize(smallSize)));
                }
                if (j == 3){
                    if (i == 10){
                        table5_1.addCell(new Cell(9, 1).add(new Paragraph("\n")
                                .setFont(font).setFontSize(smallSize)));
                    }
                    else if (i == 20)
                        table5_1.addCell(new Cell(7, 1).add(new Paragraph("\n")
                                .setFont(font).setFontSize(smallSize)));
                    else if (i == 28)
                        table5_1.addCell(new Cell(7, 1).add(new Paragraph("\n")
                                .setFont(font).setFontSize(smallSize)));
                    else if (i == 36)
                        table5_1.addCell(new Cell(6, 1).add(new Paragraph("\n")
                                .setFont(font).setFontSize(smallSize)));
                    else if (i == 43)
                        table5_1.addCell(new Cell(7, 1).add(new Paragraph("\n")
                                .setFont(font).setFontSize(smallSize)));
                    else if (i>=11&&i<=18||i>=21&&i<=26||i>=29&&i<=34||i>=37&&i<=41||i>=44&&i<=49)
                        continue;
                    else
                        table5_1.addCell(new Cell().add(new Paragraph("\n")
                                .setFont(font).setFontSize(smallSize)));
                }
                else{
                    table5_1.addCell(new Cell().add(new Paragraph("\n").setFont(font).setFontSize(smallSize)));
                }
            }
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
        float[] table5_2Width = new float[]{54, 216, 162, 162};
        Table table5_2 = new Table(table5_2Width).setWidthPercent(100);
        table5_2.addCell(new Cell().add(new Paragraph("序号").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_2.addCell(new Cell().add(new Paragraph("软件名称").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_2.addCell(new Cell().add(new Paragraph("软件标识").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_2.addCell(new Cell().add(new Paragraph("软件版本").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 3; j++){
                if (j == 0){
                    table5_2.addCell(new Cell().add(new Paragraph(String.valueOf(i+1))
                            .setFont(font).setFontSize(smallSize)));
                }
                if (j == 2){
                    table5_2.addCell(new Cell().add(new Paragraph(softwareVersion[i])
                            .setFont(font).setFontSize(smallSize)));
                }
                else{
                    table5_2.addCell(new Cell().add(new Paragraph("\n").setFont(font).setFontSize(smallSize)));
                }
            }
        }

        // 图5-1标题
        String img5_1Title = "图5-1\tXXXXXXXXXX设备照片";
        // 5.1.2 主要功能
        String mainFunction = "a)\tXXXXXXX。";
        // 5.1.3 系统交联关系图
        String systemRelationImg = "XXXXXXXXXX系统交联关系见图5-2。";
        // 图5-2标题
        String img5_2Title = "图5-2\tXXXXXXXXXX系统交联图";
        // 5.1.6 被试品技术状态
        String techState = "被试品符合设计鉴定技术状态和工艺状态，符合成品协议书和产品规范的要求，通过XXX检验合格。";
        // 5.2 被试品装机
        String install = "XXXXXXXXXX为XXXX状态工作。";
        MidParagraph img5_1 = new MidParagraph(img5_1Title, font, smallSize);
        LeftParagraph t5_1_2 = new LeftParagraph("5.1.2 主要功能", font,smallSize);
        bodyParagraph c5_1_2 = new bodyParagraph(mainFunction, font, smallSize);
        LeftParagraph t5_1_3 = new LeftParagraph("5.1.3 系统交联关系图", font, smallSize);
        bodyParagraph c5_1_3 = new bodyParagraph(systemRelationImg, font, smallSize);
        MidParagraph img5_2 = new MidParagraph(img5_2Title, font, smallSize);
        LeftParagraph t5_1_4 = new LeftParagraph("5.1.4　被试品电源端口", font, smallSize);
        LeftParagraph t5_1_5 = new LeftParagraph("5.1.5　被试品互联端口", font, smallSize);
        LeftParagraph t5_1_6 = new LeftParagraph("5.1.6　被试品技术状态", font, smallSize);
        bodyParagraph c5_1_6p1 = new bodyParagraph(techState, font, smallSize);
        bodyParagraph c5_1_6p2 = new bodyParagraph("软件均已通过软件测评，被试品软件清单见表5-2。", font, smallSize);
        LeftParagraph t5_2 = new LeftParagraph("5.2　被试品装机XXXXX", font, smallSize);
        bodyParagraph c5_2 = new bodyParagraph(install, font, smallSize);
        LeftParagraph t5_3 = new LeftParagraph("5.3　陪试品的数量和技术状态", font, smallSize);
        bodyParagraph c5_3 = new bodyParagraph("配试设备数量及编号见表5-4所示，配试设备参试应经检验合格或检定合格。",
                font, smallSize);
        MidParagraph table5_3Title = new MidParagraph("表5-3\t配试设备数量及编号汇总表", font, smallSize);
        float[] table5_3Width = new float[]{132, 66, 66, 262, 66};
        Table table5_3 = new Table(table5_3Width).setWidthPercent(100);
        table5_3.addCell(new Cell().add(new Paragraph("配试设备名称").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_3.addCell(new Cell().add(new Paragraph("数量（套）").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_3.addCell(new Cell().add(new Paragraph("型号").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_3.addCell(new Cell().add(new Paragraph("功能简述").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table5_3.addCell(new Cell().add(new Paragraph("备注").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 5; j++){
                table5_3.addCell(new Cell().add(new Paragraph("\n").setFont(font).setFontSize(smallSize)));
            }
        }

        document.add(t1);
        catalogs.put("1 任务依据", pdf.getNumberOfPages());
        document.add(t1_1);
        catalogs.put("1.1 任务来源", pdf.getNumberOfPages());
        document.add(c1_1);
        document.add(t1_2);
        catalogs.put("1.2 编制依据和引用文件", pdf.getNumberOfPages());
        document.add(t1_2p1);
        document.add(c1_2p1);
        document.add(t1_2p2);
        for (int i = 0; i < useDoc.length; i++)
            document.add(c1_2p2[i]);
        document.add(t2);
        catalogs.put("2. 试验性质", pdf.getNumberOfPages());
        document.add(c2);
        document.add(t3);
        catalogs.put("3 试验的目的", pdf.getNumberOfPages());
        document.add(c3);
        document.add(t4);
        catalogs.put("4 试验时间和地点", pdf.getNumberOfPages());
        document.add(c4p1);
        document.add(c4p2);
        document.add(t5);
        catalogs.put("5 被试品、陪试品的数量及技术状态", pdf.getNumberOfPages());
        document.add(t5_1);
        catalogs.put("5.1 被试品组成和功能", pdf.getNumberOfPages());
        document.add(t5_1_1);
        catalogs.put("5.1.1 被试品组成", pdf.getNumberOfPages());
        document.add(c5_1_1);
        document.add(tableTitle5_1);
        document.add(table5_1);
        document.add(table5_1t);
        document.add(tableTitle5_2);
        document.add(table5_2);
        document.add(img5_1);
        document.add(t5_1_2);
        catalogs.put("5.1.2 主要功能", pdf.getNumberOfPages());
        document.add(c5_1_2);
        document.add(t5_1_3);
        catalogs.put("5.1.3 系统交联关系图", pdf.getNumberOfPages());
        document.add(c5_1_3);
        breakline(document, 1);
        document.add(img5_2);
        breakline(document, 1);
        document.add(t5_1_4);
        catalogs.put("5.1.4 被试品电源端口", pdf.getNumberOfPages());
        document.add(t5_1_5);
        catalogs.put("5.1.5 被试品互联端口", pdf.getNumberOfPages());
        document.add(t5_1_6);
        catalogs.put("5.1.6 被试品技术状态", pdf.getNumberOfPages());
        document.add(c5_1_6p1);
        document.add(c5_1_6p2);
        document.add(t5_2);
        catalogs.put("5.2 被试品装机位置及方向", pdf.getNumberOfPages());
        document.add(c5_2);
        document.add(t5_3);
        catalogs.put("5.3　陪试品的数量和技术状态", pdf.getNumberOfPages());
        document.add(c5_3);
        breakline(document, 1);
        document.add(table5_3Title);
        document.add(table5_3);

        /** 6, 6.1*/
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        LeftParagraph t6 = new LeftParagraph("6　试验项目、方法及要求", font, smallSize);
        LeftParagraph t6_1 = new LeftParagraph("6.1　试验项目及限值要求", font, smallSize);
        bodyParagraph c6_1 = new bodyParagraph("本次设计鉴定电磁兼容性试验项目及限值要求见表6-1。", font, smallSize);
        MidParagraph table6_1Title = new MidParagraph("表6-1\t电磁兼容试验项目及限值要求", font, smallSize);
        // 试验项目
        String[] checkprojects = {"CE102", "CE106", "CS101", "CS114", "CS115", "CS116", "RE102", "RS103"};
        // 试验内容
        String[] checkCotents = {"10kHz～10MHz电源线传导发射", "10kHz～40GHz天线端子传导发射", "25Hz～50kHz电源线传导敏感度",
                "10kHz～400MHz电缆束注入传导敏感度", "电缆束注入脉冲激励传导敏感度", "10kHz～100MHz电缆和电源线阻尼正弦瞬变传导敏感度",
                "2MHz～18GHz电场辐射发射", "2MHz～18GHz电场辐射敏感度"};
        // 限值要求
        String[] limitReq = {"按GJB151A/152A-1997要求，限值采用AC220V供电设备限值要求。",
                "按GJB151A/152A-1997要求，限值采用GJB151A-1997中的5.3.3.2条规定限值。",
                "按GJB151A/152A-1997要求，极限值采用图CS101-1中的曲线1要求。",
                "按GJB151A/152A-1997要求，极限值采用图CS114-1中的曲线2。",
                "按GJB151A/152A-1997要求，注入电流幅度为5A。",
                "按GJB151A/152A-1997要求，极限值采用图CS116-2中曲线要求，Imax=5A。",
                "按GJB151A/152A-1997要求，限值采用图RE102-3中海军（固定的）和空军的限值要求。",
                "按GJB151A/152A-1997要求，采用地面设备极限值要求中的频段2MHz～1GHz，10V/m；频段1GHz～18GHz，50V/m。"};
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
        for (int i = 0; i < 10; i++){
            table6_1.addCell(new Cell().add(new Paragraph(String.valueOf(i+1))
                    .setFont(font).setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
            if (i <= 1){
                for (int j = 0; j < 3; j++)
                    table6_1.addCell(new Cell().add(new Paragraph("\n").setFont(font).setFontSize(smallSize)));
            }
            else{
                table6_1.addCell(new Cell().add(new Paragraph(checkprojects[i - 2])
                        .setFont(font).setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
                table6_1.addCell(new Cell().add(new Paragraph(checkCotents[i - 2])
                        .setFont(font).setFontSize(smallSize)).setTextAlignment(TextAlignment.CENTER));
                table6_1.addCell(new Cell().add(new Paragraph(limitReq[i - 2])
                        .setFont(font).setFontSize(smallSize)).setTextAlignment(TextAlignment.LEFT));
            }
        }

        document.add(t6);
        catalogs.put("6 试验项目、方法及要求", pdf.getNumberOfPages());
        document.add(t6_1);
        catalogs.put("6.1 试验项目及名称", pdf.getNumberOfPages());
        document.add(c6_1);
        document.add(table6_1Title);
        document.add(table6_1);

        /** 6.2 -- 6.5.1.4*/
        // 6.2 试验环境与条件要求
        String envAndCondition = "本项试验环境条件要求如下：\n" +
                "a)\t温度：0℃～+35℃；\n" +
                "b)\t相对湿度（RH）：20%～80%；\n" +
                "c)\t大气压力：试验场所气压。\n" +
                "d)\t电磁环境： 被试品断电和所有辅助设备通电时测得的电磁环境电平应低于规定试验极限值6dB。\n";
        // 监测点：
        String examPoint = "监测点：";
        // 监测手段：
        String examTool = "监测手段：";
        // 评定准则：
        String examStandard = "评定准则：";
        // 驻留时间：
        String waitTime = "驻留时间：";
        // 试验内容
        String checkContent = "试验内容：10kHz～10MHz电源线传导发射；\n" +
                "极限要求：在GJB151A中图CE102-1基准限值的基础上整体放宽9dB作为限值。";
        // 试验目的
        String checkAim = "考核被试品输入电源线（包括回线）上的传导发射是否符合GJB151A规定。";
        // 工作状态
        String workState = "工作状态1：被试品所有设备处于加电工作状态，配试设备处于正常工作状态；" +
                "被试系统与适配设备的上下行通信链路处于锁定并可执行控制指令状态。";
        // 测试位置
        String examLocation = "测试位置：XXXX外接AC220V电源线。";

        LeftParagraph t6_2 = new LeftParagraph("6.2　试验环境与条件要求", font, smallSize);
        bodyParagraph c6_2 = new bodyParagraph(envAndCondition, font, smallSize);
        LeftParagraph t6_3 = new LeftParagraph("6.3　发射及敏感度测试参数设置", font, smallSize);
        LeftParagraph t6_4 = new LeftParagraph("6.4　敏感度判据及监测方法", font, smallSize);
        bodyParagraph c6_4p1 = new bodyParagraph(examPoint, font, smallSize);
        bodyParagraph c6_4p2 = new bodyParagraph(examTool, font, smallSize);
        bodyParagraph c6_4p3 = new bodyParagraph(examStandard, font, smallSize);
        bodyParagraph c6_4p4 = new bodyParagraph(waitTime, font, smallSize);
        LeftParagraph t6_5 = new LeftParagraph("6.5　试验方法及要求", font, smallSize);
        LeftParagraph t6_5_1 = new LeftParagraph("6.5.1　CE102试验", font, smallSize);
        LeftParagraph t6_5_1_1 = new LeftParagraph("6.5.1.1　试验内容", font, smallSize);
        bodyParagraph c6_5_1_1 = new bodyParagraph(checkContent, font, smallSize);
        LeftParagraph t6_5_1_2 = new LeftParagraph("6.5.1.2　试验目的", font, smallSize);
        bodyParagraph c6_5_1_2 = new bodyParagraph(checkAim, font, smallSize);
        LeftParagraph t6_5_1_3 = new LeftParagraph("6.5.1.3　试验状态及测试位置", font, smallSize);
        bodyParagraph c6_5_1_3p1 = new bodyParagraph(workState, font, smallSize);
        bodyParagraph c6_5_1_3p2 = new bodyParagraph(examLocation, font, smallSize);
        /** 红色部分*/
        LeftParagraph t6_5_1_4 = new LeftParagraph("6.5.1.4　试验方法", font, smallSize);
        bodyParagraph c6_5_1_4 = new bodyParagraph("按照图6-1的试验基本配置，和GJB152A-1997方法对CE102开展试验。",
                font, smallSize);

        document.add(t6_2);
        catalogs.put("6.2 试验环境与条件要求", pdf.getNumberOfPages());
        document.add(c6_2);
        document.add(t6_3);
        catalogs.put("6.3 发射及敏感度测试参数设置", pdf.getNumberOfPages());
        document.add(t6_4);
        catalogs.put("6.4 敏感度判据及监测方法", pdf.getNumberOfPages());
        document.add(c6_4p1);
        document.add(c6_4p2);
        document.add(c6_4p3);
        document.add(c6_4p4);
        document.add(t6_5);
        catalogs.put("6.5 试验方法及要求", pdf.getNumberOfPages());
        document.add(t6_5_1);
        catalogs.put("6.5.1 CE102试验", pdf.getNumberOfPages());
        document.add(t6_5_1_1);
        catalogs.put("6.5.1.1 试验内容", pdf.getNumberOfPages());
        document.add(c6_5_1_1);
        document.add(t6_5_1_2);
        catalogs.put("6.5.1.2 试验目的", pdf.getNumberOfPages());
        document.add(c6_5_1_2);
        document.add(t6_5_1_3);
        catalogs.put("6.5.1.3 试验状态及测试位置", pdf.getNumberOfPages());
        document.add(c6_5_1_3p1);
        document.add(c6_5_1_3p2);
        document.add(t6_5_1_4);
        catalogs.put("6.5.1.4 试验方法", pdf.getNumberOfPages());
        document.add(c6_5_1_4);

        /** 图6-1 -- 7.1*/
        // 图6-1
        String image6_1Name = "img6_1.png";
        int sizeRate = 2;
        Image img6_1 = new Image(ImageDataFactory.create("src/main/resources/img/"+image6_1Name)).
                scaleToFit(PageSize.A4.getWidth() / sizeRate, PageSize.A4.getHeight() / sizeRate);
        // 6.5.1.5 数据处理方法
        String dataSolution = "待记录数据为被试品电源线传导发射幅度和频率的数据，该数据通过测试软件自动记录得出，并已完成所有线缆衰减、" +
                "线路阻抗稳定网络电压分压系数、衰减器等回路设备系数的补偿，软件记录结果即为最终实测值。";
        // 6.5.1.6 测试结果评定准则
        String examResult = "被试品在10kHz～10MHz频率范围内传导发射实测值不超过GJB 151A-1997" +
                "图CE102-1中AC220V基准限值的基础上整体放宽9dB，判定为合格，否则判定为不合格。";
        // CE102测量设备的名称
        String []examMachineNames = {"频谱分析仪", "信号源", "频率计", "功率计100W", "误码仪", "直流稳压电源",
                "可变衰减器", "固定衰减器", "数字万用表", "接地电阻表", "数字微欧计", "场强仪"};
        Paragraph image6_1 = new Paragraph().add(img6_1).setMarginLeft(PageSize.A4.getWidth() / (16 / sizeRate));
        MidParagraph image6_1Title = new MidParagraph("图6-1\tCE102试验配置图", font, smallSize);
        LeftParagraph t6_5_1_5 = new LeftParagraph("6.5.1.5　数据处理方法", font, smallSize);
        bodyParagraph c6_5_1_5 = new bodyParagraph(dataSolution, font, smallSize);
        LeftParagraph t6_5_1_6 = new LeftParagraph("6.5.1.6　测试结果评定准则", font, smallSize);
        bodyParagraph c6_5_1_6 = new bodyParagraph(examResult, font, smallSize);
        LeftParagraph t7 = new LeftParagraph("7　测试测量要求", font, smallSize);
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
        table7_1.addCell(new Cell().add(new Paragraph("名称").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table7_1.addCell(new Cell().add(new Paragraph("主要性能指标").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table7_1.addCell(new Cell().add(new Paragraph("数量").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table7_1.addCell(new Cell().add(new Paragraph("备注").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        for (int i = 0; i < 12; i++){
            for (int j = 0; j < 4; j++){
                if (j == 0){
                    table7_1.addCell(new Cell().add(new Paragraph(examMachineNames[i])
                            .setFont(font).setFontSize(smallSize)));
                }
                else{
                    table7_1.addCell(new Cell().add(new Paragraph("\n").setFont(font).setFontSize(smallSize)));
                }
            }
        }

        document.add(image6_1);
        document.add(image6_1Title);
        document.add(t6_5_1_5);
        catalogs.put("6.5.1.5 数据处理方法", pdf.getNumberOfPages());
        document.add(c6_5_1_5);
        document.add(t6_5_1_6);
        catalogs.put("6.5.1.6 测试结果评定准则", pdf.getNumberOfPages());
        document.add(c6_5_1_6);
        document.add(t7);
        catalogs.put("7 测试测量要求", pdf.getNumberOfPages());
        document.add(t7_1);
        catalogs.put("7.1 电磁兼容性测试场地", pdf.getNumberOfPages());
        document.add(table7_0);
        document.add(t7_2);
        catalogs.put("7.2 电磁兼容性测试设备", pdf.getNumberOfPages());
        document.add(t7_2_1);
        catalogs.put("7.2.1 CE102测试设备", pdf.getNumberOfPages());
        document.add(table7_1Title);
        document.add(table7_1);

        /** 7.2 -- 14*/
        // 7.3 陪试及监测设备
        String partnerCheck = "功能性能试验测试测量设备名称和精度见表7-1。" +
                "测试设备检定合格并在有效期内。测试设备的精度不应低于试验条件允许误差的1/3。";
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
        String examStopHandle = "试验过程中出现下列情形之一时，承试单位应中断试验，同时通知有关单位；\n" +
                "a)\t出现安全、保密事故征兆；\n" +
                "b)\t试验结果已判定关键战术技术指标达不到要求；\n" +
                "c)\t出现影响性能和使用的重大技术问题；\n" +
                "d)\t出现短期内难以排除的故障。";
        // 故障处理
        String errorHandle = "当确认受试产品发生故障时，应按相关规定将故障信息及时记入“故障报告表”、" +
                "“故障分析报告表”和“故障纠正措施报告表”。\n\t若经分析认为针对发生的故障采取的纠正措施不影响前期试验结果，" +
                "发生故障的试验项目应重做；若采取的纠正措施影响到前期试验结果，则发生故障的试验项目和受影响的试验项目均应重做。";
        // 试验恢复处理
        String recoverHandle = "承研承制单位对试验中暴露的问题采取改进措施，经试验验证其他有关单位确认问题已解决，" +
                "承试单位提出恢复或重新试验的申请，经批准后由原承试单位实施试验。";
        // 试验数据记录要求
        String dataLogReq = "应明确试验期间试验数据记录要求，试验数据主要包括试验样品信息和状态记录、" +
                "试验设备和检测仪器信息及校准状态、样品连接、试验样品响应和功能性能测试数据、试验中断处理信息等。";
        // 试验组织及任务分工
        String orgAndDivision = "为确保试验顺利实施，应成立试验工作组，对试验过程进行管理和监控。\n" +
                "\t由工业和信息化部电子第四研究院（简称“电子四院”）等单位共同成立电磁兼容性试验工作组。\n" +
                "\t组长单位：工业和信息化部电子第四研究院；\n" +
                "\t副组长单位：XXXX；\n" +
                "\t成员单位：XXXXXX；\n" +
                "\t根据功能性能试验工作组结构，本次试验任务分工如下：\n" +
                "\tXXXXXXX";
        // 试验保障
        String examSafeguard = "a)\t环境条件符合试验要求；\n" +
                "\tb)\t试验场地满足测试要求；\n" +
                "\tc)\t所有试验及试验设备的技术保障工作，由工业和信息化部电子第四研究院统一负责；\n" +
                "\td)\t试验期间地面设备间的通信保障工作由工业和信息化部电子第四研究院提供；\n" +
                "\te)\t试验期间的录像、照相工作由工业和信息化部电子第四研究院承担。";
        // 试验安全
        String examSafety = "所有参加试验的人员必须经过登记，试验中的操作按照试验分工要求由相应人员依据操作规范执行。" +
                "试验安全保证要求如下：\n" +
                "\ta)\t试验现场应规范、整洁，特别是用电设备及电缆使用、放置合理，不出现拉扯、绞缠的现象；\n" +
                "\tb)\t试验设备由专人操作，试验过程中，无关人员不在试验现场来回走动、不随意操作试验设备；\n" +
                "\tc)\t对试验过程进行监控，确保能够及时发现并处理问题；\n" +
                "\td)\t试验现场应有防护措施。\n" +
                "\te)\t试验前，严格按照试验配置图进行配置并检查确认；\n" +
                "\tf)\t在试验过程中，各级人员按照职责分工进行相应操作。";
        // 试验的其它要求和有关问题说明
        String otherReq = "无";
        LeftParagraph t7_2_2 = new LeftParagraph("7.2.2　RE102测试设备", font, smallSize);
        LeftParagraph t7_3 = new LeftParagraph("7.3　陪试及监测设备", font, smallSize);
        bodyParagraph c7_3 = new bodyParagraph(partnerCheck, font, smallSize);
        MidParagraph table7_2Title = new MidParagraph("表7-2\t陪试及监测设备", font, smallSize);
        float[] table7_2Width = new float[]{60, 180, 90, 120, 150};
        Table table7_2 = new Table(table7_2Width).setWidthPercent(100).setMarginLeft(50).setMarginRight(50);
        table7_2.addCell(new Cell().add(new Paragraph("序号").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table7_2.addCell(new Cell().add(new Paragraph("名称").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table7_2.addCell(new Cell().add(new Paragraph("参考型号").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table7_2.addCell(new Cell().add(new Paragraph("主要功能").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        table7_2.addCell(new Cell().add(new Paragraph("检验/检定状态").setFont(font).
                setFontSize(smallSize))).setTextAlignment(TextAlignment.CENTER);
        for (int i = 0; i < 12; i++){
            for (int j = 0; j < 5; j++){
                table7_2.addCell(new Cell().add(new Paragraph("\n").setFont(font).setFontSize(smallSize)));
            }
        }
        LeftParagraph t8 = new LeftParagraph("8 与标准的偏离说明", font, smallSize);
        bodyParagraph c8 = new bodyParagraph(plusMinusParts, font, smallSize);
        LeftParagraph t9 = new LeftParagraph("9 试验的中断处理与恢复", font, smallSize);
        /** 红色部分*/
        LeftParagraph t9_1 = new LeftParagraph("9.1 故障分类", font, smallSize);
        LeftParagraph t9_1_1 = new LeftParagraph("9.1.1 非责任故障的判定", font, smallSize);
        bodyParagraph c9_1_1 = new bodyParagraph(nonresponsErrorDisgust, font, smallSize);
        LeftParagraph t9_1_2 = new LeftParagraph("9.1.2 责任故障的判定", font, smallSize);
        bodyParagraph c9_1_2 = new bodyParagraph(responseErrorDisgust, font, smallSize);
        LeftParagraph t9_2 = new LeftParagraph("9.2 试验中断处理", font, smallSize);
        bodyParagraph c9_2 = new bodyParagraph(examStopHandle, font, smallSize);
        LeftParagraph t9_3 = new LeftParagraph("9.3 故障处理", font, smallSize);
        bodyParagraph c9_3 = new bodyParagraph(errorHandle, font, smallSize);
        LeftParagraph t9_4 = new LeftParagraph("9.4 试验恢复处理", font, smallSize);
        bodyParagraph c9_4 = new bodyParagraph(recoverHandle, font, smallSize);
        LeftParagraph t9_5 = new LeftParagraph("9.5 试验数据记录要求", font, smallSize);
        bodyParagraph c9_5 = new bodyParagraph(dataLogReq, font, smallSize);
        LeftParagraph t10 = new LeftParagraph("10 试验组织及任务分工", font, smallSize);
        bodyParagraph c10 = new bodyParagraph(orgAndDivision, font, smallSize);
        LeftParagraph t11 = new LeftParagraph("11 试验保障", font, smallSize);
        bodyParagraph c11 = new bodyParagraph(examSafeguard, font, smallSize);
        LeftParagraph t12 = new LeftParagraph("12 试验安全", font, smallSize);
        bodyParagraph c12 = new bodyParagraph(examSafety, font, smallSize);
        LeftParagraph t13 = new LeftParagraph("13 保密要求与措施", font, smallSize);
        LeftParagraph t14 = new LeftParagraph("14 试验的其它要求和有关问题说明", font, smallSize);
        bodyParagraph c14 = new bodyParagraph(otherReq, font, smallSize);

        document.add(t7_2_2);
        catalogs.put("7.2.2 RE102测试设备", pdf.getNumberOfPages());
        document.add(t7_3);
        catalogs.put("7.3 陪试及监测设备", pdf.getNumberOfPages());
        document.add(c7_3);
        document.add(table7_2Title);
        document.add(t8);
        catalogs.put("8 与标准的偏离说明", pdf.getNumberOfPages());
        document.add(c8);
        document.add(t9);
        catalogs.put("9 试验的中断处理与恢复", pdf.getNumberOfPages());
        document.add(t9_1);
        catalogs.put("9.1 故障分类", pdf.getNumberOfPages());
        document.add(t9_1_1);
        catalogs.put("9.1.1 非责任故障的判定", pdf.getNumberOfPages());
        document.add(c9_1_1);
        document.add(t9_1_2);
        catalogs.put("9.1.2 责任故障的判定", pdf.getNumberOfPages());
        document.add(c9_1_2);
        document.add(t9_2);
        catalogs.put("9.2 试验中断处理", pdf.getNumberOfPages());
        document.add(c9_2);
        document.add(t9_3);
        catalogs.put("9.3 故障处理", pdf.getNumberOfPages());
        document.add(c9_3);
        document.add(t9_4);
        catalogs.put("9.4 试验恢复处理", pdf.getNumberOfPages());
        document.add(c9_4);
        document.add(t9_5);
        catalogs.put("9.5 试验数据记录要求", pdf.getNumberOfPages());
        document.add(c9_5);
        document.add(t10);
        catalogs.put("10 试验组织及任务分工", pdf.getNumberOfPages());
        document.add(c10);
        document.add(t11);
        catalogs.put("11 试验保障", pdf.getNumberOfPages());
        document.add(c11);
        document.add(t12);
        catalogs.put("12 试验安全", pdf.getNumberOfPages());
        document.add(c12);
        document.add(t13);
        catalogs.put("13 保密要求与措施", pdf.getNumberOfPages());
        document.add(t14);
        catalogs.put("14 试验的其它要求和有关问题说明", pdf.getNumberOfPages());
        document.add(c14);
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        /** 15*/
        // 试验实施网络图
        String examNetwork = "试验计划201X年X月X日开始，201X年X月XX日结束，持续XX天，试验实施网络图见图13-1。";

        LeftParagraph t15 = new LeftParagraph("15　试验实施网络图", font, smallSize);
        bodyParagraph c15 = new bodyParagraph(examNetwork, font, smallSize);
        MidParagraph image13_1Title = new MidParagraph("图13-1\t试验实施网络图", font, smallSize);

        document.add(t15);
        catalogs.put("15 试验实施网络图", pdf.getNumberOfPages());
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
        String degreeOfPassword = "--";
        // 型号+名称
        String sizeAndName = "型号+名称";
        // 首页标注的页数
        String totalPage = "XX";
        // 编制单位
        String Unit = "(编制单位)";
        // 首页年份
        String year = "201X";
        // 首页月份
        String month = "X";
        // 编制日期
        String orgDate = "201X年X月X日";
        // 校对日期
        String checkDate = "201X年X月X日";
        // 审核日期
        String examDate = "201X年X月X日";
        // 批准日期
        String apprvDate = "201X年X月X日";

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
        PdfDocument newPdf = new PdfDocument(new PdfWriter(NEWDEST));
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
            catch(IOException e){ }
        }
    }
}
