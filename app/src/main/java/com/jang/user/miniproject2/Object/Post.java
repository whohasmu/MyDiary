package com.jang.user.miniproject2.Object;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Post implements Serializable {
    String writerUID;
    String writerName;
    String title;
    String content;
    String imageUrl;
    long writeTime;
    Map<String, Comment> commentMap = new HashMap<>();


    public Post() {
    }

    public Post(String writerUID, String writerName, String title,String content, String imageUrl, long writeTime, Map<String, Comment> commentMap) {
        this.writerUID = writerUID;
        this.writerName = writerName;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.writeTime = writeTime;
        this.commentMap = commentMap;
    }

    public String getWriterUID() {
        return writerUID;
    }

    public void setWriterUID(String writerUID) {
        this.writerUID = writerUID;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(long writeTime) {
        this.writeTime = writeTime;
    }

    public Map<String, Comment> getCommentMap() {
        return commentMap;
    }

    public void setCommentMap(Map<String, Comment> commentMap) {
        this.commentMap = commentMap;
    }
}