package cn.cdsoft.common.utils;

import cn.cdsoft.modules.warm.web.PicUploadController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传工具类
 *
 * @author yangdc
 * @date Apr 18, 2012
 *
 * <pre>
 * </pre>
 */
public class ImageUploadUtil {
    /**
     * 表单字段常量
     */
    public static final String FORM_FIELDS = "form_fields";
    /**
     * 文件域常量
     */
    public static final String FILE_FIELDS = "file_fields";

    // 最大文件大小
    private long maxSize = 5000000;
    // 定义允许上传的文件扩展名
    private Map<String, String> extMap = new HashMap<String, String>();
    // 文件保存目录相对路径
    private String basePath = "upload";
    // 文件的目录名
    private String dirName = "images";
    // 上传临时路径
    private static final String TEMP_PATH = "/temp";
    private String tempPath = basePath + TEMP_PATH;
    // 若不指定则文件名默认为 yyyyMMddHHmmss_xyz
    private String fileName;

    // 文件保存目录路径
    private String savePath;
    // 文件保存目录url
    private String saveUrl;
    // 文件最终的url包括文件名
    private String fileUrl;

    private static final Logger LOGGER= LoggerFactory.getLogger(PicUploadController.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    // 允许上传的格式
    private static final String[] IMAGE_TYPE=new String[]{".image",".png",".bmp",".jepg",".gif"};

    public ImageUploadUtil() {
        // 其中images,flashs,medias,files,对应文件夹名称,对应dirName
        // key文件夹名称
        // value该文件夹内可以上传文件的后缀名
        extMap.put("images", "gif,jpg,jpeg,png,bmp");
        extMap.put("flashs", "swf,flv");
        extMap.put("medias", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
        extMap.put("files", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
    }

    /**
     * 文件上传
     *
     * @param request
     * @return infos info[0] 验证文件域返回错误信息 info[1] 上传文件错误信息 info[2] savePath info[3] saveUrl info[4] fileUrl
     */
    @SuppressWarnings("unchecked")
    public String uploadFile(MultipartFile uploadFile, HttpServletRequest request) {
        String flag = this.validateFields(request);
//        if(flag != "true"){
//            return null;
//        }
        // 校验图片格式
        boolean isLegal = false;
        /**
         * 用for循环判断上传的文件，是不是以type作为结尾，并且忽略大小写。type类型来自于IMAGE_TYPE
         * 然后做一个标记true表示合法
         */
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(), type)) {
                isLegal = true;
                break;
            }
        }
        // 获取文件新路径，也就是保存的路径
        String filePath = getFilePath(uploadFile.getOriginalFilename(),request);
//       判断是否启用了debug,如果启用就Pic file upload图片文件上传哪里到哪里，就会在日志中显示清楚
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Pic file upload .[{}] to [{}] .", uploadFile.getOriginalFilename(), filePath);
        }
        // 生成图片的绝对引用地
        String picUrl = StringUtils.replace(StringUtils.substringAfter(filePath, request.getSession().getServletContext().getRealPath("upload")), "\\", "/");
        String url = request.getSession().getServletContext().getRealPath("upload") + picUrl;
//        fileUploadResult.setUrl(request.getSession().getServletContext().getRealPath("upload") + picUrl);
        System.out.println("picUrl:"+picUrl);
        System.out.println("Url:"+url);
//      找打一个新生成的文件，
        File newFile = new File(filePath);
        // 把上传的文件写入到目标文件中去；该语句执行完成后，就把上传的文件写入到目标地址中了
        try {
            uploadFile.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 校验图片是否合法
//        isLegal = false;
//        try {
////            通过BufferedImage读取图片，该内容数据Java界面编程
//            BufferedImage image = ImageIO.read(newFile);
//            if (image != null) {
////                    获取图片的宽和高
//                fileUploadResult.setWidth(image.getWidth() + "");
//                fileUploadResult.setHeigth(image.getHeight() + "");
////                        标记为true表示合法
//                isLegal = true;
//            }
//        } catch (IOException e) {
//        }
//
//        // 再次设置上传的状态
//        fileUploadResult.setError(isLegal ? 0 : 1);
//
//        if (!isLegal) {
//            // 不合法，将磁盘上的文件删除
//            newFile.delete();
//        }
//        response.setContentType(MediaType.TEXT_HTML_VALUE);
//        将Java对象序列化成json数据
        return url;
    }

    /**
     * 上传验证,并初始化文件目录
     *
     * @param request
     */
    public String validateFields(HttpServletRequest request) {
        String errorInfo = "true";
        // boolean errorFlag = true;
        // 获取内容类型
        String contentType = request.getContentType();
        int contentLength = request.getContentLength();
        // 文件保存目录路径
        savePath = request.getSession().getServletContext().getRealPath("/") + basePath + "/";
        // 文件保存目录URL
        saveUrl = request.getContextPath() + "/" + basePath + "/";
        File uploadDir = new File(savePath);
        if (contentType == null || !contentType.startsWith("multipart")) {
            // TODO
            System.out.println("请求不包含multipart/form-data流");
            errorInfo = "请求不包含multipart/form-data流";
        } else if (maxSize < contentLength) {
            // TODO
            System.out.println("上传文件大小超出文件最大大小");
            errorInfo = "上传文件大小超出文件最大大小[" + maxSize + "]";
        } else if (!ServletFileUpload.isMultipartContent(request)) {
            // TODO
            errorInfo = "请选择文件";
        } else if (!uploadDir.canWrite()) {
            // TODO
            errorInfo = "上传目录[" + savePath + "]没有写权限";
        } else if (!extMap.containsKey(dirName)) {
            // TODO
            errorInfo = "目录名不正确";
        } else {

        }

        return errorInfo;
    }


    //    6.最终返回的路径如：E:\\0725\\taotao-upload\\images\\2017(yyyy)\\08(MM)\\08(dd)\\20170808162211(yyyyMMddhhmmssSSSS).jpg(IMAGE_TYPE)
//    上面的地址就是图片上传到服务器保存的绝对路径
    private String getFilePath(String sourceFileName,HttpServletRequest request) {
//        1.定义一个目录，并在该目录下创建一个imagers文件夹
        String baseFolder = request.getSession().getServletContext().getRealPath("upload") + File.separator + "images";
//        2.创建时间对象
        Date nowDate = new Date();
        // yyyy/MM/dd
//        DateTime使用的是时间操作组件，功能很强大，就是用来操作时间的。比JDK提供的时间类要更好用
//       3. 获取目录
        String fileFolder = baseFolder + File.separator + new DateTime(nowDate).toString("yyyy") + File.separator + new DateTime(nowDate).toString("MM") + File.separator
                + new DateTime(nowDate).toString("dd");
//      4.  判断目录是否存在
        File file = new File(fileFolder);
        if (!file.isDirectory()) {
            // 如果目录不存在，则创建目录
            file.mkdirs();
        }
        //5.最后 生成新的文件名
        String fileName = new DateTime(nowDate).toString("yyyyMMddhhmmssSSSS") + RandomUtils.nextInt(100, 9999) + "." + StringUtils.substringAfterLast(sourceFileName, ".");
        return fileFolder + File.separator + fileName;
    }

    /** **********************get/set方法********************************* */

    public String getSavePath() {
        return savePath;
    }

    public String getSaveUrl() {
        return saveUrl;
    }

    public long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    public Map<String, String> getExtMap() {
        return extMap;
    }

    public void setExtMap(Map<String, String> extMap) {
        this.extMap = extMap;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
        tempPath = basePath + TEMP_PATH;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getTempPath() {
        return tempPath;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
