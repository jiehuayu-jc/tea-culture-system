package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.entity.XinlizixunEntity;
import com.entity.view.XinlizixunView;
import com.entity.vo.XinlizixunVO;
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
public interface XinlizixunDao extends BaseMapper<XinlizixunEntity> {
	
	List<XinlizixunVO> selectListVO(@Param("ew") Wrapper<XinlizixunEntity> wrapper);
	
	XinlizixunVO selectVO(@Param("ew") Wrapper<XinlizixunEntity> wrapper);
	
	List<XinlizixunView> selectListView(@Param("ew") Wrapper<XinlizixunEntity> wrapper);

	List<XinlizixunView> selectListView(Pagination page,@Param("ew") Wrapper<XinlizixunEntity> wrapper);

	
	XinlizixunView selectView(@Param("ew") Wrapper<XinlizixunEntity> wrapper);
	

    List<Map<String, Object>> selectValue(@Param("params") Map<String, Object> params,@Param("ew") Wrapper<XinlizixunEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(@Param("params") Map<String, Object> params,@Param("ew") Wrapper<XinlizixunEntity> wrapper);

    List<Map<String, Object>> selectGroup(@Param("params") Map<String, Object> params,@Param("ew") Wrapper<XinlizixunEntity> wrapper);



}
