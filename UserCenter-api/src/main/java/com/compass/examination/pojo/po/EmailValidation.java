package com.compass.examination.pojo.po;

import java.io.Serializable;
import java.util.Date;

public class EmailValidation implements Serializable {
    private Long id;

    private Long tenantId;

    private String activeCode;

    private String activeMd5;

    private Long expireMillis;

    private Date gmtCreate;

    private Date gmtModified;

    private static final long serialVersionUID = -7216585489193968832L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode == null ? null : activeCode.trim();
    }

    public String getActiveMd5() {
        return activeMd5;
    }

    public void setActiveMd5(String activeMd5) {
        this.activeMd5 = activeMd5 == null ? null : activeMd5.trim();
    }

    public Long getExpireMillis() {
        return expireMillis;
    }

    public void setExpireMillis(Long expireMillis) {
        this.expireMillis = expireMillis;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}