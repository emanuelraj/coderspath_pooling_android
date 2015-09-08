package com.herosandzeros.pooling.db;

import com.orm.SugarRecord;

/**
 * Created by mathan on 6/9/15.
 */
public class UsersListDB extends SugarRecord<UsersListDB> {

    private String name;
    private int userid;

    public UsersListDB() {

    }

    public int getUserId() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
