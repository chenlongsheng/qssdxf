package cn.cdsoft.modules.settings.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.modules.settings.dao.EMapConfigDao;

@Service
@Transactional(readOnly = true)
public class EMapConfigService {

	@Autowired
	EMapConfigDao eMapConfigDao;
	
	public List<Map<String,Object>> getChannelListByOrgId(String orgId) {		
		return eMapConfigDao.getChannelListByOrgId(orgId);		
	}	
	public List<Map<String,Object>> getDeviceListByOrgId(String orgId) {		
		return eMapConfigDao.getDeviceListByOrgId(orgId);
	}	
	//获取device地图的type类型
	public List<MapEntity> eMapSelect(String orgId){
		return eMapConfigDao.eMapSelect(orgId);
	}
	//获取channel地图的type类型
    public List<MapEntity> eMapChannelSelect(String orgId){
    	
    	return eMapConfigDao.eMapChannelSelect(orgId);
    }
		
		
	
	
}
