package cn.gjb151b.outline.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface OutlineDemoMapper {
    // 这都是很不安全的东西！但是很好用！直接无脑注入SQL语句
    void updateCol(@Param("outlineID")int outlineID, @Param("colName")String colName, @Param("data")String data);

    String selectCol(@Param("outlineID")int outlineID, @Param("colName")String colName);
}