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
public class ThreadResetDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Before reset out :" + Thread.currentThread().isInterrupted());
                    Thread.interrupted();
                    System.out.println("After reset out :" + Thread.currentThread().isInterrupted());
                }
            }

        }, "reset-thread");

        thread.start();

        TimeUnit.SECONDS.sleep(1);
        thread.interrupt();
    }
}
