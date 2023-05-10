package cn.cdsoft.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import cn.cdsoft.common.persistence.DataEntity;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2018-08-16.
 */
public class CdzAdminUser extends DataEntity<CdzAdminUser> {
    //昵称
    private String nickname;
    //用户名
    private String username;
    private String password;
    private Long money;
    private Date lastLoginTime;
    private Date createTime;
    //地区名称
    private String orgName;
    //地区id
    private ArrayList<String> orgs = new ArrayList<String>();
    //子区域id
    private ArrayList<String> childrenOrgs = new ArrayList<String>();
    //性别
    private String sex;
    //启用禁用
    private String isEnable;
    //删除标记
    private String defFlag;
    //电话
    private String phone;
    //备注
    private String remark;
    //父账号id
    private Long parentAccountId;
    //权限
    private Integer authority;
    //标记
    private Integer flag;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public ArrayList<String> getOrgs() {
        return orgs;
    }

    public void setOrgs(ArrayList<String> orgs) {
        this.orgs = orgs;
    }
    public void orgAdd(String orgName){
        this.orgs.add(orgName);
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getDefFlag() {
        return defFlag;
    }

    public void setDefFlag(String defFlag) {
        this.defFlag = defFlag;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getParentAccountId() {
        return parentAccountId;
    }

    public void setParentAccountId(Long parentAccountId) {
        this.parentAccountId = parentAccountId;
    }

    public Integer getAuthority() {
        return authority;
    }

    public void setAuthority(Integer authority) {
        this.authority = authority;
    }

    public ArrayList<String> getChildrenOrgs() {
        return childrenOrgs;
    }

    public void setChildrenOrgs(ArrayList<String> childrenOrgs) {
        this.childrenOrgs = childrenOrgs;
    }

    public void ChildrenOrgsAdd(String orgName){
        this.childrenOrgs.add(orgName);
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}
