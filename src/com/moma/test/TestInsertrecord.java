package com.moma.test;

import com.moma.bean.Record;
import com.moma.db.DaoException;
import com.moma.db.RecordDaoImpl;
import com.moma.utils.LocalInfo;

import java.sql.Date;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimerTask;

public class TestInsertrecord extends TimerTask{



    @Override
    public synchronized void run() {
        RecordDaoImpl dao = new RecordDaoImpl();

            Record record = new Record();
            record.setLocal(LocalInfo.UUID);
            record.setNo("98f46889038d47d3b202afbe56b85c03");
            record.setType(0);
            try {
                dao.insertRecord(record);
            } catch (DaoException e) {
                e.printStackTrace();
            }

    }
}
