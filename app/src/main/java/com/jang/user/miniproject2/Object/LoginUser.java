package com.jang.user.miniproject2.Object;

public class LoginUser {
    public String userId;
    public String user_uri;
    public String user_name;
    public String pushToken;
    public String comment;

    public LoginUser() {
    }

    public LoginUser(String userId, String user_uri, String user_name) {
        this.userId = userId;
        this.user_uri = user_uri;
        this.user_name = user_name;
        this.pushToken = pushToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUser_uri() {
        return user_uri;
    }

    public void setUser_uri(String user_uri) {
        this.user_uri = user_uri;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }
}
