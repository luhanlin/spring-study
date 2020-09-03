package com.luhanlin.concurrent.volatiled;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-26 10:56]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class VolatileTest {

    private static volatile boolean stop = false;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            int i = 0;
            while (!stop) {
                i++;
            }
        });

        thread.start();
        thread.sleep(1000);
        stop = true;
    }
}
