package com.enation.itbuilder.image2code.api;

import java.util.HashMap;
import java.util.Map;

/**
 * 工作流上下文
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
public class WorkFlowContext {
    private static final ThreadLocal<Map<ActionType,Object>> actionResult = ThreadLocal.withInitial(() -> new HashMap<>());

    public static void setResult(ActionType type,Object result) {
        actionResult.get().put(type,result);
    }

    public static Object getResult(ActionType type) {
        return actionResult.get().get(type);
    }
}
