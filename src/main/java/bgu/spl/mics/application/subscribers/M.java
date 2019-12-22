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
			System.out.println("Listener " + getName() + " got a new message from " + message.getSenderId() + "! (currentTimeTick: " + currentTimeTick + ")");
			if(currentTimeTick > message.getTimeToTerminate()){

				terminate();
			}
		});


		subscribeEvent(MissionReceivedEvent.class, message -> {
			System.out.println(getName() + " MissionReceivedEvent " );
			Future<AgentMissionDetail> futureAgents= getSimplePublisher().sendEvent(new AgentsAvailableEvent(message.getMission().getSerialAgentsNumbers(),getName(),senderId));
			Future<GadgetMissionDetail> futureGadget= getSimplePublisher().sendEvent(new GadgetAvailableEvent(getName(),senderId,message.getMission().getGadget()));
			Future<Boolean> futureSendOrAbort;


			if(futureAgents.get((long) message.getMission().getDuration(), TimeUnit.MILLISECONDS).getAnswer()== true && futureGadget.get(message.getMission().getDuration(), TimeUnit.MILLISECONDS).getAnswer() == true){
				if(currentTimeTick <= message.getMission().getTimeExpired()){
					futureSendOrAbort= getSimplePublisher().sendEvent(new SendOrAbortAgentsEvent(getName(),senderId,true,message.getMission().getSerialAgentsNumbers(),message.getMission().getDuration()));
					Report newReport = new Report();
					writeReport(newReport, message.getMission(), futureAgents.get().getIdMoneyPenny(), futureAgents.get().getListAgentsNames(), futureGadget.get().getRecievedInQtime());
					diary.addReport(newReport);
				}
				else{
					futureSendOrAbort= getSimplePublisher().sendEvent(new SendOrAbortAgentsEvent(getName(),senderId,false,message.getMission().getSerialAgentsNumbers(),0));

				}
			}
			else{
				futureSendOrAbort= getSimplePublisher().sendEvent(new SendOrAbortAgentsEvent(getName(),senderId,false,message.getMission().getSerialAgentsNumbers(),0));

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
