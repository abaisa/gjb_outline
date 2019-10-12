package cn.gjb151b.outline.Constants;

public enum PathStoreEnum {
    WINDOWS_SQLDATA_AND_IMG_SOURTH_PATH("C://Users//GJB_user//Desktop//导入//"),
    WINDOWS_IMG_CHANGED_DEST_PATH("D://GJB151B//gjb151b//changed_img//"),
    WINDOWS_IMG_STANDARD_DEST_PATH("D://GJB151B//gjb151b//standard_img//"),
    WINDOWS_IMG_UPLOAD_DEST_PATH("D://GJB151B//gjb_outline//upload_img//"),
    WINDOWS_ARCHIVED_FILR_DEST_PATH("C://Users//GJB_user//Desktop//导入//归档//"),
    WINDOWS_PDF_EXPORT_PATH("C://Users//GJB_user//Desktop//导出//"),
    WINDOWS_CONFIG_PATH("D://GJB151B//config.properties");

//    WINDOWS_SQLDATA_AND_IMG_SOURTH_PATH("/Users/chenzhehao/Desktop/导入/"),
//    WINDOWS_IMG_CHANGED_DEST_PATH("/Users/chenzhehao/GJB151B/gjb151b/changed_img/"),
//    WINDOWS_IMG_STANDARD_DEST_PATH("/Users/chenzhehao/GJB151B/gjb151b/standard_img/"),
//    WINDOWS_IMG_UPLOAD_DEST_PATH("/Users/chenzhehao/GJB151B/gjb_outline/upload_img/"),
//    WINDOWS_ARCHIVED_FILR_DEST_PATH("/Users/chenzhehao/Desktop/导入/归档/"),
//    WINDOWS_PDF_EXPORT_PATH("/Users/chenzhehao/Desktop/导出/"),
//    WINDOWS_CONFIG_PATH("/Users/chenzhehao/GJB151B/config.properties");
    private String value;

    PathStoreEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
