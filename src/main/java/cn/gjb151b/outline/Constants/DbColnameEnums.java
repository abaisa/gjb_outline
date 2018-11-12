package cn.gjb151b.outline.Constants;

public enum DbColnameEnums {
    // 列名前缀常量部分
    SCHEMA_PREFIX("schema_"),
    DATA_PREFIX("data_"),

    // 列名常量部分
    OUTLINE_ID("outline_id");

    private String name;

    DbColnameEnums(String name){
        this.setValue(name);
    }

    public String getValue() {
        return name;
    }

    private void setValue(String name) {
        this.name = name;
    }
}
