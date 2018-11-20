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

}