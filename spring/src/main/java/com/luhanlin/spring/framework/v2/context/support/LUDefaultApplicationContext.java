package com.luhanlin.spring.framework.v2.context.support;

import com.luhanlin.spring.framework.annotation.LUAutowired;
import com.luhanlin.spring.framework.annotation.LUController;
import com.luhanlin.spring.framework.annotation.LUService;
import com.luhanlin.spring.framework.v2.beans.LUBeanFactory;
import com.luhanlin.spring.framework.v2.beans.LUBeanWrapper;
import com.luhanlin.spring.framework.v2.beans.config.LUBeanDefinition;
import com.luhanlin.spring.framework.v2.beans.support.LUBeanDefinitionReader;
import com.luhanlin.spring.framework.v2.beans.support.LUDefaultListableBeanFactory;
import com.luhanlin.spring.framework.v2.beans.utils.BeanNameUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-18 12:16]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LUDefaultApplicationContext extends LUDefaultListableBeanFactory implements LUBeanFactory {

    private String[] configLocations;

    private LUBeanDefinitionReader reader;

    public LUDefaultApplicationContext(String[] configLocations) {
        this.configLocations = configLocations;
        refresh();
    }

    @Override
    protected void refresh() {
        // 1. 定位资源
        reader = new LUBeanDefinitionReader(this.configLocations);

        // 2. 加载配置资源，将资源封装为 BeanDefinition
        List<LUBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

        // 3. 注册 IOC 容器
        doRegisterBeanDefinition(beanDefinitions);

        // 4. 判断延时加载，如果不是延时加载，直接初始化 bean
        doAutowired();
    }

    @Override
    public Object getBean(String beanName) {
        if (this.factoryBeanInstanceCache.containsKey(beanName)) return factoryBeanInstanceCache.get(beanName).getWrappedInstance();

        LUBeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        /**
         * 先初始化在进行注入，可以解决循环依赖的问题
         */
        // 初始化bean
        LUBeanWrapper beanWrapper = createBeanInstance(beanName, beanDefinition);

        // 依赖注入
        populateBean(beanName, beanDefinition, beanWrapper);

        return beanWrapper.getWrappedInstance();
    }

    @Override
    public Object getBean(Class<?> className) {
        return getBean(BeanNameUtil.toLowerFirstCase(className.getSimpleName()));
    }

    private void populateBean(String factoryBeanName, LUBeanDefinition beanDefinition, LUBeanWrapper beanWrapper) {
        Class<?> wrappedClass = beanWrapper.getWrappedClass();
        Object wrappedInstance = beanWrapper.getWrappedInstance();
        if(!(wrappedClass.isAnnotationPresent(LUController.class) || wrappedClass.isAnnotationPresent(LUService.class))){
            return;
        }
        //Declared 所有的，特定的 字段，包括private/protected/default
        //正常来说，普通的OOP编程只能拿到public的属性
        Field[] fields = wrappedClass.getDeclaredFields();
        for (Field field : fields) {
            if(!field.isAnnotationPresent(LUAutowired.class)){continue;}
            LUAutowired autowired = field.getAnnotation(LUAutowired.class);

            //如果用户没有自定义beanName，默认就根据类型注入
            //这个地方省去了对类名首字母小写的情况的判断，这个作为课后作业
            //小伙伴们自己去完善
            String beanName = autowired.value().trim();
            if("".equals(beanName)){
                //获得接口的类型，作为key待会拿这个key到ioc容器中去取值
                beanName = BeanNameUtil.toLowerFirstCase(field.getType().getSimpleName());
            }

            //如果是public以外的修饰符，只要加了@Autowired注解，都要强制赋值
            field.setAccessible(true);

            try {
                //用反射机制，动态给字段赋值
                //为什么会为NULL，先留个坑顺序加载，可能出现依赖的没有加载的情况，可以记录后续在加载
//                if(this.factoryBeanInstanceCache.get(beanName) == null){ continue; }
                System.out.println("依赖注入的 BeanName = " + beanName);
                System.out.println("依赖注入的 BeanWrapper = " + this.singletonBeanMap.get(beanName));
                field.set(wrappedInstance,this.singletonBeanMap.get(beanName));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (!factoryBeanInstanceCache.containsKey(factoryBeanName)) {
            this.factoryBeanInstanceCache.put(factoryBeanName, beanWrapper);
        }
    }

    /**
     * 封装BeanWrapper
     * @param beanName 存储在容器中的beanName
     * @param beanDefinition
     * @return
     */
    private LUBeanWrapper createBeanInstance(String beanName, LUBeanDefinition beanDefinition) {
        LUBeanWrapper wr = new LUBeanWrapper();

        String beanClassName = beanDefinition.getBeanClassName();
        Object instance = null;
        try {
            Class<?> clazz = Class.forName(beanClassName);
            wr.setWrappedClass(clazz);
            String factoryBeanName = BeanNameUtil.toLowerFirstCase(clazz.getSimpleName());
            if (singletonBeanMap.containsKey(factoryBeanName)) {
                instance = singletonBeanMap.get(factoryBeanName);
            } else {
                instance = clazz.newInstance();
                singletonBeanMap.put(factoryBeanName, instance);
            }
            wr.setWrappedInstance(instance);
//            if (!factoryBeanInstanceCache.containsKey(beanName)) {
//                this.factoryBeanInstanceCache.put(beanName, wr);
//            }

            // 将器接口层也放入ioc容器管理
            //3、根据类型自动赋值,投机取巧的方式
            for (Class<?> i : clazz.getInterfaces()) {
                String instanceName = BeanNameUtil.toLowerFirstCase(i.getSimpleName());
                if(singletonBeanMap.containsKey(instanceName)){
//                    throw new Exception("The “" + instanceName + "” is exists!!");
                    continue;
                }
                //把接口的类型直接当成key了
                singletonBeanMap.put(instanceName,instance);
//                if (!factoryBeanInstanceCache.containsKey(instanceName)) {
//                    this.factoryBeanInstanceCache.put(instanceName, wr);
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wr;
    }

    /**
     * 进行类的依赖注入
     */
    private void doAutowired() {
        for (Map.Entry<String, LUBeanDefinition> beanDefinitionEntry : beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if (!beanDefinitionEntry.getValue().isLazyInit()) {
                getBean(beanName);
            }
        }
    }

    private void doRegisterBeanDefinition(List<LUBeanDefinition> beanDefinitions) {
        beanDefinitions.forEach(bd -> {
            beanDefinitionMap.put(bd.getFactoryBeanName(), bd);
            createBeanInstance(bd.getFactoryBeanName(), bd);
        });
    }

    public void printBeanWrapperMap(){
        this.factoryBeanInstanceCache.keySet().forEach(System.out::println);
    }

    public void printBeanDefinitionMap(){
        this.beanDefinitionMap.values().forEach(System.out::println);
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount(){
        return this.beanDefinitionMap.size();
    }

    public Properties getConfig(){
        return this.reader.getConfig();
    }
}
