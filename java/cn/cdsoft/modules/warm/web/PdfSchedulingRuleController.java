package cn.cdsoft.modules.warm.web;

import cn.cdsoft.common.web.BaseController;
import cn.cdsoft.modules.warm.entity.PdfSchedulings;
import cn.cdsoft.modules.warm.service.PdfSchedulingRuleService;
import com.alibaba.fastjson.JSONObject;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.modules.warm.entity.PdfSchedulingRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZZUSER on 2018/12/12.
 */
@Controller
@RequestMapping("${adminPath}/warm")
public class PdfSchedulingRuleController extends BaseController {
    @Autowired
    PdfSchedulingRuleService pdfSchedulingRuleService;

    /**
     * 排班规则页赛选条件区域获取
     * @return
     */
    @RequestMapping("/getFirstOrgList")
    @ResponseBody
    public JSONObject getFirstOrgList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
		/*星号表示所有的域都可以接受，*/
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST");
        JSONObject jsonObject = new JSONObject();
        try {
            List<Map> list =  pdfSchedulingRuleService.getFirstOrgList();
            jsonObject.put("success",true);
            jsonObject.put("data",list);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject;
    }

    @RequestMapping("/getOrgList")
    @ResponseBody
    public JSONObject getOrgList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
		/*星号表示所有的域都可以接受，*/
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST");
        JSONObject jsonObject = new JSONObject();
        try {
            List<Map> list = pdfSchedulingRuleService.getOrgList();
            jsonObject.put("success",true);
            jsonObject.put("data",list);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject;
    }

    @RequestMapping("/getUseList")
    @ResponseBody
    public JSONObject getUseList(String orgId){
        JSONObject jsonObject = new JSONObject();
        try{
            List<Map> list = pdfSchedulingRuleService.getUseList(orgId);
            jsonObject.put("success",true);
            jsonObject.put("data",list);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject;
//        return pdfSchedulingRuleService.getUseList(orgId);
    }

    @RequestMapping("/addSchedulingRule")
    @ResponseBody
    public JSONObject addSchedulingRule(@RequestBody PdfSchedulings pdfSchedulings) throws ParseException {
        JSONObject jsonObject = new JSONObject();
        try{
            pdfSchedulingRuleService.addSchedulingRule(pdfSchedulings);
            jsonObject.put("success",true);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject;
    }

    @RequestMapping("/demo")
    public String demo(){
        return "modules/warm/demo";
    }

    @RequestMapping("/findRuleList")
    @ResponseBody
    public JSONObject findRuleList(PdfSchedulingRule pdfSchedulingRule, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        Page page = new Page(httpServletRequest,httpServletResponse);
        pdfSchedulingRule.setPage(page);
        JSONObject jsonObject = new JSONObject();
        try {
            List<Map> list = pdfSchedulingRuleService.findRuleList(pdfSchedulingRule);
            pdfSchedulingRule.setPage(null);
            List list1 = pdfSchedulingRuleService.findRuleList(pdfSchedulingRule);
            Map resultMap = new HashMap();
            resultMap.put("data",list);
            resultMap.put("length",list1.size());
            jsonObject.put("data",resultMap);
            jsonObject.put("success",true);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",true);
        }
       return jsonObject;
    }

    @RequestMapping("/deleteRules")
    @ResponseBody
    public JSONObject deleteRules(String ids){
        JSONObject jsonObject = new JSONObject();
        try {
            pdfSchedulingRuleService.deleteRules(ids);
            jsonObject.put("success",true);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject;
    }


}
