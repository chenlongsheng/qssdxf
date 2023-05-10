package cn.cdsoft.common.mq.rabbitmq;

import java.util.List;

public class DevData {

	Long dev_id;
	Long status;
	String occur_time;
	List<ChData> items;
	public Long getDev_id() {
		return dev_id;
	}
	public void setDev_id(Long dev_id) {
		this.dev_id = dev_id;
	}
	public List<ChData> getItems() {
		return items;
	}
	public void setItems(List<ChData> items) {
		this.items = items;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public String getOccur_time() {
		return occur_time;
	}
	public void setOccur_time(String occur_time) {
		this.occur_time = occur_time;
	}
	
	
}
