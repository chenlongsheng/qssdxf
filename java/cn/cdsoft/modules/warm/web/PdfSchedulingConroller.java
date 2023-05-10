package cn.cdsoft.modules.warm.web;

import cn.cdsoft.common.web.BaseController;
import cn.cdsoft.modules.qxz.excel.SchedulingExcel;
import cn.cdsoft.modules.sys.entity.User;
import com.alibaba.fastjson.JSONObject;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.modules.qxz.excel.ExcelUtil;
import cn.cdsoft.modules.warm.entity.PdfSchedulingRule;
import cn.cdsoft.modules.warm.service.PdfSchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ZZUSER on 2018/12/12.
 */
@Controller
@RequestMapping("${adminPath}/warm")
public class PdfSchedulingConroller extends BaseController {
    @Autowired
    PdfSchedulingService pdfSchedulingService;

    /**
     * 根据规则id获取排班
     * @param ruleId
     * @return
     */
    @RequestMapping("/findSchedulingByRuleId")
    @ResponseBody
    public JSONObject findSchedulingByRuleId(String ruleId, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
		/*星号表示所有的域都可以接受，*/
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST");
        JSONObject jsonObject = new JSONObject();
        try {
            List<Map> list = pdfSchedulingService.findSchedulingByRuleId(ruleId);
            jsonObject.put("success",true);
            jsonObject.put("data",list);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject;
    }


    /**
     * 获取排班集合
     * @param pdfSchedulingRule
     * @return
     */
    @RequestMapping("/findSchedulings")
    @ResponseBody
    public JSONObject findSchedulings(PdfSchedulingRule pdfSchedulingRule, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
		/*星号表示所有的域都可以接受，*/
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST");
        Page page = new Page<PdfSchedulingRule>(httpServletRequest,httpServletResponse);
        pdfSchedulingRule.setPage(page);
        JSONObject jsonObject = new JSONObject();
        try {
            Map map =  pdfSchedulingService.findSchedulings(pdfSchedulingRule);
            jsonObject.put("success",true);
            jsonObject.put("data",map);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject;
    }


    /**
     * 获取当天值班人员
     * @param pdfSchedulingRule
     * @return
     */
    @RequestMapping("/findSchedulingUser")
    @ResponseBody
    public JSONObject findSchedulingUser(PdfSchedulingRule pdfSchedulingRule, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
		/*星号表示所有的域都可以接受，*/
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST");
        JSONObject jsonObject = new JSONObject();
        try{
            List<User> list = pdfSchedulingService.findSchedulingUser(pdfSchedulingRule);
            jsonObject.put("data",list);
            jsonObject.put("success",true);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject;
    }

    @RequestMapping("/export")
    @ResponseBody
    public void export(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
//        GzhUser gzhUser = new GzhUser();
        PdfSchedulingRule pdfSchedulingRule = new PdfSchedulingRule();
        pdfSchedulingRule.setPage(null);
        Map map = pdfSchedulingService.findSchedulings(pdfSchedulingRule);
        Map schedulingMap = (Map) map.get("data");
        List<SchedulingExcel> resultList = new ArrayList();

        Iterator entries = schedulingMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String)entry.getKey();
            List<Map> value = (List<Map>) entry.getValue();
            System.out.println("Key = " + key + ", Value = " + value);

            SchedulingExcel excel = new SchedulingExcel();
            excel.setOrgName(key);
            resultList.add(excel);
            for(int i=0;i<value.size();i++){
                excel = new SchedulingExcel();
                excel.setOrgName((String) value.get(i).get("orgName"));
                excel.setName((String) value.get(i).get("name"));
                excel.setStartTime((String) value.get(i).get("start_time"));
                excel.setEndTime((String) value.get(i).get("end_time"));
                List<User> userList = (List<User>) value.get(i).get("userList");
                String watchkeeper ="";
                for(int j=0;j<userList.size();j++){
                    if(j==0){
                        watchkeeper = userList.get(j).getName();
                    }else {
                        watchkeeper = watchkeeper + "," + userList.get(j).getName();
                    }
                }
                excel.setWatchkeeper(watchkeeper);
                resultList.add(excel);
            }
        }

        ExcelUtil.exportScheduling(httpServletRequest,httpServletResponse,resultList);
        System.out.println(11);
    }

}
