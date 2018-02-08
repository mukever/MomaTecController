package com.moma.utils;

import com.moma.FFMR.MMFR;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * 仓库，用来存放MMFR
 * Created with IntelliJ IDEA
 * Created By Diamond
 * Date: 17-11-28
 * Time: 下午5:30
 */

public class Storage {
    public BlockingQueue<MMFR> queues = new LinkedBlockingQueue<MMFR>(10);

    /**
     * 生产
     *
     * @param mmfr
     *            产品
     * @throws InterruptedException
     */
    public void push(MMFR mmfr) throws InterruptedException {
        queues.put(mmfr);
    }

    /**
     * 消费
     *
     * @return 产品
     * @throws InterruptedException
     */
    public MMFR pop() throws InterruptedException {
        return queues.take();
    }

    public Storage(int number_mmfr) throws InterruptedException {
        for(int i = 0;i<number_mmfr;i++){
            this.push(new MMFR());

        }
    }
    public synchronized void freeUnmanaged() throws InterruptedException {
        for(int i=0;i<queues.size();i++) {
            MMFR mmfr = queues.take();
            mmfr.freeUnmanaged();
        }
        System.out.println("gcccccc");
    }


}