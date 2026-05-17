package org.jeecg.modules.system.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.MD5Util;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.entity.SysPermissionDataRule;
import org.jeecg.modules.system.entity.SysRolePermission;
import org.jeecg.modules.system.model.SysPermissionTree;
import org.jeecg.modules.system.model.TreeModel;
import org.jeecg.modules.system.service.ISysPermissionDataRuleService;
import org.jeecg.modules.system.service.ISysPermissionService;
import org.jeecg.modules.system.service.ISysRolePermissionService;
import org.jeecg.modules.system.util.PageData;
import org.jeecg.modules.system.util.PermissionDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 菜单权限表 前端控制器
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
@Slf4j
@RestController
@RequestMapping("/sys/permission")
public class SysPermissionController extends BaseController{

	private static final String NEXUS_PORTAL_USER_ID_PREFIX = "PREFIX_NEXUS_PORTAL_USER_ID_";
	private static final String NEXUS_PORTAL_ACCESS_TOKEN_PREFIX = "PREFIX_NEXUS_PORTAL_ACCESS_TOKEN_";

	@Autowired
	private ISysPermissionService sysPermissionService;

	@Autowired
	private ISysRolePermissionService sysRolePermissionService;

	@Autowired
	private ISysPermissionDataRuleService sysPermissionDataRuleService;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${gk-nexus.sync.user-permissions-url:http://localhost:3000/api/sync/users/{userId}/permissions}")
	private String nexusUserPermissionsUrl;

	@Value("${gk-nexus.sync.sys-code:GK_DATA_ANALYSIS}")
	private String nexusSysCode;

	/**
	 * 加载数据节点
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<List<SysPermissionTree>> list() {
		Result<List<SysPermissionTree>> result = new Result<>();
		try {
			LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
			query.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
			query.orderByAsc(SysPermission::getSortNo);
			List<SysPermission> list = sysPermissionService.list(query);
			List<SysPermissionTree> treeList = new ArrayList<>();
			getTreeList(treeList, list, null);
			result.setResult(treeList);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

//	/**
//	 * 查询用户拥有的菜单权限和按钮权限（根据用户账号）
//	 * 
//	 * @return
//	 */
//	@RequestMapping(value = "/queryByUser", method = RequestMethod.GET)
//	public Result<JSONArray> queryByUser(HttpServletRequest req) {
//		Result<JSONArray> result = new Result<>();
//		try {
//			String username = req.getParameter("username");
//			List<SysPermission> metaList = sysPermissionService.queryByUser(username);
//			JSONArray jsonArray = new JSONArray();
//			this.getPermissionJsonArray(jsonArray, metaList, null);
//			result.setResult(jsonArray);
//			result.success("查询成功");
//		} catch (Exception e) {
//			result.error500("查询失败:" + e.getMessage());
//			log.error(e.getMessage(), e);
//		}
//		return result;
//	}

	/**
	 * 验证用户跳转菜单权限
	 * @param param
	 * @return
	 */
	@PostMapping("/checkUserAccessRight")
	public Map<String, Object> checkUserAccessRight(
			@RequestBody JSONObject param
	){
		Map<String, Object> res = new HashMap<>();
	 	String result = "";
	 	String msg = "";
		PageData pd = this.getPageData(param);
		List<String> urlArr = (List<String>)JSONObject.parse(pd.getString("urlArr"));
		if( urlArr == null || urlArr.size() == 0 ){
			res.put("result", "false");
			res.put("msg", "权限不足，无法查看");
			return res;
		}
		pd.put("urlArr", urlArr);
		List<SysPermission> metaList = sysPermissionService.getPermissionInfoByUser(pd);
		if(urlArr.size() > metaList.size()){
			res.put("result", "false");
			res.put("msg", "权限不足，无法查看");
			return res;
		}
		for (int i = 0; i < urlArr.size(); i++) {
			for (SysPermission meta : metaList) {
				if (urlArr.get(i).equals(meta.getUrl())) {
					result = "success";
					msg = "";
					break;
				} else {
					result = "false";
					msg = "权限不足，无法查看";
				}
			}
		}
		res.put("result", result);
		res.put("msg", msg);
		return res;
	}

	/**
	 * 查询用户拥有的菜单权限和按钮权限（根据TOKEN）
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getUserPermissionByToken", method = RequestMethod.GET)
	public Result<?> getUserPermissionByToken(@RequestParam(name = "token", required = true) String token) {
		Result<JSONObject> result = new Result<JSONObject>();
		try {
			if (oConvertUtils.isEmpty(token)) {
				return Result.error("TOKEN不允许为空！");
			}
			log.info(" ------ 通过令牌获取用户拥有的访问菜单 ---- TOKEN ------ " + token);
			String username = JwtUtil.getUsername(token);
			boolean portalContext = hasPortalPermissionContext(token);
			List<SysPermission> metaList;
			if (portalContext) {
				JSONObject portalPermissionData = fetchPortalPermissionData(token);
				Set<String> portalPermissionCodes = extractPortalPermissionCodes(portalPermissionData);
				metaList = buildPermissionListByPortalPermissions(portalPermissionData);
				if (metaList.isEmpty()) {
					metaList = buildPermissionListByPortalCodes(portalPermissionCodes);
				}
				long matchedMenuCount = metaList.stream()
						.filter(p -> p.getMenuType() != null && (CommonConstant.MENU_TYPE_0.equals(p.getMenuType()) || CommonConstant.MENU_TYPE_1.equals(p.getMenuType())))
						.count();
				log.info("门户权限同步结果，username={}, portalCodeCount={}, localPermissionCount={}, matchedMenuCount={}", username,
						portalPermissionCodes == null ? 0 : portalPermissionCodes.size(), metaList.size(), matchedMenuCount);
			} else {
				metaList = loadLocalPermissionList(username);
				long localMenuCount = metaList.stream()
						.filter(p -> p.getMenuType() != null && (CommonConstant.MENU_TYPE_0.equals(p.getMenuType()) || CommonConstant.MENU_TYPE_1.equals(p.getMenuType())))
						.count();
				log.info("本地登录权限结果，username={}, localPermissionCount={}, localMenuCount={}", username, metaList.size(), localMenuCount);
			}
			PermissionDataUtil.addIndexPage(metaList);
			JSONObject json = new JSONObject();
			JSONArray menujsonArray = new JSONArray();
			this.getPermissionJsonArray(menujsonArray, metaList, null);
			JSONArray authjsonArray = new JSONArray();
			this.getAuthJsonArray(authjsonArray, metaList);
			//查询所有的权限
			LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
			query.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
			query.eq(SysPermission::getMenuType, CommonConstant.MENU_TYPE_2);
			//query.eq(SysPermission::getStatus, "1");
			List<SysPermission> allAuthList = sysPermissionService.list(query);
			JSONArray allauthjsonArray = new JSONArray();
			this.getAllAuthJsonArray(allauthjsonArray, allAuthList);
			json.put("menu", menujsonArray);
			json.put("auth", authjsonArray);
			json.put("allAuth", allauthjsonArray);
			result.setResult(json);
			result.success("查询成功");
		} catch (Exception e) {
			result.error500("查询失败:" + e.getMessage());  
			log.error(e.getMessage(), e);
		}
		return result;
	}

	private boolean hasPortalPermissionContext(String localJwtToken) {
		String portalUserId = (String) redisUtil.get(NEXUS_PORTAL_USER_ID_PREFIX + localJwtToken);
		String portalAccessToken = (String) redisUtil.get(NEXUS_PORTAL_ACCESS_TOKEN_PREFIX + localJwtToken);
		return oConvertUtils.isNotEmpty(portalUserId) && oConvertUtils.isNotEmpty(portalAccessToken);
	}

	private JSONObject fetchPortalPermissionData(String localJwtToken) {
		String portalUserId = (String) redisUtil.get(NEXUS_PORTAL_USER_ID_PREFIX + localJwtToken);
		String portalAccessToken = (String) redisUtil.get(NEXUS_PORTAL_ACCESS_TOKEN_PREFIX + localJwtToken);
		if (oConvertUtils.isEmpty(portalUserId) || oConvertUtils.isEmpty(portalAccessToken)) {
			return null;
		}

		long begin = System.currentTimeMillis();
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + portalAccessToken);
			HttpEntity<Void> requestEntity = new HttpEntity<Void>(headers);

			Map<String, String> uriVars = new HashMap<String, String>();
			uriVars.put("userId", portalUserId);
			String requestUrl = nexusUserPermissionsUrl + "?sysCode=" + nexusSysCode;
			log.info("开始拉取门户权限，portalUserId={}, sysCode={}, requestUrl={}", portalUserId, nexusSysCode, requestUrl);
			ResponseEntity<JSONObject> response = restTemplate.exchange(requestUrl, HttpMethod.GET, requestEntity, JSONObject.class, uriVars);
			log.info("门户权限拉取完成，status={}, costMs={}", response.getStatusCodeValue(), (System.currentTimeMillis() - begin));
			JSONObject body = response.getBody();
			if (body == null || !Boolean.TRUE.equals(body.getBoolean("success"))) {
				return null;
			}
			return body.getJSONObject("data");
		} catch (Exception e) {
			log.warn("门户权限拉取失败，costMs={}, errorType={}, error={}", (System.currentTimeMillis() - begin), e.getClass().getSimpleName(), e.getMessage());
			return null;
		}
	}

	private Set<String> extractPortalPermissionCodes(JSONObject data) {
		if (data == null) {
			return Collections.emptySet();
		}
		JSONArray grantedCodes = data.getJSONArray("grantedLocalPermissionCodes");
		if (grantedCodes == null || grantedCodes.isEmpty()) {
			grantedCodes = data.getJSONArray("permissionCodes");
		}
		if (grantedCodes == null || grantedCodes.isEmpty()) {
			return Collections.emptySet();
		}

		Set<String> codeSet = new HashSet<String>();
		for (int i = 0; i < grantedCodes.size(); i++) {
			String code = grantedCodes.getString(i);
			if (oConvertUtils.isNotEmpty(code)) {
				codeSet.add(code);
			}
		}
		return codeSet;
	}

	private List<SysPermission> buildPermissionListByPortalPermissions(JSONObject data) {
		if (data == null) {
			return new ArrayList<SysPermission>();
		}
		JSONArray permissions = data.getJSONArray("permissions");
		if (permissions == null || permissions.isEmpty()) {
			return new ArrayList<SysPermission>();
		}

		Map<String, String> portalIdToCode = new HashMap<String, String>();
		for (int i = 0; i < permissions.size(); i++) {
			JSONObject item = permissions.getJSONObject(i);
			if (item == null) {
				continue;
			}
			String localCode = firstNonEmpty(item.getString("localPermissionCode"), item.getString("code"), item.getString("id"));
			if (oConvertUtils.isEmpty(localCode)) {
				continue;
			}
			String portalId = item.getString("id");
			if (oConvertUtils.isNotEmpty(portalId)) {
				portalIdToCode.put(portalId, localCode);
			}
		}

		List<SysPermission> list = new ArrayList<SysPermission>();
		Set<String> parentIds = new HashSet<String>();
		for (int i = 0; i < permissions.size(); i++) {
			JSONObject item = permissions.getJSONObject(i);
			if (item == null) {
				continue;
			}

			String localCode = firstNonEmpty(item.getString("localPermissionCode"), item.getString("code"), item.getString("id"));
			if (oConvertUtils.isEmpty(localCode)) {
				continue;
			}

			String parentCode = item.getString("localParentPermissionCode");
			if (oConvertUtils.isEmpty(parentCode)) {
				String portalParentId = item.getString("parentId");
				if (oConvertUtils.isNotEmpty(portalParentId) && !"0".equals(portalParentId)) {
					parentCode = portalIdToCode.get(portalParentId);
				}
			}

			SysPermission permission = new SysPermission();
			permission.setId(localCode);
			permission.setParentId(parentCode);
			permission.setName(item.getString("name"));
			permission.setPerms(localCode);
			permission.setPermsType("1");
			permission.setIcon(item.getString("icon"));
			permission.setComponent(item.getString("component"));
			permission.setComponentName(item.getString("key"));
			permission.setUrl(item.getString("path"));
			permission.setSortNo(item.getInteger("sort"));
			permission.setMenuType(convertPortalType(item.getString("type")));
			permission.setRoute(!CommonConstant.MENU_TYPE_2.equals(permission.getMenuType()));
			permission.setKeepAlive(false);
			permission.setRuleFlag(0);
			permission.setDelFlag(CommonConstant.DEL_FLAG_0);
			permission.setStatus(Boolean.TRUE.equals(item.getBoolean("enabled")) ? CommonConstant.STATUS_1 : "0");
			permission.setAlwaysShow(false);
			permission.setHidden(Boolean.TRUE.equals(item.getBoolean("hideInMenu")));

			if (oConvertUtils.isNotEmpty(parentCode)) {
				parentIds.add(parentCode);
			}
			list.add(permission);
		}

		for (SysPermission permission : list) {
			permission.setLeaf(!parentIds.contains(permission.getId()));
		}
		return list;
	}

	private Integer convertPortalType(String portalType) {
		if ("BUTTON".equalsIgnoreCase(portalType)) {
			return CommonConstant.MENU_TYPE_2;
		}
		if ("DIRECTORY".equalsIgnoreCase(portalType)) {
			return CommonConstant.MENU_TYPE_0;
		}
		return CommonConstant.MENU_TYPE_1;
	}

	private String firstNonEmpty(String... values) {
		if (values == null || values.length == 0) {
			return null;
		}
		for (String value : values) {
			if (oConvertUtils.isNotEmpty(value)) {
				return value;
			}
		}
		return null;
	}

	private List<SysPermission> buildPermissionListByPortalCodes(Set<String> portalCodes) {
		if (portalCodes == null || portalCodes.isEmpty()) {
			log.warn("门户权限码为空，返回空权限集合（不回退本地权限）");
			return new ArrayList<SysPermission>();
		}

		Set<String> normalizedPortalCodes = new HashSet<String>();
		for (String portalCode : portalCodes) {
			if (oConvertUtils.isEmpty(portalCode)) {
				continue;
			}
			String normalized = normalizePortalCode(portalCode);
			if (oConvertUtils.isNotEmpty(normalized)) {
				normalizedPortalCodes.add(normalized);
			}
		}
		if (normalizedPortalCodes.isEmpty()) {
			log.warn("门户权限码归一化后为空，返回空权限集合（不回退本地权限）");
			return new ArrayList<SysPermission>();
		}

		LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
		query.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
		query.eq(SysPermission::getStatus, CommonConstant.STATUS_1);
		query.orderByAsc(SysPermission::getSortNo);
		List<SysPermission> allPermissions = sysPermissionService.list(query);

		Map<String, SysPermission> idMap = new HashMap<String, SysPermission>();
		Set<String> selectedIds = new HashSet<String>();
		for (SysPermission permission : allPermissions) {
			idMap.put(permission.getId(), permission);
			if (permissionMatchesPortalCodes(permission, normalizedPortalCodes)) {
				selectedIds.add(permission.getId());
			}
		}

		if (selectedIds.isEmpty()) {
			List<String> samplePortalCodes = normalizedPortalCodes.stream().limit(20).collect(Collectors.toList());
			log.warn("门户权限码未匹配到本地权限定义，portalCodeSample={}", samplePortalCodes);
		}

		Set<String> ancestorIds = new HashSet<String>();
		for (String selectedId : selectedIds) {
			String currentId = selectedId;
			while (oConvertUtils.isNotEmpty(currentId)) {
				SysPermission current = idMap.get(currentId);
				if (current == null) {
					break;
				}
				ancestorIds.add(current.getId());
				currentId = current.getParentId();
			}
		}

		List<SysPermission> result = new ArrayList<SysPermission>();
		for (SysPermission permission : allPermissions) {
			if (ancestorIds.contains(permission.getId())) {
				result.add(permission);
			}
		}

		long menuCount = result.stream()
				.filter(p -> p.getMenuType() != null && (CommonConstant.MENU_TYPE_0.equals(p.getMenuType()) || CommonConstant.MENU_TYPE_1.equals(p.getMenuType())))
				.count();
		if (menuCount == 0L) {
			List<String> samplePortalCodes = normalizedPortalCodes.stream().limit(20).collect(Collectors.toList());
			log.warn("门户权限映射后无可见菜单，portalCodeSample={}, matchedPermissionIds={}", samplePortalCodes, selectedIds.stream().limit(20).collect(Collectors.toList()));
		}
		return result;
	}

	private List<SysPermission> loadLocalPermissionList(String username) {
		if (oConvertUtils.isEmpty(username)) {
			return new ArrayList<SysPermission>();
		}
		return sysPermissionService.queryByUser(username);
	}

	private boolean permissionMatchesPortalCodes(SysPermission permission, Set<String> normalizedPortalCodes) {
		if (permission == null || normalizedPortalCodes == null || normalizedPortalCodes.isEmpty()) {
			return false;
		}

		Set<String> candidates = new HashSet<String>();
		if (oConvertUtils.isNotEmpty(permission.getId())) {
			candidates.add(permission.getId());
		}
		if (oConvertUtils.isNotEmpty(permission.getPerms())) {
			candidates.add(permission.getPerms());
		}
		if (oConvertUtils.isNotEmpty(permission.getUrl())) {
			candidates.add(permission.getUrl());
		}
		if (oConvertUtils.isNotEmpty(permission.getComponentName())) {
			candidates.add(permission.getComponentName());
		}

		for (String candidate : candidates) {
			String normalizedCandidate = normalizePortalCode(candidate);
			if (oConvertUtils.isNotEmpty(normalizedCandidate) && normalizedPortalCodes.contains(normalizedCandidate)) {
				return true;
			}
		}
		return false;
	}

	private String normalizePortalCode(String code) {
		if (oConvertUtils.isEmpty(code)) {
			return null;
		}
		String normalized = code.trim();
		while (normalized.startsWith("/")) {
			normalized = normalized.substring(1);
		}
		while (normalized.endsWith("/")) {
			normalized = normalized.substring(0, normalized.length() - 1);
		}
		return normalized;
	}

	/**
	  * 添加菜单
	 * @param permission
	 * @return
	 */
	@RequiresRoles({ "admin" })
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Result<SysPermission> add(@RequestBody SysPermission permission) {
		Result<SysPermission> result = new Result<SysPermission>();
		try {
			permission = PermissionDataUtil.intelligentProcessData(permission);
			sysPermissionService.addPermission(permission);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	  * 编辑菜单
	 * @param permission
	 * @return
	 */
	@RequiresRoles({ "admin" })
	@CacheEvict(value= CacheConstant.LOGIN_USER_RULES_CACHE, allEntries=true)
	@RequestMapping(value = "/edit", method = { RequestMethod.PUT, RequestMethod.POST })
	public Result<SysPermission> edit(@RequestBody SysPermission permission) {
		Result<SysPermission> result = new Result<>();
		try {
			permission = PermissionDataUtil.intelligentProcessData(permission);
			sysPermissionService.editPermission(permission);
			result.success("修改成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	  * 删除菜单
	 * @param id
	 * @return
	 */
	@RequiresRoles({ "admin" })
	@CacheEvict(value=CacheConstant.LOGIN_USER_RULES_CACHE, allEntries=true)
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public Result<SysPermission> delete(@RequestParam(name = "id", required = true) String id) {
		Result<SysPermission> result = new Result<>();
		try {
			sysPermissionService.deletePermission(id);
			sysPermissionService.deletePermRuleByPermId(id);
			result.success("删除成功!");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500(e.getMessage());
		}
		return result;
	}

	/**
	  * 批量删除菜单
	 * @param ids
	 * @return
	 */
	@RequiresRoles({ "admin" })
	@CacheEvict(value=CacheConstant.LOGIN_USER_RULES_CACHE, allEntries=true)
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	public Result<SysPermission> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		Result<SysPermission> result = new Result<>();
		try {
            String[] arr = ids.split(",");
			for (String id : arr) {
				if (oConvertUtils.isNotEmpty(id)) {
					sysPermissionService.deletePermission(id);
				}
			}
			result.success("删除成功!");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("删除成功!");
		}
		return result;
	}

	/**
	 * 获取全部的权限树
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
	public Result<Map<String, Object>> queryTreeList() {
		Result<Map<String, Object>> result = new Result<>();
		// 全部权限ids
		List<String> ids = new ArrayList<>();
		try {
			LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
			query.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
			query.orderByAsc(SysPermission::getSortNo);
			List<SysPermission> list = sysPermissionService.list(query);
			for (SysPermission sysPer : list) {
				ids.add(sysPer.getId());
			}
			List<TreeModel> treeList = new ArrayList<>();
			getTreeModelList(treeList, list, null);

			Map<String, Object> resMap = new HashMap<String, Object>();
			resMap.put("treeList", treeList); // 全部树节点数据
			resMap.put("ids", ids);// 全部树ids
			result.setResult(resMap);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 异步加载数据节点
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryListAsync", method = RequestMethod.GET)
	public Result<List<TreeModel>> queryAsync(@RequestParam(name = "pid", required = false) String parentId) {
		Result<List<TreeModel>> result = new Result<>();
		try {
			List<TreeModel> list = sysPermissionService.queryListByParentId(parentId);
			if (list == null || list.size() <= 0) {
				result.error500("未找到角色信息");
			} else {
				result.setResult(list);
				result.setSuccess(true);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return result;
	}

	/**
	 * 查询角色授权
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryRolePermission", method = RequestMethod.GET)
	public Result<List<String>> queryRolePermission(@RequestParam(name = "roleId", required = true) String roleId) {
		Result<List<String>> result = new Result<>();
		try {
			List<SysRolePermission> list = sysRolePermissionService.list(new QueryWrapper<SysRolePermission>().lambda().eq(SysRolePermission::getRoleId, roleId));
			result.setResult(list.stream().map(SysRolePermission -> String.valueOf(SysRolePermission.getPermissionId())).collect(Collectors.toList()));
			result.setSuccess(true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 保存角色授权
	 * 
	 * @return
	 */
	@RequestMapping(value = "/saveRolePermission", method = RequestMethod.POST)
	@RequiresRoles({ "admin" })
	public Result<String> saveRolePermission(@RequestBody JSONObject json) {
		long start = System.currentTimeMillis();
		Result<String> result = new Result<>();
		try {
			String roleId = json.getString("roleId");
			String permissionIds = json.getString("permissionIds");
			String lastPermissionIds = json.getString("lastpermissionIds");
			this.sysRolePermissionService.saveRolePermission(roleId, permissionIds, lastPermissionIds);
			result.success("保存成功！");
			log.info("======角色授权成功=====耗时:" + (System.currentTimeMillis() - start) + "毫秒");
		} catch (Exception e) {
			result.error500("授权失败！");
			log.error(e.getMessage(), e);
		}
		return result;
	}

	private void getTreeList(List<SysPermissionTree> treeList, List<SysPermission> metaList, SysPermissionTree temp) {
		for (SysPermission permission : metaList) {
			String tempPid = permission.getParentId();
			SysPermissionTree tree = new SysPermissionTree(permission);
			if (temp == null && oConvertUtils.isEmpty(tempPid)) {
				treeList.add(tree);
				if (!tree.getIsLeaf()) {
					getTreeList(treeList, metaList, tree);
				}
			} else if (temp != null && tempPid != null && tempPid.equals(temp.getId())) {
				temp.getChildren().add(tree);
				if (!tree.getIsLeaf()) {
					getTreeList(treeList, metaList, tree);
				}
			}

		}
	}

	private void getTreeModelList(List<TreeModel> treeList, List<SysPermission> metaList, TreeModel temp) {
		for (SysPermission permission : metaList) {
			String tempPid = permission.getParentId();
			TreeModel tree = new TreeModel(permission);
			if (temp == null && oConvertUtils.isEmpty(tempPid)) {
				treeList.add(tree);
				if (!tree.getIsLeaf()) {
					getTreeModelList(treeList, metaList, tree);
				}
			} else if (temp != null && tempPid != null && tempPid.equals(temp.getKey())) {
				temp.getChildren().add(tree);
				if (!tree.getIsLeaf()) {
					getTreeModelList(treeList, metaList, tree);
				}
			}

		}
	}
	
	/**
	  *  获取权限JSON数组
	 * @param jsonArray
	 * @param metaList
	 * @param parentJson
	 */
	private void getAllAuthJsonArray(JSONArray jsonArray,List<SysPermission> allList) {
		JSONObject json = null;
		for (SysPermission permission : allList) {
			json = new JSONObject();
			json.put("action", permission.getPerms());
			json.put("status", permission.getStatus());
			json.put("type", permission.getPermsType());
			json.put("describe", permission.getName());
			jsonArray.add(json);
		}
	}

	/**
	  *  获取权限JSON数组
	 * @param jsonArray
	 * @param metaList
	 * @param parentJson
	 */
	private void getAuthJsonArray(JSONArray jsonArray,List<SysPermission> metaList) {
		for (SysPermission permission : metaList) {
			if(permission.getMenuType()==null) {
				continue;
			}
			JSONObject json = null;
			if(permission.getMenuType().equals(CommonConstant.MENU_TYPE_2) &&CommonConstant.STATUS_1.equals(permission.getStatus())) {
				json = new JSONObject();
				json.put("action", permission.getPerms());
				json.put("type", permission.getPermsType());
				json.put("describe", permission.getName());
				jsonArray.add(json);
			}
		}
	}
	/**
	  *  获取菜单JSON数组
	 * @param jsonArray
	 * @param metaList
	 * @param parentJson
	 */
	private void getPermissionJsonArray(JSONArray jsonArray, List<SysPermission> metaList, JSONObject parentJson) {
		for (SysPermission permission : metaList) {
			if (permission.getMenuType() == null) {
				continue;
			}
			String tempPid = permission.getParentId();
			JSONObject json = getPermissionJsonObject(permission);
			if(json==null) {
				continue;
			}
			if (parentJson == null && oConvertUtils.isEmpty(tempPid)) {
				jsonArray.add(json);
				if (!permission.isLeaf()) {
					getPermissionJsonArray(jsonArray, metaList, json);
				}
			} else if (parentJson != null && oConvertUtils.isNotEmpty(tempPid) && tempPid.equals(parentJson.getString("id"))) {
				// 类型( 0：一级菜单 1：子菜单 2：按钮 )
				if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_2)) {
					JSONObject metaJson = parentJson.getJSONObject("meta");
					if (metaJson.containsKey("permissionList")) {
						metaJson.getJSONArray("permissionList").add(json);
					} else {
						JSONArray permissionList = new JSONArray();
						permissionList.add(json);
						metaJson.put("permissionList", permissionList);
					}
					// 类型( 0：一级菜单 1：子菜单 2：按钮 )
				} else if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_1) || permission.getMenuType().equals(CommonConstant.MENU_TYPE_0)) {
					if (parentJson.containsKey("children")) {
						parentJson.getJSONArray("children").add(json);
					} else {
						JSONArray children = new JSONArray();
						children.add(json);
						parentJson.put("children", children);
					}

					if (!permission.isLeaf()) {
						getPermissionJsonArray(jsonArray, metaList, json);
					}
				}
			}

		}
	}

	private JSONObject getPermissionJsonObject(SysPermission permission) {
		JSONObject json = new JSONObject();
		// 类型(0：一级菜单 1：子菜单 2：按钮)
		if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_2)) {
			//json.put("action", permission.getPerms());
			//json.put("type", permission.getPermsType());
			//json.put("describe", permission.getName());
			return null;
		} else if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_0) || permission.getMenuType().equals(CommonConstant.MENU_TYPE_1)) {
			json.put("id", permission.getId());
			if (permission.isRoute()) {
				json.put("route", "1");// 表示生成路由
			} else {
				json.put("route", "0");// 表示不生成路由
			}

			if (isWWWHttpUrl(permission.getUrl())) {
				json.put("path", MD5Util.MD5Encode(permission.getUrl(), "utf-8"));
			} else {
				json.put("path", permission.getUrl());
			}

			// 重要规则：路由name (通过URL生成路由name,路由name供前端开发，页面跳转使用)
			if (oConvertUtils.isNotEmpty(permission.getComponentName())) {
				json.put("name", permission.getComponentName());
			} else {
				json.put("name", urlToRouteName(permission.getUrl()));
			}

			// 是否隐藏路由，默认都是显示的
			if (permission.isHidden()) {
				json.put("hidden", true);
			}
			// 聚合路由
			if (permission.isAlwaysShow()) {
				json.put("alwaysShow", true);
			}
			json.put("component", permission.getComponent());
			JSONObject meta = new JSONObject();
			// 由用户设置是否缓存页面 用布尔值
			if (permission.isKeepAlive()) {
				meta.put("keepAlive", true);
			} else {
				meta.put("keepAlive", false);
			}

			meta.put("title", permission.getName());
			if (oConvertUtils.isEmpty(permission.getParentId())) {
				// 一级菜单跳转地址
				json.put("redirect", permission.getRedirect());
				if (oConvertUtils.isNotEmpty(permission.getIcon())) {
					meta.put("icon", permission.getIcon());
				}
			} else {
				if (oConvertUtils.isNotEmpty(permission.getIcon())) {
					meta.put("icon", permission.getIcon());
				}
			}
			if (isWWWHttpUrl(permission.getUrl())) {
				meta.put("url", permission.getUrl());
			}
			json.put("meta", meta);
		}

		return json;
	}

	/**
	 * 判断是否外网URL 例如： http://localhost:8080/jeecg-boot/swagger-ui.html#/ 支持特殊格式： {{
	 * window._CONFIG['domianURL'] }}/druid/ {{ JS代码片段 }}，前台解析会自动执行JS代码片段
	 * 
	 * @return
	 */
	private boolean isWWWHttpUrl(String url) {
		if (url != null && (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("{{"))) {
			return true;
		}
		return false;
	}

	/**
	 * 通过URL生成路由name（去掉URL前缀斜杠，替换内容中的斜杠‘/’为-） 举例： URL = /isystem/role RouteName =
	 * isystem-role
	 * 
	 * @return
	 */
	private String urlToRouteName(String url) {
		if (oConvertUtils.isNotEmpty(url)) {
			if (url.startsWith("/")) {
				url = url.substring(1);
			}
			url = url.replace("/", "-");

			// 特殊标记
			url = url.replace(":", "@");
			return url;
		} else {
			return null;
		}
	}

	/**
	 * 根据菜单id来获取其对应的权限数据
	 * 
	 * @param sysPermissionDataRule
	 * @return
	 */
	@RequestMapping(value = "/getPermRuleListByPermId", method = RequestMethod.GET)
	public Result<List<SysPermissionDataRule>> getPermRuleListByPermId(SysPermissionDataRule sysPermissionDataRule) {
		List<SysPermissionDataRule> permRuleList = sysPermissionDataRuleService.getPermRuleListByPermId(sysPermissionDataRule.getPermissionId());
		Result<List<SysPermissionDataRule>> result = new Result<>();
		result.setSuccess(true);
		result.setResult(permRuleList);
		return result;
	}

	/**
	 * 添加菜单权限数据
	 * 
	 * @param sysPermissionDataRule
	 * @return
	 */
	@RequestMapping(value = "/addPermissionRule", method = RequestMethod.POST)
	public Result<SysPermissionDataRule> addPermissionRule(@RequestBody SysPermissionDataRule sysPermissionDataRule) {
		Result<SysPermissionDataRule> result = new Result<SysPermissionDataRule>();
		try {
			sysPermissionDataRule.setCreateTime(new Date());
			sysPermissionDataRuleService.savePermissionDataRule(sysPermissionDataRule);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

	@RequestMapping(value = "/editPermissionRule", method = { RequestMethod.PUT, RequestMethod.POST })
	public Result<SysPermissionDataRule> editPermissionRule(@RequestBody SysPermissionDataRule sysPermissionDataRule) {
		Result<SysPermissionDataRule> result = new Result<SysPermissionDataRule>();
		try {
			sysPermissionDataRuleService.saveOrUpdate(sysPermissionDataRule);
			result.success("更新成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	 * 删除菜单权限数据
	 * 
	 * @param sysPermissionDataRule
	 * @return
	 */
	@RequestMapping(value = "/deletePermissionRule", method = RequestMethod.DELETE)
	public Result<SysPermissionDataRule> deletePermissionRule(@RequestParam(name = "id", required = true) String id) {
		Result<SysPermissionDataRule> result = new Result<SysPermissionDataRule>();
		try {
			sysPermissionDataRuleService.deletePermissionDataRule(id);
			result.success("删除成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	 * 查询菜单权限数据
	 * 
	 * @param sysPermissionDataRule
	 * @return
	 */
	@RequestMapping(value = "/queryPermissionRule", method = RequestMethod.GET)
	public Result<List<SysPermissionDataRule>> queryPermissionRule(SysPermissionDataRule sysPermissionDataRule) {
		Result<List<SysPermissionDataRule>> result = new Result<>();
		try {
			List<SysPermissionDataRule> permRuleList = sysPermissionDataRuleService.queryPermissionRule(sysPermissionDataRule);
			result.setResult(permRuleList);
			result.success("查询成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

}
