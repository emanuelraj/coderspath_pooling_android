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
import com.herosandzeros.pooling.model.RegistrationRequestModel;
import com.herosandzeros.pooling.model.RegistrationResponseModel;
import com.herosandzeros.pooling.model.UsersList;
import com.herosandzeros.pooling.utils.MySharedPreference;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;
import rx.functions.Func3;

public class RegistrationActivity extends AppCompatActivity {

    @Bind(R.id.name)
    AppCompatEditText mName;
    @Bind(R.id.email_id)
    AppCompatEditText mEmailId;
    @Bind(R.id.choose_password)
    AppCompatEditText mChoosePassword;
    @Bind(R.id.sign_up)
    AppCompatButton mSignUp;
    @Bind(R.id.name_input_layout)
    TextInputLayout mNameInputLayout;
    @Bind(R.id.email_id_input_layout)
    TextInputLayout mEmailIdInputLayout;
    @Bind(R.id.password_input_layout)
    TextInputLayout mPasswordInputLayout;
    private Observable<OnTextChangeEvent> mNameChangeObservable;
    private Observable<OnTextChangeEvent> mEmailChangeObservable;
    private Observable<OnTextChangeEvent> mPasswordChangeObservable;
    private Subscription mSubscription = null;
    private boolean isValid = false;
    private EventBus mEventBus = EventBus.getDefault();
    private MySharedPreference mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        mEventBus.register(this);
        mPrefs = new MySharedPreference(getApplicationContext());

        if (!TextUtils.isEmpty(mPrefs.getUserId())) {
            startActivity(new Intent(getApplicationContext(), ContactsListActvity.class));
            finish();
        }

        mNameChangeObservable = WidgetObservable.text(mName);
        mEmailChangeObservable = WidgetObservable.text(mEmailId);
        mPasswordChangeObservable = WidgetObservable.text(mChoosePassword);
        checkValidation();
    }

    @OnClick(R.id.login)
    public void gotoLoginActivity() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    @OnClick(R.id.sign_up)
    public void signUp() {
        if (isValid) {
            RegistrationRequestModel registrationModel = new RegistrationRequestModel();
            registrationModel.setName(mName.getText().toString());
            registrationModel.setEmail(mEmailId.getText().toString());
            registrationModel.setPassword(mChoosePassword.getText().toString());
            mEventBus.post(registrationModel);
        }
    }

    private void checkValidation() {
        mSubscription = Observable.combineLatest(mNameChangeObservable, mEmailChangeObservable,
                mPasswordChangeObservable, new Func3<OnTextChangeEvent, OnTextChangeEvent,
                        OnTextChangeEvent, Boolean>() {
                    @Override
                    public Boolean call(OnTextChangeEvent onNameTextChangeEvent, OnTextChangeEvent
                            onEmailTextChangeEvent, OnTextChangeEvent onPasswordTextChangeEvent) {
                        boolean isNameValid = !TextUtils.isEmpty(onNameTextChangeEvent.text()
                                .toString());

                        if (!isNameValid) {
                            mNameInputLayout.setError(getString(R.string.please_enter_name));
                        } else {
                            mNameInputLayout.setError(null);
                        }

                        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
                        Matcher matcher = pattern.matcher(onEmailTextChangeEvent.text().toString());
                        boolean isEmailValid = matcher.matches();

                        if (!isEmailValid) {
                            mEmailIdInputLayout.setError(getString(R.string.please_enter_email_id));
                        } else {
                            mEmailIdInputLayout.setError(null);
                        }
                        boolean isPasswordValid = (!TextUtils.isEmpty(onPasswordTextChangeEvent
                                .text().toString()) && onPasswordTextChangeEvent.text().toString
                                ().length() >= 4);

                        if (!isPasswordValid) {
                            mPasswordInputLayout.setError(getString(R.string.please_enter_password));
                        } else {
                            mPasswordInputLayout.setError(null);
                        }

                        return isNameValid && isEmailValid && isPasswordValid;
                    }
                })
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean value) {
                        isValid = value;
                    }
                });
    }

    public void onEvent(RegistrationResponseModel registrationResponseModel) {
        if (registrationResponseModel.getStatus() == 1) {
            showToast(R.string.registration_successful);
            mPrefs.setUserId("" + registrationResponseModel.getUserId());
            mPrefs.setUserName(mName.getText().toString());
            storeUsersList(registrationResponseModel.getUsersList());
            startActivity(new Intent(getApplicationContext(), ContactsListActvity.class));
            finish();
        } else {
            showToast(R.string.already_registered);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

}
