package com.moma.socket.handle;

import com.moma.bean.Feature;
import com.moma.db.*;
import com.moma.pool.MMSocketThreadPoolUtils;
import com.moma.socket.req.AffirmCIFeatureThread;

import java.util.List;

public class DeleteDBRecordThread implements Runnable {

    @Override
    public void run() {
        //插入数据库操作

        try {
            RecordDao recordDao = new RecordDaoImpl();
            recordDao.deleteRecord();

        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
}
