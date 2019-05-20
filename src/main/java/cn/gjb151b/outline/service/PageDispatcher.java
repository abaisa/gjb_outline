package cn.gjb151b.outline.service;

import cn.gjb151b.outline.dao.ManageSysDevelopMapper;
import cn.gjb151b.outline.model.ManageSysDevelop;
import cn.gjb151b.outline.model.ManageSysOutline;
import cn.gjb151b.outline.outlineDao.ManageSysOutlineMapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 页面控制代码，决定页面上下页逻辑
 * 这个单子类写的有问题，有点搞事，可以改下，弄成配置文件加载什么的都行，初始化好丑啊
 */
@Component(value = "PageDispatcher")
public class PageDispatcher {
    @Autowired
    private ManageSysDevelopMapper manageSysDevelopMapper;
    @Autowired
    private ManageSysOutlineMapper manageSysOutlineMapper;

    private static PageDispatcher instance = null;

    private HashMap<Integer, Integer> nextPageRouter = new HashMap<>();
    private HashMap<Integer, Integer> previousPageRouter = new HashMap<>();

    private PageDispatcher() {
//        String developProjectList
//        ArrayList<Integer> pageNumberList = new ArrayList<>();
//        pageNumberList.add(36);
//        pageNumberList.add(55);
//        if (pageNumberList.size() > 0) {
//            nextPageRouter.put(34, pageNumberList.get(0));
//            for (int i = 0; i < pageNumberList.size() - 1; i++) {
//                nextPageRouter.put(pageNumberList.get(i), pageNumberList.get(i + 1));
//            }
//        }

//        nextPageRouter.put(3, 35);

        // 特殊上一页
//        previousPageRouter.put(10, 1006);
//        previousPageRouter.put(1001, 9);
//        previousPageRouter.put(14, 10);
    }

    public static PageDispatcher getInstance() {
        if (instance == null) {
            instance = new PageDispatcher();
        }
        return instance;
    }

    public Integer next(Integer i) {
        if (nextPageRouter.containsKey(i)) {
            return nextPageRouter.get(i);
        }
        return i + 1;
    }

    public Integer previous(Integer i) {
        if (previousPageRouter.containsKey(i)) {
            return previousPageRouter.get(i);
        }
        return i - 1;
    }

    public Integer nextPageNumber(String projectList, int sourcePageNumber) {
//        ManageSysOutline devObject = manageSysOutlineMapper.selectByPrimaryKey(outlineId);
//        String devItemId = manageSysOutlineMapper.selectCol(outlineId, "outline_dev_itemid");
//        String devItemId = devObject.getOutlineDevItemid();
//        String projectList = manageSysDevelopMapper.selectColByDevItemId("project_list", devItemId);
        JSONArray projectJsonArray = (JSONArray) JSON.parse(projectList);
        List<String> projectStrList = new ArrayList<>();
        List<Integer> pageNumberList = new ArrayList<>();
        if (projectJsonArray != null) {
            for (int i = 0; i < projectJsonArray.size(); i++) {
                projectStrList.add((String) projectJsonArray.get(i));
            }
            for (String projectStr : projectStrList) {
                switch (projectStr) {
                    case "CE101":
                        pageNumberList.add(14);
                        pageNumberList.add(35);
                        break;
                    case "CE102":
                        pageNumberList.add(15);
                        pageNumberList.add(36);
                        break;
                    case "CE106":
                        pageNumberList.add(16);
                        pageNumberList.add(37);
                        break;
                    case "CE107":
                        pageNumberList.add(17);
                        pageNumberList.add(38);
                        break;
                    case "CS101":
                        pageNumberList.add(18);
                        pageNumberList.add(39);
                        break;
                    case "CS102":
                        pageNumberList.add(19);
                        pageNumberList.add(40);
                        break;
                    case "CS103":
                        pageNumberList.add(20);
                        pageNumberList.add(41);
                        break;
                    case "CS104":
                        pageNumberList.add(21);
                        pageNumberList.add(42);
                        break;
                    case "CS105":
                        pageNumberList.add(22);
                        pageNumberList.add(43);
                        break;
                    case "CS106":
                        pageNumberList.add(23);
                        pageNumberList.add(44);
                        break;
                    case "CS109":
                        pageNumberList.add(24);
                        pageNumberList.add(45);
                        break;
                    case "CS112":
                        pageNumberList.add(25);
                        pageNumberList.add(46);
                        break;
                    case "CS114":
                        pageNumberList.add(26);
                        pageNumberList.add(47);
                        break;
                    case "CS115":
                        pageNumberList.add(27);
                        pageNumberList.add(48);
                        break;
                    case "CS116":
                        pageNumberList.add(28);
                        pageNumberList.add(49);
                        break;
                    case "RE101":
                        pageNumberList.add(29);
                        pageNumberList.add(50);
                        break;
                    case "RE102":
                        pageNumberList.add(30);
                        pageNumberList.add(51);
                        break;
                    case "RE103":
                        pageNumberList.add(31);
                        pageNumberList.add(52);
                        break;
                    case "RS101":
                        pageNumberList.add(32);
                        pageNumberList.add(53);
                        break;
                    case "RS103":
                        pageNumberList.add(33);
                        pageNumberList.add(54);
                        break;
                    case "RS105":
                        pageNumberList.add(34);
                        pageNumberList.add(55);
                        break;
                }
            }
        }
        Collections.sort(pageNumberList);
        nextPageRouter.clear();
        if (projectJsonArray.contains("1")) {
            nextPageRouter.put(8, 10);
        } else {
            nextPageRouter.put(9, 1001);
            nextPageRouter.put(1001, 1002);
            nextPageRouter.put(1002, 1003);
            nextPageRouter.put(1003, 1004);
            nextPageRouter.put(1004, 1005);
            nextPageRouter.put(1005, 1006);
            nextPageRouter.put(1006, 10);
        }
        nextPageRouter.put(13, pageNumberList.get(0));
        for (int i = 0; i < pageNumberList.size() - 1; i++) {
            nextPageRouter.put(pageNumberList.get(i), pageNumberList.get(i + 1));
        }
        nextPageRouter.put(pageNumberList.get(pageNumberList.size() - 1), 56);
        return next(sourcePageNumber);

    }

