package com.herosandzeros.pooling;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.widget.Toast;

import com.herosandzeros.pooling.db.UsersListDB;
import com.herosandzeros.pooling.model.LoginRequestModel;
import com.herosandzeros.pooling.model.LoginResponseModel;
import com.herosandzeros.pooling.model.UsersList;
import com.herosandzeros.pooling.utils.MySharedPreference;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.email_id)
    AppCompatEditText mEmailId;
    @Bind(R.id.email_id_input_layout)
    TextInputLayout mEmailIdInputLayout;
    @Bind(R.id.choose_password)
    AppCompatEditText mChoosePassword;
    @Bind(R.id.password_input_layout)
    TextInputLayout mPasswordInputLayout;
    @Bind(R.id.login)
    AppCompatButton mLogin;
    private EventBus mEventBus = EventBus.getDefault();
    private MySharedPreference mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mEventBus.register(this);
        mPrefs = new MySharedPreference(getApplicationContext());
    }

    @OnClick(R.id.login)
    public void gotoLogin() {

        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(mEmailId.getText().toString());
        boolean isEmailValid = matcher.matches();

        if (!isEmailValid) {
            mEmailIdInputLayout.setError(getString(R.string.please_enter_email_id));
        } else {
            mEmailIdInputLayout.setError(null);
        }
        boolean isPasswordValid = (!TextUtils.isEmpty(mChoosePassword.getText().toString()) &&
                mChoosePassword.getText().toString().length() >= 4);

        if (!isPasswordValid) {
            mPasswordInputLayout.setError(getString(R.string.please_enter_password));
        } else {
            mPasswordInputLayout.setError(null);
        }

        if (isEmailValid && isPasswordValid) {
            LoginRequestModel loginRequestModel = new LoginRequestModel();
            loginRequestModel.setEmail(mEmailId.getText().toString());
            loginRequestModel.setPassword(mChoosePassword.getText().toString());
            mEventBus.post(loginRequestModel);
        }
    }

    public void onEvent(LoginResponseModel loginResponseModel) {
        if (loginResponseModel.getStatus() == 1) {
            showToast(R.string.sign_in_successful);
            mPrefs.setUserId("" + loginResponseModel.getUserId());
            Intent intent = new Intent(getApplicationContext(), ContactsListActvity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            showToast(R.string.please_check_username_or_password);
        }
    }

    private void storeUsersList(List<UsersList> usersList) {
        for (UsersList users : usersList) {
            UsersListDB usersListDB = new UsersListDB();
            usersListDB.setUserid(users.getId());
            usersListDB.setName(users.getName());
            usersListDB.save();
        }
    }

    private void showToast(final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
