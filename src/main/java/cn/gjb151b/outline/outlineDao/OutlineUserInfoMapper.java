package cn.gjb151b.outline.outlineDao;

import cn.gjb151b.outline.model.OutlineUserInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OutlineUserInfoMapper {
    OutlineUserInfo selectUserByName(@Param("userName") String userName);

    List<OutlineUserInfo> findAllUser();

    void updateCol(@Param("userId") int userId, @Param("colName") String colName, @Param("data") String data);

    OutlineUserInfo selectUserById(@Param("userId") int userId);

    void deleteUserById(@Param("userId") int userId);

    void updateUser(@Param("userId") int userId, @Param("colName") String colName, @Param("data") Integer data);

    void addUser(OutlineUserInfo outlineUserInfo);

    List<String> selectNewItemOperator(@Param("outlineDevItemId") String OutlineItemId);

    List<String> selectProofreadItemOperator(@Param("outlineDevItemId") String OutlineItemId);

    List<String> selectAuditItemOperator(@Param("outlineDevItemId") String OutlineItemId);

    List<String> selectAuthorizeItemOperator(@Param("outlineDevItemId") String OutlineItemId);



}