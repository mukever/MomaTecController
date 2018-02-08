package com.moma.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MMSocketThreadPoolUtils {

    public static ExecutorService executor =  Executors.newFixedThreadPool(Runtime.getRuntime()
            .availableProcessors() * 20);

}
