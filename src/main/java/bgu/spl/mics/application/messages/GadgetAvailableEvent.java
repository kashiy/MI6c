package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

    public class GadgetAvailableEvent implements Event<Boolean>{
        private String gadget;
        private String senderName;
        private String senderId;


        public  GadgetAvailableEvent(String senderName, String senderId, String gadget) {
            this.senderName = senderName;
            this.senderId = senderId;
            this.gadget = gadget;

        }

        public String getGadget() {
            return gadget;
        }
}
