package com.luhanlin.spring.framework.v2.webmvc.servlet;

import org.springframework.util.ClassUtils;

import java.io.File;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-19 14:42]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LUViewResolver {
    private final String DEFAULT_TEMPLATE_SUFFIX = ".html";

    private File templateRootDir;

    public LUViewResolver(String templateRoot) {
        String templateRootPath = ClassUtils.getDefaultClassLoader().getResource(templateRoot.replaceAll("\\.", "/")).getFile();
        this.templateRootDir = new File(templateRootPath);
    }

    public LUView resolveViewName(String viewName, Object o) {
        if(null == viewName || "".equals(viewName.trim())){return null;}
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName : (viewName + DEFAULT_TEMPLATE_SUFFIX);
        File templateFile = new File((templateRootDir.getPath() + "/" + viewName).replaceAll("/+","/"));
        return new LUView(templateFile);
    }
}
