package com.lihang.selfmvvm.base;

import com.lihang.selfmvvm.bean.User;
import com.lihang.selfmvvm.bean.basebean.ParamsBuilder;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.bean.basebean.ResponModel;
import com.lihang.selfmvvm.common.PARAMS;
import com.lihang.selfmvvm.retrofitwithrxjava.uploadutils.UploadFileRequestBody;
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

import androidx.lifecycle.MutableLiveData;
import okhttp3.MultipartBody;

/**
 * ????????????api
 */
public class RepositoryImpl extends BaseModel {

    //----------------------------------------------------?????????-------------------------------------------------------------
    //??????
    public MutableLiveData<Resource<User>> login(HashMap<String, Object> map, ParamsBuilder paramsBuilder) {
        MutableLiveData<Resource<User>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().login(map), liveData, paramsBuilder);
    }

    /**
     * ????????????
     *
     * @param destDir
     * @param fileName
     * @param url
     * @return
     */
    public MutableLiveData<Resource<File>> downFile(String destDir, String fileName, String url) {
        MutableLiveData<Resource<File>> liveData = new MutableLiveData<>();
        return downLoadFile(getApiService().downloadFile(url), liveData, destDir, fileName);
    }

    /**
     * ????????????
     *
     * @param destDir
     * @param fileName
     * @param url
     * @param currentLength
     * @return
     */
    public MutableLiveData<Resource<File>> downFile(String destDir, String fileName, String url, long currentLength) {
        String range = "bytes=" + currentLength + "-";
        MutableLiveData<Resource<File>> liveData = new MutableLiveData<>();
        return downLoadFile(getApiService().downloadFile(url, range), liveData, destDir, fileName, currentLength);
    }

    /**
     * ????????????(????????????)
     *
     * @param type
     * @param file
     * @return
     */
    public MutableLiveData<Resource<UploadSingleResVo>> uploadSigleFile(String type, File file) {
        MutableLiveData<Resource<UploadSingleResVo>> liveData = new MutableLiveData<>();

        UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody(file, liveData);
        //"file"  ???key
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), uploadFileRequestBody);
        return upLoadFile(getApiService().uploadSigleFile(PARAMS.changeToRquestBody(type), body), liveData);
    }

    //??????????????????(????????????)  ??????????????????????????????????????????????????? ???????????????PictureProgressUtil?????????????????????init???????????????setProgress??????
    public MutableLiveData<Resource<List<UploadAttachmentResVo>>> uploadMultyFile(List<MultipartBody.Part> parts) {
        MutableLiveData<Resource<List<UploadAttachmentResVo>>> liveData = new MutableLiveData<>();
        return upLoadFile(getApiService().uploadMultyFile(parts), liveData);
    }

    /**
     * ??????-?????????
     *
     * @param loginReqVo
     * @return
     */
    public MutableLiveData<Resource<LoginDataVo>> govassLogin(LoginReqVo loginReqVo) {
        MutableLiveData<Resource<LoginDataVo>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().govassLogin(loginReqVo), liveData);
    }

    /**
     * ????????????-?????????
     *
     * @param token
     * @return
     */
    public MutableLiveData<Resource<String>> govassLogout(Object token) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().govassLogout(token), liveData);
    }

    /**
     * ??????-?????????
     *
     * @param registerReqVo
     * @return
     */
    public MutableLiveData<Resource<String>> register(RegisterReqVo registerReqVo) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().register(registerReqVo), liveData);
    }

    /**
     * ??????????????????
     *
     * @param plainMsgReqVo
     * @return
     */
    public MutableLiveData<Resource<String>> savePlainMsg(PlainMsgReqVo plainMsgReqVo) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().savePlainMsg(plainMsgReqVo), liveData);
    }

    /**
     * ????????????
     *
     * @param addOdReqVo
     * @return
     */
    public MutableLiveData<Resource<String>> saveOd(AddOdReqVo addOdReqVo) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().saveOd(addOdReqVo), liveData);
    }

    /**
     * ??????????????????
     *
     * @return
     */
    public MutableLiveData<Resource<UserInfoVo>> getUserInfo() {
        MutableLiveData<Resource<UserInfoVo>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getUserInfo(), liveData);
    }

    /**
     * ???????????????
     *
     * @param token
     * @return
     */
    public MutableLiveData<Resource<List<ImageDataInfo>>> getGovassBannerList(String token) {
        MutableLiveData<Resource<List<ImageDataInfo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getBannerList(token), liveData);
    }

    /**
     * ??????????????????
     *
     * @param token
     * @return
     */
    public MutableLiveData<Resource<CsDataInfoVo>> getCustomerService(String token) {
        MutableLiveData<Resource<CsDataInfoVo>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getCustomerService(token), liveData);
    }

    //----------------------------------------------------????????? ?????????------------------------------------------------------------
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

    //??????????????????
    public MutableLiveData<Resource<List<MemberDetailResVo>>> searchGovernmentUser(String userName) {
        MutableLiveData<Resource<List<MemberDetailResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().searchGovernmentUser(userName), liveData);
    }

    public MutableLiveData<Resource<List<MemberDetailResVo>>> searchEnterpriseUser(String userName) {
        MutableLiveData<Resource<List<MemberDetailResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().searchEnterpriseUser(userName), liveData);
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
     * ????????????
     *
     * @param groupIds
     * @return
     */
    public MutableLiveData<Resource<String>> deleteGroup(List<Integer> groupIds) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().deleteGroup(groupIds), liveData);
    }

    public MutableLiveData<Resource<String>> updatePwd(UpdatePwdBean bean) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().updatePwd(bean), liveData);
    }

    public MutableLiveData<Resource<String>> removeUser(RemoveUserReqVo removeUserReqVo) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().removeUser(removeUserReqVo), liveData);
    }

    public MutableLiveData<Resource<String>> addUser(RemoveUserReqVo removeUserReqVo) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().addUser(removeUserReqVo), liveData);
    }

    /**
     * ????????????????????????
     *
     * @param page
     * @param status
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<QuestionNaireItemResVo>>> getQuestiontList(int page, String status) {
        MutableLiveData<Resource<ListBaseResVo<QuestionNaireItemResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getQuestiontList(page, status), liveData);
    }

    /**
     * ????????????id??????????????????
     *
     * @param page
     * @param status
     * @param questionnaireRecordId
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<EnterpriseVo>>> getEnpriceList(int page, String status, int questionnaireRecordId) {
        MutableLiveData<Resource<ListBaseResVo<EnterpriseVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getEnpriceList(page, status, questionnaireRecordId), liveData);
    }

    /**
     * ????????????????????????
     *
     * @param page
     * @param status
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<QuestionNaireItemResVo>>> getEnQuestiontList(int page, String status) {
        MutableLiveData<Resource<ListBaseResVo<QuestionNaireItemResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getEnQuestiontList(page, status), liveData);
    }

    /**
     * ????????????????????????
     *
     * @param page
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<MsgMeResVo>>> getMsgMeList(int page) {
        MutableLiveData<Resource<ListBaseResVo<MsgMeResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getMsgMeList(page), liveData);
    }

    public MutableLiveData<Resource<ListBaseResVo<PlainMsgResVo>>> getPlainMsgList(int page) {
        MutableLiveData<Resource<ListBaseResVo<PlainMsgResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getPlainMsgList(page), liveData);
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<EnterpriseVo>>> getEnterpriseList(int page) {
        MutableLiveData<Resource<ListBaseResVo<EnterpriseVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getEnterpriseList(page), liveData);
    }

    /**
     * ????????????????????????
     *
     * @param page
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<CommunicateMsgVo>>> getFeedBackListMe(int page) {
        MutableLiveData<Resource<ListBaseResVo<CommunicateMsgVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getFeedBackListMe(page), liveData);
    }

    public MutableLiveData<Resource<ListBaseResVo<CommunicateMsgVo>>> getFeedBackList(int page) {
        MutableLiveData<Resource<ListBaseResVo<CommunicateMsgVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getFeedBackList(page), liveData);
    }

    /**
     * ??????????????????
     *
     * @param id
     * @return
     */
    public MutableLiveData<Resource<CommunicateMsgVo>> getFeedBackInfo(int id) {
        MutableLiveData<Resource<CommunicateMsgVo>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getFeedBackInfo(id), liveData);
    }

    public MutableLiveData<Resource<String>> getMsgUnRead() {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getMsgUnRead(), liveData);
    }


    public MutableLiveData<Resource<String>> fillEnterpriseInfo(FillEnterpriseReqVo enterpriseVo) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().fillEnterpriseInfo(enterpriseVo), liveData);
    }

