package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.AgentMissionDetail;
import bgu.spl.mics.application.passiveObjects.Report;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.concurrent.TimeUnit;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
	private int serialID;
	private Squad squad;
	private int currentTimeTick;

	public Moneypenny(String name, int serialID) {
		super(name);
		this.serialID=serialID;
		squad = Squad.getInstance();
		this.currentTimeTick=0;
	}

	@Override
	protected void initialize() throws InterruptedException {
		System.out.println("Listener " + getName() + " started");


		subscribeBroadcast(TickBroadcast.class, message -> {
			currentTimeTick = message.getCurrentTime();
			System.out.println("Listener " + getName() + " got a new message from " + message.getSenderId() + "! (currentTimeTick: " + currentTimeTick + ")");
			if(currentTimeTick > message.getTimeToTerminate()){
				terminate();
			}
		});

		if(serialID % 2 == 1) {
			subscribeEvent(AgentsAvailableEvent.class, message -> {
				System.out.println(getName() + " AgentsAvailableEvent " );
				Boolean agentsAvailable = squad.getAgents(message.getSerialAgentsNumbers());

				//she do the send agnets only if M tells her OR maybe release the agents if aborted

				AgentMissionDetail newDetail = new AgentMissionDetail(agentsAvailable, serialID, squad.getAgentsNames(message.getSerialAgentsNumbers()));

				complete(message, newDetail);
			});
		}
		else{
			subscribeEvent(SendOrAbortAgentsEvent.class, message -> {
				System.out.println(getName() + " SendOrAbortAgentsEvent " );
				boolean send = true;
				Boolean sendOrAbort = message.getAnswer();

				if (sendOrAbort == send){
					squad.sendAgents(message.getSerialAgentsNumbers(),message.getTime());

				}
				else{
					squad.releaseAgents(message.getSerialAgentsNumbers());

				}

				complete(message, true);
			});
		}
		
	}

}
