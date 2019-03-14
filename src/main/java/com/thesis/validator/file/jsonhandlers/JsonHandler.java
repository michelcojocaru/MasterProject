package com.thesis.validator.file.jsonhandlers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Iterator;

public class JsonHandler {

    public static void writeJson(){
        JSONObject obj = new JSONObject();
        obj.put("name", "mkyong.com");
        obj.put("age", new Integer(100));

        JSONArray list = new JSONArray();
        list.add("msg 1");
        list.add("msg 2");
        list.add("msg 3");

        obj.put("messages", list);

        try (FileWriter file = new FileWriter("/Users/michel/Dropbox/VU_Master_Resources/Disertation/project/MasterProject/uploads/write_test.json")) {

            file.write(obj.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print(obj);
    }

    public static void readJson(){

        File jsonFile = new File("/Users/michel/Dropbox/VU_Master_Resources/Disertation/project/MasterProject/uploads/cargo_v1.json");

        JSONParser parser = new JSONParser();

        try {

            Object obj = parser.parse(new FileReader(jsonFile));

            JSONObject jsonObject = (JSONObject) obj;
            System.out.println(jsonObject);

            String services = (String) jsonObject.get("services");
            System.out.println(services);

            String relations = (String) jsonObject.get("relations");
            System.out.println(relations);

            // loop array
            JSONArray msg = (JSONArray) jsonObject.get("services");
            Iterator<String> iterator = msg.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
