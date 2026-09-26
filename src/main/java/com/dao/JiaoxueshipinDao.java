package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.entity.JiaoxueshipinEntity;
import com.entity.view.JiaoxueshipinView;
import com.entity.vo.JiaoxueshipinVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 
 * 
 * @author 
 * @email 
 * @date 2024-03-05 11:41:23
 */
public interface JiaoxueshipinDao extends BaseMapper<JiaoxueshipinEntity> {
	
	List<JiaoxueshipinVO> selectListVO(@Param("ew") Wrapper<JiaoxueshipinEntity> wrapper);
	
	JiaoxueshipinVO selectVO(@Param("ew") Wrapper<JiaoxueshipinEntity> wrapper);
	
	List<JiaoxueshipinView> selectListView(@Param("ew") Wrapper<JiaoxueshipinEntity> wrapper);

	List<JiaoxueshipinView> selectListView(Pagination page,@Param("ew") Wrapper<JiaoxueshipinEntity> wrapper);

	
	JiaoxueshipinView selectView(@Param("ew") Wrapper<JiaoxueshipinEntity> wrapper);
	

}
