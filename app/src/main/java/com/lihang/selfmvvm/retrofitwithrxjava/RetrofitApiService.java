package com.lihang.selfmvvm.retrofitwithrxjava;


import com.lihang.selfmvvm.bean.BannerBean;
import com.lihang.selfmvvm.bean.User;
import com.lihang.selfmvvm.bean.basebean.HomeFatherBean;
import com.lihang.selfmvvm.bean.basebean.ResponModel;
import com.lihang.selfmvvm.vo.req.AddGroupReqVo;
import com.lihang.selfmvvm.vo.req.AddOdReqVo;
import com.lihang.selfmvvm.vo.req.AddProjectReqVo;
import com.lihang.selfmvvm.vo.req.AuditReqVo;
import com.lihang.selfmvvm.vo.req.LoginReqVo;
import com.lihang.selfmvvm.vo.req.PlainMsgReqVo;
import com.lihang.selfmvvm.vo.req.RegisterReqVo;
import com.lihang.selfmvvm.vo.req.RemoveUserReqVo;
import com.lihang.selfmvvm.vo.res.BaseGroupResVo;
import com.lihang.selfmvvm.vo.res.CsDataInfoVo;
import com.lihang.selfmvvm.vo.res.EnpriceOdVo;
import com.lihang.selfmvvm.vo.res.EnterpriseVo;
import com.lihang.selfmvvm.vo.res.GroupDetailsResVo;
import com.lihang.selfmvvm.vo.res.GroupResVo;
import com.lihang.selfmvvm.vo.res.ImageDataInfo;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.LoginDataVo;
import com.lihang.selfmvvm.vo.res.MemberDetailResVo;
import com.lihang.selfmvvm.vo.res.MsgMeResVo;
import com.lihang.selfmvvm.vo.res.NoticeResVo;
import com.lihang.selfmvvm.vo.res.OfficialDocResVo;
import com.lihang.selfmvvm.vo.res.PlainMsgAttachmentListResVo;
import com.lihang.selfmvvm.vo.res.PlainMsgResVo;
import com.lihang.selfmvvm.vo.res.ProjectResVo;
import com.lihang.selfmvvm.vo.res.QuestionNaireItemResVo;
import com.lihang.selfmvvm.vo.res.UploadAttachmentResVo;
import com.lihang.selfmvvm.vo.res.UploadSingleResVo;
import com.lihang.selfmvvm.vo.res.UserInfoVo;
import com.lihang.selfmvvm.vo.res.VersionVo;

