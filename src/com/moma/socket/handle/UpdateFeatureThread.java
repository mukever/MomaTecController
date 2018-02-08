package com.moma.socket.handle;

import com.moma.bean.Feature;
import com.moma.db.DaoException;
import com.moma.db.FeatureDao;
import com.moma.db.FeatureDaoImpl;
import com.moma.pool.MMDBThreadPoolUtils;
import com.moma.pool.MMSocketThreadPoolUtils;
import com.moma.socket.req.AffirmCIFeatureThread;
import com.moma.utils.LocalInfo;

import java.util.ArrayList;
import java.util.List;

public class UpdateFeatureThread implements Runnable {

    private String address;
    private ArrayList<Feature> list ;
    public UpdateFeatureThread(String address,ArrayList<Feature> list){
        this.list = list;
        this.address = address;
    }
    @Override
    public void run() {
        //插入数据库操作
     MMSocketThreadPoolUtils.executor.execute(new AffirmCIFeatureThread(LocalInfo.ADDRESS,list));
    }
}
