package com.xuecheng.ucenter.service;

import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;

public interface AuthService {

	/**
	 * @description 认证方法
	 * @param authParamsDto 认证参数
	 * @return com.xuecheng.ucenter.model.po.XcUser 用户信息
	 * @author Mr.M
	 * @date 2022/9/29 12:11
	 */
	XcUserExt execute(AuthParamsDto authParamsDto);

}