package com.muse.pay.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.muse.common.util.FileUtils;
import com.muse.common.util.RandomUtils;
import com.muse.common.util.RedisUtil;
import com.muse.common.util.TextUtils;
import com.muse.pay.dao.BookInfoMapper;
import com.muse.pay.dao.UserInfoDao;
import com.muse.pay.entity.BookInfo;
import com.muse.pay.service.BookInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Created by code generator  on 2018/07/22.
 */
@Service("bookInfoService")
public class BookInfoServiceImpl extends BaseService<BookInfoMapper, BookInfo> implements BookInfoService {
    private static Logger log = LoggerFactory.getLogger("BookInfoServiceImpl");

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public ResultData add(BookInfo obj) throws Exception {
        if (obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        int result = mapper.insert(obj);
        return ResultData.getSuccessResult(result);
    }


    @Override
    public ResultData update(BookInfo obj) throws Exception {
        if (obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        if (obj.getId() == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        int result = mapper.updateByPrimaryKey(obj);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData get(Integer id) throws Exception {
        log.info("查询数据详情，id=" + id);
        if (id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        // 先从缓存中取
        Object obj = redisUtil.get("BOOK_" + id);
        if (obj != null) {
            return ResultData.getSuccessResult((BookInfo) obj);
        }

        BookInfo result = mapper.selectByPrimaryKey(id);
        if (result != null) {
            redisUtil.set("BOOK_" + id, result, RandomUtils.getRandomNum(60, 100)*60);
        }
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData del(Integer id) throws Exception {
        if (id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        int result = mapper.deleteByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData list(BookInfo bookInfo, int pageNum, int pageSize) {
        pageSize = 20;
        PageHelper.startPage(pageNum, pageSize);

        List<BookInfo> bookList = mapper.selectByColumn(bookInfo);
        PageInfo<BookInfo> pageInfo = new PageInfo<>(bookList);
        log.info("分页查询数据，返回记录数=" + bookList.size());

        return ResultData.getSuccessResult(pageInfo);
    }

    @Override
    public ResultData queryBookIn(List<Integer> ids) throws Exception {
        if (ids == null || ids.size() == 0) {
            return ResultData.getErrResult("根据ID集合查询图书，参数不能为空");
        }
        List<BookInfo> bookList = new ArrayList<>();

        // 先从缓存中获取，不存在，再从数据库中获取
        if (ids.size() == 1) {
            ResultData resultData = get(ids.get(0));
            if (!resultData.isOk() || resultData.resultIsEmpty()) {
                return resultData;
            }
            BookInfo bookInfo = (BookInfo) resultData.getData();
            bookList.add(bookInfo);

        } else {
            List<Integer> subIds = new ArrayList<>();
            for (Integer id : ids) {
                Object obj = redisUtil.get("BOOK_" + id);
                if (obj != null) {
                    bookList.add((BookInfo) obj);
                } else {
                    subIds.add(id);
                }
            }
            if (subIds.size() > 0) {
                List<BookInfo> subBookList = mapper.queryBookIn(ids);
                if (subBookList != null && subBookList.size() > 0) {
                    bookList.addAll(subBookList);
                }
            }
        }

        return ResultData.getSuccessResult(bookList);
    }

    @Override
    public ResultData getRecommend(int size) throws Exception {
        List<Integer> idList = new ArrayList<>();
        Set<Integer> bookIdSet = new HashSet<>();
        if (size == 0) size = 20;

        while (bookIdSet.size() < size) {
            Random random = new Random();
            bookIdSet.add(random.nextInt(10000));
        }

        Iterator<Integer> it = bookIdSet.iterator();
        while (it.hasNext()) {
            idList.add(it.next());
        }

        if (idList.size() == 0) {
            log.error("推荐的ID列表不能为空");
            return ResultData.getErrResult("异常");
        }

        List<BookInfo> recommendBookList = mapper.queryBookIn(idList);
        log.info("推荐列表长度=" + recommendBookList.size());

        return ResultData.getSuccessResult(recommendBookList);
    }

    @Override
    public ResultData genRandomBookFile(int count, String path) throws Exception {

        // 获取所有图书的ID集合
        List<Integer> bookIdList = mapper.getAllBookIds();

        // 获取所有用户ID
        List<Integer> userIdList = userInfoDao.getAllUserIds();

        Set<String> contentSet = new HashSet<>();
        while (contentSet.size() < count) {
            // 随机选取图书ID
            String books = "";
            int bookCount = Integer.valueOf(TextUtils.getNumRandomStr(1)) % 6;
            if (bookCount == 0) bookCount = 1;
            for (int i=0; i<bookCount; i++) {
                Random bookRandom = new Random();
                int bookIdIndex = bookRandom.nextInt(bookIdList.size());
                books += bookIdList.get(bookIdIndex) + "a";
            }
            if (books.length() > 1) books = books.substring(0, books.length()-1);

            // 随机选取用户ID
            Random userRandom = new Random();
            int userIdIndex = userRandom.nextInt(userIdList.size());
            int userId = userIdList.get(userIdIndex);

            contentSet.add(userId + "," + books);
        }

        // 写入文件中
        FileUtils.writeContentToFile(path, contentSet);

        return ResultData.getSuccessResult();
    }


}
