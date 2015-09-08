package com.herosandzeros.pooling.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mathan on 7/9/15.
 */
public class NewMessageModel {
    
    @SerializedName("user_id")
    private String mUserId;
    @SerializedName("sender_id")
    private String mSenderId;
    @SerializedName("message")
    private String mMessage;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getSenderId() {
        return mSenderId;
    }

    public void setSenderId(String senderId) {
        mSenderId = senderId;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }
}
