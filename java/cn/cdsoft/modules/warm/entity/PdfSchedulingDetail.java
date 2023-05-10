package cn.cdsoft.modules.warm.entity;

/**
 * Created by ZZUSER on 2018/12/13.
 */
public class PdfSchedulingDetail {

    private String id;

    private String schedulingId;//排班id

    private String SchedulingDate;//日期

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchedulingId() {
        return schedulingId;
    }

    public void setSchedulingId(String schedulingId) {
        this.schedulingId = schedulingId;
    }

    public String getSchedulingDate() {
        return SchedulingDate;
    }

    public void setSchedulingDate(String schedulingDate) {
        SchedulingDate = schedulingDate;
    }
}
