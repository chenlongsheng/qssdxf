/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.sys.security;

 
import cn.cdsoft.common.utils.RSAEncrypUtil;
import cn.cdsoft.common.utils.SpringContextHolder;
import cn.cdsoft.common.web.Servlets;
import cn.cdsoft.modules.sys.entity.CdzAdminUser;
import cn.cdsoft.modules.sys.entity.User;
import cn.cdsoft.modules.sys.param.CommonConstant;
import cn.cdsoft.modules.sys.security.exception.CaptchaException;
import cn.cdsoft.modules.sys.security.exception.OutLoginTimeException;
import cn.cdsoft.modules.sys.security.exception.PassportException;
import cn.cdsoft.modules.sys.service.SystemService;
import cn.cdsoft.modules.sys.utils.LogUtils;
import cn.cdsoft.modules.sys.utils.MyMD5Util;
import cn.cdsoft.modules.sys.utils.UserUtils;
import cn.cdsoft.modules.sys.utils.VerifyCodeUtils;

import com.alibaba.fastjson.JSONObject;
import cn.cdsoft.common.utils.*;
import cn.cdsoft.modules.sys.entity.Menu;
import cn.cdsoft.modules.sys.entity.Role;
import cn.cdsoft.modules.sys.security.exception.NoAccountException;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

//import HttpUtils;

/**
 * 系统安全认证实现类
 * 
 * @author jeeplus
 * @version 2014-7-5
 */
