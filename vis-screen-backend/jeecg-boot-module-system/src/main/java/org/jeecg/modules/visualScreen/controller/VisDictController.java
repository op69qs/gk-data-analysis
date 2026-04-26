package org.jeecg.modules.visualScreen.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.vo.SysDictPage;
import org.jeecg.modules.visualScreen.util.VisLegacyDictScope;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "vis字典")
@RequestMapping("/visDict")
public class VisDictController {

	@Autowired
	private ISysDictService sysDictService;

	@Autowired
	private ISysDictItemService sysDictItemService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ApiOperation("vis字典列表")
	public Result<IPage<SysDict>> queryPageList(SysDict sysDict,
									@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
									@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
									HttpServletRequest req) {
		Result<IPage<SysDict>> result = new Result<IPage<SysDict>>();
		QueryWrapper<SysDict> queryWrapper = QueryGenerator.initQueryWrapper(sysDict, req.getParameterMap());
		VisLegacyDictScope.apply(queryWrapper);
		List<SysDict> sortedList = VisLegacyDictScope.sort(sysDictService.list(queryWrapper));
		result.setSuccess(true);
		result.setResult(VisLegacyDictScope.buildPage(sortedList, pageNo, pageSize));
		return result;
	}

	@RequestMapping(value = "/exportXls", method = RequestMethod.GET)
	@ApiOperation("导出vis字典")
	public ModelAndView exportXls(SysDict sysDict, HttpServletRequest request) {
		QueryWrapper<SysDict> queryWrapper = QueryGenerator.initQueryWrapper(sysDict, request.getParameterMap());
		VisLegacyDictScope.apply(queryWrapper);
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<SysDictPage> pageList = new ArrayList<SysDictPage>();

		List<SysDict> sysDictList = VisLegacyDictScope.sort(sysDictService.list(queryWrapper));
		for (SysDict dictMain : sysDictList) {
			SysDictPage vo = new SysDictPage();
			BeanUtils.copyProperties(dictMain, vo);
			List<SysDictItem> sysDictItemList = sysDictItemService.selectItemsByMainId(dictMain.getId());
			vo.setSysDictItemList(sysDictItemList);
			pageList.add(vo);
		}

		mv.addObject(NormalExcelConstants.FILE_NAME, "vis数据字典");
		mv.addObject(NormalExcelConstants.CLASS, SysDictPage.class);
		LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("vis数据字典列表", "导出人:" + user.getRealname(), "vis数据字典"));
		mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
		return mv;
	}
}