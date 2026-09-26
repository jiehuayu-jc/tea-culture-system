package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;


/**
 * 心理咨询
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2024-12-26 16:14:55
 */
@TableName("xinlizixun")
public class XinlizixunEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public XinlizixunEntity() {
		
	}
	
	public XinlizixunEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 主键id
	 */
    @TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 咨询名称
	 */
					
	private String zixunmingcheng;
	
	/**
	 * 咨询分类
	 */
					
	private String zixunfenlei;
	
	/**
	 * 封面图片
	 */
					
	private String fengmiantupian;
	
	/**
	 * 咨询费用
	 */
					
	private Double zixunfeiyong;
	
	/**
	 * 开放时间
	 */
					
	private String kaifangshijian;
	
	/**
	 * 咨询介绍
	 */
					
	private String zixunjieshao;
	
	/**
	 * 咨询详情
	 */
					
	private String zixunxiangqing;
	
	/**
	 * 咨询师账号
	 */
					
	private String zixunshizhanghao;
	
	/**
	 * 咨询师姓名
	 */
					
	private String zixunshixingming;
	
	
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 设置：咨询名称
	 */
	public void setZixunmingcheng(String zixunmingcheng) {
		this.zixunmingcheng = zixunmingcheng;
	}
	/**
	 * 获取：咨询名称
	 */
	public String getZixunmingcheng() {
		return zixunmingcheng;
	}
	/**
	 * 设置：咨询分类
	 */
	public void setZixunfenlei(String zixunfenlei) {
		this.zixunfenlei = zixunfenlei;
	}
	/**
	 * 获取：咨询分类
	 */
	public String getZixunfenlei() {
		return zixunfenlei;
	}
	/**
	 * 设置：封面图片
	 */
	public void setFengmiantupian(String fengmiantupian) {
		this.fengmiantupian = fengmiantupian;
	}
	/**
	 * 获取：封面图片
	 */
	public String getFengmiantupian() {
		return fengmiantupian;
	}
	/**
	 * 设置：咨询费用
	 */
	public void setZixunfeiyong(Double zixunfeiyong) {
		this.zixunfeiyong = zixunfeiyong;
	}
	/**
	 * 获取：咨询费用
	 */
	public Double getZixunfeiyong() {
		return zixunfeiyong;
	}
	/**
	 * 设置：开放时间
	 */
	public void setKaifangshijian(String kaifangshijian) {
		this.kaifangshijian = kaifangshijian;
	}
	/**
	 * 获取：开放时间
	 */
	public String getKaifangshijian() {
		return kaifangshijian;
	}
	/**
	 * 设置：咨询介绍
	 */
	public void setZixunjieshao(String zixunjieshao) {
		this.zixunjieshao = zixunjieshao;
	}
	/**
	 * 获取：咨询介绍
	 */
	public String getZixunjieshao() {
		return zixunjieshao;
	}
	/**
	 * 设置：咨询详情
	 */
	public void setZixunxiangqing(String zixunxiangqing) {
		this.zixunxiangqing = zixunxiangqing;
	}
	/**
	 * 获取：咨询详情
	 */
	public String getZixunxiangqing() {
		return zixunxiangqing;
	}
	/**
	 * 设置：咨询师账号
	 */
	public void setZixunshizhanghao(String zixunshizhanghao) {
		this.zixunshizhanghao = zixunshizhanghao;
	}
	/**
	 * 获取：咨询师账号
	 */
	public String getZixunshizhanghao() {
		return zixunshizhanghao;
	}
	/**
	 * 设置：咨询师姓名
	 */
	public void setZixunshixingming(String zixunshixingming) {
		this.zixunshixingming = zixunshixingming;
	}
	/**
	 * 获取：咨询师姓名
	 */
	public String getZixunshixingming() {
		return zixunshixingming;
	}

}
