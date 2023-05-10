package cn.cdsoft.modules.app.web;

import cn.cdsoft.common.mq.rabbitmq.RabbitMQProducer;
import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.utils.ServletUtils;
import cn.cdsoft.common.utils.StringUtils;
import cn.cdsoft.common.web.BaseController;
import cn.cdsoft.modules.app.service.AppService;
import cn.cdsoft.modules.homepage.dao.HomepageDao;
import cn.cdsoft.modules.homepage.service.AlarmLogService;
import cn.cdsoft.modules.settings.entity.TChannel;
import cn.cdsoft.modules.settings.entity.TDeviceDetail;
import cn.cdsoft.modules.settings.service.TChannelService;
import cn.cdsoft.modules.settings.service.TDeviceDetailService;
import cn.cdsoft.modules.sys.entity.Area;
import cn.cdsoft.modules.sys.security.SystemAuthorizingRealm.Principal;
import cn.cdsoft.modules.sys.service.AreaService;
import cn.cdsoft.modules.sys.utils.UserUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.omg.PortableServer.Servant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rabbitmq.client.Channel;

/**
 * Created by ZZUSER on 2018/12/7.
 */
@Controller
@RequestMapping("/app")
public class AppController extends BaseController {

	@Autowired
	private AppService appService;

	@Autowired
	AlarmLogService alarmLogService;

	@Autowired
	HomepageDao homepageDao;

	@Autowired
	TDeviceDetailService tDeviceDetailService;

	@Autowired
	TChannelService tChannelService;

	@Autowired
	RabbitMQProducer rabbitMQProducer;

//	${adminPath}
	@RequestMapping(value = "/checkToken")
	@ResponseBody
	public String checkToken(HttpServletRequest request, HttpServletResponse response) {

		JSONObject jsonObject = new JSONObject();
		Principal principal = UserUtils.getPrincipal();
		if (principal != null) {
			jsonObject.put("token", true);
			SecurityUtils.getSubject().logout();
		} else {
			jsonObject.put("token", false);
		}
		return cn.cdsoft.common.utils.ServletUtils.buildRs(true, "登出", jsonObject);
	}

	@ResponseBody
	@RequestMapping(value = "/getOrgList")
	public String getOrgList() {// 获取莆田学院建筑
		// 获取建筑
		return cn.cdsoft.common.utils.ServletUtils.buildRs(true, "获取师大学院建筑", appService.getOrgList());
	}

	@ResponseBody
	@RequestMapping(value = "/getAlarmLog")
	public String getAlarmLog(String orgId) {
		// 获取建筑
		return cn.cdsoft.common.utils.ServletUtils.buildRs(true, "获取师大报警列表", appService.getAlarmLog(orgId));
	}

	@ResponseBody
	@RequestMapping(value = "/updateAlarmBychId1")
	public String updateAlarmBychId1(String chId) {
		// 确定为已经
		try {
			appService.updateAlarmBychId(chId);
		} catch (Exception e) {
			e.printStackTrace();
			return cn.cdsoft.common.utils.ServletUtils.buildRs(false, "更新已报警失败", "");
		}
		return cn.cdsoft.common.utils.ServletUtils.buildRs(true, "更新已报警成功", "");
	}

	@RequestMapping("/updateAlarmBychId")
	@ResponseBody
	public String updateAlarmBychId(String chId) {

		try {
			appService.updateAlarmBychId(chId);
			alarmLogService.insertAlarmLog(chId);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// TODO
		Channel rmqChannel = null;
		try {
			// 创建一个通道
			rmqChannel = rabbitMQProducer.createChannel();
			TChannel tchannel = tChannelService.get(chId);
			System.out.println(tchannel.getDevId() + "==============");
			TDeviceDetail tdd = tDeviceDetailService.get(tchannel.getDevId() + "");
			if (tdd != null && tchannel != null) {
				String message = "{\"mac\":\"" + tdd.getMac() + "\",\"bus_addr\":\"" + tdd.getBusAddr()
						+ "\",\"channels\":{\"" + tchannel.getChNo() + "\":{\"value\":\"0\"}}}";
				// 发送消息到队列中
				rmqChannel.basicPublish("org.10", "dev.command.10.01", null, message.getBytes("UTF-8"));
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
				logger.debug(e1.toString());
			} catch (java.util.concurrent.TimeoutException e1) {
				logger.debug(e1.toString());
			}
			logger.debug(e.toString());
			return cn.cdsoft.common.utils.ServletUtils.buildRs(false, "更新已报警失败", "");
		}
		return cn.cdsoft.common.utils.ServletUtils.buildRs(true, "更新已报警成功", "");
	}

	@RequestMapping("/getDeviceDetails")
	@ResponseBody
	public String getDeviceDetails() {

		return ServletUtils.buildRs(true, "设备实时详情", homepageDao.getDeviceDetails());
	}

}
