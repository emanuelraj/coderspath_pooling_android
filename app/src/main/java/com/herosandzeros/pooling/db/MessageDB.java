package com.herosandzeros.pooling.db;

import com.orm.SugarRecord;

/**
 * Created by mathan on 7/9/15.
 */
public class MessageDB extends SugarRecord<MessageDB> {

    private String senderId;
    private String receiverId;
    private String message;
    private String opponentId;

    public MessageDB() {

    }

    public String getOpponentId() {
        return opponentId;
    }

    public void setOpponentId(String opponentId) {
        this.opponentId = opponentId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
