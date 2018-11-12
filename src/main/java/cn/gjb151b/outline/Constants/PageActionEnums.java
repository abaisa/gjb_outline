package cn.gjb151b.outline.Constants;

/**
 * 页面请求枚举
 * 当前有下一页和上一页两个动作
 */
public enum PageActionEnums {
    NEXT(1),
    PREVIOUS(2);

    private int status;

    PageActionEnums(int status){
        this.setValue(status);
    }

    public int getValue() {
        return status;
    }

    private void setValue(int status) {
        this.status = status;
    }

}
