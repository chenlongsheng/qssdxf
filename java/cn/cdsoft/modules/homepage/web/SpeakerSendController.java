/**
 * 
 */
package cn.cdsoft.modules.homepage.web;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.utils.ServletUtils;
import cn.cdsoft.modules.homepage.dao.HomepageDao;
import cn.cdsoft.modules.homepage.service.AlarmLogService;
import cn.cdsoft.modules.homepage.service.SpeakerSendService;

/**
 * @author admin
 *
 */
@Controller
@RequestMapping("${adminPath}/home")
public class SpeakerSendController {
//	@RequestMapping("${adminPath}/home")
	@Value("${speakerUrl}")
	private String speakerUrl;

	@Value("${loginUrl}")
	private String loginUrl;

	@Value("${sendTtsUrl}")
	private String sendTtsUrl;

	@Value("${userId}")
	private String userId;

	@Autowired
	SpeakerSendService speakerSendService;

//	http://speaker-app.cdsoft.cn/speaker-app/deviceList?userId=494

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
				System.out.println(res);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
	 

	@ResponseBody
	@RequestMapping(value = { "speakerDeviceList1" })
	public String speakerDeviceList1() {

		System.out.println("-----------分割线------------");

		JSONObject jsonObject = new JSONObject();
		speakerUrl += "?userId=" + userId;
		System.out.println(speakerUrl);
		String receive = doPost(speakerUrl, null);
		return receive;


	}

	@ResponseBody
	@RequestMapping(value = { "speakerDeviceList" })
	public String speakerDeviceList(String orgId) {

//		speakerSendService.delDelFlag();
		return ServletUtils.buildRs(true, "", speakerSendService.selectDeviceByVoice(orgId));

	}

	
	

}
