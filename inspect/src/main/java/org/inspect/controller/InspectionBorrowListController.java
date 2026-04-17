package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.util.FileDownload;
import org.inspect.util.PageData;
import org.inspect.util.Xml2XmlDoc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@Api(tags="借阅清单")
@RequestMapping(value = "/inspectionBorrowListController", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionBorrowListController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private  String saveDir;

    @RequestMapping(value = "/viewBorrowList")
    @ApiOperation("预览文件")
    public void viewBorrowList(@RequestBody(required = false) JSONObject param,HttpServletResponse response) throws Exception {
        PageData pd = this.getPageData(param);

    }
}
