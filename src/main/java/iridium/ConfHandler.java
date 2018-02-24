package iridium;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class ConfHandler {
    private static HashMap<String,String> conf = new HashMap<>();

    public ConfHandler(){
        conf.put("shortcut:","ping");
        conf.put("stdprefix:", "Y");
        conf.put("fontstyle:", "'Menlo'");
        conf.put("name:", "Iridium");
        conf.put("backgroundcolor:", "255 255 255");
        conf.put("fontcolor:", "0 0 0");
        conf.put("prefixcolor:", "0 0 0");
        conf.put("prefixtext:", "");
        conf.put("dontshow:", "N");
        conf.put("fontsize:", "12");
        ReadConf();
    }

    public static void setConf(String key, String value){
        conf.put(key, value);
    }

    public static String getConf(String key){
            return conf.get(key);
    }

    public static void writeConf(){
        File file = new File("iridium/Iridiumconfig.txt");
        try {
            FileWriter fw = new FileWriter(file);
            Iterator<String> it = conf.keySet().iterator();
            while (it.hasNext()) {
                String c = it.next();
                fw.write(c + " " + conf.get(c) + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    public static void writeConf(String key, String value){
        conf.put(key, value);
        writeConf();
    }

    private void ReadConf(){
        File folder = new File("iridium");
        File Dateien = new File("iridium/Docs");
        File Musik = new File("iridium/Music");
        File Programmeinfo = new File("iridium/Programconfig.txt");
        File file = new File("iridium/Iridiumconfig.txt");

        if (!folder.exists())
            folder.mkdir();
        if (!Dateien.exists())
            Dateien.mkdir();
        if (!Musik.exists())
            Musik.mkdir();
        if (!file.exists())
        {
            try {
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (!Programmeinfo.exists())
        {
            try {
                Programmeinfo.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine())
            {
                String[] a = sc.nextLine().split("\\s+");
                String b = "";
                if (a.length > 1)
                    for (int i = 1;i < a.length;i++)
                        b += a[i] + " ";

                conf.put(a[0],b);
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
    }
}
