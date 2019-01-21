package cn.gjb151b.outline.model;
/**
 * 定义用户项目信息类
 */

public class UserProject {
    private String userName;
    private String userNew;
    private String userProofread;
    private String userAudit;
    private String userAuthorize;

    public UserProject() {
    }

    public UserProject(String userName, String userNew, String userProofread, String userAudit, String userAuthorize) {
        this.userName = userName;
        this.userNew = userNew;
        this.userProofread = userProofread;
        this.userAudit = userAudit;
        this.userAuthorize = userAuthorize;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserNew(String userNew) {
        this.userNew = userNew;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserNew() {
        return userNew;
    }

    public String getUserProofread() {
        return userProofread;
    }

    public String getUserAudit(){return userAudit;}

    public String getUserAuthorize(){return userAuthorize;}

    public void setUserProofread(String userProofread) {
        this.userProofread = userProofread;
    }

    public void setUserAudit(String userAudit){this.userAudit = userAudit;}

    public void setUserAuthorize(String userAuthorize){this.userAuthorize = userAuthorize;}
}
