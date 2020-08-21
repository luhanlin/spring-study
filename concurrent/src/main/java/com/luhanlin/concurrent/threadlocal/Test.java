package com.luhanlin.concurrent.threadlocal;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-20 14:22]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class Test {

    private static Demo demo = new Demo();

    static ThreadLocal<Demo> num = ThreadLocal.withInitial(() -> demo);

    public static void main(String[] args) {
        Thread[] threads = new Thread[20];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(()->{
                Demo demo1 = num.get();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int result = demo1.incr();
                System.out.println(Thread.currentThread().getName() + ":" + result);
            }, "thread-" + i);
        }

        for (Thread thread : threads) {
            thread.start();
        }
    }
}
