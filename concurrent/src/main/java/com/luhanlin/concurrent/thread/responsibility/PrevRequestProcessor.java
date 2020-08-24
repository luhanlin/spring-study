package com.luhanlin.concurrent.thread.responsibility;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-24 16:50]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class PrevRequestProcessor extends Thread implements IRequestProcessor {

    // 存储数据的阻塞队列
    private static final LinkedBlockingDeque<IRequest> requestQueue = new LinkedBlockingDeque<>();

    private IRequestProcessor nextProcessor;

    private volatile boolean isFinished = false;

    public PrevRequestProcessor() {
    }

    public PrevRequestProcessor(IRequestProcessor processor) {
        this.nextProcessor = processor;
    }

    @Override
    public void run() {
        while (!isFinished()) {
            try {
                IRequest request = requestQueue.take();
                System.out.println("Prev process request ...");
                nextProcessor.process(request);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isFinished() {
        return isFinished;
    }

    @Override
    public void process(IRequest request) {
        requestQueue.add(request);
        System.out.println("添加了一条请求");
    }
}
