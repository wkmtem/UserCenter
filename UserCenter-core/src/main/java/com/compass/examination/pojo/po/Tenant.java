package com.compass.examination.pojo.po;

import java.io.Serializable;
import java.util.Date;

public class Tenant implements Serializable {
    private Long id;

    private String account;

    private String name;

    private String address;

    private String telephone;

    private String fax;

    private String description;

    private String type;

    private String salt;

    private Long adminUserId;

    private Boolean state;

    private Date gmtCreate;

    private Date gmtModified;

    private Long activeStamp;

    private static final long serialVersionUID = -1471588327189024128L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax == null ? null : fax.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public Long getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(Long adminUserId) {
        this.adminUserId = adminUserId;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
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

    public Long getActiveStamp() {
        return activeStamp;
    }

    public void setActiveStamp(Long activeStamp) {
        this.activeStamp = activeStamp;
    }
}