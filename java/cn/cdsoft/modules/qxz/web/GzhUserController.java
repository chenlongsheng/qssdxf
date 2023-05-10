package cn.cdsoft.modules.qxz.web;

import cn.cdsoft.common.json.AjaxJson;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.common.web.BaseController;
import cn.cdsoft.modules.qxz.entity.GzhUser;
import cn.cdsoft.modules.qxz.excel.Excel;
import cn.cdsoft.modules.qxz.excel.ExcelUtil;
import cn.cdsoft.modules.qxz.service.GzhUserService;
import cn.cdsoft.modules.settings.entity.TOrg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 63446 on 2018/11/6.
 */
@Controller
@RequestMapping("/gzh/user")
public class GzhUserController extends BaseController {
    @Autowired
    GzhUserService gzhUserService;

    @RequestMapping("/add")
    @ResponseBody
    public AjaxJson AddUser(GzhUser gzhUser){
        AjaxJson ajaxJson =new AjaxJson();
        try{
            gzhUserService.addUser(gzhUser);
            ajaxJson.setSuccess(true);
            ajaxJson.setErrorCode("0");
        }catch (Exception e){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return ajaxJson;
    }

    @RequestMapping("/update")
    @ResponseBody
    public AjaxJson updateUser(GzhUser gzhUser){
        AjaxJson ajaxJson =new AjaxJson();
        try{
            gzhUserService.updateUser(gzhUser);
            ajaxJson.setSuccess(true);
            ajaxJson.setErrorCode("0");
        }catch (Exception e){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return ajaxJson;
    }

    @RequestMapping("/deleteUser")
    @ResponseBody
    public AjaxJson deleteUser(String ids){
        AjaxJson ajaxJson =new AjaxJson();
        try{
            gzhUserService.deleteUser(ids);
            ajaxJson.setSuccess(true);
            ajaxJson.setErrorCode("0");
        }catch (Exception e){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return ajaxJson;
    }

    //绑定
    @RequestMapping("/binding")
    @ResponseBody
    public AjaxJson binding(GzhUser gzhUser){
        AjaxJson ajaxJson =new AjaxJson();
        try{
            gzhUserService.addUser(gzhUser);
            ajaxJson.setSuccess(true);
            ajaxJson.setErrorCode("0");
        }catch (Exception e){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg(e.getMessage());
        }
        return ajaxJson;
    }

    //绑定
    @RequestMapping("/findGzhUserList")
    @ResponseBody
    public Map findGzhUserList(GzhUser gzhUser, HttpServletRequest request,
                               HttpServletResponse response){
        AjaxJson j = new AjaxJson();
        Map map = new HashMap();
        Page page = new Page<GzhUser>(request,response);
        gzhUser.setPage(page);
        List<Map> list = gzhUserService.findListByPage(gzhUser);
        GzhUser gzhUser1 = new GzhUser();
        Page page1 = new Page();
        gzhUser.setPage(page);
        int length = gzhUserService.count(gzhUser);
        map.put("total",length);
        map.put("rows",list);
        return map;
    }

    @RequestMapping("/findOrgTree")
    @ResponseBody
    public List<Map> findOrgTree(){
        return gzhUserService.findOrgTree();
    }

    @RequestMapping("/findOrgTree1")
    @ResponseBody
    public List<Map> findOrgTree1(){
        return gzhUserService.findOrgTree1();
    }

    @RequestMapping("/roleTree")
    @ResponseBody
    public List<Map> roleTree(String orgIds){
        return gzhUserService.roleTree(orgIds);
    }

    @RequestMapping("/orgRoleList")
    @ResponseBody
    public List<Map> orgRoleList(String id){
        return gzhUserService.orgRoleList(id);
    }

    @RequestMapping("/gzhUserListPage")
    public String gzhUserListPage(){
        return "modules/wx/gzhUser";
    }

    @RequestMapping("/export")
    @ResponseBody
    public void export(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse){
//        GzhUser gzhUser = new GzhUser();
        List<GzhUser> list = gzhUserService.getAllGzhUser();
        List<Excel> resultList = new ArrayList();
        for(int i=0;i<list.size();i++){
            String orgIds = list.get(i).getOrgIds();
            String[] arr = orgIds.split(",");
            List<TOrg> orgList = gzhUserService.findOrgByArr(arr);
            for(int j=0;j<orgList.size();j++){
                Excel excel = new Excel();
                if(list.get(i).getState()==0){
                    excel.setState("启用");
                }else {
                    excel.setState("禁用");
                }
                excel.setDesct(list.get(i).getDesct());
                excel.setEmail(list.get(i).getEmail());
                excel.setKeyword(list.get(i).getKeyword());
                excel.setOpenId(list.get(i).getOpenId());
                excel.setPhone(list.get(i).getPhone());
                excel.setDesct(list.get(i).getDesct());
                excel.setOrg(orgList.get(j).getName());
                resultList.add(excel);
            }
        }
        ExcelUtil.export(httpServletRequest,httpServletResponse,resultList);
        System.out.println(11);
    }


}
