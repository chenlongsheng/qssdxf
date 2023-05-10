package cn.cdsoft.modules.qxz.excel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Created by ZZUSER on 2018/12/3.
 */
@Controller
@RequestMapping(value = "/report")
public class ReportFormController{


//    /**
//     * 导出报表
//     * @return
//     */
//    @RequestMapping(value = "/export")
//    @ResponseBody
//    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        //获取数据
////        List<PageData> list = reportService.bookList(page);
//        List list = new ArrayList();
//        Student student = new Student();
//        student.setAge(10);
//        student.setName("张三");
//        student.setId(1);
//        student.setBirth(new Date("2018-12-11"));
//        Student student1 = new Student();
//        student1.setAge(12);
//        student1.setName("李四");
//        student1.setId(2);
//        student1.setBirth(new Date("2018-12-12"));
//        list.add(student);
//        list.add(student1);
//        //excel标题
//        String[] title = {"名称","性别","年龄","学校","班级"};
//
//         //excel文件名
//        String fileName = "学生信息表"+System.currentTimeMillis()+".xls";
//
//  //sheet名
//        String sheetName = "学生信息表";
//            Content content = new Content();
//           for (int i = 0; i < list.size(); i++) {
//            content[i] = new String[title.length];
//            PageData obj = list.get(i);
//            content[i][0] = obj.get("stuName").tostring();
//            content[i][1] = obj.get("stuSex").tostring();
//            content[i][2] = obj.get("stuAge").tostring();
//            content[i][3] = obj.get("stuSchoolName").tostring();
//            content[i][4] = obj.get("stuClassName").tostring();
//　　　　　　}
//
//　　　　　　//创建HSSFWorkbook
//　　　　　　HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);
//
//　　　　　　//响应到客户端
//　　　　　　try {
//　　　　　　　　this.setResponseHeader(response, fileName);
//       　　　　OutputStream os = response.getOutputStream();
//       　　　　wb.write(os);
//       　　　　os.flush();
//       　　　　os.close();
// 　　　　　　} catch (Exception e) {
//       　　　　e.printStackTrace();
// 　　　　　　}
//　　}

    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
