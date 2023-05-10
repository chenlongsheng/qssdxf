package cn.cdsoft.modules.warm.entity;

import cn.cdsoft.common.persistence.DataEntity;

/**
 * Created by ZZUSER on 2018/12/7.
 */
public class PdfOrderRecorder extends DataEntity<PdfOrderRecorder> {
    private String userId;//处理人

    private String date;//处理日期

    private int state;//工单状态

    private String orderId;//工单id

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
