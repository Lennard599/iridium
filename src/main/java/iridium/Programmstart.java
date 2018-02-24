package iridium;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Programmstart {
    static HashMap<String, String> progams = new HashMap<>();

    public static void readPrograms(){
        try {
            File f = new File("./iridium/Programconfig.txt");
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
                String[] a = sc.nextLine().split("\\s+");
                String b = "";
                if (a.length > 1)
                    for (int i = 1;i < a.length;i++)
                        b += a[i] + " ";

                progams.put(a[0].trim(),b.trim());
            }
        } catch (Exception e) {
        }
    }

    public static void launch(String program) {
        try {
            new ProcessBuilder(progams.get(program)).start();
        } catch (Exception e) {
            Presentation.update(e.getMessage(),false);
        }
    }

    public static void addProgram(String[] a) {
        for (int i = 0;i <a.length/2;i = i+2) {
            progams.put(a[i], a[i + 1]);
        }
        writeProgam();
    }

    public static void addProgram(String a) {
        String[] b = a.split("\\s+");
        String bb = "";

        if (b[1].startsWith("./"))
            bb = Filehandeling.path.toAbsolutePath()+"/";
        if (!b[0].contains(a)) {
            if (b.length > 1)
                for (int i = 1;i < b.length;i++)
                    bb += b[i] + " ";

            progams.put(b[0],bb);
            writeProgam();
        }
    }

    public static void addProgram(ArrayList<String> a){
        addProgram(a.toArray(new String[a.size()]));
    }

    public static void writeProgam() {
        File file = new File("iridium/Programconfig.txt");
        ArrayList<String> c = getProgram();
        String b = "";
        try {
            FileWriter fw = new FileWriter(file);
            for (int i = 0; i < c.size(); i = i+2)
                b += c.get(i) + " " + c.get(i+1) + "\n";

            fw.write(b.trim());
            fw.close();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        readPrograms();
    }

    public static ArrayList<String> getProgram() {
        ArrayList<String> a = new ArrayList<>();
        Set<String> b = progams.keySet();

        for (String aa:b) {
            a.add(aa);
            a.add(progams.get(aa));
        }

        return a;
    }
}
