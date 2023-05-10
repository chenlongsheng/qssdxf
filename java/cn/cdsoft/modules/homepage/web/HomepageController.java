package cn.cdsoft.modules.homepage.web;

import cn.cdsoft.common.service.ServiceResult;
import cn.cdsoft.common.utils.ServletUtils;
import cn.cdsoft.modules.homepage.dao.WebcamPostDao;
import cn.cdsoft.modules.homepage.service.HomepageService;
import cn.cdsoft.modules.homepage.vo.ChangeDevSwitchVO;
import cn.cdsoft.modules.homepage.vo.ChangeSwitchVO;
import cn.cdsoft.modules.settings.entity.TChannel;
import cn.cdsoft.modules.settings.entity.TOrg;
import cn.cdsoft.modules.settings.service.TChannelService;
import cn.cdsoft.modules.settings.service.TOrgService;
import cn.cdsoft.modules.sys.entity.User;
import cn.cdsoft.modules.sys.utils.UserUtils;
import cn.cdsoft.modules.websocket.MyWebSocketHandler;
import com.alibaba.fastjson.JSONObject;

import org.restlet.engine.adapter.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ZZUSER on 2019/2/13.
 */
@Controller
@RequestMapping("${adminPath}/home")
public class HomepageController {
//	
	@Value("${ipFile}")
	private String ipFile;

	@Autowired
	HomepageService homepageService;

	@Autowired
	TChannelService channelService;

	@Autowired
	TOrgService orgService;

	@Autowired
	WebcamPostDao webcamPostDao;

	/**
	 * 获取首页楼栋
	 * 
	 * @return
	 */
	@RequestMapping("getVisibleOrg2")
	@ResponseBody
	public JSONObject getVisibleOrg2() {
		ServiceResult serviceResult = homepageService.getVisibleOrg2();
		return ServletUtils.buildJsonRs(serviceResult.isSuccess(), serviceResult.getMessage(), serviceResult.getData());
	}

	@RequestMapping("getVisibleOrg")
	@ResponseBody
	public JSONObject getVisibleOrg() {
		ServiceResult serviceResult = homepageService.getVisibleOrg1();
		return ServletUtils.buildJsonRs(serviceResult.isSuccess(), serviceResult.getMessage(), serviceResult.getData());
	}

	/**
	 * 首页根据设备类型获取设备
	 * 
	 * @param type
	 * @return
	 */
	@RequestMapping("/getDevByType")
	@ResponseBody
	public JSONObject getDevByType(int type) {
		ServiceResult serviceResult = homepageService.getDevByType(type);
		return ServletUtils.buildJsonRs(serviceResult.isSuccess(), serviceResult.getMessage(), serviceResult.getData());
	}

	/**
	 * 根据楼栋获取所有楼层
	 * 
	 * @param buildingId
	 * @return
	 */
	@RequestMapping("/getFloorByBuilding")
	@ResponseBody
	public JSONObject getFloorByBuilding(String buildingId) {
		ServiceResult serviceResult = homepageService.getFloorByBuilding(buildingId);
		return ServletUtils.buildJsonRs(serviceResult.isSuccess(), serviceResult.getMessage(), serviceResult.getData());
	}

	/**
	 * 根据楼层获取楼层所有设备类型
	 * 
	 * @param floors
	 * @return
	 */
	@RequestMapping("/getTypeByFloor")
	@ResponseBody
	public JSONObject getTypeByFloor(String floors) {
		ServiceResult serviceResult = homepageService.getTypeByFloor(floors);
		return ServletUtils.buildJsonRs(serviceResult.isSuccess(), serviceResult.getMessage(), serviceResult.getData());
	}

	/**
	 * 切换开关
	 * 
	 * @param changeSwitchVO
	 * @return
	 */
	@RequestMapping("/changeSwitch")
	@ResponseBody
	public JSONObject changeSwitch(ChangeSwitchVO changeSwitchVO) {
		ServiceResult serviceResult = homepageService.changeSwitch(changeSwitchVO);
		return ServletUtils.buildJsonRs(serviceResult.isSuccess(), serviceResult.getMessage(), serviceResult.getData());
	}

	/**
	 * 获取某一层所有的设备
	 * 
	 * @param orgId
	 * @return
	 */
	// 获取设备所有---显示状态
	@RequestMapping("/getAllDevByFloor")
	@ResponseBody
	public JSONObject getAllDevByFloor(String orgId, HttpServletRequest request) {
		String path = ipFile;
		ServiceResult serviceResult = homepageService.getAllDevByFloor(orgId, path);
		return ServletUtils.buildJsonRs(serviceResult.isSuccess(), serviceResult.getMessage(), serviceResult.getData());
	}

	// 获取设备所有---显示状态
	@RequestMapping("/getAllChannelByFloor")
	@ResponseBody
	public JSONObject getAllChannelByFloor(String orgId, HttpServletRequest request) {
		String path = ipFile;
		ServiceResult serviceResult = homepageService.getAllChannelByFloor(orgId, path);
		return ServletUtils.buildJsonRs(serviceResult.isSuccess(), serviceResult.getMessage(), serviceResult.getData());
	}

