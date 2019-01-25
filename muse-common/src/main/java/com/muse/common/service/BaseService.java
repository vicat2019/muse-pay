package com.muse.common.service;

import com.muse.common.entity.BaseEntityInfo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基础service,通过mapper提供基本操作方法
 *
 * @author Vic
 * @create 2017-08-10 15:40
 */
public abstract class BaseService<Mapper, T extends BaseEntityInfo> {

	@Autowired
	protected Mapper mapper;
}
