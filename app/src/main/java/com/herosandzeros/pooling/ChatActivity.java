package com.herosandzeros.pooling;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.herosandzeros.pooling.adapters.ChatAdapter;
import com.herosandzeros.pooling.db.MessageDB;
import com.herosandzeros.pooling.db.UsersListDB;
import com.herosandzeros.pooling.model.ChatHistoryRequestModel;
import com.herosandzeros.pooling.model.NewMessageModel;
import com.herosandzeros.pooling.model.NewMessageResponse;
import com.herosandzeros.pooling.utils.MySharedPreference;
import com.herosandzeros.pooling.utils.SpaceItemDecoration;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class ChatActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.app_bar)
    AppBarLayout mAppBar;
    @Bind(R.id.chat_list)
    RecyclerView mChatList;
    @Bind(R.id.message)
    AppCompatEditText mMessageBar;
    @Bind(R.id.chat_linear)
    LinearLayout mChatLinear;
    @Bind(R.id.send)
    ImageView mSend;
    private EventBus mBus = EventBus.getDefault();
    private UsersListDB mUsersListDB;
    private MySharedPreference mPrefs;
    private List<MessageDB> mMessageDBList;
    private ChatAdapter mChatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        mBus.registerSticky(this);
        mPrefs = new MySharedPreference(getApplicationContext());
        if (mUsersListDB != null) {
            mToolbar.setTitle(mUsersListDB.getName());
        }
        setSupportActionBar(mToolbar);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(true);

        mChatList.setLayoutManager(manager);
        mChatList.addItemDecoration(new SpaceItemDecoration(dpToPx(1)));
        mChatAdapter = new ChatAdapter(getApplicationContext());
        mChatList.setAdapter(mChatAdapter);
        mChatAdapter.updateMessages(getMessageDBList());
        mChatAdapter.notifyDataSetChanged();
        getChatHistory();
    }

    private void getChatHistory() {
        ChatHistoryRequestModel chatHistoryRequestModel = new ChatHistoryRequestModel();
        chatHistoryRequestModel.setSenderId("" + mUsersListDB.getUserId());
        chatHistoryRequestModel.setUserId(mPrefs.getUserId());
        mBus.post(chatHistoryRequestModel);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public void onEvent(UsersListDB usersListDB) {
        mUsersListDB = usersListDB;
    }

    public void onEvent(final NewMessageResponse newMessageResponse) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mChatAdapter.updateMessages(getMessageDBList());
                mChatAdapter.notifyDataSetChanged();
                mChatList.scrollToPosition(getMessageDBList().size() - 1);
            }
        });
    }

    private List<MessageDB> getMessageDBList() {
        //return MessageDB.listAll(MessageDB.class);
        return Select.from(MessageDB.class).where(Condition.prop("sender_id")
                .eq("" + mUsersListDB.getUserId())).or(Condition.prop("receiver_id").eq("" + mUsersListDB.getUserId())).list();
        /*return MessageDB.findWithQuery(MessageDB.class, "Select * from message_db where " +
                "opponent_id = ?", "" + mUsersListDB.getUserId());*/
    }

    @OnClick(R.id.send)
    public void sendMessage() {
        if (TextUtils.isEmpty(mMessageBar.getText().toString())) {
            return;
        }

        NewMessageModel newMessageModel = new NewMessageModel();
        newMessageModel.setMessage(mMessageBar.getText().toString());
        newMessageModel.setUserId(mPrefs.getUserId());
        newMessageModel.setSenderId("" + mUsersListDB.getUserId());
        mBus.post(newMessageModel);
        mMessageBar.setText("");
        mChatAdapter.updateMessages(getMessageDBList());
        mChatAdapter.notifyDataSetChanged();
    }

}
