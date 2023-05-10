package cn.cdsoft.modules.homepage.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface SpeakerSendDao {

	List<String> getDevice();

	void insertDevice(@Param("id") String id, @Param("name") String name, @Param("orgId") String orgId,
			@Param("online") String online);

	void updateDevice(@Param("id") String id, @Param("online") String online, @Param("name") String name,@Param("updateTime") String updateTime);

	void updateDelFlag();
	
	void delDelFlag();
	
	List<MapEntity>  selectDeviceByVoice(@Param("orgId") String orgId);
	
	
	List<MapEntity> getSmokeWarning();
	
	
	List<MapEntity> getChannelTest(@Param("chId") String chId);
	
	
	
	
}