import java.io.File;
import java.util.HashMap;
import java.util.List;

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
     *
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
     *
     * @param registerReqVo
     * @return
     */
    @POST("sys/enterpriseuser/updateByEnterprise")
    Observable<ResponModel<String>> updateRegister(@Body RegisterReqVo registerReqVo);

    /**
     * 获取用户信息
     *
     * @return
     */
    @GET("sys/user/info")
    Observable<ResponModel<UserInfoVo>> getUserInfo();

    /**
     * 获取轮播图
     *
     * @param token
     * @return
     */
    @GET("sys/rotationplot/listForApp")
    Observable<ResponModel<List<ImageDataInfo>>> getBannerList(@Query("token") String token);

    /**
     * 获取调查问卷详情（已填报）
     *
     * @param id
     * @return
     */
    @GET("sys/questionnairerecorddata/info/{id}")
    Observable<ResponModel<QuestionNaireItemResVo>> getQuestionnairerecordData(@Path("id") int id);

    /**
     * 调查问卷(政府)
     *
     * @param page
     * @param status
     * @return
     */
    @GET("sys/questionnairerecord/list")
    Observable<ResponModel<ListBaseResVo<QuestionNaireItemResVo>>> getQuestiontList(@Query("page") int page, @Query("status") String status);

    /**
     * 根据问卷id获取企业列表
     *
     * @param page
     * @param status
     * @return
     */
    @GET("sys/questionnairerecorddata/list")
    Observable<ResponModel<ListBaseResVo<EnterpriseVo>>> getEnpriceList(@Query("page") int page, @Query("status") String status, @Query("questionnaireRecordId") int questionnaireRecordId);

    /**
     * 调查问卷（企业）
     *
     * @param page
     * @param status
     * @return
     */
    @GET("sys/questionnairerecord/listMe")
    Observable<ResponModel<ListBaseResVo<QuestionNaireItemResVo>>> getEnQuestiontList(@Query("page") int page, @Query("status") String status);

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
    @POST("file/batchUpload")
    @Multipart
    Observable<ResponModel<List<UploadAttachmentResVo>>> uploadMultyFile(@Part() List<MultipartBody.Part> parts);

    /**
     * 文件下载
     */
    @GET
    @Streaming
    //10以上用@streaming。不会造成oom，反正你用就是了
    Observable<File> downloadFile(@Url String url);

    //----------------------------------------------------政企通 通讯录 api------------------------------------------------------------

    /**
     * 添加用户到指定分组
     *
     * @param registerReqVo
     * @return
     */
    @POST("sys/group/addUser")
    Observable<ResponModel<String>> addUser(@Body RegisterReqVo registerReqVo);

    /**
     * 查询分组名是否重复
     *
     * @param type
     * @return
     */
    @GET("sys/group/checkGroupNameRepeat")
    Observable<ResponModel<String>> checkGroupNameRepeat(@Query("type") int type, @Query("name") String name);


    /**
     * 更新分组名
     *
     * @param type
     * @return
     */
    @GET("sys/group/checkGroupNameRepeat")
    Observable<ResponModel<String>> updateGroupName(@Query("groupId") int groupId, @Query("type") int type, @Query("name") String name);

    /**
     * 删除通讯录分组
     *
     * @param groupIds
     * @return
     */
    @POST("sys/group/delete")
    Observable<ResponModel<String>> deleteGroup(@Body List<Integer> groupIds);

    /**
     * 移除用户
     *
     * @param removeUserReqVo
     * @return
     */
    @POST("sys/group/removeUser")
    Observable<ResponModel<String>> removeUser(@Body RemoveUserReqVo removeUserReqVo);

    /**
     * 保存通讯录分组
     *
     * @param addGroupReqVo
     * @return
     */
    @POST("sys/group/save")
    Observable<ResponModel<String>> saveGroup(@Body AddGroupReqVo addGroupReqVo);

    /**
     * 修改通讯录分组
     *
     * @param registerReqVo
     * @return
     */
    @POST("sys/group/update")
    Observable<ResponModel<String>> updateGroup(@Body RegisterReqVo registerReqVo);


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
     *
     * @param groupId
     * @return
     */
    @GET("sys/group/info/{groupId}")
    Observable<ResponModel<GroupDetailsResVo>> getGroupAllUser(@Query("groupId") int groupId);

    /**
     * 获取全部政府用户
     *
     * @return
     */
    @GET("sys/user/listAllGovernment")
    Observable<ResponModel<List<MemberDetailResVo>>> getAllGovernment();

    /**
     * 获取全部企业用户
     *
     * @return
     */
    @GET("sys/enterpriseuser/listAllEnterprise")
    Observable<ResponModel<List<MemberDetailResVo>>> getAllEnterprise();

    /**
     * 获取政府某个分组下所有用户信息
     *
     * @param groupId
     * @return
     */
    @GET("sys/user/listAllGovernment")
    Observable<ResponModel<List<MemberDetailResVo>>> getGovernmentFromId(@Query("groupId") int groupId);

    /**
     * 获取企业某个分组下所有用户信息
     *
     * @param groupId
     * @return
     */
    @GET("sys/enterpriseuser/listAllEnterprise")
    Observable<ResponModel<List<MemberDetailResVo>>> getEnterpriseFromId(@Query("groupId") int groupId);

    //----------------------------------------------------政企通 公文 api------------------------------------------------------------

    /**
     * 删除公文
     *
     * @param registerReqVo
     * @return
     */
    @POST("sys/officialdocument/delete")
    Observable<ResponModel<String>> deleteOfficalDoc(@Body RegisterReqVo registerReqVo);

    /**
     * 查询公文详情
     *
     * @param id
     * @return
     */
    @GET("sys/officialdocument/info/{id}")
    Observable<ResponModel<OfficialDocResVo>> getOfficalDocDetail(@Path("id") int id);

    /**
     * 我的公文（政府端）
     *
     * @return
     */
    @GET("sys/officialdocument/list")
    Observable<ResponModel<ListBaseResVo<OfficialDocResVo>>> getOfficalDoc(@Query("page") int page);


    /**
     * 我的收文（企业端）
     *
     * @return
     */
    @GET("sys/officialdocument/list-receive")
    Observable<ResponModel<ListBaseResVo<OfficialDocResVo>>> getReceiveOfficalDoc(@Query("page") int page);

    /**
     * 待审核公文列表
     *
     * @return
     */
    @GET("sys/officialdocument/list-pending")
    Observable<ResponModel<ListBaseResVo<OfficialDocResVo>>> getWaitPendingOfficalDoc();

    /**
     * 保存公文
     *
     * @param registerReqVo
     * @return
     */
    @POST("sys/officialdocument/save")
    Observable<ResponModel<String>> saveOfficalDoc(@Body RegisterReqVo registerReqVo);

    /**
     * 修改公文
     *
     * @param registerReqVo
     * @return
     */
    @POST("sys/officialdocument/update")
    Observable<ResponModel<String>> updateOfficalDoc(@Body RegisterReqVo registerReqVo);

    //----------------------------------------------------政企通 消息 api------------------------------------------------------------


    /**
     * 获取未读消息
     *
     * @return
     */
    @GET("sys/msg/un-read")
    Observable<ResponModel<String>> getMsgUnRead();

    /**
     * 查询我的公文列表
     *
     * @return
     */
    @GET("sys/msg/list-me")
    Observable<ResponModel<ListBaseResVo<MsgMeResVo>>> getMsgMeList(@Query("page") int page);

    /**
     * 查询我的企业列表
     *
     * @return
     */
    @GET("sys/enterprise/list")
    Observable<ResponModel<ListBaseResVo<EnterpriseVo>>> getEnterpriseList(@Query("page") int page);


    /**
     * 查询消息详情
     *
     * @return
     */
    @POST("sys/msg/readMsg/{id}")
    Observable<ResponModel<MsgMeResVo>> getMsgDetail(@Path("id") int id);


    /**
     * 获取普通消息的接收者
     *
     * @return
     */
    @GET("sys/msg/list-receiver")
    Observable<ResponModel<List<MemberDetailResVo>>> getMsgReceiver();


    /**
     * 标为已读 （方便做批量标记的）
     *
     * @return
     */
    @POST("sys/msg/read")
    Observable<ResponModel<List<MemberDetailResVo>>> read();


    /**
     * 阅读信息
     *
     * @return
     */
    @POST("sys/msg/readMsg/{id}")
    Observable<ResponModel<MsgMeResVo>> transferReadFlag(@Path("id") int id);

    //----------------------------------------------------政企通 公告 api------------------------------------------------------------
    @GET("sys/notice/list-published")
    Observable<ResponModel<ListBaseResVo<NoticeResVo>>> getPublishedNotice(@Query("page") int page);


    //----------------------------------------------------政企通 版本更新 api------------------------------------------------------------
    @GET("sys/app-version/lastest")
    Observable<ResponModel<VersionVo>> getNewVersion(@Query("device") int device);

    //----------------------------------------------------政企通 普通消息 api------------------------------------------------------------

    /**
     * 删除普通消息管理
     *
     * @return
     */
    @POST("sys/plainmsg/delete")
    Observable<ResponModel<String>> deletePlainMsg(@Body List<Integer> idList);

    /**
     * 发送消息
     *
     * @return
     */
    @POST("sys/plainmsg/save")
    Observable<ResponModel<String>> savePlainMsg(@Body PlainMsgReqVo plainMsgReqVo);

    /**
     * 修改普通消息管理
     *
     * @return
     */
    @POST("sys/plainmsg/update")
    Observable<ResponModel<List<MemberDetailResVo>>> updatePlainMsg();


    /**
     * 查询普通消息管理详情
     *
     * @return
     */
    @GET("sys/plainmsg/info/{id}")
    Observable<ResponModel<PlainMsgResVo>> getPlainMsgDetail(@Path("id") int id);


    /**
     * 查询普通消息管理列表
     *
     * @return
     */
    @GET("sys/plainmsg/list")
    Observable<ResponModel<ListBaseResVo<PlainMsgResVo>>> getPlainMsgList();

    /**
     * 查询普通消息管理附件列表
     *
     * @return
     */
    @GET("sys/plainmsg/list-attachment/{id}")
    Observable<ResponModel<List<PlainMsgAttachmentListResVo>>> getPlainMsgAttachmentList(@Path("id") int id);

    //----------------------------------------------------政企通 项目申报 api------------------------------------------------------------

    /**
     * 批量删除 项目填报
     *
     * @return
     */
    @POST("sys/project/delete")
    Observable<ResponModel<List<MemberDetailResVo>>> deleteProject();

    /**
     * 新增 项目填报
     *
     * @return
     */
    @POST("sys/project/save")
    Observable<ResponModel<String>> saveProject(@Body AddProjectReqVo addProjectReqVo);

    /**
     * 修改 项目填报
     *
     * @return
     */
    @POST("sys/project/update")
    Observable<ResponModel<List<MemberDetailResVo>>> updateProject();

    /**
     * 查看 项目填报 详情
     *
     * @return
     */
    @GET("sys/project/info/{id}")
    Observable<ResponModel<ProjectResVo>> getProjectDetail(@Path("id") int id);

    /**
     * 通过审核
     *
     * @param auditReqVo
     * @return
     */
    @POST("sys/project/pass")
    Observable<ResponModel<String>> pass(@Body AuditReqVo auditReqVo);

    /**
     * 不通过审核
     *
     * @param auditReqVo
     * @return
     */
    @POST("sys/project/noPass")
    Observable<ResponModel<String>> noPass(@Body AuditReqVo auditReqVo);

    /**
     * 待审核项目  （政府端）
     *
     * @return
     */
    @GET("sys/project/list-pending")
    Observable<ResponModel<ListBaseResVo<ProjectResVo>>> getWaitPendingProject(@Query("page") int page);

    /**
     * 已审核项目  （政府端）
     *
     * @return
     */
    @GET("sys/project/list-handled")
    Observable<ResponModel<ListBaseResVo<ProjectResVo>>> getPendingProject(@Query("page") int page);

    /**
     * 待审核项目  （企业端）
     *
     * @return
     */
    @GET("sys/project/listMePending")
    Observable<ResponModel<ListBaseResVo<ProjectResVo>>> getListMePending(@Query("page") int page);

    /**
     * 已审核项目  （企业端）
     *
     * @return
     */
    @GET("sys/project/listMeHandled")
    Observable<ResponModel<ListBaseResVo<ProjectResVo>>> listMeHandled(@Query("page") int page);

    /**
     * 已发布企业公告  （企业端）
     *
     * @return
     */
    @GET("sys/enterprise-notice/list-audit")
    Observable<ResponModel<ListBaseResVo<EnpriceOdVo>>> getEnterpriseNoticeList(@Query("page") int page, @Query("status") int status);

    /**
     * 待审核企业公告 （企业端）
     *
     * @return
     */
    @GET("sys/enterprise-notice/list-audit")
    Observable<ResponModel<ListBaseResVo<EnpriceOdVo>>> getEnterpriseNoticeAuditList(@Query("page") int page, @Query("status") int status);

    /**
     * 我的企业列表（企业端）
     *
     * @param page
     * @param status
     * @return
     */
    @GET("sys/enterprise-notice/list-me")
    Observable<ResponModel<ListBaseResVo<EnpriceOdVo>>> getEnterpriseNoticeListComment(@Query("page") int page, @Query("status") int status);

    /**
     * 发布公告
     *
     * @param addOdReqVo
     * @return
     */
    @POST("sys/enterprise-notice/save")
    Observable<ResponModel<String>> saveOd(@Body AddOdReqVo addOdReqVo);
}
