package cn.gjb151b.outline.service;

import cn.gjb151b.outline.Constants.ExceptionEnums;
import cn.gjb151b.outline.dao.ManageSysOutlineMapper;
import cn.gjb151b.outline.utils.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 封装操作数据库的方法
 * 对data 和 schema 的数据库操作全部从这里走
 * 之后也建议将新的数据库操作
 */
@Service(value = "DBService")
public class DBService {

    private final ManageSysOutlineMapper mapper;

    @Autowired
    public DBService(ManageSysOutlineMapper manageSysOutlineMapper) {
        this.mapper = manageSysOutlineMapper;
    }

    /**
     *
     * @param outlineID 大纲id
     * @param pageID    页码
     * @param data      写入数据
     * @param prefix    列名前缀
     * @throws Exception
     */
    public void submitData(int outlineID, int pageID, String prefix, String data) throws Exception{
        String colName = prefix + pageID;
        if (pageID < 1){
            throw new ServiceException(ExceptionEnums.DB_FETCH_ERR);
        }
        mapper.updateCol(outlineID, colName, data);
    }

    /**
     *
     * @param outlineID 大纲id
     * @param data      写入数据
     * @param colName   列名
     * @throws Exception
     */
    public void submitData(int outlineID, String colName, String data) throws Exception{
        mapper.updateCol(outlineID, colName, data);
    }

    /**
     *
     * @param outlineID 大纲id
     * @param pageID    页码
     * @param prefix    列名前缀,或者
     * @return data
     * @throws Exception
     */
    public String fetchData(int outlineID, int pageID, String prefix) throws Exception {
        String colName = prefix + pageID;
        if (pageID < 1){
            throw new ServiceException(ExceptionEnums.DB_FETCH_ERR);
        }
        return mapper.selectCol(outlineID, colName);
    }

    /**
     *
     * @param outlineID 大纲id
     * @param colName   列名
     * @return data
     * @throws Exception
     */
    public String fetchData(int outlineID, String colName) throws Exception {
        return mapper.selectCol(outlineID, colName);
    }
}
