package com.luhanlin.concurrent.thread;

import java.util.concurrent.TimeUnit;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-24 16:10]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ThreadInterruptDemo {

    private static Integer count = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
//                try {
//                    System.out.println("Before interrupt out :" + Thread.currentThread().isInterrupted());
//                    TimeUnit.SECONDS.sleep(100);
//                    System.out.println("After interrupt out :" + Thread.currentThread().isInterrupted());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                count++;
            }

            System.out.println(count);
        }, "interrupt-thread");

        thread.start();

        TimeUnit.SECONDS.sleep(1);
        thread.interrupt();
    }
}
