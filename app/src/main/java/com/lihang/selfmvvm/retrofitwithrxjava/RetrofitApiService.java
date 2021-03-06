package com.lihang.selfmvvm.retrofitwithrxjava;


import com.lihang.selfmvvm.bean.BannerBean;
import com.lihang.selfmvvm.bean.User;
import com.lihang.selfmvvm.bean.basebean.HomeFatherBean;
import com.lihang.selfmvvm.bean.basebean.ResponModel;
import com.lihang.selfmvvm.ui.updatepwd.UpdatePwdBean;
import com.lihang.selfmvvm.vo.model.CommunicateMsgVo;
import com.lihang.selfmvvm.vo.req.AddDynamicReqVo;
import com.lihang.selfmvvm.vo.req.AddFeedBackReqVo;
import com.lihang.selfmvvm.vo.req.AddGroupReqVo;
import com.lihang.selfmvvm.vo.req.AddOdReqVo;
import com.lihang.selfmvvm.vo.req.AddProjectReqVo;
import com.lihang.selfmvvm.vo.req.AuditReqVo;
import com.lihang.selfmvvm.vo.req.FillEnterpriseReqVo;
import com.lihang.selfmvvm.vo.req.IdReqVo;
import com.lihang.selfmvvm.vo.req.LoginReqVo;
import com.lihang.selfmvvm.vo.req.PlainMsgReqVo;
import com.lihang.selfmvvm.vo.req.RegisterReqVo;
import com.lihang.selfmvvm.vo.req.RemoveUserReqVo;
import com.lihang.selfmvvm.vo.res.BaseGroupResVo;
import com.lihang.selfmvvm.vo.res.CsDataInfoVo;
import com.lihang.selfmvvm.vo.res.DynamicVo;
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
import com.lihang.selfmvvm.vo.res.SearchValueResVo;
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
 * Retrofit ???????????????????????????
 */
public interface RetrofitApiService {
    //wanAndroid????????????banner?????????
    @GET("banner/json")
    Observable<ResponModel<List<BannerBean>>> getBanner();

    //????????????,curPage????????????0??????
    @GET("article/list/{curPage}/json")
    Observable<ResponModel<HomeFatherBean>> getHomeArticles(@Path("curPage") int curPage);

    //??????????????????
    @GET("lg/collect/list/{curPage}/json")
    Observable<ResponModel<HomeFatherBean>> getCollectArticles(@Path("curPage") int curPage);

    //??????????????????
    @POST("lg/collect/{id}/json")
    Observable<ResponModel<String>> collectArticle(@Path("id") int id);

    //??????????????????
    @FormUrlEncoded
    @POST("lg/collect/add/json")
    Observable<ResponModel<String>> collectOutArticle(@Field("title") String title, @Field("author") String author, @Field("link") String link);

    //???????????? -- ??????????????????
    @POST("lg/uncollect_originId/{id}/json")
    Observable<ResponModel<String>> unCollectByHome(@Path("id") int id);

    //???????????? -- ??????????????????
    @FormUrlEncoded
    @POST("lg/uncollect/{id}/json")
    Observable<ResponModel<String>> unCollectByMe(@Path("id") int id, @Field("originId") int originId);


    //????????????
    @GET("user/logout/json")
    Observable<ResponModel<String>> loginOut();

    @POST("user/login")
    @FormUrlEncoded
    Observable<ResponModel<User>> login(@FieldMap HashMap<String, Object> map);

    //Retrofit get??????
    @GET("xiandu/category/wow")
    Observable<String> getGank(@Query("en_name") String en_name);


    //Retrofit post??????
    @POST("add2gank")
    @FormUrlEncoded
    Observable<ResponseBody> postAddGank(@FieldMap HashMap<String, String> map);

    @GET
    @Streaming
    Observable<ResponseBody> downloadFile(@Url String url, @Header("RANGE") String range);


    //----------------------------------------------------????????? api ??????---------------------------------------------------------------

    /**
     * ??????-?????????
     *
     * @param loginReqVo
     * @return
     */
    @POST("sys/login")
    Observable<ResponModel<LoginDataVo>> govassLogin(@Body LoginReqVo loginReqVo);

    /**
     * ????????????-?????????
     *
     * @param token
     * @return
     */
    @POST("sys/logout")
    Observable<ResponModel<String>> govassLogout(@Body Object token);

    /**
     * ??????-????????????
     *
     * @param registerReqVo
     * @return
     */
    @POST("sys/enterpriseuser/register")
    Observable<ResponModel<String>> register(@Body RegisterReqVo registerReqVo);

