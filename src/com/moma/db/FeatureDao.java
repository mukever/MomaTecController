package com.moma.db;

import java.util.List;
import java.util.Map;

import com.moma.bean.Feature;


public interface FeatureDao {
	
	public Map<String, List> getFeatureList() throws DaoException;
	public  void updateFeatures(List<Feature> lsit) throws Exception;

}