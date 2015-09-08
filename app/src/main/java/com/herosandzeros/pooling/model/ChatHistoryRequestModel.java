package com.herosandzeros.pooling.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mathan on 8/9/15.
 */
public class ChatHistoryRequestModel {
    @SerializedName("sender_id")
    public String mSenderId;

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getSenderId() {
        return mSenderId;
    }

    public void setSenderId(String senderId) {
        mSenderId = senderId;
    }

    @SerializedName("user_id")
    public String mUserId;
}
