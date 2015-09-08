package com.herosandzeros.pooling.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mathan on 6/9/15.
 */
public class GetUsersListRequest {

    @SerializedName("user_id")
    private String mUserId;

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }


}
