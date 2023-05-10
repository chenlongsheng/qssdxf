package cn.cdsoft.modules.warm.entity;

import cn.cdsoft.common.persistence.DataEntity;

/** openid绑定
 * Created by ZZUSER on 2018/12/12.
 */
public class PdfBind extends DataEntity<PdfBind> {

    private String openId;

    private String userId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
