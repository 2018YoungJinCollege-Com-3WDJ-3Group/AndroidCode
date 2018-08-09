package com.example.asd76.okonomiorgel.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by asd76 on 2018-03-16.
 */

public class LoginInfo {

    @SerializedName("check")
    private Boolean login_check;
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("user_name")
    private String user_name;

    private String session_id;
    private String password;
    private String email;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

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
