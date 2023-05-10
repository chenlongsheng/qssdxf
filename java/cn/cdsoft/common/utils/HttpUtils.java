package cn.cdsoft.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * HTTP 请求工具类
 * @author lhy
 *
 */
public class HttpUtils {

	/**
	 * GET方式请求
	 */
	public static String sendGet(String url, Map<String, Object> params, Map<String, Object> headers) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url;
			if (params != null) {
				StringBuffer p = new StringBuffer();
				Set<Entry<String, Object>> set = params.entrySet();
				for (Entry<String, Object> entry : set) {
					String key = entry.getKey();
					Object value = entry.getValue();
					p.append(key).append("=").append(value).append("&");
				}
				String temp = p.toString();
				if (temp.length() > 0) {
					temp = temp.substring(0, temp.length() - 1);
					urlNameString = urlNameString + "?" + temp;
				}
			}
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			if (headers != null) {
				for (Entry<String, Object> entry : headers.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue().toString();
					connection.setRequestProperty(key, value);
				}
			}
			// 建立实际的连接
			connection.connect();

			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * POST方式请求
	 */
	public static String sendPost(String url, Map<String, Object> params, Map<String, Object> headers) {
		return sendPost(url, params, headers, false);
	}

	/**
	 *  发送JSON格式的 GET请求
	 */
	public static String sendJSONGet(String url, Map<String, Object> params, Map<String, Object> headers) {
		if (headers == null) {
			headers = new HashMap<>();
		}
		headers.put("Content-Type", "application/json");
		return sendGet(url, params, headers);
	}
	/**
	 *   发送JSON格式的 POST请求
	 */
	public static String sendJSONPost(String url, Map<String, Object> params, Map<String, Object> headers) {
		if (headers == null) {
			headers = new HashMap<>();
		}
		headers.put("Content-Type", "application/json");
		return sendPost(url, params, headers, true);
	}

	/**
	 * 发送数据格式的POST请求
	 */
	public static String sendPost(String url,String data, Map<String, Object> headers) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {

			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			if (headers != null) {
				for (Entry<String, Object> entry : headers.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue().toString();
					conn.setRequestProperty(key, value);
				}
			}
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(data);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	private static String sendPost(String url, Map<String, Object> params, Map<String, Object> headers, boolean isJSON) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		String temp = "";
		try {
			if (params != null) {
				if (isJSON) {
					StringBuffer p = new StringBuffer();
					p.append("{");
					Set<Entry<String, Object>> set = params.entrySet();
					for (Entry<String, Object> entry : set) {
						String key = entry.getKey();
						Object value = entry.getValue();
						p.append("\"").append(key).append("\"").append(":").append("\"").append(value).append("\"")
								.append(",");
					}
					temp = p.toString();
					if (temp.length() > 0) {
						temp = temp.substring(0, temp.length() - 1);
					}
					temp = temp + "}";
				} else {
					StringBuffer p = new StringBuffer();
					Set<Entry<String, Object>> set = params.entrySet();
					for (Entry<String, Object> entry : set) {
						String key = entry.getKey();
						Object value = entry.getValue();
						p.append(key).append("=").append(value).append("&");
					}
					temp = p.toString();
					if (temp.length() > 0) {
						temp = temp.substring(0, temp.length() - 1);
					}
				}

			}

			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			if (headers != null) {
				for (Entry<String, Object> entry : headers.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue().toString();
					conn.setRequestProperty(key, value);
				}
			}
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(temp);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		// Map<String, Object> headers = new HashMap<>();
//			headers.put("Content-Type", "application/x-www-form-urlencoded");
//			headers.put("mobileLogin", true);
//			headers.put("token", "e27ee9746d5d429792a0bb033e62ee22");

		Map<String, Object> params = new HashMap<>();
		params.put("id", "237276409423204352");

		// String result = sendPost("http://localhost:8080/car_park/weChat/test/a",
		// params, headers);
		// System.out.println(result);
		String result = sendJSONPost("http://localhost:8080/car_park/weChat/test/a", params, null);
		System.out.println(result);
	}
}
