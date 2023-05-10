package cn.cdsoft.common.mq.rabbitmq.mqtt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.utils.DateUtils;
import cn.cdsoft.common.utils.ServletUtils;
import freemarker.template.utility.DateUtil;

public class MqttProducer {

	public Logger logger = LoggerFactory.getLogger(MqttProducer.class);

	@Resource
	private MqttPahoMessageHandler mqttHandler;

//	MqttConnectOptions options = new MqttConnectOptions();
//	options.setMaxInflight(1000);

	private static String getLocalMac(InetAddress ia) throws SocketException {
		// TODO Auto-generated method stub
		// 获取网卡，获取地址
		byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		System.out.println("mac数组长度：" + mac.length);
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			// 字节转换为整数
			int temp = mac[i] & 0xff;
			String str = Integer.toHexString(temp);
			System.out.println("每8位:" + str);
			if (str.length() == 1) {
				sb.append("0" + str);
			} else {
				sb.append(str);
			}
		}
		System.out.println("本机MAC地址:" + sb.toString().toUpperCase());
		return sb.toString();
	}

	public String sendMessage(String devId, String message) {

//		String json4T = "{\"command\": 12,\"Tel\": \"18259005635\",\"Sms\": \"短信测试\"}";

		String json4T = "{\"command\": 14,\"PlayTTS\":\"" + message + "\"" + "}";

		System.out.println("json4T:  " + json4T);

		// 构建消息
		Message<String> messages = MessageBuilder.withPayload(json4T).setHeader(MqttHeaders.TOPIC, "/imei/" + devId)
				.build();
		// 发送消息
		mqttHandler.handleMessage(messages);
		System.out.println("message_content:" + messages);
		return "";

	}

//	public static String sendMobileMessage(String phone, String mobanMessage) {
//		MapEntity entity = new MapEntity();
//
//		System.out.println(phone + "-----------phone");
//
//		entity.put("messageCode", mobanMessage);
//		// 创建StringBuffer对象用来操作字符串
//		StringBuffer sb = new StringBuffer(
//				"http://cdsoft.cn/aliyun-java-sdk/sendCdsoftVerifyCode/?templdateCcode=SMS_169898632");
//		// 向StringBuffer追加用户名
//		sb.append("&phone=" + phone);// 在此申请企信通uid，并进行配置用户名
//		// 向StringBuffer追加密码（密码采用MD5 32位 小写）
//		sb.append("&code=" + mobanMessage);
//		// 创建url对象
//		try {
//			URL url = new URL(sb.toString());
//			// 打开url连接
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			// 设置url请求方式 ‘get’ 或者 ‘post’
//			connection.setRequestMethod("POST");
//			// 发送
//			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
//			// 返回发送结果
//			String inputline = in.readLine();
//		} catch (IOException e) {
////			e.printStackTrace();
//		}
//		return ServletUtils.buildRs(true, "手机:" + phone, entity);
//	}

	public String sendMobileMessage(String phone, String address, String device) {

		MapEntity entity = new MapEntity();

		System.out.println(phone + "-----------phone");
		Random random = new Random();
		Long now = new Date().getTime() / 1000;

		// 创建StringBuffer对象用来操作字符串
		StringBuffer sb = new StringBuffer("https://cdsoft.cn/aliyun-java-sdk/sendCdsoftFireAlarm/?");
		// 向StringBuffer追加用户名
		sb.append("phone=" + phone);// 在此申请企信通uid，并进行配置用户名
		try {

			sb.append("&address=" + URLEncoder.encode(address, "utf-8"));
			sb.append("&device=" + URLEncoder.encode(device, "utf-8"));

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		// 向StringBuffer追加密码（密码采用MD5 32位 小写）
		// sb.append("&code=" + mobanMessage);
		// 创建url对象
		try {
			URL url = new URL(sb.toString());
			System.out.println("sb========" + sb.toString());
			// 打开url连接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// 设置url请求方式 ‘get’ 或者 ‘post’
			connection.setRequestMethod("POST");
			// 发送
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			// 返回发送结果
			String inputline = in.readLine();
			System.out.println("=====" + inputline + "========短信还回结果");
			return inputline;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
}
