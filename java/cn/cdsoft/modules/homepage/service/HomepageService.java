package cn.cdsoft.modules.homepage.service;

import cn.cdsoft.common.mq.rabbitmq.RabbitMQProducer;
import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.service.ServiceResult;
import cn.cdsoft.modules.gismap.service.MapService;
import cn.cdsoft.modules.homepage.dao.HomepageDao;
import cn.cdsoft.modules.homepage.vo.ChangeDevSwitchVO;
import cn.cdsoft.modules.homepage.vo.ChangeSwitchVO;
import cn.cdsoft.modules.settings.dao.TOrgDao;
import cn.cdsoft.modules.settings.entity.TDeviceDetail;
import cn.cdsoft.modules.settings.entity.TOrg;
import cn.cdsoft.modules.settings.service.TDeviceDetailService;
import cn.cdsoft.modules.settings.service.TOrgService;
import cn.cdsoft.modules.sys.entity.User;
import cn.cdsoft.modules.sys.utils.UserUtils;
import cn.cdsoft.modules.warm.dao.PdfOrderDao;
import cn.cdsoft.modules.warm.entity.PdfOrder;
import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by ZZUSER on 2019/2/13.
 */
@Service
public class HomepageService {

	@Autowired
	HomepageDao homepageDao;

	@Autowired
	RabbitMQProducer rabbitMQProducer;

	// @Autowired
	// RabbitMQProducer rabbitMQProducer;

	@Autowired
	TDeviceDetailService tDeviceDetailService;

	@Autowired
	PdfOrderDao pdfOrderDao;

	@Autowired
	MapService mapService;

	@Autowired
	TOrgService orgService;

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	public ServiceResult getVisibleOrg2() {
		ServiceResult result = new ServiceResult();
		User user = UserUtils.getUser();
		String id = user.getId();
		List<JSONObject> list = homepageDao.getVisibleOrg(id);
		for (int i = 0; i < list.size(); i++) {
			String orgId = list.get(i).get("id").toString();
			TOrg org = orgService.get(orgId);
			List<JSONObject> alarmList = pdfOrderDao.getAlarmByBuild(org);
			if (alarmList.size() > 0) {
				list.get(i).put("alarm", true);
			}
		}
		result.setData(list);
		result.setSuccess(true);
		return result;
	}

	public ServiceResult getVisibleOrg1() {
		ServiceResult result = new ServiceResult();
		List<JSONObject> list = homepageDao.getVisibleOrg1("");
		for (int i = 0; i < list.size(); i++) {
			Long count = (Long) list.get(i).get("count");
			if (count > 0) {
				list.get(i).put("alarm", true);
			}
		}
		result.setData(list);
		result.setSuccess(true);
		return result;
	}

