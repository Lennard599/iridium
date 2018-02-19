package iridium;

import net.iharder.dnd.FileDrop;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Filehandeling {
    public static Path path = Paths.get(".");

    public static Path toPath(String a){
        Path p = path;
        if (a.trim().startsWith("./"))
            p = p.resolve(a);
        else
        if (a.indexOf("/") == -1) {
            p = p.resolve(a);
        }
        else {
            p = Paths.get(a);
        }

        return p;
    }

    public static void enableFiledrop(){
        try {
            path = path.toRealPath(LinkOption.NOFOLLOW_LINKS);
        } catch (java.io.IOException e) {}
        new FileDrop(Iridium.scroll, files -> {
            Presentation.update(Presentation.out_l+" "+files[0].getAbsolutePath(),false);
        });
    }

    public static void create(String name) {
        Path p = path;
        if (name.trim().startsWith("./"))
            p = p.resolve(name);
        else
            p = Paths.get(name);
        try {
            p = p.toRealPath(LinkOption.NOFOLLOW_LINKS);
        } catch (IOException e){
            try {
                if (name.contains("docx")) {
                    XWPFDocument document = new XWPFDocument();
                    FileOutputStream out = new FileOutputStream(p.toFile());
                    document.write(out);
                    out.close();
                }
                else
                    p.toFile().createNewFile();
            } catch (IOException ex){
                Presentation.update("something went wrong", false);
            }
        }
        Presentation.update("succesful", false);
    }

    public static void Löschen(String name) {
        Path p = toPath(name);
        try {
            p = p.toRealPath(LinkOption.NOFOLLOW_LINKS);
            if (p.toFile().delete())
                Presentation.update("Successful", false);
            else
                Presentation.update("error!!", false);
        } catch (IOException e){
            Presentation.update("File not found", false);
        }
    }

    public static void move(String name, String newname){
        Path p = toPath(name);
        Path p1 = toPath(newname);
        try {
            p = p.toRealPath(LinkOption.NOFOLLOW_LINKS);
            p.toFile().renameTo(p1.toFile());
            Presentation.update("Successful", false);
        } catch (IOException e){
            Presentation.update("File not found", false);
        }
    }

    public static File[] getFiles(boolean out) {
        String inhalt = "";
        File[] a = path.toFile().listFiles();
        for (File aa : a)
            inhalt += aa.getName() + "  ";
        if (out)
            Presentation.update(inhalt, false);
        return a;
    }

    public static void changeDiretory(String ziel){
        Path p = path;
        if (ziel.trim().startsWith("./"))
            p = p.resolve(ziel);
        else
            p = Paths.get(ziel);
        try {
            p = p.toRealPath(LinkOption.NOFOLLOW_LINKS);
            if (p.toFile().isDirectory())
                path = p;
            else
                Presentation.update(p.toString()+"is not a Direktory",false);
        } catch (IOException e){
            Presentation.update(p.toString()+"is not a Direktory",false);
        }
    }

    public static void pwd() {
        Presentation.update(path.toString(),false);
    }

    public static void Schreiben(String text, Path ziel, boolean newl) {
            String[] split = text.split("\\s+");
            try {
                if (!ziel.toFile().exists())
                    ziel.toFile().createNewFile();
                ziel = ziel.toRealPath(LinkOption.NOFOLLOW_LINKS);
            } catch (IOException e){}
            try {
                FileWriter fw = new FileWriter(ziel.toFile());
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

    public static ArrayList<String> Lesen(Path wo){
        return Lesen(wo.toFile());
    }

    public static ArrayList<String> Lesen(String wo){
        Path p = toPath(wo);
        try {
            p = p.toRealPath(LinkOption.NOFOLLOW_LINKS);
        } catch (IOException e){
            try {
                p.toFile().createNewFile();
            } catch (IOException e1){}
        }
        return Lesen(p);
    }

    public static ArrayList<String> Lesen(File wo) {
        ArrayList<String> Lesen = new ArrayList<>();
        try {
            Scanner sc = new Scanner(wo);
            while (sc.hasNextLine()) {
                Lesen.add(sc.nextLine()+"\n");
            }
        } catch (FileNotFoundException e) {
            Presentation.update("The File will be created with Save and quit", false);
        }
        return Lesen;
    }

    public static void open(String path) {
        if (path.contains(".txt"))
            Iridium.Aus.add(new Editor(Lesen(path), path, Iridium.Aus));
        else if (path.contains(".mp3"))
            Player.Spiele(path);
        else {
            try {
                XWPFDocument docx = new XWPFDocument(new FileInputStream(toPath(path).toAbsolutePath().toString()));
                XWPFWordExtractor we = new XWPFWordExtractor(docx);
                Iridium.Aus.add(new Editor(we.getText(), path, Iridium.Aus));
            } catch (Exception e) {
                Presentation.update("Error file type not supported", true);
            }
        }
    }
}