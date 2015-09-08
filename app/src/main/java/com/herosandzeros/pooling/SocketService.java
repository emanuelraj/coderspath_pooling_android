package com.herosandzeros.pooling;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.herosandzeros.pooling.db.MessageDB;
import com.herosandzeros.pooling.model.ChatHistoryRequestModel;
import com.herosandzeros.pooling.model.GetUsersListRequest;
import com.herosandzeros.pooling.model.GetUsersListResponse;
import com.herosandzeros.pooling.model.LoginRequestModel;
import com.herosandzeros.pooling.model.LoginResponseModel;
import com.herosandzeros.pooling.model.NewMessageModel;
import com.herosandzeros.pooling.model.NewMessageResponse;
import com.herosandzeros.pooling.model.RegistrationRequestModel;
import com.herosandzeros.pooling.model.RegistrationResponseModel;
import com.herosandzeros.pooling.utils.Constants;
import com.herosandzeros.pooling.utils.MySharedPreference;

import java.net.URISyntaxException;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

/**
 * Created by mathan on 26/8/15.
 */
public class SocketService extends Service {

    private static final String TAG = "!POOLING_";
    private Socket mSocket;
    private EventBus mEventBus = EventBus.getDefault();
    private MySharedPreference mPrefs;

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Timber.tag(TAG + "STATUS");
            Timber.e("Error");
            mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        }
    };

    private Emitter.Listener onTimeoutError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Timber.tag(TAG + "STATUS");
            Timber.e("Timeout");
            mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onTimeoutError);
        }
    };

    private Emitter.Listener onConnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Timber.tag(TAG + "STATUS");
            Timber.e("Connected");
        }
    };

    private Emitter.Listener onUsersListResponse = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Timber.tag(TAG + "RECEIVED");
            Timber.d(args[0].toString());
            Gson gson = new Gson();
            GetUsersListResponse getUsersListResponse = gson.fromJson(args[0].toString(),
                    GetUsersListResponse.class);
            mEventBus.post(getUsersListResponse);
        }
    };

    private Emitter.Listener onRegistrationResponse = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Timber.tag(TAG + "RECEIVED");
            Timber.d(args[0].toString());
            Gson gson = new Gson();
            RegistrationResponseModel responseModel = gson.fromJson(args[0].toString(),
                    RegistrationResponseModel.class);
            mEventBus.post(responseModel);
        }

    };

    private Emitter.Listener onNewMessageReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Timber.tag(TAG + "RECEIVED");
            Timber.d(args[0].toString());
            Gson gson = new Gson();
            NewMessageResponse newMessageResponse = gson.fromJson(args[0].toString(),
                    NewMessageResponse.class);
            saveMessage(newMessageResponse.getSenderId(), mPrefs.getUserId(), newMessageResponse
                    .getMessage());
            mEventBus.post(newMessageResponse);
        }
    };

    private Emitter.Listener onLoginSuccess = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Timber.tag(TAG + "RECEIVED");
            Timber.d(args[0].toString());
            Gson gson = new Gson();
            LoginResponseModel loginResponseModel = gson.fromJson(args[0].toString
                    (), LoginResponseModel.class);
            mEventBus.post(loginResponseModel);
        }
    };

    private Emitter.Listener onChatHistoryResponse = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Timber.tag(TAG + "RECEIVED");
            Timber.d(args[0].toString());
            Gson gson = new Gson();
        }
    };

    {
        try {
            mSocket = IO.socket(Constants.SOCKET_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSocket.connect();
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onTimeoutError);
        mSocket.on(Socket.EVENT_CONNECT, onConnected);
        setSocketListeners();
        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this);
        }
        return START_STICKY;
    }

    public void onEvent(RegistrationRequestModel registrationRequestModel) {
        Gson gson = new Gson();
        Timber.tag(TAG + "SENDING");
        Timber.d(gson.toJson(registrationRequestModel));
        mSocket.emit(Constants.REGISTRATION_REQUEST, gson.toJson(registrationRequestModel));
    }

    private void setSocketListeners() {
        mSocket.on(Constants.REGISTRATION_RESPONSE, onRegistrationResponse);
        mSocket.on(Constants.USERS_LIST_RESPONSE, onUsersListResponse);
        mSocket.on(Constants.NEW_MESSAGE_RECEIVED, onNewMessageReceived);
        mSocket.on(Constants.LOGIN_RESPONSE, onLoginSuccess);
        mSocket.on(Constants.CHAT_HISTORY_RESPONSE, onChatHistoryResponse);
    }

    public void onEvent(LoginRequestModel requestModel) {
        Gson gson = new Gson();
        Timber.tag(TAG + "SENDING");
        Timber.d(gson.toJson(requestModel));
        mSocket.emit(Constants.LOGIN_REQUEST, gson.toJson(requestModel));
    }

    public void onEvent(GetUsersListRequest getUsersListRequest) {
        Gson gson = new Gson();
        Timber.tag(TAG + "SENDING");
        Timber.d(gson.toJson(getUsersListRequest));
        mSocket.emit(Constants.GET_USERS_REQUEST, gson.toJson(getUsersListRequest));
    }

    public void onEvent(NewMessageModel newMessageModel) {
        Gson gson = new Gson();
        Timber.tag(TAG + "SENDING");
        Timber.d(gson.toJson(newMessageModel));
        saveMessage(mPrefs.getUserId(), newMessageModel.getSenderId(), newMessageModel.getMessage());
        mSocket.emit(Constants.NEW_MESSAGE_REQUEST, gson.toJson(newMessageModel));
    }

    public void onEvent(ChatHistoryRequestModel chatHistoryRequestModel) {
        Gson gson = new Gson();
        Timber.tag(TAG + "SENDING");
        Timber.d(gson.toJson(chatHistoryRequestModel));
        mSocket.emit(Constants.CHAT_HISTORY_RESPONSE, gson.toJson(chatHistoryRequestModel));
    }

    private void saveMessage(String senderId, String receiverId, String message) {
        MessageDB messageDB = new MessageDB();
        messageDB.setSenderId(senderId);
        messageDB.setReceiverId(receiverId);
        messageDB.setMessage(message);
        messageDB.save();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onTimeoutError);
        mEventBus.unregister(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mEventBus.register(this);
        mPrefs = new MySharedPreference(getApplicationContext());
    }


}