	public ServiceResult getDevByType(int type) {
		ServiceResult result = new ServiceResult();
		if (type == 4) {
			JSONObject jsonObject = mapService.showConfig("7601");
			result.setData(jsonObject.get("resultList"));
			result.setSuccess(true);
			return result;
		}
		User user = UserUtils.getUser();
		Map map = new HashMap();
		map.put("userId", user.getId());

		if (type == 3)
			map.put("type", type);
		if (type == 6)
			map.put("type", type);
		if (type == 7)
			map.put("type", type);
		if (type == 8)
			map.put("type", type);

		List<JSONObject> list = homepageDao.getDevByType(map);
		// List<JSONObject> list = new ArrayList<JSONObject>();
		if (type == 3) {
			JSONObject obj = new JSONObject();
			obj.put("id", "1");
			obj.put("name", "烟感1");
			obj.put("orgId", 1);
			obj.put("devType", 2);
			obj.put("typeId", 2);
			obj.put("coordsX", 119.011355);
			obj.put("coordsY", 25.4455);

			JSONObject obj2 = new JSONObject();
			obj2.put("id", "2");
			obj2.put("name", "烟感2");
			obj2.put("orgId", 2);
			obj2.put("devType", 2);
			obj2.put("typeId", 2);
			obj2.put("coordsX", 119.018);
			obj2.put("coordsY", 25.446727);

			JSONObject obj3 = new JSONObject();
			obj2.put("id", "3");
			obj2.put("name", "烟感3");
			obj2.put("orgId", 2);
			obj2.put("devType", 2);
			obj2.put("typeId", 2);
			obj2.put("coordsX", 119.0105);
			obj2.put("coordsY", 25.4469);

			JSONObject obj4 = new JSONObject();
			obj4.put("id", "4");
			obj4.put("name", "烟感4");
			obj4.put("orgId", 2);
			obj4.put("devType", 2);
			obj4.put("typeId", 2);
			obj4.put("coordsX", 119.007);
			obj4.put("coordsY", 25.4493);

			JSONObject obj5 = new JSONObject();
			obj5.put("id", "5");
			obj5.put("name", "烟感5");
			obj5.put("orgId", 2);
			obj5.put("devType", 2);
			obj5.put("typeId", 2);
			obj5.put("coordsX", 119.0105);
			obj5.put("coordsY", 25.4478);

			list.add(obj);
			list.add(obj2);
			list.add(obj3);
			list.add(obj4);
			list.add(obj5);
		}
		if (type == 6) {
			JSONObject obj = new JSONObject();
			obj.put("", "");
		}
		if (type == 7) {
			JSONObject obj = new JSONObject();
			obj.put("", "");
		}
		if (type == 8) {
			JSONObject obj = new JSONObject();
			obj.put("", "");
		}

		result.setData(list);
		result.setSuccess(true);
		return result;
	}

	public ServiceResult getFloorByBuilding(String buildingId) {
		ServiceResult result = new ServiceResult();
		List<JSONObject> list = homepageDao.getFloorByBuilding(buildingId);
		result.setData(list);
		result.setSuccess(true);
		return result;
	}

	public ServiceResult getTypeByFloor(String floors) {
		ServiceResult result = new ServiceResult();
		String[] arr = floors.split(",");
		List<JSONObject> list = homepageDao.getTypeByFloor(arr);
		result.setData(list);
		result.setSuccess(true);
		return result;
	}

	@Transactional(readOnly = false)
	public ServiceResult changeSwitch(ChangeSwitchVO changeSwitchVO) {
		ServiceResult result = new ServiceResult();
		String[] arr = changeSwitchVO.getFloors().split(",");
		changeSwitchVO.setArray(arr);
		List<JSONObject> list = homepageDao.getDevByFloor(changeSwitchVO);

		com.rabbitmq.client.Channel rmqChannel = null;
		try {
			// 创建一个通道
			rmqChannel = rabbitMQProducer.createChannel();
			// String message =
			// "{\"dev_id\":"+channel.getDevId()+",\"ch_id\":"+channel.getId()+",\"cmd_type\":1,\"seq_uuid\":\""+IdGen.uuid()+"\",\"params\":{\"off_on\":"+1+",\"open_minute\":"+time+"}}";
			// String message =
			// "{\"mac\":\""+tDeviceDetail.getMac()+"\",\"channels\":{\""+tChannel.getChNo()+"\":{\"value\":\"1\"},\""+(switchChannel.getChNo()+12)+"\":{\"value\":\""+minute+"\"}}}";
			for (int i = 0; i < list.size(); i++) {
				TDeviceDetail tDeviceDetail = tDeviceDetailService.get(String.valueOf(list.get(i)));
				String message = "{\"mac\\\":\"" + (tDeviceDetail.getMac() == null ? "" : tDeviceDetail.getMac())
						+ "\",\"imei\":\"" + (tDeviceDetail.getImei() == null ? "" : tDeviceDetail.getImei())
						+ "\",\"outer_id\":\"" + (tDeviceDetail.getOuterId() == null ? "" : tDeviceDetail.getOuterId())
						+ "\",\"channels\":{\"" + 1 + "\":{\"value\":\"" + changeSwitchVO.getValue() + "\"}}}";
				// 发送消息到队列中
				rmqChannel.basicPublish("org.10", "dev.command.10.01", null, message.getBytes("UTF-8"));
				ChangeDevSwitchVO vo = new ChangeDevSwitchVO();
				vo.setDevId(String.valueOf(list.get(i)));
				vo.setValue(changeSwitchVO.getValue());
				homepageDao.changeSwitch(vo);
				System.out.println("/**============================================================**/");
				System.out.println("Producer Send +'" + message + "'  to " + "dev.command.10.01");
				System.out.println("/**============================================================**/");
				logger.debug("/**============================================================**/");
				logger.debug("Producer Send +'" + message + "'  to " + "dev.command.10.01");
				logger.debug("/**============================================================**/");
			}
			// 关闭通道和连接
			rmqChannel.close();
		} catch (Exception e) {
			try {
				rmqChannel.close();
			} catch (IOException e1) {
				logger.error(e1.toString());
				e1.printStackTrace();
			} catch (TimeoutException e1) {
				logger.error(e1.toString());
				e1.printStackTrace();
			}
		}
		// result.setData(list);
		result.setSuccess(true);
		return result;
	}

