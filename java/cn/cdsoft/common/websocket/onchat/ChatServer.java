/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.common.websocket.onchat;

import cn.cdsoft.common.json.AjaxJson;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Collection;

public class ChatServer extends WebSocketServer {

	
	public ChatServer(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
	}

	public ChatServer(InetSocketAddress address) {
		super(address);
	}

	/**
	 * 触发连接事件
	 */
	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake ) {
//		Collection<String> onlineUsers = ChatServerPool.getOnlineUser();
//		AjaxJson j = new AjaxJson();
//		j.put("data", onlineUsers);
//		ChatServerPool.sendMessageToUser(conn, "_online_all_status_"+j.getJsonStr());//首次登陆系统时，获取用户的在线状态
//		System.out.println("有新连接加入！当前在线人数为"+j.toString());
	}

	/**
	 * 触发关闭事件
	 */
	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote ) {
		userLeave(conn);
		Collection<String> onlineUsers = ChatServerPool.getOnlineUser();
		AjaxJson j = new AjaxJson();
		j.put("data", onlineUsers);
		ChatServerPool.sendMessage("_online_all_status_"+j.getJsonStr());//通知所有用户更新在线信息
	}

	/**
	 * 客户端发送消息到服务器时触发事件
	 */
	@Override
	public void onMessage(WebSocket conn, String message){
		
	}
	
	@Override
	public void onMessage(WebSocket conn, ByteBuffer buffer){
		System.out.println("执行了发送消息的动作2");
		 Charset charset = null;
	        CharsetDecoder decoder = null;
	        CharBuffer charBuffer = null;
		try {
			 charset = Charset.forName("UTF-8");
	            decoder = charset.newDecoder();
	            // charBuffer = decoder.decode(buffer);//用这个的话，只能输出来一次结果，第二次显示为空
	            charBuffer = decoder.decode(buffer.asReadOnlyBuffer());
	            //return charBuffer.toString();
		System.out.println( charBuffer.toString());
		} catch (Exception ex) {
		ex.printStackTrace();
		}
	}

	public void onFragment(WebSocket conn, Framedata fragment ) {
	}

	/**
	 * 触发异常事件
	 */
	@Override
	public void onError(WebSocket conn, Exception ex ) {
		ex.printStackTrace();
		if( conn != null ) {
			//some errors like port binding failed may not be assignable to a specific websocket
		}
	}

	
	/**
	 * 用户加入处理
	 * @param user
	 */
	public void userjoin(String user, WebSocket conn){
//		AjaxJson j = new AjaxJson();
//		j.put("type", "user_join");
//		j.put("user", "<a onclick=\"toUserMsg('"+user+"');\">"+user+"</a>");
//		MsgServerPool.sendMessage(j.getJsonStr());				//把当前用户加入到所有在线用户列表中
//		String joinMsg = "{\"from\":\"[系统]\",\"content\":\""+user+"上线了\",\"timestamp\":"+new Date().getTime()+",\"type\":\"message\"}";
//		MsgServerPool.sendMessage(joinMsg);						//向所有在线用户推送当前用户上线的消息
//		j = new AjaxJson();
//		j.put("type", "get_online_user");
		ChatServerPool.addUser(user,conn);							//向连接池添加当前的连接对象
//		j.put("list", MsgServerPool.getOnlineUser());
//		MsgServerPool.sendMessageToUser(conn, j.getJsonStr());	//向当前连接发送当前在线用户的列表
	}
	
	/**
	 * 用户下线处理
	 * @param user
	 */
	public void userLeave(WebSocket conn){
		String user = ChatServerPool.getUserByKey(conn);
		boolean b = ChatServerPool.removeUser(conn);				//在连接池中移除连接
//		if(b){
//			AjaxJson j = new AjaxJson();
//			j.put("type", "user_leave");
//			j.put("user", "<a onclick=\"toUserMsg('"+user+"');\">"+user+"</a>");
//			MsgServerPool.sendMessage(j.getJsonStr());			//把当前用户从所有在线用户列表中删除
//			String joinMsg = "{\"from\":\"[系统]\",\"content\":\""+user+"下线了\",\"timestamp\":"+new Date().getTime()+",\"type\":\"message\"}";
//			MsgServerPool.sendMessage(joinMsg);					//向在线用户发送当前用户退出的消息
//		}
	}
	public static void main( String[] args ) throws InterruptedException , IOException {
		WebSocketImpl.DEBUG = false;
		int port = 8667; //端口
		ChatServer s = new ChatServer(port);
		s.start();
		//System.out.println( "服务器的端口" + s.getPort() );
	}

}

