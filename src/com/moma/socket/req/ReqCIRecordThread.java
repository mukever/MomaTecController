package com.moma.socket.req;

import com.moma.bean.Feature;
import com.moma.bean.Record;
import com.moma.db.RecordDaoImpl;
import com.moma.pool.MMDBThreadPoolUtils;
import com.moma.pool.MMSocketThreadPoolUtils;
import com.moma.socket.handle.DeleteDBRecordThread;
import com.moma.socket.handle.UpdateFeatureThread;
import com.moma.socket.port.ControllerPort;
import com.moma.utils.FeatureUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA
 * Created By Diamond
 * Date: 17-12-2
 * Time: 下午4:10
 */
public class ReqCIRecordThread extends TimerTask{

    private final int port = ControllerPort.CIRECORD.value();
    private  String address ;
    public ReqCIRecordThread(String address) {
        this.address = address;
    }

    @Override
    public void run() {
        Socket socket =null;
        try {
            System.out.println("上传Reocrd请求启动");

            //开始发起一个socket
            //上传recordlist
            //record上传成功
            //更新数据库
            RecordDaoImpl dao = new RecordDaoImpl();
            List<Record>  list = dao.getRecordList();
            System.out.println("读取数据库完成");
            if(list.size()>0){
                socket = new Socket(address,ControllerPort.CIRECORD.value());
                byte[] code = new byte[3];
                code[0] =(byte) 0xEA;
                code[1] =(byte) 0xAE;
                code[2] = (byte)0x00;
                socket.getOutputStream().write(code);

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                objectOutputStream.writeObject(list);
                System.out.println("发送object——list完成");
                byte[] affirmfromserver = new byte[3];
                socket.getInputStream().read(affirmfromserver,0,3);
                if(!"eaae00".equals(FeatureUtils.bytesToHexString(code))) {
                    System.out.println(Thread.currentThread().getName()+"   error-data");
                    System.out.println("错误消息头");
                    socket.close();
                    return;
                }else {
                    System.out.println("get return record-head");
                    MMDBThreadPoolUtils.executor.execute(new DeleteDBRecordThread());
                }


            }else {
                //其他操作
                System.out.println("Record记录为0");
            }
            System.out.println("数据上传成功");

        }catch (Exception e){

       // e.printStackTrace();
            System.out.println("连接出错");

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
