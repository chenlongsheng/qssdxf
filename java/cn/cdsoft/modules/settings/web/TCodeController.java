/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.web;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import cn.cdsoft.common.config.Global;
import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.common.utils.DateUtils;
import cn.cdsoft.common.utils.MyBeanUtils;
import cn.cdsoft.common.utils.StringUtils;
import cn.cdsoft.common.utils.excel.ExportExcel;
import cn.cdsoft.common.utils.excel.ImportExcel;
import cn.cdsoft.common.web.BaseController;
import cn.cdsoft.modules.settings.entity.TChannel;
import cn.cdsoft.modules.settings.entity.TCode;
import cn.cdsoft.modules.settings.entity.TCodeType;
import cn.cdsoft.modules.settings.service.TCodeService;
import cn.cdsoft.modules.settings.service.TCodeTypeService;
import cn.cdsoft.modules.settings.service.TDeviceService;
import cn.cdsoft.modules.sys.entity.Area;
import cn.cdsoft.modules.sys.security.exception.NoAccountException;
import cn.cdsoft.modules.sys.utils.UserUtils;

/**
 * code管理Controller
 * 
 * @author long
 * @version 2018-08-09
 */
@Controller
@RequestMapping(value = "${adminPath}/settings/tCode")
public class TCodeController extends BaseController {

    @Autowired
    private TCodeService tCodeService;
    @Autowired
    private TCodeTypeService tCodeTypeService;

    /**
     * code管理列表页面
     */
    @RequiresPermissions("settings:tCode:list")
    @RequestMapping(value = { "list", "" })
    public String list(String id, String name, Long typeId, String signLogo, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        if (signLogo == null) {
            request.getSession().removeAttribute("tCodeId");
            request.getSession().removeAttribute("tCodeName");
            request.getSession().removeAttribute("tCodeTypeId");
        }
        if (id != null || name != null || typeId != null) {
            request.getSession().setAttribute("tCodeId", id);
            request.getSession().setAttribute("tCodeName", name);
            request.getSession().setAttribute("tCodeTypeId", typeId);
        }
        if (signLogo != null) {
            id = (String) request.getSession().getAttribute("tCodeId");
            name = (String) request.getSession().getAttribute("tCodeName");
            typeId = (Long) request.getSession().getAttribute("tCodeTypeId");
        }
        TCode tCode = new TCode();
        tCode.setId(id);
        tCode.setName(name);
        tCode.setTypeId(typeId);
        List<TCodeType> tCodeTypelist = tCodeTypeService.findList(new TCodeType());
        model.addAttribute("tCodeTypelist", tCodeTypelist);
        model.addAttribute("id", tCode.getId());
        model.addAttribute("name", tCode.getName());
        model.addAttribute("typeId", tCode.getTypeId());
        Page<TCode> page = tCodeService.findPage(new Page<TCode>(request, response), tCode);
        model.addAttribute("page", page);
        return "modules/settings/tCodeList";
    }

    /**
     * 查看，增加，编辑code管理表单页面
     */
    @RequiresPermissions(value = { "settings:tCode:view", "settings:tCode:edit" }, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(String id, String typeId, Model model) {
        TCode tCode = null;
        if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(typeId)) {
            tCode = tCodeService.get(id, typeId);
        }
        if (tCode == null) {
            tCode = new TCode();
        }
        model.addAttribute("tCode", tCode);
        List<TCodeType> tCodeTypelist = tCodeTypeService.findList(new TCodeType());
        model.addAttribute("tCodeTypelist", tCodeTypelist);
        return "modules/settings/tCodeForm";
    }

    @RequiresPermissions(value = { "settings:tCode:add" }, logical = Logical.OR)
    @RequestMapping(value = "formAdd")
    public String formAdd(Model model) {

        TCode tCode = new TCode();
        model.addAttribute("tCode", tCode);
        List<TCodeType> tCodeTypelist = tCodeTypeService.findList(new TCodeType());
        model.addAttribute("tCodeTypelist", tCodeTypelist);
        return "modules/settings/tCodeFormAdd";
    }

    /**
     * 保存code管理
     */
    @RequiresPermissions(value = { "settings:tCode:edit" }, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(TCode tCode, Model model, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, tCode)) {
            return form(tCode.getId(), tCode.getTypeId() + "", model);
        }
        if (!tCode.getIsNewRecord()) {// 编辑表单保存
            TCode t = tCodeService.get(tCode.getId(), tCode.getTypeId() + "");// 从数据库取出记录的值
            MyBeanUtils.copyBeanNotNull2Bean(tCode, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
            tCodeService.save(t);// 保存
        }
        // else{//新增表单保存
        // tCodeService.save(tCode);//保存
        // }
        addMessage(redirectAttributes, "保存字典管理成功");
        return "redirect:" + Global.getAdminPath() + "/settings/tCode/?repage&signLogo=0";
    }