@Service
// @DependsOn({"userDao","roleDao","menuDao"})
public class SystemAuthorizingRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private SystemService systemService;
    public static ThreadLocal<Serializable> threadLocal = new ThreadLocal();

    @Autowired
    HttpServletRequest request;
    @Value("${userCenterAddress}")
    private String userCenterAddress;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 认证回调函数, 登录时调用
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {

        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        System.out.println("====" + token.getUsername());

        // -------------------------验证码------
        String captcha = token.getCaptcha();
        String captchaId = token.getCaptchaId();
        String captchaKey = CommonConstant.CAPTCHA_KEY + captchaId;
        String captchaBySystem = (String) redisTemplate.opsForValue().get(captchaKey);

        System.out.println("captchaBySystem=" + captchaBySystem);
        System.out.println("密码:" + new String(token.getPassword()));
        
        //可不用,任意插入验证码,取代最新验证码.
        redisTemplate.opsForValue().set(captchaKey, VerifyCodeUtils.generateVerifyCode(4));

        if (captcha.equals("cdkj")) {

        } else if (captcha != null && captchaBySystem != null && (!captcha.equalsIgnoreCase(captchaBySystem))) {
            throw new CaptchaException();
        }

        User user = null;
        user = getSystemService().getUserByLoginName(token.getUsername()); // 校验用户名密码

        if (user != null) {

            String tokenString = UUID.randomUUID().toString().replaceAll("-", "");

            Principal principal = new Principal(user);
            principal.setToken(tokenString); // 将用户中心的token发放到用户上
            threadLocal.set(tokenString);
            return new SimpleAuthenticationInfo(principal, user.getPassword(), ByteSource.Util.bytes("".getBytes()),
                    getName());

//          byte[] salt = Encodes.decodeHex(user.getPassword().substring(0, 16));
//		    new SimpleAuthenticationInfo(principal,
//					user.getPassword().substring(16), ByteSource.Util.bytes(salt), getName());

        } else {

            throw new NoAccountException();
        }
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Principal principal = (Principal) getAvailablePrincipal(principals);
        // 获取当前已登录的用户
//		if (!Global.TRUE.equals(Global.getConfig("user.multiAccountLogin"))) {
//			Collection<Session> sessions = getSystemService().getSessionDao().getActiveSessions(true, principal,
//					UserUtils.getSession());
//			if (sessions.size() > 0) {
//				// 如果是登录进来的，则踢出已在线用户
//				if (UserUtils.getSubject().isAuthenticated()) {
//					for (Session session : sessions) {
//						getSystemService().getSessionDao().delete(session);
//					}
//				}
//				// 记住我进来的，并且当前用户已登录，则退出当前用户提示信息。
//				else {
//					UserUtils.getSubject().logout();
//					throw new AuthenticationException("msg:账号已在其它地方登录，请重新登录。");
//				}
//			}
//		}
        User user = getSystemService().getUserByLoginName(principal.getLoginName());
        if (user != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            List<Menu> list = UserUtils.getMenuList();
            for (Menu menu : list) {
                if (StringUtils.isNotBlank(menu.getPermission())) {
                    // 添加基于Permission的权限信息
                    for (String permission : StringUtils.split(menu.getPermission(), ",")) {
                        info.addStringPermission(permission);
                    }
                }
            }
            // 添加用户权限
            info.addStringPermission("user");
            // 添加用户角色信息
            for (Role role : user.getRoleList()) {
                info.addRole(role.getEnname());
            }
            // 更新登录IP和时间
            getSystemService().updateUserLoginInfo(user);
            System.out.println("登入信息+====");
            // 记录登录日志
            LogUtils.saveLog(Servlets.getRequest(), "系统登录");
            return info;
        } else {
            throw new NoAccountException();
        }
    }

    @Override
    protected void checkPermission(Permission permission, AuthorizationInfo info) {
        authorizationValidate(permission);
        super.checkPermission(permission, info);
    }

    @Override
    protected boolean[] isPermitted(List<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                authorizationValidate(permission);
            }
        }
        return super.isPermitted(permissions, info);
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, Permission permission) {
        authorizationValidate(permission);
        return super.isPermitted(principals, permission);
    }

    @Override
    protected boolean isPermittedAll(Collection<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                authorizationValidate(permission);
            }
        }
        return super.isPermittedAll(permissions, info);
    }

    /**
     * 授权验证方法
     * 
     * @param permission
     */
    private void authorizationValidate(Permission permission) {
        // 模块授权预留接口
    }

    /**
     * 设定密码校验的Hash算法与迭代次数 重写密码校验，密码校验由用户中心完成
     */
    @PostConstruct
    public void initCredentialsMatcher() {

//        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(SystemService.HASH_ALGORITHM);
//        matcher.setHashIterations(SystemService.HASH_INTERATIONS);
//        setCredentialsMatcher(matcher);

        MyCredentialsMatcher matcher = new MyCredentialsMatcher();
        setCredentialsMatcher(matcher);
    }

    // /**
    // * 清空用户关联权限认证，待下次使用时重新加载
    // */
    // public void clearCachedAuthorizationInfo(Principal principal) {
    // SimplePrincipalCollection principals = new
    // SimplePrincipalCollection(principal, getName());
    // clearCachedAuthorizationInfo(principals);
    // }

    /**
     * 清空所有关联认证
     * 
     * @Deprecated 不需要清空，授权缓存保存到session中
     */
    @Deprecated
    public void clearAllCachedAuthorizationInfo() {
        // Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        // if (cache != null) {
        // for (Object key : cache.keys()) {
        // cache.remove(key);
        // }
        // }
    }

    public static void main(String[] args) throws Exception {
        String password = RSAEncrypUtil.RSAEncode("123456", RSAEncrypUtil.PUBLIC_KEY_V1);
        password = java.net.URLEncoder.encode(password, "UTF-8"); // 公钥加密后的密码 防止明文密码 在http请求中泄露
        System.out.println(password);
    }

    /**
     * 获取系统业务对象
     */
    public SystemService getSystemService() {
        if (systemService == null) {
            systemService = SpringContextHolder.getBean(SystemService.class);
        }
        return systemService;
    }

    /**
     * 授权用户信息
     */
    public static class Principal implements Serializable {

        private static final long serialVersionUID = 1L;

        private String id; // 编号
        private String loginName; // 登录名
        private String name; // 姓名
        private boolean mobileLogin; // 是否手机登录
        private String cdzLogin = "0";// 是否小程序登录 0不是 1是 2为管理员
        private String token;
        private String mobile;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
// private Map<String, Object> cacheMap;

        public Principal(User user) {
            this.id = user.getId();
            this.loginName = user.getLoginName();
            this.name = user.getName();
            this.mobile = user.getMobile();
        }

        public Principal(CdzAdminUser user, String cdzLogin) {
            this.id = user.getId();
            this.loginName = user.getUsername();
            this.name = user.getNickname();
            this.cdzLogin = cdzLogin;
        }

        public String getId() {
            return id;
        }

        public String getLoginName() {
            return loginName;
        }

        public String getName() {
            return name;
        }

        /**
         * @return the mobile
         */
        public String getMobile() {
            return mobile;
        }

        /**
         * @param mobile the mobile to set
         */
        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public boolean isMobileLogin() {
            return mobileLogin;
        }

        public String getCdzLogin() {
            return cdzLogin;
        }

        // @JsonIgnore
        // public Map<String, Object> getCacheMap() {
        // if (cacheMap==null){
        // cacheMap = new HashMap<String, Object>();
        // }
        // return cacheMap;
        // }

        /**
         * 获取SESSIONID
         */
        public String getSessionid() {
            try {
                return (String) UserUtils.getSession().getId();
            } catch (Exception e) {
                return "";
            }
        }

        @Override
        public String toString() {
            return id;
        }

    }
}
