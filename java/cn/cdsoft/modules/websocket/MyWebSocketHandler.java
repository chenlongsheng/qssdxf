package cn.cdsoft.modules.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class MyWebSocketHandler implements WebSocketHandler {

	Logger logger = LoggerFactory.getLogger(getClass());
	
    //当MyWebSocketHandler类被加载时就会创建该Map，随类而生
    public static final Hashtable<String, WebSocketSession> userSocketSessionMap = new Hashtable<String, WebSocketSession>();

    //握手实现连接后
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
    	logger.debug("ConnectionEstablished");
        userSocketSessionMap.put(webSocketSession.getId(), webSocketSession);
    }
    
    //连接断开后
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
    	logger.debug("ConnectionClosed");
    	userSocketSessionMap.remove(webSocketSession.getId());
    }

    //发送信息前的处理
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
    	logger.debug("handleMessage");
    	System.out.println(webSocketMessage);
    }

    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

    }

    public boolean supportsPartialMessages() {
        return false;
    }
    
    //发送信息的实现
    public void sendMessageToUser(String uid,TextMessage message) throws IOException {
    	logger.debug("/****** send message to all users ******/");
    	logger.debug(message.toString());
    	logger.debug("/***************************************/");
    	
    	Set<Entry<String,WebSocketSession>> setWebSocketSession = userSocketSessionMap.entrySet();
    	for(Entry<String,WebSocketSession> entry : setWebSocketSession) {
    		WebSocketSession session = entry.getValue();
            if (session != null && session.isOpen()) {
                session.sendMessage(message);
            }
    	}
    }
    
}
