package com.controller;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.YuyuezixunEntity;
import com.entity.view.YuyuezixunView;
import com.service.YuyuezixunService;
import com.utils.DeSensUtil;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 预约咨询
 * 后端接口
 * @author 
 * @email 
 * @date 2024-12-26 16:14:56
 */
@RestController
@RequestMapping("/yuyuezixun")
public class YuyuezixunController {
    @Autowired
    private YuyuezixunService yuyuezixunService;




    



    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,YuyuezixunEntity yuyuezixun,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("yonghu")) {
			yuyuezixun.setYonghuzhanghao((String)request.getSession().getAttribute("username"));
		}
		if(tableName.equals("xinlizixunshi")) {
			yuyuezixun.setZixunshizhanghao((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<YuyuezixunEntity> ew = new EntityWrapper<YuyuezixunEntity>();



		PageUtils page = yuyuezixunService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yuyuezixun), params), params));
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }
    
    /**
     * 前台列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,YuyuezixunEntity yuyuezixun, 
		HttpServletRequest request){
        EntityWrapper<YuyuezixunEntity> ew = new EntityWrapper<YuyuezixunEntity>();
        String tableName = request.getSession().getAttribute("tableName").toString();

        if(tableName.equals("yonghu")) {
            yuyuezixun.setYonghuzhanghao((String)request.getSession().getAttribute("username"));
        }
		PageUtils page = yuyuezixunService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yuyuezixun), params), params));
		
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }



	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( YuyuezixunEntity yuyuezixun){
       	EntityWrapper<YuyuezixunEntity> ew = new EntityWrapper<YuyuezixunEntity>();
      	ew.allEq(MPUtil.allEQMapPre( yuyuezixun, "yuyuezixun")); 
        return R.ok().put("data", yuyuezixunService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(YuyuezixunEntity yuyuezixun){
        EntityWrapper< YuyuezixunEntity> ew = new EntityWrapper< YuyuezixunEntity>();
 		ew.allEq(MPUtil.allEQMapPre( yuyuezixun, "yuyuezixun")); 
		YuyuezixunView yuyuezixunView =  yuyuezixunService.selectView(ew);
		return R.ok("查询预约咨询成功").put("data", yuyuezixunView);
    }
	
    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        YuyuezixunEntity yuyuezixun = yuyuezixunService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(yuyuezixun,deSens);
        return R.ok().put("data", yuyuezixun);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        YuyuezixunEntity yuyuezixun = yuyuezixunService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(yuyuezixun,deSens);
        return R.ok().put("data", yuyuezixun);
    }
    



    /**
     * 后台保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody YuyuezixunEntity yuyuezixun, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(yuyuezixun);
        yuyuezixunService.insert(yuyuezixun);
        return R.ok();
    }
    
    /**
     * 前台保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody YuyuezixunEntity yuyuezixun, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(yuyuezixun);
        yuyuezixunService.insert(yuyuezixun);
        return R.ok().put("data",yuyuezixun.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody YuyuezixunEntity yuyuezixun, HttpServletRequest request){
        //ValidatorUtils.validateEntity(yuyuezixun);
        //全部更新
        yuyuezixunService.updateById(yuyuezixun);

        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        yuyuezixunService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	











}
