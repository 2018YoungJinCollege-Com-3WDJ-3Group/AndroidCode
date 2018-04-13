package com.example.asd76.okonomiorgel.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by asd76 on 2018-03-16.
 */

public class User_info {

    @SerializedName("check")
    private Boolean login_check;
    private String session_id;
    private int id;
    private String name;
    private String password;
    private String email;

    public Boolean getLogin_check() {
        return login_check;
    }

    public void setLogin_check(Boolean login_check) {
        this.login_check = login_check;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
