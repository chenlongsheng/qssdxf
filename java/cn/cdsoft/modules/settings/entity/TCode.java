/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.entity;

import javax.validation.constraints.NotNull;

import cn.cdsoft.common.persistence.DataEntity;
import cn.cdsoft.common.utils.excel.annotation.ExcelField;

/**
 * code管理Entity
 * @author long
 * @version 2018-08-09
 */
public class TCode extends DataEntity<TCode> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// Code名称
	private Long typeId;		// Code类型ID
	private Long orderNo;		// 排序号
	private String monad;		// 单位符号
	private String iconSkin;		// 图标
	private String warnIconSkin;		// 报警图标
	private String offlineIconSkin;	    //离线图标
	private String defenceIconSkin;		// 布防图标
	private String withdrawingIconSkin;		//撤防图标
	private String sidewayIconSkin;		// 旁路图标
	
	private Long codeId;
	
	private TCodeType tCodeType;
	
	public TCode() {
		super();
	}

	public TCode(String id){
		super(id);
	}

	
	public Long getCodeId() {
		return codeId;
	}

	public void setCodeId(Long codeId) {
		this.codeId = codeId;
	}

	public TCodeType gettCodeType() {
		return tCodeType;
	}

	public void settCodeType(TCodeType tCodeType) {
		this.tCodeType = tCodeType;
	}

	@ExcelField(title="离线图标", align=2, sort=7)
	public String getOfflineIconSkin() {
		return offlineIconSkin;
	}

	public void setOfflineIconSkin(String offlineIconSkin) {
		this.offlineIconSkin = offlineIconSkin;
	}
	
	

	public String getDefenceIconSkin() {
		return defenceIconSkin;
	}

	public void setDefenceIconSkin(String defenceIconSkin) {
		this.defenceIconSkin = defenceIconSkin;
	}

	public String getWithdrawingIconSkin() {
		return withdrawingIconSkin;
	}

	public void setWithdrawingIconSkin(String withdrawingIconSkin) {
		this.withdrawingIconSkin = withdrawingIconSkin;
	}

	public String getSidewayIconSkin() {
		return sidewayIconSkin;
	}

	public void setSidewayIconSkin(String sidewayIconSkin) {
		this.sidewayIconSkin = sidewayIconSkin;
	}

	@ExcelField(title="Code名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="Code类型ID不能为空")
	@ExcelField(title="Code类型ID", align=2, sort=2)
	public Long getTypeId() {
		return typeId;
	}
    
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	
	@ExcelField(title="排序号", align=2, sort=3)
	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}
	
	@ExcelField(title="单位符号", align=2, sort=4)
	public String getMonad() {
		return monad;
	}

	public void setMonad(String monad) {
		this.monad = monad;
	}
	
	@ExcelField(title="图标", align=2, sort=5)
	public String getIconSkin() {
		return iconSkin;
	}

	public void setIconSkin(String iconSkin) {
		this.iconSkin = iconSkin;
	}
	
	@ExcelField(title="报警图标", align=2, sort=6)
	public String getWarnIconSkin() {
		return warnIconSkin;
	}

	public void setWarnIconSkin(String warnIconSkin) {
		this.warnIconSkin = warnIconSkin;
	}
	
}