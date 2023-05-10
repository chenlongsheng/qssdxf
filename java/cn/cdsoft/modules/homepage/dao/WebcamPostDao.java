/**
 * 
 */
package cn.cdsoft.modules.homepage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;

/**
 * @author admin
 *
 */
@MyBatisDao
public interface WebcamPostDao {

	
	
	
	void updataWebcam(@Param("realValue") String realValue,@Param("warn") String warn,@Param("sn") String sn);
	
	String getWebcam();
	
	List<MapEntity> getVideos();
	
}
