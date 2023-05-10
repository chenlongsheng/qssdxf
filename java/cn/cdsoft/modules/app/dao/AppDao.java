package cn.cdsoft.modules.app.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;
/**
 * Created by ZZUSER on 2018/12/12.
 */
@MyBatisDao
public interface AppDao  {

	List<MapEntity> getOrgList();// 获取建筑
	
	List<MapEntity> getAlarmLog(@Param(value = "orgId")String orgId);
	
	void updateAlarmBychId(@Param(value = "chId")String chId);

}