	@Autowired
	TOrgDao tOrgDao;

	public ServiceResult getAllDevByFloor(String orgId, String filePath) {

		String path = filePath + "/static_modules/emap_upload/";
		String fiPath = filePath + "/static_modules/device/";
		ServiceResult result = new ServiceResult();
		TOrg tOrg = tOrgDao.get(orgId);

		if (tOrg.getType() != 7) {
			JSONObject jsonObject = homepageDao.getFirstFloor(orgId);
			if (jsonObject != null) {
				orgId = String.valueOf(jsonObject.get("id"));
			}
		}

		List<JSONObject> list = homepageDao.getAllDevByFloor(orgId);

		for (int i = 0; i < list.size(); i++) {
			List<JSONObject> list1 = homepageDao.getNewDataByDev(String.valueOf(list.get(i).get("id")));
			String iconPath = fiPath + String.valueOf(list.get(i).get("iconSkin"));
			String warnIconSkin = fiPath + String.valueOf(list.get(i).get("warnIconSkin"));

			list.get(i).put("iconSkin", iconPath);// 添加小图标
			list.get(i).put("warnIconSkin", warnIconSkin);

			list.get(i).put("newData", list1);
			System.out.println(list1.size());
			if (list1.size() == 0) {
				list.get(i).put("state", "0");
			}

			String wa = "0";
			for (int k = 0; k < list1.size(); k++) {
				String warn = String.valueOf(list1.get(k).get("warn"));
				Integer historyValue = Integer.parseInt(String.valueOf(list1.get(k).get("historyValue")));

				int param0 = Integer.parseInt(String.valueOf(list1.get(k).get("param0")));
				int param1 = Integer.parseInt(String.valueOf(list1.get(k).get("param1")));
				int param2 = Integer.parseInt(String.valueOf(list1.get(k).get("param2")));
				double key = (float) (historyValue - param2) / param1;
				System.out.println("double------" + key);
				BigDecimal bg = new BigDecimal(key);
				double history = bg.setScale(param0, BigDecimal.ROUND_HALF_UP).doubleValue();
				System.out.println("历史值=====" + history);

				list1.get(k).put("historyValue", history);
				System.out.println(warn);
				if (!"0".equals(warn)) {
					wa = warn;
				}
				list.get(i).put("state", wa);
			}
			// 获取视频通道
			List<JSONObject> video = homepageDao.getViedioByDev(String.valueOf(list.get(i).get("id")));
			list.get(i).put("video", video);
			if (video.size() > 0) {
				list.get(i).put("state", "0");
				String name = String.valueOf(list.get(i).get("name"));
//				String chName = String.valueOf(list.get(i).get("chName"));
//				list.get(i).put("name", name + "(" + chName + ")");
			}
		}
		tOrg = tOrgDao.get(orgId);
		String image = tOrg.getImage();
		image = path + image;

		Map resultMap = new HashMap();
		resultMap.put("data", list);
		resultMap.put("image", image);
		result.setData(resultMap);
		result.setSuccess(true);
		return result;
	}

