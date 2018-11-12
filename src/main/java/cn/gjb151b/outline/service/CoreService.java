package cn.gjb151b.outline.service;

import cn.gjb151b.outline.Constants.DbColnameEnums;
import cn.gjb151b.outline.Constants.ExceptionEnums;
import cn.gjb151b.outline.Constants.PageActionEnums;
import cn.gjb151b.outline.utils.ServiceException;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 我真的不知道该叫啥名字了
 */
@Service(value = "CoreService")
public class CoreService {

    private final DBService dbService;

    @Autowired
    public CoreService(DBService dbService) {
        this.dbService = dbService;
    }

    public String getResponseData(int outlineID, int sourcePageNumber, int pageAction) throws Exception {
        // 获取下一页页码
        int pageNumber;
        if (pageAction == PageActionEnums.NEXT.getValue()) {
            pageNumber = PageDispatcher.getInstance().next(sourcePageNumber);
        } else if (pageAction == PageActionEnums.PREVIOUS.getValue()) {
            pageNumber = PageDispatcher.getInstance().previous(sourcePageNumber);
        } else {
            throw new ServiceException(ExceptionEnums.PARAM_PAGE_ID_ERR);
        }

        // 从db取出schema和data
        Map<String, String> result = new HashMap<>();
        String schema = dbService.fetchData(outlineID, pageNumber, DbColnameEnums.SCHEMA_PREFIX.getValue());
        String data = dbService.fetchData(outlineID, pageNumber, DbColnameEnums.DATA_PREFIX.getValue());
        System.out.println(schema);
        System.out.println(data);
        if (Strings.isNullOrEmpty(schema) || Strings.isNullOrEmpty(data)) {
            throw new ServiceException(ExceptionEnums.DB_EMPTY_ERR);
        }

        // 打包返回数据
        result.put("schema", schema);
        result.put("data", data);
        result.put("page_id", String.valueOf(pageNumber));

        System.out.println("getResponseData return json >>> " + JSON.toJSONString(result));
        return JSON.toJSONString(result);
    }


    public void submitPageData(Integer outlineID, Integer sourcePageNumber, String data) throws Exception {
        dbService.submitData(outlineID, sourcePageNumber, DbColnameEnums.DATA_PREFIX.getValue(), data);
    }
}


// String s = "{\"type\":\"array\",\"title\":\"DBJsonEditor\",\"items\":{\"type\":\"object\",\"title\":\"JsonEditorElement\",\"headerTemplate\":\"{{ i1 }} - {{ self.name }} (age {{ self.age }})\",\"properties\":{\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"}}}}";