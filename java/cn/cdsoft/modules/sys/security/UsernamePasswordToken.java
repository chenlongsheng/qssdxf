/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.sys.security;

/**
 * 用户和密码（包含验证码）令牌类
 * 
 * @author jeeplus
 * @version 2013-5-19
 */
public class UsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken {

    private static final long serialVersionUID = 1L;

    private String captcha;
    private String captchaId;

    private boolean mobileLogin;
    private String token;

    public UsernamePasswordToken() {
        super();
    }

    public UsernamePasswordToken(String username, char[] password, boolean rememberMe, String host, String captcha,
            String captchaId, boolean mobileLogin) {

        super(username, password, rememberMe, host);
        this.captcha = captcha;
        this.captchaId = captchaId;
        this.mobileLogin = mobileLogin;
    }

    public UsernamePasswordToken(String username, char[] password, boolean rememberMe, String host, String token) {
        super(username, password, rememberMe, host);
        this.token = token;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getCaptchaId() {
        return captchaId;
    }

    public void setCaptchaId(String captchaId) {
        this.captchaId = captchaId;
    }

    public boolean isMobileLogin() {
        return mobileLogin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}