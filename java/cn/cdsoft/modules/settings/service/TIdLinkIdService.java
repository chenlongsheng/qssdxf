/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.common.service.CrudService;
import cn.cdsoft.modules.settings.dao.TCodeDao;
import cn.cdsoft.modules.settings.dao.TDeviceDao;
import cn.cdsoft.modules.settings.dao.TIdLinkIdDao;
import cn.cdsoft.modules.settings.entity.TCode;
import cn.cdsoft.modules.settings.entity.TIdLinkId;
import cn.cdsoft.modules.sys.entity.Area;

/**
 * 联动管理Service
 * 
 * @author long
 * @version 2018-08-08
 */
@Service
@Transactional(readOnly = true)
public class TIdLinkIdService extends CrudService<TIdLinkIdDao, TIdLinkId> {
	@Autowired
	private TCodeDao tCodeDao;
	@Autowired
	private TIdLinkIdDao tIdLinkIdDao;

	public TIdLinkId get(String id) {
		return super.get(id);
	}

	public List<TIdLinkId> findList(TIdLinkId tIdLinkId) {
		return super.findList(tIdLinkId);
	}

	public Page<MapEntity> findPage(Page<MapEntity> page, MapEntity entity) {
		entity.setPage(page);
		page.setList(tIdLinkIdDao.TIdLinkIdList(entity));
		return page;
	}

	@Transactional(readOnly = false)
	public void save(TIdLinkId tIdLinkId) {
		super.save(tIdLinkId);
	}

	@Transactional(readOnly = false)
	public void delete(TIdLinkId tIdLinkId) {
		super.delete(tIdLinkId);
	}

	// -------------------------------

	public List<MapEntity> codeList() {
		return tIdLinkIdDao.codeList();
	}

	// 更改启用禁用
	@Transactional(readOnly = false)
	public Integer saveUse(TIdLinkId tIdLinkId) {

		return tIdLinkIdDao.saveUse(tIdLinkId);
		
	}

	// 区域 通道底下
	public List<Area> findAreaList(String name) {
		return tIdLinkIdDao.findAreaList(name);
	}

	public List<MapEntity> getVideoChannelLinkList(String channelSrcId) {
		return tIdLinkIdDao.getVideoChannelLinkList(channelSrcId);
	}
}