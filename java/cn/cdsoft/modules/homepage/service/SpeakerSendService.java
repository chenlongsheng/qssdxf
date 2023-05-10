package cn.cdsoft.modules.homepage.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.cdsoft.common.mq.rabbitmq.SpeakerSendMqtt;
import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.common.service.ServiceResult;
import cn.cdsoft.modules.homepage.dao.AlarmLogDao;
import cn.cdsoft.modules.homepage.dao.SpeakerSendDao;
import cn.cdsoft.modules.homepage.vo.ChangeDevSwitchVO;

@Service
@Lazy(false)
public class SpeakerSendService {

	public static Logger logger = LoggerFactory.getLogger(SpeakerSendService.class);

	@Value("${speakerUrl}")
	private String speakerUrl;

	@Value("${loginUrl}")
	private String loginUrl;

	@Value("${sendTtsUrl}")
	private String sendTtsUrl;

	@Value("${userId}")
	private String userId;

	@Value("${onlineTime}")
	private String onlineTime;

	@Value("${repeatTime}")
	private String repeatTime;

	@Autowired
	SpeakerSendMqtt mqtt_logic;

	@Autowired
	SpeakerSendDao speakerSendDao;

	public List<String> getDevice() {
		return speakerSendDao.getDevice();
	}

	@Transactional(readOnly = false)
	public void insertDevice(String id, String online, String orgId, String name) {

		speakerSendDao.insertDevice(id, name, orgId, online);
	}

	@Transactional(readOnly = false)
	public void updateDevice(String id, String online, String name, String updateTime) {

		speakerSendDao.updateDevice(id, online, name, updateTime);
	}

	@Transactional(readOnly = false)
	public void updateDelFlag() {

		speakerSendDao.updateDelFlag();

	}

	@Transactional(readOnly = false)
	public void delDelFlag() {

		speakerSendDao.delDelFlag();

	}

	public List<MapEntity> selectDeviceByVoice(String orgId) {

		List<MapEntity> selectDeviceByVoice = speakerSendDao.selectDeviceByVoice(orgId);
		return selectDeviceByVoice;
	}

	public static String doPost(String url, String token) {
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);

		postMethod.addRequestHeader("accept", "*/*");
		postMethod.addRequestHeader("connection", "Keep-Alive");
		// 设置json格式传送
		postMethod.addRequestHeader("Content-Type", "application/json;charset=utf-8");

		postMethod.addRequestHeader("token", token);
		// 必须设置下面这个Header
		postMethod.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
		// 添加请求参数
//		postMethod.addParameter("userId", json.getString("userId"));

		String res = "";
		try {
			int code = httpClient.executeMethod(postMethod);
			if (code == 200) {
				res = postMethod.getResponseBodyAsString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	@Scheduled(cron = "0 */1 * * * ?")
	public void sendSmokeWarning() throws Exception {

		List<MapEntity> smokeWarning = speakerSendDao.getSmokeWarning();
		for (MapEntity mapEntity : smokeWarning) {

			String realTime = mapEntity.get("realTime").toString();
			String addr = mapEntity.get("addr").toString();
			String devname = mapEntity.get("name").toString();
			String chId = mapEntity.get("chId").toString();
			String realValue = mapEntity.get("realValue").toString();

			System.out.println("realTime   =" + realTime);
			DateFormat parseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date datass = null;
			try {
				datass = (Date) parseDate.parse(realTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			long nowTime = new Date().getTime();
			long minutes = (nowTime - datass.getTime()) / (1000 * 60);
			Long m = minutes % Integer.parseInt(repeatTime);
			if (minutes > Integer.parseInt(repeatTime) && m == 0) {
//				
				String shortMessage = "请注意" + addr + devname + "发生报警，请及时确认";
			
				logger.debug(shortMessage);
 	            mqtt_logic.sendMessage(chId,URLEncoder.encode(shortMessage, "UTF-8"));
			}
		}
	}

	@Scheduled(cron = "0 */10 * * * ?")
	public void checkOnline() throws Exception {// 获取音箱的在线状态

		logger.debug("-----------分割线------------");

		List<String> deviceList = getDevice();
		updateDelFlag();

		JSONObject jsonObject = new JSONObject();
		speakerUrl += "?userId=" + userId;
		logger.debug(speakerUrl);
		String receive = doPost(speakerUrl, null);

		JSONObject jsonObj = JSON.parseObject(receive);
		String success = jsonObj.getString("success");
		JSONArray ja = jsonObj.getJSONArray("data");
//		String ids = "";

		for (int m = 0; m < ja.size(); m++) {
			MapEntity enreceive = JSONObject.parseObject(ja.get(m).toString(), MapEntity.class);

			String id = enreceive.get("id").toString();
			String online = "1";
			String name = enreceive.get("name").toString();

			String ext_param = enreceive.get("ext_param").toString();

			String updateTime = JSONObject.parseObject(ext_param).get("updateTime").toString();

			// logger.debug("updateTime =" + updateTime);
			DateFormat parseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date datass = null;

			try {
				datass = (Date) parseDate.parse(updateTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			long nowTime = new Date().getTime();
			long minutes = (nowTime - datass.getTime()) / (1000 * 60);
			if (minutes > Integer.parseInt(onlineTime)) {
				online = "0";
			} else {
				online = "1";
			}
			if (deviceList.contains(id)) {
				updateDevice(id, online, name, updateTime);
			} else {
				insertDevice(id, online, "10000", name);
			}
		}

	}

}
