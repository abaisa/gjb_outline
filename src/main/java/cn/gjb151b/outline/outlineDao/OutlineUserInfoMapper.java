package cn.gjb151b.outline.outlineDao;

import cn.gjb151b.outline.model.OutlineUserInfo;
import org.apache.ibatis.annotations.Param;

public interface OutlineUserInfoMapper {
    OutlineUserInfo selectUserByName(@Param("userName") String userName);

}