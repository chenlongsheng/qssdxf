package cn.cdsoft.modules.warm.service;

import cn.cdsoft.modules.sys.entity.User;
import cn.cdsoft.modules.warm.dao.PdfSchedulingDao;
import cn.cdsoft.modules.warm.entity.PdfScheduling;
import cn.cdsoft.common.service.CrudService;
import cn.cdsoft.modules.warm.dao.PdfOrderDao;
import cn.cdsoft.modules.warm.entity.PdfSchedulingRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ZZUSER on 2018/12/12.
 */
@Service
public class PdfSchedulingService extends CrudService<PdfSchedulingDao,PdfScheduling> {
    @Autowired
    PdfSchedulingDao pdfSchedulingDao;

    @Autowired
    PdfOrderDao pdfOrderDao;

    /**
     * 根据规则id获取排班
     * @param ruleId
     * @return
     */
    public List<Map> findSchedulingByRuleId(String ruleId){
        List<Map> list = pdfSchedulingDao.findSchedulingByRuleId(ruleId);
        for(int i=0;i<list.size();i++){
            String ids = String.valueOf(list.get(i).get("watchkeeper"));
            String[] arr = ids.split(",");
            List<User> list1 = pdfOrderDao.getUserByIds(arr);
            list.get(i).put("userList",list1);
        }
        return pdfSchedulingDao.findSchedulingByRuleId(ruleId);
    }

    public Map findSchedulings(PdfSchedulingRule pdfSchedulingRule){
        List<Map> list = pdfSchedulingDao.findSchedulings(pdfSchedulingRule);
        Map<String,List<Map>> resultMap = new LinkedHashMap();
        for(int i = 0;i<list.size();i++){
            String ids = String.valueOf(list.get(i).get("watchkeeper"));
            String[] arr = ids.split(",");
            List<User> list1 = pdfOrderDao.getUserByIds(arr);
            String watchkeepers = "";
            for(int j=0;j<list1.size();j++){
                if(j==0){
                    watchkeepers = list1.get(j).getName();
                }else {
                    watchkeepers = watchkeepers +","+list1.get(j).getName();
                }
            }
            list.get(i).put("watchkeeper",watchkeepers);
            list.get(i).put("userList",list1);

            List<Map> list2 = resultMap.get(String.valueOf(list.get(i).get("schedulingDate")));
            if(list2 !=null){
                list2.add(list.get(i));
            }else {
                list2 = new ArrayList();
                list2.add(list.get(i));
            }
            resultMap.put(String.valueOf(list.get(i).get("schedulingDate")),list2);
        }
        pdfSchedulingRule.setPage(null);
        List<Map> list1 = pdfSchedulingDao.findSchedulings(pdfSchedulingRule);
        Map map = new HashMap();
        map.put("data",resultMap);
        map.put("length",list1.size());
        return map;
    }

    /**
     * 获取当天值班人员
     * @param pdfSchedulingRule
     * @return
     */
    public List<User> findSchedulingUser(PdfSchedulingRule pdfSchedulingRule){
        Date date = new Date();
        SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        pdfSchedulingRule.setEffectiveDateStart(sDateFormat.format(date));
        List<String> list = pdfSchedulingDao.findSchedulingUser(pdfSchedulingRule);
        String ids = "";
        for(int i=0;i<list.size();i++){
            if(i==0){
                ids = list.get(i);
            }else {
                ids = ids +","+list.get(i);
            }
        }
        String[] arr = ids.split(",");
        return pdfOrderDao.getUserByIds(arr);
    }
}
