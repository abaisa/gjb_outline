package cn.gjb151b.outline.model;

/**
 * Created by ddgdd on 2018/8/23 0023 15:36
 */
public class UserLogin {

    private String userName;
    private String userPassword;
    private int userLevel;
    private int userId;

    public UserLogin() {
    }

    public UserLogin(String userName, String userPassword, int userLevel, int userId) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userLevel = userLevel;
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
