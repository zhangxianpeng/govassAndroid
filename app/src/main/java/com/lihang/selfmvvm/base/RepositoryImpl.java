package com.lihang.selfmvvm.base;

import com.lihang.selfmvvm.bean.BannerBean;
import com.lihang.selfmvvm.bean.User;
import com.lihang.selfmvvm.bean.basebean.HomeFatherBean;
import com.lihang.selfmvvm.bean.basebean.ParamsBuilder;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.bean.basebean.ResponModel;
import com.lihang.selfmvvm.common.PARAMS;
import com.lihang.selfmvvm.common.SystemConst;
import com.lihang.selfmvvm.retrofitwithrxjava.uploadutils.UploadFileRequestBody;
import com.lihang.selfmvvm.vo.req.LoginReqVo;
import com.lihang.selfmvvm.vo.req.RegisterReqVo;
import com.lihang.selfmvvm.vo.res.CsDataInfoVo;
import com.lihang.selfmvvm.vo.res.GroupDetailsResVo;
import com.lihang.selfmvvm.vo.res.GroupResVo;
import com.lihang.selfmvvm.vo.res.ImageDataInfo;
import com.lihang.selfmvvm.vo.res.LoginDataVo;
import com.lihang.selfmvvm.vo.res.MemberDetailResVo;
import com.lihang.selfmvvm.vo.res.UploadSingleResVo;
import com.lihang.selfmvvm.vo.res.UserInfoVo;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.MutableLiveData;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 网络请求api
 */
public class RepositoryImpl extends BaseModel {

    //获取 banner列表
    public MutableLiveData<Resource<List<BannerBean>>> getBannerList() {
        MutableLiveData<Resource<List<BannerBean>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getBanner(), liveData);
    }

    //获取首页文章
    public MutableLiveData<Resource<HomeFatherBean>> getHomeArticles(int curPage, ParamsBuilder paramsBuilder) {

        MutableLiveData<Resource<HomeFatherBean>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getHomeArticles(curPage), liveData, paramsBuilder);
    }

    //获取收藏列表
    public MutableLiveData<Resource<HomeFatherBean>> getCollectArticles(int curPage, ParamsBuilder paramsBuilder) {
        MutableLiveData<Resource<HomeFatherBean>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getCollectArticles(curPage), liveData, paramsBuilder);
    }

    //站内收藏文章
    public MutableLiveData<Resource<String>> collectArticle(int id) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().collectArticle(id), liveData, ParamsBuilder.build().isShowDialog(false));//不显示加载logo
    }

    //站外收藏文章
    public MutableLiveData<Resource<String>> collectOutArticle(String title, String author, String link) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().collectOutArticle(title, author, link), liveData, ParamsBuilder.build().isShowDialog(false));
    }

    //取消收藏 -- 首页列表
    public MutableLiveData<Resource<String>> unCollectByHome(int id) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().unCollectByHome(id), liveData, ParamsBuilder.build().isShowDialog(false));
    }


    public MutableLiveData<Resource<String>> unCollectByMe(int id, int originId) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().unCollectByMe(id, originId), liveData, null);
    }


    //退出登录
    public MutableLiveData<Resource<String>> LoginOut() {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().loginOut(), liveData);
    }


    //登录
    public MutableLiveData<Resource<User>> login(HashMap<String, Object> map, ParamsBuilder paramsBuilder) {
        MutableLiveData<Resource<User>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().login(map), liveData, paramsBuilder);
    }


    //正常下载，
    public MutableLiveData<Resource<File>> downFile(String destDir, String fileName) {
        MutableLiveData<Resource<File>> liveData = new MutableLiveData<>();
        return downLoadFile(getApiService().downloadFile(SystemConst.QQ_APK), liveData, destDir, fileName);
    }

    //断点下载
    public MutableLiveData<Resource<File>> downFile(String destDir, String fileName, long currentLength) {
        String range = "bytes=" + currentLength + "-";
        MutableLiveData<Resource<File>> liveData = new MutableLiveData<>();
        return downLoadFile(getApiService().downloadFile(SystemConst.QQ_APK, range), liveData, destDir, fileName, currentLength);
    }


    /**
     * 上传文件(进度监听)
     * @param type
     * @param file
     * @return
     */
    public MutableLiveData<Resource<UploadSingleResVo>> uploadSigleFile(String type, File file) {
        MutableLiveData<Resource<UploadSingleResVo>> liveData = new MutableLiveData<>();

        UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody(file, liveData);
        //"file"  是key
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), uploadFileRequestBody);
        return upLoadFile(getApiService().uploadSigleFile(PARAMS.changeToRquestBody(type), body), liveData);
    }


    //上传多张图片(进度监听)  多张图片进度监听，图片一张一张上传 所以用到了PictureProgressUtil工具类。用之前init初始数据，setProgress即可
    public MutableLiveData<Resource<String>> upLoadPicss(String type, HashMap<String, File> files) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();

        Map<String, RequestBody> bodyMap = new HashMap<>();
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody(file, liveData);
            bodyMap.put("files" + "\"; filename=\"" + file.getName(), uploadFileRequestBody);
        }

        //如果是传统的不带进度监听 只需要
