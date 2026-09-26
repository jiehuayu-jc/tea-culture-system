package com.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 * 预约咨询
 * @author 
 * @email 
 * @date 2024-12-26 16:14:56
 */
public class YuyuezixunVO implements Serializable {
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
	 * 预约时间
	 */
		
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 
	private Date yuyueshijian;
		
	/**
	 * 咨询费用
	 */
	
	private String zixunfeiyong;
		
	/**
	 * 需求描述
	 */
	
	private String xuqiumiaoshu;
		
	/**
	 * 预约详情
	 */
	
	private String yuyuexiangqing;
		
	/**
	 * 用户账号
	 */
	
	private String yonghuzhanghao;
		
	/**
	 * 用户姓名
	 */
	
	private String yonghuxingming;
		
	/**
	 * 咨询师账号
	 */
	
	private String zixunshizhanghao;
		
	/**
	 * 咨询师姓名
	 */
	
	private String zixunshixingming;
		
	/**
	 * 是否支付
	 */
	
	private String ispay;
				
	
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
	 * 设置：预约时间
	 */
	 
	public void setYuyueshijian(Date yuyueshijian) {
		this.yuyueshijian = yuyueshijian;
	}
	
	/**
	 * 获取：预约时间
	 */
	public Date getYuyueshijian() {
		return yuyueshijian;
	}
				
	
	/**
	 * 设置：咨询费用
	 */
	 
	public void setZixunfeiyong(String zixunfeiyong) {
		this.zixunfeiyong = zixunfeiyong;
	}
	
	/**
	 * 获取：咨询费用
	 */
	public String getZixunfeiyong() {
		return zixunfeiyong;
	}
				
	
	/**
	 * 设置：需求描述
	 */
	 
	public void setXuqiumiaoshu(String xuqiumiaoshu) {
		this.xuqiumiaoshu = xuqiumiaoshu;
	}
	
	/**
	 * 获取：需求描述
	 */
	public String getXuqiumiaoshu() {
		return xuqiumiaoshu;
	}
				
	
	/**
	 * 设置：预约详情
	 */
	 
	public void setYuyuexiangqing(String yuyuexiangqing) {
		this.yuyuexiangqing = yuyuexiangqing;
	}
	
	/**
	 * 获取：预约详情
	 */
	public String getYuyuexiangqing() {
		return yuyuexiangqing;
	}
				
	
	/**
	 * 设置：用户账号
	 */
	 
	public void setYonghuzhanghao(String yonghuzhanghao) {
		this.yonghuzhanghao = yonghuzhanghao;
	}
	
	/**
	 * 获取：用户账号
	 */
	public String getYonghuzhanghao() {
		return yonghuzhanghao;
	}
				
	
	/**
	 * 设置：用户姓名
	 */
	 
	public void setYonghuxingming(String yonghuxingming) {
		this.yonghuxingming = yonghuxingming;
	}
	
	/**
	 * 获取：用户姓名
	 */
	public String getYonghuxingming() {
		return yonghuxingming;
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
				
	
	/**
	 * 设置：是否支付
	 */
	 
	public void setIspay(String ispay) {
		this.ispay = ispay;
	}
	
	/**
	 * 获取：是否支付
	 */
	public String getIspay() {
		return ispay;
	}
			
}
