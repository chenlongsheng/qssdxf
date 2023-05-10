package cn.cdsoft.common.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;

import javax.xml.bind.annotation.XmlTransient;
import java.util.HashMap;
import java.util.Map;


public class MapEntity extends HashMap<String,Object>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 当前实体分页对象
	 */
	protected Page<MapEntity> page;
	
	/**
	 * 自定义SQL（SQL标识，SQL内容）
	 */
	protected Map<String, String> sqlMap;
	
	@JsonIgnore
	@XmlTransient
	public Page<MapEntity> getPage() {
		if (page == null){
			page = new Page<MapEntity>();
		}
		return page;
	}
	
	public Page<MapEntity> setPage(Page<MapEntity> page) {
		this.page = page;
		return page;
	}

	@JsonIgnore
	@XmlTransient
	public Map<String, String> getSqlMap() {
		if (sqlMap == null){
			sqlMap = Maps.newHashMap();
		}
		return sqlMap;
	}

	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}
}
