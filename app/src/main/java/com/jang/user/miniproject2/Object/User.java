package com.jang.user.miniproject2.Object;

import java.util.ArrayList;
import java.util.HashMap;

public class User {

    public String uid;
    public String google_uri;
    public String user_uri;
    public String google_name;
    public String user_name;
    public String pushToken;
    public String statusMessage;
    public HashMap<String,Object> friendUID;


    public User() {

    }

    public User(String uid, String google_uri, String google_name) {
        this.uid = uid;
        this.google_uri = google_uri;
        this.google_name = google_name;
    }

    public User(String uid, String google_uri, String user_uri, String google_name, String user_name, String pushToken) {
        this.uid = uid;
        this.google_uri = google_uri;
        this.user_uri = user_uri;
        this.google_name = google_name;
        this.user_name = user_name;
        this.pushToken = pushToken;
    }

    public User(String uid, String google_uri, String user_uri, String google_name, String user_name, String pushToken, String statusMessage, HashMap<String,Object> friendUID) {
        this.uid = uid;
        this.google_uri = google_uri;
        this.user_uri = user_uri;
        this.google_name = google_name;
        this.user_name = user_name;
        this.pushToken = pushToken;
        this.statusMessage = statusMessage;
        this.friendUID = friendUID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGoogle_uri() {
        return google_uri;
    }

    public void setGoogle_uri(String google_uri) {
        this.google_uri = google_uri;
    }

    public String getUser_uri() {
        if(this.user_uri==null){
            return this.google_uri;
        }else {
            return user_uri;
        }
    }

    public void setUser_uri(String user_uri) {
        this.user_uri = user_uri;
    }

    public String getGoogle_name() {
        return google_name;
    }

    public void setGoogle_name(String google_name) {
        this.google_name = google_name;
    }

    public String getUser_name() {
        if(this.user_name==null){
            return this.google_name;
        }else {
            return user_name;
        }
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

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public HashMap<String,Object> getFriendUID() {
        return friendUID;
    }

    public void setFriendUID(HashMap<String,Object> friendUID) {
        this.friendUID = friendUID;
    }
}
