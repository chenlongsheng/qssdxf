package cn.cdsoft.modules.gismap.web;

import cn.cdsoft.common.service.ServiceResult;
import cn.cdsoft.common.utils.ServletUtils;
import cn.cdsoft.modules.gismap.service.MapService;
import cn.cdsoft.modules.gismap.vo.MapConfigAddVO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author LiJianRong
 * @date 2019/2/13.
 */
@Controller
@RequestMapping(value="${adminPath}/map")
public class MapController {

    @Autowired
    private MapService mapService;


    /**
     * 查询区域列表(只含省市)
     * @return
     */
    @RequestMapping(value="findOrgList")
    @ResponseBody
    public JSONObject findOrgList(){
        List<JSONObject> list = mapService.findOrgList();
        return ServletUtils.buildJsonRs(true,"",list);
    }

    /**
     * 上传地图
     * @param request
     * @return
     */
    @RequestMapping(value="up")
    @ResponseBody
    public JSONObject uploadMap(HttpServletRequest request){
        ServiceResult result = mapService.uploadMap(request);
        return ServletUtils.buildJsonRs(result.isSuccess(),result.getMessage(),result.getData());
    }

    /**
     * 删除地图
     * @param id
     * @return
     */
    @RequestMapping(value="delete")
    @ResponseBody
    public JSONObject deleteMap(String id){
        ServiceResult result = mapService.deleteMap(id);
        return ServletUtils.buildJsonRs(result.isSuccess(),result.getMessage(),result.getData());
    }

    /**
     * 配置地图
     * @param vo
     * @return
     */
    @RequestMapping(value="configMap")
    @ResponseBody
    public JSONObject configMap(@RequestBody MapConfigAddVO vo){
        ServiceResult result = mapService.configMap(vo);
        return ServletUtils.buildJsonRs(result.isSuccess(),result.getMessage(),result.getData());
    }

    /**
     * 展示地图配置
     * @param orgId
     * @return
     */
    @RequestMapping(value="showConfig")
    @ResponseBody
    public JSONObject showConfig(String orgId){
        JSONObject result = mapService.showConfig(orgId);
        return ServletUtils.buildJsonRs(true,"",result);
    }
}
