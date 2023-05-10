package cn.cdsoft.modules.settings.web;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.utils.MyBeanUtils;
import cn.cdsoft.common.utils.ServletUtils;
import cn.cdsoft.common.utils.StringUtils;
import cn.cdsoft.modules.settings.entity.TChannel;
import cn.cdsoft.modules.settings.entity.TDevice;
import cn.cdsoft.modules.settings.service.EMapConfigService;
import cn.cdsoft.modules.settings.service.TChannelService;
import cn.cdsoft.modules.settings.service.TCodeService;
import cn.cdsoft.modules.settings.service.TDeviceService;
import cn.cdsoft.modules.sys.entity.Area;
import cn.cdsoft.modules.sys.security.exception.NoAccountException;
import cn.cdsoft.modules.sys.service.AreaService;

@Controller
@RequestMapping(value = "${adminPath}/settings/map")
public class EMapConfigController {

    @Autowired
    private EMapConfigService eMapConfigService;
    @Autowired
    private TDeviceService tDeviceService;
    @Autowired
    private TChannelService tChannelService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private TCodeService tCodeService;

    // 获取区域id==TChannel
    @RequestMapping(value = { "getChannelListByOrgId" })
    @ResponseBody
    public String getChannelListByOrgId(HttpServletRequest request, HttpServletResponse response, String orgId) {

        return ServletUtils.buildRs(true, "", eMapConfigService.getChannelListByOrgId(orgId));
    }

    // 获取区域id==AudTDevice
    @RequestMapping(value = { "getDeviceListByOrgId" })
    @ResponseBody
    public String getDeviceListByOrgId(HttpServletRequest request, HttpServletResponse response, String orgId) {
        System.out.println("orgId======" + orgId);
        return ServletUtils.buildRs(true, "", eMapConfigService.getDeviceListByOrgId(orgId));
    }

    // 保存坐标==TChannel
    @RequestMapping(value = { "saveChannelCoordsXCoordsY" }, method = RequestMethod.POST)
    @ResponseBody
    public String saveChannelCoordsXCoordsY(String channellist) throws Exception {
        System.out.println(channellist);

        JSONArray ja = JSONArray.parseArray(channellist);
        for (int i = 0; i < ja.size(); i++) {
            TChannel tChannel = JSONObject.parseObject(ja.get(i).toString(), TChannel.class);
            TChannel t = tChannelService.get(tChannel.getId());// 从数据库取出记录的值
            MyBeanUtils.copyBeanNotNull2Bean(tChannel, t);
            tChannelService.save(t);
        }
        return ServletUtils.buildRs(true, "保存坐标成功!", channellist);
    }

    // 保存坐标==AudTDevice
    @RequestMapping(value = { "saveDeviceCoordsXCoordsY" }, method = RequestMethod.POST)
    @ResponseBody
    public String saveDeviceCoordsXCoordsY(String devicelist) throws Exception {
        System.out.println("nihao");
        System.out.println(devicelist);
        JSONArray ja = JSONArray.parseArray(devicelist);
        for (int i = 0; i < ja.size(); i++) {
            TDevice tDevice = JSONObject.parseObject(ja.get(i).toString(), TDevice.class);
            TDevice t = tDeviceService.get(tDevice.getId());// 从数据库取出记录的值
            MyBeanUtils.copyBeanNotNull2Bean(tDevice, t);
            tDeviceService.save(t);
        }
        return ServletUtils.buildRs(true, "保存坐标成功!", devicelist);
    }

    // 上传图片
    @RequestMapping(value = { "upLoadPic" })
    @ResponseBody
    public JSONObject upLoadPic(String areaId, HttpServletRequest request, MultipartFile imgFile,
            HttpServletResponse response) throws IllegalStateException, IOException {

        long startTime = System.currentTimeMillis(); // 获取开始时间

        // 获取文件原始名称
        String originalFilename = imgFile.getOriginalFilename();

        String result = originalFilename.substring(originalFilename.length() - 4, originalFilename.length());

        String str = "89504E47 FFD8FF 47494638 255044462D312E";
        byte[] byteArr = imgFile.getBytes();
        InputStream is = new ByteArrayInputStream(byteArr);
        byte[] b = new byte[3];
        is.read(b, 0, b.length);
        String xxx = tCodeService.bytesToHexString(b);
        xxx = xxx.toUpperCase();
        System.out.println("头文件是：" + xxx);

        boolean status = str.contains(xxx);
        if (!status) {
            throw new NoAccountException();
        }

        if (result.equals(".png") || result.equals(".jpg")) {

        } else {
            throw new NoAccountException();
        }
        // 上传图片
        if (imgFile != null && originalFilename != null && originalFilename.length() > 0) {
            // 存储图片的物理路径
            String pic_path = request.getSession().getServletContext().getRealPath("");
//			String pic_path = fileRoot;
            // 新的图片名称
            String newFileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
            File file = new File(pic_path + "/static_modules/emap_upload/");
            // 如果文件夹不存在则创建
            if (!file.exists() && !file.isDirectory()) {
                file.mkdir();
            }
            System.out.println(pic_path + "/static_modules/emap_upload/");
            // 新图片
            File newFile = new File(pic_path + "/static_modules/emap_upload/" + newFileName);

            // 将内存中的数据写入磁盘
            imgFile.transferTo(newFile);
            Area area = new Area();
            area.setId(areaId);
            area.setImage(newFileName);
            areaService.saveImage(area);
        }
        JSONObject json = new JSONObject();
        json.put("suc", "成功上传");
        long endTime = System.currentTimeMillis(); // 获取结束时间
        // 耗时(秒)
        System.out.println("时间" + new BigDecimal(endTime - startTime).divide(new BigDecimal(1000)).doubleValue());
        return json;
    }

    // 下载电子配置图片
    @RequestMapping(value = { "getPic" }, method = RequestMethod.POST)
    @ResponseBody
    public String getpic(String areaId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(areaId);
        Area area = areaService.get(areaId);
//		String image = "static_modules/emap_upload/" + area.getImage();
        String image = "/static_modules/emap_upload/" + area.getImage();
        System.out.println(image);
        String path = request.getSession().getServletContext().getRealPath("") + image;

        System.out.println(path);
        File file = new File(path);
        System.out.println(file.length());
        if (StringUtils.isBlank(area.getImage())) {
            return "false";
        }
        return image;
    }

    // 设备的类型的
    @RequestMapping(value = { "eMapList" }, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject eMapList(String orgId) {

        JSONObject json = new JSONObject();
        List<MapEntity> list = eMapConfigService.eMapSelect(orgId);
        json.put("list", list);
        return json;
    }

    // 通道的类型的
    @RequestMapping(value = { "eMapChannelList" }, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject eMapChannelList(String orgId) {
        System.out.println(orgId + "-------通道t_code列表");
        JSONObject json = new JSONObject();
        List<MapEntity> list = eMapConfigService.eMapChannelSelect(orgId);
        json.put("list", list);
        return json;
    }

}
