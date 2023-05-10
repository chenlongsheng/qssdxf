package cn.cdsoft.common.utils;

import org.apache.shiro.SecurityUtils;

/**获取当前登录用户所属orgId
 * Created by ZZUSER on 2019/1/11.
 */
public class OrgUtil {

    public static String getOrgId(){
        return "7579";
    }

    public static String getUserId(){
//        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        return "1";
    }
}
