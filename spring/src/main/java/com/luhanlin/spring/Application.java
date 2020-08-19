package com.luhanlin.spring;

import com.luhanlin.spring.framework.v2.webmvc.LUDispatcherServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@ServletComponentScan
public class Application {

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new LUDispatcherServlet(), "/*");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
