package cn.gjb151b.outline.dao;

import cn.gjb151b.outline.model.ManageSysDevelop;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManageSysDevelopMapper {
    ManageSysDevelop selectByPrimaryKey(@Param("devItemId") String devItemId);

    List<ManageSysDevelop> findAllItem();

    String selectColByName(@Param("colName") String colName, @Param("devName") String devName);

    Integer selectDevIdByName(@Param("devName") String denName);

    String selectColByDevItemId(@Param("colName") String colName, @Param("devItemId") String devItemId);

    Integer addItem(ManageSysDevelop manageSysDevelop);

    ManageSysDevelop getProjectByDevName(@Param("devName") String devName);
}
