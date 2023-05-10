package cn.cdsoft.modules.settings.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface EMapConfigDao {

	public List<Map<String, Object>> getChannelListByOrgId(@Param("orgId")String orgId);
	
	public List<Map<String, Object>> getDeviceListByOrgId(@Param("orgId")String orgId);
	//获取device地图的type类型
	public List<MapEntity> eMapSelect(@Param("orgId")String orgId);
	
	//获取channel地图的type类型
     public List<MapEntity> eMapChannelSelect(@Param("orgId")String orgId);
		
	
	
}
