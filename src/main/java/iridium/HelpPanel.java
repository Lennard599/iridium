package iridium;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HelpPanel extends JPanel {
    JTextField search = new  JTextField();
    JTextArea message = new JTextArea();

    public HelpPanel() {
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

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tx = "";
                if (Call.checkCommands(search.getText().trim())) {
                    ArrayList<String> a = Filehandeling.Lesen("iridium/help/" + search.getText() + ".txt", false);
                    for (int i = 0; i < a.size(); i++)
                        tx += a.get(i);
                    message.setText(tx);
                }
                else if (search.getText().trim().isEmpty()){
                    message.setText("Type in a Command to get its description \nhere is a list of commands:\n"+Call.getCommands());
                }
                else
                    message.setText("command not Found try one of these"+Call.getCommands());
            }
        });
    }
}
