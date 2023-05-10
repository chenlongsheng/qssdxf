/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.entity;


import cn.cdsoft.common.persistence.DataEntity;
import cn.cdsoft.common.utils.excel.annotation.ExcelField;

/**
 * CodeType管理Entity
 * @author ywk
 * @version 2018-06-22
 */
public class TCodeType extends DataEntity<TCodeType> {
	
	private static final long serialVersionUID = 1L;
	private String typeName;		// 类型名称
	private String typeDesc;		// 类型描述
	
	public TCodeType() {
		super();
	}

	public TCodeType(String id){
		super(id);
	}

	@ExcelField(title="类型名称", align=2, sort=1)
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	@ExcelField(title="类型描述", align=2, sort=2)
	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	
}