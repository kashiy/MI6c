package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Inventory;
import com.google.gson.*;

import javax.xml.transform.*;

import java.lang.reflect.*;

import java.io.FileReader;
import java.util.*;
import java.nio.file.*;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import jdk.internal.vm.compiler.collections.EconomicMap;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
        try {
            //Json

            FileReader reader = new FileReader(
                    "C/Users/yakirzagron/Downloads/MI6c/src/input201 - 2.json");
            JsonElement jsonElement = new JsonParser().parse(reader);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray gag = jsonObject.get("inventory").getAsJsonArray();



        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
