package cn.cdsoft.common.mq.rabbitmq;

public class Alarm {
	Long ch_id;
	String occur_time;
	Long alarm_level; //1~4，1级最高，4级最低
	Long alarm_type;
	String alarm_info;
	Long alarm_log_id;
	public Long getCh_id() {
		return ch_id;
	}
	public void setCh_id(Long ch_id) {
		this.ch_id = ch_id;
	}
	public String getOccur_time() {
		return occur_time;
	}
	public void setOccur_time(String occur_time) {
		this.occur_time = occur_time;
	}
	public Long getAlarm_level() {
		return alarm_level;
	}
	public void setAlarm_level(Long alarm_level) {
		this.alarm_level = alarm_level;
	}
	public Long getAlarm_type() {
		return alarm_type;
	}
	public void setAlarm_type(Long alarm_type) {
		this.alarm_type = alarm_type;
	}
	public String getAlarm_info() {
		return alarm_info;
	}
	public void setAlarm_info(String alarm_info) {
		this.alarm_info = alarm_info;
	}
	public Long getAlarm_log_id() {
		return alarm_log_id;
	}
	public void setAlarm_log_id(Long alarm_log_id) {
		this.alarm_log_id = alarm_log_id;
	}
	
	
}
