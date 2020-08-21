package com.luhanlin.spring.framework.core;

import com.luhanlin.spring.framework.annotation.*;
import org.springframework.util.ClassUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 类详细描述：自定义实现任务分发
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2020/8/8 2:20 下午
 */
//@WebServlet(urlPatterns = "/lu", loadOnStartup = 3)
public class LUDispatcherServlet extends HttpServlet {

    // 保存配置文件的内容
    private Properties contextConfig = new Properties();

    // 保存全限定类名
    private List<String> classNames = new ArrayList<>();

    // IOC容器，不考虑并发
    private Map<String, Object> ioc = new HashMap<>();

    private List<LUHandlerMapping> handlerMappings = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 进行调用
        try {
            doDispatcher(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("500 Exection,Detail : " + Arrays.toString(e.getStackTrace()));
        }


    }

    private void doDispatcher(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // 通过request 获取url对应的handler
        LUHandlerMapping handler = getHandler(req);

        // 反射调用对应的方法处理
        //获得方法的形参列表
        Class<?>[] paramTypes = handler.getParamType();

        Object[] paramValues = new Object[paramTypes.length];

        Map<String, String[]> params = req.getParameterMap();
        for (Map.Entry<String, String[]> parm : params.entrySet()) {
            String value = Arrays.toString(parm.getValue()).replaceAll("\\[|\\]", "")
                    .replaceAll("\\s", ",");

            if (!handler.paramIndexMap.containsKey(parm.getKey())) {
                continue;
            }

            int index = handler.paramIndexMap.get(parm.getKey());
            paramValues[index] = convert(paramTypes[index], value);
        }

//        if(handler.paramIndexMap.containsKey(HttpServletRequest.class.getName())) {
//            int reqIndex = handler.paramIndexMap.get(HttpServletRequest.class.getName());
//            paramValues[reqIndex] = req;
//        }
//
//        if(handler.paramIndexMap.containsKey(HttpServletResponse.class.getName())) {
//            int respIndex = handler.paramIndexMap.get(HttpServletResponse.class.getName());
//            paramValues[respIndex] = resp;
//        }

        Object returnValue = handler.method.invoke(handler.controller, paramValues);
        if (returnValue == null || returnValue instanceof Void) {
            return;
        }
        resp.getWriter().write(returnValue.toString());
    }

    private LUHandlerMapping getHandler(HttpServletRequest req) {
        if (this.handlerMappings.isEmpty()) return null;
        String url = req.getRequestURI();

        url = url.replace(req.getContextPath(), "").replaceAll("/+", "/");

        // 在处理集合中寻找匹配的url
        for (LUHandlerMapping handler : this.handlerMappings) {
            if (handler.getUrl().matcher(url).matches()) {
                return handler;
            }
        }
        return null;
    }

    //url传过来的参数都是String类型的，HTTP是基于字符串协议
    //只需要把String转换为任意类型就好
    private Object convert(Class<?> type, String value) {
        //如果是int
        if (Integer.class == type) {
            return Integer.valueOf(value);
        } else if (Double.class == type) {
            return Double.valueOf(value);
        }
        //如果还有double或者其他类型，继续加if
        //这时候，我们应该想到策略模式了
        //在这里暂时不实现，希望小伙伴自己来实现
        return value;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("开始初始化servlet");
        // 1. 加载配置文件资源
        readResource();

        // 2. 扫描配置文件资源相关类
        scanPackageClass(String.valueOf(contextConfig.get("base-package")));

        // 3. 进行配置类初始化，并装载bean到IOC容器
        loadBeanToIoc();

        // 4. 完成依赖注入
        doAutowired();

        // 5. 进行 handlerMapping 的关联
        initHandlerMapping();

    }

    private void readResource() {
        //直接从类路径下找到Spring主配置文件所在的路径
        //并且将其读取出来放到Properties对象中
        //相对于scanPackage=com.gupaoedu.demo 从文件中保存到了内存中
        InputStream fis = this.getClass().getClassLoader().getResourceAsStream("application.properties");
        try {
            contextConfig.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void scanPackageClass(String basePackage) {

        // 将路径的中的点置换成'/' 此处坑在spring-boot 在使用classloader.getResource() 路径前不加'/'
        URL url = ClassUtils.getDefaultClassLoader().getResource(basePackage.replaceAll("\\.", "/"));

        File filePath = new File(url.getFile());
        File[] files = filePath.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                scanPackageClass(basePackage + "." + file.getName());
            } else {
                String fileName = file.getName();
                if (!fileName.endsWith(".class")) continue;

                fileName = basePackage + "." + fileName.replace(".class", "");
                System.out.println("读取class文件成功：" + fileName);

                classNames.add(fileName);
            }
        }
    }

