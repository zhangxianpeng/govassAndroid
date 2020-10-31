package com.lihang.selfmvvm.vo.req;

import com.lihang.selfmvvm.vo.res.AttachmentResVo;

import java.util.List;

/**
 * created by zhangxianpeng
 * 新增千企动态
 */
public class AddDynamicReqVo {
    private List<AttachmentResVo> attachmentList;
    private String content;
    private int contentType;
    private int id;
    private String title;

    public List<AttachmentResVo> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<AttachmentResVo> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
