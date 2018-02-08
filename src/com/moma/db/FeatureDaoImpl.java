package com.moma.db;


import com.arcsoft.AFR_FSDK_FACEMODEL;
import com.moma.bean.Feature;
import com.moma.utils.FeatureUtils;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.moma.db.ConnectionPool.getConnection;


public class FeatureDaoImpl implements FeatureDao {

	@Override
	public Map<String, List> getFeatureList() throws DaoException {

		Map<String,List> data = new HashMap<>();
		String GET_User_SQL = "SELECT * FROM userid";

		List<AFR_FSDK_FACEMODEL> featureList = new ArrayList<>();
		ArrayList<String> noList = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pStatment =null;
		ResultSet rs = null;
		try{
			conn = getConnection();
			pStatment = conn.prepareStatement(GET_User_SQL);
			rs = pStatment.executeQuery();
			while(rs.next()){
				String feature_str = rs.getString("feature");
				String no = rs.getString("no");
				byte[] featureInByte_temp = FeatureUtils.hexStringToBytes(feature_str);
				featureList.add(AFR_FSDK_FACEMODEL.fromByteArray(featureInByte_temp));
				noList.add(no);
			}
			data.put("UsersFeature",featureList);
			data.put("UsersNo",noList);
		}catch(Exception e){
			throw new DaoException("Erorr getting Users. " + e.getMessage());
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
		
		return data;

	}



	public void insertFeatures(Feature feature) throws Exception {
		/*
		*
		* 得到的no feature
		* 将no 转为id
		* 然后将id插入／更新
		 */
		String insert_sql;

		insert_sql = "insert userid (no,feature) values(?,?)";
		Connection conn = null;
		PreparedStatement pStatment =null;
		ResultSet rs = null;
		try{
			conn = getConnection();


			//insert;
			pStatment = conn.prepareStatement(insert_sql);
			pStatment.setString(1,feature.getNo());
			pStatment.setString(2,feature.getFeature());

			System.out.println("插入"+pStatment);
			pStatment.execute();


		}catch (Exception e){
			e.printStackTrace();
		}finally{
			try {
				if(rs!=null){
					rs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(pStatment!=null){
					pStatment.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ConnectionPool.returnConnection(conn);
		}

	}

	public void updateFeatures(List<Feature> list) throws Exception {
		/*
		 *
		 * 得到的no feature
		 * 将no 转为id
		 * 然后将id插入／更新
		 */
		String update_sql;
		String insert_sql;
		String query_sql;
		String delete_sql;
		query_sql = "select *  from userid where no=?";
		insert_sql = "insert userid (no,feature) values(?,?)";
		update_sql = "update userid set feature=? where no = ?";
		delete_sql = "delete from userid where no=?";
		Connection conn = null;
		PreparedStatement pStatment =null;
		ResultSet rs = null;
		try{
			conn = getConnection();
			for(Feature feature :list){
				//query;
				pStatment = conn.prepareStatement(query_sql);
				pStatment.setString(1,feature.getNo());
				rs = pStatment.executeQuery();
				System.out.println("查询"+pStatment);
				System.out.println(feature.getFeature());
				if(rs.next()){
					//update;
					if(feature.getIsdelete()==0){

						pStatment = conn.prepareStatement(update_sql);
						pStatment.setString(1,feature.getFeature());
						pStatment.setString(2,feature.getNo());
						System.out.println("更新"+pStatment);
						pStatment.execute();
					}else {
						//删除

						pStatment = conn.prepareStatement(delete_sql);
						pStatment.setString(1,feature.getNo());
						System.out.println("删除"+pStatment);
						pStatment.execute();

					}


				}
				else {
					//insert;
					pStatment = conn.prepareStatement(insert_sql);
					pStatment.setString(1,feature.getNo());
					pStatment.setString(2,feature.getFeature());

					System.out.println("插入"+pStatment);
					pStatment.execute();

				}


			}
		}finally{
			try {
				if(rs!=null){
					rs.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(pStatment!=null){
					pStatment.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ConnectionPool.returnConnection(conn);
		}

	}

}
