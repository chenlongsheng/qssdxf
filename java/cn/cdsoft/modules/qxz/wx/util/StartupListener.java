package cn.cdsoft.modules.qxz.wx.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.Timer;

/**
 * Created by ZZUSER on 2018/10/17.
 */
@Component
@Controller
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    cn.cdsoft.common.mq.rabbitmq.RabbitMQCustomer rabbitMqCustomer;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(contextRefreshedEvent.getApplicationContext().getParent() == null){//root application context 没有parent，他就是老大.
//            JedisUtils.setObject("newsList",list,0);
            final String appId = PropertiesUtil.getProperty("appId");
            final String appsecret = PropertiesUtil.getProperty("appsecret");
//            TimerTask task = new TimerTask() {
//                @Override
//                 public void run() {
//                    try{
//                        String accessToken = WeChatApiUtil.getToken(appId,appsecret);
//                        JedisUtils.setObject("WxAccessToken",accessToken,0);
//                        System.out.println("成功");
//                        System.out.println(accessToken);
//                        Thread.sleep(7000*1000);
//                        System.out.println("继续");
//                    }catch (Exception e){
//                        try {
//                            Thread.sleep(3000);
//                            System.out.println("重新");
//                        } catch (InterruptedException e1) {
//                            e1.printStackTrace();
//                        }
//                    }
//                }
//            };
            try {
                rabbitMqCustomer.startProccessData();
            }catch (Exception e){
                e.printStackTrace();
            }
            Timer timer = new Timer();
            long delay = 0;
            long intevalPeriod = 1 * 1000;
            // schedules the task to be run in an interval
//            timer.scheduleAtFixedRate(task, delay, intevalPeriod);
        }
    }
}
