package cn.cdsoft.modules.homepage.dao;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;
import cn.cdsoft.modules.homepage.vo.ChangeDevSwitchVO;
import cn.cdsoft.modules.homepage.vo.ChangeSwitchVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * Created by ZZUSER on 2019/2/13.
 */
@MyBatisDao
public interface HomepageDao {

	/**
	 * 获取首页楼栋
	 * 
	 * @param id
	 * @return
	 */
	List<JSONObject> getVisibleOrg(String id);
	
		
	List<JSONObject> getVisibleOrg1(String id);
	
	
	List<MapEntity> getWarterOrg();

	/**
	 * 首页根据设备类型获取设备
	 * 
	 * @param map
	 * @return
	 */
	List<JSONObject> getDevByType(Map map);

	/**
	 * 根据楼栋获取所有楼层
	 * 
	 * @param orgId
	 * @return
	 */
	List<JSONObject> getFloorByBuilding(String orgId);

	/**
	 * 根据楼层获取设备类型
	 * 
	 * @param arr
	 * @return
	 */
	List<JSONObject> getTypeByFloor(String[] arr);

	/**
	 * 根据楼层及类型获取设备
	 * 
	 * @param changeSwitchVO
	 * @return
	 */
	List<JSONObject> getDevByFloor(ChangeSwitchVO changeSwitchVO);

	/**
	 * 获取某一层所有设备
	 * 
	 * @param orgId
	 * @return
	 */
	List<JSONObject> getAllDevByFloor(String orgId);

	/**
	 * 获取某一层所有通道
	 * 
	 * @param orgId
	 * @return
	 */
	List<JSONObject> getAllChannelByFloor(String orgId);

	/**
	 * 根据设备id获取该设备实时数据
	 * 
	 * @param devId
	 * @return
	 */
	List<JSONObject> getNewDataByDev(String devId);

	/**
	 * 控制某个设备开关
	 * 
	 * @param vo
	 */
	void changeSwitch(ChangeDevSwitchVO vo);

	/**
	 * 根据设备获取到对应的联动设备
	 * 
	 * @param devId
	 * @return
	 */
	List<JSONObject> getViewdeo(String devId);

	/**
	 * 修改设备状态
	 * 
	 * @param map
	 */
	void updateDevStatus(Map map);

	/**
	 * 根据楼栋获取消控主机
	 * 
	 * @param orgId
	 * @return
	 */
	List<JSONObject> getFireHost(String orgId);

	/**
	 * 根据设备获取 通道
	 * 
	 * @param devId
	 * @return
	 */
	List<JSONObject> getChByDev(String devId);

	/**
	 * 获取设备报警状态
	 * 
	 * @param chId
	 * @return
	 */
	Integer getChStatus(String chId);

	/**
	 * 获取绑定在消控主机下的设备
	 * 
	 * @param devId
	 * @return
	 */
	List<JSONObject> getDevByFireHost(String devId);

	/**
	 * 根据楼栋获取到第一层
	 * 
	 * @param orgId
	 * @return
	 */
	JSONObject getFirstFloor(String orgId);

	/**
	 * 根据设备获取视频通道
	 * 
	 * @param devId
	 * @return
	 */
	List<JSONObject> getViedioByDev(String devId);

	/**
	 * 根据建筑ID获取报警列表
	 * 
	 * @param devId
	 * @return
	 */
	List<JSONObject> getBuildingAlarmList(String buildId);

	/**
	 * 获取通道实时数据
	 * 
	 * @param string
	 * @return
	 */
	List<JSONObject> getChannelRealData(String channelId);

	/**
	 * 获取视频通道
	 * 
	 * @param string
	 * @return
	 */
	List<JSONObject> getVideoByChannelId(String channelId);

	/**
	 * 获取消防主机下设备状态
	 * 
	 * @param orgId
	 * @return
	 */
	List<MapEntity> getMainDevList(String orgId);

	/**
	 * 获取消防主机状态
	 * 
	 * @param orgId
	 * @return
	 */
	List<MapEntity> getMainframeList(String orgId);

	List<JSONObject> getBuildingIdByChannelId(String chId);

	List<MapEntity> getChannelByFloor(@Param(value = "orgId") String orgId,@Param(value = "fiPath") String fiPath);
	
	List<MapEntity> getDeviceDetails();
	

	
}