	public String getFirstFloor(String orgId) {// 新写9999

		TOrg tOrg = tOrgDao.get(orgId);
		if (tOrg.getType() != 7) {
			JSONObject jsonObject = homepageDao.getFirstFloor(orgId);
			if (jsonObject != null) {
				orgId = String.valueOf(jsonObject.get("id"));
			}
		}
		return orgId;
	}

	public MapEntity getChannelByFloor(String orgId, String filePath) {// 新写9999

		String path = filePath + "/static_modules/emap_upload/";
		String fiPath = filePath + "/static_modules/channel/";

		MapEntity entity = new MapEntity();
		TOrg tOrg = tOrgDao.get(orgId);
		if (tOrg.getType() != 7) {
			JSONObject jsonObject = homepageDao.getFirstFloor(orgId);
			if (jsonObject != null) {
				orgId = String.valueOf(jsonObject.get("id"));
			}
		}
		tOrg = tOrgDao.get(orgId);
		String image = path + tOrg.getImage();

		entity.put("image", image);
		entity.put("data", homepageDao.getChannelByFloor(orgId, fiPath));
		return entity;
	}

	public ServiceResult getAllChannelByFloor(String orgId, String filePath) {

		String path = filePath + "/static_modules/emap_upload/";
		String fiPath = filePath + "/static_modules/channel/";
		ServiceResult result = new ServiceResult();
		TOrg tOrg = tOrgDao.get(orgId);

		if (tOrg.getType() != 7) {
			JSONObject jsonObject = homepageDao.getFirstFloor(orgId);
			if (jsonObject != null) {
				orgId = String.valueOf(jsonObject.get("id"));
			}
		}

		List<JSONObject> list = homepageDao.getAllChannelByFloor(orgId);

		for (int i = 0; i < list.size(); i++) {
			List<JSONObject> list1 = homepageDao.getChannelRealData(list.get(i).get("id").toString());
			String iconPath = fiPath + String.valueOf(list.get(i).get("iconSkin"));
			String warnIconSkin = fiPath + String.valueOf(list.get(i).get("warnIconSkin"));
			String offlineIconSkin = fiPath + String.valueOf(list.get(i).get("offlineIconSkin"));

			list.get(i).put("iconSkin", iconPath);// 添加小图标
			list.get(i).put("warnIconSkin", warnIconSkin);
			list.get(i).put("offlineIconSkin", offlineIconSkin);

			list.get(i).put("newData", list1);
			System.out.println(list1.size());
			if (list1.size() == 0) {
				list.get(i).put("state", "0");
			}
			String wa = "0";
			for (int k = 0; k < list1.size(); k++) {
				String warn = String.valueOf(list1.get(k).get("warn"));
				int historyValue = Integer.parseInt(String.valueOf(list1.get(k).get("historyValue")));
				int param0 = Integer.parseInt(String.valueOf(list1.get(k).get("param0")));
				int param1 = Integer.parseInt(String.valueOf(list1.get(k).get("param1")));
				int param2 = Integer.parseInt(String.valueOf(list1.get(k).get("param2")));

				double key = (float) (historyValue - param2) / param1;
				System.out.println("double------" + key);
				BigDecimal bg = new BigDecimal(key);
				double history = bg.setScale(param0, BigDecimal.ROUND_HALF_UP).doubleValue();
				System.out.println("历史值=====" + history);
				list1.get(k).put("historyValue", history);
//				System.out.println(warn);
//				if (!"0".equals(warn)) {
//					wa = warn;
//				}
				list.get(i).put("state", historyValue);
			}
			// 获取视频通道
			List<JSONObject> video = homepageDao.getVideoByChannelId(String.valueOf(list.get(i).get("id")));
			list.get(i).put("video", video);
			if (video.size() > 0) {
				list.get(i).put("state", "0");
				String name = String.valueOf(list.get(i).get("name"));
//				String chName = String.valueOf(list.get(i).get("chName"));
//				list.get(i).put("name", name + "(" + chName + ")");
			}
		}
		tOrg = tOrgDao.get(orgId);
		String image = tOrg.getImage();
		image = path + image;

		Map resultMap = new HashMap();
		resultMap.put("data", list);
		resultMap.put("image", image);
		result.setData(resultMap);
		result.setSuccess(true);
		return result;
	}

