package cn.cdsoft.modules.warm.web;

import cn.cdsoft.common.mq.rabbitmq.SpeakerSendMqtt;
import cn.cdsoft.common.web.BaseController;
import cn.cdsoft.modules.homepage.service.SpeakerSendService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ZZUSER on 2018/12/13.
 */
@Controller
@RequestMapping("/warm")
public class WebController extends BaseController {
	
	
	@Autowired
	SpeakerSendService speakerSendService;
	@Autowired
	SpeakerSendMqtt mqtt_logic;

    @RequestMapping("/schedulingRulePage")
    public String  schedulingRulePage(){
        return "modules/warm/schedulingRulePage";
    }

    @RequestMapping("/schedulingPage")
    public String  schedulingPage(){
        return "modules/warm/schedulingPage";
    }
    
    
    @RequestMapping("/test2")
    public void  test2(String chId){
    	
    	try {
			mqtt_logic.sendMessage(chId,URLEncoder.encode("", "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


    }

}
