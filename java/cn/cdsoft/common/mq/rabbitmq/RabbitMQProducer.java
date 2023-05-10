package cn.cdsoft.common.mq.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class RabbitMQProducer {
	
	private ConnectionFactory factory = new ConnectionFactory();
	
	//创建一个新的连接
    private Connection connection = null;
    
    private String rabbitmqHost;
    
    private String rabbitmqUsername;
    
    private String rabbitmqPassword;
    
    private int rabbitmqPort;
    
    private String rabbitmqVirtualHost;
    
    public String getRabbitmqHost() {
		return rabbitmqHost;
	}

	public void setRabbitmqHost(String rabbitmqHost) {
		this.rabbitmqHost = rabbitmqHost;
	}

	public String getRabbitmqUsername() {
		return rabbitmqUsername;
	}

	public void setRabbitmqUsername(String rabbitmqUsername) {
		this.rabbitmqUsername = rabbitmqUsername;
	}

	public String getRabbitmqPassword() {
		return rabbitmqPassword;
	}

	public void setRabbitmqPassword(String rabbitmqPassword) {
		this.rabbitmqPassword = rabbitmqPassword;
	}

	public int getRabbitmqPort() {
		return rabbitmqPort;
	}

	public void setRabbitmqPort(int rabbitmqPort) {
		this.rabbitmqPort = rabbitmqPort;
	}

	public String getRabbitmqVirtualHost() {
		return rabbitmqVirtualHost;
	}

	public void setRabbitmqVirtualHost(String rabbitmqVirtualHost) {
		this.rabbitmqVirtualHost = rabbitmqVirtualHost;
	}

	private boolean isInit = false;
	
	Map<String,Channel> channelMap = new HashMap<String,Channel>();
	
	public Channel getChannel(String channelName) {
		return channelMap.get(channelName);
	}
	
	public void putChannel(String channelName,Channel channel) {
		channelMap.put(channelName, channel);
	}
	
	public void removeChannel(String channelName) {
		Channel channel = channelMap.get(channelName);
		try {
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		channelMap.remove(channelName);
	}
	
	public Channel createChannel(){
		Channel channel = null;
		
		if(isInit == false) {
			this.factory.setHost(rabbitmqHost);
			this.factory.setUsername(rabbitmqUsername);
			this.factory.setPassword(rabbitmqPassword);
			this.factory.setPort(rabbitmqPort);
			this.factory.setVirtualHost(rabbitmqVirtualHost);
	        // 关键所在，指定线程池  
	        ExecutorService service = Executors.newFixedThreadPool(10);  
	        factory.setSharedExecutor(service);  
	        // 设置自动恢复  
	        factory.setAutomaticRecoveryEnabled(true);  
	        factory.setNetworkRecoveryInterval(2);// 设置 没10s ，重试一次  
	        factory.setTopologyRecoveryEnabled(false);// 设置不重新声明交换器，队列等信息。  
			isInit = true;
		}
		
		if(connection == null) {
			try {
				connection = factory.newConnection();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
		}
		
		if(connection == null || !connection.isOpen()) {
			return null;
		} else {
			try {
				 channel = connection.createChannel();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return channel;
	}
	
}
