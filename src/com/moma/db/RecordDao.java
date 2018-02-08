package com.moma.db;

import com.moma.bean.Record;

import java.util.List;


public interface RecordDao {
	
	public List<Record> getRecordList() throws DaoException;
	public  void insertRecord (Record record) throws DaoException ;
	public  void deleteRecord () throws DaoException ;

}