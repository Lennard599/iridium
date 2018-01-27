package iridium;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class OutputPanel extends JPanel{
    JTextArea text = new JTextArea();
    JTextArea title = new JTextArea();

    public OutputPanel(String title, String content, JComponent parent){
        setPreferredSize(new Dimension((int)(Iridium.meinFrame.getWidth()-Iridium.meinFrame.getWidth()*0.05),Iridium.meinFrame.getHeight()/2));
        setMaximumSize(getPreferredSize());
        setLayout(new BorderLayout());
        text.setText(content);
        this.title.setText(title);
        this.title.setEditable(false);
        this.title.setBackground(Color.BLACK);
        this.title.setForeground(Color.WHITE);
        add(this.title, BorderLayout.NORTH);
        add(text, BorderLayout.CENTER);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

}
