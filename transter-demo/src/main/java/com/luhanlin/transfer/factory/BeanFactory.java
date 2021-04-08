package com.luhanlin.transfer.factory;

import com.alibaba.druid.util.StringUtils;
import com.luhanlin.transfer.annotation.*;
import com.luhanlin.transfer.utils.StrUtils;
import javafx.scene.control.Separator;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Struct;
import java.util.ArrayList;
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

    private static List<String> classList = new ArrayList<>();
    private static List<String> fieldAlreadyProcessedList = new ArrayList<>();

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
            /**
             * 采用自定义注解进行 bean 注入的方式
             */
            Element component = (Element) rootElement.selectSingleNode("//component-scan");
            String basePackage = component.attributeValue("base-package");

            // 扫描 basePackage
            doScan(basePackage);
            System.out.println("扫描到的资源为：" + classList);

            // 生成实例
            doInstance();

            // 依赖注入
            doAutowired();
            System.out.println("生成的实例为：" + beanMap);

            // 事务维护
            doTransactional();

            /**
             * 以下为直接读取 bean 方式
             */
//            List<Element> beans = rootElement.selectNodes("//bean");
//            // 将实体放入到容器中
//            // <bean id="accountDao" class="com.luhanlin.transfer.dao.impl.JdbcTemplateDaoImpl">
//            for (Element bean : beans) {
//                String id = bean.attributeValue("id");
//                String beanClass = bean.attributeValue("class");
//                if (beanMap.containsKey(id)) continue;
//
//                Class<?> aClass = Class.forName(beanClass);
//                Object beanObj = aClass.newInstance();
//                beanMap.put(id, beanObj);
//            }
//
//            // 依赖项注入到对应的实体中
//            // <property name="ConnectionUtils" ref="connectionUtils"/>
//            List<Element> propertyList = rootElement.selectNodes("//property[@ref]");
//            for (Element property : propertyList) {
//                String name = property.attributeValue("name");
//                String ref = property.attributeValue("ref");
//                Element parent = property.getParent();
//                String parentId = parent.attributeValue("id");
//                // 默认 name 首字母是大写，使用 set + name 的方式进行反射注入
//                if (name != null && ref != null && parentId != null) {
//                    Object parentObj = beanMap.get(parentId);
//                    Method[] methods = parentObj.getClass().getMethods();
//                    for (Method method : methods) {
//                        if (method.getName().equalsIgnoreCase("set" + name)){
//                            method.invoke(parentObj, beanMap.get(ref));
//                            // 修改参数值之后，需要更新 容器中的实例化对象
//                            beanMap.put(parentId, parentObj);
//                            break;
//                        }
//                    }
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 任务二：对外提供获取实例对象的接口（根据id获取）
    public static  Object getBean(String id) {
        return beanMap.get(id);
    }

    /**
     * 扫描包下需要被容器管理的类
     * @param basePackage
     */
    private static void doScan(String basePackage) {
        String scanPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + basePackage.replaceAll("\\.", "/");
        System.out.println("读取资源路径为：" + scanPath);

        File packagePath = new File(scanPath);
        File[] files = packagePath.listFiles();
        for (File file : files) {
            String className = basePackage + "." + file.getName();
            if (file.isDirectory()) {
                doScan(className);
            } else if (file.getName().endsWith(".class")) {
                classList.add(className.replace(".class", ""));
            }
        }
    }

    private static void doInstance() {
        if (classList.isEmpty()) return;

        try {
            for (String className :classList){
                Class<?> aClass = Class.forName(className);
                if (aClass.isAnnotationPresent(LuComponent.class)
                        || aClass.isAnnotationPresent(LuService.class)
                        || aClass.isAnnotationPresent(LuRepository.class)) {
                    Object bean = aClass.newInstance();

                    // 开始实例化并放入到容器中
                    String beanName = null;

                    if (aClass.isAnnotationPresent(LuComponent.class)) {
                        beanName = aClass.getAnnotation(LuComponent.class).value();
                    } else if (aClass.isAnnotationPresent(LuService.class)) {
                        beanName = aClass.getAnnotation(LuService.class).value();
                    } else if (aClass.isAnnotationPresent(LuRepository.class)) {
                        beanName = aClass.getAnnotation(LuRepository.class).value();
                    }

                    if (StringUtils.isEmpty(beanName.trim())) {
                        beanName = StrUtils.lowerFirst(aClass.getSimpleName());
                    }
                    beanMap.put(beanName, bean);

                    // 接口类也需要和实例对象进行映射
                    Class<?>[] interfaces = aClass.getInterfaces();
                    for (Class<?> anInterface : interfaces) {
                        // 此处不考虑一个接口有多个实现类的问题
                        beanMap.put(anInterface.getName(), bean);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void doAutowired() throws IllegalAccessException {
        if (beanMap.isEmpty()) return;

        for (Object bean : beanMap.values()) {
            doObjectDependency(bean);
        }
    }

    private static void doObjectDependency(Object bean) throws IllegalAccessException {
        Field[] beanFields = bean.getClass().getDeclaredFields();

        if (beanFields == null || beanFields.length == 0) return;

        for (Field beanField : beanFields) {
            if (!beanField.isAnnotationPresent(LuAutowired.class)) continue;

            String beanFieldName = beanField.getType().getName();
            String fieldProcessedName = bean.getClass().getName() + "." + beanField.getName();
            System.out.println("进行属性的处理：" + fieldProcessedName);
            if (fieldAlreadyProcessedList.contains(fieldProcessedName)) continue;

            // 根据接口找不到就根据类名首字母小写来找
            Object injectBean = beanMap.getOrDefault(beanFieldName, beanMap.get(StrUtils.lowerFirst(beanField.getType().getSimpleName())));
            if (injectBean == null) {
                throw new RuntimeException(bean.getClass().getSimpleName() + "属性" + beanField.getName() + "注入异常");
            }

            // 记录被处理过的属性
            fieldAlreadyProcessedList.add(fieldProcessedName);

            doObjectDependency(injectBean);

            beanField.setAccessible(true);
            beanField.set(bean, injectBean);
        }

    }

    /**
     * 给加了事务控制的类创建代理
     */
    private static void doTransactional() {
        ProxyFactory proxyFactory = (ProxyFactory) beanMap.get("proxyFactory");

        beanMap.forEach((className, instance) -> {
            if (instance.getClass().isAnnotationPresent(LuTransactional.class)) {
                // 看此类有无接口
                Class<?>[] interfaces = instance.getClass().getInterfaces();
                if (interfaces.length > 0) {
                    beanMap.put(className, proxyFactory.getJdkProxy(instance));
                } else {
                    beanMap.put(className, proxyFactory.getCglibProxy(instance));
                }
                System.out.println(className + " 已做代理");
            }
        });
    }

}
