package cn.cdsoft.modules.qxz.excel;

/**
 * Created by ZZUSER on 2018/12/3.
 */


import org.apache.poi.hssf.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

public class ExcelUtil {

    /**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param values 内容
     * @param wb HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[]title, String[][]values, HSSFWorkbook wb){

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        //创建内容
        for(int i=0;i<values.length;i++){
            row = sheet.createRow(i + 1);
            for(int j=0;j<values[i].length;j++){
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return wb;
    }

    public static void export(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,List<Excel> list){
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("用户表");
// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
// 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        sheet.setDefaultColumnWidth(30);
        HSSFFont nameRowFont = wb.createFont();
        nameRowFont.setFontName("微软雅黑");
        nameRowFont.setFontHeightInPoints((short) 10);// 设置字体大小
//        nameRowFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
        style.setFont(nameRowFont);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("归属区域");
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue("描述");
        cell.setCellStyle(style);
        cell = row.createCell((short) 2);
        cell.setCellValue("手机");
        cell.setCellStyle(style);
        cell = row.createCell((short) 3);
        cell.setCellValue("邮箱");
        cell.setCellStyle(style);
        cell = row.createCell((short) 4);
        cell.setCellValue("关键字");
        cell.setCellStyle(style);
        cell = row.createCell((short) 5);
        cell.setCellValue("openId");
        cell.setCellStyle(style);
        cell = row.createCell((short) 6);
        cell.setCellValue("启用/禁用");
        cell.setCellStyle(style);
        cell = row.createCell((short) 7);
        cell.setCellValue("备注");
        cell.setCellStyle(style);
// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
//        List list = null;
//        try {
//            list = getStudent();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        for (int i = 0; i < list.size(); i++)
        {
            row = sheet.createRow((int) i + 1);
            Excel excel = (Excel) list.get(i);
// 第四步，创建单元格，并设置值
            cell = row.createCell((short) 0);
            cell.setCellValue( excel.getOrg());
            cell.setCellStyle(style);
            cell = row.createCell((short) 1);
            cell.setCellValue(excel.getPrec());
            cell.setCellStyle(style);
            cell = row.createCell((short) 2);
            cell.setCellValue(excel.getPhone());
            cell.setCellStyle(style);
            cell = row.createCell((short) 3);
            cell.setCellValue(excel.getEmail());
            cell.setCellStyle(style);
            cell = row.createCell((short) 4);
            cell.setCellValue(excel.getKeyword());
            cell.setCellStyle(style);
            cell = row.createCell((short) 5);
            cell.setCellValue(excel.getOpenId());
            cell.setCellStyle(style);
            cell = row.createCell((short) 6);
            cell.setCellValue(excel.getState());
            cell.setCellStyle(style);
            cell = row.createCell((short) 7);
            cell.setCellValue(excel.getDesct());
            cell.setCellStyle(style);
        }
// 第六步，将文件存到指定位置
//        try
//        {
//            FileOutputStream fout = new FileOutputStream("D:/students.xls");
//            wb.write(fout);
//            fout.close();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }


        //响应到客户端
        try {
            String fileName = "用户表"+ System.currentTimeMillis();
            setResponseHeader(httpServletResponse, fileName);
            OutputStream os = httpServletResponse.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //报警报表导出
    public static void exportReport(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,List<ReportExcel> list){
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("各单位报警处理情况表");
// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
// 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        sheet.setDefaultColumnWidth(30);
        HSSFFont nameRowFont = wb.createFont();
        nameRowFont.setFontName("微软雅黑");
        nameRowFont.setFontHeightInPoints((short) 12);// 设置字体大小
//        nameRowFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
        style.setFont(nameRowFont);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("报警区域");
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue("配电房名称");
        cell.setCellStyle(style);
        cell = row.createCell((short) 2);
        cell.setCellValue("报警类型");
        cell.setCellStyle(style);
        cell = row.createCell((short) 3);
        cell.setCellValue("报警来源");
        cell.setCellStyle(style);
        cell = row.createCell((short) 4);
        cell.setCellValue("总报警次数");
        cell.setCellStyle(style);
        cell = row.createCell((short) 5);
        cell.setCellValue("待确认次数");
        cell.setCellStyle(style);
        cell = row.createCell((short) 6);
        cell.setCellValue("已确认次数");
        cell.setCellStyle(style);
        cell = row.createCell((short) 7);
        cell.setCellValue("已派单次数");
        cell.setCellStyle(style);
        cell = row.createCell((short) 8);
        cell.setCellValue("已接单次数");
        cell.setCellStyle(style);
        cell = row.createCell((short) 9);
        cell.setCellValue("处理中次数");
        cell.setCellStyle(style);
        cell = row.createCell((short) 10);
        cell.setCellValue("报警解除次数");
        cell.setCellStyle(style);
        cell = row.createCell((short) 11);
        cell.setCellValue("报警处理完成率");
        cell.setCellStyle(style);

        HSSFCellStyle style1 = wb.createCellStyle();
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
//        sheet.setDefaultColumnWidth(30);
        HSSFFont nameRowFont1 = wb.createFont();
        nameRowFont1.setFontName("微软雅黑");
        nameRowFont1.setFontHeightInPoints((short) 10);// 设置字体大小
        style1.setFont(nameRowFont1);
        for (int i = 0; i < list.size(); i++)
        {
            row = sheet.createRow((int) i + 1);
            ReportExcel excel =  list.get(i);
// 第四步，创建单元格，并设置值
            cell = row.createCell((short) 0);
            cell.setCellValue( excel.getAlarmAddr());
            cell.setCellStyle(style1);
            cell = row.createCell((short) 1);
            cell.setCellValue(excel.getPdfName());
            cell.setCellStyle(style1);
            cell = row.createCell((short) 2);
            cell.setCellValue(excel.getAlarmType());
            cell.setCellStyle(style1);
            cell = row.createCell((short) 3);
            cell.setCellValue(excel.getAlarmSource());
            cell.setCellStyle(style1);
            cell = row.createCell((short) 4);
            cell.setCellValue(excel.getTotal());
            cell.setCellStyle(style1);
            cell = row.createCell((short) 5);
            cell.setCellValue(excel.getState0());
            cell.setCellStyle(style1);
            cell = row.createCell((short) 6);
            cell.setCellValue(excel.getState1());
            cell.setCellStyle(style1);
            cell = row.createCell((short) 7);
            cell.setCellValue(excel.getState2());
            cell.setCellStyle(style1);
            cell = row.createCell((short) 8);
            cell.setCellValue(excel.getState3());
            cell.setCellStyle(style1);
            cell = row.createCell((short) 9);
            cell.setCellValue(excel.getState4());
            cell.setCellStyle(style1);
            cell = row.createCell((short) 10);
            cell.setCellValue(excel.getState5());
            cell.setCellStyle(style1);
            cell = row.createCell((short) 11);
            cell.setCellValue(excel.getOver());
            cell.setCellStyle(style1);
        }
        //响应到客户端
        try {
            String fileName = "各单位报警处理情况表"+ System.currentTimeMillis();
            setResponseHeader(httpServletResponse, fileName);
            OutputStream os = httpServletResponse.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //值班导出
    public static void exportScheduling(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,List<SchedulingExcel> list){
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("值班表");
// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
// 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        sheet.setDefaultColumnWidth(30);
        HSSFFont nameRowFont = wb.createFont();
        nameRowFont.setFontName("微软雅黑");
        nameRowFont.setFontHeightInPoints((short) 12);// 设置字体大小
//        nameRowFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
        style.setFont(nameRowFont);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("地区");
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue("规则名称");
        cell.setCellStyle(style);
        cell = row.createCell((short) 2);
        cell.setCellValue("开始时间");
        cell.setCellStyle(style);
        cell = row.createCell((short) 3);
        cell.setCellValue("结束时间");
        cell.setCellStyle(style);
        cell = row.createCell((short) 4);
        cell.setCellValue("值班人员");
        cell.setCellStyle(style);

        HSSFCellStyle style1 = wb.createCellStyle();
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
//        sheet.setDefaultColumnWidth(30);
        HSSFFont nameRowFont1 = wb.createFont();
        nameRowFont1.setFontName("微软雅黑");
        nameRowFont1.setFontHeightInPoints((short) 10);// 设置字体大小
        style1.setFont(nameRowFont1);

        HSSFCellStyle style2 = wb.createCellStyle();
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
//        sheet.setDefaultColumnWidth(30);
        HSSFFont nameRowFont2 = wb.createFont();
        nameRowFont2.setFontName("微软雅黑");
        nameRowFont2.setFontHeightInPoints((short) 10);// 设置字体大小
        nameRowFont2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
        style2.setFont(nameRowFont2);

        for (int i = 0; i < list.size(); i++)
        {
            row = sheet.createRow((int) i + 1);
            SchedulingExcel excel =  list.get(i);
// 第四步，创建单元格，并设置值

            cell = row.createCell((short) 0);
            cell.setCellValue( excel.getOrgName());
            if(excel.getName()==null){
                cell.setCellStyle(style2);
            }else {
                cell.setCellStyle(style1);
            }
            cell = row.createCell((short) 1);
            cell.setCellValue(excel.getName());
            cell.setCellStyle(style1);
            cell = row.createCell((short) 2);
            cell.setCellValue(excel.getStartTime());
            cell.setCellStyle(style1);
            cell = row.createCell((short) 3);
            cell.setCellValue(excel.getEndTime());
            cell.setCellStyle(style1);
            cell = row.createCell((short) 4);
            cell.setCellValue(excel.getWatchkeeper());
            cell.setCellStyle(style1);
        }
        //响应到客户端
        try {
            String fileName = "值班表"+ System.currentTimeMillis();
            setResponseHeader(httpServletResponse, fileName);
            OutputStream os = httpServletResponse.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //发送响应流方法
    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
//            try {
////                fileName = new String(fileName.getBytes(),"ISO8859-1");
//            } catch (UnsupportedEncodingException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            response.setContentType("application/octet-stream;charset=UTF-8");
//            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
//            response.addHeader("Pargam", "no-cache");
//            response.addHeader("Cache-Control", "no-cache");

//            response.setContentType("application/ms-excel;charset=UTF-8");
//            response.setHeader("Content-Disposition", "attachment;filename="
//                    .concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));

            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String((fileName).getBytes("gb2312"), "ISO-8859-1") + ".xls");
            System.out.println("成功");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
