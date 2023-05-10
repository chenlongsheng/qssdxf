/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.maintenance.entity;


import cn.cdsoft.common.persistence.DataEntity;
import cn.cdsoft.common.utils.excel.annotation.ExcelField;

/**
 * 我方人员管理Entity
 * @author long
 * @version 2019-01-09
 */
public class PdfUser extends DataEntity<PdfUser> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名字
	private Long phoneOne;		// 电话1
	private Long phoneTwo;		// 电话2
	private String departOrgId;   //电业局id
	private String positionName;		// 所属电业局
	private String orgName;
	private String departOrgName;  //电业局名称
	private String remarks;
	private Long positionId;		// 所属岗位
	private Long notUse;		// 启用禁用
	private String orgId; //配电房id
	
	private String codeId;
	private String typeId;
	
	
	public PdfUser() {
		super();
	}

	public PdfUser(String id){
		super(id);
	}

	@ExcelField(title="名字", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	@ExcelField(title="电话1", align=2, sort=2)
	public Long getPhoneOne() {
		return phoneOne;
	}

	public void setPhoneOne(Long phoneOne) {
		this.phoneOne = phoneOne;
	}
	
	@ExcelField(title="电话2", align=2, sort=3)
	public Long getPhoneTwo() {
		return phoneTwo;
	}

	public void setPhoneTwo(Long phoneTwo) {
		this.phoneTwo = phoneTwo;
	}
	

	
	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	

	public String getDepartOrgName() {
		return departOrgName;
	}

	public void setDepartOrgName(String departOrgName) {
		this.departOrgName = departOrgName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@ExcelField(title="所属岗位", align=2, sort=5)
	public Long getPositionId() {
		return positionId;
	}

	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}
	
	@ExcelField(title="启用禁用", align=2, sort=6)
	public Long getNotUse() {
		return notUse;
	}

	public void setNotUse(Long notUse) {
		this.notUse = notUse;
	}

	public String getDepartOrgId() {
		return departOrgId;
	}

	public void setDepartOrgId(String departOrgId) {
		this.departOrgId = departOrgId;
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
    

}