    /**
     * ??????????????????
     *
     * @param registerReqVo
     * @return
     */
    @POST("sys/enterpriseuser/updateByEnterprise")
    Observable<ResponModel<String>> updateRegister(@Body RegisterReqVo registerReqVo);

    /**
     * ??????????????????
     *
     * @return
     */
    @GET("sys/user/info")
    Observable<ResponModel<UserInfoVo>> getUserInfo();

    /**
     * ???????????????
     *
     * @param token
     * @return
     */
    @GET("sys/rotationplot/listForApp")
    Observable<ResponModel<List<ImageDataInfo>>> getBannerList(@Query("token") String token);

    /**
     * ???????????????????????????????????????
     *
     * @param id
     * @return
     */
    @GET("sys/questionnairerecorddata/info/{id}")
    Observable<ResponModel<QuestionNaireItemResVo>> getQuestionnairerecordData(@Path("id") int id);

    /**
     * ????????????(??????)
     *
     * @param page
     * @param status
     * @return
     */
    @GET("sys/questionnairerecord/list")
    Observable<ResponModel<ListBaseResVo<QuestionNaireItemResVo>>> getQuestiontList(@Query("page") int page, @Query("status") String status);

    /**
     * ????????????id??????????????????
     *
     * @param page
     * @param status
     * @return
     */
    @GET("sys/questionnairerecorddata/list")
    Observable<ResponModel<ListBaseResVo<EnterpriseVo>>> getEnpriceList(@Query("page") int page, @Query("status") String status, @Query("questionnaireRecordId") int questionnaireRecordId);

    /**
     * ????????????????????????
     *
     * @param page
     * @param status
     * @return
     */
    @GET("sys/questionnairerecord/listMe")
    Observable<ResponModel<ListBaseResVo<QuestionNaireItemResVo>>> getEnQuestiontList(@Query("page") int page, @Query("status") String status);

    /**
     * ????????????
     *
     * @param token
     * @return
     */
    @GET("sys/customerservice/default")
    Observable<ResponModel<CsDataInfoVo>> getCustomerService(@Query("token") String token);

    /**
     * ??????????????????
     */
    @POST("file/upload")
    @Multipart
    Observable<ResponModel<UploadSingleResVo>> uploadSigleFile(@Part("type") RequestBody type, @Part MultipartBody.Part file);

    /**
     * ??????????????????
     */
    @POST("file/batchUpload")
    @Multipart
    Observable<ResponModel<List<UploadAttachmentResVo>>> uploadMultyFile(@Part() List<MultipartBody.Part> parts);

    /**
     * ????????????
     */
    @GET
    @Streaming
    //10?????????@streaming???????????????oom????????????????????????
    Observable<File> downloadFile(@Url String url);

    //----------------------------------------------------????????? ????????? api------------------------------------------------------------



    /**
     * ???????????????????????????
     *
     * @param type
     * @return
     */
    @GET("sys/group/checkGroupNameRepeat")
    Observable<ResponModel<String>> checkGroupNameRepeat(@Query("type") int type, @Query("name") String name);


    /**
     * ???????????????
     *
     * @param type
     * @return
     */
    @GET("sys/group/checkGroupNameRepeat")
    Observable<ResponModel<String>> updateGroupName(@Query("groupId") int groupId, @Query("type") int type, @Query("name") String name);

    /**
     * ?????????????????????
     *
     * @param groupIds
     * @return
     */
    @POST("sys/group/delete")
    Observable<ResponModel<String>> deleteGroup(@Body List<Integer> groupIds);

    /**
     * ????????????
     *
     * @param removeUserReqVo
     * @return
     */
    @POST("sys/group/removeUser")
    Observable<ResponModel<String>> removeUser(@Body RemoveUserReqVo removeUserReqVo);

    /**
     * ???????????????????????????
     *
     * @param removeUserReqVo
     * @return
     */
    @POST("sys/group/addUser")
    Observable<ResponModel<String>> addUser(@Body RemoveUserReqVo removeUserReqVo);

    /**
     * ?????????????????????
     *
     * @param addGroupReqVo
     * @return
     */
    @POST("sys/group/save")
    Observable<ResponModel<String>> saveGroup(@Body AddGroupReqVo addGroupReqVo);

    /**
     * ?????????????????????
     *
     * @param registerReqVo
     * @return
     */
    @POST("sys/group/update")
    Observable<ResponModel<String>> updateGroup(@Body RegisterReqVo registerReqVo);


