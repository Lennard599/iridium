package iridium;

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
    private static String cursor =  "<font color=yellow>&#9611</font>";
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


                if ( e.getKeyCode() != 10 & e.getKeyCode() != 38 & e.getKeyCode() != 40 & e.getKeyCode() != 20 & e.getKeyCode() != 16 & e.getKeyCode() != 0 & e.getKeyCode() != 18 & e.getKeyCode() != 17 & e.getKeyCode() != 37 & e.getKeyCode() != 39 & e.getKeyCode() != 157 & e.getKeyCode() != 8) {
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

        ColorConfig colorConfig;

        switch(_farbe.trim()) {
            case "0":
            case "weiß":
                colorConfig = new ColorConfig(Color.WHITE, "0"); break;
            case "1":
            case "grün":
                colorConfig = new ColorConfig(Color.GREEN, "1"); break;
            case "2":
            case "blau":
                colorConfig = new ColorConfig(Color.BLUE, "2"); break;
            case "3":
            case "schwarz":
                colorConfig = new ColorConfig(Color.BLACK, "3"); break;
            case "4":
            case "rot":
                colorConfig = new ColorConfig(Color.RED, "4"); break;
            case "5":
            case "gelb":
                colorConfig = new ColorConfig(Color.YELLOW, "5"); break;
            default:
                colorConfig = new ColorConfig();
        }

        ConfHandler.setConf("backgroundcolor:", colorConfig.conf_color);

        Iridium.meinFrame.setBackground(colorConfig.bg_color);
        Iridium.Aus.setBackground(colorConfig.bg_color);
        Iridium.status.setBackground(colorConfig.bg_color);

        ConfHandler.writeConf("backgroundcolor:", colorConfig.conf_color);
    }

    public static void Farbeschrift(String _farbe) {
        String farbe = _farbe.trim();
        if (farbe.equals("3") || farbe.equals("blau")) {
            ConfHandler.setConf("fontcolor:", "3");
            Farbe = "<font color=blue>";
        }
        else if (farbe.equals("1") || farbe.equals("grün")) {
            ConfHandler.setConf("fontcolor:", "1");
            Farbe = "<font color=green>";
        }
        else if (farbe.equals("2") || farbe.equals("weiß")) {
            ConfHandler.setConf("fontcolor:", "2");
            Farbe = "<font color=white>";
        }
        else if (farbe.equals("0") || farbe.equals("schwarz")) {
            ConfHandler.setConf("fontcolor:", "0");
            Farbe = "<font color=black>";
        }
        else if (farbe.equals("4") || farbe.equals("rot")) {
            ConfHandler.setConf("fontcolor:", "4");
            Farbe = "<font color=red>";
        }
        else if (farbe.equals("5") || farbe.equals("gelb")) {
            ConfHandler.setConf("fontcolor:", "5");
            Farbe = "<font color=yellow>";
        }
        else {
            System.out.println("err");
            farbe = "0";
            Farbe = "<font color=black>";
        }

        ConfHandler.writeConf("fontcolor:",farbe);
    }

    public static void FarbePrefix(String _farbe) {
        String farbe = _farbe.trim();
        String a = "<font color=black>";
        if (farbe.equals("3") || farbe.equals("blau")) {
            ConfHandler.setConf("prefixcolor:","3");
            a =  "<font color=blue>";
        }
        else if (farbe.equals("1") || farbe.equals("grün")) {
            ConfHandler.setConf("prefixcolor:","1");
            a =  "<font color=green>";
        }
        else if (farbe.equals("2") || farbe.equals("weiß")) {
            ConfHandler.setConf("prefixcolor:","2");
            a =  "<font color=white>";
        }
        else if (farbe.equals("0") || farbe.equals("schwarz")) {
            ConfHandler.setConf("prefixcolor:","0");
            a =  "<font color=black>";
        }
        else if (farbe.equals("4") || farbe.equals("rot")) {
            ConfHandler.setConf("prefixcolor:","4");
            a =  "<font color=red>";
        }
        else if (farbe.equals("5") || farbe.equals("gelb")) {
            ConfHandler.setConf("prefixcolor:","5");
            a =  "<font color=yellow>";
        }
        else
            farbe = "0";
        if (ConfHandler.getConf("stdprefix:").trim().equals("Y"))
            prefix = a + ConfHandler.getConf("name:") + "@" + System.getProperty("os.name") + ": " + "</font>";
        else
            prefix = a + ConfHandler.getConf("prefixtext:");
        cursor = a + "&#9611</font>";
        ConfHandler.writeConf("prefixcolor:" , farbe);
    }
}

class ColorConfig {
    public Color bg_color = Color.WHITE;
    public String conf_color = "0";

    ColorConfig(){

    }

    ColorConfig(Color bg_color, String conf_color) {
        this.bg_color = bg_color;
        this.bg_color = bg_color;
    }
}