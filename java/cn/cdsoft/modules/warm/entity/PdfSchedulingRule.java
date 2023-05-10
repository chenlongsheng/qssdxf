package cn.cdsoft.modules.warm.entity;

import cn.cdsoft.common.persistence.DataEntity;

/**
 *  排班规则
 * Created by ZZUSER on 2018/12/12.
 */
public class PdfSchedulingRule extends DataEntity<PdfSchedulingRule> {

    private String name;//规则名称

    private String orgId;//所属区域

    private String effectiveDateStart;//生效开始日期

    private String effectiveDateEnd;//生效结束日期

    private String watchkeepers;//值班人员

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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

    public String getWatchkeepers() {
        return watchkeepers;
    }

    public void setWatchkeepers(String watchkeepers) {
        this.watchkeepers = watchkeepers;
    }
}
