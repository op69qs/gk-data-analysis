package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.enumSetting.util.PageData;
import org.jeecg.modules.system.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
	List<Map<String, Object>> getUserList(@Param("params") Map pd) ;
	int getUserListCount(@Param("params") Map pd) ;
	SysUser getUserById(@Param("id") String id) ;
	void addUserGuoku(@Param("params") Map params);
	void DelUserGuoku(@Param("params") Map params);
	/**
	  * 通过用户账号查询用户信息
	 * @param username
	 * @return
	 */
	public SysUser getUserByName(@Param("username") String username);

	/**
	 *  根据部门Id查询用户信息
	 * @param page
	 * @param departId
	 * @return
	 */
	IPage<SysUser> getUserByDepId(Page page, @Param("departId") String departId, @Param("username") String username);

	/**
	 * 根据角色Id查询用户信息
	 * @param page
	 * @param
	 * @return
	 */
	IPage<SysUser> getUserByRoleId(Page page, @Param("roleId") String roleId, @Param("username") String username);
	
	/**
	 * 根据用户名设置部门ID
	 * @param username
	 * @param departId
	 */
	void updateUserDepart(@Param("username") String username,@Param("orgCode") String orgCode);
	
	/**
	 * 根据手机号查询用户信息
	 * @param phone
	 * @return
	 */
	public SysUser getUserByPhone(@Param("phone") String phone);
	
	
	/**
	 * 根据邮箱查询用户信息
	 * @param email
	 * @return
	 */
	public SysUser getUserByEmail(@Param("email")String email);

	/**
	 * 同步更新时间
	 * @param login_id
	 * @return
	 */
	void updateUserUpdateTime(@Param("login_id")String login_id);

}
