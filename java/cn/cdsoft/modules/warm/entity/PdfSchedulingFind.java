package cn.cdsoft.modules.warm.entity;

/**
 *  排班查询类
 * Created by ZZUSER on 2018/12/12.
 */
public class PdfSchedulingFind {

    private String ruleName;//规则名称

    private String orgId;//区域

    private String effectiveDateStart;//生效开始日期

    private String effectiveDateEnd;//生效结束日期

    private String ruleId;//规则id

    private int periodStart;//周期开始

    private int periodEnd;//周期结束

    private String startTime;//值班开始日期

    private String endTime;//值班结束日期

    private String watchkeeper;//值班人员


    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEffectiveDateStart() {
        return effectiveDateStart;
    }

    public void setEffectiveDateStart(String effectiveDateStart) {
        this.effectiveDateStart = effectiveDateStart;
    }

    public String getEffectiveDateEnd() {
        return effectiveDateEnd;
    }

    public void setEffectiveDateEnd(String effectiveDateEnd) {
        this.effectiveDateEnd = effectiveDateEnd;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public int getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(int periodStart) {
        this.periodStart = periodStart;
    }

    public int getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(int periodEnd) {
        this.periodEnd = periodEnd;
    }

    public String getWatchkeeper() {
        return watchkeeper;
    }

    public void setWatchkeeper(String watchkeeper) {
        this.watchkeeper = watchkeeper;
    }
}
