/**
 * 
 */
package cn.cdsoft.common.mq.rabbitmq;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctc.wstx.util.StringUtil;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.modules.homepage.dao.SpeakerSendDao;

/**
 * @author admin
 *
 */
@Service
@Transactional(readOnly = true)
public class SpeakerSendMqtt {

	protected org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${speakerUrl}")
	private String speakerUrl;

	@Value("${loginUrl}")
	private String loginUrl;

	@Value("${sendTtsUrl}")
	private String sendTtsUrl;

	@Value("${userId}")
	private String userId;

	protected static String userId0 = "";
	protected static String speakerUrl0 = "";
	protected static String loginUrl0 = "";
	protected static String sendTtsUrl0 = "";

	@Autowired
	SpeakerSendDao SpeakerSendDao;

	protected static String token = "";

	/**
	 * post请求
	 * 
	 * @param url
	 * @param json
	 * @return
	 */
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

	public String getIds(String userId) {

		logger.debug("-----------分割线------------");

		JSONObject jsonObject = new JSONObject();
		speakerUrl += "?userId=" + userId;

		logger.debug("发送的url:  " + speakerUrl);

		String receive = doPost(speakerUrl, null);

		JSONObject jsonObj = JSON.parseObject(receive);
		String success = jsonObj.getString("success");
		JSONArray ja = jsonObj.getJSONArray("data");
		String ids = "";
		for (int m = 0; m < ja.size(); m++) {
			MapEntity enreceive = JSONObject.parseObject(ja.get(m).toString(), MapEntity.class);
			if (m != 0) {
				ids += ",";
			}
			ids += enreceive.get("id").toString();
		}
		return ids;
	}

	public static String login(String url, JSONObject json) {

 
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);

		postMethod.addRequestHeader("accept", "*/*");
		postMethod.addRequestHeader("connection", "Keep-Alive");
		// 设置json格式传送
		postMethod.addRequestHeader("Content-Type", "application/json;charset=utf-8");
		// 必须设置下面这个Header
		postMethod.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
		// 添加请求参数
//  		postMethod.addParameter("username", json.getString("username"));

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

	public String login() {

		JSONObject jsonObject = new JSONObject();
        logger.debug("登入的url: ="+loginUrl);
		String receive = login(loginUrl, jsonObject);

		return receive;

	}

	public String sendMessage(String chId, String ttsText) {

		if (userId0 == "") {
			userId0 = userId;
			speakerUrl0 = speakerUrl;
			loginUrl0 = loginUrl;
			sendTtsUrl0 = sendTtsUrl;

		} else {
			userId = userId0;
			speakerUrl = speakerUrl0;
			loginUrl = loginUrl0;
			sendTtsUrl = sendTtsUrl0;
		}

		List<MapEntity> channelTest = SpeakerSendDao.getChannelTest(chId);

		for (MapEntity mapEntity : channelTest) {
			userId = mapEntity.get("userId").toString();
			speakerUrl = mapEntity.get("speakerUrl").toString();
			loginUrl = mapEntity.get("loginUrl").toString();
			sendTtsUrl = mapEntity.get("sendTtsUrl").toString();
		}

		logger.debug(userId);
		logger.debug(speakerUrl);
		logger.debug(loginUrl);
		logger.debug(sendTtsUrl);

		String url = sendTtsUrl;
		url += "&userId=" + userId;
		String ids = getIds(userId);
		logger.debug("ids===" + ids);

		url += "&deviceIds=" + ids;
		url += "&ttsText=" + ttsText;

		logger.debug(url);

		if (StringUtils.isNotBlank(userId)) {
			String message = doPost(url, token);// 首次发送

			JSONObject jsonObj = JSON.parseObject(message);
			Boolean success = jsonObj.getBoolean("success");
			if (success) {
				return "";
			}
		}

		System.out.println("以下登入----");

		// 以下登入

		String receive = login();
		
		System.out.println(receive);
		JSONObject loginObj = JSON.parseObject(receive);
		Boolean success1 = loginObj.getBoolean("success");

		if (!success1) {
			return "";
		}

		userId = loginObj.getJSONObject("data").getString("id");
		token = loginObj.getJSONObject("data").getString("token");

		url = sendTtsUrl;
		url += "&userId=" + userId;

		ids = getIds(userId);
		logger.debug("ids===" + ids);

		url += "&deviceIds=" + ids;
		url += "&ttsText=" + ttsText;

		logger.debug("第二次url= " + url);
		String sendmessage = doPost(url, token);
		logger.debug("发送返回的message===" + sendmessage);
		return "";
	}

	public static void main(String[] args) {
		
		
		String url = "http://speaker-app.cdsoft.cn/speaker-app/login?username=xcq123&pwd=fc307fb892ae3ca69540d161188bcdb2&phone=18259005635&code=456852";
		
		System.out.println("url");
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);

		postMethod.addRequestHeader("accept", "*/*");
		postMethod.addRequestHeader("connection", "Keep-Alive");
		// 设置json格式传送
		postMethod.addRequestHeader("Content-Type", "application/json;charset=utf-8");
		// 必须设置下面这个Header
		postMethod.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
		// 添加请求参数
//  		postMethod.addParameter("username", json.getString("username"));

		String res = "";
		try {
			int code = httpClient.executeMethod(postMethod);
			if (code == 200) {
				res = postMethod.getResponseBodyAsString();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("收到=="+res);
	 
		
		
		

	//	String message = doPost(
//				"http://speaker-app.cdsoft.cn/speaker-app/api/v1/task/send_tts?ttsVoiceType=1&loginPhone=18259005625&playCount=1&weekday=1,2,3,4,5,6,7&userId=494&deviceIds=1662,1673,1643,1672,1660,1670,1642,1664,1674,1665,1663,1631,1676,1669,1677,1322,1678&ttsText=请注意行政楼1层西强电井强切)发生报警，请及时排查",
//				"494_004c10c47c2448e2966133e5de93bfd5");
	}

}