//        bodyMap=PARAMS.manyFileToPartBody(files);
        return upLoadFile(getApiService().uploadPicss(PARAMS.changeToRquestBody(type), bodyMap), liveData);
    }

    //----------------------------------------------------政企通--------------------------------------------------------------

    /**
     * 登录-政企通
     *
     * @param loginReqVo
     * @return
     */
    public MutableLiveData<Resource<LoginDataVo>> govassLogin(LoginReqVo loginReqVo) {
        MutableLiveData<Resource<LoginDataVo>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().govassLogin(loginReqVo), liveData);
    }

    /**
     * 注销登录-政企通
     * @param token
     * @return
     */
    public MutableLiveData<Resource<String>> govassLogout(Object token) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().govassLogout(token), liveData);
    }

    /**
     * 注册-政企通
     * @param registerReqVo
     * @return
     */
    public MutableLiveData<Resource<String>> register(RegisterReqVo registerReqVo) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().register(registerReqVo), liveData);
    }



    /**
     * 获取用户信息
     *
     * @param token
     * @return
     */
    public MutableLiveData<Resource<UserInfoVo>> getUserInfo(String token) {
        MutableLiveData<Resource<UserInfoVo>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getUserInfo(token), liveData);
    }

    /**
     * 获取轮播图
     *
     * @param token
     * @return
     */
    public MutableLiveData<Resource<List<ImageDataInfo>>> getGovassBannerList(String token) {
        MutableLiveData<Resource<List<ImageDataInfo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getBannerList(token), liveData);
    }

    /**
     * 获取客服信息
     *
     * @param token
     * @return
     */
    public MutableLiveData<Resource<CsDataInfoVo>> getCustomerService(String token) {
        MutableLiveData<Resource<CsDataInfoVo>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getCustomerService(token), liveData);
    }

    //----------------------------------------------------政企通 通讯录------------------------------------------------------------
    public MutableLiveData<Resource<GroupResVo>> getGuoupList(int type) {
        MutableLiveData<Resource<GroupResVo>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getGuoupList(type), liveData);
    }

    public MutableLiveData<Resource<GroupDetailsResVo>> getGroupAllUser(int groupId) {
        MutableLiveData<Resource<GroupDetailsResVo>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getGroupAllUser(groupId), liveData);
    }

    public MutableLiveData<Resource<List<MemberDetailResVo>>> getAllGovernment() {
        MutableLiveData<Resource<List<MemberDetailResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getAllGovernment(), liveData);
    }

    public MutableLiveData<Resource<List<MemberDetailResVo>>> getAllEnterprise() {
        MutableLiveData<Resource<List<MemberDetailResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getAllEnterprise(), liveData);
    }

    public MutableLiveData<Resource<List<MemberDetailResVo>>> getGovernmentFromId(int groupId) {
        MutableLiveData<Resource<List<MemberDetailResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getGovernmentFromId(groupId), liveData);
    }

    public MutableLiveData<Resource<List<MemberDetailResVo>>> getEnterpriseFromId(int groupId) {
        MutableLiveData<Resource<List<MemberDetailResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getEnterpriseFromId(groupId), liveData);
    }
}
