package com.moma.socket.req;

import com.arcsoft.LoadUtils;
import com.moma.bean.Feature;
import com.moma.db.FeatureDao;
import com.moma.db.FeatureDaoImpl;
import com.moma.pool.MMDBThreadPoolUtils;
import com.moma.pool.MMSocketThreadPoolUtils;
import com.moma.socket.handle.UpdateFeatureThread;
import com.moma.socket.port.ControllerPort;
import com.moma.utils.LocalInfo;
import com.moma.utils.UpdateStorage;
import net.sf.json.JSONArray;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA
 * Created By Diamond
 * Date: 17-12-2
 * Time: 下午4:10
 */
public class ReqCIFeatureThread  extends TimerTask {

    private final int port = ControllerPort.CIGETFEATURE.value();
    private  String address = "";
    public ReqCIFeatureThread(String address) {
        this.address = address;
    }

    @Override
    public void run() {
        Socket socket =null;
        try {
            System.out.println("发送获取特征请求");

            //开始发起一个socket
            //获取特征值list
            //更新数据库
            //发送获取成功请求
            socket = new Socket(address,ControllerPort.CIGETFEATURE.value());
            byte[] code = new byte[3];
            code[0] =(byte) 0xEA;
            code[1] =(byte) 0xAE;
            code[2] = (byte)0x00;
            socket.getOutputStream().write(code);
            socket.getOutputStream().write((LocalInfo.UUID+"\n").getBytes());
            //socket.getOutputStream().flush();
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());


            JSONArray list = (JSONArray) objectInputStream.readObject();
            //System.out.println(list);
            ArrayList<Feature> features = (ArrayList<Feature>)JSONArray.toList(list, Feature.class);

            if(list.size()>0){
                System.out.println("update:::"+list.size());
                FeatureDao featureDao = new FeatureDaoImpl();
                featureDao.updateFeatures(features);
                MMSocketThreadPoolUtils.executor.execute(new AffirmCIFeatureThread(LocalInfo.ADDRESS,features));
            }else {
                System.out.println("无数据更新");
            }
            System.out.println("Feacture GET OK");
            //socket.close();
        }catch (Exception e){

       e.printStackTrace();
            System.out.println("连接出错");

        }finally {
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                //update the feature
                UpdateStorage updateStorage = new UpdateStorage();
                updateStorage.run();
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }
}
