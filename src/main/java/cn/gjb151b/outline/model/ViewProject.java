package cn.gjb151b.outline.model;
/**
 * 定义一个项目的所有参数类
 */

public class ViewProject {
    private String outlineName;
    private int outlineStatus;
    private String outlineItemid;
    private int outlineId;

    public ViewProject() {
    }

    public ViewProject(String outlineName, int outlineStatus, String outlineItemid, int outlineId) {
        this.outlineName = outlineName;
        this.outlineStatus = outlineStatus;
        this.outlineItemid = outlineItemid;
        this.outlineId = outlineId;
    }

    public String getOutlineName(){return outlineName;}

    public void setOutlineName(String outlineName) {
        this.outlineName = outlineName;
    }

    public int getOutlineStatus(){return outlineStatus;}

    public void setOutlineStatus(int outlineStatus) {
        this.outlineStatus = outlineStatus;
    }

    public String getOutlineItemid(){return outlineItemid;}

    public void  setOutlineItemid(String outlineItemid){this.outlineItemid = outlineItemid;}

    public int getOutlineId(){return  outlineId;}

    public void setOutlineId(int outlineId){this.outlineId = outlineId;}
}

