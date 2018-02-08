package com.moma.socket;

/**
 * Created with IntelliJ IDEA
 * Created By diamond
 * Date: 17-11-28
 * Time: 下午5:06
 */

import com.moma.socket.handle.Compute;
import com.moma.utils.LocalInfo;
import com.moma.utils.Storage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListenComputeSocket extends Thread{


    private ServerSocket serverSocket;
    private ExecutorService executorService;

    private final int POOL_SIZE = 40;
    private Storage s ;
    public ListenComputeSocket(int port) {
        try {
            serverSocket = new ServerSocket(port);
            executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
                    .availableProcessors() * POOL_SIZE);

            this.s = LocalInfo.s;
            System.out.println("计算服务已启动");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void service() {
        int i =0 ;
        //Long time = System.currentTimeMillis();
        while (true) {
            System.out.println("计算客户端 第"+String.valueOf(i)+"个请求");
            i++;
            Socket socket = null;
            try {

                socket = serverSocket.accept();
                executorService.execute(new Compute(socket,s));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void run() {
        this.service();
    }


}
