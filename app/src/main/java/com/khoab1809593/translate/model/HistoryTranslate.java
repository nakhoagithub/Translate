package com.khoab1809593.translate.model;

public class HistoryTranslate {
    String from;
    String to;
    String source;
    String content;
    String key;

    public HistoryTranslate() {
    }

    public HistoryTranslate(String from, String to, String source, String content) {
        this.from = from;
        this.to = to;
        this.source = source;
        this.content = content;
    }

    public HistoryTranslate(String from, String to, String source, String content, String key) {
        this.from = from;
        this.to = to;
        this.source = source;
        this.content = content;
        this.key = key;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