    private void loadBeanToIoc() {
        // 遍历获取的类文件
        if (classNames.isEmpty()) return;

        try {
            for (String className : classNames) {
                System.out.println("开始处理class，name=" + className);
                Class<?> clazz = Class.forName(className);
                String clazzName = clazz.getName();
                if (clazz.isAnnotationPresent(LUController.class)) {
                    Object instance = clazz.newInstance();
                    clazzName = toLowerFirstCase(className);
                    ioc.put(clazzName, instance);
                } else if (clazz.isAnnotationPresent(LUService.class)) {
                    LUService service = clazz.getAnnotation(LUService.class);
                    clazzName = service.value();
                    if ("".equals(clazzName)) {
                        clazzName = toLowerFirstCase(className);
                    }
                    Object instance = clazz.newInstance();
                    ioc.put(clazzName, instance);

                    // 将器接口层也放入ioc容器管理
                    //3、根据类型自动赋值,投机取巧的方式
                    for (Class<?> i : clazz.getInterfaces()) {
                        if (ioc.containsKey(i.getName())) {
                            throw new Exception("The “" + i.getName() + "” is exists!!");
                        }
                        //把接口的类型直接当成key了
                        ioc.put(i.getName(), instance);
                    }
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void doAutowired() {
        if (ioc.isEmpty()) return;
        ;
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            //Declared 所有的，特定的 字段，包括private/protected/default
            //正常来说，普通的OOP编程只能拿到public的属性
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(LUAutowired.class)) {
                    continue;
                }
                LUAutowired autowired = field.getAnnotation(LUAutowired.class);

                //如果用户没有自定义beanName，默认就根据类型注入
                //这个地方省去了对类名首字母小写的情况的判断，这个作为课后作业
                //小伙伴们自己去完善
                String beanName = autowired.value().trim();
                if ("".equals(beanName)) {
                    //获得接口的类型，作为key待会拿这个key到ioc容器中去取值
                    beanName = field.getType().getName();
                }

                //如果是public以外的修饰符，只要加了@Autowired注解，都要强制赋值
                //反射中叫做暴力访问， 强吻
                field.setAccessible(true);

                try {
                    //用反射机制，动态给字段赋值
                    field.set(entry.getValue(), ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initHandlerMapping() {
        if (ioc.isEmpty()) return;
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();
            if (!clazz.isAnnotationPresent(LUController.class)) continue;

            String baseUrl = "";
            if (clazz.isAnnotationPresent(LURequestMapping.class)) {
                LURequestMapping mapping = clazz.getAnnotation(LURequestMapping.class);
                baseUrl = "/" + mapping.value();
            }

            for (Method method : clazz.getMethods()) {
                if (!method.isAnnotationPresent(LURequestMapping.class)) continue;

                LURequestMapping fieldRm = method.getAnnotation(LURequestMapping.class);

                baseUrl = (baseUrl + "/" + fieldRm.value()).replaceAll("/+", "/");
                Pattern urlPattern = Pattern.compile(baseUrl);

                this.handlerMappings.add(new LUHandlerMapping(urlPattern, method, entry.getValue()));

                System.out.println("请求 [" + baseUrl + "] 映射成功！");
            }

        }
    }

    private String toLowerFirstCase(String name) {
        char[] chars = name.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private class LUHandlerMapping {

        private Pattern urlPattern;  //正则

        private Method method;

        // 方法所在的controller类
        private Object controller;

        private Class<?>[] paramType;

        public Map<String, Integer> paramIndexMap;

        public LUHandlerMapping(Pattern urlPattern, Method method, Object controller) {
            this.urlPattern = urlPattern;
            this.method = method;
            this.controller = controller;
            this.paramType = method.getParameterTypes();
            this.paramIndexMap = new HashMap<>();

            // 将方法参数放入paramIndexMap
            putParamIndexMap(method);
        }

        private void putParamIndexMap(Method method) {
            // 先装载包含注解的部分
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (int i = 0; i < parameterAnnotations.length; i++) {
                for (Annotation annotation : parameterAnnotations[i]) {
                    if (annotation instanceof LURequestParam) {
                        String paramName = ((LURequestParam) annotation).value();
                        if (!"".equals(paramName.trim())) {
                            paramIndexMap.put(paramName, i);
                        }
                    }
                }
            }
            // 再装载不包含注解的参数
            for (int i = 0; i < paramType.length; i++) {
                Class<?> parameterType = paramType[i];
                if (parameterType == HttpServletRequest.class || parameterType == HttpServletResponse.class) {
                    paramIndexMap.put(parameterType.getName(), i);
                }
            }
        }

        public Pattern getUrl() {
            return urlPattern;
        }

        public Method getMethod() {
            return method;
        }

        public Object getController() {
            return controller;
        }

        public Class<?>[] getParamType() {
            return paramType;
        }
    }
}
