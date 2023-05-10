package cn.cdsoft.modules.app.service;

import cn.cdsoft.modules.warm.entity.PdfBind;
import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.service.CrudService;
import cn.cdsoft.modules.app.dao.AppDao;
import cn.cdsoft.modules.warm.dao.PdfBindDao;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by ZZUSER on 2018/12/12.
 */
@Service
public class AppService extends CrudService<PdfBindDao, PdfBind> {
	@Autowired
	AppDao appDao;

	public List<MapEntity> getOrgList() {
		// 获取建筑
		return appDao.getOrgList();
	}

	public List<MapEntity> getAlarmLog(String orgId) {

		return appDao.getAlarmLog(orgId);
	}

	@Transactional(readOnly = false)
	public void updateAlarmBychId(String chId) {

		appDao.updateAlarmBychId(chId);
	}

}
