package cn.cdsoft.modules.warm.dao;

import cn.cdsoft.common.persistence.CrudDao;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;
import cn.cdsoft.modules.settings.entity.TOrg;
import cn.cdsoft.modules.sys.entity.Area;
import cn.cdsoft.modules.warm.entity.PdfSchedulingRule;

import java.util.List;
import java.util.Map;

/**
 * Created by ZZUSER on 2018/12/12.
 */
@MyBatisDao
public interface PdfSchedulingRuleDao extends CrudDao<PdfSchedulingRule> {

    int addSchedulingRule(PdfSchedulingRule pdfSchedulingRule);//新增排班规则

    long getMaxId();

    List<Map> getFirstOrgList(TOrg tOrg);//排班规则页赛选条件区域获取

    List<Map> getOrgList(Area area);//获取区域集合

    List<Map> getUserList(String[] arr);

    List<Map> getUserList1(TOrg torg);//根据区域获取人员

    TOrg getOrgById(String id);

    List<Map> findRuleList(PdfSchedulingRule pdfSchedulingRule);//按条件获取规则集合

    void updateRule(PdfSchedulingRule pdfSchedulingRule);

    void deleteRules(String[] arr);
}
