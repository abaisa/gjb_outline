package cn.gjb151b.outline.service;

import java.util.HashMap;

/**
 * 页面控制代码，决定页面上下页逻辑
 * 这个单子类写的有问题，有点搞事，可以改下，弄成配置文件加载什么的都行，初始化好丑啊
 */
public class PageDispatcher {

    private static PageDispatcher instance = null;

    private HashMap<Integer, Integer> nextPageRouter = new HashMap<>();
    private HashMap<Integer, Integer> previousPageRouter = new HashMap<>();

    private PageDispatcher() {
        // 特殊下一页
        nextPageRouter.put(9, 1001);
        nextPageRouter.put(1001, 10);
        nextPageRouter.put(13, 3);

        // 特殊上一页
        previousPageRouter.put(10, 1001);
        previousPageRouter.put(1001, 9);
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
}
