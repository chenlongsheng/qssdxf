package cn.cdsoft.modules.warm.util;

import cn.cdsoft.common.service.BaseService;
import cn.cdsoft.common.utils.JedisUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import com.jeeplus.modules.powercharge.entity.CdzAdminUser;

/**
 * Created by Administrator on 2018-08-14.
 */
public class PdfInterceptor extends BaseService implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //获取url
        String url = httpServletRequest.getRequestURL().toString();
        String a[] = url.split("/");
        String u = a[a.length - 1];
        httpServletResponse.setCharacterEncoding("UTF-8");
        if(!u.equals("login")){
            String token = httpServletRequest.getParameter("token");
            //未登录
//            if(StringUtils.isBlank(token)){
//                JSONObject obj =new JSONObject();
//                obj.put("success",false);
//                obj.put("error",400);
//                obj.put("msg","请先登录");
//                httpServletResponse.getWriter().print(obj.toJSONString());
//                return false;
//            }
            //查看redis中是否有该token
            String str = JedisUtils.get("token"+token);
//            CdzAdminUser admin = JSONObject.parseObject(JedisUtils.get(token),new TypeReference<CdzAdminUser>() {});
            if(str == null){
                JSONObject obj =new JSONObject();
                obj.put("success",false);
                obj.put("error",200);
                obj.put("logout",true);
                obj.put("msg","登录信息已经失效，请重新登录");
                httpServletResponse.getWriter().print(obj.toJSONString());
                return false;
            }else {
//                JedisUtils.expireTTL(token,1800);
            }
        }
        return true;
    }

//    public void outPutJson(HttpServletResponse httpServletResponse,int status,String value){
//        OutputStream outputStream = null;
//        PrintWriter wirte = null;
//        try{
//            AjaxJson json = new AjaxJson();
//            json.put("status",status);
//            json.put("data",value);
//            wirte = httpServletResponse.getWriter();
////            outputStream = httpServletResponse.getOutputStream();
//            httpServletResponse.setHeader("content-type", "text/html;charset=UTF-8");
////            byte[] data = new String("请先登录").getBytes("UTF-8");
////            String data = JSONObject.toJSONString(json);
////            outputStream.write(data.getBytes("UTF-8"));
////            outputStream.flush();
//            httpServletResponse.sendError(400);
//            wirte.print(json.toString());
//            wirte.flush();
//        }catch (Exception e){
//
//        }finally {
//            try {
//                if (outputStream != null){
//                    outputStream.close();
//                }
//                if(wirte != null){
//                    wirte.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
