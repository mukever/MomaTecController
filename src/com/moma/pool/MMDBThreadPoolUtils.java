package com.moma.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MMDBThreadPoolUtils {

    public static ExecutorService executor =  Executors.newFixedThreadPool(Runtime.getRuntime()
            .availableProcessors() * 20);

}
