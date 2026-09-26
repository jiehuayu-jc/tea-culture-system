package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.JiaoxueshipinEntity;
import com.entity.view.JiaoxueshipinView;
import com.entity.vo.JiaoxueshipinVO;
import com.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 
 *
 * @author 
 * @email 
 * @date 2024-03-05 11:41:23
 */
public interface JiaoxueshipinService extends IService<JiaoxueshipinEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<JiaoxueshipinVO> selectListVO(Wrapper<JiaoxueshipinEntity> wrapper);
   	
   	JiaoxueshipinVO selectVO(@Param("ew") Wrapper<JiaoxueshipinEntity> wrapper);
   	
   	List<JiaoxueshipinView> selectListView(Wrapper<JiaoxueshipinEntity> wrapper);
   	
   	JiaoxueshipinView selectView(@Param("ew") Wrapper<JiaoxueshipinEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<JiaoxueshipinEntity> wrapper);

   	

}

