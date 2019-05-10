package cn.gjb151b.outline.service;

import cn.gjb151b.outline.Constants.ExceptionEnums;
import cn.gjb151b.outline.outlineDao.ManageSysOutlineMapper;
import cn.gjb151b.outline.outlineDao.ManageSysSchemaMapper;
import cn.gjb151b.outline.utils.ServiceException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 封装操作数据库的方法
 * 对data 和 schema 的数据库操作全部从这里走
 * 之后也建议将新的数据库操作
 */
@Service(value = "DBService")
public class DBService {

    private final ManageSysOutlineMapper manageSysOutlineMapper;
    private final ManageSysSchemaMapper manageSysSchemaMapper;

    @Autowired
    public DBService(ManageSysOutlineMapper manageSysOutlineMapper, ManageSysSchemaMapper manageSysSchemaMapper) {
        this.manageSysSchemaMapper = manageSysSchemaMapper;
        this.manageSysOutlineMapper = manageSysOutlineMapper;
    }

    /**
     * @param outlineID 大纲id
     * @param pageID    页码
     * @param data      写入数据
     * @param prefix    列名前缀
     * @throws Exception
     */
    public void submitData(int outlineID, int pageID, String prefix, String data) throws Exception {
        String colName = prefix + pageID;
        if (pageID < 1) {
            throw new ServiceException(ExceptionEnums.DB_FETCH_ERR);
        }
        if (pageID == 4) {
            JSONObject currentOutlineData4Object = JSON.parseObject(data);
            String outlineData4 = manageSysOutlineMapper.selectCol(outlineID, "outline_data_4");
            JSONObject outlineDataObject = JSON.parseObject(outlineData4);
            JSONArray equipmentPicArray = outlineDataObject.getJSONArray("分系统/设备照片");
            JSONArray equipmentRelationPicArray = outlineDataObject.getJSONArray("分系统/设备关系图");
            if (equipmentPicArray != null) {
                currentOutlineData4Object.put("分系统/设备照片", equipmentPicArray);
            }
            if (equipmentRelationPicArray != null) {
                currentOutlineData4Object.put("分系统/设备关系图", equipmentRelationPicArray);
            }
            data = JSON.toJSONString(currentOutlineData4Object);
        }

        if (pageID >= 14 && pageID <= 34) {
            JSONObject currentOutlineData14To34Object = JSON.parseObject(data);
            String outlineData14To34 = manageSysOutlineMapper.selectCol(outlineID, colName);
            JSONObject outlineDataObject = JSON.parseObject(outlineData14To34);
            if (outlineDataObject.containsKey("项目试验图")) {
                if (outlineDataObject.getString("项目试验图") != null) {
                    currentOutlineData14To34Object.put("项目试验图", outlineDataObject.getString("项目试验图"));
                }
            }
            if (outlineDataObject.containsKey("修改图形理由")) {
                if (outlineDataObject.getString("修改图形理由") != null) {
                    currentOutlineData14To34Object.put("修改图形理由", outlineDataObject.getString("修改图形理由"));
                }
            }
            if (outlineDataObject.containsKey("修改方法")) {
                if (outlineDataObject.getString("修改方法") != null) {
                    currentOutlineData14To34Object.put("修改方法", outlineDataObject.getString("修改方法"));
                }
            }
            data = JSON.toJSONString(currentOutlineData14To34Object);
        }

        data = data.replaceAll("\\\\", "\\\\\\\\");
        manageSysOutlineMapper.updateCol(outlineID, colName, data);
    }

    /**
     * @param outlineID 大纲id
     * @param data      写入数据
     * @param colName   列名
     * @throws Exception
     */
    public void submitData(int outlineID, String colName, String data) throws Exception {
        data = data.replaceAll("\\\\", "\\\\\\\\");
        manageSysOutlineMapper.updateCol(outlineID, colName, data);
    }


    public void updatePageNumber(int outlineID, String colName, int data) throws Exception {
        manageSysOutlineMapper.updateCol2(outlineID, colName, data);

    }

    /**
     * @param outlineID 大纲id
     * @param data      写入整型数据
     * @param colName   列名
     * @throws Exception
     */
    public void submitStatus(int outlineID, String colName, int data) throws Exception {
        manageSysOutlineMapper.updateColInt(outlineID, colName, data);

    }

    /**
     * @param outlineID 大纲id
     * @param pageID    页码
     * @param prefix    列名前缀,或者
     * @return data
     * @throws Exception
     */
    public String fetchData(int outlineID, int pageID, String prefix) throws Exception {
        String colName = prefix + pageID;
        if (pageID < 1) {
            throw new ServiceException(ExceptionEnums.DB_FETCH_ERR);
        }

        return manageSysOutlineMapper.selectCol(outlineID, colName);
    }

    public String fetchSchema(int pageID, String prefix) throws Exception {
        String colName = prefix + pageID;
        if (pageID < 1) {
            throw new ServiceException(ExceptionEnums.DB_FETCH_ERR);
        }

//        return manageSysOutlineMapper.selectCol(1, colName);
        return manageSysSchemaMapper.selectCol(1,colName);
    }

    public String fetchDefaultData(int pageID, String colName) throws Exception {
        if (pageID < 1) {
            throw new ServiceException(ExceptionEnums.DB_FETCH_ERR);
        }

//        return manageSysOutlineMapper.selectCol(1, colName);
        return manageSysSchemaMapper.selectCol(1, colName);
    }

    /**
     * @param outlineID 大纲id
     * @param colName   列名
     * @return data
     * @throws Exception
     */
    public String fetchData(int outlineID, String colName) throws Exception {
        return manageSysOutlineMapper.selectCol(outlineID, colName);
    }

    public Integer fetehPageNumber(int outlineID, String colName) throws Exception{
        return  manageSysOutlineMapper.selectCol2(outlineID, colName);
    }

}
