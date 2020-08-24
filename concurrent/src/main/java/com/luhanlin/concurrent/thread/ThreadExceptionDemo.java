package com.luhanlin.concurrent.thread;

import java.util.concurrent.TimeUnit;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-24 16:32]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ThreadExceptionDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("线程还在运行中哦！");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "exception-thread");

        thread.start();

        TimeUnit.SECONDS.sleep(1);
        thread.interrupt();

        System.out.println(thread.isInterrupted()); // 中断线程会先复位再抛出异常
    }
}
