package com.daansander.reporter.utils;

import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class UUIDFetcher {

    private static final JSONParser jsonParser = new JSONParser();

    public static synchronized UUID getUUID(String name) {
        String output = "";
        try {
            URL api = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            BufferedReader in = new BufferedReader(new InputStreamReader(api.openStream()));
            String input;
            while ((input = in.readLine()) != null) {
                output = input;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parseString(get("id", output));
    }

    public static String get(String args, String jsonString) {
        String str = jsonString;
        String f = "";
        JSONParser parser = new JSONParser();
        ContainerFactory containerFactory = new ContainerFactory() {
            public List creatArrayContainer() {
                return new LinkedList();
            }

            public Map createObjectContainer() {
                return new LinkedHashMap();
            }

        };

        try {
            Map json = (Map) parser.parse(str, containerFactory);
            Iterator iter = json.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                if (entry.getKey().equals(args)) {
                    f = entry.getValue().toString();
                    break;
                }
            }
        } catch (ParseException pe) {
            System.out.println(pe);
        }
        return f;
    }

    public static void main(String[] args) {


//        System.out.println(getPlayerID("DaanSander").toString());
    }

    private static UUID parseString(String id) {
        try {
            return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }
}
