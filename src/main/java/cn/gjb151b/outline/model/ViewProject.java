package cn.gjb151b.outline.model;
/**
 * 定义一个项目的所有参数类
 */

public class ViewProject {
    private String outlineName;
    private int outlineStatus;
    private String outlineDevItemId;
    private int outlineId;

    public ViewProject() {
    }

    public ViewProject(String outlineName, int outlineStatus, String outlineDevItemId, int outlineId) {
        this.outlineName = outlineName;
        this.outlineStatus = outlineStatus;
        this.outlineDevItemId = outlineDevItemId;
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

    public String getOutlineDevItemId(){return outlineDevItemId;}

    public void  setOutlineDevItemId(String outlineItemid){this.outlineDevItemId= outlineItemid;}

    public int getOutlineId(){return  outlineId;}

    public void setOutlineId(int outlineId){this.outlineId = outlineId;}
}

