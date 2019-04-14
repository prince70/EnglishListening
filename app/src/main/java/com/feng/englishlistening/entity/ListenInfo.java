package com.feng.englishlistening.entity;

/**
 * Created by prince70 on 2019/4/13.
 */
public class ListenInfo {
    private String url;
    private String title;
    private String content;

    public ListenInfo() {
    }

    public ListenInfo(String url, String title, String content) {
        this.url = url;
        this.title = title;
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}