	public ServiceResult getAlarm() {
		ServiceResult result = new ServiceResult();
		User user = UserUtils.getUser();
		// 获取可见楼栋
		List<JSONObject> orgList = homepageDao.getVisibleOrg(user.getId());
		PdfOrder pdfOrder = new PdfOrder();
		String[] arr = new String[orgList.size()];
		for (int i = 0; i < orgList.size(); i++) {
			arr[i] = String.valueOf(orgList.get(i).get("id"));
		}
		pdfOrder.setArr(arr);
		pdfOrder.setState(0);
		// 获取报警工单
		List<Map> orderList = pdfOrderDao.findOrder(pdfOrder);

		// 根据楼栋进行分组
		Map<String, List> resultMap = new HashMap();
		for (int i = 0; i < orderList.size(); i++) {
			if (resultMap.get(orderList.get(i).get("buildingName")) == null) {
				List list = new ArrayList();
				list.add(orderList.get(i));
				resultMap.put(String.valueOf(orderList.get(i).get("buildingName")), list);
			} else {
				List list = resultMap.get(orderList.get(i).get("buildingName"));
				list.add(orderList.get(i));
				resultMap.put(String.valueOf(orderList.get(i).get("buildingName")), list);
			}
		}
		result.setData(resultMap);
		result.setSuccess(true);
		return result;
	}

	@Transactional(readOnly = false)
	public ServiceResult changeDevSwitch(ChangeDevSwitchVO vo) {
		ServiceResult result = new ServiceResult();
		com.rabbitmq.client.Channel rmqChannel = null;
		try {
			// 创建一个通道
			rmqChannel = rabbitMQProducer.createChannel();
			// String message =
			// "{\"dev_id\":"+channel.getDevId()+",\"ch_id\":"+channel.getId()+",\"cmd_type\":1,\"seq_uuid\":\""+IdGen.uuid()+"\",\"params\":{\"off_on\":"+1+",\"open_minute\":"+time+"}}";
			// String message =
			// "{\"mac\":\""+tDeviceDetail.getMac()+"\",\"channels\":{\""+tChannel.getChNo()+"\":{\"value\":\"1\"},\""+(switchChannel.getChNo()+12)+"\":{\"value\":\""+minute+"\"}}}";
			TDeviceDetail tDeviceDetail = tDeviceDetailService.get(vo.getDevId());
			String message = "{\"mac\\\":\"" + (tDeviceDetail.getMac() == null ? "" : tDeviceDetail.getMac())
					+ "\",\"imei\":\"" + (tDeviceDetail.getImei() == null ? "" : tDeviceDetail.getImei())
					+ "\",\"outer_id\":\"" + (tDeviceDetail.getOuterId() == null ? "" : tDeviceDetail.getOuterId())
					+ "\",\"channels\":{\"" + 1 + "\":{\"value\":\"" + vo.getValue() + "\"}}}";
			// 发送消息到队列中
			rmqChannel.basicPublish("org.10", "dev.command.10.01", null, message.getBytes("UTF-8"));
			homepageDao.changeSwitch(vo);
			System.out.println("/**============================================================**/");
			System.out.println("Producer Send +'" + message + "'  to " + "dev.command.10.01");
			System.out.println("/**============================================================**/");
			logger.debug("/**============================================================**/");
			logger.debug("Producer Send +'" + message + "'  to " + "dev.command.10.01");
			logger.debug("/**============================================================**/");
			// 关闭通道和连接
			rmqChannel.close();
		} catch (Exception e) {
			try {
				rmqChannel.close();
			} catch (IOException e1) {
				logger.error(e1.toString());
				e1.printStackTrace();
			} catch (TimeoutException e1) {
				logger.error(e1.toString());
				e1.printStackTrace();
			}
		}
		// result.setData(list);
		result.setSuccess(true);
		return result;
	}

