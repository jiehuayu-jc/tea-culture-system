package com.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.XinlizixunDao;
import com.entity.XinlizixunEntity;
import com.entity.view.XinlizixunView;
import com.entity.vo.XinlizixunVO;
import com.service.XinlizixunService;
import com.utils.PageUtils;
import com.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("xinlizixunService")
public class XinlizixunServiceImpl extends ServiceImpl<XinlizixunDao, XinlizixunEntity> implements XinlizixunService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<XinlizixunEntity> page = this.selectPage(
                new Query<XinlizixunEntity>(params).getPage(),
                new EntityWrapper<XinlizixunEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<XinlizixunEntity> wrapper) {
		  Page<XinlizixunView> page =new Query<XinlizixunView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}

    
    @Override
	public List<XinlizixunVO> selectListVO(Wrapper<XinlizixunEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public XinlizixunVO selectVO(Wrapper<XinlizixunEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<XinlizixunView> selectListView(Wrapper<XinlizixunEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public XinlizixunView selectView(Wrapper<XinlizixunEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}

    @Override
    public List<Map<String, Object>> selectValue(Map<String, Object> params, Wrapper<XinlizixunEntity> wrapper) {
        return baseMapper.selectValue(params, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params, Wrapper<XinlizixunEntity> wrapper) {
        return baseMapper.selectTimeStatValue(params, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectGroup(Map<String, Object> params, Wrapper<XinlizixunEntity> wrapper) {
        return baseMapper.selectGroup(params, wrapper);
    }




}
