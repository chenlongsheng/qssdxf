package cn.cdsoft.modules.warm.entity;

import cn.cdsoft.common.persistence.DataEntity;

/**
 * Created by ZZUSER on 2018/12/7.
 */
public class PdfOrderDeal extends DataEntity<PdfOrderDeal> {
    private String sendUser;//发送人

    private String recieveUser;//接收人

    private String content;//处理内容

    private String image;//图片

    private int type;

    private String orderId;//工单id

    private String sendDate;//发送日期

    private String replyId;//回复的是那条记录

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }

    public String getRecieveUser() {
        return recieveUser;
    }

    public void setRecieveUser(String recieveUser) {
        this.recieveUser = recieveUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }
}
