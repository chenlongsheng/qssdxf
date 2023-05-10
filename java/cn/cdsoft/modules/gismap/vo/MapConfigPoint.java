package cn.cdsoft.modules.gismap.vo;

/**
 * @author LiJianRong
 * @date 2019/2/27.
 */
public class MapConfigPoint {
    private String id;
    private String xLine;
    private String yLine;
    private Integer sort;
    private String orgId;
    private String lineId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getxLine() {
        return xLine;
    }

    public void setxLine(String xLine) {
        this.xLine = xLine;
    }

    public String getyLine() {
        return yLine;
    }

    public void setyLine(String yLine) {
        this.yLine = yLine;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }
}
