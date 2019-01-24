package cn.gjb151b.outline.outlineDao;

import cn.gjb151b.outline.model.ManageSysOutline;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManageSysOutlineMapper {
    // 这都是很不安全的东西！但是很好用！直接无脑注入SQL语句
    void updateCol(@Param("outlineID") int outlineID, @Param("colName") String colName, @Param("data") String data);

    void updateColInt(@Param("outlineID") int outlineID, @Param("colName") String colName, @Param("data") Integer data);

    String selectCol(@Param("outlineID") int outlineID, @Param("colName") String colName);

    ManageSysOutline selectByPrimaryKey(@Param("outlineId") Integer outlineId);

    ManageSysOutline selectProjectByItemId(@Param("outlineDevItemId") String outlineDevItemId);

    List<ManageSysOutline> findAllItem();

    void updateItemCol(@Param("colName")  String colName, @Param("data") String data, @Param("outlineDevItemid") String outlineDevItemid);

    String selectItemidByName(@Param("outlineName") String outlineName);

    void deleteItemByName(@Param("outlineName") String outlineName);

    void addItem(@Param("colName1") String colName1, @Param("data1") String data1, @Param("colName2") String colName2, @Param("data2") String data2, @Param("colName3") String colName3, @Param("data3") String data3, @Param("colName4") String colName4, @Param("data4") Integer data4);


}