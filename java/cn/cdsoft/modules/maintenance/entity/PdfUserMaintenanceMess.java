package cn.cdsoft.modules.maintenance.entity;

import cn.cdsoft.common.persistence.DataEntity;

/**
 * Created by Administrator on 2018-12-25.
 */
public class PdfUserMaintenanceMess extends DataEntity<PdfUserMaintenanceMess>{
	
	private static final long serialVersionUID = 1L;
	
	
	private String orgParentId; //管辖id
	private String orgParentName; //管辖名称
	private String orgId;  //配电房id
	private String orgName;  //配电房名称
	private String codeName;  //设备类型名称
	private String codeIds;   //设备类型全
	private String codeId;   
	private String typeId;
	private String ourName;  //我方人员名称
	private String PhoneOne;  //我方人员电话1
	private String maintenanceName;  //维保公司名称
	private String timeStart;   //维保开始时间
	private String timeStop;   //维保结束时间
	private String notUse;
	private String isEnable;
	
	public PdfUserMaintenanceMess(String orgParentId, String orgId, String orgName, String codeName,
			String codeIds,String codeId, String typeId, String ourName, String phoneOne, String maintenanceName, String timeStart,
			String timeStop) {
		super();
		this.isEnable = isEnable;
		this.notUse = notUse;
		this.orgParentId = orgParentId;
		this.orgId = orgId;
		this.orgName = orgName;
		this.codeName = codeName;
		this.codeIds = codeIds;
		this.codeId = codeId;
		this.typeId = typeId;
		this.ourName = ourName;
		this.PhoneOne = phoneOne;
		this.maintenanceName = maintenanceName;
		this.timeStart = timeStart;
		this.timeStop = timeStop;
	}


	public PdfUserMaintenanceMess() {
		super();
	}

	
	
	

	public String getNotUse() {
		return notUse;
	}


	public void setNotUse(String notUse) {
		this.notUse = notUse;
	}


	public String getIsEnable() {
		return isEnable;
	}


	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}


	public String getCodeIds() {
		return codeIds;
	}


	public void setCodeIds(String codeIds) {
		this.codeIds = codeIds;
	}


	public String getOrgParentId() {
		return orgParentId;
	}


	public void setOrgParentId(String orgParentId) {
		this.orgParentId = orgParentId;
	}


	public String getOrgId() {
		return orgId;
	}


	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}


	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}


	public String getOrgParentName() {
		return orgParentName;
	}


	public void setOrgParentName(String orgParentName) {
		this.orgParentName = orgParentName;
	}


	public String getCodeName() {
		return codeName;
	}


	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}


	public String getCodeId() {
		return codeId;
	}


	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}


	public String getTypeId() {
		return typeId;
	}


	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}


	public String getOurName() {
		return ourName;
	}


	public void setOurName(String ourName) {
		this.ourName = ourName;
	}


	public String getPhoneOne() {
		return PhoneOne;
	}


	public void setPhoneOne(String phoneOne) {
		PhoneOne = phoneOne;
	}


	public String getMaintenanceName() {
		return maintenanceName;
	}


	public void setMaintenanceName(String maintenanceName) {
		this.maintenanceName = maintenanceName;
	}


	public String getTimeStart() {
		return timeStart;
	}


	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}


	public String getTimeStop() {
		return timeStop;
	}


	public void setTimeStop(String timeStop) {
		this.timeStop = timeStop;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PdfUserMaintenanceMess [id=");
		builder.append(id);
		builder.append(", orgParentId=");
		builder.append(orgParentId);
		builder.append(", orgId=");
		builder.append(orgId);
		builder.append(", orgName=");
		builder.append(orgName);
		builder.append(", codeName=");
		builder.append(codeName);
		builder.append(", codeId=");
		builder.append(codeId);
		builder.append(", typeId=");
		builder.append(typeId);
		builder.append(", ourName=");
		builder.append(ourName);
		builder.append(", PhoneOne=");
		builder.append(PhoneOne);
		builder.append(", maintenanceName=");
		builder.append(maintenanceName);
		builder.append(", timeStart=");
		builder.append(timeStart);
		builder.append(", timeStop=");
		builder.append(timeStop);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
	
	
	
	
	
	
    
}
