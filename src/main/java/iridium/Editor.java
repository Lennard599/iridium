package iridium;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static iridium.TextareaEngine.getOutput;

public class Editor extends JPanel {
    static JTextArea text;
    static JTextPane controles;
    JTextPane titlearea;
    boolean saved = true;
    static String a, path = "";
    static String title = "";
    JComponent parent;
    boolean rela = true;
    static KeyAdapter spezial;
    boolean sure;
    static SimpleAttributeSet center = new SimpleAttributeSet();

    public Editor(ArrayList<String> a, String title, boolean rela, String Path, JComponent parent) {
        String in = "";

        for (int i = 0; i < a.size(); i++)
            in += a.get(i);

        create(in, title, rela, Path, parent);
    }

    public Editor(String aa, String title, boolean rela, String Path, JComponent parent) {
        create(aa, title, rela, Path, parent);
    }

    private void create(String a, String title, boolean rela, String path, JComponent parent) {
        this.a = a;
        this.title = title;
        this.rela = rela;
        this.path = path;
        this.parent = parent;
        setLayout(new BorderLayout());
        setSize(parent.getSize());

        titlearea = new JTextPane();
        StyledDocument doc = titlearea.getStyledDocument();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        titlearea.setText(title);
        add(titlearea, BorderLayout.NORTH);
        titlearea.setEditable(false);

        controles = new JTextPane();
        aligment("center");
        controles.setText("save: cmd + s / ctrl + s    quit: cmd + x / ctrl + x");
        add(controles, BorderLayout.SOUTH);
        controles.setEditable(false);

        text = new JTextArea();
        add(text, BorderLayout.CENTER);
        text.setText(a);
        spezial = new TextareaEngine(controles, false, "save as: ", title, "|", () ->{
            text.addKeyListener(normal);
            save(true, path, getOutput());
            settitle(getOutput());
            aligment("center");
            controles.setText("save: cmd + s / ctrl + s    quit: cmd + x / ctrl + x");
        });
        text.addKeyListener(normal);
        titlearea.setForeground(Color.WHITE);
        controles.setForeground(Color.WHITE);
        titlearea.setBackground(Color.BLACK);
        controles.setBackground(Color.BLACK);

    }

    private static void save(boolean rela, String path, String title) {
        if (rela) {
            Filehandeling.Schreiben(text.getText(), title, true, false);
        } else
            Filehandeling.Schreiben(text.getText(), path, false, false);
        text.removeKeyListener(spezial);
        a = text.getText();
    }

    KeyAdapter normal = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            if (saved)
                if (!text.getText().equals(a)) {
                    saved = false;
                    titlearea.setText("*" + titlearea.getText());
                }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if ((e.isControlDown() || e.isMetaDown()) && e.getKeyCode() == 83) {
                saved = true;
                titlearea.setText(titlearea.getText().replace("*", ""));
                text.removeKeyListener(normal);
                text.addKeyListener(spezial);
                aligment("left");
                controles.setText("save as: " + title +"|");
            }

            if ((e.isControlDown() || e.isMetaDown()) && e.getKeyCode() == 88) {
                if (saved) {
                    parent.removeAll();
                    parent.repaint();
                    parent.revalidate();
                } else {
                    sure = true;
                    aligment("left");
                    controles.setText("save before quiting? (Y/N):");
                    text.setEditable(false);
                }
            }
                if (sure) {
                    if (e.getKeyChar() == 'N' || e.getKeyChar() == 'n') {
                        parent.removeAll();
                        parent.repaint();
                        parent.revalidate();
                    } else if (e.getKeyChar() == 'Y' || e.getKeyChar() == 'y') {
                        saved = true;
                        titlearea.setText(titlearea.getText().replace("*", ""));
                        text.addKeyListener(spezial);
                        text.removeKeyListener(normal);
                        aligment("left");
                        controles.setText("save as: " + title +"|");
                        text.addKeyListener(new TextareaEngine(controles, false, "save as: ", title, "|", () ->{
                            save(true, path, getOutput());
                            settitle(getOutput());
                            parent.removeAll();
                            parent.repaint();
                            parent.revalidate();
                        }));
                        }
                }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    };

    public static void settitle(String t){
        title = t;
    }

    public static void aligment(String alignment){
        StyleConstants.setAlignment(center,
                alignment == "right"
                        ? StyleConstants.ALIGN_RIGHT
                        : alignment == "center"
                        ? StyleConstants.ALIGN_CENTER
                        : alignment == "left"
                        ? StyleConstants.ALIGN_LEFT
                        : StyleConstants.ALIGN_LEFT);
        StyledDocument doc = controles.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }
}