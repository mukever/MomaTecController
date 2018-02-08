package com.moma.db;


import com.moma.bean.Record;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.moma.db.ConnectionPool.getConnection;
import static com.moma.db.ConnectionPool.returnConnection;


public class RecordDaoImpl implements RecordDao {
    @Override
    public void deleteRecord() throws DaoException {
        String GET_Record_SQL = "DELETE  FROM record ";

        List<Record> RecordList = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pStatment =null;
        ResultSet rs = null;
        try{
            conn = ConnectionPool.getConnection();
            pStatment = conn.prepareStatement(GET_Record_SQL);

            pStatment.execute();

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                if(rs!=null) { rs.close(); }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                pStatment.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ConnectionPool.returnConnection(conn);
        }

        return ;
    }

    @Override
	public List<Record> getRecordList() throws DaoException {
		String GET_Record_SQL = "SELECT * FROM record ";

        List<Record> RecordList = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pStatment =null;
        ResultSet rs = null;
        try{
            conn = ConnectionPool.getConnection();
            pStatment = conn.prepareStatement(GET_Record_SQL);
            rs = pStatment.executeQuery();
            while(rs.next()){
                Record record = new Record();

                record.setNo(rs.getString("no"));
                record.setTime(rs.getTimestamp("time"));
                record.setType(rs.getInt("type"));
                record.setLocal(rs.getString("local"));

                RecordList.add(record);
            }
        }catch(Exception e){
            e.printStackTrace();
            //throw new DaoException("Erorr getting Feature. " + e.getMessage());
        }finally{
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                pStatment.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ConnectionPool.returnConnection(conn);
        }

        return RecordList;
	}

	@Override
	public void insertRecord(Record record) throws DaoException {

		String sql;
		sql ="insert into record(no,type,local) values(?,?,?)";
		Connection conn = null;
		PreparedStatement pStatment =null;
		ResultSet rs = null;
		try{
			conn = getConnection();
			pStatment = conn.prepareStatement(sql);
			pStatment.setString(1,record.getNo());
			pStatment.setInt(2,record.getType());
			pStatment.setString(3,record.getLocal());
			pStatment.execute();
            System.out.println("插入数据库操作"+pStatment);
		}catch(Exception e){
			throw new DaoException("Erorr  " + e.getMessage());
		}finally{
			try {

			    if(rs!=null){
			        rs.close();
                }
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pStatment.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ConnectionPool.returnConnection(conn);
		}

	}


//	@Override
//	public User getUser(String id) throws DaoException {
//		User user = new User();
//		String sql;
//		sql ="select * from user where id=?";
//		Object[] object=new Object[]{id};
//
//		Connection conn = null;
//		PreparedStatement pStatment =null;
//		ResultSet rs = null;
//		try{
//			conn = getConnection();
//			pStatment = conn.prepareStatement(sql);
//            for(int i=0;i<object.length;i++){
//                pStatment.setObject(i+1, object[i]);
//            }
//			rs = pStatment.executeQuery();
//			while(rs.next()){
//				//User User = new User();
//				user.setId(rs.getInt("id"));
//				user.setUsername(rs.getString("username"));
//				//UserList.add(User);
//			}
//		}catch(Exception e){
//			throw new DaoException("Erorr getting Users. " + e.getMessage());
//		}finally{
//			closeDbObject(rs, pStatment, conn);
//		}
//		return user;
//
//	}
//
//	@Override
//	public void deleteUser(String id) throws DaoException {
//		String sql;
//		sql ="delete from user where id=?";
//		Object[] object=new Object[]{id};
//
//		Connection conn = null;
//		PreparedStatement pStatment =null;
//		ResultSet rs = null;
//		try{
//			conn = getConnection();
//			pStatment = conn.prepareStatement(sql);
//            for(int i=0;i<object.length;i++){
//                pStatment.setObject(i+1, object[i]);
//            }
//			pStatment.execute();
//
//		}catch(Exception e){
//			throw new DaoException("Erorr getting Users. " + e.getMessage());
//		}finally{
//			closeDbObject(rs, pStatment, conn);
//		}
//
//
//
//	}
//
//	@Override
//	public String getUserPassword(String username) throws DaoException {
//		String password = null;
//		String sql;
//		sql ="select * from user where username=?";
//		Object[] object=new Object[]{username};
//
//		Connection conn = null;
//		PreparedStatement pStatment =null;
//		ResultSet rs = null;
//		try{
//			conn = getConnection();
//			pStatment = conn.prepareStatement(sql);
//            for(int i=0;i<object.length;i++){
//                pStatment.setObject(i+1, object[i]);
//            }
//			rs = pStatment.executeQuery();
//			while(rs.next()){
//				password = rs.getString("password");
//			}
//		}catch(Exception e){
//			throw new DaoException("Erorr getting Users. " + e.getMessage());
//		}finally{
//			closeDbObject(rs, pStatment, conn);
//		}
//		return password;
//
//	}
//
//	@Override
//    public List<User> findUsers(int page, int count) throws DaoException {
//		Connection conn = null;
//		PreparedStatement pStatment =null;
//		ResultSet rs = null;
//		Object[] object=new Object[]{(page-1)*count,count};
//		List<User> users = new ArrayList<>();
//		try{
//			conn = getConnection();
//			String sql ="SELECT id,username FROM user LIMIT ?,?";
//			pStatment = conn.prepareStatement(sql);
//            for(int i=0;i<object.length;i++){
//                pStatment.setObject(i+1, object[i]);
//            }
//			rs = pStatment.executeQuery();
//			while(rs.next()){
//				User user = new User();
//				user.setId(rs.getInt(1));
//				user.setUsername(rs.getString(2));
//	            users.add(user);
//			}
//		}catch(Exception e){
//			throw new DaoException("Erorr getting Users. " + e.getMessage());
//		}finally{
//			closeDbObject(rs, pStatment, conn);
//		}
//
//        return users;
//
//	}
//
//	@Override
//    public int count() throws DaoException {
//		Connection conn = null;
//		PreparedStatement pStatment =null;
//		ResultSet rs = null;
//		 int count = 0;
//		try{
//			conn = getConnection();
//			String sql ="SELECT COUNT(*) FROM user";
//			pStatment = conn.prepareStatement(sql);
//			rs = pStatment.executeQuery();
//			while(rs.next()){
//				count = rs.getInt(1);
//			}
//		}catch(Exception e){
//			throw new DaoException("Erorr getting Users. " + e.getMessage());
//		}finally{
//			closeDbObject(rs, pStatment, conn);
//		}
//        return count;
//    }
//
//

}
