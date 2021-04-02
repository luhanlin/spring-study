package com.luhanlin.transfer.factory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean 工厂类
 *  使用容器化形式，实例化相关类
        任务一：读取解析xml，通过反射技术实例化对象并且存储待用（map集合）
 *      任务二：对外提供获取实例对象的接口（根据id获取）
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class BeanFactory {

    // 由于系统的 bean 只会初始化一次，不会有并发性的问题， 使用HashMap
    private static Map<String, Object> beanMap = new HashMap<>();

    static {
        System.out.println("开始进行 xml 的解析...");
        // 1. 加载 xml 文件
        InputStream in = BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");

        // Dom4j 解析xml
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(in);
            Element rootElement = document.getRootElement();
            List<Element> beans = rootElement.selectNodes("//bean");
            // 将实体放入到容器中
            // <bean id="accountDao" class="com.luhanlin.transfer.dao.impl.JdbcTemplateDaoImpl">
            for (Element bean : beans) {
                String id = bean.attributeValue("id");
                String beanClass = bean.attributeValue("class");
                if (beanMap.containsKey(id)) continue;

                Class<?> aClass = Class.forName(beanClass);
                Object beanObj = aClass.newInstance();
                beanMap.put(id, beanObj);
            }

            // 依赖项注入到对应的实体中
            // <property name="ConnectionUtils" ref="connectionUtils"/>
            List<Element> propertyList = rootElement.selectNodes("//property[@ref]");
            for (Element property : propertyList) {
                String name = property.attributeValue("name");
                String ref = property.attributeValue("ref");
                Element parent = property.getParent();
                String parentId = parent.attributeValue("id");
                // 默认 name 首字母是大写，使用 set + name 的方式进行反射注入
                if (name != null && ref != null && parentId != null) {
                    Object parentObj = beanMap.get(parentId);
                    Method[] methods = parentObj.getClass().getMethods();
                    for (Method method : methods) {
                        if (method.getName().equalsIgnoreCase("set" + name)){
                            method.invoke(parentObj, beanMap.get(ref));
                            // 修改参数值之后，需要更新 容器中的实例化对象
                            beanMap.put(parentId, parentObj);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 任务二：对外提供获取实例对象的接口（根据id获取）
    public static  Object getBean(String id) {
        return beanMap.get(id);
    }

}
