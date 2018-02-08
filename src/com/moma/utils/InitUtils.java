package com.moma.utils;



import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class InitUtils {


    public static void getInitInfo() throws IOException {

        HashMap<String, String> info = new HashMap<String, String>();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null; // 用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
        fis = new FileInputStream("info.txt");// FileInputStream
        // 从文件系统中的某个文件中获取字节
        isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
        br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new	             InputStreamReader的对象
        String[] in = new String[1];
        String str = "";
        try {
            while ((str = br.readLine()) != null) {
                System.out.println(str);
                in = str.split(":");
                info.put(in[0], in[1]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            info.put(in[0], "");
            br.close();
            isr.close();
            fis.close();
        }

        String address = "";

        //start getinfo
        address = info.get("address");
        LocalInfo.ADDRESS = address;
        LocalInfo.UUID = info.get("uuid");
        LocalInfo.HOUR = Integer.valueOf(info.get("hour"));
        LocalInfo.MIN = Integer.valueOf(info.get("min"));
        LocalInfo.SEC = Integer.valueOf(info.get("sec"));
        LocalInfo.ACC = Double.valueOf(info.get("acc"));
        LocalInfo.NUMBERMMFR = Integer.valueOf(info.get("numbermmfr"));
    }

}
