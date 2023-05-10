/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.sys.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.cdsoft.common.service.TreeService;
import cn.cdsoft.modules.sys.dao.AreaDao;
import cn.cdsoft.modules.sys.entity.Area;
import cn.cdsoft.modules.sys.utils.UserUtils;

/**
 * 区域Service
 * 
 * @author jeeplus
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends TreeService<AreaDao, Area> {

	@Autowired
	private AreaDao areaDao;

	public List<Area> findAll() {
		return UserUtils.getAreaList();
	}

	@Transactional(readOnly = false)
	public void save(Area area) {
		super.save(area);
		int type = Integer.parseInt(area.getType())+1;
		areaDao.updateSonOrg(type, area.getId());
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}

	@Transactional(readOnly = false)
	public void delete(Area area) {
		super.delete(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}
	// ----------------------------------

	@Transactional(readOnly = false)
	public int saveImage(Area area) {
		return areaDao.saveImage(area);
	}

	// 区域user下的集合
	public List<Area> userOrgList(String orgId) {

		return areaDao.userOrgList(orgId);
	}

	// 区域code获取
	public String selectCode(String orgId) {

		return areaDao.selectCode(orgId);
	}
	
	//区域底下子集的最大code获取
	public String maxCode(String orgId) {
		return areaDao.maxCode(orgId);
	}
	
	
	

}
