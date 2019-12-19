package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import java.util.List;

public class AgentsAvailableEvent implements Event<Boolean>{ //Boolean, idMoneyPenny, listAgentsName //TODO YUVAL
    private List<String> serialAgentsNumbers;
    private String senderName;
    private String senderId;

    public AgentsAvailableEvent(List<String> serialAgentsNumbers, String senderName, String senderId){
        this.serialAgentsNumbers=serialAgentsNumbers;
        this.senderName = senderName;
        this.senderId=senderId;
    }

    public List<String> getSerialAgentsNumbers() {
        return serialAgentsNumbers;
    }

}
