package iridium;

import net.iharder.dnd.FileDrop;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Filehandeling {

    public static void enableFiledrop(){
        new FileDrop(Iridium.scroll, files -> {
            for (File a : files) {
                if (a.getName().contains(".mp3"))
                    Player.Spiele(a.getAbsolutePath());
                else {
                    try {
                        XWPFDocument docx = new XWPFDocument(new FileInputStream(a.getAbsolutePath()));
                        XWPFWordExtractor we = new XWPFWordExtractor(docx);

                        methods.AusgabeFeld(we.getText(), a.getName(), false, false, "", true);

                        System.out.println(we.getText());
                    } catch (Exception e) {
                        Presentation.update("Error filetype not supported", true);
                    }
                }
            }
        });
    }

    public static void Datei(String Name) {
        File folder = new File("iridium/Dateien");
        File file = new File("iridium/Dateien/" + Name + ".txt");
        if (!folder.exists()) {
            folder.mkdir();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        Presentation.update("Erfolgreich", false);
    }

    public static void Löschen(String Name) {
        File file = new File("iridium/Dateien/" + Name);

        if (file.delete())
            Presentation.update("Erfolgreich", false);
        else
            Presentation.update("Fehler!!", false);

    }

    public static File[] getFiles(String wo, boolean out) {
        String inhalt = "";
        File folder = new File(wo);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                inhalt += file.getName() + "  ";
            }
        }
        if (out)
            Presentation.update(inhalt, false);
        return listOfFiles;
    }

    public static void Schreiben(String text, String wo, boolean rela, boolean newl) {
        String path;
        String[] split = text.split("\\s+");
        if (rela)
            path = "iridium/Dateien/" + wo + ".txt";
        else
            path = wo;
        File file = new File(path);
        try {
            FileWriter fw = new FileWriter(file);
            if (!newl)
                for (String a : split)
                    fw.write(" " + a);
            else
                for (String a : split)
                    fw.write(a + System.lineSeparator());

            fw.close();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    public static ArrayList<String> Lesen(String wo, boolean rela) {
        String P;
        ArrayList<String> Lesen = new ArrayList<>();
        if (rela)
            P = "iridium/Dateien/" + wo + ".txt";
        else
            P = wo;
        try {
            File f = new File(P);
            Scanner sc = new Scanner(f);
            while (sc.hasNext()) {
                Lesen.add(sc.next());
            }
        } catch (FileNotFoundException e) {
            Presentation.update("The File will be created with Save and quit", false);
        }
        return Lesen;
    }
}
