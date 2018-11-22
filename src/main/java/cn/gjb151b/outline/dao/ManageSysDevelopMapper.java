package cn.gjb151b.outline.dao;

import cn.gjb151b.outline.model.ManageSysDevelop;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ManageSysDevelopMapper {
    ManageSysDevelop selectByPrimaryKey(@Param("devItemId") String devItemId);
}