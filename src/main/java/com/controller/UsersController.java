
package com.controller;


import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.TokenEntity;
import com.entity.UsersEntity;
import com.service.TokenService;
import com.service.UsersService;
import com.utils.CommonUtil;
import com.utils.EncryptUtil;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.ValidatorUtils;

/**
 * 登录相关
 */
@RequestMapping("users")
@RestController
public class UsersController{
	
	@Autowired
	private UsersService userService;
	
	@Autowired
	private TokenService tokenService;

	/**
	 * 登录
	 */
	@IgnoreAuth
	@RequestMapping(value = "/login")
	public R login(String username, String password, String captcha, HttpServletRequest request) {
		try {
			if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
				return R.error("账号或密码不能为空");
			}
			UsersEntity user = userService.selectOne(new EntityWrapper<UsersEntity>().eq("username", username.trim()));
			// 兼容历史明文/MD5，新密码一律 BCrypt
			boolean ok = user != null && (EncryptUtil.matches(password, user.getPassword())
					|| StringUtils.equals(password, user.getPassword()));
			if (!ok) {
				return R.error("账号或密码不正确");
			}
			// 旧明文/MD5 密码在登录成功后透明升级为 BCrypt
			if (user.getPassword() == null || !user.getPassword().startsWith("$2")) {
				UsersEntity up = new UsersEntity();
				up.setId(user.getId());
				up.setPassword(EncryptUtil.bcrypt(password));
				userService.updateById(up);
			}
			if (user.getId() == null) {
				return R.error("用户数据异常，请检查 users 表");
			}
			String role = StringUtils.isNotBlank(user.getRole()) ? user.getRole() : "管理员";
			String token = tokenService.generateToken(user.getId(), username.trim(), "users", role);
			return R.ok().put("token", token);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("登录失败，请检查数据库 users、token 表是否已按脚本导入");
		}
	}
	
	/**
	 * 注册
	 */
	@IgnoreAuth
	@PostMapping(value = "/register")
	public R register(@RequestBody UsersEntity user){
//    	ValidatorUtils.validateEntity(user);
    	if(userService.selectOne(new EntityWrapper<UsersEntity>().eq("username", user.getUsername())) !=null) {
    		return R.error("用户已存在");
    	}
        userService.insert(user);
        return R.ok();
    }

	/**
	 * 退出
	 */
	@GetMapping(value = "logout")
	public R logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return R.ok("退出成功");
	}
	
	/**
     * 密码重置
     */
    @IgnoreAuth
	@RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request){
    	UsersEntity user = userService.selectOne(new EntityWrapper<UsersEntity>().eq("username", username));
    	if(user==null) {
    		return R.error("账号不存在");
    	}
	    	user.setPassword(EncryptUtil.bcrypt("123456"));
	        userService.update(user,null);
	        return R.ok("密码已重置为：123456");
    }
	
	/**
     * 列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,UsersEntity user){
        EntityWrapper<UsersEntity> ew = new EntityWrapper<UsersEntity>();
    	PageUtils page = userService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.allLike(ew, user), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/list")
    public R list( UsersEntity user){
       	EntityWrapper<UsersEntity> ew = new EntityWrapper<UsersEntity>();
      	ew.allEq(MPUtil.allEQMapPre( user, "user")); 
        return R.ok().put("data", userService.selectListView(ew));
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
        UsersEntity user = userService.selectById(id);
        return R.ok().put("data", user);
    }
    
    /**
     * 获取用户的session用户信息
     */
    @RequestMapping("/session")
    public R getCurrUser(HttpServletRequest request){
    	Long id = (Long)request.getSession().getAttribute("userId");
        UsersEntity user = userService.selectById(id);
        return R.ok().put("data", user);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody UsersEntity user){
//    	ValidatorUtils.validateEntity(user);
    	if(userService.selectOne(new EntityWrapper<UsersEntity>().eq("username", user.getUsername())) !=null) {
    		return R.error("用户已存在");
    	}
    	if(StringUtils.isNotBlank(user.getPassword())) {
    		user.setPassword(EncryptUtil.bcrypt(user.getPassword()));
    	}
        userService.insert(user);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody UsersEntity user){
//        ValidatorUtils.validateEntity(user);
    	UsersEntity u = userService.selectOne(new EntityWrapper<UsersEntity>().eq("username", user.getUsername()));
    	if(u!=null && u.getId()!=user.getId() && u.getUsername().equals(user.getUsername())) {
    		return R.error("用户名已存在。");
    	}
    	// 密码有变化时以 BCrypt 落库；与库中一致则视为未改密，原样跳过
    	if(StringUtils.isNotBlank(user.getPassword()) && u!=null && !StringUtils.equals(user.getPassword(), u.getPassword())) {
    		user.setPassword(EncryptUtil.bcrypt(user.getPassword()));
    	} else if (StringUtils.isNotBlank(user.getPassword()) && u==null) {
    		user.setPassword(EncryptUtil.bcrypt(user.getPassword()));
    	}
        userService.updateById(user);//全部更新
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        userService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}
