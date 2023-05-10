/**
 * 
 */
package cn.cdsoft.modules.app.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.web.BaseController;
import cn.cdsoft.modules.settings.entity.TDevice;
import cn.cdsoft.modules.warm.dao.PdfOrderDao;
import cn.cdsoft.modules.warm.entity.PdfOrder;
import cn.cdsoft.modules.warm.service.PdfOrderService;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

/**
 * @author admin
 *
 */
@Controller
@RequestMapping("/app")
public class PushController extends BaseController {

	@Autowired
	PdfOrderDao pdfOrderDao;

	// 设置好账号的app_key和masterSecret是必须的
//    private static String APP_KEY = "6e6d468e0382b88cb60b3c7d";
//    private static String MASTER_SECRET = "ebcecbb396571be010a4167c";

	private static String APP_KEY = "572ad7432243bcfb4b04bae8";// 莆田消防
	private static String MASTER_SECRET = "94b2b13b06241b16e8a3bc1e";

	// 极光推送>>Android
	// Map<String, String> parm是我自己传过来的参数,可以自定义参数
	public static void jpushAndroid(Map<String, String> parm) {

		// 创建JPushClient(极光推送的实例)
		JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
		// 推送的关键,构造一个payload
		PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.android())// 指定android平台的用户
				.setAudience(Audience.all())// 你项目中的所有用户
//                .setAudience(Audience.alias(parm.get("alias")))//设置别名发送,单发，点对点方式
				// .setAudience(Audience.tag("tag1"))//设置按标签发送，相当于群发
//                .setAudience(Audience.registrationId(parm.get("id")))//registrationId指定用户
				.setNotification(Notification.android(parm.get("msg"), parm.get("title"), parm)) // 发送内容
				.setOptions(Options.newBuilder().setApnsProduction(true).setTimeToLive(7200).build())
				// apnProduction指定开发环境 true为生产模式 false 为测试模式 (android不区分模式,ios区分模式) 不用设置也没关系
				// TimeToLive 两个小时的缓存时间
				.setMessage(Message.content(parm.get("msg")))// 自定义信息
				.build();
		try {
			PushResult pu = jpushClient.sendPush(payload);
			System.out.println(pu.toString());
		} catch (APIConnectionException e) {
			e.printStackTrace();
		} catch (APIRequestException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(JSONObject devAlarm) {

		PdfOrder pdfOrder = new PdfOrder();
		pdfOrder.setDevId(devAlarm.getString("dev_id"));
		String name = "";
		String addr = "";
		String orgName = "";
		try {
			String chId = devAlarm.getString("ch_id");
			logger.debug("=======通道Id=======" + chId);

			MapEntity channel = pdfOrderDao.getChannel(chId);
			name = (String) channel.get("name");
			addr = (String) channel.get("addr");
			orgName = (String) channel.get("orgName");
		} catch (Exception e) {
			logger.debug("sendMessage catch Excpetion");
			logger.debug(e.toString());
		}
		JSONArray array = devAlarm.getJSONArray("items");
		pdfOrder.setPrec(array.getJSONObject(0).getString("alarm_info"));
		pdfOrder.setAlarmLevel(array.getJSONObject(0).getInteger("alarm_level"));
		pdfOrder.setAlarmTime(array.getJSONObject(0).getString("occur_time"));

		Map<String, String> parm = new HashMap<String, String>();
		// 设置提示信息,内容是文章标题
		parm.put("msg", "福建师大消防" + addr + name + "报警");
		parm.put("title", "报警通知");
		parm.put("alias", "福建师大消防报警推送平台");
		jpushAndroid(parm);

	}

	public static void main(String[] args) {
		// 设置推送参数
		// 这里可以自定义推送参数了
		Map<String, String> parm = new HashMap<String, String>();
		// 设置提示信息,内容是文章标题
		parm.put("msg", "福建师大消防消息极光推动测试");
		parm.put("title", "福建师大消防");
		parm.put("alias", "川大");
		jpushAndroid(parm);
	}

	// 极光推送>>All所有平台
//    public static void jpushAll(Map<String, String> parm) {
// 
//        //创建JPushClient
//        JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
//        //创建option
//        PushPayload payload = PushPayload.newBuilder()
//                .setPlatform(Platform.all())  //所有平台的用户
//                .setAudience(Audience.all())//registrationId指定用户
//                .setNotification(Notification.newBuilder()
//                        .addPlatformNotification(IosNotification.newBuilder() //发送ios
//                                .setAlert(parm.get("msg")) //消息体
//                                .setBadge(+1)
//                                .setSound("happy") //ios提示音
//                                .addExtras(parm) //附加参数
//                                .build())
//                        .addPlatformNotification(AndroidNotification.newBuilder() //发送android
//                                .addExtras(parm) //附加参数
//                                .setAlert(parm.get("msg")) //消息体
//                                .build())
//                        .build())
//                .setOptions(Options.newBuilder().setApnsProduction(true).build())//指定开发环境 true为生产模式 false 为测试模式 (android不区分模式,ios区分模式)
//                .setMessage(Message.newBuilder().setMsgContent(parm.get("msg")).addExtras(parm).build())//自定义信息
//                .build();
//        try {
//            PushResult pu = jpushClient.sendPush(payload);
//            System.out.println(pu.toString());
//        } catch (APIConnectionException e) {
//            e.printStackTrace();
//        } catch (APIRequestException e) {
//            e.printStackTrace();
//        }
//    }

}
