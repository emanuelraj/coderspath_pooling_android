package com.herosandzeros.pooling.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by mathan on 6/9/15.
 */
public class MySharedPreference {

    private SharedPreferences mPrefs;
    private Context mContext;
    private SharedPreferences.Editor mEditor;

    public MySharedPreference(Context context) {
        mContext = context;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public String getUserId() {
        return mPrefs.getString(Constants.USER_ID, "");
    }

    public void setUserId(String userId) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(Constants.USER_ID, userId);
        editor.apply();
    }

    public String getUserName() {
        return mPrefs.getString(Constants.USER_NAME, "");
    }

    public void setUserName(String userName) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(Constants.USER_NAME, userName);
        editor.apply();
    }

}
