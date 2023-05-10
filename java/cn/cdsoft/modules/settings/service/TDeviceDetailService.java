/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.common.service.CrudService;
import cn.cdsoft.modules.settings.dao.TDeviceDao;
import cn.cdsoft.modules.settings.dao.TDeviceDetailDao;
import cn.cdsoft.modules.settings.entity.TDevice;
import cn.cdsoft.modules.settings.entity.TDeviceDetail;

/**
 * 数据配置Service
 * @author long
 * @version 2018-07-24
 */
@Service
@Transactional(readOnly = true)
public class TDeviceDetailService extends CrudService<TDeviceDetailDao, TDeviceDetail>  {

	public TDeviceDetail get(String id) {
		return super.get(id);
	}
	
	public List<TDeviceDetail> findList(TDeviceDetail tDeviceDetail) {
		return super.findList(tDeviceDetail);
	}
	
	public Page<TDeviceDetail> findPage(Page<TDeviceDetail> page, TDeviceDetail tDeviceDetail) {
		return super.findPage(page, tDeviceDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(TDeviceDetail tDeviceDetail) {
		super.save(tDeviceDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(TDeviceDetail tDeviceDetail) {
		super.delete(tDeviceDetail);
	}
	//-----------------------------------
	@Transactional(readOnly = false)
	public void saveDeviceDetail(TDeviceDetail tDeviceDetail) {
		if (tDeviceDetail.getIsNewRecord()){
			tDeviceDetail.preInsert();
			dao.insert(tDeviceDetail);
		}else{
			tDeviceDetail.preUpdate();
			dao.update(tDeviceDetail);
		}
	}
	
	
}