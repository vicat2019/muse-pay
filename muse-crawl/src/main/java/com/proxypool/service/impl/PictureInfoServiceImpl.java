package com.proxypool.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.muse.common.service.BaseService;
import com.proxypool.dao.PictureInfoMapper;
import com.proxypool.picture.PictureInfo;
import com.proxypool.service.PictureInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * @program: muse-pay
 * @description: Picture服务类
 * @author: Vincent
 * @create: 2019-01-23 18:04
 **/
@Service("pictureInfoService")
public class PictureInfoServiceImpl extends BaseService<PictureInfoMapper, PictureInfo> implements PictureInfoService {
    private Logger log = LoggerFactory.getLogger("PictureInfoServiceImpl");

    @Override
    public int insert(PictureInfo pictureInfo) throws Exception {
        // 检查参数
        Preconditions.checkNotNull(pictureInfo);
        // 地址是否为空
        Preconditions.checkArgument(!StringUtils.isEmpty(pictureInfo.getBigUrl()),
                "主地址不能为空");

        return mapper.insertOrUpdate(pictureInfo);
    }

    @Override
    public PageInfo<Collection> query(int page, int pageSize) throws Exception {
        PageHelper.startPage(page, pageSize);

        List<PictureInfo> picList = mapper.queryPicture();

        // 分成矩阵的形式
        List<List<PictureInfo>> resultData = Lists.partition(picList, 4);
        PageInfo pageInfo = new PageInfo<>(picList);
        log.info(pageInfo.toString());

        pageInfo.setList(resultData);

        log.info("查询分页数据，page=" + page + ", size=" + pageSize + ", 返回数据=" + pageInfo.getList().size());
        return pageInfo;
    }


}
