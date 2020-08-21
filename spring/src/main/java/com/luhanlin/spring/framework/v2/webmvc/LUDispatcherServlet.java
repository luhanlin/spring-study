package com.luhanlin.spring.framework.v2.webmvc;

import com.luhanlin.spring.framework.annotation.LUController;
import com.luhanlin.spring.framework.annotation.LURequestMapping;
import com.luhanlin.spring.framework.v2.context.support.LUDefaultApplicationContext;
import com.luhanlin.spring.framework.v2.webmvc.servlet.*;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
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

    private List<LUHandlerMapping> handlerMappings = new ArrayList<>();

    private Map<LUHandlerMapping, LUHandlerAdapter> handlerAdapters = new HashMap<>();

    private List<LUViewResolver> viewResolvers = new ArrayList<LUViewResolver>();

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
        // 1. 通过request 获取url对应的handler
        LUHandlerMapping handler = getHandler(req);

        if (handler == null) {
            processDispatchResult(req, resp, new LUModelAndView("404"));
            return;
        }

        // 2. Determine handler adapter for the current request.
        LUHandlerAdapter ha = getHandlerAdapter(handler);

        // 3. 真正的调用方法,返回ModelAndView存储了要传页面上值，和页面模板的名称
        LUModelAndView mv = ha.handle(req, resp, handler);

        processDispatchResult(req, resp, mv);
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, LUModelAndView mv) throws Exception {
        //把ModelAndView变成一个HTML、json
        //ContextType
        if (null == mv) {
            return;
        }

        //如果ModelAndView不为null，怎么办？
        if (this.viewResolvers.isEmpty()) {
            return;
        }

        for (LUViewResolver viewResolver : this.viewResolvers) {
            LUView view = viewResolver.resolveViewName(mv.getViewName(), null);
            view.render(mv.getModel(), req, resp);
            return;
        }
    }

    private LUHandlerAdapter getHandlerAdapter(LUHandlerMapping handler) {
        if (this.handlerAdapters.isEmpty()) {
            return null;
        }
        LUHandlerAdapter ha = this.handlerAdapters.get(handler);
        if (ha.supports(handler)) {
            return ha;
        }
        return null;
    }

    private LUHandlerMapping getHandler(HttpServletRequest req) {
        if (this.handlerMappings.isEmpty()) return null;
        String url = req.getRequestURI();

        url = url.replace(req.getContextPath(), "").replaceAll("/+", "/");

        // 在处理集合中寻找匹配的url
        for (LUHandlerMapping handler : this.handlerMappings) {
            if (handler.getUrlPattern().matcher(url).matches()) {
                return handler;
            }
        }
        return null;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("开始初始化servlet");
        // 1. 初始化自定义spring容器
        LUDefaultApplicationContext context = new LUDefaultApplicationContext(new String[]{"application.properties"});
        // 2. 初始化 Spring MVC 9 大组件
        initStrategies(context);

    }

    /**
     * 初始化 Spring MVC 9 大组件
     *
     * @param context
     */
    protected void initStrategies(LUDefaultApplicationContext context) {
//        initMultipartResolver(context);
//        initLocaleResolver(context);
//        initThemeResolver(context);
        initHandlerMappings(context);
        initHandlerAdapters(context);
//        initHandlerExceptionResolvers(context);
//        initRequestToViewNameTranslator(context);
        initViewResolvers(context);
//        initFlashMapManager(context);
    }

    private void initViewResolvers(LUDefaultApplicationContext context) {
        //拿到模板的存放目录
        String templateRoot = context.getConfig().getProperty("templateRoot");

        String templateRootPath = ClassUtils.getDefaultClassLoader().getResource(templateRoot.replaceAll("\\.", "/")).getFile();

        File templateRootDir = new File(templateRootPath);
        String[] templates = templateRootDir.list();
        for (int i = 0; i < templates.length; i++) {
            //这里主要是为了兼容多模板，所有模仿Spring用List保存
            //在我写的代码中简化了，其实只有需要一个模板就可以搞定
            //只是为了仿真，所有还是搞了个List
            this.viewResolvers.add(new LUViewResolver(templateRoot));
        }
    }

    private void initHandlerAdapters(LUDefaultApplicationContext context) {
        if (this.handlerMappings.isEmpty()) return;

        for (LUHandlerMapping handlerMapping : handlerMappings) {
            handlerAdapters.put(handlerMapping, new LUHandlerAdapter());
        }
    }

    private void initHandlerMappings(LUDefaultApplicationContext context) {
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            Object bean = context.getBean(beanName);

            Class<?> clazz = bean.getClass();
            if (!clazz.isAnnotationPresent(LUController.class)) continue;

            String baseUrl = "";
            if (clazz.isAnnotationPresent(LURequestMapping.class)) {
                LURequestMapping mapping = clazz.getAnnotation(LURequestMapping.class);
                baseUrl = "/" + mapping.value();
            }

            for (Method method : clazz.getMethods()) {
                if (!method.isAnnotationPresent(LURequestMapping.class)) continue;

                LURequestMapping fieldRm = method.getAnnotation(LURequestMapping.class);

                String url = (baseUrl + "/" + fieldRm.value()).replaceAll("/+", "/");
                Pattern urlPattern = Pattern.compile(url);

                this.handlerMappings.add(new LUHandlerMapping(urlPattern, method, bean));

                System.out.println("请求 [" + url + "] 映射成功！");
            }

        }
    }
}
