package cn.cdsoft.modules.maintenance.entity;

public class PdfMaintenanceCode {
	
	private static final long serialVersionUID = 1L;

	private String id;
	 //维保单位id
    private String maintenanceId;
    private String orgParentId;
	private String mainDetailId;
	private String orgId;
	private String codeId;
	private String typeId;
	private String orderNo;
	private String updateDate;

	private String codeIds;

	public PdfMaintenanceCode() {
		super();
	}

	public PdfMaintenanceCode(String id, String maintenanceId,String mainDetailId, String orgParentId,String orgId, String codeId, String typeId,
			String orderNo, String updateDate, String codeIds) {
		super();
		this.id = id;
		this.maintenanceId =maintenanceId;
		this.mainDetailId = mainDetailId;
		this.orgParentId = orgParentId;
		this.orgId = orgId;
		this.codeId = codeId;
		this.typeId = typeId;
		this.orderNo = orderNo;
		this.updateDate = updateDate;
		this.codeIds = codeIds;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrgParentId() {
		return orgParentId;
	}

	public void setOrgParentId(String orgParentId) {
		this.orgParentId = orgParentId;
	}

	public String getMainDetailId() {
		return mainDetailId;
	}

	public void setMainDetailId(String mainDetailId) {
		this.mainDetailId = mainDetailId;
	}

	public String getMaintenanceId() {
		return maintenanceId;
	}

	public void setMaintenanceId(String maintenanceId) {
		this.maintenanceId = maintenanceId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
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

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getCodeIds() {
		return codeIds;
	}

	public void setCodeIds(String codeIds) {
		this.codeIds = codeIds;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PdfMaintenanceCode [id=");
		builder.append(id);
		builder.append(", mainDetailId=");
		builder.append(mainDetailId);
		builder.append(", orgId=");
		builder.append(orgId);
		builder.append(", codeId=");
		builder.append(codeId);
		builder.append(", typeId=");
		builder.append(typeId);
		builder.append(", orderNo=");
		builder.append(orderNo);
		builder.append(", updateDate=");
		builder.append(updateDate);
		builder.append(", codeIds=");
		builder.append(codeIds);
		builder.append("]");
		return builder.toString();
	}

}
