package com.moma.socket.send;

import com.moma.socket.port.ControllerPort;
import com.moma.utils.FeatureUtils;
import com.moma.utils.LocalInfo;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA
 * Created By Diamond
 * Date: 17-12-2
 * Time: 下午4:10
 */
public class ComputerToTerminalThread extends Thread{

    private  String address;
    private double[] info;
    private byte[] id;

    public ComputerToTerminalThread(String address,  double[] info) {
        this.address = address;
        this.info = info;
        id= FeatureUtils.intToBytes2((int) info[2]);
    }

    @Override
    public void run() {
        OutputStream os = null;
        Socket socket = null;
        try {

            socket = new Socket(address, ControllerPort.terminal_ctl.value()+10);
            os =  socket.getOutputStream();
             /*发送信息*/
            byte[] code = new byte[15];
            code[0] =(byte) 0xEA;
            code[1] =(byte) 0xAE;
            code[2] =(byte) 0x01;
            code[3] =(byte) 0x00;
            code[4] =(byte) 0x00;
            code[5] =(byte) 0x00;
            code[6] =(byte) 0x0F;
            code[7] =(byte) 0x00;
            code[8] = id[0];
            code[9] = id[1];
            code[10] = id[2];
            code[11] = id[3];
            code[12] =(byte) 0xEE;
            code[13] =(byte) 0xEE;
            code[14] =(byte) 0xEE;

                if(info[1]> LocalInfo.ACC){
                    code[7] =(byte) 0x01;
                }
            os.write(code);
            os.flush();
            os.close();

        }catch (IOException e){
           e.printStackTrace();
        }finally {
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(socket!=null || !socket.isClosed()){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
