package com.moma;

import com.moma.db.ConnectionPool;
import com.moma.db.FeatureDaoImpl;
import com.moma.socket.ListenComputeSocket;
import com.moma.socket.ListenDiscorySocket;
import com.moma.socket.ListenECHOSocket;
import com.moma.socket.port.ControllerPort;
import com.moma.socket.req.ReqCIFeatureThread;
import com.moma.socket.timemanger.ReqCIFeatureTimerManager;
import com.moma.socket.timemanger.ReqCIRecordTimerManager;
import com.moma.test.InsertRecordTimerManager;
import com.moma.utils.InitUtils;
import com.moma.utils.LocalInfo;
import com.moma.utils.UpdateStorage;

/**
 * Created with IntelliJ IDEA
 * Created By Diamond
 * Date: 17-12-3
 * Time: 下午7:26
 */
public class Controller {

    public static void main(String[] args) throws Exception {


        Controller controller = new Controller();
        String path = controller.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        LocalInfo.SOPATH = path.substring(0,path.lastIndexOf("/"));
        System.out.println(LocalInfo.SOPATH);
        //初始化 address and uuid
        InitUtils.getInitInfo();

        //数据库连接
        ConnectionPool.createPool();
        FeatureDaoImpl dao = new FeatureDaoImpl();

        if(dao.getFeatureList().get("UsersNo").size()==0){
            //联网获取
            ReqCIFeatureThread reqCIFeatureThread = new ReqCIFeatureThread(LocalInfo.ADDRESS);
            reqCIFeatureThread.run();
        }

        //update feature
        UpdateStorage updateStorage = new UpdateStorage();
        updateStorage.run();

        //服务启动
        ListenDiscorySocket discorySocket = new ListenDiscorySocket(ControllerPort.controller_discover.value());
        ListenComputeSocket computeSocket = new ListenComputeSocket(ControllerPort.terminal_req.value());
        ListenECHOSocket echoSocket = new ListenECHOSocket(ControllerPort.terminal_echo.value());
        computeSocket.setPriority(7);
        echoSocket.setPriority(6);
        echoSocket.start();
        discorySocket.start();
        computeSocket.start();

        //start timetasker
        ReqCIFeatureTimerManager ciFeatureTimerManager = new ReqCIFeatureTimerManager();
        ReqCIRecordTimerManager ciRecordTimerManager = new ReqCIRecordTimerManager();

        //InsertRecordTimerManager insertRecordTimerManager = new InsertRecordTimerManager();

    }

}
