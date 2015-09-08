package com.herosandzeros.pooling.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mathan on 7/9/15.
 */
public class NewMessageResponse {

    @SerializedName("status")
    private String mStatus;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("sender_id")
    private String mSenderId;

    public String getSenderId() {
        return mSenderId;
    }

    public void setSenderId(String senderId) {
        mSenderId = senderId;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }
}
