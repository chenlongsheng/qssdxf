package cn.cdsoft.modules.qxz.wx.util;

import cn.cdsoft.common.mapper.JsonMapper;
import cn.cdsoft.common.mq.rabbitmq.ServiceFacade;
import cn.cdsoft.common.mq.rabbitmq.DevAlarm;
import cn.cdsoft.common.mq.rabbitmq.DevData;
import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * Created by ZZUSER on 2018/11/23.
 */
public class RabbitMqCustomer {
    @SuppressWarnings("unused")
    private ServiceFacade serviceFacade;

    @Autowired
    SendTemplateUtil sendTemplateUtil;

    public void setServiceFacade(ServiceFacade serviceFacade) {
        this.serviceFacade = serviceFacade;
    }

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

    public static BlockingQueue<DevAlarm> devAlarmQueue = new LinkedBlockingQueue<DevAlarm>();
    public static BlockingQueue<DevData> devDataQueue = new LinkedBlockingQueue<DevData>();

    private final static String DEV_ALARM_QUEUE = "dev.alarm";
    private final static String DEV_DATA_QUEUE = "dev.status";
    public void startProccessData(){
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
        factory.setNetworkRecoveryInterval(2);// 设置 每10s ，重试一次
        factory.setTopologyRecoveryEnabled(false);// 设置不重新声明交换器，队列等信息。
        try {
            connection = factory.newConnection();


            /**报警消息 */
            //创建一个通道
            Channel devAlarmChannel = connection.createChannel();
            //声明要关注的队列
            devAlarmChannel.queueDeclare(DEV_ALARM_QUEUE, true, false, false, null);
            System.out.println("Customer Waiting Received messages");
            //DefaultConsumer类实现了Consumer接口，通过传入一个频道，
            // 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
            Consumer alarmConsumer = new DefaultConsumer(devAlarmChannel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println("Customer Received '" + message + "'");
                    DevAlarm devAlarm = JsonMapper.getInstance().fromJson(message, DevAlarm.class);
                    sendTemplateUtil.sendAlarmTemplate(devAlarm);
                    try {
                        devAlarmQueue.put(devAlarm);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            //自动回复队列应答 -- RabbitMQ中的消息确认机制
            devAlarmChannel.basicConsume(DEV_ALARM_QUEUE, true, alarmConsumer);

            /**数据消息 */
            //创建一个通道
            Channel devDataChannel = connection.createChannel();
            //声明要关注的队列
            devDataChannel.queueDeclare(DEV_DATA_QUEUE, true, false, false, null);
            System.out.println("Customer Dev data Waiting Received messages");
            //DefaultConsumer类实现了Consumer接口，通过传入一个频道，
            // 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
            Consumer devDataConsumer = new DefaultConsumer(devDataChannel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println("Customer Received '" + message + "'");
                    DevData devData = JsonMapper.getInstance().fromJson(message, DevData.class);
                    try {
                        devDataQueue.put(devData);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            //自动回复队列应答 -- RabbitMQ中的消息确认机制
            devDataChannel.basicConsume(DEV_DATA_QUEUE, true, devDataConsumer);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
