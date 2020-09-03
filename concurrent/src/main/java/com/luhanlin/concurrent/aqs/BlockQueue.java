package com.luhanlin.concurrent.aqs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-28 10:53]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class BlockQueue {

    private List queue;
    private ReentrantLock lock;
    private Condition putCondition;
    private Condition takeCondition;
    private static final Integer SIZE = 10;

    public BlockQueue() {
        this.queue = new ArrayList<>(SIZE);
        this.lock = new ReentrantLock();
        this.putCondition = lock.newCondition();
        this.takeCondition = lock.newCondition();
    }

    public static void main(String[] args) {
        BlockQueue queue = new BlockQueue();

        new Thread(()->{
            for (int i = 0; i < 100; i++) {
                queue.put(i);
            }
        },"插入线程").start();

        new Thread(()->{
            while (true) {
                System.out.println("获取到数据： "+queue.take());
            }
        },"获取数据线程").start();
    }

    public void put(Object obj) {
        try {
            lock.lock();
            if (queue.size() > SIZE) {
                System.out.println("当前线程【" + Thread.currentThread().getName() + "】请求 put数据过多，线程进入等待。。。");
                putCondition.await();
            }
            queue.add(obj);
            takeCondition.signalAll();
            System.out.println("当前线程【" + Thread.currentThread().getName() + "】请求PUT数据成功,take 操作被唤起");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public Object take() {
        try {
            lock.lock();
            if (queue.size() <= 0) {
                System.out.println("当前线程【" + Thread.currentThread() + "】请求take数据，队列暂无数据，进入阻塞...");
                takeCondition.await();
            }
            Object result = queue.remove(0);
            putCondition.signalAll();
            System.out.println("当前线程【" + Thread.currentThread() + "】请求TAKE数据成功, put操作被唤起...");

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }

}
