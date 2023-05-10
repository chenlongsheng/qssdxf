/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.common.service.CrudService;
import cn.cdsoft.modules.settings.dao.TAlarmPolicyDao;
import cn.cdsoft.modules.settings.entity.TAlarmPolicy;
import cn.cdsoft.modules.settings.entity.TCode;
import cn.cdsoft.modules.settings.entity.TCodeType;

/**
 * 数据配置Service
 * 
 * @author long
 * @version 2018-10-23
 */
@Service
@Transactional(readOnly = true)
public class TAlarmPolicyService extends CrudService<TAlarmPolicyDao, TAlarmPolicy> {

	public TAlarmPolicy get(String id) {
		return super.get(id);
	}

	public List<TAlarmPolicy> findList(TAlarmPolicy tAlarmPolicy) {
		return super.findList(tAlarmPolicy);
	}

	public Page<TAlarmPolicy> findPage(Page<TAlarmPolicy> page, TAlarmPolicy tAlarmPolicy) {
		return super.findPage(page, tAlarmPolicy);
	}

	@Transactional(readOnly = false)
	public void save(TAlarmPolicy tAlarmPolicy) {
		super.save(tAlarmPolicy);
	}

	@Transactional(readOnly = false)
	public void delete(TAlarmPolicy tAlarmPolicy) {
		super.delete(tAlarmPolicy);
	}

	// ------------------------

	// 获取级别
	public List<TCode> codeList(String codeTypeId) {
		return dao.codeList(codeTypeId);
	}

	// 通道主类型集合
	public List<TCodeType> codeTypeList() {
		
       return dao.codeTypeList();
	}

}