    /**
     * ??????????????????
     *
     * @param type
     * @return
     */
    @GET("sys/group/list")
    Observable<ResponModel<GroupResVo>> getGuoupList(@Query("type") int type);

    /**
     * ??????????????????
     *
     * @param type
     * @return
     */
    @GET("sys/group/list")
    Call<BaseGroupResVo> getGuoupListinCall(@Query("type") int type);

    /**
     * ???????????????????????????????????????
     *
     * @param groupId
     * @return
     */
    @GET("sys/group/info/{groupId}")
    Observable<ResponModel<GroupDetailsResVo>> getGroupAllUser(@Query("groupId") int groupId);

    /**
     * ????????????????????????
     *
     * @return
     */
    @GET("sys/user/listAllGovernment")
    Observable<ResponModel<List<MemberDetailResVo>>> getAllGovernment();

    /**
     * ??????????????????
     *
     * @param username
     * @return
     */
    @GET("sys/user/listAllGovernment")
    Observable<ResponModel<List<MemberDetailResVo>>> searchGovernmentUser(@Query("username") String username);

    /**
     * ????????????????????????
     *
     * @return
     */
    @GET("sys/enterpriseuser/listAllEnterprise")
    Observable<ResponModel<List<MemberDetailResVo>>> getAllEnterprise();

    /**
     * ??????????????????
     *
     * @param username
     * @return
     */
    @GET("sys/enterpriseuser/listAllEnterprise")
    Observable<ResponModel<List<MemberDetailResVo>>> searchEnterpriseUser(@Query("username") String username);

    /**
     * ?????????????????????????????????????????????
     *
     * @param groupId
     * @return
     */
    @GET("sys/user/listAllGovernment")
    Observable<ResponModel<List<MemberDetailResVo>>> getGovernmentFromId(@Query("groupId") int groupId);

    /**
     * ?????????????????????????????????????????????
     *
     * @param groupId
     * @return
     */
    @GET("sys/enterpriseuser/listAllEnterprise")
    Observable<ResponModel<List<MemberDetailResVo>>> getEnterpriseFromId(@Query("groupId") int groupId);

    //----------------------------------------------------????????? ?????? api------------------------------------------------------------

    /**
     * ????????????
     *
     * @param registerReqVo
     * @return
     */
    @POST("sys/officialdocument/delete")
    Observable<ResponModel<String>> deleteOfficalDoc(@Body RegisterReqVo registerReqVo);

    /**
     * ??????????????????
     *
     * @param id
     * @return
     */
    @GET("sys/officialdocument/info/{id}")
    Observable<ResponModel<OfficialDocResVo>> getOfficalDocDetail(@Path("id") int id);

    /**
     * ???????????????????????????
     *
     * @return
     */
    @GET("sys/officialdocument/list")
    Observable<ResponModel<ListBaseResVo<OfficialDocResVo>>> getOfficalDoc(@Query("page") int page);


    /**
     * ???????????????????????????
     *
     * @return
     */
    @GET("sys/officialdocument/list-receive")
    Observable<ResponModel<ListBaseResVo<OfficialDocResVo>>> getReceiveOfficalDoc(@Query("page") int page);

    /**
     * ?????????????????????
     *
     * @return
     */
    @GET("sys/officialdocument/list-pending")
    Observable<ResponModel<ListBaseResVo<OfficialDocResVo>>> getWaitPendingOfficalDoc();

    /**
     * ????????????
     *
     * @param registerReqVo
     * @return
     */
    @POST("sys/officialdocument/save")
    Observable<ResponModel<String>> saveOfficalDoc(@Body RegisterReqVo registerReqVo);

    /**
     * ????????????
     *
     * @param registerReqVo
     * @return
     */
    @POST("sys/officialdocument/update")
    Observable<ResponModel<String>> updateOfficalDoc(@Body RegisterReqVo registerReqVo);

    //----------------------------------------------------????????? ?????? api------------------------------------------------------------


    /**
     * ??????????????????
     *
     * @return
     */
    @GET("sys/msg/un-read")
    Observable<ResponModel<String>> getMsgUnRead();

    /**
     * ????????????????????????
     *
     * @return
     */
    @GET("sys/msg/list-me")
    Observable<ResponModel<ListBaseResVo<MsgMeResVo>>> getMsgMeList(@Query("page") int page);

    /**
     * ????????????????????????
     *
     * @return
     */
    @GET("sys/enterprise/list")
    Observable<ResponModel<ListBaseResVo<EnterpriseVo>>> getEnterpriseList(@Query("page") int page);


