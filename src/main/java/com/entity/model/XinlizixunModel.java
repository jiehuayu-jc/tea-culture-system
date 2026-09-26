package com.entity.model;

import java.io.Serializable;


/**
 * 心理咨询
 * 接收传参的实体类  
 *（实际开发中配合移动端接口开发手动去掉些没用的字段， 后端一般用entity就够用了） 
 * 取自ModelAndView 的model名称
 * @author 
 * @email 
 * @date 2024-12-26 16:14:55
 */
public class XinlizixunModel implements Serializable {
	private static final long serialVersionUID = 1L;

	 			
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