	public ServiceResult simulateAlarm(String devId) {
		ServiceResult result = new ServiceResult();
		List<JSONObject> list = homepageDao.getViewdeo(devId);
		result.setData(list);
		result.setSuccess(true);
		return result;
	}

	public ServiceResult getFireHost(String buildId) {
		ServiceResult result = new ServiceResult();
		List<JSONObject> fireHostList = homepageDao.getFireHost(buildId);
		for (int i = 0; i < fireHostList.size(); i++) {
			List<JSONObject> chList = homepageDao.getChByDev(String.valueOf(fireHostList.get(i).get("id")));

			for (int j = 0; j < chList.size(); j++) {
				Integer status = homepageDao.getChStatus(String.valueOf(chList.get(j).get("id")));
				if (status != null) {
					if (status == 3) {
						chList.get(j).put("alarm", false);
					} else {
						chList.get(j).put("alarm", true);
					}
				} else {
					chList.get(j).put("alarm", false);
				}
			}
			fireHostList.get(i).put("chList", chList);
			// 获取绑定在消控主机下的设备
			List<JSONObject> devList = homepageDao.getDevByFireHost(String.valueOf(fireHostList.get(i).get("id")));
			for (int j = 0; j < devList.size(); j++) { // 设备的报警状态
				String id = String.valueOf(devList.get(j).get("id"));
				List<JSONObject> list1 = homepageDao.getNewDataByDev(id);
				if (list1.size() == 0) {
					devList.get(j).put("state", "0");// 真实数据不存在时正常
				}
				String wa = "0";
				for (int k = 0; k < list1.size(); k++) {
					String warn = String.valueOf(list1.get(k).get("warn"));
					if (!"0".equals(warn)) {
						wa = warn;
					}
					devList.get(j).put("state", wa);
				}
			}
			List<JSONObject> devList1 = new ArrayList<JSONObject>();
			// 按state排序
			for (JSONObject json : devList) {
				String state = String.valueOf(json.get("state"));
				if (!state.equals("0")) {
					devList1.add(json);
				}
			}
			for (JSONObject json : devList) {
				String state = String.valueOf(json.get("state"));
				if (state.equals("0")) {
					devList1.add(json);
				}
			}
			fireHostList.get(i).put("devList", devList1);
		}
		result.setData(fireHostList);
		result.setSuccess(true);
		return result;
	}

	public ServiceResult getBuildingAlarmList(String buildId) {
		ServiceResult result = new ServiceResult();
		List<JSONObject> alarmList = homepageDao.getBuildingAlarmList(buildId);
		result.setData(alarmList);
		result.setSuccess(true);
		return result;
	}

	/**
	 * 获取消防主机状态999-- 新写接口
	 * 
	 * @param orgId
	 * @return
	 */
	public MapEntity getMainframeList(String orgId) {
		MapEntity entity = new MapEntity();
		entity.put("getMainDevList", homepageDao.getMainDevList(orgId));// 烟感
		entity.put("getMainframeList", homepageDao.getMainframeList(orgId));// 水压
		return entity;
	}

	public ServiceResult getBuildingIdByChannelId(String chId) {
		ServiceResult result = new ServiceResult();
		List<JSONObject> alarmList = homepageDao.getBuildingIdByChannelId(chId);
		result.setData(alarmList);
		result.setSuccess(true);
		return result;
	}


	
}