    /**
     * ??????????????????
     *
     * @return
     */
    @POST("sys/msg/readMsg/{id}")
    Observable<ResponModel<MsgMeResVo>> getMsgDetail(@Path("id") int id);


    /**
     * ??????????????????????????????
     *
     * @return
     */
    @GET("sys/msg/list-receiver")
    Observable<ResponModel<List<MemberDetailResVo>>> getMsgReceiver();


    /**
     * ???????????? ??????????????????????????????
     *
     * @return
     */
    @POST("sys/msg/read")
    Observable<ResponModel<List<MemberDetailResVo>>> read();


    /**
     * ????????????
     *
     * @return
     */
    @POST("sys/msg/readMsg/{id}")
    Observable<ResponModel<MsgMeResVo>> transferReadFlag(@Path("id") int id);

    /**
     * ??????
     *
     * @param page
     * @return
     */
    @GET("sys/notice/list-published")
    Observable<ResponModel<ListBaseResVo<NoticeResVo>>> getPublishedNotice(@Query("page") int page);

    /**
     * ???????????????????????????
     *
     * @param page
     * @return
     */
    @GET("sys/policy/list-published")
    Observable<ResponModel<ListBaseResVo<OfficialDocResVo>>> getPolicyList(@Query("page") int page);

    /**
     * ???????????????????????????
     *
     * @param id
     * @return
     */
    @GET("sys/policy/info/{id}")
    Observable<ResponModel<OfficialDocResVo>> getPolicyInfo(@Path("id") int id);

    /**
     * ????????????
     *
     * @param page
     * @return
     */
    @GET("sys/enterprise-notice/list")
    Observable<ResponModel<ListBaseResVo<DynamicVo>>> getDynamicList(@Query("page") int page, @Query("contentType") int contentType);

    /**
     * ????????????????????????
     *
     * @param page
     * @return
     */
    @GET("sys/enterprise-notice/list-me")
    Observable<ResponModel<ListBaseResVo<DynamicVo>>> getDynamicHistoryList(@Query("page") int page);

    /**
     * ??????????????????
     *
     * @param page
     * @return
     */
    @GET("sys/common-search/list")
    Observable<ResponModel<ListBaseResVo<SearchValueResVo>>> getSearchValue(@Query("page") int page, @Query("limit") int limit, @Query("query") String query);

    /**
     * ??????????????????
     *
     * @param addDynamicReqVo
     * @return
     */
    @POST("sys/enterprise-notice/save")
    Observable<ResponModel<String>> saveDynamic(@Body AddDynamicReqVo addDynamicReqVo);

    @POST("sys/user/password")
    Observable<ResponModel<String>> updatePwd(@Body UpdatePwdBean updatePwdBean);

    //??????????????????
    @POST("sys/enterprise/fill")
    Observable<ResponModel<String>> fillEnterpriseInfo(@Body FillEnterpriseReqVo enterpriseVo);

    /**
     * ????????????
     *
     * @param addFeedBackReqVo
     * @return
     */
    @POST("sys/feedback/save")
    Observable<ResponModel<String>> saveFeedBack(@Body AddFeedBackReqVo addFeedBackReqVo);

    /**
     * ??????????????????  (??????????????????)
     *
     * @param page
     * @return
     */
    @GET("sys/feedback/list-me")
    Observable<ResponModel<ListBaseResVo<CommunicateMsgVo>>> getFeedBackListMe(@Query("page") int page);

    /**
     * ????????????????????????
     * @param page
     * @return
     */
    @GET("sys/feedback/list")
    Observable<ResponModel<ListBaseResVo<CommunicateMsgVo>>> getFeedBackList(@Query("page") int page);

    /**
     * ??????????????????
     *
     * @param id
     * @return
     */
    @GET("sys/feedback/info/{id}")
    Observable<ResponModel<CommunicateMsgVo>> getFeedBackInfo(@Path("id") int id);

    /**
     * ??????/????????????
     *
     * @param idReqVo
     * @return
     */
    @POST("sys/enterprise-notice/like")
    Observable<ResponModel<String>> likeDynamic(@Body IdReqVo idReqVo);

    /**
     * ????????????
     *
     * @param device
     * @return
     */
    @GET("sys/app-version/lastest")
    Observable<ResponModel<VersionVo>> getNewVersion(@Query("device") int device);

    //----------------------------------------------------????????? ???????????? api------------------------------------------------------------

    /**
     * ????????????????????????
     *
     * @return
     */
    @POST("sys/plainmsg/delete")
    Observable<ResponModel<String>> deletePlainMsg(@Body List<Integer> idList);

