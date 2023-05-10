package cn.cdsoft.modules.qxz.wx.entry;

import java.util.Map;

/**
 * Created by ZZUSER on 2018/11/2.
 */
public class TextObject {
    private String toUser;

    private String msgtype;

    private Map text;

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public Map getText() {
        return text;
    }

    public void setText(Map text) {
        this.text = text;
    }
}
