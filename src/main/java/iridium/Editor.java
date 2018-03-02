package iridium;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static iridium.TextareaEngine.getOutput;

public class Editor extends JPanel {
    static JTextArea text;
    static JScrollPane scroll;
    static JTextPane controles;
    JTextPane titlearea;
    boolean saved = true;
    static String a, path = "";
    JComponent parent;
    JFrame frame;
    static KeyAdapter spezial;
    boolean sure;
    static SimpleAttributeSet center = new SimpleAttributeSet();
    
    public Editor(ArrayList<String> a, String Path, JComponent parent, JFrame frame) {
        String in = "";
        
        for (int i = 0; i < a.size(); i++)
            in += a.get(i);
        
        create(in, Path, parent, frame);
    }
    
    public Editor(String aa, String Path, JComponent parent, JFrame frame) {
        create(aa, Path, parent, frame);
    }
    
    private void create(String a, String p, JComponent parent, JFrame frame) {
        this.a = a;
        this.path = p;
        this.parent = parent;
        this.frame = frame;

        frame.requestFocus();
        setLayout(new BorderLayout());
        titlearea = new JTextPane();
        StyledDocument doc = titlearea.getStyledDocument();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        titlearea.setText(path);
        add(titlearea, BorderLayout.NORTH);
        titlearea.setEditable(false);
        titlearea.setPreferredSize(new Dimension(Iridium.meinFrame.getWidth(),20));
        titlearea.setMinimumSize(new Dimension(Iridium.meinFrame.getWidth(),20));

        controles = new JTextPane();
        aligment("center");
        controles.setText("save: cmd + s / ctrl + s    quit: cmd + x / ctrl + x");
        add(controles, BorderLayout.SOUTH);
        controles.setEditable(false);
        controles.setPreferredSize(new Dimension(frame.getWidth(),20));
        controles.setMinimumSize(new Dimension(frame.getWidth(),20));

        text = new JTextArea();
        text.requestFocus();
        scroll = new JScrollPane(text);
        add(scroll, BorderLayout.CENTER);
        text.setText(a);
        spezial = new TextareaEngine(controles, false, "save as: ", path, "|", () ->{
            text.addKeyListener(normal);
            save(getOutput());
            settitle(getOutput());
            aligment("center");
            controles.setText("save: cmd + s / ctrl + s    quit: cmd + x / ctrl + x");
        });
        text.addKeyListener(normal);
        text.setBackground(Presentation.getColor(parent));
        String[] f = ConfHandler.getConf("fontcolor:").split("\\s+");
        text.setForeground(new Color(Integer.parseInt(f[0]), Integer.parseInt(f[1]), Integer.parseInt(f[2])));
        text.setCaretColor(Presentation.getColor(parent).equals(Color.BLACK) ? Color.WHITE : Color.BLACK);
        scroll.setViewportBorder(null);
        scroll.setBorder(null);
        Color main = Presentation.getColor(parent).equals(Color.BLACK) ? Color.BLACK : Color.WHITE;
        Color secondary = Presentation.getColor(parent).equals(Color.BLACK) ? Color.WHITE : Color.BLACK;
        titlearea.setForeground(main);
        controles.setForeground(main);
        titlearea.setBackground(secondary);
        controles.setBackground(secondary);
        Presentation.setColor(Iridium.status,secondary);
    }

    private void end(){
        frame.remove(this);
        frame.add(Iridium.scroll,BorderLayout.CENTER);
        frame.repaint();
        frame.revalidate();
        parent.requestFocus();
    }
    
    private static void save(String path) {
        Path p = Filehandeling.path;
        if (path.startsWith("./")) {
            p = p.resolve(path);
        }
        else
            if (path.indexOf("/") == -1) {
                p = p.resolve(path);
            }
            else {
                p = Paths.get(path);
            }
        try {
            p = p.toRealPath(LinkOption.NOFOLLOW_LINKS);
        } catch (IOException e){
            try {
                p.toFile().createNewFile();
            } catch (IOException e1){}
        }
        if (path.contains("docx")) {
            try {
                XWPFDocument document = new XWPFDocument();
                FileOutputStream out = new FileOutputStream(p.toFile());

                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                run.setText(text.getText());
                document.write(out);
                out.close();
            } catch (IOException e){System.out.println(e);}
        }
        else
            Filehandeling.Schreiben(text.getText(), p, false);

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
                controles.setText("save as: " + path +"|");
            }

            if ((e.isControlDown() || e.isMetaDown()) && e.getKeyCode() == 88) {
                if (saved) {
                    Presentation.setColor(Iridium.status, Presentation.getColor(Iridium.Aus));
                    end();
                } else {
                    sure = true;
                    aligment("left");
                    controles.setText("save before quiting? (Y/N):");
                    text.setEditable(false);
                }
            }
            if (sure) {
                if (e.getKeyChar() == 'N' || e.getKeyChar() == 'n') {
                    Presentation.setColor(Iridium.status, Presentation.getColor(Iridium.Aus));
                    end();
                } else if (e.getKeyChar() == 'Y' || e.getKeyChar() == 'y') {
                    saved = true;
                    titlearea.setText(titlearea.getText().replace("*", ""));
                    text.addKeyListener(spezial);
                    text.removeKeyListener(normal);
                    aligment("left");
                    controles.setText("save as: " + path +"|");
                    text.addKeyListener(new TextareaEngine(controles, false, "save as: ", path, "|", () ->{
                        save(getOutput());
                        settitle(getOutput());
                        Presentation.setColor(Iridium.status, Presentation.getColor(Iridium.Aus));
                        end();
                    }));
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    };

    public static void settitle(String t){
path = t;
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
