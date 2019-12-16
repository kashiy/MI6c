package bgu.spl.mics.application.passiveObjects;

import java.util.List;

/**
 * Passive data-object representing a delivery vehicle of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Report {

	String missionNAme;
	int mSerialNumber;
	int moneyPennySerialNumber;
	List<String> agentsSerialNumbers;
	List<String> agentsNAmes;
	String gadgetNAme;
	int timeISsued;
	int QTime;
	int timeCreateD;


	/**
     * Retrieves the mission name.
     */
	public String getMissionName() {

		return missionNAme;
	}

	/**
	 * Sets the mission name.
	 */
	public void setMissionName(String missionName) {
		missionNAme=missionName;
	}

	/**
	 * Retrieves the M's id.
	 */
	public int getM() {

		return mSerialNumber;
	}

	/**
	 * Sets the M's id.
	 */
	public void setM(int m) {
		mSerialNumber=m;
	}

	/**
	 * Retrieves the Moneypenny's id.
	 */
	public int getMoneypenny() {

		return moneyPennySerialNumber;
	}

	/**
	 * Sets the Moneypenny's id.
	 */
	public void setMoneypenny(int moneypenny) {
		moneyPennySerialNumber=moneypenny;
	}

	/**
	 * Retrieves the serial numbers of the agents.
	 * <p>
	 * @return The serial numbers of the agents.
	 */
	public List<String> getAgentsSerialNumbersNumber() {

		return agentsSerialNumbers;
	}

	/**
	 * Sets the serial numbers of the agents.
	 */
	public void setAgentsSerialNumbersNumber(List<String> agentsSerialNumbersNumber) {
		agentsSerialNumbers=agentsSerialNumbersNumber;
	}

	/**
	 * Retrieves the agents names.
	 * <p>
	 * @return The agents names.
	 */
	public List<String> getAgentsNames() {

		return agentsNAmes;
	}

	/**
	 * Sets the agents names.
	 */
	public void setAgentsNames(List<String> agentsNames) {
		agentsNAmes=agentsNames;
	}

	/**
	 * Retrieves the name of the gadget.
	 * <p>
	 * @return the name of the gadget.
	 */
	public String getGadgetName() {

		return gadgetNAme;
	}

	/**
	 * Sets the name of the gadget.
	 */
	public void setGadgetName(String gadgetName) {
		gadgetNAme=gadgetName;
	}

	/**
	 * Retrieves the time-tick in which Q Received the GadgetAvailableEvent for that mission.
	 */
	public int getQTime() {

		return QTime;
	}

	/**
	 * Sets the time-tick in which Q Received the GadgetAvailableEvent for that mission.
	 */
	public void setQTime(int qTime) {
		QTime=qTime;
	}

	/**
	 * Retrieves the time when the mission was sent by an Intelligence Publisher.
	 */
	public int getTimeIssued() {

		return timeISsued;
	}

	/**
	 * Sets the time when the mission was sent by an Intelligence Publisher.
	 */
	public void setTimeIssued(int timeIssued) {
		timeISsued= timeIssued;
	}

	/**
	 * Retrieves the time-tick when the report has been created.
	 */
	public int getTimeCreated() {

		return timeCreateD;
	}

	/**
	 * Sets the time-tick when the report has been created.
	 */
	public void setTimeCreated(int timeCreated) {
		timeCreateD=timeCreated;
	}
}
