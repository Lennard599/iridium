package iridium;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class ConfHandler {
    public static String FarbeH2, FarbeS2, FarbeP2;
    public static String NameB = "Iridium";
    public static boolean dontShow = false;

    public ConfHandler(){
        ReadConf();
    }
    public void ReadConf(){
        File folder = new File("iridium");
        File Dateien = new File("iridium/Dateien");
        File Musik = new File("iridium/Musik");
        File Programmeinfo = new File("iridium/Programmeconfig.txt");
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
            if (sc.hasNext())
            {
                NameB = sc.nextLine();

                FarbeH2 = sc.nextLine();

                FarbeS2 = sc.nextLine();

                FarbeP2 = sc.nextLine();

                if (sc.nextLine().equals("J"))
                    dontShow = true;

            }
            else
            {
                String a = "";
                for(int i = 3;i < Filehandeling.Lesen("iridium/Iridiumconfig.txt", false).size(); i++)
                    a += Filehandeling.Lesen("iridium/Iridiumconfig.txt", false).get(i) + " ";
                Filehandeling.Schreiben(NameB + " " + "0 " + "0 " + " 0" + "N " + " Shortcut " ,"iridium/Iridiumconfig.txt" ,false ,true);
                FarbeH2 = "0";
                FarbeS2 = "0";
                FarbeP2 = "0";
                System.out.println("no config found");
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
    }
}
