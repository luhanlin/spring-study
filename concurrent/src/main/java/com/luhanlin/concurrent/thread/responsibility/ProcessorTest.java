package com.luhanlin.concurrent.thread.responsibility;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-24 17:01]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ProcessorTest {

    @SneakyThrows
    public static void main(String[] args) {
        PrintRequestProcessor processor = new PrintRequestProcessor();
        processor.start();
        SaveRequestProcessor saveRequestProcessor = new SaveRequestProcessor(processor);
        saveRequestProcessor.start();
        PrevRequestProcessor prevRequestProcessor = new PrevRequestProcessor(saveRequestProcessor);
        prevRequestProcessor.start();

        for (int i = 0; i < 100; i++) {
            TimeUnit.SECONDS.sleep(1);
            prevRequestProcessor.process(new IRequest());
        }
    }
}