    @RequiresPermissions(value = { "settings:tCode:add" }, logical = Logical.OR)
    @RequestMapping(value = "saveAdd")
    public String saveAdd(TCode tCode, String id, Model model, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, tCode)) {
            return form(tCode.getId(), tCode.getTypeId() + "", model);
        }
        TCode t = tCodeService.get(tCode.getId(), tCode.getTypeId() + "");
        if (t == null) {
            tCodeService.saveAdd(tCode);
            addMessage(redirectAttributes, "保存字典管理成功");
        } else {
            addMessage(redirectAttributes, "已存在ID,保存字典管理失败");
        }
        return "redirect:" + Global.getAdminPath() + "/settings/tCode/?repage&signLogo=0";
    }

    /**
     * 删除code管理
     */
    @RequiresPermissions("settings:tCode:del")
    @RequestMapping(value = "delete")
    public String delete(TCode tCode, RedirectAttributes redirectAttributes) {
        tCodeService.delete(tCode);
        addMessage(redirectAttributes, "删除字典管理成功");
        return "redirect:" + Global.getAdminPath() + "/settings/tCode/?repage&signLogo=0";
    }

    public String newFile(MultipartFile iconFile, Long typeId, HttpServletRequest request)
            throws IllegalStateException, IOException {

        String newFileName = null;
        String originalFilename = iconFile.getOriginalFilename();

        String result = originalFilename.substring(originalFilename.length() - 4, originalFilename.length());

        if (result.equals(".png") || result.equals(".jpg") || result.equals(".gif")) {

        } else {

            throw new NoAccountException();
        }

        // 上传图片
        if (iconFile != null && originalFilename != null && originalFilename.length() > 0) {
            // 存储图片的物理路径
            String pic_path = request.getSession().getServletContext().getRealPath("");

            String path = null;
            if (typeId == 1) {
                path = "/static_modules/device/";
            } else {
                path = "/static_modules/channel/";
            }
            File file = new File(pic_path + path);
            // 如果文件夹不存在则创建
            if (!file.exists() && !file.isDirectory()) {
                file.mkdir();
            } else {
            }

            newFileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
            // 新图片
            File newFile = new File(pic_path + path + newFileName);
            // 将内存中的数据写入磁盘
            iconFile.transferTo(newFile);
        }
        return newFileName;
    }

    // 上传图片
    @RequestMapping(value = { "upLoadPic" })
    @ResponseBody
    public JSONObject upLoadPic(String id, Long typeId, HttpServletRequest request, MultipartFile iconFile,
            MultipartFile warnFile, MultipartFile offlineFile, MultipartFile defenceFile, MultipartFile withdrawingFile,
            MultipartFile sidewayFile, HttpServletResponse response) throws Exception {

        JSONObject json = new JSONObject();
        TCode tCode = new TCode();
        tCode.setId(id);
        tCode.setTypeId(typeId);

        String checkPic = tCodeService.checkPic(iconFile, warnFile, offlineFile, defenceFile, withdrawingFile, sidewayFile);

        if (iconFile != null) {
            String newFileName = newFile(iconFile, typeId, request);
            tCode.setIconSkin(newFileName);
            if (newFileName.equals("false")) {
                json.put("格式错误", "");
                return json;
            }
        }
        if (warnFile != null) {
            String newFileName = newFile(warnFile, typeId, request);
            tCode.setWarnIconSkin(newFileName);
            if (newFileName.equals("false")) {
                json.put("格式错误", "");
                return json;
            }
        }
        if (offlineFile != null) {
            String newFileName = newFile(offlineFile, typeId, request);
            tCode.setOfflineIconSkin(newFileName);
            if (newFileName.equals("false")) {
                json.put("格式错误", "");
                return json;
            }
        }
        if (defenceFile != null) {
            String newFileName = newFile(defenceFile, typeId, request);
            tCode.setDefenceIconSkin(newFileName);
            if (newFileName.equals("false")) {
                json.put("格式错误", "");
                return json;
            }
        }
        if (withdrawingFile != null) {
            String newFileName = newFile(withdrawingFile, typeId, request);
            tCode.setWithdrawingIconSkin(newFileName);
            if (newFileName.equals("false")) {
                json.put("格式错误", "");
                return json;
            }

        }
        if (sidewayFile != null) {
            String newFileName = newFile(sidewayFile, typeId, request);
            tCode.setSidewayIconSkin(newFileName);
            if (newFileName.equals("false")) {
                json.put("格式错误", "");
                return json;
            }
        }

        TCode t = tCodeService.get(tCode.getId(), tCode.getTypeId() + "");// 从数据库取出记录的值
        MyBeanUtils.copyBeanNotNull2Bean(tCode, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
        try {
            tCodeService.save(t);// 保存
        } catch (Exception e) {
            json.put("suc", "上传失败!");
        }
        json.put("suc", "成功上传!");
        return json;
    }

    // 下载tcode小图标
    @RequestMapping(value = { "getPic" }, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getpic(String id, String typeId, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        JSONObject json = new JSONObject();

        TCode tCode = tCodeService.get(id, typeId);
        String imageIcon = tCode.getIconSkin();

        String imageWarn = tCode.getWarnIconSkin();
        String imageOffline = tCode.getOfflineIconSkin();

        String defenceIcon = tCode.getDefenceIconSkin();
        String withdrawingIcon = tCode.getWithdrawingIconSkin();
        String sidewayIcon = tCode.getSidewayIconSkin();

        String path = null;
        if (tCode.getTypeId() == 1) {
            path = "/static_modules/device/";
        } else {
            path = "/static_modules/channel/";
        }
        String fileRoot = request.getSession().getServletContext().getRealPath("");

        String imageIconPath = fileRoot + path + imageIcon;
        String imageWarnPath = fileRoot + path + imageWarn;
        String imageOfflinePath = fileRoot + path + imageOffline;
        String imageDefenceIcon = fileRoot + path + defenceIcon;
        String imageWithdrawingIcon = fileRoot + path + withdrawingIcon;
        String imageSidewayIcon = fileRoot + path + sidewayIcon;
        File file1 = new File(imageIconPath);
        File file2 = new File(imageWarnPath);
        File file3 = new File(imageOfflinePath);
        File file4 = new File(imageDefenceIcon);
        File file5 = new File(imageWithdrawingIcon);
        File file6 = new File(imageSidewayIcon);

        if (StringUtils.isNotBlank(tCode.getIconSkin()) && StringUtils.isNotBlank(file1.getAbsolutePath())) {
            json.put("icon", path + imageIcon);
        }
        if (StringUtils.isNotBlank(tCode.getWarnIconSkin()) && StringUtils.isNotBlank(file2.getAbsolutePath())) {
            json.put("warn", path + imageWarn);
        }
        if (StringUtils.isNotBlank(tCode.getOfflineIconSkin()) && StringUtils.isNotBlank(file3.getAbsolutePath())) {
            json.put("offline", path + imageOffline);
        }
        if (StringUtils.isNotBlank(tCode.getDefenceIconSkin()) && StringUtils.isNotBlank(file4.getAbsolutePath())) {
            json.put("defence", path + defenceIcon);
        }
        if (StringUtils.isNotBlank(tCode.getWithdrawingIconSkin()) && StringUtils.isNotBlank(file5.getAbsolutePath())) {
            json.put("withdrawing", path + withdrawingIcon);
        }
        if (StringUtils.isNotBlank(tCode.getSidewayIconSkin()) && StringUtils.isNotBlank(file6.getAbsolutePath())) {
            json.put("sideway", path + sidewayIcon);
        }

        return json;
    }

    /**
     * 批量删除code管理
     */
    @RequiresPermissions("settings:tCode:del")
    @RequestMapping(value = "deleteAll")
    public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
        String idArray[] = ids.split(",");
        for (String is : idArray) {
            String id[] = is.split(" ");
            tCodeService.delete(tCodeService.get(id[0], id[1]));
        }
        addMessage(redirectAttributes, "删除字典管理成功");
        return "redirect:" + Global.getAdminPath() + "/settings/tCode/?repage&signLogo=0";
    }

    /**
     * 导出excel文件
     */
    @RequiresPermissions("settings:tCode:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public String exportFile(TCode tCode, HttpServletRequest request, HttpServletResponse response,
            RedirectAttributes redirectAttributes) {
        try {
            String fileName = "code管理" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<TCode> page = tCodeService.findPage(new Page<TCode>(request, response, -1), tCode);
            new ExportExcel("code管理", TCode.class).setDataList(page.getList()).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导出code管理记录失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/settings/tCode/?repage";
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("settings:tCode:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<TCode> list = ei.getDataList(TCode.class);
            for (TCode tCode : list) {
                try {
                    tCodeService.save(tCode);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条code管理记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条code管理记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入code管理失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/settings/tCode/?repage";
    }

    /**
     * 下载导入code管理数据模板
     */
    @RequiresPermissions("settings:tCode:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "code管理数据导入模板.xlsx";
            List<TCode> list = Lists.newArrayList();
            new ExportExcel("code管理数据", TCode.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/settings/tCode/?repage";
    }

    // 小图片回调
    @RequestMapping(value = "codeRedirect")
    public String codeRedirect() throws Exception {
        return "redirect:" + Global.getAdminPath() + "/settings/tCode/?repage&signLogo=0";
    }

}