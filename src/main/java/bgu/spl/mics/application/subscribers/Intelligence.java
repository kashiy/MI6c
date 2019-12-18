package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {

	private List<MissionInfo> myMissions;
	private int currentTimeTick;


	public Intelligence(String name, List<MissionInfo> myMissions) {
		super(name); //constructor of subscriber registers on the messagebroker
		this.myMissions=myMissions;
		this.currentTimeTick=0;

	}



	@Override
	protected void initialize() throws InterruptedException {
		System.out.println("Listener " + getName() + " started");


		subscribeBroadcast(TickBroadcast.class, message -> {
			currentTimeTick = message.getCurrentTime();
			System.out.println("Listener " + getName() + " got a new message from " + message.getSenderId() + "! (currentTimeTick: " + currentTimeTick + ")");
			for (MissionInfo mission: myMissions){
				if(mission.getTimeIssued()<= currentTimeTick) {
					this.getSimplePublisher().sendEvent(new MissionReceivedEvent(getName(), mission));
					myMissions.remove(mission);
				}
			}
		});

	}

}
