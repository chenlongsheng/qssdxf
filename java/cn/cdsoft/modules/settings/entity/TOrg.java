package cn.cdsoft.modules.settings.entity;

import java.util.ArrayList;

import cn.cdsoft.common.persistence.DataEntity;

/**
 * Created by Administrator on 2018-08-17.
 */
public class TOrg extends DataEntity<TOrg> {
    //父级编号
    private Long parentId;
    //所有父级编号
    private String parentIds;
    //名称
    private String name;
    //区域编码
    private String code;
    //排序
    private Integer orderNo;
    //备注信息
    private String remarks;
    //子类小区名称
    private String childrenOrgName;
    //子类小区id
    private String childrenOrgId;
    //小区下配置的区域
    private ArrayList<String> housePots;
    //小区下配置的充电桩
    private ArrayList<String> chargePots;
    //充电桩底下的插头
    private ArrayList<String> tchannles;
    //区域类型
    private Integer type;
    //坐标x
    private String coordsX;
    //坐标Y
    private String coordsY;
    private String image;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String getRemarks() {
        return remarks;
    }

    @Override
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public ArrayList<String> getChargePots() {
        return chargePots;
    }

    public void setChargePots(ArrayList<String> chargePots) {
        this.chargePots = chargePots;
    }

    public ArrayList<String> getTchannles() {
        return tchannles;
    }

    public void setTchannles(ArrayList<String> tchannles) {
        tchannles = tchannles;
    }

    public ArrayList<String> getHousePots() {

        return housePots;
    }

    public void setHousePots(ArrayList<String> housePots) {
        this.housePots = housePots;
    }

    public void housePotsAdd(String id){
        this.housePots.add(id);
    }

    public void tchannlesAdd(String id){
        this.tchannles.add(id);
    }

    public void chargePotsAdd(String id){
        this.chargePots.add(id);
    }

    public String getChildrenOrgName() {
        return childrenOrgName;
    }

    public void setChildrenOrgName(String childrenOrgName) {
        this.childrenOrgName = childrenOrgName;
    }

    public String getChildrenOrgId() {
        return childrenOrgId;
    }

    public void setChildrenOrgId(String childrenOrgId) {
        this.childrenOrgId = childrenOrgId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCoordsX() {
        return coordsX;
    }

    public void setCoordsX(String coordsX) {
        this.coordsX = coordsX;
    }

    public String getCoordsY() {
        return coordsY;
    }

    public void setCoordsY(String coordsY) {
        this.coordsY = coordsY;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
