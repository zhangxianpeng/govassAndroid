package com.lihang.selfmvvm.base;

import com.lihang.selfmvvm.bean.BannerBean;
import com.lihang.selfmvvm.bean.User;
import com.lihang.selfmvvm.bean.basebean.HomeFatherBean;
import com.lihang.selfmvvm.bean.basebean.ParamsBuilder;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.common.PARAMS;
import com.lihang.selfmvvm.common.SystemConst;
import com.lihang.selfmvvm.retrofitwithrxjava.uploadutils.UploadFileRequestBody;
import com.lihang.selfmvvm.vo.req.AddGroupReqVo;
import com.lihang.selfmvvm.vo.req.AddProjectReqVo;
import com.lihang.selfmvvm.vo.req.LoginReqVo;
import com.lihang.selfmvvm.vo.req.RegisterReqVo;
import com.lihang.selfmvvm.vo.req.RemoveUserReqVo;
import com.lihang.selfmvvm.vo.res.CsDataInfoVo;
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
import com.lihang.selfmvvm.vo.res.PlainMsgResVo;
import com.lihang.selfmvvm.vo.res.ProjectResVo;
import com.lihang.selfmvvm.vo.res.QuestionNaireResVo;
import com.lihang.selfmvvm.vo.res.UploadAttachmentResVo;
import com.lihang.selfmvvm.vo.res.UploadSingleResVo;
import com.lihang.selfmvvm.vo.res.UserInfoVo;
import com.lihang.selfmvvm.vo.res.VersionVo;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import okhttp3.MultipartBody;

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
     *
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
    public MutableLiveData<Resource<List<UploadAttachmentResVo>>> uploadMultyFile(List<MultipartBody.Part> parts) {
        MutableLiveData<Resource<List<UploadAttachmentResVo>>> liveData = new MutableLiveData<>();
        return upLoadFile(getApiService().uploadMultyFile(parts), liveData);
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
     *
     * @param token
     * @return
     */
    public MutableLiveData<Resource<String>> govassLogout(Object token) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().govassLogout(token), liveData);
    }

    /**
     * 注册-政企通
     *
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

    public MutableLiveData<Resource<List<MemberDetailResVo>>> getGovernmentFromId(int groupId) {
        MutableLiveData<Resource<List<MemberDetailResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getGovernmentFromId(groupId), liveData);
    }

    public MutableLiveData<Resource<List<MemberDetailResVo>>> getAllEnterprise() {
        MutableLiveData<Resource<List<MemberDetailResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getAllEnterprise(), liveData);
    }


    public MutableLiveData<Resource<List<MemberDetailResVo>>> getEnterpriseFromId(int groupId) {
        MutableLiveData<Resource<List<MemberDetailResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getEnterpriseFromId(groupId), liveData);
    }

    public MutableLiveData<Resource<String>> checkGroupNameRepeat(int type, String groupName) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().checkGroupNameRepeat(type, groupName), liveData);
    }

    public MutableLiveData<Resource<String>> updateGroupName(int groupId, int type, String groupName) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().updateGroupName(groupId, type, groupName), liveData);
    }

    public MutableLiveData<Resource<String>> saveGroup(AddGroupReqVo addGroupReqVo) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().saveGroup(addGroupReqVo), liveData);
    }

    /**
     * 删除分组
     *
     * @param groupIds
     * @return
     */
    public MutableLiveData<Resource<String>> deleteGroup(List<Integer> groupIds) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().deletePlainMsg(groupIds), liveData);
    }

    /**
     * 移除用户
     *
     * @param removeUserReqVo
     * @return
     */
    public MutableLiveData<Resource<String>> removeUser(RemoveUserReqVo removeUserReqVo) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().removeUser(removeUserReqVo), liveData);
    }

    /**
     * 调查问卷（政府）
     *
     * @param page
     * @param status
     * @return
     */
    public MutableLiveData<Resource<QuestionNaireResVo>> getQuestiontList(int page, int status) {
        MutableLiveData<Resource<QuestionNaireResVo>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getQuestiontList(page, status), liveData);
    }

    /**
     * 调查问卷（企业）
     *
     * @param page
     * @param status
     * @return
     */
    public MutableLiveData<Resource<QuestionNaireResVo>> getEnQuestiontList(int page, int status) {
        MutableLiveData<Resource<QuestionNaireResVo>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getEnQuestiontList(page, status), liveData);
    }

    public MutableLiveData<Resource<ListBaseResVo<MsgMeResVo>>> getMsgMeList() {
        MutableLiveData<Resource<ListBaseResVo<MsgMeResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getMsgMeList(), liveData);
    }

    /**
     * 获取我的企业列表
     *
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<EnterpriseVo>>> getEnterpriseList(int page) {
        MutableLiveData<Resource<ListBaseResVo<EnterpriseVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getEnterpriseList(page), liveData);
    }


    public MutableLiveData<Resource<String>> getMsgUnRead() {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getMsgUnRead(), liveData);
    }


    public MutableLiveData<Resource<ListBaseResVo<MsgMeResVo>>> getProjectList() {
        MutableLiveData<Resource<ListBaseResVo<MsgMeResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getMsgMeList(), liveData);
    }

    public MutableLiveData<Resource<ListBaseResVo<PlainMsgResVo>>> getPlainMsgList() {
        MutableLiveData<Resource<ListBaseResVo<PlainMsgResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getPlainMsgList(), liveData);
    }

    public MutableLiveData<Resource<String>> deletePlainMsgList(List<Integer> idList) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().deletePlainMsg(idList), liveData);
    }

    public MutableLiveData<Resource<ListBaseResVo<OfficialDocResVo>>> getWaitPendingOfficalDoc() {
        MutableLiveData<Resource<ListBaseResVo<OfficialDocResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getWaitPendingOfficalDoc(), liveData);
    }

    /**
     * 我的公文（政府端）
     *
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<OfficialDocResVo>>> getOfficalDoc() {
        MutableLiveData<Resource<ListBaseResVo<OfficialDocResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getOfficalDoc(), liveData);
    }

    /**
     * 我的收文（企业端）
     *
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<OfficialDocResVo>>> getReceiveOfficalDoc() {
        MutableLiveData<Resource<ListBaseResVo<OfficialDocResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getReceiveOfficalDoc(), liveData);
    }

    /**
     * 已发布公告
     *
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<NoticeResVo>>> getPublishedNotice() {
        MutableLiveData<Resource<ListBaseResVo<NoticeResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getPublishedNotice(), liveData);
    }

    /**
     * 获取版本更新
     *
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<VersionVo>>> getNewVersion(int device) {
        MutableLiveData<Resource<ListBaseResVo<VersionVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getNewVersion(device), liveData);
    }


    /**
     * 公文详情
     *
     * @return
     */
    public MutableLiveData<Resource<OfficialDocResVo>> getOfficalDocDetail(int id) {
        MutableLiveData<Resource<OfficialDocResVo>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getOfficalDocDetail(id), liveData);
    }

    public MutableLiveData<Resource<MsgMeResVo>> getMsgDetail(int id) {
        MutableLiveData<Resource<MsgMeResVo>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getMsgDetail(id), liveData);
    }

    public MutableLiveData<Resource<String>> transferReadFlag(int id) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().transferReadFlag(id), liveData);
    }

    public MutableLiveData<Resource<PlainMsgResVo>> getPlainMsgDetail(int id) {
        MutableLiveData<Resource<PlainMsgResVo>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getPlainMsgDetail(id), liveData);
    }


    /**
     * 待审核项目 （政府）
     *
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<ProjectResVo>>> getWaitPendingProject(int page) {
        MutableLiveData<Resource<ListBaseResVo<ProjectResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getWaitPendingProject(page), liveData);
    }

    /**
     * 已审核项目 （政府）
     *
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<ProjectResVo>>> getPendingProject(int page) {
        MutableLiveData<Resource<ListBaseResVo<ProjectResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getPendingProject(page), liveData);
    }

    /**
     * 待审核项目 （企业）
     *
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<ProjectResVo>>> getListMePending(int page) {
        MutableLiveData<Resource<ListBaseResVo<ProjectResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getListMePending(page), liveData);
    }

    /**
     * 已审核项目 （企业）
     *
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<ProjectResVo>>> getListMeHandled(int page) {
        MutableLiveData<Resource<ListBaseResVo<ProjectResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().listMeHandled(page), liveData);
    }

    /**
     * 项目详情
     *
     * @param id
     * @return
     */
    public MutableLiveData<Resource<ProjectResVo>> getProjectDetail(int id) {
        MutableLiveData<Resource<ProjectResVo>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getProjectDetail(id), liveData);
    }

    /**
     * 新增项目申报 （企业）
     *
     * @param addProjectReqVo
     * @return
     */
    public MutableLiveData<Resource<String>> saveProject(AddProjectReqVo addProjectReqVo) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().saveProject(addProjectReqVo), liveData);
    }

}
