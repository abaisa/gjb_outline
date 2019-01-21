package cn.gjb151b.outline.model;
/**
 * 定义一个项目的所有参数类
 */

public class ViewProject {
    private String devName;
    private int devStatus;
    private String devItemid;
    private int devId;

    public ViewProject() {
    }

    public ViewProject(String devName, int devStatus, String devItemid, int devId) {
        this.devName = devName;
        this.devStatus = devStatus;
        this.devItemid = devItemid;
        this.devId = devId;
    }

    public String getDevName(){return devName;}

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public int getDevStatus(){return devStatus;}

    public void setDevStatus(int devStatus) {
        this.devStatus = devStatus;
    }

    public String getDevItemid(){return devItemid;}

    public void  setDevItemid(String devItemid){this.devItemid = devItemid;}

    public int getDevId(){return  devId;}

    public void setDevId(int devId){this.devId = devId;}
}

