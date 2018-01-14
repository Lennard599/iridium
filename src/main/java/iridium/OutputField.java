package iridium;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class OutputField {

    public OutputField(ArrayList<String> a, String title, boolean be, boolean rela, String Path) {
        String in = "";

        for (int i = 0;i<a.size();i++)
            in += a.get(i);

        OutputField(in, title, be, rela, Path);
    }

    public OutputField(String aa, String title, boolean be, boolean rela, String Path){
        OutputField(aa,title,be,rela,Path);
    }

    public void OutputField(String a, String title, boolean be, boolean rela, String Path) {
        JFrame Ausgabe = new JFrame(title);
        Ausgabe.setVisible(true);
        Ausgabe.setSize(450, 600);
        Ausgabe.setResizable(true);
        Ausgabe.setLocationRelativeTo(null);
        Ausgabe.setLayout(new BorderLayout());
        Ausgabe.setIconImage(Iridium.icon.getImage());

        if (be) {
            Ausgabe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            WindowListener close = new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    int confirm = JOptionPane.showConfirmDialog(Ausgabe,
                            "quit without saving?",
                            "quit?", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);

                    if (confirm == 0)
                        Ausgabe.dispose();

                }
            };
            Ausgabe.addWindowListener(close);

        } else
            Ausgabe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JButton Speichern = new JButton("save and quit");

        if (be)
            Ausgabe.add(Speichern, BorderLayout.SOUTH);

        JTextArea text = new JTextArea();
        JScrollPane scroll = new JScrollPane(text);

        Ausgabe.add(scroll, BorderLayout.CENTER);

        if (!be)
            text.setEditable(false);

        //text.setFont(new Font(ConfHandler.getConf("fontstyle:"),Font.PLAIN,12));

        text.setText(a);

        Speichern.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (rela) {
                    Filehandeling.Schreiben(text.getText(),title,true,false);
                }
                else
                    Filehandeling.Schreiben(text.getText(), Path, false, false);
                Ausgabe.dispose();
                Presentation.update("saved", true);
            }
        });
    }
}