    /**
     * ????????????
     *
     * @return
     */
        @POST("sys/plainmsg/save")
    Observable<ResponModel<String>> savePlainMsg(@Body PlainMsgReqVo plainMsgReqVo);

    /**
     * ????????????????????????
     *
     * @return
     */
    @POST("sys/plainmsg/update")
    Observable<ResponModel<List<MemberDetailResVo>>> updatePlainMsg();


    /**
     * ??????????????????????????????
     *
     * @return
     */
    @GET("sys/plainmsg/info/{id}")
    Observable<ResponModel<PlainMsgResVo>> getPlainMsgDetail(@Path("id") int id);


    /**
     * ??????????????????????????????
     *
     * @return
     */
    @GET("sys/plainmsg/list")
    Observable<ResponModel<ListBaseResVo<PlainMsgResVo>>> getPlainMsgList(@Query("page") int page);

    /**
     * ????????????????????????????????????
     *
     * @return
     */
    @GET("sys/plainmsg/list-attachment/{id}")
    Observable<ResponModel<List<PlainMsgAttachmentListResVo>>> getPlainMsgAttachmentList(@Path("id") int id);

    //----------------------------------------------------????????? ???????????? api------------------------------------------------------------

    /**
     * ???????????? ????????????
     *
     * @return
     */
    @POST("sys/project/delete")
    Observable<ResponModel<List<MemberDetailResVo>>> deleteProject();

    /**
     * ?????? ????????????
     *
     * @return
     */
    @POST("sys/project/save")
    Observable<ResponModel<String>> saveProject(@Body AddProjectReqVo addProjectReqVo);

    /**
     * ?????? ????????????
     *
     * @return
     */
    @POST("sys/project/update")
    Observable<ResponModel<List<MemberDetailResVo>>> updateProject();

    /**
     * ?????? ???????????? ??????
     *
     * @return
     */
    @GET("sys/project/info/{id}")
    Observable<ResponModel<ProjectResVo>> getProjectDetail(@Path("id") int id);

    /**
     * ????????????
     *
     * @param auditReqVo
     * @return
     */
    @POST("sys/project/pass")
    Observable<ResponModel<String>> pass(@Body AuditReqVo auditReqVo);

    /**
     * ???????????????
     *
     * @param auditReqVo
     * @return
     */
    @POST("sys/project/noPass")
    Observable<ResponModel<String>> noPass(@Body AuditReqVo auditReqVo);

    /**
     * ???????????????  ???????????????
     *
     * @return
     */
    @GET("sys/project/list-pending")
    Observable<ResponModel<ListBaseResVo<ProjectResVo>>> getWaitPendingProject(@Query("page") int page);

    /**
     * ???????????????  ???????????????
     *
     * @return
     */
    @GET("sys/project/list-handled")
    Observable<ResponModel<ListBaseResVo<ProjectResVo>>> getPendingProject(@Query("page") int page);

    /**
     * ???????????????  ???????????????
     *
     * @return
     */
    @GET("sys/project/listMePending")
    Observable<ResponModel<ListBaseResVo<ProjectResVo>>> getListMePending(@Query("page") int page);

    /**
     * ???????????????  ???????????????
     *
     * @return
     */
    @GET("sys/project/listMeHandled")
    Observable<ResponModel<ListBaseResVo<ProjectResVo>>> listMeHandled(@Query("page") int page);

    /**
     * ?????????????????????  ???????????????
     *
     * @return
     */
    @GET("sys/enterprise-notice/list-audit")
    Observable<ResponModel<ListBaseResVo<EnpriceOdVo>>> getEnterpriseNoticeList(@Query("page") int page, @Query("status") int status);

    /**
     * ????????????????????? ???????????????
     *
     * @return
     */
    @GET("sys/enterprise-notice/list-audit")
    Observable<ResponModel<ListBaseResVo<EnpriceOdVo>>> getEnterpriseNoticeAuditList(@Query("page") int page, @Query("status") int status);

    /**
     * ?????????????????????????????????
     *
     * @param page
     * @param status
     * @return
     */
    @GET("sys/enterprise-notice/list-me")
    Observable<ResponModel<ListBaseResVo<EnpriceOdVo>>> getEnterpriseNoticeListComment(@Query("page") int page, @Query("status") int status);

    /**
     * ????????????
     *
     * @param addOdReqVo
     * @return
     */
    @POST("sys/enterprise-notice/save")
    Observable<ResponModel<String>> saveOd(@Body AddOdReqVo addOdReqVo);
}
