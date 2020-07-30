package com.lihang.selfmvvm.common;


import com.lihang.selfmvvm.vo.req.LoginReqVo;
import com.lihang.selfmvvm.vo.req.RegisterReqVo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 键值对上传类
 */

public class PARAMS {
    public static String pageSize = "10";

    public static HashMap<String, Object> login(String username, String password) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        return map;
    }

    public static HashMap<String, String> gankPost(String url, String desc, String who, String type, String debug) {
        HashMap<String, String> map = new HashMap<>();
        map.put("url", url);
        map.put("desc", desc);
        map.put("who", who);
        map.put("type", type);
        map.put("debug", debug);
        return map;
    }

    public static RequestBody changeToRquestBody(String param) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), param);
    }

    public static MultipartBody.Part changeToMutiPartBody(String key, File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData(key, file.getName(), requestFile);
        return body;
    }

    //不同key 不同图片
    public static Map<String, RequestBody> manyFileToPartBody(Map<String, File> fileMap) {
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        for (String key : fileMap.keySet()) {
            requestBodyMap.put(key, RequestBody.create(MediaType.parse("multipart/form-data"), fileMap.get(key)));
        }
        return requestBodyMap;
    }

    //同一key 多张图片
    public static Map<String, RequestBody> manyFileToPartBody(String key, ArrayList<File> files) {
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        for (int i = 0; i < files.size(); i++) {
            requestBodyMap.put(key, RequestBody.create(MediaType.parse("multipart/form-data"), files.get(i)));
        }
        return requestBodyMap;
    }


    /**
     * 登录-政企通
     *
     * @param captcha
     * @param username
     * @param password
     * @param uuid
     * @return
     */
    public static LoginReqVo govassLogin(String captcha, String username, String password, String uuid) {
        LoginReqVo loginReqVo = new LoginReqVo();
        loginReqVo.setCaptcha(captcha);
        loginReqVo.setUsername(username);
        loginReqVo.setPassword(password);
        loginReqVo.setUuid(uuid);
        return loginReqVo;
    }

    /**
     * 注册-政企通
     * @param address
     * @param businessLicenseImg
     * @param businessScope
     * @param businessTerm
     * @param businessType
     * @param email
     * @param enterpriseCode
     * @param enterpriseName
     * @param headUrl
     * @param identityCard
     * @param legalRepresentative
     * @param mobile
     * @param password
     * @param registeredCapital
     * @param setUpDate
     * @param userId
     * @param username
     * @return
     */
    public static RegisterReqVo govassRegister(String address, String businessLicenseImg,
                                               String businessScope, String businessTerm,
                                               String businessType, String email,
                                               String enterpriseCode, String enterpriseName,
                                               String headUrl, String identityCard,
                                               String legalRepresentative, String mobile,
                                               String password, String registeredCapital,
                                               String setUpDate, int userId, String username) {
        RegisterReqVo registerReqVo = new RegisterReqVo();
        registerReqVo.setAddress(address);
        registerReqVo.setBusinessLicenseImg(businessLicenseImg);
        registerReqVo.setBusinessScope(businessScope);
        registerReqVo.setBusinessTerm(businessTerm);
        registerReqVo.setBusinessType(businessType);
        registerReqVo.setEmail(email);
        registerReqVo.setEnterpriseCode(enterpriseCode);
        registerReqVo.setEnterpriseName(enterpriseName);
        registerReqVo.setHeadUrl(headUrl);
        registerReqVo.setIdentityCard(identityCard);
        registerReqVo.setLegalRepresentative(legalRepresentative);
        registerReqVo.setMobile(mobile);
        registerReqVo.setPassword(password);
        registerReqVo.setRegisteredCapital(registeredCapital);
        registerReqVo.setSetUpDate(setUpDate);
        registerReqVo.setUserId(userId);
        registerReqVo.setUsername(username);
        return registerReqVo;
    }
}
