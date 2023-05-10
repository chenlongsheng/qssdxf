/**
 * 
 */
package cn.cdsoft.modules.homepage.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctc.wstx.util.StringUtil;
import com.google.gson.JsonObject;

import cn.cdsoft.common.mq.rabbitmq.mqtt.MqttProducer;
import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.utils.ServletUtils;
import cn.cdsoft.modules.homepage.dao.WebcamPostDao;
import cn.cdsoft.modules.homepage.service.AlarmLogService;

/**
 * @author admin
 *
 */
@Controller
@RequestMapping("")
public class WebcamPostController {

	public Logger logger = LoggerFactory.getLogger(WebcamPostController.class);
	@Autowired
	WebcamPostDao webcamPostDao;

	@Value("${waitingTime}")
	private String waitingTime;

	public static MapEntity entity = new MapEntity();

	@ResponseBody
	@RequestMapping(value = { "updataWebcam" })
	public String updataWebcam(@RequestBody String data) throws ParseException {
		// {"event":0,"eventid":"2022-01-11_15-04-23_000","motostatus":0,"personcount":1,"dooropen":2,"devid":"B7B1D3942680619EDAEBA1E9","nonce":"vmueagjuanqfbjtfuaorvztjvabixwsu","timestamp":1641884663556,"sign":"smartcamera"}
		logger.debug(data);

		JSONObject jsonObj = JSON.parseObject(data);
		String personcount = jsonObj.getString("personcount");

		String sn = jsonObj.getString("sn");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logger.debug(formatter.format(new Date()));
		String warn = "0";
		if (Integer.parseInt(personcount) > 0) { // 正常
			warn = "0";
			webcamPostDao.updataWebcam(personcount, warn, sn);
			Date date = new Date();
			entity.put("time_" + sn, formatter.format(date));
			logger.debug("time_" + sn + ":  " + formatter.format(date));

		} else {// 报警
			warn = "1";
			Date date = new Date();
			entity.put("newTime_" + sn, formatter.format(date));

			String time = (String) entity.get("time_" + sn);
			if (StringUtils.isBlank(time)) {
				time = formatter.format(new Date());
				entity.put("time_" + sn, time);
				System.out.println(time);
			}

			Date d1 = formatter.parse(time);
			Date d2 = formatter.parse(entity.get("newTime_" + sn).toString());
			System.out.println(d1);
			System.out.println(d2);

			if (((d2.getTime() - d1.getTime()) / (60 * 1000)) % 60 >= Integer.parseInt(waitingTime)) {
				webcamPostDao.updataWebcam(personcount, warn, sn);
			}
		}
		return ServletUtils.buildRs(true, "", "");
	}

	@RequestMapping("/getVideos")
	@ResponseBody
	public String getVideos() {

		return ServletUtils.buildRs(true, "获取视频信息", webcamPostDao.getVideos());
	}

}
