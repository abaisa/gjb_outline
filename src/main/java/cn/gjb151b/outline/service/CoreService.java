package cn.gjb151b.outline.service;

import cn.gjb151b.outline.Constants.DbColnameEnums;
import cn.gjb151b.outline.Constants.PageActionEnums;
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
        int pageNumber;
        // todo 跳转页面的逻辑会更加复杂，需要参数判断
        if (pageAction == PageActionEnums.NEXT.getValue()) {
            pageNumber = PageDispatcher.getInstance().next(sourcePageNumber);
        } else if (pageAction == PageActionEnums.PREVIOUS.getValue()) {
            pageNumber = PageDispatcher.getInstance().previous(sourcePageNumber);
        } else {
            throw new Exception("page action error");
        }

        Map<String, String> result = new HashMap<>();
        String schema = dbService.fetchData(outlineID, pageNumber, DbColnameEnums.SCHEMA_PREFIX.getValue());
        String data = dbService.fetchData(outlineID, pageNumber, DbColnameEnums.DATA_PREFIX.getValue());
        System.out.println(schema);
        System.out.println(data);
        if (Strings.isNullOrEmpty(schema) || Strings.isNullOrEmpty(data)) {
            throw new Exception("error db response");
        }
        result.put("schema", schema);
        result.put("data", data);

        System.out.println("getResponseData return json >>> " + JSON.toJSONString(result));
        return JSON.toJSONString(result);
    }


    public void submitPageData(Integer outlineID, Integer sourcePageNumber, String data) throws Exception {
        dbService.submitData(outlineID, sourcePageNumber, DbColnameEnums.DATA_PREFIX.getValue(), data);
    }
}


// String s = "{\"type\":\"array\",\"title\":\"DBJsonEditor\",\"items\":{\"type\":\"object\",\"title\":\"JsonEditorElement\",\"headerTemplate\":\"{{ i1 }} - {{ self.name }} (age {{ self.age }})\",\"properties\":{\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"}}}}";