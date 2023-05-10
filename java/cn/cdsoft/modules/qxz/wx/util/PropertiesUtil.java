package cn.cdsoft.modules.qxz.wx.util;

import cn.cdsoft.common.utils.PropertiesLoader;

/**
 * Created by ZZUSER on 2018/11/29.
 */
public final  class PropertiesUtil {
    //日志,不必要
//    private static Logger _loger = (Logger) LoggerFactory.getLogger(PropertiesUtil.class);
    //指定配置文件地址//resource目录下:
    private static PropertiesLoader propertiesLoader = new PropertiesLoader(
            "classpath:/wx.properties");


    public static String getProperty(String key) {
        String property;
        try {
            property = propertiesLoader.getProperty(key);
        } catch (Exception e) {
            System.out.println("Property key not exit: " + key);
            return null;
        }
        return property;
    }

    public static String getProperty(String key, String value) {
        if (propertiesLoader.getProperty(key) == null) {
            return value;
        }
        return propertiesLoader.getProperty(key);
    }

}
