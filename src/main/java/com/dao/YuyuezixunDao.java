package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.entity.YuyuezixunEntity;
import com.entity.view.YuyuezixunView;
import com.entity.vo.YuyuezixunVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 预约咨询
 * 
 * @author 
 * @email 
 * @date 2024-12-26 16:14:56
 */
public interface YuyuezixunDao extends BaseMapper<YuyuezixunEntity> {
	
	List<YuyuezixunVO> selectListVO(@Param("ew") Wrapper<YuyuezixunEntity> wrapper);
	
	YuyuezixunVO selectVO(@Param("ew") Wrapper<YuyuezixunEntity> wrapper);
	
	List<YuyuezixunView> selectListView(@Param("ew") Wrapper<YuyuezixunEntity> wrapper);

	List<YuyuezixunView> selectListView(Pagination page,@Param("ew") Wrapper<YuyuezixunEntity> wrapper);

	
	YuyuezixunView selectView(@Param("ew") Wrapper<YuyuezixunEntity> wrapper);
	

}
