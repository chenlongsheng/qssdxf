package cn.cdsoft.modules.sys.web;

import cn.cdsoft.modules.sys.param.CommonConstant;
import cn.cdsoft.modules.sys.utils.VerifyCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@RequestMapping("captcha")
@Controller
public class CaptchaController {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取验证码图片 默认宽100x高40
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public void captcha(HttpServletRequest request, HttpServletResponse response, Integer w, Integer h,@RequestParam  String captchaId) {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        try {
            //生成随机字串
            String captcha = VerifyCodeUtils.generateVerifyCode(4);
            String key = CommonConstant.CAPTCHA_KEY + captchaId;
            redisTemplate.opsForValue().set(key, captcha);
            redisTemplate.expire(key, 3, TimeUnit.MINUTES);
            //生成图片
            int width = 100;
            int height = 40;
            if (w != null && h != null) {
                width = w;
                height = h;
            }
            VerifyCodeUtils.outputImage(width, height, response.getOutputStream(), captcha);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
