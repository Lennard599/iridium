package iridium;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class RGBmenue extends JPanel{
    JButton apply = new JButton("apply");
    JButton back = new JButton("back");
    JSlider[] rgb = new JSlider[3];
    JPanel[] jps = new JPanel[7];
    JTextPane ja = new JTextPane();
    JLabel[] rgblabels = new JLabel[3];
    JTextField[] rgbText = new JTextField[3];
    JPanel me = this;
    String[] _rgb = new String[] {"Red", "Green", "Blue"};
    String[] fontvalues = new String[3];
    String[] backvalues = new String[3];
    String[] prefixvaluse = new String[3];
    String[] strings = new String[]{"Font", "Background", "Prefix"};
    Color[] colors = new Color[3];
    JComboBox jb = new JComboBox(strings);

    public RGBmenue(JTabbedPane parent, JComponent replace, JComponent toColor){
        setLayout(new BorderLayout());

        jps[4] = new JPanel();
        jps[4].setLayout(new GridLayout(3,1));

        jps[6] = new JPanel();
        jps[6].add(ja);
        ja.setEditable(false);
        ja.setBackground(Presentation.getColor(Iridium.Aus));
        ja.setContentType("text/html");
        ja.setText("<font color=rgb("+ConfHandler.getConf("prefixcolor:").replaceAll("\\s", ",")+")>"+ConfHandler.getConf("name:")+"@ Iridium: "+"<font color=rgb("+ConfHandler.getConf("fontcolor:").replaceAll("\\s", ",")+")> Look at me I'm a beautiful butterfly<br>" +
                "<font color=rgb("+ConfHandler.getConf("prefixcolor:").replaceAll("\\s", ",")+")>"+ConfHandler.getConf("name:")+"@ Iridium: "+"<font color=rgb("+ConfHandler.getConf("fontcolor:").replaceAll("\\s", ",")+")> Fluttering in the moonlight<br>"+
                "<font color=rgb("+ConfHandler.getConf("prefixcolor:").replaceAll("\\s", ",")+")>"+ConfHandler.getConf("name:")+"@ Iridium: "+"<font color=rgb("+ConfHandler.getConf("fontcolor:").replaceAll("\\s", ",")+")> Waiting for the day when<br>"+
                "<font color=rgb("+ConfHandler.getConf("prefixcolor:").replaceAll("\\s", ",")+")>"+ConfHandler.getConf("name:")+"@ Iridium: "+"<font color=rgb("+ConfHandler.getConf("fontcolor:").replaceAll("\\s", ",")+")> You would use Iridium more<br>");
        add(jps[6], BorderLayout.WEST);

        for (int i = 0;i < 3;i++){
            rgbText[i] = new JTextField(3);
            rgblabels[i] = new JLabel(_rgb[i]);
            rgb[i] = new JSlider();
            rgb[i].setMaximum(255);
            rgb[i].setMinimum(0);
            rgb[i].setSnapToTicks(true);
            jps[i+1] = new JPanel();
            jps[i+1].add(rgb[i]);
            jps[i+1].add(rgblabels[i]);
            jps[i+1].add(rgbText[i]);
            jps[4].add(jps[i+1]);
        }

        add(jps[4], BorderLayout.EAST);
        jps[0] = new JPanel();
        jps[0].add(jb);
        jps[0].add(back);
        add(jps[0], BorderLayout.NORTH);

        jps[5] = new JPanel();
        jps[5].add(apply);
        add(jps[5], BorderLayout.SOUTH);

        String[] f = ConfHandler.getConf("fontcolor:").split("\\s+");
        colors[0] = new Color(Integer.parseInt(f[0]), Integer.parseInt(f[1]), Integer.parseInt(f[2]));
        rgb[0].setValue(colors[0].getRed());
        rgb[1].setValue(colors[0].getGreen());
        rgb[2].setValue(colors[0].getBlue());
        rgbText[0].setText(String.valueOf(colors[0].getRed()));
        rgbText[1].setText(String.valueOf(colors[0].getGreen()));
        rgbText[2].setText(String.valueOf(colors[0].getBlue()));

        String[] co = ConfHandler.getConf("prefixcolor:").split("\\s+");
        colors[2] = new Color(Integer.parseInt(co[0]), Integer.parseInt(co[1]), Integer.parseInt(co[2]));
        prefixvaluse[0] = String.valueOf(colors[2].getRed());
        prefixvaluse[1] = String.valueOf(colors[2].getGreen());
        prefixvaluse[2] = String.valueOf(colors[2].getBlue());

        co = ConfHandler.getConf("backgroundcolor:").split("\\s+");
        colors[1] = new Color(Integer.parseInt(co[0]), Integer.parseInt(co[1]), Integer.parseInt(co[2]));
        backvalues[0] = String.valueOf(colors[1].getRed());
        backvalues[1] = String.valueOf(colors[1].getGreen());
        backvalues[2] = String.valueOf(colors[1].getBlue());

        co = ConfHandler.getConf("fontcolor:").split("\\s+");
        colors[0] = new Color(Integer.parseInt(co[0]), Integer.parseInt(co[1]), Integer.parseInt(co[2]));
        fontvalues[0] = String.valueOf(colors[0].getRed());
        fontvalues[1] = String.valueOf(colors[0].getGreen());
        fontvalues[2] = String.valueOf(colors[0].getBlue());

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.remove(me);
                parent.add(replace, "Visuals", 1);
                parent.setSelectedIndex(1);
            }
        });

        apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (jb.getSelectedIndex()){
                    case 0: ConfHandler.setConf("fontcolor:", fontvalues[0] + " " + fontvalues[1] + " " + fontvalues[2]);
                        break;
                    case 1: ConfHandler.setConf("backgroundcolor:", backvalues[0] + " " + backvalues[1] + " " + backvalues[2]);
                        break;
                    case 2: ConfHandler.setConf("prefixcolor:", prefixvaluse[0] + " " + prefixvaluse[1] + " " + prefixvaluse[2]);
                        break;
                }
                Presentation.FarbePrefix(prefixvaluse[0] + " " + prefixvaluse[1] + " " + prefixvaluse[2]);
                Presentation.FarbeHintergrund(backvalues[0] + " " + backvalues[1] + " " + backvalues[2]);
                Presentation.Farbeschrift(fontvalues[0] + " " + fontvalues[1] + " " + fontvalues[2]);
                toColor.setBackground(new Color(Integer.parseInt(backvalues[0]), Integer.parseInt(backvalues[1]), Integer.parseInt(backvalues[2])));
            }
        });

        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jb.getSelectedIndex() == 0) {
                    colors[0] = new Color(Integer.parseInt(fontvalues[0]), Integer.parseInt(fontvalues[1]), Integer.parseInt(fontvalues[2]));
                    rgb[0].setValue(colors[0].getRed());
                    rgb[1].setValue(colors[0].getGreen());
                    rgb[2].setValue(colors[0].getBlue());
                    rgbText[0].setText(String.valueOf(colors[0].getRed()));
                    rgbText[1].setText(String.valueOf(colors[0].getGreen()));
                    rgbText[2].setText(String.valueOf(colors[0].getBlue()));
                }

                if (jb.getSelectedIndex() == 1) {
                    colors[1] = new Color(Integer.parseInt(backvalues[0]), Integer.parseInt(backvalues[1]), Integer.parseInt(backvalues[2]));
                    rgb[0].setValue(colors[1].getRed());
                    rgb[1].setValue(colors[1].getGreen());
                    rgb[2].setValue(colors[1].getBlue());
                    rgbText[0].setText(String.valueOf(colors[1].getRed()));
                    rgbText[1].setText(String.valueOf(colors[1].getGreen()));
                    rgbText[2].setText(String.valueOf(colors[1].getBlue()));
                }

                if (jb.getSelectedIndex() == 2) {
                    colors[2] = new Color(Integer.parseInt(prefixvaluse[0]), Integer.parseInt(prefixvaluse[1]), Integer.parseInt(prefixvaluse[2]));
                    rgb[0].setValue(colors[2].getRed());
                    rgb[1].setValue(colors[2].getGreen());
                    rgb[2].setValue(colors[2].getBlue());
                    rgbText[0].setText(String.valueOf(colors[2].getRed()));
                    rgbText[1].setText(String.valueOf(colors[2].getGreen()));
                    rgbText[2].setText(String.valueOf(colors[2].getBlue()));
                }
            }
        });

        jb.setSelectedIndex(1);

        for (int i = 0;i < 3;i++) {
            JTextField a = rgbText[i];
            JSlider as = rgb[i];
            int index = i;
            a.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if (e.getExtendedKeyCode() == 10) {
                        if (a.getText().matches("\\b(1?[0-9]{1,2}|2[0-4][0-9]|25[0-5])\\b")) {
                            as.setValue(Integer.valueOf(a.getText()));
                            switch (jb.getSelectedIndex()) {
                                case 0: fontvalues[index] = a.getText(); break;
                                case 1: backvalues[index] = a.getText(); break;
                                case 2: prefixvaluse[index] = a.getText(); break;
                            }
                        } else {
                            try {
                                a.setText(Integer.valueOf(a.getText()) >= 255 ? "255" : "0");
                                as.setValue(Integer.valueOf(a.getText()));
                                switch (jb.getSelectedIndex()) {
                                    case 0: fontvalues[index] = a.getText(); break;
                                    case 1: backvalues[index] = a.getText(); break;
                                    case 2: prefixvaluse[index] = a.getText(); break;
                                }
                            } catch (Exception ex) {
                                switch (jb.getSelectedIndex()) {
                                    case 0: a.setText(fontvalues[index]); break;
                                    case 1: a.setText(backvalues[index]); break;
                                    case 2: a.setText(prefixvaluse[index]); break;
                                }
                            }
                        }
                    }
                }
            });
            a.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {

                }

                @Override
                public void focusLost(FocusEvent e) {
                    switch (jb.getSelectedIndex()) {
                        case 0: a.setText(fontvalues[index]); break;
                        case 1: a.setText(backvalues[index]); break;
                        case 2: a.setText(prefixvaluse[index]); break;
                    }
                }
            });
        }

        for (int i = 0;i < 3;i++) {
            JTextField a = rgbText[i];
            JSlider as = rgb[i];
            int index = i;
            as.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    a.setText(String.valueOf(as.getValue()));
                    switch (jb.getSelectedIndex()){
                        case 2: ja.setText("<font color=rgb("+rgb[0].getValue()+","+rgb[1].getValue()+","+rgb[2].getValue()+")>"+ConfHandler.getConf("name:")+"@ Iridium: "+"<font color=rgb("+ConfHandler.getConf("fontcolor:").replaceAll("\\s", ",")+")> This is a Test Message<br>" +
                                "<font color=rgb("+rgb[0].getValue()+","+rgb[1].getValue()+","+rgb[2].getValue()+")>"+ConfHandler.getConf("name:")+"@ Iridium: "+"<font color=rgb("+ConfHandler.getConf("fontcolor:").replaceAll("\\s", ",")+")> Here you can see your choice");
                            prefixvaluse[index] = String.valueOf(rgb[index].getValue());
                            break;
                        case 0: ja.setText("<font color=rgb("+ConfHandler.getConf("prefixcolor:").replaceAll("\\s", ",")+")>"+ConfHandler.getConf("name:")+"@ Iridium: "+"<font color=rgb("+rgb[0].getValue()+","+rgb[1].getValue()+","+rgb[2].getValue()+")> This is a Test Message<br>" +
                                "<font color=rgb("+ConfHandler.getConf("prefixcolor:").replaceAll("\\s", ",")+")>"+ConfHandler.getConf("name:")+"@ Iridium: "+"<font color=rgb("+rgb[0].getValue()+","+rgb[1].getValue()+","+rgb[2].getValue()+")> Here you can see your choice");
                            fontvalues[index] = String.valueOf(rgb[index].getValue());
                            break;
                        case 1: ja.setBackground(new Color(+rgb[0].getValue(),rgb[1].getValue(),rgb[2].getValue()));
                            backvalues[index] = String.valueOf(rgb[index].getValue());
                            break;
                    }
                }
            });
        }
    }
}