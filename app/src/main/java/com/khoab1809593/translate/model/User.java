package com.khoab1809593.translate.model;

public class User {
    String name;
    String email;
    String id;
    String urlPhoto;

    public User(String email, String id) {
        this.email = email;
        this.id = id;
    }

    public User(String name, String email, String id, String urlPhoto) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.urlPhoto = urlPhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }
}
