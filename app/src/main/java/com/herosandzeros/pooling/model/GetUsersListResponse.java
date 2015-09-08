
package com.herosandzeros.pooling.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetUsersListResponse {

    @Expose
    private Integer status;
    @Expose
    private String message;
    @SerializedName("users_list")
    @Expose
    private List<UsersList> usersList = new ArrayList<UsersList>();

    /**
     * 
     * @return
     *     The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 
     * @param message
     *     The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 
     * @return
     *     The usersList
     */
    public List<UsersList> getUsersList() {
        return usersList;
    }

    /**
     * 
     * @param usersList
     *     The users_list
     */
    public void setUsersList(List<UsersList> usersList) {
        this.usersList = usersList;
    }

}
