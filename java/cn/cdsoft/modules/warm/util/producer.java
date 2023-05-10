package cn.cdsoft.modules.warm.util;

import cn.cdsoft.common.mq.rabbitmq.DevAlarm;
import cn.cdsoft.common.mq.rabbitmq.RabbitMQProducer;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * Created by ZZUSER on 2018/11/23.
 */
public class producer {

    public static void main(String[] args){
        RabbitMQProducer rabbitMQProducer = new RabbitMQProducer();
        com.rabbitmq.client.Channel rmqChannel = null;
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = null;
        final BlockingQueue<DevAlarm> devAlarmQueue = new LinkedBlockingQueue<DevAlarm>();
        String DEV_ALARM_QUEUE = "dev.alarm";
        factory.setHost("222.77.181.112");
        factory.setUsername("webclient");
        factory.setPassword("webclient");
        factory.setPort(5672);
        factory.setVirtualHost("cdiot");
        // 关键所在，指定线程池
//        ExecutorService service = Executors.newFixedThreadPool(10);
//        factory.setSharedExecutor(service);
        // 设置自动恢复
        factory.setAutomaticRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(2);// 设置 每10s ，重试一次
        factory.setTopologyRecoveryEnabled(false);// 设置不重新声明交换器，队列等信息。
        try
        {
            connection = factory.newConnection();
            System.out.println(111111);
            //创建一个通道
            rmqChannel = connection.createChannel();
            System.out.println(222222);
            System.out.println(rabbitMQProducer);
            //String message = "{\"dev_id\":"+channel.getDevId()+",\"ch_id\":"+channel.getId()+",\"cmd_type\":1,\"seq_uuid\":\""+IdGen.uuid()+"\",\"params\":{\"off_on\":"+1+",\"open_minute\":"+time+"}}";
//            String message = "{\"mac\":\""+tDeviceDetail.getMac()+"\",\"channels\":{\""+tChannel.getChNo()+"\":{\"value\":\"1\"},\""+(switchChannel.getChNo()+12)+"\":{\"value\":\""+minute+"\"}}}";
//            String message = "{\"mac\\\":\""+(tDeviceDetail.getMac()==null?"":tDeviceDetail.getMac())+"\",\"imei\":\""+(tDeviceDetail.getImei()==null?"":tDeviceDetail.getImei())+"\",\"outer_id\":\""+(tChannel.getOuterId()==null?"":tChannel.getOuterId())+"\",\"channels\":{\""+tChannel.getChNo()+"\":{\"value\":\""+tChannel.getRemarks()+"\"}}}";
            String message = "{\"dev_id\":342,\"items\":[{\"ch_id\":2,\"occur_time\":\"2019-02-19 16:20:00\",\"alarm_level\":3,\"alarm_type\":3,\"alarm_info\":\"报警\",\"alarm_log_id\":23}]}";
            //发送消息到队列中
            rmqChannel.basicPublish("org.10", "dev.alarm.10.01", null, message.getBytes("UTF-8"));
            System.out.println("/**============================================================**/");
            System.out.println("Producer Send +'" + message + "'  to " + "dev.alarm.10.01");
            System.out.println("/**============================================================**/");
//            logger.debug("/**============================================================**/");
//            logger.debug("Producer Send +'" + message + "'  to " + "dev.command.10.01");
//            logger.debug("/**============================================================**/");
            //关闭通道和连接
            rmqChannel.close();
            connection.close();
        } 	catch (Exception e) {
            try {
                rmqChannel.close();
                connection.close();
            } catch (IOException e1) {
//                logger.error(e1.toString());
                e1.printStackTrace();
            } catch (TimeoutException e1) {
                e1.printStackTrace();
            }
        }
    }
}

