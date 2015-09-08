package com.herosandzeros.pooling.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mathan on 29/8/15.
 */
public class RegistrationRequestModel {

    @SerializedName("name")
    private String mName;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("password")
    private String mPassword;

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

}
