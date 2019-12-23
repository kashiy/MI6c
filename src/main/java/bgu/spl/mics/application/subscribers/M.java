package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.*;

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
	private int senderId;

	public M(String name, int senderId) {
		super(name);
		this.senderId=senderId;
		diary = Diary.getInstance();

	}

	@Override
	protected void initialize() throws InterruptedException {
		System.out.println("Listener " + getName() + " started");


		subscribeBroadcast(TickBroadcast.class, message -> {
			currentTimeTick = message.getCurrentTime();
			//System.out.println("Listener " + getName() + " got a new message from " + message.getSenderId() + "! (currentTimeTick: " + currentTimeTick + ")");
			if(currentTimeTick >= message.getTimeToTerminate()){ //Todo terminate ==

				terminate();
			}
		});


		subscribeEvent(MissionReceivedEvent.class, message -> {
			System.out.println(Thread.currentThread().getName() +" " + getName() + " got a new MissionReceivedEvent named " + message.getMission().getMissionName() + " from " + message.getSenderName() );
			Future<AgentMissionDetail> futureAgents= getSimplePublisher().sendEvent(new AgentsAvailableEvent(message.getMission().getSerialAgentsNumbers(),getName(),this.senderId));
			Future<GadgetMissionDetail> futureGadget= getSimplePublisher().sendEvent(new GadgetAvailableEvent(getName(),this.senderId,message.getMission().getGadget()));
			Future<Boolean> futureSendOrAbort;



			if((futureAgents.get()!=null) && futureGadget.get()!= null ) {
				if (futureAgents.get().getAnswer() == true && futureGadget.get().getAnswer() == true && currentTimeTick <= message.getMission().getTimeExpired() ) {
						futureSendOrAbort = getSimplePublisher().sendEvent(new SendOrAbortAgentsEvent(getName(), this.senderId, true, message.getMission().getSerialAgentsNumbers(), message.getMission().getDuration()));
						Report newReport = new Report();
						writeReport(newReport, message.getMission(), futureAgents.get().getIdMoneyPenny(), futureAgents.get().getListAgentsNames(), futureGadget.get().getRecievedInQtime());
						diary.addReport(newReport);
					System.out.println("complited :" + Thread.currentThread().getName() +" " + getName() + "Mission Event named " + message.getMission().getMissionName() + " from " + message.getSenderName() ); //TODO delete this

				} else {
					futureSendOrAbort = getSimplePublisher().sendEvent(new SendOrAbortAgentsEvent(getName(), this.senderId, false, message.getMission().getSerialAgentsNumbers(), 0));
					System.out.println("abrot but not null :" + Thread.currentThread().getName() +" " + getName() + " Mission named " + message.getMission().getMissionName() + " from " + message.getSenderName() + " futureAgents.get().getAnswer(): "+ futureAgents.get().getAnswer()+ " futureGadget.get().getAnswer(): "+ futureGadget.get().getAnswer() + " currentTimeTick <= message.getMission().getTimeExpired() " + currentTimeTick + " "+ message.getMission().getTimeExpired()); //TODO delete this
				}
			}
			else{
				futureSendOrAbort= getSimplePublisher().sendEvent(new SendOrAbortAgentsEvent(getName(),this.senderId,false,message.getMission().getSerialAgentsNumbers(),0));
				System.out.println("abrot because null :" + Thread.currentThread().getName() +" Event Handler " + getName() + " Mission named " + message.getMission().getMissionName() + " from " + message.getSenderName() ); //TODO delete this
			}


			diary.incrementTotal();

			complete(message,true);// tells intelligence it was completed
		});




		
	}

	private void writeReport(Report newReport, MissionInfo mission, int MPserialnumber , List<String> agentsNames, int Qtime){
		newReport.setAgentsNames(agentsNames);
		newReport.setAgentsSerialNumbersNumber(mission.getSerialAgentsNumbers());
		newReport.setGadgetName(mission.getGadget());
		newReport.setM(senderId);
		newReport.setMissionName(mission.getMissionName());
		newReport.setMoneypenny(MPserialnumber);
		newReport.setQTime(Qtime);
		newReport.setTimeCreated(currentTimeTick);
		newReport.setTimeIssued(mission.getTimeIssued());
	}

}
