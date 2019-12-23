package bgu.spl.mics.application;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.passiveObjects.*;
import com.google.gson.*;
import bgu.spl.mics.application.subscribers.*;
import bgu.spl.mics.application.publishers.*;


import java.io.FileReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
        try {
            //Json
            FileReader reader = new FileReader(
                    "C:\\Users\\Yuval Kashi\\IdeaProjects\\MI6c2\\src\\input201 - 2.json"); //todo in labs check the args[0]
            JsonElement jsonElement = new JsonParser().parse(reader);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            //inventory
            JsonArray gadgets = jsonObject.get("inventory").getAsJsonArray();
            String[] gadgetsArray = new String[gadgets.size()];
            int i = 0;
            for (JsonElement element : gadgets) {
                gadgetsArray[i] = element.toString().substring(1, element.toString().length() - 1);
                i++;
            }
            Inventory.getInstance().load(gadgetsArray);

            //squad
            String name;
            String serialNumber;
            JsonArray squad = jsonObject.get("squad").getAsJsonArray();
            Agent[] agents = new Agent[squad.size()];
            int u = 0;
            for (JsonElement element : squad) {
                name = element.getAsJsonObject().get("name").toString();
                name = name.substring(1, name.length() - 1);
                serialNumber = element.getAsJsonObject().get("serialNumber").toString();
                serialNumber = serialNumber.substring(1, serialNumber.length() - 1);
                Agent addAgent = new Agent();
                addAgent.setName(name);
                addAgent.setSerialNumber(serialNumber);
                addAgent.release();//all agents are available when add them to the list.
                agents[u] = addAgent;
                u++;
            }
            Squad.getInstance().load(agents);


            //services
            int M =  jsonObject.get("services").getAsJsonObject().get("M").getAsInt();
            int time =  jsonObject.get("services").getAsJsonObject().get("time").getAsInt(); //TODO put in time service
            int Moneypenny  = jsonObject.get("services").getAsJsonObject().get("Moneypenny").getAsInt();
            JsonArray intelligenceMissions =  jsonObject.get("services").getAsJsonObject().get("intelligence").getAsJsonArray();
            List<Intelligence> intelligencesList = new LinkedList<>();
            int SerialId = 1;
            String nameIntelligence = "Intelligence";
            for (JsonElement element1 : intelligenceMissions ) {
                JsonArray mission = element1.getAsJsonObject().get("missions").getAsJsonArray(); //TODO two intelligence - each one will get the missions
                List<MissionInfo> intelligenceMissionsList = new LinkedList<>();
                for (JsonElement element2 : mission) {
                    JsonArray serialAgentsNumbers = element2.getAsJsonObject().get("serialAgentsNumbers").getAsJsonArray();
                    List<String> serials = new LinkedList<>();
                    for (JsonElement element3 : serialAgentsNumbers) {
                        serials.add(element3.toString().substring(1, element3.toString().length() - 1));
                    }
                    int duration = element2.getAsJsonObject().get("duration").getAsInt();
                    int timeExpired = element2.getAsJsonObject().get("timeExpired").getAsInt();
                    int timeIssued = element2.getAsJsonObject().get("timeIssued").getAsInt();
                    String missionName = element2.getAsJsonObject().get("missionName").getAsString();
                    // missionName =missionName.substring(0,missionName.length()-1);
                    String gadget = element2.getAsJsonObject().get("gadget").getAsString();
                    // gadget= gadget.substring(1,gadget.length()-1);
                    MissionInfo missionCreated = new MissionInfo();
                    missionCreated.setGadget(gadget);
                    missionCreated.setDuration(duration);
                    missionCreated.setMissionName(missionName);
                    missionCreated.setSerialAgentsNumbers(serials);
                    missionCreated.setTimeExpired(timeExpired);
                    missionCreated.setTimeIssued(timeIssued);
                    intelligenceMissionsList.add(missionCreated);

                }
                String SerialIdString = Integer.toString(SerialId);
                String nameIntelligenceWithSerial = nameIntelligence + SerialIdString;
                Intelligence intelligence = new Intelligence(nameIntelligenceWithSerial, intelligenceMissionsList, SerialIdString);
                intelligencesList.add(intelligence);
                SerialId++;
            }

/*
            //create report
             Report newReport = new Report();
             String missionName = "Check1";
             int mSerialNumber = 1;
             int moneyPennySerialNumber = 2;
             List<String> agentsSerialNumbers = new LinkedList<>();
             agentsSerialNumbers.add("001Check");
             List<String> agentsNames = new LinkedList<>();
             agentsNames.add("001 Serial");
             String gadgetName  = "Check1gad";
             int timeIssued = 3;
             int QTime = 4 ;
             int timeCreated = 5 ;
            newReport.setAgentsNames(agentsNames);
            newReport.setAgentsSerialNumbersNumber(agentsSerialNumbers);
            newReport.setGadgetName(gadgetName);
            newReport.setM(mSerialNumber);
            newReport.setMissionName(missionName);
            newReport.setMoneypenny(moneyPennySerialNumber);
            newReport.setQTime(QTime);
            newReport.setTimeCreated(timeCreated);
            newReport.setTimeIssued(timeIssued);
            Diary diary = Diary.getInstance();
            diary.getReports().add(newReport);
            diary.incrementTotal();
            diary.printToFile("diaryOutputFile.json");
*/
            //Checks

            /*
            for(Agent ag: agents){
                System.out.println(ag.getName());
                System.out.println(ag.getSerialNumber());

            }

            for(Intelligence int1 : intelligencesList){
                 System.out.println(int1.getName());

               for (MissionInfo miss :int1.getMyMissions()) {
                   System.out.println(miss.getMissionName());
                   System.out.println(miss.getGadget());
                   System.out.println(miss.getTimeIssued());
               }
               }
               */
            List<Thread> threadsList = new LinkedList<>();
            Semaphore sem= new Semaphore(1);
            sem.acquire();

            for (int k = 1 ; k<= M; k++) {// todo only m1 in use.
                M newM = new M("M"+k, k);
                Thread newThread= new Thread(newM);
                threadsList.add(newThread);
                newThread.start();
            }

            for (int j = 1 ; j<= Moneypenny; j++) {
                Moneypenny newMoneyPenny= new Moneypenny("Mp"+j,j);
                Thread newThread= new Thread(newMoneyPenny);
                threadsList.add(newThread);
                newThread.start();
            }
            Q newQ =new Q("Q", 1);
            Thread newThreadQ= new Thread(newQ);
            threadsList.add(newThreadQ);
            newThreadQ.start();


           for(Intelligence intelligence: intelligencesList){
                Thread newThread= new Thread(intelligence);
                threadsList.add(newThread);
                newThread.start();

            }

          /*
            //Check with one intelligencesList
            Thread newThread= new Thread(intelligencesList.get(1));
            threadsList.add(newThread);
            newThread.start();
            */

            TimeService newTimeService= new TimeService("TimeService", time);
            Thread newThreadTime= new Thread(newTimeService);
            threadsList.add(newThreadTime);
            newThreadTime.start();
            System.out.println("time tick started");
            for(Thread thread: threadsList) {
                thread.join();
            }
            System.out.print(threadsList.size());
            sem.release();


            Diary.getInstance().printToFile("diaryOutputFile.json");
            Inventory.getInstance().printToFile("inventoryOutputFile.json");


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
