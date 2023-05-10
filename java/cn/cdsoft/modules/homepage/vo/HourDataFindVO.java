package cn.cdsoft.modules.homepage.vo;

/**统计某一天各时段数据
 * Created by ZZUSER on 2019/2/15.
 */
public class HourDataFindVO {

    private Long devId;//设备id

    private String startDate;//开始日期

    private String endDate;//结束日期


    public Long getDevId() {
        return devId;
    }

    public void setDevId(Long devId) {
        this.devId = devId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
