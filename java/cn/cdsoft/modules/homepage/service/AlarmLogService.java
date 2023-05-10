package cn.cdsoft.modules.homepage.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.modules.homepage.dao.AlarmLogDao;

@Service
public class AlarmLogService {

	@Autowired
	AlarmLogDao alarmLogDao;

	public List<Map<String, Object>> getAllAlarm(String state) {
		return alarmLogDao.getAllAlarm(state);
	}

	public List<Map<String, Object>> getAllAlarm1(String state) {
		return alarmLogDao.getAllAlarm1(state);
	}

	public List<Map<String, Object>> getAllAlarm999(String state, String alarmTypeFilter) {// 新写
		if (state.equals("0")) {
			return alarmLogDao.getAllAlarm999(alarmTypeFilter);
		} else {
			return alarmLogDao.getAllAlarm1000(alarmTypeFilter);
		}

	}

	public List<Map<String, Object>> getAlarmTime(String chId) {// 新写

		return alarmLogDao.getAlarmTime(chId);
	}

	public Page<MapEntity> findPage(Page<MapEntity> page, MapEntity entity) { // 替代上面的
		entity.setPage(page);
		List<MapEntity> list = alarmLogDao.getAllAlarm1(entity);
		page.setList(list);
		return page;
	}

	@Transactional(readOnly = false)
	public void confirmAlarmIds(String ids) {
		alarmLogDao.confirmAlarmIds(ids);
	}

	public List<Map<String, Object>> getAlarmByIds(String ids) {
		return alarmLogDao.getAlarmByIds(ids);
	}
	// --------------------

	public List<MapEntity> getAlarmByChids(String ids) {
		return alarmLogDao.getAlarmByChids(ids);
	}

	@Transactional(readOnly = false)
	public void insertAlarmLog(String ids) {

		List<MapEntity> alarmByChids = alarmLogDao.getAlarmByChids(ids);

		for (MapEntity mapEntity : alarmByChids) {
			String chId = mapEntity.get("id").toString();
			String alarmValue = mapEntity.get("realValue").toString();
			String realTime = mapEntity.get("realTime").toString();
			String alarmLevel = mapEntity.get("warn").toString();

			alarmLogDao.insertAlarmLog(chId, alarmLevel, alarmValue, realTime);
		}

	}

}
