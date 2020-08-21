package com.luhanlin.spring.framework.v2.beans.support;

import com.luhanlin.spring.framework.v2.beans.config.LUBeanDefinition;
import com.luhanlin.spring.framework.v2.beans.utils.BeanNameUtil;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * <类详细描述> 对 本地资源配置进行BeanDefinition 的封装
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-18 15:54]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LUBeanDefinitionReader {

    private final static String BASE_PACKAGE = "base-package";

    // 保存全限定类名
    private final List<String> classNames = new ArrayList<>();

    // 保存配置文件的内容
    private Properties contextConfig = new Properties();

    /**
     * 主方法，进行 beanDefinition 加载
     *
     * @return
     */
    public List<LUBeanDefinition> loadBeanDefinitions() {
        List<LUBeanDefinition> result = new ArrayList<>();

        classNames.forEach(className -> {
            LUBeanDefinition beanDefinition = doCreateBeanDefinition(className);
            if (beanDefinition != null) {
                result.add(beanDefinition);
            }
        });

        return result;
    }

    public LUBeanDefinitionReader(String... configLocations) {
        //直接从类路径下找到Spring主配置文件所在的路径
        //并且将其读取出来放到Properties对象中
        //相对于scanPackage=com.gupaoedu.demo 从文件中保存到了内存中
        InputStream fis = this.getClass().getClassLoader().getResourceAsStream(configLocations[0]);
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
        scanPackageClass(String.valueOf(contextConfig.get(BASE_PACKAGE)));
    }

    private LUBeanDefinition doCreateBeanDefinition(String className) {
        try {
            Class<?> clzz = Class.forName(className);

            if (!clzz.isInterface()) {
                LUBeanDefinition beanDefinition = new LUBeanDefinition();
                beanDefinition.setBeanClassName(className);
                beanDefinition.setFactoryBeanName(BeanNameUtil.toLowerFirstCase(clzz.getSimpleName()));
                return beanDefinition;
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 扫描基础包下的文件
     *
     * @param basePackage
     */
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

    public Properties getConfig() {
        return this.contextConfig;
    }

}
