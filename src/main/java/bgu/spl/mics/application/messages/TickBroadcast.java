package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast{

    private int currentTime;
    private String senderId;

    public TickBroadcast(String senderId, int currentTime) {
        this.currentTime = currentTime;
        this.senderId=senderId;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public String getSenderId(){
        return senderId;
    }

}
