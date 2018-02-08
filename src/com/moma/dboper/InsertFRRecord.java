package com.moma.dboper;

import com.moma.bean.Record;
import com.moma.db.DaoException;
import com.moma.db.RecordDaoImpl;
import com.moma.utils.LocalInfo;

public class InsertFRRecord implements Runnable {

    private String no;
    private int type;
    public InsertFRRecord(String no,int type){
        this.no = no;
        this.type = type;
    }
    @Override
    public synchronized  void run() {
        //获取数据连接
        RecordDaoImpl dao = new RecordDaoImpl();

        Record record = new Record();
        record.setLocal(LocalInfo.UUID);
        record.setNo(no);
        record.setType(type);
        try {

            //插入一条记录
            dao.insertRecord(record);
        } catch (DaoException e) {
            e.printStackTrace();
        }

    }
}
