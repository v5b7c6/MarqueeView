package com.v5b7c6.android.marqueeview;

public class ItemBean {

    private int position;
    private String content;

    public ItemBean(String content, int position) {
        this.content = content;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
