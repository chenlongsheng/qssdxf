package cn.cdsoft.modules.maintenance.entity;

import cn.cdsoft.common.persistence.DataEntity;

/**
 * Created by Administrator on 2018-12-26.
 */
public class PdfMaintenanceAptitude extends DataEntity<PdfMaintenanceAptitude> {
    //维保单位id
    private String maintenanceId;
    //图片地址
    private String url;

    public String getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(String maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
