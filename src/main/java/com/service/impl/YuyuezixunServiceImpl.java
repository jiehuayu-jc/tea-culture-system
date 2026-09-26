package com.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.YuyuezixunDao;
import com.entity.YuyuezixunEntity;
import com.entity.view.YuyuezixunView;
import com.entity.vo.YuyuezixunVO;
import com.service.YuyuezixunService;
import com.utils.PageUtils;
import com.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("yuyuezixunService")
public class YuyuezixunServiceImpl extends ServiceImpl<YuyuezixunDao, YuyuezixunEntity> implements YuyuezixunService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<YuyuezixunEntity> page = this.selectPage(
                new Query<YuyuezixunEntity>(params).getPage(),
                new EntityWrapper<YuyuezixunEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<YuyuezixunEntity> wrapper) {
		  Page<YuyuezixunView> page =new Query<YuyuezixunView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}

    
    @Override
	public List<YuyuezixunVO> selectListVO(Wrapper<YuyuezixunEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public YuyuezixunVO selectVO(Wrapper<YuyuezixunEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<YuyuezixunView> selectListView(Wrapper<YuyuezixunEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public YuyuezixunView selectView(Wrapper<YuyuezixunEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}


}
