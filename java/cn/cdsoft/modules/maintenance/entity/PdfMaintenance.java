package cn.cdsoft.modules.maintenance.entity;

import cn.cdsoft.common.persistence.DataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-12-25.
 */
public class PdfMaintenance extends DataEntity<PdfMaintenance> {
    //维保单位名称
    private String name;
    //0.消防  1.安防
    private String type;
    //单位地址
    private String address;
    //0.启用  1.禁用
    private String isEnable;
    //备注
    private String remark;
    
    private String orgParentId;
    
    private String aptitude;
    
    private String codeIds;
    
    private String codeId;
    private String typeId;
    
    private String timeStart;
    private String timeStop;
    private String contacts;
    private String phone;

//    private String aptitudeId;
    //维保详细
    private List<PdfMaintenanceDetail> list = new ArrayList<PdfMaintenanceDetail>();
    //资质信息
    private List<String> url = new ArrayList<String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    

//    public String getAptitudeId() {
//		return aptitudeId;
//	}
//
//	public void setAptitudeId(String aptitudeId) {
//		this.aptitudeId = aptitudeId;
//	}

	public String getAddress() {
        return address;
    }

    public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public void setAddress(String address) {
        this.address = address;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void listAdd(PdfMaintenanceDetail detail){
        this.list.add(detail);
    }

    public List<PdfMaintenanceDetail> getList() {
        return list;
    }

    public void setList(List<PdfMaintenanceDetail> list) {
        this.list = list;
    }

    public void urlAdd(String url){
        this.url.add(url);
    }

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }

	public String getOrgParentId() {
		return orgParentId;
	}

	public void setOrgParentId(String orgParentId) {
		this.orgParentId = orgParentId;
	}

	public String getAptitude() {
		return aptitude;
	}

	public void setAptitude(String aptitude) {
		this.aptitude = aptitude;
	}

	public String getCodeIds() {
		return codeIds;
	}

	public void setCodeIds(String codeIds) {
		this.codeIds = codeIds;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
    
    
}
