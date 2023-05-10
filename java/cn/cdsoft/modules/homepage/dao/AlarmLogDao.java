package cn.cdsoft.modules.homepage.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface AlarmLogDao {

	List<Map<String, Object>> getAllAlarm(@Param("state")String state);
	
	List<MapEntity> getAllAlarm1(MapEntity entity);

	void confirmAlarmIds(@Param("ids")String ids);

	List<Map<String, Object>> getAlarmByIds(@Param("ids")String ids);
	
	List<Map<String, Object>> getAllAlarm1(@Param("state")String state);
	
	List<Map<String, Object>> getAllAlarm999(@Param("alarmTypeFilter") String alarmTypeFilter);
	
	List<Map<String, Object>> getAllAlarm1000(@Param("alarmTypeFilter") String alarmTypeFilter);
	
	List<Map<String, Object>> getAlarmTime(@Param("chId")String chId);
	
	List<MapEntity> getAlarmByChids(@Param("chIds")String chIds);
	
	void insertAlarmLog(@Param("chId")String chId,@Param("alarmLevel")String alarmLevel,@Param("alarmValue")String alarmValue,@Param("alarmTime")String alarmTime);
	
 
}
