package cn.gjb151b.outline.model;

import lombok.*;

import java.util.Date;

@Data
public class ManageSysOutline {

    private Integer outlineId;

    private String outlineItemid;

    private String outlineDevItemid;

    private String outlineName;

    private Integer outlineDevSubsysEqp;

    private String outlineDevSubsysEqpName;

    private String outlineDevSubsysEqpModel;

    private String outlineDevSubsysEqpNum;

    private String outlineTestBoundary;

    private String outlineSchema3;

    private String outlineData3;

    private String outlineSchema4;

    private String outlineData4;

    private String outlineSchema5;

    private String outlineData5;

    private String outlineSchema7;

    private String outlineData7;

    private String outlineSchema8;

    private String outlineData8;

    private String outlineSchema10;

    private String outlineData10;

    private String outlineSchema13;

    private String outlineData13;

    private String outlineSchema14;

    private String outlineData14;

    private String outlineSchema15;

    private String outlineData15;

    private String outlineSchema17;

    private String outlineData17;

    private String outlineSchema18;

    private String outlineData18;

    private String outlineSchema23;

    private String outlineData23;

    private String outlineSchema26;

    private String outlineData26;

    private String outlineSchema27;

    private String outlineData27;

    private String outlineSchema28;

    private String outlineData28;

    private String outlineSchemaSubsysEqp;

    private String outlineDataSubsysEqp;

    private Date outlineCreateTime;

    private Date outlineNewTime;

    private Date outlineModifyTime;

    private Date outlineProofreadTime;

    private Date outlineAuditTime;

    private Date outlineAuthorizeTime;

    private Date outlineUpdateTime;

    private Integer outlineOpeator;

    private Integer outlineStatus;

    private String outlineAdviceProofread;

    private String outlineAdviceAudit;

    private String outlineAdviceAuthorize;

    public String getOutlineDevItemid(){return outlineDevItemid;}

    public String getOutlineDataSubsysEqp(){return outlineDataSubsysEqp;}

    public String getOutlineDevSubsysEqpName(){return outlineDevSubsysEqpName;}

    public String getOutlineDevSubsysEqpModel(){return outlineDevSubsysEqpModel;}

    public String getOutlineDevSubsysEqpNum(){return outlineDevSubsysEqpNum;}

    public String getOutlineSchemaSubsysEqp(){return outlineSchemaSubsysEqp;}

    public String getOutlineAdviceProofread(){return  outlineAdviceProofread;}

    public String getOutlineAdviceAudit(){return  outlineAdviceAudit;}

    public String getOutlineAdviceAuthorize(){return  outlineAdviceAuthorize;}


}