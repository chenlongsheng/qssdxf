package cn.cdsoft.modules.qxz.wx.controller;

import com.alibaba.fastjson.JSONObject;
import cn.cdsoft.common.mapper.JsonMapper;
import cn.cdsoft.common.mq.rabbitmq.DevAlarm;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by ZZUSER on 2018/11/23.
 */
public class customer {
    public static void main(String[] args) {
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
                    System.out.println(message);
                    JSONObject jsonObject = JSONObject.parseObject(message);
                    String devId = String.valueOf(jsonObject.get("dev_id"));
                    List<Map> list  = (List<Map>) jsonObject.get("items");
                    System.out.println(list);
                    String chId = String.valueOf(list.get(0).get("ch_id"));
                    String alarmType = String.valueOf(list.get(0).get("alarm_type"));
                    System.out.println("Customer Received '" + message + "'");
                    System.out.println("devId:" + devId);
                    System.out.println("chId:" + chId);
                    System.out.println("alarmType:" + alarmType);
                    DevAlarm devAlarm = JsonMapper.getInstance().fromJson(message, DevAlarm.class);
                    try {
                        devAlarmQueue.put(devAlarm);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            //自动回复队列应答 -- RabbitMQ中的消息确认机制
            devAlarmChannel.basicConsume(DEV_ALARM_QUEUE, true, alarmConsumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
