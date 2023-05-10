package cn.cdsoft.modules.homepage.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;

import cn.cdsoft.common.mq.rabbitmq.RabbitMQProducer;
import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.common.utils.ServletUtils;
import cn.cdsoft.common.utils.StringUtils;
import cn.cdsoft.common.web.BaseController;
import cn.cdsoft.modules.homepage.service.AlarmLogService;
import cn.cdsoft.modules.settings.entity.TChannel;
import cn.cdsoft.modules.settings.entity.TDeviceConfig;
import cn.cdsoft.modules.settings.entity.TDeviceDetail;
import cn.cdsoft.modules.settings.service.TChannelService;
import cn.cdsoft.modules.settings.service.TDeviceDetailService;
import cn.cdsoft.modules.sys.utils.UserUtils;

@Controller
@RequestMapping("${adminPath}/alarmController")
public class AlarmController extends BaseController {

	@Autowired
	AlarmLogService alarmLogService;

	@Autowired
	TDeviceDetailService tDeviceDetailService;

	@Autowired
	TChannelService tChannelService;

	@Autowired
	RabbitMQProducer rabbitMQProducer;

	@RequestMapping("/getAllAlarm2")
	@ResponseBody
	public JSONObject getAllAlarm2(String state) {
		List<Map<String, Object>> alarmList = alarmLogService.getAllAlarm(state);
		return ServletUtils.buildJsonRs(true, "实时报警数据", alarmList);
	}

	@RequestMapping("/getAllAlarm9")
	@ResponseBody
	public JSONObject getAllAlarm9(String state) {
		List<Map<String, Object>> alarmList = alarmLogService.getAllAlarm1(state);
		return ServletUtils.buildJsonRs(true, "实时报警数据", alarmList);
	}

	@RequestMapping("/getAllAlarm") // 新写
	@ResponseBody
	public JSONObject getAllAlarm(String state, String alarmTypeFilter) {

		logger.debug("===0000=====" + alarmTypeFilter);
		List<Map<String, Object>> alarmList = alarmLogService.getAllAlarm999(state, alarmTypeFilter);
		return ServletUtils.buildJsonRs(true, "报警数据", alarmList);
	}

	@RequestMapping("/getAlarmTime") //
	@ResponseBody
	public JSONObject getAlarmTime(String chId, String type) {
		return ServletUtils.buildJsonRs(true, "报警数据", alarmLogService.getAlarmTime(chId));
	}

	@ResponseBody
	@RequestMapping(value = { "getAllAlarm0" }) // 无用
	public String getAllAlarm0(String state, HttpServletRequest request, HttpServletResponse response) {

		MapEntity entity = new MapEntity();
		entity.put("state", state);
		Page<MapEntity> page = alarmLogService.findPage(new Page<MapEntity>(request, response), entity);

		return ServletUtils.buildRs(true, "实时报警数据", page);
	}

	@RequestMapping("/confirmAlarm")
	@ResponseBody
	public JSONObject confirmAlarm(String ids) {
		String ds = "";
		String[] idss = ids.split(",");
		for (int i = 0; i < idss.length; i++) {
			String[] idx = idss[i].split("_");
			String id = idx[1];
			if (i != 0) {
				ds += ",";
			}
			ds += id;
		}
		System.out.println(ds);
		try {			
			alarmLogService.confirmAlarmIds(ds);
			alarmLogService.insertAlarmLog(ds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Map<String, Object>> alarmList = alarmLogService.getAlarmByIds(ds);
		
		// TODO
		Channel rmqChannel = null;
		try {
			// 创建一个通道
			rmqChannel = rabbitMQProducer.createChannel();
			for (Map<String, Object> map : alarmList) {
				TDeviceDetail tdd = tDeviceDetailService.get(map.get("devId").toString());
				TChannel tchannel = tChannelService.get(map.get("id").toString());
				System.out.println();
				if (tdd != null && tchannel != null) {
					String message = "{\"mac\":\"" + tdd.getMac() + "\",\"bus_addr\":\"" + tdd.getBusAddr()
							+ "\",\"channels\":{\"" + tchannel.getChNo() + "\":{\"value\":\"0\"}}}";
					// 发送消息到队列中
					rmqChannel.basicPublish("org.10", "dev.command.10.01", null, message.getBytes("UTF-8"));
					logger.debug("/**============================================================**/");
					logger.debug("Producer Send +'" + message + "'  to " + "dev.command.10.01");
					logger.debug("/**============================================================**/");
				}
			}
			// 关闭通道和连接
			rmqChannel.close();
		} catch (Exception e) {
			try {
				rmqChannel.close();
			} catch (IOException e1) {
				logger.debug(e1.toString());
			} catch (java.util.concurrent.TimeoutException e1) {
				logger.debug(e1.toString());
			}
			logger.debug(e.toString());
		}
		return ServletUtils.buildJsonRs(true, "", null);
	}

}
