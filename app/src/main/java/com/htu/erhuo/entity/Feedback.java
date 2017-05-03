package com.htu.erhuo.entity;

/**
 * Description
 * Created by yzw on 2017/5/3.
 */

public class Feedback {

    private Integer id;// 主键

    private Integer fbId;// 编号

    private String fbType;// 类型

    private String fbContent;// 内容

    private String userId;// 反馈者

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFbId() {
        return fbId;
    }

    public void setFbId(Integer fbId) {
        this.fbId = fbId;
    }

    public String getFbType() {
        return fbType;
    }

    public void setFbType(String fbType) {
        this.fbType = fbType == null ? null : fbType.trim();
    }

    public String getFbContent() {
        return fbContent;
    }

    public void setFbContent(String fbContent) {
        this.fbContent = fbContent == null ? null : fbContent.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

}
