package cn.cdsoft.modules.qxz.excel;

/**
 * Created by ZZUSER on 2018/12/25.
 */
public class SchedulingExcel {

    private String orgName;//区域名称

    private String name;//规则名称

    private String startTime;//开始时间

    private String endTime;//结束时间

    private String watchkeeper;//值班人员

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
