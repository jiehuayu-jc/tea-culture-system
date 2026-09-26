package com.controller;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.XinlizixunEntity;
import com.entity.view.XinlizixunView;
import com.service.XinlizixunService;
import com.utils.DeSensUtil;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 心理咨询
 * 后端接口
 * @author 
 * @email 
 * @date 2024-12-26 16:14:55
 */
@RestController
@RequestMapping("/xinlizixun")
public class XinlizixunController {
    @Autowired
    private XinlizixunService xinlizixunService;




    



    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,XinlizixunEntity xinlizixun,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("xinlizixunshi")) {
			xinlizixun.setZixunshizhanghao((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<XinlizixunEntity> ew = new EntityWrapper<XinlizixunEntity>();



		PageUtils page = xinlizixunService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, xinlizixun), params), params));
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }
    
    /**
     * 前台列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,XinlizixunEntity xinlizixun, 
		HttpServletRequest request){
        EntityWrapper<XinlizixunEntity> ew = new EntityWrapper<XinlizixunEntity>();

		PageUtils page = xinlizixunService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, xinlizixun), params), params));
		
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }



	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( XinlizixunEntity xinlizixun){
       	EntityWrapper<XinlizixunEntity> ew = new EntityWrapper<XinlizixunEntity>();
      	ew.allEq(MPUtil.allEQMapPre( xinlizixun, "xinlizixun")); 
        return R.ok().put("data", xinlizixunService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(XinlizixunEntity xinlizixun){
        EntityWrapper< XinlizixunEntity> ew = new EntityWrapper< XinlizixunEntity>();
 		ew.allEq(MPUtil.allEQMapPre( xinlizixun, "xinlizixun")); 
		XinlizixunView xinlizixunView =  xinlizixunService.selectView(ew);
		return R.ok("查询心理咨询成功").put("data", xinlizixunView);
    }
	
    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        XinlizixunEntity xinlizixun = xinlizixunService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(xinlizixun,deSens);
        return R.ok().put("data", xinlizixun);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        XinlizixunEntity xinlizixun = xinlizixunService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(xinlizixun,deSens);
        return R.ok().put("data", xinlizixun);
    }
    



    /**
     * 后台保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody XinlizixunEntity xinlizixun, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(xinlizixun);
        xinlizixunService.insert(xinlizixun);
        return R.ok();
    }
    
    /**
     * 前台保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody XinlizixunEntity xinlizixun, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(xinlizixun);
        xinlizixunService.insert(xinlizixun);
        return R.ok().put("data",xinlizixun.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody XinlizixunEntity xinlizixun, HttpServletRequest request){
        //ValidatorUtils.validateEntity(xinlizixun);
        //全部更新
        xinlizixunService.updateById(xinlizixun);

        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        xinlizixunService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	








        /**
     * （按值统计）
     */
    @RequestMapping("/value/{xColumnName}/{yColumnName}")
    public R value(@PathVariable("yColumnName") String yColumnName, @PathVariable("xColumnName") String xColumnName,HttpServletRequest request) throws IOException {
        java.nio.file.Path path = java.nio.file.Paths.get("value_xinlizixun_" + xColumnName + "_" + yColumnName + "_timeType.json");
        if(java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }else{
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        params.put("yColumn", yColumnName);
        EntityWrapper<XinlizixunEntity> ew = new EntityWrapper<XinlizixunEntity>();
        String tableName = request.getSession().getAttribute("tableName").toString();
                                        if(tableName.equals("xinlizixunshi")) {
            ew.eq("zixunshizhanghao", (String)request.getSession().getAttribute("username"));
        }
                    List<Map<String, Object>> result = xinlizixunService.selectValue(params, ew);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map<String, Object> m : result) {
            for(String k : m.keySet()) {
                if(m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date)m.get(k)));
                }
            }
        }
        Collections.sort(result, (map1, map2) -> {
            // 假设 total 总是存在并且是数值类型
            Number total1 = (Number) map1.get("total");
            Number total2 = (Number) map2.get("total");
            return Double.compare(total2.doubleValue(), total1.doubleValue());
        });
        return R.ok().put("data", result);
        }
    }
    
    /**
     * （按值统计(多)）
     */
    @RequestMapping("/valueMul/{xColumnName}")
    public R valueMul(@PathVariable("xColumnName") String xColumnName,@RequestParam String yColumnNameMul,HttpServletRequest request)  throws IOException {
        java.nio.file.Path path = java.nio.file.Paths.get("value_xinlizixun_" + xColumnName + "_" + yColumnNameMul + "_timeType.json");
        if(java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }else{
        String[] yColumnNames = yColumnNameMul.split(",");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        List<List<Map<String, Object>>> result2 = new ArrayList<List<Map<String,Object>>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        EntityWrapper<XinlizixunEntity> ew = new EntityWrapper<XinlizixunEntity>();
String tableName = request.getSession().getAttribute("tableName").toString();
                                        if(tableName.equals("xinlizixunshi")) {
            ew.eq("zixunshizhanghao", (String)request.getSession().getAttribute("username"));
        }
                for(int i=0;i<yColumnNames.length;i++) {
            params.put("yColumn", yColumnNames[i]);
            List<Map<String, Object>> result = xinlizixunService.selectValue(params, ew);
            for(Map<String, Object> m : result) {
                for(String k : m.keySet()) {
                    if(m.get(k) instanceof Date) {
                        m.put(k, sdf.format((Date)m.get(k)));
                    }
                }
            }
            result2.add(result);
        }
        return R.ok().put("data", result2);
    }
}
    
    /**
     * （按值统计）时间统计类型
     */
    @RequestMapping("/value/{xColumnName}/{yColumnName}/{timeStatType}")
    public R valueDay(@PathVariable("yColumnName") String yColumnName, @PathVariable("xColumnName") String xColumnName, @PathVariable("timeStatType") String timeStatType,HttpServletRequest request) throws IOException {
        java.nio.file.Path path = java.nio.file.Paths.get("value_xinlizixun_" + xColumnName + "_" + yColumnName + "_"+timeStatType+".json");
        if(java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }else{
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("xColumn", xColumnName);
            params.put("yColumn", yColumnName);
            params.put("timeStatType", timeStatType);
            EntityWrapper<XinlizixunEntity> ew = new EntityWrapper<XinlizixunEntity>();
    String tableName = request.getSession().getAttribute("tableName").toString();
                                                                                                            if(tableName.equals("xinlizixunshi")) {
                ew.eq("zixunshizhanghao", (String)request.getSession().getAttribute("username"));
            }
                                            List<Map<String, Object>> result = xinlizixunService.selectTimeStatValue(params, ew);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for(Map<String, Object> m : result) {
                for(String k : m.keySet()) {
                    if(m.get(k) instanceof Date) {
                        m.put(k, sdf.format((Date)m.get(k)));
                    }
                }
            }
            return R.ok().put("data", result);
        }
    }
    
        /**
     * （按值统计）时间统计类型(多)
     */
    @RequestMapping("/valueMul/{xColumnName}/{timeStatType}")
    public R valueMulDay(@PathVariable("xColumnName") String xColumnName, @PathVariable("timeStatType") String timeStatType,@RequestParam String yColumnNameMul,HttpServletRequest request) throws IOException
    {
        java.nio.file.Path path = java.nio.file.Paths.get("value_xinlizixun_" + xColumnName + "_" + yColumnNameMul + "_" + timeStatType + ".json");
        if (java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }else{
            String[] yColumnNames = yColumnNameMul.split(",");
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("xColumn", xColumnName);
            params.put("timeStatType", timeStatType);
            List<List<Map<String, Object>>> result2 = new ArrayList<List<Map<String,Object>>>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            EntityWrapper<XinlizixunEntity> ew = new EntityWrapper<XinlizixunEntity>();
    String tableName = request.getSession().getAttribute("tableName").toString();
                                                                                                            if(tableName.equals("xinlizixunshi")) {
                ew.eq("zixunshizhanghao", (String)request.getSession().getAttribute("username"));
            }
                                    for(int i=0;i<yColumnNames.length;i++) {
                params.put("yColumn", yColumnNames[i]);
                List<Map<String, Object>> result = xinlizixunService.selectTimeStatValue(params, ew);
                for(Map<String, Object> m : result) {
                    for(String k : m.keySet()) {
                        if(m.get(k) instanceof Date) {
                            m.put(k, sdf.format((Date)m.get(k)));
                        }
                    }
                }
                result2.add(result);
            }
            return R.ok().put("data", result2);
        }
    }
    
        /**
     * 分组统计
     */
    @RequestMapping("/group/{columnName}")
    public R group(@PathVariable("columnName") String columnName,HttpServletRequest request) throws IOException {
        java.nio.file.Path path = java.nio.file.Paths.get("group_xinlizixun_" + columnName + "_timeType.json");
        if(java.nio.file.Files.exists(path)){
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }else{
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("column", columnName);
        EntityWrapper<XinlizixunEntity> ew = new EntityWrapper<XinlizixunEntity>();
String tableName = request.getSession().getAttribute("tableName").toString();
                                        if(tableName.equals("xinlizixunshi")) {
            ew.eq("zixunshizhanghao", (String)request.getSession().getAttribute("username"));
        }
                    List<Map<String, Object>> result = xinlizixunService.selectGroup(params, ew);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map<String, Object> m : result) {
            for(String k : m.keySet()) {
                if(m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date)m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
        }
    }    
    
    




    /**
     * 总数量
     */
    @RequestMapping("/count")
    public R count(@RequestParam Map<String, Object> params,XinlizixunEntity xinlizixun, HttpServletRequest request){
        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("xinlizixunshi")) {
            xinlizixun.setZixunshizhanghao((String)request.getSession().getAttribute("username"));
        }
        EntityWrapper<XinlizixunEntity> ew = new EntityWrapper<XinlizixunEntity>();
        int count = xinlizixunService.selectCount(MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, xinlizixun), params), params));
        return R.ok().put("data", count);
    }



}
