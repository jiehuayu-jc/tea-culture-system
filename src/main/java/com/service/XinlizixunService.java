package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.XinlizixunEntity;
import com.entity.view.XinlizixunView;
import com.entity.vo.XinlizixunVO;
import com.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 心理咨询
 *
 * @author 
 * @email 
 * @date 2024-12-26 16:14:55
 */
public interface XinlizixunService extends IService<XinlizixunEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<XinlizixunVO> selectListVO(Wrapper<XinlizixunEntity> wrapper);
   	
   	XinlizixunVO selectVO(@Param("ew") Wrapper<XinlizixunEntity> wrapper);
   	
   	List<XinlizixunView> selectListView(Wrapper<XinlizixunEntity> wrapper);
   	
   	XinlizixunView selectView(@Param("ew") Wrapper<XinlizixunEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<XinlizixunEntity> wrapper);

   	

    List<Map<String, Object>> selectValue(Map<String, Object> params,Wrapper<XinlizixunEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params,Wrapper<XinlizixunEntity> wrapper);

    List<Map<String, Object>> selectGroup(Map<String, Object> params,Wrapper<XinlizixunEntity> wrapper);



}

