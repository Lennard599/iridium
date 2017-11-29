package iridium;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Programmstart {
    public static void ProgrammStart(String Programm) {
        String b = "";
        ArrayList<String> a = Filehandeling.Lesen("iridium/Programmeconfig.txt", false);
        int pos = a.indexOf(Programm);
        for (int i = pos + 1; i < a.size(); i++) {
            if (!a.get(i).equals("ende"))
                b += a.get(i) + " ";
            else
                break;
        }
        try {
            new ProcessBuilder(b).start();
        } catch (Exception ignore) {

        }

    }

    public static void addProgramm(String[] a) {
        String b = "";
        ArrayList<String> c = Filehandeling.Lesen("iridium/Programmeconfig.txt", false);
        if (!c.contains(a[3]) || !c.contains(a[2])) {
            for (int i = 0; i < c.size(); i++)
                b += c.get(i) + " ";

            for (int i = 1; i < a.length; i++)
                b += a[i] + " ";
            b += "ende";
            SchreibenP(b);
        }
    }

    public static void addProgramm(String a) {
        boolean weiter = true;
        if (a.contains(":/")) {
            String b = "";
            ArrayList<String> c = Filehandeling.Lesen("iridium/Programmeconfig.txt", false);
            ArrayList<String> d = getProgramm();
            for (String e : d)
                if (a.contains(e)) {
                    weiter = false;
                }
            if (weiter) {
                for (String e : c)
                    b += e + " ";

                b += a + " ";
                b += "ende";
                SchreibenP(b);
            }
        }
    }

    public static void SchreibenP(String b) {
        File file = new File("iridium/Programmeconfig.txt");
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(b + System.lineSeparator());
            fw.close();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    public static ArrayList<String> getProgramm() {
        ArrayList<String> a = new ArrayList<>();
        String b = "";
        File f = new File("iridium/Programmeconfig.txt");
        try {
            Scanner sc = new Scanner(f);
            a.add(sc.next());
            while (sc.hasNext()) {
                if (!sc.hasNext("ende"))
                    b += sc.next();
                else {
                    a.add(b);
                    sc.next();
                    if (sc.hasNext())
                        a.add(sc.next());
                    b = "";
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return a;
    }
}
