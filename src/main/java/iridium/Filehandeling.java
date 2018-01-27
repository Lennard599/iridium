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
                if (files[0].getName().contains(".mp3"))
                    Player.Spiele(files[0].getAbsolutePath());
                else if (files[0].isDirectory())
                    Presentation.update(files[0].getAbsolutePath(),false);
                else if (files[0].getName().contains(".txt"))
                    Iridium.Aus.add(new Editor(Lesen(files[0].getAbsolutePath(),false),files[0].getName(),false,files[0].getAbsolutePath(),Iridium.Aus));
                else {
                    try {
                        XWPFDocument docx = new XWPFDocument(new FileInputStream(files[0].getAbsolutePath()));
                        XWPFWordExtractor we = new XWPFWordExtractor(docx);

                        Iridium.Aus.add(new Editor(we.getText(), files[0].getName(), false, files[0].getAbsolutePath(),Iridium.Aus));

                    } catch (Exception e) {
                        Presentation.update("Error file type not supported", true);
                    }
                 }
        });
    }

    public static void Datei(String Name) {
        File folder = new File("iridium/Docs");
        File file = new File("iridium/Docs/" + Name + ".txt");
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
        Presentation.update("succesful", false);
    }

    public static void Löschen(String Name) {
        File file = new File("iridium/Docs/" + Name+".txt");

        if (file.delete())
            Presentation.update("Successful", false);
        else
            Presentation.update("error!!", false);

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
            path = "iridium/Docs/" + wo + ".txt";
        else
            path = wo;
        File file = new File(path);
        try {
            FileWriter fw = new FileWriter(file);
            if (!newl)
                fw.write(text);
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
            P = "iridium/Docs/" + wo + ".txt";
        else
            P = wo;
        try {
            File f = new File(P);
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
                Lesen.add(sc.nextLine()+"\n");
            }
        } catch (FileNotFoundException e) {
            Presentation.update("The File will be created with Save and quit", false);
        }
        return Lesen;
    }
}