/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.entity;

import javax.validation.constraints.NotNull;

import cn.cdsoft.common.persistence.DataEntity;
import cn.cdsoft.common.utils.excel.annotation.ExcelField;
import cn.cdsoft.modules.sys.entity.Area;

/**
 * 运营管理Entity
 * @author long
 * @version 2018-08-17
 */
public class TOrgExecUnitBind extends DataEntity<TOrgExecUnitBind> {
	
	private static final long serialVersionUID = 1L;
	private String orgId;		// 区域id
	private String execUnitId;		// 运行单元id
	private String orgName;
	private String execUnitName;

	
	public TOrgExecUnitBind() {
		super();
	}
    
	public TOrgExecUnitBind(String id){
		super(id);
	}
	

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	

	@NotNull(message="区域id不能为空")
	@ExcelField(title="区域id", align=2, sort=1)
	public String getOrgId() {
		return orgId;
	}

	public String getExecUnitName() {
		return execUnitName;
	}

	public void setExecUnitName(String execUnitName) {
		this.execUnitName = execUnitName;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@NotNull(message="运行单元id不能为空")
	@ExcelField(title="运行单元id", align=2, sort=2)
	public String getExecUnitId() {
		return execUnitId;
	}

	public void setExecUnitId(String execUnitId) {
		this.execUnitId = execUnitId;
	}
	
}