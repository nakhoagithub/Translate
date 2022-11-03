package com.khoab1809593.translate.model;

import java.util.ArrayList;

public class Language {
    String code;
    String name;

    public Language(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Language() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Language> listLanguage() {
        ArrayList<Language> list = new ArrayList<>();
        //list.add(new Language("af", ""));
        //list.add(new Language("sq", ""));
        //list.add(new Language("ar", ""));
        //list.add(new Language("hy", ""));
        //list.add(new Language("az", ""));
        //list.add(new Language("eu", ""));
        //list.add(new Language("be", ""));
        //list.add(new Language("bn", ""));
        //list.add(new Language("bg", ""));
        //list.add(new Language("ca", ""));
        //list.add(new Language("hr", ""));
        //list.add(new Language("cs", ""));
        //list.add(new Language("da", ""));
        //list.add(new Language("nl", ""));
        list.add(new Language("en", "English"));
        //list.add(new Language("et", ""));
        //list.add(new Language("tl", ""));
        //list.add(new Language("fi", ""));
        list.add(new Language("fr", "French"));
        //list.add(new Language("gl", ""));
        //list.add(new Language("ka", ""));
        list.add(new Language("de", "German"));
        //list.add(new Language("el", ""));
        //list.add(new Language("gu", ""));
        //list.add(new Language("ht", ""));
        //list.add(new Language("iw", ""));
        list.add(new Language("hi", "Hindi"));
        //list.add(new Language("hu", ""));
        //list.add(new Language("is", ""));
        list.add(new Language("id", "Indonesian"));
        //list.add(new Language("ga", ""));
        list.add(new Language("it", "Italian"));
        list.add(new Language("ja", "Japanese"));
        //list.add(new Language("kn", ""));
        list.add(new Language("ko", "Korean"));
        //list.add(new Language("la", ""));
        list.add(new Language("lo", "Lao"));
        //list.add(new Language("lt", ""));
        //list.add(new Language("mk", ""));
        list.add(new Language("ms", "Malay"));
        //list.add(new Language("mt", ""));
        //list.add(new Language("no", ""));
        //list.add(new Language("fa", ""));
        //list.add(new Language("pl", ""));
        //list.add(new Language("pt", ""));
        //list.add(new Language("ro", ""));
        list.add(new Language("ru", "Russian"));
        //list.add(new Language("sr", ""));
        //list.add(new Language("sk", ""));
        //list.add(new Language("sl", ""));
        list.add(new Language("es", "Spanish"));
        //list.add(new Language("sw", ""));
        //list.add(new Language("sv", ""));
        //list.add(new Language("ta", ""));
        //list.add(new Language("te", ""));
        list.add(new Language("th", "Thai"));
        //list.add(new Language("tr", ""));
        //list.add(new Language("uk", ""));
        //list.add(new Language("ur", ""));
        list.add(new Language("vi", "Vietnamese"));
        //list.add(new Language("cy", ""));
        //list.add(new Language("yi", ""));
        list.add(new Language("zh-cn", "Chinese Simplified"));
        list.add(new Language("zh-tw", "Chinese Traditional"));
        return list;
    }
}
