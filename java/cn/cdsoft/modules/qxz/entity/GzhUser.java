package cn.cdsoft.modules.qxz.entity;

import cn.cdsoft.common.persistence.DataEntity;

/**
 * Created by 63446 on 2018/11/6.
 */
public class GzhUser extends DataEntity<GzhUser> {

    private String image;//头像

    private String orgIds;//归属区域

    private String openId;

    private String keyword;//关键字

    private String loginName;//登录名

    private String prec;//描述

    private String password;

    private String phone;//手机

    private String email;//邮箱

    private String userRole;//用户角色

    private Integer state;//启用/禁用

    private Integer type;//前端类型

    private String desct;//备注

    private String parentOrgIds;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(String orgIds) {
        this.orgIds = orgIds;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPrec() {
        return prec;
    }

    public void setPrec(String prec) {
        this.prec = prec;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDesct() {
        return desct;
    }

    public void setDesct(String desct) {
        this.desct = desct;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getParentOrgIds() {
        return parentOrgIds;
    }

    public void setParentOrgIds(String parentOrgIds) {
        this.parentOrgIds = parentOrgIds;
    }

}
