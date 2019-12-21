package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;
import bgu.spl.mics.application.passiveObjects.Diary;
import com.google.gson.*;
import bgu.spl.mics.application.subscribers.Intelligence;

import java.io.FileReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

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
                    "/Users/yakirzagron/Downloads/MI6c/src/input201 - 2.json");
            JsonElement jsonElement = new JsonParser().parse(reader);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            //inventory
            JsonArray gadgets = jsonObject.get("inventory").getAsJsonArray();
            String[] gadgetsArray = new String[gadgets.size()];
            int i =0;
            for(JsonElement element: gadgets)
            {
                gadgetsArray[i]=element.toString().substring(1,element.toString().length()-1);
                i++;
            }
            Inventory inventory = Inventory.getInstance();
            inventory.load(gadgetsArray);
            for(String s :inventory.getGadgets())
                System.out.println(s);
            inventory.printToFile("Inventory Output.json");

            //squad
            String name;
            String serialNumber;
            JsonArray squad =  jsonObject.get("squad").getAsJsonArray();
            Agent[] agents = new Agent[squad.size()];
            int u =0;
            for (JsonElement element : squad){
                name = element.getAsJsonObject().get("name").toString();
                name =name.substring(1,name.length()-1);
                serialNumber = element.getAsJsonObject().get("serialNumber").toString();
                serialNumber = serialNumber.substring(1,serialNumber.length()-1);
                Agent addAgent = new Agent();
                addAgent.setName(name);
                addAgent.setSerialNumber(serialNumber);
                addAgent.release();//all agents are available when add them to the list.
                agents[u] = addAgent;
                u++;
            }

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
                        serials.add(element3.toString());
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
            diary.printToFile("Diary Output.json");

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


                    //Todo - we need to create the threadpool/thread
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
