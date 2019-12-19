package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private Diary diary;
	private int currentTimeTick;
	private String senderId;

	public M(String name, String senderId) {
		super(name);
		this.senderId=senderId;
		diary = Diary.getInstance();

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


		subscribeEvent(MissionReceivedEvent.class, message -> {
			Future<Boolean> futureAgents= getSimplePublisher().sendEvent(new AgentsAvailableEvent(message.getMission().getSerialAgentsNumbers(),getName(),senderId));
			Future<Boolean> futureGadget= getSimplePublisher().sendEvent(new GadgetAvailableEvent(getName(),senderId,message.getMission().getGadget()));

			if(currentTimeTick <= message.getMission().getTimeExpired()){
				if(futureAgents.get((long) message.getMission().getDuration(), TimeUnit.MILLISECONDS)== true && futureGadget.get(message.getMission().getDuration(), TimeUnit.MILLISECONDS) == true){
					//TODO Future<Boolean> futureSendOrAbort= getSimplePublisher().sendEvent(new SendOrAbortAgentsEvent(getName(),senderId,true));
					Report newReport = new Report();
					writeReport(newReport, message.getMission(), futureAgents.getMPserialnumber(), futureAgents.getAgentsNames);
					diary.addReport(newReport);
					diary.incrementTotal();

				}
				else{
					//TODO Abrot
					diary.incrementTotal();
				}
			}


			complete(message,true);// tells intelligence it was completed
		});




		
	}

	private void writeReport(Report newReport, MissionInfo mission, int MPserialnumber , List<String> agentsNames){
		//TODO ALL THE SETVALUE YUVAL
	}

}
