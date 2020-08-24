package com.luhanlin.concurrent.thread;

import java.util.concurrent.TimeUnit;

/**
 * <类详细描述> 可以使用 jps jstack 查看各个线程的状态
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-24 15:52]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ThreadBlockDemo {

    public static void main(String[] args) {
        new Thread(()->{
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "thread-01").start();

        new Thread(()->{
            while (true) {
                synchronized (ThreadBlockDemo.class) {
                    try {
                        ThreadBlockDemo.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "thread-02").start();

        new Thread(new InnerBlock(),"Thread-03").start();
        new Thread(new InnerBlock(),"Thread-04").start();
    }

    static class InnerBlock implements Runnable{

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (InnerBlock.class) {
                    try {
                        TimeUnit.SECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
