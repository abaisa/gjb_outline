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

    void updateColByName(@Param("colName") String colName, @Param("data") String data, @Param("userName") String userName);

    List<String> selectNewItemOperator(@Param("outlineDevItemId") String OutlineItemId);

    List<String> selectProofreadItemOperator(@Param("outlineDevItemId") String OutlineItemId);

    List<String> selectAuditItemOperator(@Param("outlineDevItemId") String OutlineItemId);

    List<String> selectAuthorizeItemOperator(@Param("outlineDevItemId") String OutlineItemId);

    void updateUserNewItem(@Param("devItemId") String devItemId);

    void updateUserProofreadItem(@Param("devItemId") String devItemId);

    void updateUserAuditItem(@Param("devItemId") String devItemId);

    void updateUserAuthorizeItem(@Param("devItemId") String devItemId);

    void addUserItem(@Param("cloName1") String cloName1, @Param("colName2") String colName2, @Param("data1") String data1, @Param("data2") String data2, @Param("userName") String userName, @Param("colName3") String colName3,@Param("data3") int data3 );






}