package com.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import java.util.Collections;

import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import com.entity.TokenEntity;
import com.utils.ValidatorUtils;
import com.utils.DeSensUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;
import com.annotation.SysLog;

import com.entity.ShangjiaEntity;
import com.entity.view.ShangjiaView;

import com.service.ShangjiaService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.EncryptUtil;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 茶商
 * 后端接口
 * @author 
 * @email 
 * @date 2025-01-14 09:07:18
 */
@RestController
@RequestMapping("/shangjia")
public class ShangjiaController {
    @Autowired
    private ShangjiaService shangjiaService;






    
	@Autowired
	private TokenService tokenService;
	
	/**
	 * 登录
	 */
	@IgnoreAuth
	@RequestMapping(value = "/login")
	public R login(String username, String password, String captcha, HttpServletRequest request) {
		ShangjiaEntity u = shangjiaService.selectOne(new EntityWrapper<ShangjiaEntity>().eq("shangjiazhanghao", username));
        if(u==null || !u.getMima().equals(EncryptUtil.md5(password))) {
			return R.error("账号或密码不正确");
		}
        if(!"是".equals(u.getSfsh())) return R.error("账号已锁定，请联系管理员审核。");
		String token = tokenService.generateToken(u.getId(), username,"shangjia",  "茶商" );
		return R.ok().put("token", token);
	}


	
	/**
     * 注册
     */
	@IgnoreAuth
    @RequestMapping("/register")
    public R register(@RequestBody ShangjiaEntity shangjia){
    	//ValidatorUtils.validateEntity(shangjia);
    	if (shangjia == null || StringUtils.isBlank(shangjia.getShangjiazhanghao())) {
    		return R.error("茶商账号不能为空");
    	}
    	if (StringUtils.isBlank(shangjia.getMima())) {
    		return R.error("密码不能为空");
    	}
    	ShangjiaEntity u = shangjiaService.selectOne(new EntityWrapper<ShangjiaEntity>().eq("shangjiazhanghao", shangjia.getShangjiazhanghao().trim()));
		if(u!=null) {
			return R.error("注册用户已存在");
		}
		Long uId = new Date().getTime();
		shangjia.setId(uId);
		shangjia.setShangjiazhanghao(shangjia.getShangjiazhanghao().trim());
		if (shangjia.getMoney() == null) {
			shangjia.setMoney(0.0);
		}
		if (StringUtils.isBlank(shangjia.getSfsh())) {
			shangjia.setSfsh("待审核");
		}
        shangjia.setMima(EncryptUtil.md5(shangjia.getMima()));
        try {
        	shangjiaService.insert(shangjia);
        } catch (Exception e) {
        	e.printStackTrace();
        	return R.error("注册失败，请检查数据库是否已导入、字段是否匹配，或账号是否重复");
        }
        return R.ok("注册成功，账号为待审核状态，需管理员在后台审核通过后才能登录");
    }

	
	/**
	 * 退出
	 */
	@RequestMapping("/logout")
	public R logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return R.ok("退出成功");
	}
	
	/**
     * 获取用户的session用户信息
     */
    @RequestMapping("/session")
    public R getCurrUser(HttpServletRequest request){
    	Long id = (Long)request.getSession().getAttribute("userId");
        ShangjiaEntity u = shangjiaService.selectById(id);
        return R.ok().put("data", u);
    }
    
    /**
     * 密码重置
     */
    @IgnoreAuth
	@RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request){
    	ShangjiaEntity u = shangjiaService.selectOne(new EntityWrapper<ShangjiaEntity>().eq("shangjiazhanghao", username));
    	if(u==null) {
    		return R.error("账号不存在");
    	}
        u.setMima(EncryptUtil.md5("123456"));
        shangjiaService.updateById(u);
        return R.ok("密码已重置为：123456");
    }



    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,ShangjiaEntity shangjia,
		HttpServletRequest request){
        EntityWrapper<ShangjiaEntity> ew = new EntityWrapper<ShangjiaEntity>();



		PageUtils page = shangjiaService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shangjia), params), params));
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }
    
    /**
     * 前台列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,ShangjiaEntity shangjia, 
		HttpServletRequest request){
        EntityWrapper<ShangjiaEntity> ew = new EntityWrapper<ShangjiaEntity>();

		PageUtils page = shangjiaService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shangjia), params), params));
		
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }



	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( ShangjiaEntity shangjia){
       	EntityWrapper<ShangjiaEntity> ew = new EntityWrapper<ShangjiaEntity>();
      	ew.allEq(MPUtil.allEQMapPre( shangjia, "shangjia")); 
        return R.ok().put("data", shangjiaService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ShangjiaEntity shangjia){
        EntityWrapper< ShangjiaEntity> ew = new EntityWrapper< ShangjiaEntity>();
 		ew.allEq(MPUtil.allEQMapPre( shangjia, "shangjia")); 
		ShangjiaView shangjiaView =  shangjiaService.selectView(ew);
		return R.ok("查询茶商成功").put("data", shangjiaView);
    }
	
    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ShangjiaEntity shangjia = shangjiaService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(shangjia,deSens);
        return R.ok().put("data", shangjia);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        ShangjiaEntity shangjia = shangjiaService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(shangjia,deSens);
        return R.ok().put("data", shangjia);
    }
    



    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增茶商") 
    public R save(@RequestBody ShangjiaEntity shangjia, HttpServletRequest request){
        if(shangjiaService.selectCount(new EntityWrapper<ShangjiaEntity>().eq("shangjiazhanghao", shangjia.getShangjiazhanghao()))>0) {
            return R.error("茶商账号已存在");
        }
    	shangjia.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(shangjia);
    	ShangjiaEntity u = shangjiaService.selectOne(new EntityWrapper<ShangjiaEntity>().eq("shangjiazhanghao", shangjia.getShangjiazhanghao()));
		if(u!=null) {
			return R.error("用户已存在");
		}
		shangjia.setId(new Date().getTime());
        shangjia.setMima(EncryptUtil.md5(shangjia.getMima()));
        shangjiaService.insert(shangjia);
        return R.ok();
    }
    
    /**
     * 前台保存
     */
    @SysLog("新增茶商")
    @RequestMapping("/add")
    public R add(@RequestBody ShangjiaEntity shangjia, HttpServletRequest request){
        if(shangjiaService.selectCount(new EntityWrapper<ShangjiaEntity>().eq("shangjiazhanghao", shangjia.getShangjiazhanghao()))>0) {
            return R.error("茶商账号已存在");
        }
    	shangjia.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(shangjia);
    	ShangjiaEntity u = shangjiaService.selectOne(new EntityWrapper<ShangjiaEntity>().eq("shangjiazhanghao", shangjia.getShangjiazhanghao()));
		if(u!=null) {
			return R.error("用户已存在");
		}
		shangjia.setId(new Date().getTime());
        shangjia.setMima(EncryptUtil.md5(shangjia.getMima()));
        shangjiaService.insert(shangjia);
        return R.ok().put("data",shangjia.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改茶商")
    public R update(@RequestBody ShangjiaEntity shangjia, HttpServletRequest request){
        //ValidatorUtils.validateEntity(shangjia);
        if(shangjiaService.selectCount(new EntityWrapper<ShangjiaEntity>().ne("id", shangjia.getId()).eq("shangjiazhanghao", shangjia.getShangjiazhanghao()))>0) {
            return R.error("茶商账号已存在");
        }
	ShangjiaEntity shangjiaEntity = shangjiaService.selectById(shangjia.getId());
        if(StringUtils.isNotBlank(shangjia.getMima()) && !shangjia.getMima().equals(shangjiaEntity.getMima())) {
            shangjia.setMima(EncryptUtil.md5(shangjia.getMima()));
        }
        //全部更新
        shangjiaService.updateById(shangjia);
    if(null!=shangjia.getShangjiazhanghao())
    {
        // 修改token
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUsername(shangjia.getShangjiazhanghao());
        tokenService.update(tokenEntity, new EntityWrapper<TokenEntity>().eq("userid", shangjia.getId()));
    }


        return R.ok();
    }

    /**
     * 审核
     */
    @RequestMapping("/shBatch")
    @Transactional
    @SysLog("审核茶商")
    public R update(@RequestBody Long[] ids, @RequestParam String sfsh, @RequestParam String shhf){
        List<ShangjiaEntity> list = new ArrayList<ShangjiaEntity>();
        for(Long id : ids) {
            ShangjiaEntity shangjia = shangjiaService.selectById(id);
            shangjia.setSfsh(sfsh);
            shangjia.setShhf(shhf);
            list.add(shangjia);
        }
        shangjiaService.updateBatchById(list);
        return R.ok();
    }


    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除茶商")
    public R delete(@RequestBody Long[] ids){
        shangjiaService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	












}