	// 获取设备所有---显示状态
	@RequestMapping("/getAllChannelByFloor2")
	@ResponseBody
	public String getAllChannelByFloor2(String orgId, HttpServletRequest request) { // ----99999
		String path = ipFile;
		return ServletUtils.buildRs(true, "", homepageService.getChannelByFloor(orgId, path));
	}

	// 获取设备所有---显示状态
	@RequestMapping("/getAllChannelByFloor3")
	@ResponseBody
	public String getAllChannelByFloor3(String orgId, HttpServletRequest request) { // ----99999
		String path = ipFile;
		return ServletUtils.buildRs(true, "", homepageService.getChannelByFloor(orgId, path));
	}

	/**
	 * 获取报警
	 * 
	 * @return
	 */
	@RequestMapping("/getAlarm")
	@ResponseBody
	public JSONObject getAlarm() {
		ServiceResult serviceResult = homepageService.getAlarm();
		return ServletUtils.buildJsonRs(serviceResult.isSuccess(), serviceResult.getMessage(), serviceResult.getData());
	}

	/**
	 * 某个设备开关控制
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping("/changeDevSwitch")
	@ResponseBody
	public JSONObject changeDevSwitch(ChangeDevSwitchVO vo) {
		ServiceResult serviceResult = homepageService.changeDevSwitch(vo);
		return ServletUtils.buildJsonRs(serviceResult.isSuccess(), serviceResult.getMessage(), serviceResult.getData());
	}

	/**
	 * 模拟报警点启动
	 */
	@RequestMapping("/simulateAlarm")
	@ResponseBody
	public JSONObject simulateAlarm(String devId) {
		ServiceResult serviceResult = homepageService.simulateAlarm(devId);
		return ServletUtils.buildJsonRs(serviceResult.isSuccess(), serviceResult.getMessage(), serviceResult.getData());
	}

	@RequestMapping("/getFireHost")
	@ResponseBody
	public JSONObject getFireHost(String buildId) {
		ServiceResult serviceResult = homepageService.getFireHost(buildId);
		return ServletUtils.buildJsonRs(serviceResult.isSuccess(), serviceResult.getMessage(), serviceResult.getData());
	}

	@Autowired
	MyWebSocketHandler myWebSocketHandler;

	@RequestMapping("/sendMessage")
	@ResponseBody
	public void sendMessage() {
		TextMessage textMessage = new TextMessage("alarm");
		try {
			myWebSocketHandler.sendMessageToUser("1", textMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/getUser")
	@ResponseBody
	public JSONObject getUser() {
		JSONObject jsonObject = new JSONObject();
		User user = UserUtils.getUser();
		jsonObject.put("data", user);
		return jsonObject;
	}

	@RequestMapping("/getBuildingAlarmListByChannelId")
	@ResponseBody
	public JSONObject getBuildingAlarmListByChannelId(String alarmChannelId) {
		TChannel channel = channelService.get(alarmChannelId);
		String orgId = channel.getLogicOrgId();
		TOrg org = orgService.get(orgId);
		int type = org.getType();

		String buildId = "";
		if (type == 6) {
			buildId = org.getId();
		} else if (type == 7) {
			buildId = org.getParentId().toString();
		}
		ServiceResult serviceResult = homepageService.getBuildingAlarmList(buildId);
		return ServletUtils.buildJsonRs(serviceResult.isSuccess(), serviceResult.getMessage(), serviceResult.getData());
	}

	@RequestMapping("/getBuildingAlarmListByBuildingId")
	@ResponseBody
	public JSONObject getBuildingAlarmListByBuildingId(String buildId) {
		ServiceResult serviceResult = homepageService.getBuildingAlarmList(buildId);
		return ServletUtils.buildJsonRs(serviceResult.isSuccess(), serviceResult.getMessage(), serviceResult.getData());
	}

	@RequestMapping("/getMainframeList")
	@ResponseBody
	public String getMainframeList(String orgId) {

		return ServletUtils.buildRs(true, "获取消防主机状态", homepageService.getMainframeList(orgId));
	}

	@RequestMapping("/getBuildingIdByChannelId")
	@ResponseBody
	public JSONObject getBuildingIdByChannelId(String chId) {
		ServiceResult serviceResult = homepageService.getBuildingIdByChannelId(chId);
		return ServletUtils.buildJsonRs(serviceResult.isSuccess(), serviceResult.getMessage(), serviceResult.getData());
	}

	@RequestMapping("/getFirstFloor")
	@ResponseBody
	public String getFirstFloor(String orgId) { // 新写9999

		return ServletUtils.buildRs(true, "获取一层ID", homepageService.getFirstFloor(orgId));
	}

	@RequestMapping("/getVideos")
	@ResponseBody
	public String getVideos() {

		return ServletUtils.buildRs(true, "获取视频信息", webcamPostDao.getVideos());
	}

}
