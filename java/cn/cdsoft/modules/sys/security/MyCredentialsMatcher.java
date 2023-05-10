package cn.cdsoft.modules.sys.security;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.springframework.util.DigestUtils;

import cn.cdsoft.modules.sys.security.exception.NoAccountException;
import cn.cdsoft.modules.sys.service.SystemService;
import cn.cdsoft.modules.sys.utils.MyMD5Util;

/**
 * Created by LJR on 2018/12/29.
 */
public class MyCredentialsMatcher extends SimpleCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        System.out.println(token.getPassword() + "====密码---");
        String s = String.valueOf(token.getPassword());

        System.out.println("页面输入的密码===" + s);
        String plainPassword = String.valueOf(getCredentials(info));
        System.out.println("数据库的密码====" + plainPassword);

        boolean validPassword = false;
        try {

            if (plainPassword.equals(new String(token.getPassword()))) {
                validPassword = true;
            }

//            String md5DigestAsHex = DigestUtils.md5DigestAsHex(new String(token.getPassword()).getBytes());            
//            System.out.println("md5DigestAsHex=== "+md5DigestAsHex);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return validPassword;
    }

}