    public Integer previousPageNumber(String projectList, int sourcePageNumber) {
//        ManageSysOutline devObject = manageSysOutlineMapper.selectByPrimaryKey(outlineId);
//        String devItemId = manageSysOutlineMapper.selectCol(outlineId, "outline_dev_itemid");
//        String devItemId = devObject.getOutlineDevItemid();
//        String projectList = manageSysDevelopMapper.selectColByDevItemId("project_list", devItemId);
        JSONArray projectJsonArray = (JSONArray) JSON.parse(projectList);
        List<String> projectStrList = new ArrayList<>();
        List<Integer> pageNumberList = new ArrayList<>();
        if (projectJsonArray != null) {
            for (int i = 0; i < projectJsonArray.size(); i++) {
                projectStrList.add((String) projectJsonArray.get(i));
            }
            for (String projectStr : projectStrList) {
                switch (projectStr) {
                    case "CE101":
                        pageNumberList.add(14);
                        pageNumberList.add(35);
                        break;
                    case "CE102":
                        pageNumberList.add(15);
                        pageNumberList.add(36);
                        break;
                    case "CE106":
                        pageNumberList.add(16);
                        pageNumberList.add(37);
                        break;
                    case "CE107":
                        pageNumberList.add(17);
                        pageNumberList.add(38);
                        break;
                    case "CS101":
                        pageNumberList.add(18);
                        pageNumberList.add(39);
                        break;
                    case "CS102":
                        pageNumberList.add(19);
                        pageNumberList.add(40);
                        break;
                    case "CS103":
                        pageNumberList.add(20);
                        pageNumberList.add(41);
                        break;
                    case "CS104":
                        pageNumberList.add(21);
                        pageNumberList.add(42);
                        break;
                    case "CS105":
                        pageNumberList.add(22);
                        pageNumberList.add(43);
                        break;
                    case "CS106":
                        pageNumberList.add(23);
                        pageNumberList.add(44);
                        break;
                    case "CS109":
                        pageNumberList.add(24);
                        pageNumberList.add(45);
                        break;
                    case "CS112":
                        pageNumberList.add(25);
                        pageNumberList.add(46);
                        break;
                    case "CS114":
                        pageNumberList.add(26);
                        pageNumberList.add(47);
                        break;
                    case "CS115":
                        pageNumberList.add(27);
                        pageNumberList.add(48);
                        break;
                    case "CS116":
                        pageNumberList.add(28);
                        pageNumberList.add(49);
                        break;
                    case "RE101":
                        pageNumberList.add(29);
                        pageNumberList.add(50);
                        break;
                    case "RE102":
                        pageNumberList.add(30);
                        pageNumberList.add(51);
                        break;
                    case "RE103":
                        pageNumberList.add(31);
                        pageNumberList.add(52);
                        break;
                    case "RS101":
                        pageNumberList.add(32);
                        pageNumberList.add(53);
                        break;
                    case "RS103":
                        pageNumberList.add(33);
                        pageNumberList.add(54);
                        break;
                    case "RS105":
                        pageNumberList.add(34);
                        pageNumberList.add(55);
                        break;
                }
            }
        }
        Collections.sort(pageNumberList);
        previousPageRouter.clear();
        previousPageRouter.put(56, pageNumberList.get(pageNumberList.size() - 1));
        for (int i = pageNumberList.size() - 1; i > 0; i--) {
            previousPageRouter.put(pageNumberList.get(i), pageNumberList.get(i - 1));
        }
        previousPageRouter.put(pageNumberList.get(0), 13);
        if (projectJsonArray.contains("1")) {
            previousPageRouter.put(10, 8);
        } else {
            previousPageRouter.put(10, 1006);
            previousPageRouter.put(1006, 1005);
            previousPageRouter.put(1005, 1004);
            previousPageRouter.put(1004, 1003);
            previousPageRouter.put(1003, 1002);
            previousPageRouter.put(1002, 1001);
            previousPageRouter.put(1001, 9);
        }
        return previous(sourcePageNumber);
    }


}
