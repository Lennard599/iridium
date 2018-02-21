package iridium;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

public class HelpPanel extends JPanel {
    JTextField search = new  JTextField();
    JTextArea message = new JTextArea();

    public HelpPanel(JComponent parent) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(new BorderLayout());
        add(search, BorderLayout.NORTH);
        add(message, BorderLayout.CENTER);

        setPreferredSize(new Dimension((int)(Iridium.meinFrame.getWidth()-Iridium.meinFrame.getWidth()*0.20),Iridium.meinFrame.getHeight()/2));
        setMaximumSize(getPreferredSize());
        message.setText("Type in a Command to get its description \nhere is a list of commands:\n"+Call.getCommands());
        message.setEditable(false);
        message.setLineWrap(true);
        message.setWrapStyleWord(true);
        try {
            Robot r = new Robot();
            r.keyPress(KeyEvent.VK_TAB);
            r.keyRelease(KeyEvent.VK_TAB);
        } catch (Exception e){}


        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tx = "";
                if (Call.checkCommands(search.getText().trim(),false)) {
                    ArrayList<String> a = Filehandeling.Lesen(new File(Iridium.class.getClassLoader().getResource("help/"+search.getText().trim()+".txt").getPath()));
                    for (int i = 0; i < a.size(); i++)
                        tx += a.get(i);
                    message.setText(tx);
                }
                else if (search.getText().trim().isEmpty()){
                    message.setText("Type in a Command to get its description \nhere is a list of commands:\n"+Call.getCommands());
                }
                else
                    message.setText("command not Found try one of these "+Call.getCommands());
            }
        });

        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getExtendedKeyCode() == 27){
                    parent.removeAll();
                    parent.repaint();
                    parent.revalidate();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };
        search.addKeyListener(kl);
        message.addKeyListener(kl);
    }
}
