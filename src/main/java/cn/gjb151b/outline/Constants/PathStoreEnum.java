package cn.gjb151b.outline.Constants;

public enum PathStoreEnum {
    WINDOWS_SQLDATA_STORE_PATH("D://GJB151B//"),
    MACOS_SQLDATA_SRORE_PATH("/Users/chenzhehao/Desktop/");

    private String value;

    PathStoreEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
