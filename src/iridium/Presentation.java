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
    public static String fontfamily = "'Menlo'";
    private static String ptext = ConfHandler.NameB + "@" + System.getProperty("os.name") + ": ";
    private static String Inhalt = "<html><body style=\"font-family: " + fontfamily + "\">";
    public static String Farbe = "";
    private static String prefix = "<font color=yellow>#-# </font>";
    private static String cursor =  "<font color=yellow>&#9611</font>";
    private static String out_l,out_r = "";
    public static int posi,p = 0;
    private static ArrayList<String> history = new ArrayList<>();

    public Presentation(){
        prefix = FarbePrefix(ConfHandler.FarbeP2) + ConfHandler.NameB + "@" + System.getProperty("os.name") + ": " + "</font>";
        Farbe = Farbeschrift(ConfHandler.FarbeS2);
        Color col = FarbeHintergrund(ConfHandler.FarbeH2);
        Iridium.Aus.setBackground(col);
        Inhalt += Farbe + "Wilkommen " + ConfHandler.NameB + "<br>";

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

    public static void clear() {
        Presentation.Inhalt = "<Html><body>";
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
        Iridium.Aus.setContentType("text/html");
        prefix = FarbePrefix(ConfHandler.FarbeP2) + ptext + "</font>";
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
                    methods.runCommand(out_l + out_r);
                    history.add(0, out_l + out_r);
                    posi = 0;
                    p = 0;
                    firstup();
                }
            }
        });
    }

    private static Color FarbeHintergrund(String farbe) {
        Color output = null;

        if (farbe.equals("2") || farbe.equals("blau")) {
            output = Color.BLUE;
            ConfHandler.FarbeH2 = "2";
        }
        if (farbe.equals("1") || farbe.equals("grün")) {
            output = Color.GREEN;
            ConfHandler.FarbeH2 = "1";
        }
        if (farbe.equals("0") || farbe.equals("weiß")) {
            output = Color.WHITE;
            ConfHandler.FarbeH2 = "0";
        }
        if (farbe.equals("3") || farbe.equals("schwarz")) {
            output = Color.BLACK;
            ConfHandler.FarbeH2 = "3";
        }
        if (farbe.equals("4") || farbe.equals("rot")) {
            output = Color.RED;
            ConfHandler.FarbeH2 = "4";
        }
        if (farbe.equals("5") || farbe.equals("gelb")) {
            output = Color.YELLOW;
            ConfHandler.FarbeH2 = "5";
        }

        return output;
    }

    private static String Farbeschrift(String farbe) {
        if (farbe.equals("3") || farbe.equals("blau")) {
            ConfHandler.FarbeS2 = "3";
            return "<font color=blue>";
        }
        if (farbe.equals("1") || farbe.equals("grün")) {
            ConfHandler.FarbeS2 = "1";
            return "<font color=green>";
        }
        if (farbe.equals("2") || farbe.equals("weiß")) {
            ConfHandler.FarbeS2 = "2";
            return "<font color=white>";
        }
        if (farbe.equals("0") || farbe.equals("schwarz")) {
            ConfHandler.FarbeS2 = "0";
            return "<font color=black>";
        }
        if (farbe.equals("4") || farbe.equals("rot")) {
            ConfHandler.FarbeS2 = "4";
            return "<font color=red>";
        }
        if (farbe.equals("5") || farbe.equals("gelb")) {
            ConfHandler.FarbeS2 = "5";
            return "<font color=yellow>";
        }
        return "";
    }

    private static String FarbePrefix(String farbe) {
        if (farbe.equals("3") || farbe.equals("blau")) {
            ConfHandler.FarbeP2 = "3";
            return "<font color=blue>";
        }
        if (farbe.equals("1") || farbe.equals("grün")) {
            ConfHandler.FarbeP2 = "1";
            return "<font color=green>";
        }
        if (farbe.equals("2") || farbe.equals("weiß")) {
            ConfHandler.FarbeP2 = "2";
            return "<font color=white>";
        }
        if (farbe.equals("0") || farbe.equals("schwarz")) {
            ConfHandler.FarbeP2 = "0";
            return "<font color=black>";
        }
        if (farbe.equals("4") || farbe.equals("rot")) {
            ConfHandler.FarbeP2 = "4";
            return "<font color=red>";
        }
        if (farbe.equals("5") || farbe.equals("gelb")) {
            ConfHandler.FarbeP2 = "5";
            return "<font color=yellow>";
        }
        return "";
    }

    public static void PrefixF(String farbep) {
        String a;
        prefix = FarbePrefix(farbep) + ConfHandler.NameB + "@" + System.getProperty("os.name") + ": " + "</font>";
        a = ConfHandler.NameB + " " + ConfHandler.FarbeH2 + " " + ConfHandler.FarbeS2 + " " + farbep + " ";
        ArrayList<String> b = Filehandeling.Lesen("iridium/Iridiumconfig.txt", false);
        for (int i = 4;i < b.size();i++)
            a += b.get(i) + " ";
        Filehandeling.Schreiben(a, "iridium/Iridiumconfig.txt", false, true);
    }

    public static void HintergrundF(String farbeh) {
        String a;
        Color col = FarbeHintergrund(farbeh);
        a = ConfHandler.NameB + " " + farbeh + " " + ConfHandler.FarbeS2 + " " + ConfHandler.FarbeP2 + " ";
        ArrayList<String> b = Filehandeling.Lesen("iridium/Iridiumconfig.txt", false);
        for (int i = 4;i < b.size();i++)
            a += b.get(i) + " ";
        Filehandeling.Schreiben(a, "iridium/Iridiumconfig.txt", false, true);
        Iridium.meinFrame.setBackground(col);
        Iridium.Aus.setBackground(col);
    }

    public static void SchriftF(String farbes) {
        String a;
        Farbe = Farbeschrift(farbes);
        a = ConfHandler.NameB + " " + ConfHandler.FarbeH2 + " " + farbes + " " + ConfHandler.FarbeP2 + " ";
        ArrayList<String> b = Filehandeling.Lesen("iridium/Iridiumconfig.txt", false);
        for (int i = 4;i < b.size();i++)
            a += b.get(i) + " ";
        Filehandeling.Schreiben(a, "iridium/Iridiumconfig.txt", false, true);
    }
}
