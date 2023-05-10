package cn.cdsoft.modules.gismap.vo;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author LiJianRong
 * @date 2019/2/15.
 */
public class MapConfigAddVO {
    private String orgId;
    private List<MapConfigLine> lineList;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public List<MapConfigLine> getLineList() {
        return lineList;
    }

    public void setLineList(List<MapConfigLine> lineList) {
        this.lineList = lineList;
    }
}