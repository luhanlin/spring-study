package com.luhanlin.concurrent.thread.responsibility;

/**
 * <类详细描述> 责任链处理器
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-24 16:48]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface IRequestProcessor {

    void process(IRequest request);
}
