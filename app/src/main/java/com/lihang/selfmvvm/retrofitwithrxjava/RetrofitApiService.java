package com.lihang.selfmvvm.retrofitwithrxjava;


import com.lihang.selfmvvm.bean.BannerBean;
import com.lihang.selfmvvm.bean.User;
import com.lihang.selfmvvm.bean.basebean.HomeFatherBean;
import com.lihang.selfmvvm.bean.basebean.ResponModel;
import com.lihang.selfmvvm.vo.req.LoginReqVo;
import com.lihang.selfmvvm.vo.req.RegisterReqVo;
import com.lihang.selfmvvm.vo.res.BaseGroupResVo;
import com.lihang.selfmvvm.vo.res.CsDataInfoVo;
import com.lihang.selfmvvm.vo.res.GroupDetailsResVo;
import com.lihang.selfmvvm.vo.res.GroupResVo;
import com.lihang.selfmvvm.vo.res.ImageDataInfo;
import com.lihang.selfmvvm.vo.res.LoginDataVo;
import com.lihang.selfmvvm.vo.res.MemberDetailResVo;
import com.lihang.selfmvvm.vo.res.UploadSingleResVo;
import com.lihang.selfmvvm.vo.res.UserInfoVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Retrofit 接口请求配置都在这
 */
public interface RetrofitApiService {
    //wanAndroid的，轮播banner的接口
    @GET("banner/json")
    Observable<ResponModel<List<BannerBean>>> getBanner();

    //首页文章,curPage拼接。从0开始
    @GET("article/list/{curPage}/json")
    Observable<ResponModel<HomeFatherBean>> getHomeArticles(@Path("curPage") int curPage);

    //收藏文章列表
    @GET("lg/collect/list/{curPage}/json")
    Observable<ResponModel<HomeFatherBean>> getCollectArticles(@Path("curPage") int curPage);

    //收藏站内文章
    @POST("lg/collect/{id}/json")
    Observable<ResponModel<String>> collectArticle(@Path("id") int id);

    //收藏站外文章
    @FormUrlEncoded
    @POST("lg/collect/add/json")
    Observable<ResponModel<String>> collectOutArticle(@Field("title") String title, @Field("author") String author, @Field("link") String link);

    //取消收藏 -- 首页文章列表
    @POST("lg/uncollect_originId/{id}/json")
    Observable<ResponModel<String>> unCollectByHome(@Path("id") int id);

    //取消收藏 -- 我的收藏列表
    @FormUrlEncoded
    @POST("lg/uncollect/{id}/json")
    Observable<ResponModel<String>> unCollectByMe(@Path("id") int id, @Field("originId") int originId);


    //退出登录
    @GET("user/logout/json")
    Observable<ResponModel<String>> loginOut();

    @POST("user/login")
    @FormUrlEncoded
    Observable<ResponModel<User>> login(@FieldMap HashMap<String, Object> map);

    //Retrofit get请求
    @GET("xiandu/category/wow")
    Observable<String> getGank(@Query("en_name") String en_name);


    //Retrofit post请求
    @POST("add2gank")
    @FormUrlEncoded
    Observable<ResponseBody> postAddGank(@FieldMap HashMap<String, String> map);


    //单张图片上传
    @POST("upload/picss")
    @Multipart
    Observable<ResponModel<String>> uploadPicss(@Part("type") RequestBody type, @PartMap Map<String, RequestBody> maps);


    @GET
    @Streaming
    Observable<ResponseBody> downloadFile(@Url String url, @Header("RANGE") String range);


    //----------------------------------------------------政企通 api 接口---------------------------------------------------------------

    /**
     * 登录-政企通
     *
     * @param loginReqVo
     * @return
     */
    @POST("sys/login")
    Observable<ResponModel<LoginDataVo>> govassLogin(@Body LoginReqVo loginReqVo);

    /**
     * 注销登录-政企通
     * @param token
     * @return
     */
    @POST("sys/logout")
    Observable<ResponModel<String>> govassLogout(@Body Object token);

    /**
     * 注册-企业用户
     *
     * @param registerReqVo
     * @return
     */
    @POST("sys/enterpriseuser/register")
    Observable<ResponModel<String>> register(@Body RegisterReqVo registerReqVo);

    /**
     * 修改注册信息
     * @param registerReqVo
     * @return
     */
    @POST("sys/enterpriseuser/updateByEnterprise")
    Observable<ResponModel<String>> updateRegister(@Body RegisterReqVo registerReqVo);

    /**
     * 获取用户信息
     *
     * @param token
     * @return
     */
    @GET("sys/user/info")
    Observable<ResponModel<UserInfoVo>> getUserInfo(@Query("token") String token);

    /**
     * 获取轮播图
     *
     * @param token
     * @return
     */
    @GET("sys/rotationplot/listForApp")
    Observable<ResponModel<List<ImageDataInfo>>> getBannerList(@Query("token") String token);


    /**
     * 获取客服
     *
     * @param token
     * @return
     */
    @GET("sys/customerservice/default")
    Observable<ResponModel<CsDataInfoVo>> getCustomerService(@Query("token") String token);

    /**
     * 单个文件上传
     */
    @POST("file/upload")
    @Multipart
    Observable<ResponModel<UploadSingleResVo>> uploadSigleFile(@Part("type") RequestBody type, @Part MultipartBody.Part file);

    /**
     * 多个文件上传
     */
//    file/batchUpload

    /**
     * 文件下载
     */
//    file/download
    //Retrofit下载文件
    @GET
    @Streaming
    //10以上用@streaming。不会造成oom，反正你用就是了
    Observable<ResponseBody> downloadFile(@Url String url);

//    sys/group/addUser

    /**
     * 获取分组列表
     *
     * @param type
     * @return
     */
    @GET("sys/group/list")
    Observable<ResponModel<GroupResVo>> getGuoupList(@Query("type") int type);

    /**
     * 获取分组列表
     *
     * @param type
     * @return
     */
    @GET("sys/group/list")
    Call<BaseGroupResVo> getGuoupListinCall(@Query("type") int type);

    /**
     * 获取某个分组下所有用户信息
     * @param groupId
     * @return
     */
    @GET("sys/group/info/{groupId}")
    Observable<ResponModel<GroupDetailsResVo>> getGroupAllUser(@Query("groupId") int groupId);

    /**
     * 获取全部政府用户
     * @return
     */
    @GET("sys/user/listAllGovernment")
    Observable<ResponModel<List<MemberDetailResVo>>> getAllGovernment();

    /**
     * 获取全部企业用户
     * @return
     */
    @GET("sys/enterpriseuser/listAllEnterprise")
    Observable<ResponModel<MemberDetailResVo>> getAllEnterprise();

    /**
     * 获取政府某个分组下所有用户信息
     * @param groupId
     * @return
     */
    @GET("sys/user/listAllGovernment")
    Observable<ResponModel<MemberDetailResVo>> getGovernmentFromId(@Query("groupId") int groupId);

    /**
     * 获取企业某个分组下所有用户信息
     * @param groupId
     * @return
     */
    @GET("sys/enterpriseuser/listAllEnterprise")
    Observable<ResponModel<MemberDetailResVo>> getEnterpriseFromId(@Query("groupId") int groupId);
}
