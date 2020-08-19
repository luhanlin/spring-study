package com.luhanlin.spring.framework.v2.beans.utils;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-19 10:17]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class BeanNameUtil {

    public static String toLowerFirstCase(String name) {
        char[] chars = name.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
