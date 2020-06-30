package com.ihrm.employee.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.poi.ExcelExportUtil;
import com.ihrm.common.utils.BeanMapUtils;
import com.ihrm.common.utils.DownloadUtils;
import com.ihrm.domain.employee.*;
import com.ihrm.domain.employee.response.EmployeeReportResult;
import com.ihrm.employee.service.*;
import net.sf.jasperreports.engine.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/employees")
public class EmployeeController extends BaseController {

    @Autowired
    private UserCompanyPersonalService userCompanyPersonalService;

    @Autowired
    private UserCompanyJobsService userCompanyJobsService;

    @Autowired
    private ResignationService resignationService;

    @Autowired
    private TransferPositionService transferPositionService;

    @Autowired
    private PositiveService positiveService;

    @Autowired
    private ArchiveService archiveService;



    /**
     * 员工个人信息保存
     */
    @RequestMapping(value = "/{id}/personalInfo", method = RequestMethod.PUT)
    public Result savePersonalInfo(@PathVariable(name = "id") String uid, @RequestBody Map map) throws Exception {
        UserCompanyPersonal sourceInfo = BeanMapUtils.mapToBean(map, UserCompanyPersonal.class);
        if (sourceInfo == null) {
            sourceInfo = new UserCompanyPersonal();
        }
        sourceInfo.setUserId(uid);
        sourceInfo.setCompanyId(super.companyId);
        userCompanyPersonalService.save(sourceInfo);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 员工个人信息读取
     */
    @RequestMapping(value = "/{id}/personalInfo", method = RequestMethod.GET)
    public Result findPersonalInfo(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyPersonal info = userCompanyPersonalService.findById(uid);
        if(info == null) {
            info = new UserCompanyPersonal();
            info.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,info);
    }

    /**
     * 员工岗位信息保存
     */
    @RequestMapping(value = "/{id}/jobs", method = RequestMethod.PUT)
    public Result saveJobsInfo(@PathVariable(name = "id") String uid, @RequestBody UserCompanyJobs sourceInfo) throws Exception {
        //更新员工岗位信息
        if (sourceInfo == null) {
            sourceInfo = new UserCompanyJobs();
            sourceInfo.setUserId(uid);
            sourceInfo.setCompanyId(super.companyId);
        }
        userCompanyJobsService.save(sourceInfo);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 员工岗位信息读取
     */
    @RequestMapping(value = "/{id}/jobs", method = RequestMethod.GET)
    public Result findJobsInfo(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyJobs info = userCompanyJobsService.findById(uid);
        if(info == null) {
            info = new UserCompanyJobs();
            info.setUserId(uid);
            info.setCompanyId(companyId);
        }
        return new Result(ResultCode.SUCCESS,info);
    }

    /**
     * 离职表单保存
     */
    @RequestMapping(value = "/{id}/leave", method = RequestMethod.PUT)
    public Result saveLeave(@PathVariable(name = "id") String uid, @RequestBody EmployeeResignation resignation) throws Exception {
        resignation.setUserId(uid);
        resignationService.save(resignation);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 离职表单读取
     */
    @RequestMapping(value = "/{id}/leave", method = RequestMethod.GET)
    public Result findLeave(@PathVariable(name = "id") String uid) throws Exception {
        EmployeeResignation resignation = resignationService.findById(uid);
        if(resignation == null) {
            resignation = new EmployeeResignation();
            resignation.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,resignation);
    }



    /**
     * 调岗表单保存
     */
    @RequestMapping(value = "/{id}/transferPosition", method = RequestMethod.PUT)
    public Result saveTransferPosition(@PathVariable(name = "id") String uid, @RequestBody EmployeeTransferPosition transferPosition) throws Exception {
        transferPosition.setUserId(uid);
        transferPositionService.save(transferPosition);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 调岗表单读取
     */
    @RequestMapping(value = "/{id}/transferPosition", method = RequestMethod.GET)
    public Result findTransferPosition(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyJobs jobsInfo = userCompanyJobsService.findById(uid);
        if(jobsInfo == null) {
            jobsInfo = new UserCompanyJobs();
            jobsInfo.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,jobsInfo);
    }

    /**
     * 转正表单保存
     */
    @RequestMapping(value = "/{id}/positive", method = RequestMethod.PUT)
    public Result savePositive(@PathVariable(name = "id") String uid, @RequestBody EmployeePositive positive) throws Exception {
        positiveService.save(positive);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 转正表单读取
     */
    @RequestMapping(value = "/{id}/positive", method = RequestMethod.GET)
    public Result findPositive(@PathVariable(name = "id") String uid) throws Exception {
        EmployeePositive positive = positiveService.findById(uid);
        if(positive == null) {
            positive = new EmployeePositive();
            positive.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,positive);
    }

    /**
     * 历史归档详情列表
     */
    @RequestMapping(value = "/archives/{month}", method = RequestMethod.GET)
    public Result archives(@PathVariable(name = "month") String month, @RequestParam(name = "type") Integer type) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 归档更新
     */
    @RequestMapping(value = "/archives/{month}", method = RequestMethod.PUT)
    public Result saveArchives(@PathVariable(name = "month") String month) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 历史归档列表
     */
    @RequestMapping(value = "/archives", method = RequestMethod.GET)
    public Result findArchives(@RequestParam(name = "pagesize") Integer pagesize, @RequestParam(name = "page") Integer page, @RequestParam(name = "year") String year) throws Exception {
        Map map = new HashMap();
        map.put("year",year);
        map.put("companyId",companyId);
        Page<EmployeeArchive> searchPage = archiveService.findSearch(map, page, pagesize);
        PageResult<EmployeeArchive> pr = new PageResult(searchPage.getTotalElements(),searchPage.getContent());
        return new Result(ResultCode.SUCCESS,pr);
    }

    /**
     * 当月人事报表导出
     * 使用工具类导出的时候需要在实体类需要导出的字段上加上@excelArrtibute
     */
    @GetMapping(value = "/export/{month}")
    public void export(@PathVariable String month) throws Exception {

        // 获取报表数据
        List<EmployeeReportResult> list = userCompanyPersonalService.findByReport(companyId , month);
        //2.加载模版
        Resource resource = new ClassPathResource("excel-template/hr-demo.xlsx");
        FileInputStream fis = new FileInputStream(resource.getFile());

        //3.通过工具类下载文件
        new ExcelExportUtil(EmployeeReportResult.class , 2, 2)
                .export(response , fis , list , month + "月人事报表.xlsx");
      /*  // 获取报表数据
        List<EmployeeReportResult> list = userCompanyPersonalService.findByReport(companyId , month);
        //2.加载模版
        Resource resource = new ClassPathResource("excel-template/hr-demo.xlsx");
        FileInputStream fis = new FileInputStream(resource.getFile());
      //3.根据模板创建工作簿
        Workbook wb = new XSSFWorkbook(fis);
        //4.读取工作表
        Sheet sheet = wb.getSheetAt(0);
        //5.抽取公共样式
        Row row = sheet.getRow(2);
        CellStyle styles[] = new CellStyle[row.getLastCellNum()];
        for (int i = 0;i < row.getLastCellNum(); i++){
            Cell cell = row.getCell(i);
            styles[i] = cell.getCellStyle();
        }
        //6.构造单元格
        int rowIndex = 2;
        Cell cell = null;

        for (EmployeeReportResult employeeReportResult : list) {
            row = sheet.createRow(rowIndex++);
            //编号,
            cell = row.createCell(0);
            cell.setCellValue(employeeReportResult.getUserId());
            cell.setCellStyle(styles[0]);
            //姓名,
            cell = row.createCell(1);
            cell.setCellValue(employeeReportResult.getUsername());
            cell.setCellStyle(styles[1]);
            //手机,
            cell = row.createCell(2);
            cell.setCellValue(employeeReportResult.getMobile());
            cell.setCellStyle(styles[2]);
            //最高学历,
            cell = row.createCell(3);
            cell.setCellValue(employeeReportResult.getTheHighestDegreeOfEducation());
            cell.setCellStyle(styles[3]);
            //国家地区,
            cell = row.createCell(4);
            cell.setCellValue(employeeReportResult.getNationalArea());
            cell.setCellStyle(styles[4]);
            //护照号,
            cell = row.createCell(5);
            cell.setCellValue(employeeReportResult.getPassportNo());
            cell.setCellStyle(styles[5]);
            //籍贯,
            cell = row.createCell(6);
            cell.setCellValue(employeeReportResult.getNativePlace());
            cell.setCellStyle(styles[6]);
            //生日,
            cell = row.createCell(7);
            cell.setCellValue(employeeReportResult.getBirthday());
            cell.setCellStyle(styles[7]);
            //属相,
            cell = row.createCell(8);
            cell.setCellValue(employeeReportResult.getZodiac());
            cell.setCellStyle(styles[8]);
            //入职时间,
            cell = row.createCell(9);
            cell.setCellValue(employeeReportResult.getTimeOfEntry());
            cell.setCellStyle(styles[9]);
            //离职类型,
            cell = row.createCell(10);
            cell.setCellValue(employeeReportResult.getTypeOfTurnover());
            cell.setCellStyle(styles[10]);
            //离职原因,
            cell = row.createCell(11);
            cell.setCellValue(employeeReportResult.getReasonsForLeaving());
            cell.setCellStyle(styles[11]);
            //离职时间
            cell = row.createCell(12);
            cell.setCellValue(employeeReportResult.getResignationTime());
            cell.setCellStyle(styles[12]);
        }
        //7.下载
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        wb.write(os);
        new DownloadUtils().download(os , response , month + "月人事报表.xlsx");
*/
    }




}
