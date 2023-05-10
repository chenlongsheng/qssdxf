/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.service;

import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.common.service.CrudService;
import cn.cdsoft.modules.settings.dao.TDeviceDao;
import cn.cdsoft.modules.settings.dao.TOrgExecUnitBindDao;
import cn.cdsoft.modules.settings.entity.CdzChargePot;
import cn.cdsoft.modules.settings.entity.CpCarpark;
import cn.cdsoft.modules.settings.entity.TOrgExecUnitBind;
import cn.cdsoft.modules.sys.entity.Area;

/**
 * 运营管理Service
 * 
 * @author long
 * @version 2018-08-17
 */
@Service
@Transactional(readOnly = true)
public class TOrgExecUnitBindService extends CrudService<TOrgExecUnitBindDao, TOrgExecUnitBind> {

	@Autowired
	private TOrgExecUnitBindDao tOrgExecUnitBindDao;

	public TOrgExecUnitBind get(String id) {
		return super.get(id);
	}

	public List<TOrgExecUnitBind> findList(TOrgExecUnitBind tOrgExecUnitBind) {
		return super.findList(tOrgExecUnitBind);
	}

	public Page<TOrgExecUnitBind> findPage(Page<TOrgExecUnitBind> page, TOrgExecUnitBind tOrgExecUnitBind) {
		return super.findPage(page, tOrgExecUnitBind);
	}

	@Transactional(readOnly = false)
	public void save(TOrgExecUnitBind tOrgExecUnitBind) {
		super.save(tOrgExecUnitBind);
	}

	@Transactional(readOnly = false)
	public void delete(TOrgExecUnitBind tOrgExecUnitBind) {
		super.delete(tOrgExecUnitBind);
	}

	// ------------------------------------------------------
	

	//获取表的集合
	public List<CpCarpark> carParkList() {

		return tOrgExecUnitBindDao.carParkList();
	}

	public List<CdzChargePot> cdzChargePotList() {

		return tOrgExecUnitBindDao.cdzChargePotList();
	}
    //获取集合改造
	public Page<MapEntity> findBindPage(Page<MapEntity> page, MapEntity entity) {
		entity.setPage(page);
		List<MapEntity> findList = tOrgExecUnitBindDao.findBindList(entity);		
		page.setList(findList);
		return page;
	}
	
	public TOrgExecUnitBind getTorg(String id,String orgId) {
		
		return tOrgExecUnitBindDao.getTorg(id,orgId);
	}
	
	public Page<TOrgExecUnitBind> findNotPage(Page<TOrgExecUnitBind> page,TOrgExecUnitBind entity){
		entity.setPage(page);
		List<TOrgExecUnitBind> findList = tOrgExecUnitBindDao.findNotList(entity);		
		for (int i = 0; i < findList.size(); i++) {
			System.out.println(findList.get(i).toString()+"niao");
		}
		page.setList(findList);
		return page;
		
	}
	
	public List<Area> findAreaList(){
		
		return tOrgExecUnitBindDao.findAreaList();
		
	}
}