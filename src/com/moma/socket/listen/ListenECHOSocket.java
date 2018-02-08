package com.moma.socket;

import com.moma.socket.send.TerminalECHO;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA
 * Created By Diamond
 * Date: 17-12-3
 * Time: 下午7:17
 */
public class ListenECHOSocket extends Thread{
    private DatagramSocket serverSocket = null;
    private  DatagramPacket packet = null;
    private ExecutorService executorService;

    private final int POOL_SIZE = 10;
    public ListenECHOSocket(int port)  {
        try {
            serverSocket  = new DatagramSocket(port);
            executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
                    .availableProcessors() * POOL_SIZE);
            System.out.println("ECHO发现服务已启动");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void service() {
        int i =0 ;
        Long time = System.currentTimeMillis();
        while (true) {

            try {
                byte[] data = new byte[28];

                packet = new DatagramPacket(data, data.length);
                serverSocket.receive(packet);//此方法在接收到数据报之前会一直阻塞
                executorService.execute(new TerminalECHO(packet));
                i++;
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
