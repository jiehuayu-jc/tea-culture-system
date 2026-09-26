package com.entity.view;

import com.baomidou.mybatisplus.annotations.TableName;
import com.entity.JiaoxueshipinEntity;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;


/**
 * 
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author 
 * @email 
 * @date 2024-03-05 11:41:23
 */
@TableName("jiaoxueshipin")
public class JiaoxueshipinView extends JiaoxueshipinEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public JiaoxueshipinView(){
	}
 
 	public JiaoxueshipinView(JiaoxueshipinEntity jiaoxueshipinEntity){
 	try {
			BeanUtils.copyProperties(this, jiaoxueshipinEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}


}
