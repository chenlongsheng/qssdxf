package cn.cdsoft.modules.gismap.vo;

import java.util.List;

/**
 * @author LiJianRong
 * @date 2019/2/27.
 */
public class MapConfigLine {
    private String lineId;
    private List<MapConfigPoint> pointList;

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public List<MapConfigPoint> getPointList() {
        return pointList;
    }

    public void setPointList(List<MapConfigPoint> pointList) {
        this.pointList = pointList;
    }
}
