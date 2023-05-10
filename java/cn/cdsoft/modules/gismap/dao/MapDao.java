package cn.cdsoft.modules.gismap.dao;

import cn.cdsoft.common.persistence.annotation.MyBatisDao;
import cn.cdsoft.modules.gismap.vo.MapConfigAddVO;
import cn.cdsoft.modules.gismap.vo.MapConfigPoint;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author LiJianRong
 * @date 2019/2/13.
 */
@MyBatisDao
public interface MapDao {

    /**
     * 生成添加记录
     * @param
     * @param createBy
     */
    void generateRecord(@Param("orgId") String orgId,@Param("codePath")String codePath, @Param("createBy") String createBy,@Param("createDate")String createDate);

    /**
     * 更新添加记录
     * @param orgId
     * @param codePath
     * @param updateBy
     * @param updateDate
     */
    void updateRecord(@Param("orgId") String orgId,@Param("codePath")String codePath, @Param("updateBy") String updateBy,@Param("updateDate")String updateDate);

    /**
     * 删除地图
     * @param id
     */
    void delete(@Param("orgId") String id);

    /**
     * 查找省区域
     * @return
     */
    List<JSONObject> findProvinceList();

    /**
     * 查找市区域
     * @return
     */
    List<JSONObject> findCityList();

    /**
     * 根据区域查询code
     * @param id
     * @return
     */
    String findCodeByOrgId(@Param("orgId") String id);

    /**
     * 查询这个区域是否有地图
     * @param orgId
     * @return
     */
    Integer checkHasMap(@Param("orgId") String orgId);

    /**
     * 删除地图配置
     * @param orgId
     */
    void deleteMapConfig(@Param("orgId") String orgId);

    /**
     * 更新地图配置
     * @param vo
     */
    void updateMapConfig(MapConfigAddVO vo);

    /**
     * 保存地图配置
     * @param point
     */
    void saveMapConfig(MapConfigPoint point);

    /**
     * 查询该地图的
     * @param orgId
     * @return
     */
    List<JSONObject> showConfig(@Param("orgId") String orgId);
}
