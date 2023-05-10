/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.common.service.CrudService;
import cn.cdsoft.common.utils.DateUtils;
import cn.cdsoft.common.utils.StringUtils;
import cn.cdsoft.modules.settings.dao.TChannelDao;
import cn.cdsoft.modules.settings.dao.TDeviceConfigDao;
import cn.cdsoft.modules.settings.dao.TDeviceDao;
import cn.cdsoft.modules.settings.dao.TDeviceDetailDao;
import cn.cdsoft.modules.settings.entity.TChannel;
import cn.cdsoft.modules.settings.entity.TDevice;
import cn.cdsoft.modules.settings.entity.TDeviceConfig;
import cn.cdsoft.modules.settings.entity.TDeviceDetail;
import cn.cdsoft.modules.sys.entity.Area;

/**
 * 设备管理Service
 * 
 * @author long
 * @version 2018-07-24
 */
@Service
@Transactional(readOnly = true)
public class TDeviceService extends CrudService<TDeviceDao, TDevice> {

	@Autowired
	private TDeviceDao tDeviceDao;
	@Autowired
	private TDeviceDetailService tDeviceDetailService;
	@Autowired
	private TChannelDao tChannelDao;
	@Autowired
	private TDeviceConfigService tDeviceConfigService;

	public TDevice get(String id) {
		return super.get(id);
	}

	public List<TDevice> findList(TDevice tDevice) {
		return super.findList(tDevice);
	}

	public Page<TDevice> findPage(Page<TDevice> page, TDevice tDevice) {
		return super.findPage(page, tDevice);
	}

	// ---------------------------------------------------------

	// 修改设备图片
	@Transactional(readOnly = false)
	public Integer updatePic(String id, String picturePath) {
		return tDeviceDao.updatePic(id, picturePath);
	}

	// 获取from表单字段
	public MapEntity getDeviceFrom(String id, String deviceFromList) {

		return tDeviceDao.getDeviceFrom(id, deviceFromList);
	}

	// 更新t_channel的orgId
	@Transactional(readOnly = false)
	public int updateChannel(String orgId, String devId) {

		return tDeviceDao.updateChannel(orgId, devId);
	}

	public MapEntity getDeviceEn(String id) {

		return tDeviceDao.getDeviceEn(id);
	}

	public Page<MapEntity> findPage(Page<MapEntity> page, MapEntity entity) {
		entity.setPage(page);
		List<MapEntity> list = tDeviceDao.deviceList(entity);
		page.setList(list);
		return page;
	}

	public List<MapEntity> deviceList(MapEntity entity) {
		List<MapEntity> deviceList = tDeviceDao.deviceList(entity);
		return deviceList;
	}

	// t_code中的name集合
	public List<MapEntity> codeList() {
		List<MapEntity> codeList = tDeviceDao.codeList();
		return codeList;
	}

	@Transactional(readOnly = false)
	public void delete(TDevice tDevice) {
		super.delete(tDevice);

	}

	@Transactional(readOnly = false)
	public String saveDevice(TDevice tDevice, TDeviceDetail tDeviceDetail) {
		int key = 0;
		if (tDevice.getIsNewRecord()) {
			tDevice.preInsert();
			key = dao.insert(tDevice);
		} else {
			tDevice.preUpdate();
			key = dao.update(tDevice);
		}
		// super.save(tDevice);
		String id = tDevice.getId();
		tDeviceDetail.setDeviceId(Integer.parseInt(id + ""));
		tDeviceDetailService.saveDeviceDetail(tDeviceDetail);
		return id;
	}

	public List<TDevice> findAllList() {
		return tDeviceDao.deviceAllList();
	}

//	public List<GatewayInfo> getGatewayInfo(GatewayInfo gatewayInfo) {
//		return tDeviceDao.getGatewayInfo(gatewayInfo);
//	}

	// 更改启用禁用
	@Transactional(readOnly = false)
	public int saveUse(TDevice tDevice) {

		return tDeviceDao.saveUse(tDevice);
	}

	// 获取区域底下设备小图标
	public List<MapEntity> devicePic(String orgId,String coldId) {
        
		return tDeviceDao.devicePic(orgId,coldId);
	}
	
	@Transactional(readOnly = false)
	public Integer updateCoords(String id,String coordX,String coordY) {
		
		return tDeviceDao.updateCoords(id,coordX,coordY);
	}
	
	
	
	
	
	
	
	

	// 删除网关
	@Transactional(readOnly = false)
	public String delGateway(String id) {

		if (StringUtils.isBlank(id)) {
			return "网关id不能为空";
		}
		TDevice device = this.get(id);
		if (device == null) {
			return "网关不存在";
		} else if (device.getDevType() != 150) {
			return "该设备不是网关";
		}
		this.delete(device);

		TDevice d = new TDevice();
		d.setParentId(Long.valueOf(id));

		// 查询充电桩
		List<TDevice> charges = this.findList(d);
		if (charges == null) {
			return "删除成功";
		}
		// 查询插头
		for (TDevice charge : charges) {
			this.delete(charge);
			TDevice d2 = new TDevice();
			d2.setParentId(Long.valueOf(charge.getId()));
			List<TDevice> plus = this.findList(d2);
			// 删除插头等信息
			if (plus == null) {
				continue;
			}
			for (TDevice p : plus) {
				this.delete(p);
				tChannelDao.deleteByDevId(p.getId());
			}
		}
		return "删除成功";
	}

	// 删除充电桩
	@Transactional(readOnly = false)
	public void delPowerCharge(String id) {
		TDevice tDevice = tDeviceDao.get(id);
		this.delete(tDevice);
		TDevice d2 = new TDevice();
		d2.setParentId(Long.valueOf(id));
		List<TDevice> plus = this.findList(d2);
		// 删除插头等信息
		if (plus == null) {
			return;
		}
		for (TDevice p : plus) {
			this.delete(p);
			tChannelDao.deleteByDevId(p.getId());
		}
	}

}