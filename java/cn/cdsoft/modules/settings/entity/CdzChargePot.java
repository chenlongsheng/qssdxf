package cn.cdsoft.modules.settings.entity;

import cn.cdsoft.common.persistence.DataEntity;

/**
 * Created by Administrator on 2018-08-14.
 */
public class CdzChargePot extends DataEntity<CdzChargePot>{

	private static final long serialVersionUID = 1L;	
	
	//管理员id
    private String adminUserId;

    //姓名
	private String name;

	//地区名称
	private String OrgName;


	public String getAdminUserId() {
		return adminUserId;
	}

	public void setAdminUserId(String adminUserId) {
		this.adminUserId = adminUserId;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrgName() {
		return OrgName;
	}

	public void setOrgName(String orgName) {
		OrgName = orgName;
	}
}
