package cn.cdsoft.modules.warm.web;

/**
 * Created by ZZUSER on 2018/8/27.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import cn.cdsoft.common.json.AjaxJson;
import cn.cdsoft.common.utils.ImageUploadUtil;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;

@RequestMapping("/pdf")
@Controller
public class PicUploadController {
    private static final Logger LOGGER= LoggerFactory.getLogger(PicUploadController.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    // 允许上传的格式
    private static final String[] IMAGE_TYPE=new String[]{".image",".png",".bmp",".jepg",".gif","jpg"};
    // 校验图片格式
    /**
     * produces：指定响应的类型
     * @param
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="upload1")
    @ResponseBody
    public AjaxJson upload(@RequestParam("file") MultipartFile uploadFile , HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
		/*星号表示所有的域都可以接受，*/
        response.setHeader("Access-Control-Allow-Methods", "GET,POST");
        AjaxJson j= new AjaxJson();
        ImageUploadUtil uploadUtils = new ImageUploadUtil();
        String flag = uploadUtils.validateFields(request);
        if(flag != "true"){
            j.setMsg("图片上传失败");
            j.setErrorCode("-1");
            j.setSuccess(false);
            return null;
        }
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
        if(isLegal !=true){
            j.setMsg("图片上传失败");
            j.setErrorCode("-1");
            j.setSuccess(false);
            return null;
        }
        // 封装Result对象
//        PicUploadResult fileUploadResult = new PicUploadResult();
        // 状态
//        fileUploadResult.setError(isLegal ? 0 : 1);//如果为0表示上传成功，如果为1表示失败
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
        uploadFile.transferTo(newFile);
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
        j.setMsg("图片上传成功");
        j.setSuccess(true);
        j.put("url",url);
        j.put("picUrl",picUrl);
        return j;
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
        System.out.println("fileName:"+fileName);
        System.out.println("fileFolder:"+fileFolder);
        System.out.println("File:"+File.separator);
        return fileFolder + File.separator + fileName;
    }

    @RequestMapping(value="delete",method= RequestMethod.POST,produces= MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public void delete(HttpServletRequest request, HttpServletResponse response, String url){
        String path =request.getSession().getServletContext().getRealPath("upload")+url;
        System.out.println(request.getSession().getServletContext().getRealPath("upload"));
//        String path = "D:\\project\\DEMO\\secondeSSM\\target\\secondeSSM\\upload/images/2018/08/29/2018082905433865903967.png";
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
    }

    @RequestMapping(value = "uploadFile")
    public String uploadFile(){
        return "modules/powercharge/wisdompark";
    }


    // 校验图片格式
    /**
     * produces：指定响应的类型
     * @param
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="upload")
    @ResponseBody
    public AjaxJson upload1(@RequestParam("file") MultipartFile[] uploadFiles , HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
		/*星号表示所有的域都可以接受，*/
        response.setHeader("Access-Control-Allow-Methods", "GET,POST");
        AjaxJson j= new AjaxJson();
        ImageUploadUtil uploadUtils = new ImageUploadUtil();
        String flag = uploadUtils.validateFields(request);
        if(flag != "true"){
//            j.setMsg("图片上传失败");
//            j.setErrorCode("-1");
//            j.setSuccess(false);
//            return null;
        }
        String urls = "";
        for(int i=0;i<uploadFiles.length;i++){
            MultipartFile uploadFile = uploadFiles[i];
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
            if(isLegal !=true){
                j.setMsg("图片上传失败");
                j.setErrorCode("-1");
                j.setSuccess(false);
                return null;
            }
            // 封装Result对象
            // 获取文件新路径，也就是保存的路径
            String filePath = getFilePath(uploadFile.getOriginalFilename(),request);
//       判断是否启用了debug,如果启用就Pic file upload图片文件上传哪里到哪里，就会在日志中显示清楚
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pic file upload .[{}] to [{}] .", uploadFile.getOriginalFilename(), filePath);
            }
            // 生成图片的绝对引用地
            String picUrl = StringUtils.replace(StringUtils.substringAfter(filePath, request.getSession().getServletContext().getRealPath("upload")), "\\", "/");
            String url = request.getSession().getServletContext().getRealPath("upload") + picUrl;
            System.out.println("picUrl:"+picUrl);
            System.out.println("Url:"+url);
//      找打一个新生成的文件，
            File newFile = new File(filePath);
            // 把上传的文件写入到目标文件中去；该语句执行完成后，就把上传的文件写入到目标地址中了
            uploadFile.transferTo(newFile);
            if(urls.length()==0){
                urls = picUrl;
            }else {
                urls = urls +","+picUrl;
            }
        }
        j.setMsg("图片上传成功");
        j.setSuccess(true);
        j.put("picUrl",urls);
//        j.put("picUrl",picUrl);
        return j;
    }

}
