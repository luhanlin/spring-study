package com.luhanlin.concurrent.threadlocal;

/**
 * <类详细描述> 测试原子性和裴波那契散列
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-20 17:11]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class Demo {

    private static final int HASH_INCREMENT = 0x61c88647;

    private int count = 0;

    public int incr() {
        count++;
        return count;
    }

    public static void main(String[] args) {
        hash(16);

        hash(32);
    }

    private static void hash(int size) {
        int hashCode = 0;

        for (int i = 0; i < size; i++) {
            hashCode = i*HASH_INCREMENT + HASH_INCREMENT;

            System.out.print((hashCode & (size - 1)) + " ");
        }

        System.out.println();
    }

}
