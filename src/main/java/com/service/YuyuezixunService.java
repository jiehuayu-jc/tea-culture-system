package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.YuyuezixunEntity;
import com.entity.view.YuyuezixunView;
import com.entity.vo.YuyuezixunVO;
import com.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 预约咨询
 *
 * @author 
 * @email 
 * @date 2024-12-26 16:14:56
 */
public interface YuyuezixunService extends IService<YuyuezixunEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<YuyuezixunVO> selectListVO(Wrapper<YuyuezixunEntity> wrapper);
   	
   	YuyuezixunVO selectVO(@Param("ew") Wrapper<YuyuezixunEntity> wrapper);
   	
   	List<YuyuezixunView> selectListView(Wrapper<YuyuezixunEntity> wrapper);
   	
   	YuyuezixunView selectView(@Param("ew") Wrapper<YuyuezixunEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<YuyuezixunEntity> wrapper);

   	

}

