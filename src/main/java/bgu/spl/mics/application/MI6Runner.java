package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Inventory;
import com.google.gson.Gson;

import javax.xml.transform.Result;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) throws FileNotFoundException {
        //Json
        Gson gson = new Gson();
        Inventory inventory = gson.fromJson(new FileReader("C:\\Users\\Yakir\\Desktop\\MI6c\\input201 - 2.json"), Inventory.class);

    }
}
