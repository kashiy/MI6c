package bgu.spl.mics.application.passiveObjects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private Map<String, Agent> agentsMAP;

	private static class SingletonHolder {
		private static Squad instance = new Squad();
	}

	//need to IMPL
	private Squad(){
		agentsMAP= new HashMap<String, Agent>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		for(Agent man: agents){
			agentsMAP.put(man.getSerialNumber(), man);
		}
	}

	/**
	 * Releases agents.
	 */
	public synchronized void releaseAgents(List<String> serials){
		for(String serial: serials) {
				agentsMAP.get(serial).release();
		}
		notifyAll();
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   time ticks to sleep
	 */
	public void sendAgents(List<String> serials, int time){
		// TODO Implement this
	}

	/**
	 * acquires agents.
	 */
	private synchronized boolean acquireAgents(List<String> serials){
		for(String serial: serials) {
			if (agentsMAP.get(serial).isAvailable() == false) {
				return false;
			} else {
				agentsMAP.get(serial).acquire();
			}
		}
		return true;
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public synchronized boolean getAgents(List<String> serials) throws InterruptedException { //need to check if another sync is ok
		for(String serial: serials){
			if (agentsMAP.get(serial) == null){
				return false;
			}
		}
		while (!acquireAgents(serials)){
			releaseAgents(serials);
			wait();
		}
		return true;
	}


    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
        // TODO Implement this
	    return null;
    }

}
