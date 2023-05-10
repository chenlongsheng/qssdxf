package cn.cdsoft.modules.gismap.service;

import cn.cdsoft.common.service.ServiceResult;
import cn.cdsoft.common.utils.*;
import cn.cdsoft.modules.gismap.dao.MapDao;
import cn.cdsoft.modules.gismap.vo.MapConfigAddVO;
import cn.cdsoft.modules.gismap.vo.MapConfigLine;
import cn.cdsoft.modules.gismap.vo.MapConfigPoint;
import cn.cdsoft.modules.sys.utils.UserUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.*;

/**
 * @author LiJianRong
 * @date 2019/2/13.
 */
@Service
public class MapService {

    @Value("${fileRoot}")
    private String fileRoot;

    @Autowired
    private MapDao mapDao;

    public List<JSONObject> findOrgList() {
        List<JSONObject> provinceList = mapDao.findProvinceList();
        List<JSONObject> cityList = mapDao.findCityList();      //有地图的城市会有 codePath字段不为空
        for (JSONObject province : provinceList) {
            String provinceId = province.getString("id");
            List<JSONObject> childList = new ArrayList<>();
            for (int i = 0; i < cityList.size(); i++) {
                JSONObject city = cityList.get(i);
                if(city.getString("parentId").equals(provinceId)){
                    childList.add(city);
                    cityList.remove(i);
                    i--;
                }
            }
            province.put("city",childList);
        }
        return provinceList;
    }

    public ServiceResult uploadMap(HttpServletRequest request) {
        ServiceResult result = new ServiceResult();
        String orgId = request.getParameter("orgId");
        deleteMap(orgId);    //先删除地图
        ServiceResult uploadResult = upload(request);   //上传地图
        boolean isSuccess = uploadResult.isSuccess();
        if(isSuccess){  //保存成功
            JSONObject data = (JSONObject) uploadResult.getData();
            String codePath = data.getString("codePath");
            String createBy = UserUtils.getUser().getId();
            String createDate = DateUtils.getDateTime();
            Integer count = mapDao.checkHasMap(orgId);
            if(count > 0){  //更新保存记录
                mapDao.updateRecord(orgId,codePath,createBy,createDate);
                result.setMessage("更新成功");
            }else{  //保存添加记录
                mapDao.generateRecord(orgId,codePath,createBy,createDate);
                result.setMessage("添加成功");
            }
            result.setSuccess(true);
        }else{
            result.setMessage("添加失败");
        }
        return result;
    }

    private ServiceResult upload(HttpServletRequest request){
        ServiceResult result = new ServiceResult();
        String code = request.getParameter("code");
        String orgId = request.getParameter("orgId");
        if(StringUtils.isNotBlank(code) && code.length() % 2 != 0){
            result.setMessage("不符合条件的参数");
            return result;
        }
        String fileName = null;
        File targetFile = null;
        try {
            if (request instanceof MultipartHttpServletRequest) {
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
                Set<Map.Entry<String, MultipartFile>> set = fileMap.entrySet();     //传输的文件列表
                if(set.size() > 1){
                    result.setMessage("不支持批量上传");
                    return result;
                }
                if (set.size() == 0) {
                    result.setMessage("参数无效");
                    return result;
                }
                String dateString = DateUtils.getDateTime("yyyyMMddHHmmss");
                JSONObject data = new JSONObject();
                String basePath = fileRoot;     //基础目录 /data/ptxy
                String codePath = code2Path(code);  //区域code 目录
                String path = basePath + codePath;
                for (Map.Entry<String, MultipartFile> item : set) {     //只支持单上传
                    String key = item.getKey();             //文件code
                    MultipartFile file = item.getValue();   //上传的文件
                    String extension = FilenameUtils.getExtension(file.getOriginalFilename());  //后缀
                    fileName = key + "-" + dateString + "." + extension;
                    targetFile = new File(basePath+"/zip", fileName);
                    if (!targetFile.exists()) {
                        targetFile.mkdirs();
                    }
                    file.transferTo(targetFile);    //上传文件

                    if("zip".equals(extension)){    //判断是否是zip
                        FileUtils.unZipFiles(basePath+"/zip/"+fileName,path);
                        data.put("codePath",codePath);
                        data.put("orgId",orgId);
                        result.setData(data);
                        result.setMessage("保存成功");
                    }else{
                        result.setMessage("只支持zip压缩包");
                        return result;
                    }
                }
            } else {
                result.setMessage("参数无效");
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("参数无效");
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    public ServiceResult deleteMap(String id) {
        ServiceResult result = new ServiceResult();
        if(StringUtils.isBlank(id)){
            result.setMessage("id不能为空");
            return result;
        }
        mapDao.delete(id);      //删除记录
        String code = mapDao.findCodeByOrgId(id);    //查询id对应的code
        if(StringUtils.isBlank(code) || code.length() < 2){
            result.setMessage("参数错误");
            return result;
        }
        String headPath = fileRoot + code2Path(code);   //文件路径
        DeleteFileUtil.delete(headPath);    //删除文件
        result.setMessage("删除成功");
        return result;
    }

    //区域code转路径
    private String code2Path(String code){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < code.length(); i++) {
            if(i%2 == 0){
                sb.append("/");
            }
            sb.append(code.charAt(i));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception{
        JSONObject a = new JSONObject();
        a.put("a","a");
        JSONObject b = new JSONObject();
        b.put("b","b");
        JSONArray array = new JSONArray();
        array.add(a);
        array.add(b);
        String result = JSON.toJSONString(array);
        System.out.println(result);
    }

    public ServiceResult configMap(MapConfigAddVO vo) {
        ServiceResult result = new ServiceResult();
        String orgId = vo.getOrgId();       //区域id
        if(StringUtils.isNotBlank(orgId)){
            mapDao.deleteMapConfig(orgId);   //先删除原来的配置
        }
        List<MapConfigLine> lineList =vo.getLineList(); //线列表
        if(lineList != null && lineList.size() > 0){
            for (MapConfigLine line : lineList) {
                List<MapConfigPoint> pointList = line.getPointList();   //点列表
                String lineId = IdGenSnowFlake.uuid()+"";
                if(pointList != null && pointList.size() > 0){
                    int i = 0;
                    for (MapConfigPoint point : pointList) {
                        point.setId(IdGenSnowFlake.uuid()+"");
                        point.setOrgId(orgId);
                        point.setLineId(lineId);
                        point.setSort(i);
                        mapDao.saveMapConfig(point);
                        i++;
                    }
                }
            }
        }
        result.setMessage("保存成功");
        result.setSuccess(true);
        return result;
    }

    public JSONObject showConfig(String orgId) {
        if(StringUtils.isBlank(orgId)){
            return null;
        }
        List<JSONObject> pointList = mapDao.showConfig(orgId);
        List<List<JSONObject>> resultList = new ArrayList<>();
        while(pointList.size() > 0){
            String lineId = pointList.get(0).getString("lineId");
            List<JSONObject> list = new ArrayList<>();
            for (int i = 0; i < pointList.size(); i++) {
                JSONObject point = pointList.get(i);
                if(lineId.equals(point.getString("lineId"))){
                    list.add(point);
                }
            }
            resultList.add(list);
            pointList.removeAll(list);
        }
        JSONObject result = new JSONObject();
        result.put("resultList",resultList);
        return result;
    }
}
