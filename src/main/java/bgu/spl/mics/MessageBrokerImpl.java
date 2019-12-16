package bgu.spl.mics;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
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
	Map<Message,Future> futureMap;
	private AtomicBoolean createMessageQ;

	private static class SingletonHolder{
		private static MessageBrokerImpl instance = new MessageBrokerImpl();
	}

	//need to IMPL
	private MessageBrokerImpl(){
		messageMap = new ConcurrentHashMap<Class<Message>, BlockingQueue>();
		subscriberMap = new ConcurrentHashMap<Subscriber,BlockingQueue>();
		futureMap = new ConcurrentHashMap<Message,Future>();


	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		return SingletonHolder.instance;
	}



	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) throws InterruptedException { //TODO check try & catch
		BlockingQueue<Subscriber> queueMessage= messageMap.get(type);
		queueMessage.put(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) throws InterruptedException { //TODO check try & catch
		BlockingQueue<Subscriber> queueMessage= messageMap.get(type);
		queueMessage.put(m);

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		futureMap.get(e).resolve(result);
	}


	/**
	 * Adds the {@link Broadcast} {@code b} to the message queues of all the
	 * Subscriber subscribed to {@code b.getClass()}.
	 * <p>
	 * @param b 	The message to added to the queues.
	 */
	@Override
	public void sendBroadcast(Broadcast b) throws InterruptedException { //TODO check try & catch
		if(!messageMap.containsKey((Class<Message>) b.getClass())) { //just for not creating a queue for every broadcast
			BlockingQueue<Subscriber> subscriberBlockingQueue = new LinkedBlockingQueue<Subscriber>();
			messageMap.putIfAbsent((Class<Message>) b.getClass(), subscriberBlockingQueue);
		}

		BlockingQueue<Subscriber> queueMessage= messageMap.get(b.getClass()); //get queue

		for (Subscriber subscriberRegistered : queueMessage) {
			subscriberMap.get(subscriberRegistered).put(b); // add the broadcast to the subscribers
		}

	}

	
	@Override
	public synchronized <T> Future<T> sendEvent(Event<T> e) throws InterruptedException { //TODO check sync for more options //TODO check try & catch
		if(!messageMap.containsKey((Class<Message>) e.getClass())) { //just for not creating a queue for every broadcast
			BlockingQueue<Subscriber> subscriberBlockingQueue = new LinkedBlockingQueue<Subscriber>();
			messageMap.putIfAbsent((Class<Message>) e.getClass(), subscriberBlockingQueue);
		}

		BlockingQueue<Subscriber> queueMessage= messageMap.get(e.getClass()); //get queue of subscribers to event<T>
		if(!queueMessage.isEmpty()) {
			Subscriber tempGetSubscriber = queueMessage.take(); //remove head of the queue
			subscriberMap.get(tempGetSubscriber).put(e);
			queueMessage.put(tempGetSubscriber); //add to end of the queue - round robin
			Future<T> newFuture = new Future<T>();
			futureMap.put(e, newFuture);
			return newFuture;
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
			subscriberMap.remove(m); //delete the subscriber's queue
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
