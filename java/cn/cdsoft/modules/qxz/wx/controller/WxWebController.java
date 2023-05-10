package cn.cdsoft.modules.qxz.wx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by 63446 on 2018/11/8.
 */
@Controller
@RequestMapping("/wx")
public class WxWebController {
//    @Autowired
//    RabbitMqCustomer rabbitMqCustomer;
//    @RequestMapping(value="/gongzhonghao")
//    public String gongzhonghao(HttpServletRequest request, Model model, GzhUser gzhUser){
//        System.out.println(request.getRequestURI());
//        String openId = gzhUser.getOpenId();
//        String keyword = gzhUser.getKeyword();
//        System.out.println(openId);
//        System.out.println(keyword);
//        model.addAttribute("openId",openId);
//        model.addAttribute("keyword",keyword);
//        return "modules/wx/gongzhonghao";
//    }
//
//    @RequestMapping(value="/startCustomer")
//    @ResponseBody
//    public void startCustomer(){
//        try {
//            rabbitMqCustomer.startProccessData();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
