package cn.cdsoft.modules.homepage.vo;

/**
 * Created by ZZUSER on 2019/2/18.
 */
public class ChangeDevSwitchVO {

    private String devId;//设备id

    private Integer value;//0代表开1代表关

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
