package bgu.spl.mics;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
	Map<Class<Message>, BlockingQueue> messageMap; //register to type of message - round robin
	Map<Subscriber,BlockingQueue> subscriberMap; //missions
	private AtomicBoolean createMessageQ;

	private static class SingletonHolder{
		private static MessageBrokerImpl instance = new MessageBrokerImpl();
	}

	//need to IMPL
	private MessageBrokerImpl(){
		messageMap = new HashMap<Class<Message>, BlockingQueue>();
		subscriberMap = new HashMap<Subscriber,BlockingQueue>();


	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		return SingletonHolder.instance;
	}



	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		BlockingQueue<Subscriber> queueMessage= messageMap.get(type);
		queueMessage.add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		BlockingQueue<Subscriber> queueMessage= messageMap.get(type);
		queueMessage.add(m);

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub

	}


	/**
	 * creates the messageQueue for this type
	 * @param type The type to subscribe to
	 */
	private void createMessageQueue(Class<Message> type){
		BlockingQueue<Subscriber> subscriberBlockingQueue= new LinkedBlockingQueue<Subscriber>();
		messageMap.put(type,subscriberBlockingQueue);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		do{
			createMessageQ = false;
		}
		return null;
	}

	@Override
	public void register(Subscriber m) {
		BlockingQueue<Message> messageBlockingQueue= new LinkedBlockingQueue<Message>();
		subscriberMap.put(m,messageBlockingQueue);
	}

	@Override
	public void unregister(Subscriber m) {
		if(subscriberMap.containsKey(m)) {
			subscriberMap.remove(m); //delete the subscriberw's queue
			for (BlockingQueue messageQueue : messageMap.values()) {
				if (messageQueue.contains(m)) {
					messageQueue.remove(m);
				}
			}
		}
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		if(!subscriberMap.containsKey(m)){
			throw new IllegalStateException("The subscriber was never registered.");
		}
		try {

			BlockingQueue<Message> mQueue = subscriberMap.get(m);
			return mQueue.take();
		}
		catch(InterruptedException ignored){ //should we return null? the take will throw exception - if interrupted while waiting
			return null; //*@#$(%*@(_$%* need to fix in the messgae loop accordingly
		}
	}

	

}