//    public MutableLiveData<Resource<ListBaseResVo<MsgMeResVo>>> getProjectList() {
//        MutableLiveData<Resource<ListBaseResVo<MsgMeResVo>>> liveData = new MutableLiveData<>();
//        return observeGo(getApiService().getProjectList(), liveData);
//    }

    public MutableLiveData<Resource<QuestionNaireItemResVo>> getQuestionnairerecordData(int id) {
        MutableLiveData<Resource<QuestionNaireItemResVo>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getQuestionnairerecordData(id), liveData);
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
     * ???????????????????????????
     *
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<OfficialDocResVo>>> getOfficalDoc(int page) {
        MutableLiveData<Resource<ListBaseResVo<OfficialDocResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getOfficalDoc(page), liveData);
    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<OfficialDocResVo>>> getReceiveOfficalDoc(int page) {
        MutableLiveData<Resource<ListBaseResVo<OfficialDocResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getReceiveOfficalDoc(page), liveData);
    }

    /**
     * ???????????????
     *
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<NoticeResVo>>> getPublishedNotice(int page) {
        MutableLiveData<Resource<ListBaseResVo<NoticeResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getPublishedNotice(page), liveData);
    }

    /**
     * ??????????????????
     *
     * @param page
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<OfficialDocResVo>>> getPolicyList(int page) {
        MutableLiveData<Resource<ListBaseResVo<OfficialDocResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getPolicyList(page), liveData);
    }

    /**
     * ??????????????????
     *
     * @param page
     * @return
     */
    public MutableLiveData<Resource<OfficialDocResVo>> getPolicyInfo(int page) {
        MutableLiveData<Resource<OfficialDocResVo>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getPolicyInfo(page), liveData);
    }

    /**
     * ????????????  contentType 0 ??????  1  ????????????  status = 2 ?????????
     *
     * @param page
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<DynamicVo>>> getDynamicList(int page, int contentType) {
        MutableLiveData<Resource<ListBaseResVo<DynamicVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getDynamicList(page, contentType), liveData);
    }

    /**
     * ????????????????????????
     *
     * @param page
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<DynamicVo>>> getDynamicHistoryList(int page) {
        MutableLiveData<Resource<ListBaseResVo<DynamicVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getDynamicHistoryList(page), liveData);
    }

    /**
     * ??????????????????
     *
     * @param addDynamicReqVo
     * @return
     */
    public MutableLiveData<Resource<String>> saveDynamic(AddDynamicReqVo addDynamicReqVo) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().saveDynamic(addDynamicReqVo), liveData);
    }

    public MutableLiveData<Resource<String>> saveFeedBack(AddFeedBackReqVo addFeedBackReqVo) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().saveFeedBack(addFeedBackReqVo), liveData);
    }

    /**
     * ??????/????????????
     *
     * @param idReqVo
     * @return
     */
    public MutableLiveData<Resource<ResponModel<String>>> likeDynamic(IdReqVo idReqVo) {
        MutableLiveData<Resource<ResponModel<String>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().likeDynamic(idReqVo), liveData);
    }

    /**
     * ??????????????????
     *
     * @return
     */
    public MutableLiveData<Resource<VersionVo>> getNewVersion(int device) {
        MutableLiveData<Resource<VersionVo>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getNewVersion(device), liveData);
    }

    /**
     * ??????????????????
     *
     * @param searchValue
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<SearchValueResVo>>> getSearchValue(int page,int limit,String searchValue) {
        MutableLiveData<Resource<ListBaseResVo<SearchValueResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getSearchValue(page,limit,searchValue), liveData);
    }

    /**
     * ????????????
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

    /**
     * ??????????????????
     *
     * @param id
     * @return
     */
    public MutableLiveData<Resource<MsgMeResVo>> transferReadFlag(int id) {
        MutableLiveData<Resource<MsgMeResVo>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().transferReadFlag(id), liveData);
    }

    /**
     * ??????????????????
     *
     * @param id
     * @return
     */
    public MutableLiveData<Resource<PlainMsgResVo>> getPlainMsgDetail(int id) {
        MutableLiveData<Resource<PlainMsgResVo>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getPlainMsgDetail(id), liveData);
    }

    /**
     * ????????????????????????
     *
     * @param id
     * @return
     */
    public MutableLiveData<Resource<List<PlainMsgAttachmentListResVo>>> getPlainMsgAttachmentList(int id) {
        MutableLiveData<Resource<List<PlainMsgAttachmentListResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getPlainMsgAttachmentList(id), liveData);
    }

    /**
     * ??????????????? ????????????
     *
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<ProjectResVo>>> getWaitPendingProject(int page) {
        MutableLiveData<Resource<ListBaseResVo<ProjectResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getWaitPendingProject(page), liveData);
    }

    /**
     * ??????????????? ????????????
     *
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<ProjectResVo>>> getPendingProject(int page) {
        MutableLiveData<Resource<ListBaseResVo<ProjectResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getPendingProject(page), liveData);
    }

    /**
     * ??????????????? ????????????
     *
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<ProjectResVo>>> getListMePending(int page) {
        MutableLiveData<Resource<ListBaseResVo<ProjectResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getListMePending(page), liveData);
    }

    /**
     * ??????????????? ????????????
     *
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<ProjectResVo>>> getListMeHandled(int page) {
        MutableLiveData<Resource<ListBaseResVo<ProjectResVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().listMeHandled(page), liveData);
    }

    /**
     * ?????????????????????  ???????????????
     *
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<EnpriceOdVo>>> getEnterpriseNoticeList(int page, int status) {
        MutableLiveData<Resource<ListBaseResVo<EnpriceOdVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getEnterpriseNoticeList(page, status), liveData);
    }

    /**
     * ?????????????????????  ???????????????
     *
     * @return
     */
    public MutableLiveData<Resource<ListBaseResVo<EnpriceOdVo>>> getEnterpriseNoticeAuditList(int page, int status) {
        MutableLiveData<Resource<ListBaseResVo<EnpriceOdVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getEnterpriseNoticeAuditList(page, status), liveData);
    }


    public MutableLiveData<Resource<ListBaseResVo<EnpriceOdVo>>> getEnterpriseNoticeListComment(int page, int status) {
        MutableLiveData<Resource<ListBaseResVo<EnpriceOdVo>>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getEnterpriseNoticeListComment(page, status), liveData);
    }

    /**
     * ????????????
     *
     * @param id
     * @return
     */
    public MutableLiveData<Resource<ProjectResVo>> getProjectDetail(int id) {
        MutableLiveData<Resource<ProjectResVo>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().getProjectDetail(id), liveData);
    }

    /**
     * ????????????
     *
     * @return
     */
    public MutableLiveData<Resource<String>> pass(AuditReqVo auditReqVo) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().pass(auditReqVo), liveData);
    }

    /**
     * ???????????????
     *
     * @return
     */
    public MutableLiveData<Resource<String>> noPass(AuditReqVo auditReqVo) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().noPass(auditReqVo), liveData);
    }

    /**
     * ?????????????????? ????????????
     *
     * @param addProjectReqVo
     * @return
     */
    public MutableLiveData<Resource<String>> saveProject(AddProjectReqVo addProjectReqVo) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        return observeGo(getApiService().saveProject(addProjectReqVo), liveData);
    }

}
