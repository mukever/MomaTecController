package com.moma.socket.send;

import com.moma.socket.port.ControllerPort;
import com.moma.socket.exception.PackageException;
import com.moma.utils.FeatureUtils;

import java.io.*;
import java.net.*;


public class TerminalECHO extends Thread {

    private DatagramPacket packet;


    public TerminalECHO(DatagramPacket packet) {
        this.packet = packet;
    }

    public  void run() {
        try {

            InetAddress address = packet.getAddress();
            System.out.println("ECHO 客户端 "+address.getHostAddress());
            byte[] data = packet.getData();
            String headcode = FeatureUtils.bytesToHexString(data).substring(0,4);
            //System.out.print(headcode);
            if(!"eaae".equals(headcode)){
                throw new PackageException();
            }

            byte[] code = new byte[10];
            code[0] =(byte) 0xEA;
            code[1] =(byte) 0xAE;
            code[2] = (byte)0x00;
            code[3] = (byte)0x00;
            code[4] = (byte)0x00;
            code[5] = (byte)0x00;
            code[6] = (byte)0x0A;
            code[7] = (byte)0xEE;
            code[8] = (byte)0xEE;
            code[9] = (byte)0xEE;


            DatagramPacket sendpacket = new DatagramPacket(code, code.length, address, ControllerPort.terminal_echo.value()+10);
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(sendpacket);
        } catch (PackageException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
