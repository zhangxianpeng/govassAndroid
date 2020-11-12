/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: HomeFragmentViewModel
 * Author: zhang
 * Date: 2020/7/4 13:23
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.lihang.selfmvvm.ui.fragment;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.ImageDataInfo;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.MsgMeResVo;
import com.lihang.selfmvvm.vo.res.SearchValueResVo;
import com.lihang.selfmvvm.vo.res.VersionVo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * @ClassName: HomeFragmentViewModel
 * @Description: HomeFragment 中的数据操作
 * 需要加上 public 权限修饰符
 * @Author: zhang
 * @Date: 2020/7/4 13:23
 */
public class HomeFragmentViewModel extends BaseViewModel<RepositoryImpl> {
    public HomeFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 获取公告轮播
     *
     * @param token
     * @return
     */
    public LiveData<Resource<List<ImageDataInfo>>> getGovassBannerList(String token) {
        return getRepository().getGovassBannerList(token);
    }

    /**
     * 获取我的消息列表
     *
     * @param page
     * @return
     */
    public LiveData<Resource<ListBaseResVo<MsgMeResVo>>> getMsgMeList(int page) {
        return getRepository().getMsgMeList(page);
    }

    /**
     * 获取未读消息
     *
     * @return
     */
    public LiveData<Resource<String>> getMsgUnRead() {
        return getRepository().getMsgUnRead();
    }

    /**
     * 获取版本更新信息
     *
     * @param device
     * @return
     */
    public LiveData<Resource<VersionVo>> getNewVersion(int device) {
        return getRepository().getNewVersion(device);
    }

    /**
     * 获取搜索结果
     *
     * @param page
     * @param searchValue
     * @return
     */
    public LiveData<Resource<ListBaseResVo<SearchValueResVo>>> getSearchValue(int page, int limit, String searchValue) {
        return getRepository().getSearchValue(page, limit,searchValue);
    }

}
