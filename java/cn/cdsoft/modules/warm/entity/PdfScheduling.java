package cn.cdsoft.modules.warm.entity;

import cn.cdsoft.common.persistence.DataEntity;

/**
 * Created by ZZUSER on 2018/12/12.
 */
public class PdfScheduling extends DataEntity<PdfScheduling> {

    private String ruleId;//规则id

    private int periodStart;//周期开始

    private int periodEnd;//周期结束

    private String startTime;//开始时间

    private String endTime;//结束时间

    private String watchkeeper;//值班人员

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

    public String getWatchkeeper() {
        return watchkeeper;
    }

    public void setWatchkeeper(String watchkeeper) {
        this.watchkeeper = watchkeeper;
    }
}
