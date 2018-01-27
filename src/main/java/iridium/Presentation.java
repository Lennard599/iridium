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
import java.util.ArrayList;

public class Presentation {
    private static String Inhalt = "<html><body style=\"font-family: " + ConfHandler.getConf("fontcolor:") + "\">";
    public static String Farbe = "";
    private static String prefix;
    public static String cursor =  "<font color=rgb(255,255,0)>&#9611</font>";
    private static String out_l,out_r = "";
    public static int posi,p = 0;
    private static ArrayList<String> history = new ArrayList<>();

    public Presentation(){
        FarbePrefix(ConfHandler.getConf("prefixcolor:"));
        Farbeschrift(ConfHandler.getConf("fontcolor:"));
        Inhalt = "<html><body style=\"font-family: " + ConfHandler.getConf("fontstyle:") + "\">";
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

    public static void setColor(JComponent at, Color col){
        at.setBackground(col);
    }

    public static void update(String in, boolean prefixb) {

		if (prefixb) {
            Presentation.Inhalt += in + "<br>";
			Iridium.Aus.setContentType("text/html");
            Iridium.Aus.setText(Inhalt + prefix + Presentation.Farbe + out_l + cursor + out_r);
		}

        if (!prefixb) {
            Inhalt += Farbe + in + "<br>";
            Iridium.Aus.setContentType("text/html");
            Iridium.Aus.setText(Inhalt);
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
                            Iridium.Aus.setText(Inhalt + prefix + Farbe + out_l + cursor + out_r);
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
        Iridium.Aus.setText(Inhalt + prefix + Farbe + cursor);
    }

    public static void updatek(){
        Iridium.Aus.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    e.consume();
                }
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    e.consume();
                }
                if (e.getKeyCode() == 37){
                    if(posi > 0)
                        posi--;
                    String local_out = out_l + out_r;
                    out_l = local_out.substring(0, posi);
                    out_r = local_out.substring(posi);
                    Iridium.Aus.setText(Inhalt + prefix + Farbe + out_l + cursor + out_r);
                }
                if (e.getKeyCode() == 38)
                {
                    if(!(history.size()==0)) {
                        if (p >= history.size())
                            p = history.size()-1;
                        out_r = "";
                        out_l = history.get(p);
                        p++;
                        Iridium.Aus.setContentType("text/html");
                        Iridium.Aus.setText(Inhalt + prefix + Farbe + out_l + cursor + out_r);
                    }
                }

                if (e.getKeyCode() == 39) {
                    if(out_r.length() != 0)
                        posi++;
                    String local_out = out_l + out_r;
                    out_l = local_out.substring(0, posi);
                    out_r = local_out.substring(posi);
                    Iridium.Aus.setText(Inhalt + prefix + Farbe + out_l + cursor + out_r);
                }
                if (e.getKeyCode() == 40){
                    if(!(history.size()==0)) {
                        if (p == 0)
                            p = 1;
                        out_r = "";
                        p--;
                        out_l = history.get(p);
                        Iridium.Aus.setContentType("text/html");
                        Iridium.Aus.setText(Inhalt + prefix + Farbe + out_l + cursor + out_r);
                    }
                }


                if ( e.getKeyCode() != 10 & e.getKeyCode() != 38 & e.getKeyCode() != 40 & e.getKeyCode() != 20 & e.getKeyCode() != 16 & e.getKeyCode() != 0 & e.getKeyCode() != 18 & e.getKeyCode() != 17 & e.getKeyCode() != 37 & e.getKeyCode() != 39 & e.getKeyCode() != 157 & e.getKeyCode() != 8 & !e.isMetaDown()) {
                    out_l += e.getKeyChar();
                    posi++;

                    Iridium.Aus.setContentType("text/html");
                    Iridium.Aus.setText(Inhalt + prefix + Farbe + out_l + cursor + out_r);
                }
                if (e.getKeyCode() == 8) {
                    if (out_l.length() > 0){
                        posi--;

                        out_l = out_l.substring(0, out_l.length() - 1);

                        Iridium.Aus.setContentType("text/html");
                        Iridium.Aus.setText(Inhalt + prefix + Farbe + out_l + cursor + out_r);
                    }
                }
                if ( e.getKeyCode() == 10){
                    Inhalt += prefix + Farbe + out_l + out_r + "<br>";
                    Iridium.Aus.setContentType("text/html");
                    Iridium.Aus.setText(Inhalt);
                    Methods.runCommand(out_l + out_r);
                    history.add(0, out_l + out_r);
                    posi = 0;
                    p = 0;
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
            prefix = a + ConfHandler.getConf("name:") + "@" + System.getProperty("os.name") + ": " + "</font>";
        else
            prefix = a + ConfHandler.getConf("prefixtext:");
        cursor = a + "&#9611</font>";
        ConfHandler.writeConf("prefixcolor:" , _farbe);
    }
}