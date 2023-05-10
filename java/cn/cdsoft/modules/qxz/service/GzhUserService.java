package cn.cdsoft.modules.qxz.service;

import cn.cdsoft.modules.qxz.dao.GzhUserDao;
import cn.cdsoft.modules.qxz.entity.GzhUser;
import cn.cdsoft.modules.settings.entity.TOrg;
import cn.cdsoft.common.service.CrudService;
import cn.cdsoft.common.utils.OrgUtil;
import cn.cdsoft.modules.sys.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 63446 on 2018/11/6.
 */
@Service
public class GzhUserService extends CrudService<GzhUserDao,GzhUser> {
    @Autowired
    GzhUserDao gzhUserDao;

    @Transactional(readOnly = false)
    public void addUser(GzhUser gzhUser){
        gzhUserDao.addGzhUser(gzhUser);
    }

    @Transactional(readOnly = false)
    public void updateUser(GzhUser gzhUser){
        gzhUserDao.updateGzhUser(gzhUser);
    }

    @Transactional(readOnly = false)
    public void deleteUser(String ids){
        String[] arr = ids.split(",");
        gzhUserDao.deleteUser(arr);
    }

    public List<GzhUser> findGzhUser(GzhUser gzhUser){
        return gzhUserDao.findGzhUser(gzhUser);
    }

    public List<Map> findListByPage(GzhUser gzhUser){
        List<Map> list =  gzhUserDao.findListByPage(gzhUser);
        for(int i=0;i<list.size();i++){
            String orgIds = String.valueOf(list.get(i).get("orgIds"));
            String[] arr= orgIds.split(",");
            List<TOrg> orgList = gzhUserDao.findOrgByArr(arr);
            String name = "";
            for(int j=0;j<orgList.size();j++){
                if(j==0){
                    name = orgList.get(j).getName();
                }else {
                    name = name + ","+orgList.get(j).getName();
                }
            }
            String userRole = "";
            if(list.get(i).get("userRole") !=null && String.valueOf(list.get(i).get("userRole")).length()!=0){
                userRole = String.valueOf(list.get(i).get("userRole"));
            }
            String[] arr1 = userRole.split(",");
            List<Role> roleList = gzhUserDao.findRoleByArr(arr1);
            String name1 = "";
            for(int j=0;j<roleList.size();j++){
                if(j==0){
                    name1 = roleList.get(j).getName();
                }else {
                    name1 = name1 + ","+roleList.get(j).getName();
                }
            }
            list.get(i).put("orgNames",name);
            list.get(i).put("roleNames",name1);
        }
        return list;
    }

    public int count(GzhUser gzhUser){
        return gzhUserDao.count(gzhUser);
    }

    public List<Map> findOrgTree(){
        TOrg tOrg = new TOrg();
        tOrg.setId(OrgUtil.getOrgId());
//        tOrg.setId("233993942926888973");
        return gzhUserDao.findOrgTree(tOrg);
    }

    public List<Map> findOrgTree1(){
        TOrg tOrg = new TOrg();
        tOrg.setId(OrgUtil.getOrgId());
//        tOrg.setId("233993942926888973");
        List<Map> list =  gzhUserDao.findOrgTree(tOrg);
        for(int i=0;i<list.size();i++){
            if(String.valueOf(list.get(i).get("type")).equals("5")){
                list.get(i).put("nocheck",false);
            }else {
                list.get(i).put("nocheck",true);
            }
        }
        return list;
    }


    public List<Map> roleTree(String orgIds){
        TOrg tOrg = new TOrg();
        String[] arr = orgIds.split(",");
        List<Map> resultList = new ArrayList();
        if(orgIds.length()==0){
            return resultList;
        }
        List list = new ArrayList();
        for(int i=0;i<arr.length;i++){
            tOrg.setId(arr[i]);
            List<Map> plotList = gzhUserDao.plotsTree(tOrg);
            resultList.addAll(plotList);
        }
        for(int j=0;j<resultList.size();j++){
            list.add(resultList.get(j).get("id"));
            resultList.get(j).put("nocheck",true);
        }
        if(list.size()>0){
            List<Map> roleList = gzhUserDao.roleTree(list);
            for(int i=0;i<roleList.size();i++){
                roleList.get(i).put("type","role");
            }
            resultList.addAll(roleList);
        }
        return resultList;
    }

    public List<Map> orgRoleList(String id){
        GzhUser gzhUser = new GzhUser();
        gzhUser.setId(id);
        gzhUser = gzhUserDao.findGzhUser(gzhUser).get(0);
        String[] arr = gzhUser.getOrgIds().split(",");
        String userRole = gzhUser.getUserRole();
        if(userRole==null){
            userRole ="";
        }
        String[] roleArr = userRole.split(",");
        List<Map> list =  gzhUserDao.orgRoleList(arr);
        List<Map> roleList = gzhUserDao.roleList(roleArr);
        List resultList = new ArrayList();
        for(int i = 0;i<list.size();i++){
            for(int j=0;j<roleList.size();j++){
                if(String.valueOf(list.get(i).get("orgId")).equals(String.valueOf(roleList.get(j).get("areaId")))){
                    list.get(i).put("roleName",roleList.get(j).get("name"));
                }
            }
        }
        return list;
    }

    public List<TOrg> findOrgByArr(String[] arr){
        return gzhUserDao.findOrgByArr(arr);
    }

    public List<GzhUser> getAllGzhUser(){
        return gzhUserDao.getAllGzhUser();
    }

}
