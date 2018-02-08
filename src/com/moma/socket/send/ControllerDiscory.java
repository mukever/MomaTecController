package com.moma.socket.send;

import com.moma.socket.port.ControllerPort;
import com.moma.utils.FeatureUtils;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class ControllerDiscory extends Thread {

    private DatagramPacket packet;

    public ControllerDiscory(DatagramPacket packet) {
        this.packet = packet;
       
    }


    public  void run() {
    	InputStream br  = null;
        try {

            InetAddress address = packet.getAddress();
            System.out.println("发现 客户端 "+address.getHostAddress());
            byte[] code = new byte[10];
            code[0] =(byte) 0xEA;
            code[1] =(byte) 0xAE;
            code[2] = (byte)0x04;
            code[3] = (byte)0x00;
            code[4] = (byte)0x00;
            code[5] = (byte)0x00;
            code[6] = (byte)0x0A;
            code[7] = (byte)0xEE;
            code[8] = (byte)0xEE;
            code[9] = (byte)0xEE;

            DatagramPacket sendpacket = new DatagramPacket(code, code.length, address, ControllerPort.controller_discover.value()+10);
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(sendpacket);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
