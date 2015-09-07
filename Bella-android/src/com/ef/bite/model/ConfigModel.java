package com.ef.bite.model;

import com.ef.bite.AppConst;
import com.ef.bite.utils.JsonSerializeHelper;

/**
 * 系统设置：是否初始化？
 * @author Admin
 *
 */
public class ConfigModel extends BaseJsonModel {
	/**
	 * 后安装的chunk是否已经加载
	 */
	public boolean IsLaterinstallChunksLoaded = false;
	
	/**
	 * 预安装的chunk是否已经加载
	 */
	public boolean IsPreinstallChunksLoaded = false;
	
	/**
	 * 欢迎页面是否已经加载
	 */
	public boolean IsWelPageLoaded = false;
	/**
	 * 是否开启通知
	 */
	public boolean IsNotificationOn = true;
	/**
	 * 设置声音效果
	 */
	public boolean IsSoundEffectOn = true;
	/**
	 **课程难度 0 - beginner 1 - advanced
	 **/
	public String CourseLevel = "";
	/**
	 * App多语言设置，
	 * 0 - 随系统
	 * 1 - 英语
	 * 2 - 中文简体
	 */
	public int MultiLanguageType = AppConst.MultiLanguageType.Default;

	@Override
	public void parse(String json) {
		try{
			ConfigModel config = (ConfigModel)JsonSerializeHelper.JsonDeserialize(json, ConfigModel.class);
			this.IsLaterinstallChunksLoaded = config.IsLaterinstallChunksLoaded;
			this.IsWelPageLoaded = config.IsWelPageLoaded;
			this.IsNotificationOn = config.IsNotificationOn;
			this.IsSoundEffectOn = config.IsSoundEffectOn;
			this.MultiLanguageType = config.MultiLanguageType;
			this.IsPreinstallChunksLoaded = config.IsPreinstallChunksLoaded;
            this.CourseLevel = config.CourseLevel;
        }catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public String toJson() {
		try{
			return JsonSerializeHelper.JsonSerializer(this);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	
}
