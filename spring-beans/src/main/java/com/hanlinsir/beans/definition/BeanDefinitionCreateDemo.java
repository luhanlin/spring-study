package com.hanlinsir.beans.definition;

import com.luhanlin.ioc.domain.User;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/**
 * {@link org.springframework.beans.factory.config.BeanDefinition} 创建示例
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 * @see BeanDefinitionBuilder
 * @see AbstractBeanDefinition
 */
public class BeanDefinitionCreateDemo {

    public static void main(String[] args) {
        // 1. 通过 BeanDefinitionBuilder 构建， 实际内部采用的  GenericBeanDefinition 进行的构建
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
        builder.addPropertyValue("name","lin").addPropertyValue("age",1);
        BeanDefinition beanDefinition = builder.getBeanDefinition();
        // BeanDefinition 并非 Bean 终态，可以自定义修改
        System.out.println(beanDefinition.toString());

        // 2. 通过 GenericBeanDefinition 进行构建
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        genericBeanDefinition.setBeanClass(User.class);

        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.add("name","lin").add("age",1);

        genericBeanDefinition.setPropertyValues(propertyValues);
    }
}
