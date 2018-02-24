package iridium;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Presentation {
    private static String Inhalt = "";
    private static String sug = "";
    private static ArrayList<String> sugs;
    private static boolean newsug = true;
    private static int tabposi = 0;
    private static String properties = "<html><body style=\"font-family: " + ConfHandler.getConf("fontstyle:") + "; font-size: "+ConfHandler.getConf("fontsize:")+"px\">";
    public static String Farbe = "";
    private static String prefix;
    public static String cursor =  "<font color=rgb(255,255,0)>&#9611</font>";
    public static String out_l,out_r = "";
    public static int posi,p = 0;
    public static String path = "";
    private static ArrayList<String> history = new ArrayList<>();

    public Presentation(){
        FarbePrefix(ConfHandler.getConf("prefixcolor:"));
        Farbeschrift(ConfHandler.getConf("fontcolor:"));
        FarbeHintergrund(ConfHandler.getConf("backgroundcolor:"));
        Inhalt += Farbe + "Wilkommen " + ConfHandler.getConf("name:") + "<br>";
        Presentation.firstup();
        Presentation.updatek();
        Presentation.Mouse();
    }

    public static Color getColor(JComponent from){
        return from.getBackground();
    }

    public static String getIndexRGB(int index){
        switch (index){
            case 0: return "0 0 0";
            case 1: return "0 255 0";
            case 2: return "255 255 255";
            case 3: return "0 0 255";
            case 4: return "255 0 0";
            case 5: return "255 255 0";
            default: return "WTF";
        }
    }

    public static int getColor(String farbe){
        switch (farbe.trim()) {
            case "0 0 0": return 0;
            case "0 255 0": return 1;
            case "255 255 255": return 2;
            case "0 0 255": return 3;
            case "255 0 0": return 4;
            case "255 255 0": return 5;
            default: return 0;
        }
    }

    public static void setText(){
        Iridium.Aus.setText(getPrefix(true) + out_l + cursor + out_r + sug);
    }

    private static void suggest(){
        if (newsug)
            sugs = Call.complete(out_l);
        newsug = false;
        if (!(tabposi < sugs.size()))
            tabposi = 0;
        if (sugs.size() > 1) {
            sug = "<br>";
            for (String aa : sugs)
                sug += aa + " ";
        } else
            sug = "";
        try {
            out_l = sugs.get(tabposi);
        } catch (Exception e) {}
        tabposi++;
        posi = out_l.length();
        setText();
    }

    private static void suggestf(){
        String[] p = out_l.split("\\s+");
        Path pa = Filehandeling.path;
        String path = p[p.length-1].substring(0,p[p.length-1].lastIndexOf("/")+1);
        String pathmit = p[p.length-1];
        String lastdir = pathmit.substring(pathmit.lastIndexOf("/")+1,pathmit.length());

        if (path.trim().startsWith("./"))
            pa = pa.resolve(path);
        else
            if (path.indexOf("/") == -1)
                pa = pa.resolve(path);
            else
                pa = Paths.get(path);
        try {
            pa = pa.toRealPath(LinkOption.NOFOLLOW_LINKS);
        } catch (IOException e){System.out.println(e);}

        if (newsug){
            sugs = new ArrayList<String>();
            for (File a : pa.toFile().listFiles()) {
                if (a.getName().startsWith(lastdir.trim()))
                    sugs.add(a.getName());
            }
        }

        newsug = false;
        if (!(tabposi < sugs.size()))
            tabposi = 0;
        if (sugs.size() > 1) {
            sug = "<br>";
            for (File a : pa.toFile().listFiles())
                if (a.getName().startsWith(lastdir.trim()))
                    sug += a.getName() + " ";
        } else
            sug = "";
        try {
            if (out_l.indexOf("/") == -1) {
                out_l = "";
                p[p.length-1] = sugs.get(tabposi);
                for (int i = 0;i < p.length;i++)
                    out_l += p[i] + " ";
                out_l = out_l.trim();
            }
            else
                out_l = out_l.substring(0, out_l.lastIndexOf("/") + 1) + sugs.get(tabposi);
        } catch (Exception e) {}
        tabposi++;
        posi = out_l.length();
        setText();
    }

    public static void setColor(JComponent at, Color col){
        at.setBackground(col);
    }

    public static void update(String in, boolean prefixb) {

		if (prefixb) {
            Presentation.Inhalt += in + "<br>";
			Iridium.Aus.setContentType("text/html");
            Iridium.Aus.setText(getPrefix(true) + Presentation.Farbe + out_l + cursor + out_r);
		}

        if (!prefixb) {
            Inhalt += Farbe + in + "<br>";
            Iridium.Aus.setContentType("text/html");
            Iridium.Aus.setText(getPrefix(false));
        }
    }

    public static void update(ArrayList<String> a,boolean prefb){
        String b = "";
        for (String c:a)
            b += c + "<br>";
        update(b,prefb);
    }

    public static void clear() {
        Presentation.Inhalt = "<html><body style=\"font-family: " + ConfHandler.getConf("fontstyle:") + "\">";
        Iridium.Aus.setText("");
    }

    public static void copyToClipboard(String text) {
        Thread th = new Thread( () -> {
            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection clip = new StringSelection(text);
            clpbrd.setContents(clip, null);
        });
        th.start();
    }

    public static void Mouse(){
        Iridium.Aus.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 3){
                    Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                    Transferable t = clpbrd.getContents( null );
                    try {
                        if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                            Object o = t.getTransferData(DataFlavor.stringFlavor);
                            String data = (String) t.getTransferData(DataFlavor.stringFlavor);
                            out_l += data;
                            posi = out_l.length();
                            Iridium.Aus.setText(getPrefix(true) + out_l + cursor + out_r + sug);
                        }
                    }catch (Exception ignore){

                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (Iridium.Aus.getSelectedText() != null) {
                    String s = Iridium.Aus.getSelectedText();

                    copyToClipboard(s);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public static void firstup(){
        out_l = "";
        out_r ="";
        Iridium.Aus.setContentType("text/html");
        FarbePrefix(ConfHandler.getConf("prefixcolor:"));
        Iridium.Aus.setText(getPrefix(true) + cursor);
    }

    public static void updatek(){
        Iridium.Aus.setFocusTraversalKeysEnabled(false);
        Iridium.Aus.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.isMetaDown() || e.isControlDown()) && e.getExtendedKeyCode() == 521){
                    int s = Integer.valueOf(ConfHandler.getConf("fontsize:").trim());
                    s++;
                    ConfHandler.setConf("fontsize:", String.valueOf(s));
                    ConfHandler.writeConf();
                    Presentation.updateProperties();
                }
                if ((e.isMetaDown() || e.isControlDown()) && e.getExtendedKeyCode() == 45){
                    int s = Integer.valueOf(ConfHandler.getConf("fontsize:").trim());
                    s--;
                    if (s < 1)
                        s = 1;
                    ConfHandler.setConf("fontsize:", String.valueOf(s));
                    ConfHandler.writeConf();
                    Presentation.updateProperties();
                }
                if (e.getKeyCode() == KeyEvent.VK_TAB){
                    if (out_l.split("\\s+").length > 1)
                        suggestf();
                    else
                        suggest();
                    e.consume();
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    newsug = true;
                    e.consume();
                }
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    newsug = true;
                    e.consume();
                }
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    newsug = true;
                    e.consume();
                }
                if (e.getKeyCode() == 37){
                    newsug = true;
                    if(posi > 0)
                        posi--;
                    String local_out = out_l + out_r;
                    out_l = local_out.substring(0, posi);
                    out_r = local_out.substring(posi);
                    setText();
                }
                if (e.getKeyCode() == 38) {
                    newsug = true;
                    if(!(history.size()==0)) {
                        if (p >= history.size())
                            p = history.size()-1;
                        out_r = "";
                        out_l = history.get(p);
                        p++;
                        posi = out_l.length();
                        setText();
                    }
                }

                if (e.getKeyCode() == 39) {
                    newsug = true;
                    if(out_r.length() != 0)
                        posi++;
                    String local_out = out_l + out_r;
                    out_l = local_out.substring(0, posi);
                    out_r = local_out.substring(posi);
                    setText();
                }
                if (e.getKeyCode() == 40){
                    newsug = true;
                    if(!(history.size()==0)) {
                        if (p == 0)
                            p = 1;
                        out_r = "";
                        p--;
                        out_l = history.get(p);
                        setText();
                    }
                }


                if ( e.getKeyCode() != 10 & e.getKeyCode() != 38 & e.getKeyCode() != 40 & e.getKeyCode() != 20 & e.getKeyCode() != 16 & e.getKeyCode() != 0 & e.getKeyCode() != 18 & e.getKeyCode() != 17 & e.getKeyCode() != 37 & e.getKeyCode() != 39 & e.getKeyCode() != 157 & e.getKeyCode() != 8 & !e.isMetaDown() & !e.isControlDown() & e.getKeyCode() != 9) {
                    newsug = true;
                    out_l += e.getKeyChar();
                    posi++;

                    setText();
                }
                if (e.getKeyCode() == 8) {
                    newsug = true;
                    if (out_l.length() > 0){
                        posi--;

                        out_l = out_l.substring(0, out_l.length() - 1);

                        setText();
                    }
                }
                if ( e.getKeyCode() == 10){
                    newsug = true;
                    Inhalt += prefix + path + Farbe + out_l + out_r + "<br>";
                    Iridium.Aus.setContentType("text/html");
                    Iridium.Aus.setText(getPrefix(false));
                    if (!(out_l+out_r).isEmpty()) {
                        Methods.runCommand((out_l + out_r).trim());
                        history.add(0, out_l + out_r);
                    }
                    posi = 0;
                    p = 0;
                    sug = "";
                    firstup();
                }
            }
        });
    }

    public static void FarbeHintergrund(String _farbe) {

        String[] rgb = _farbe.split("\\s+");

        ConfHandler.setConf("backgroundcolor:", _farbe);

        Iridium.meinFrame.setBackground(new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
        Iridium.Aus.setBackground(new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
        Iridium.status.setBackground(new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
    }

    public static void Farbeschrift(String _farbe) {
        String[] rgb = _farbe.split("\\s+");

        Farbe = "<font color=rgb("+rgb[0]+","+rgb[1]+","+rgb[2]+")>";

        ConfHandler.writeConf("fontcolor:", _farbe);
    }

    public static void FarbePrefix(String _farbe) {
        String[] rgb = _farbe.split("\\s+");

        String a =  "<font color=rgb("+rgb[0]+","+rgb[1]+","+rgb[2]+")>";

        if (ConfHandler.getConf("stdprefix:").trim().equals("Y"))
            prefix = a + ConfHandler.getConf("name:") + "@" + System.getProperty("os.name") + ":";
        else
            prefix = a + ConfHandler.getConf("prefixtext:");
        cursor = a + "&#9611</font>";
        ConfHandler.writeConf("prefixcolor:" , _farbe);
    }

    public static void updateProperties(){
        properties = "<html><body style=\"font-family: " + ConfHandler.getConf("fontstyle:") + "; font-size: "+ConfHandler.getConf("fontsize:")+"px\">";
        setText();
    }

    public static String getPrefix(boolean mit){
        if (Filehandeling.path.getNameCount() > 1)
            path ="/" +  Filehandeling.path.getName(Filehandeling.path.getNameCount()-2).toString()  + "/" + Filehandeling.path.getName(Filehandeling.path.getNameCount()-1).toString() + "/";
        else
            path = Filehandeling.path.toString();
        if (mit)
            return properties + Inhalt  + prefix + path + "</font>";
        return properties + Inhalt;
    }
}