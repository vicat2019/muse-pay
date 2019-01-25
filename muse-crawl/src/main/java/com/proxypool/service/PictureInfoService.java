package com.proxypool.service;

import com.github.pagehelper.PageInfo;
import com.proxypool.picture.PictureInfo;

import java.util.Collection;

public interface PictureInfoService {

    int insert(PictureInfo pictureInfo) throws Exception;

    PageInfo<Collection> query(int page, int pageSize) throws Exception;

}
