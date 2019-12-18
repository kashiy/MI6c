package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class SendOrAbortAgentsEvent implements Event<Boolean>{

    private String senderName;
    private String senderId;
    private Boolean answer;

    public SendOrAbortAgentsEvent( String senderName, String senderId,Boolean answer){

        this.senderName = senderName;
        this.senderId=senderId;
        this.answer=answer;
    }



}
