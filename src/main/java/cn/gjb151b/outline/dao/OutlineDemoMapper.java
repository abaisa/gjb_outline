package cn.gjb151b.outline.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface OutlineDemoMapper {
    @Select("SELECT schema_1 FROM outline_demo WHERE outline_id = #{outlineID};")
    String getJsonSchema(@Param("outlineID")int outlineID, @Param("pageNumber")int pageNumber);

    @Select("SELECT data_1 FROM outline_demo WHERE outline_id = #{outlineID};")
    String getJsonData(@Param("outlineID")int outlineID, @Param("pageNumber")int pageNumber);

    // 这都是很不好的东西！但是很好用！直接无脑注入SQL语句
    void updateCol(@Param("outlineID")int outlineID, @Param("colName")String colName, @Param("data")String data);

    String selectCol(@Param("outlineID")int outlineID, @Param("colName")String colName);
}