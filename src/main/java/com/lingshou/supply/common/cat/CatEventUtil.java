package com.lingshou.supply.common.cat;

import com.dianping.cat.Cat;
import org.apache.log4j.Logger;

public class CatEventUtil {

    private static Logger ERROR = Logger.getLogger(CatEventUtil.class);

    /**
     * cat打点记录(不记录时传null)
     *
     * @param module     模块
     * @param action     行为
     * @param metricName 可显示趋势图
     * @param quantity   数量
     */
    public static void doLogEvent(String module, String action, String metricName, Integer quantity) {
        if (module != null && action != null) {
            try {
                Cat.logEvent(module, action);
            } catch (Exception e) {
                ERROR.error("Cat.logEvent error", e);
            }
        }

        if (metricName != null) {
            try {
                if (quantity != null) {
                    Cat.logMetricForCount(metricName, quantity);
                } else {
                    Cat.logMetricForCount(metricName);
                }
            } catch (Exception e) {
                ERROR.error("Cat.logMetricForCount error", e);
            }
        }
    }

    /**
     * cat打点记录
     *
     * @param module     模块
     * @param action     行为
     * @param metricName 可显示趋势图
     */
    public static void doLogEvent(String module, String action, String metricName) {
        doLogEvent(module, action, metricName, null);
    }
}
