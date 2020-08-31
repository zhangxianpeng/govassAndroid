package com.lihang.selfmvvm.vo.req;

/**
 * 政府端审核
 */
public class AuditReqVo {
    private int id;
    private String auditOpinion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion;
    }
}
