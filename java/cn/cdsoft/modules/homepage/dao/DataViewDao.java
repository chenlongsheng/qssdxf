package cn.cdsoft.modules.homepage.dao;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;
import cn.cdsoft.modules.homepage.vo.HourDataFindVO;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * Created by ZZUSER on 2019/2/14.
 */
@MyBatisDao
public interface DataViewDao {

    /**
     * 根据楼栋获取到所有水位水压设备
     * @param orgId
     * @return
     */
    List<JSONObject> getWaterDevByBuild(String orgId);

    /**
     * 根据设备id获取某一天
     * @param vo
     * @return
     */
    List<JSONObject> getHourDataByDev(HourDataFindVO vo);

    /**
     * 获取一个时间段内每天的平均数据
     * @param vo
     * @return
     */
    List<JSONObject> getAvgData(HourDataFindVO vo);
    
    List<MapEntity>  getAllWater();
    
    
   void  deleteHistoryData();
}
