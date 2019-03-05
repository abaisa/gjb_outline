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

    @Autowired
    private DependencyService dependencyService;

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
        String schema = dbService.fetchSchema( pageNumber, DbColnameEnums.SCHEMA_PREFIX.getValue());
//        String schema = dbService.fetchData(outlineID, pageNumber, DbColnameEnums.SCHEMA_PREFIX.getValue());
        String data = dbService.fetchData(outlineID, pageNumber, DbColnameEnums.DATA_PREFIX.getValue());
//        String outlineAdviceProofread = dbService.fetchData(outlineID, "outline_advice_proofread");
//        String outlineAdviceAudit = dbService.fetchData(outlineID, "outline_advice_audit");
//        String outlineAdviceAuthorize = dbService.fetchData(outlineID, "outline_advice_authorize");
        System.out.println("db fetch schema >>> " + schema);
        System.out.println("db fetch data >>> " + data);
        if (Strings.isNullOrEmpty(schema) || Strings.isNullOrEmpty(data)) {
            throw new ServiceException(ExceptionEnums.DB_EMPTY_ERR);
        }

        //处理有依赖关系的schema
        schema = dependencyService.generateDependencySchema(outlineID, pageNumber, schema);

        //处理有依赖关系的数据信息
        data = dependencyService.generateDependencyData(outlineID, pageNumber, data);

        // 打包返回数据
        result.put("schema", schema);
        result.put("data", data);
        result.put("page_id", String.valueOf(pageNumber));
        dbService.updatePageNumber(outlineID, "current_page_number", pageNumber);
//        result.put("outlineAdviceProofread", outlineAdviceProofread);
//        result.put("outlineAdviceAudit", outlineAdviceAudit);
//        result.put("outlineAdviceAuthorize", outlineAdviceAuthorize);

        System.out.println("getResponseData return json >>> " + JSON.toJSONString(result));
        return JSON.toJSONString(result);
    }


    public String getAdvice(int outlineID) throws Exception {
        Map<String, String> result = new HashMap<>();
        String outlineAdviceProofread = dbService.fetchData(outlineID, "outline_advice_proofread");
        String outlineAdviceAudit = dbService.fetchData(outlineID, "outline_advice_audit");
        String outlineAdviceAuthorize = dbService.fetchData(outlineID, "outline_advice_authorize");
        result.put("outlineAdviceProofread", outlineAdviceProofread);
        result.put("outlineAdviceAudit", outlineAdviceAudit);
        result.put("outlineAdviceAuthorize", outlineAdviceAuthorize);
        return JSON.toJSONString(result);
    }


    public void submitPageData(Integer outlineID, Integer sourcePageNumber, Integer pageAction, String data) throws Exception {
        dbService.submitData(outlineID, sourcePageNumber, DbColnameEnums.DATA_PREFIX.getValue(), data);
        if(pageAction == 3) {
            dependencyService.generateDataAfterSubmit(outlineID, sourcePageNumber, data);
        }
    }
}


// String s = "{\"type\":\"array\",\"title\":\"DBJsonEditor\",\"items\":{\"type\":\"object\",\"title\":\"JsonEditorElement\",\"headerTemplate\":\"{{ i1 }} - {{ self.name }} (age {{ self.age }})\",\"properties\":{\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"}}}}";