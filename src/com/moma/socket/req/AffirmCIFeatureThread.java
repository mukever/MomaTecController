package com.moma.socket.req;

import com.moma.bean.Feature;
import com.moma.socket.port.ControllerPort;
import net.sf.json.JSONArray;
import org.omg.CORBA.Object;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class AffirmCIFeatureThread implements Runnable {


    private final int port = ControllerPort.AFFIRMFEATURE.value();
    private  String address = "127.0.0.1";
    private List<Feature> list;
    public AffirmCIFeatureThread(String address, List<Feature> list) {
        this.address = address;
        this.list = list;
        for(Feature feature :list){
            feature.setFeature("");
        }
    }
    @Override
    public void run() {
        Socket socket =null;

        try {
            System.out.println("发送特征同步完成确认请求");
            //开始发起一个socket
            //获取特征值list
            //更新数据库
            //发送获取成功请求
            socket = new Socket(address,port);
            byte[] code = new byte[3];
            code[0] =(byte) 0xEA;
            code[1] =(byte) 0xAE;
            code[2] = (byte)0x00;
            socket.getOutputStream().write(code);

            JSONArray jsonarray = JSONArray.fromObject(list);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(jsonarray);
            System.out.println("特征获取完毕");

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
