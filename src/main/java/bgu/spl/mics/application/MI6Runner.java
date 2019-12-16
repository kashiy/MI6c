package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Inventory;
import com.google.gson.Gson;
import java.io.IOException;
import com.google.gson.GsonBuilder;
import javax.xml.transform.Result;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Collection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) throws FileNotFoundException {
        Inventory response = null;
        //Json
      /*  Gson gson = new Gson();

        String[] anotherStr = gson.fromJson( , String[].class);

        System.out.println(anotherStr[1]);

*/
      List<String> as = new LinkedList<>();

       Gson gson = new Gson();
        FileReader br = new FileReader(
              "C:\\Users\\Yakir\\Desktop\\MI6c\\input201 - 2.json");
      ;
       // for (int i = 0; i < jsonArray.size(); i++) {
         //   JsonElement str = gson.get(1);
         //   String[] anotherStr = gson.fromJson(br, String[].class);
           // System.out.println(anotherStr[1]);
         //   System.out.println(br);
       // }
        //final JsonElement dataElement = obj.get("data");
        /*
       Gson gson = new Gson();
       response = gson.fromJson( br, Inventory.class);
        if(response != null){
            for(String h :  response.getGadgets()){
                System.out.println(h);
            }

        }
        else {
            System.out.println("good Night");
        }
*/
    }
}
