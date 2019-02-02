package com.jang.user.miniproject2.Object;

import java.io.Serializable;

public class Comment implements Serializable{
    String writerUID;
    String content;
    long writeTime;

    public String getWriterUID() {
        return writerUID;
    }

    public void setWriterUID(String writerUID) {
        this.writerUID = writerUID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(long writeTime) {
        this.writeTime = writeTime;
    }

}
