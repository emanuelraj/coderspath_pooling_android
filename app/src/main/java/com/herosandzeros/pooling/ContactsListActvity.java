package com.herosandzeros.pooling;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.herosandzeros.pooling.adapters.ContactListAdapter;
import com.herosandzeros.pooling.adapters.RecyclerItemClickListener;
import com.herosandzeros.pooling.db.UsersListDB;
import com.herosandzeros.pooling.model.GetUsersListRequest;
import com.herosandzeros.pooling.model.GetUsersListResponse;
import com.herosandzeros.pooling.model.UsersList;
import com.herosandzeros.pooling.utils.MySharedPreference;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class ContactsListActvity extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener {

    private static final String TAG = "!POOLING_";
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.app_bar)
    AppBarLayout mAppBar;
    @Bind(R.id.contacts_history_list)
    RecyclerView mContactsHistoryList;
    @Bind(R.id.new_message)
    FloatingActionButton mNewMessage;
    @Bind(R.id.rootLayout)
    FrameLayout mRootLayout;
    private EventBus mBus = EventBus.getDefault();
    private MySharedPreference mPrefs;
    private ContactListAdapter mAdapter;
    private List<UsersListDB> mUsersListDBList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);
        mToolbar.setTitle("Pooling");
        setSupportActionBar(mToolbar);
        mBus.register(this);
        mPrefs = new MySharedPreference(getApplicationContext());
        GetUsersListRequest getUsersListRequest = new GetUsersListRequest();
        getUsersListRequest.setUserId(mPrefs.getUserId());
        mBus.post(getUsersListRequest);

        mContactsHistoryList
                .addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext()
                , LinearLayoutManager.VERTICAL, false);
        mContactsHistoryList.setLayoutManager(linearLayoutManager);
        mAdapter = new ContactListAdapter(this);
        mContactsHistoryList.setAdapter(mAdapter);
        mUsersListDBList = UsersListDB.listAll(UsersListDB.class);
        updateUsersList();
    }

    @OnClick(R.id.new_message)
    public void gotoChatActivity() {
        startActivity(new Intent(getApplicationContext(), ChatActivity.class));
    }

    public void onEvent(GetUsersListResponse getUsersListResponse) {
        storeUsersList(getUsersListResponse.getUsersList());
        Timber.tag(TAG + "STATUS");
        Timber.e("Stored");
    }

    private void updateUsersList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.updateUsersList(mUsersListDBList);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void storeUsersList(List<UsersList> usersList) {
        UsersListDB.deleteAll(UsersListDB.class);
        for (UsersList users : usersList) {
            UsersListDB usersListDB = new UsersListDB();
            usersListDB.setUserid(users.getId());
            usersListDB.setName(users.getName());
            usersListDB.save();
        }
        mUsersListDBList = UsersListDB.listAll(UsersListDB.class);
        updateUsersList();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        startActivity(intent);
        mBus.postSticky(mUsersListDBList.get(position));
    }
}
