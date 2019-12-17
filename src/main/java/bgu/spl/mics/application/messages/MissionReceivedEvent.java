package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

public class MissionReceivedEvent implements Event<String>{ //TODO check if string in the event

    private String senderName;
    private MissionInfo mission;


    public MissionReceivedEvent(String senderName, MissionInfo mission) {
        this.senderName = senderName;
        this.mission=mission;
    }

    public String getSenderName() {
        return senderName;
    }

    public MissionInfo getMission(){
        return mission;
    }


}
