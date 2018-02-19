package iridium;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Options extends JPanel{

    static JTextField Name = new JTextField(10);
    static JTextField Font = new JTextField(10);
    static JTextField Prefix = new JTextField(10);
    static JTextField _fontsize = new JTextField(10);

    private static void save() {
        ConfHandler.setConf("name:", Name.getText());
        ConfHandler.setConf("fontstyle:", Font.getText());
        ConfHandler.setConf("prefixtext:", Prefix.getText());
        ConfHandler.setConf("fontsize:", _fontsize.getText());
        Presentation.updateProperties();
        ConfHandler.writeConf();
    }

    public Options(int index, JComponent parent) {
        JTabbedPane pane = new JTabbedPane();
        JLabel OptionName = new JLabel("Your Name");
        String[] FarbListe = {"Black", "Green", "White", "Blue", "Red", "Yellow"};
        JComboBox FarbeH = new JComboBox(FarbListe);
        JComboBox FarbeS = new JComboBox(FarbListe);
        JComboBox FarbeP = new JComboBox(FarbListe);
        JButton anwenden = new JButton("apply");
        JButton anwenden2 = new JButton("apply");
        JButton rgb = new JButton("advanced");
        JLabel OptionFarbeH = new JLabel("backgroundcolor");
        JLabel OptionFarbeS = new JLabel("fontcolor");
        JLabel OptionFarbeP = new JLabel("Prefix color");
        JLabel OptionFont = new JLabel("Fontstyle");
        JLabel fontsize = new JLabel("Fontsize");
        JLabel OptionPrefix = new JLabel("Prefix Text");
        JLabel stdpl = new JLabel("Use Custom Prefix");
        JCheckBox stdp = new JCheckBox();

        JPanel Generl = new JPanel();
        JPanel visuals = new JPanel();

        JPanel Jp3 = new JPanel();
        JPanel Jp4 = new JPanel();
        JPanel Jp5 = new JPanel();
        JPanel Jp6 = new JPanel();
        JPanel Jp7 = new JPanel();
        JPanel Jp9 = new JPanel();
        JPanel Jp9_5 = new JPanel();
        JPanel Jp10 = new JPanel();
        JPanel Jp11 = new JPanel();
        JPanel Jp72 = new JPanel();
        JPanel Jp12 = new JPanel();

        setPreferredSize(parent.getSize());
        pane.setPreferredSize(getPreferredSize());
        add(pane);
        setBackground(Presentation.getColor(parent));

        Name.setText(ConfHandler.getConf("name:"));
        Font.setText(ConfHandler.getConf("fontstyle:"));
        Prefix.setText(ConfHandler.getConf("prefixtext:"));
        _fontsize.setText(ConfHandler.getConf("fontsize:"));

        if (ConfHandler.getConf("stdprefix:").trim().equals("N"))
            stdp.setSelected(true);

        if (!stdp.isSelected())
            Prefix.setFocusable(false);

        Jp3.add(fontsize);
        Jp3.add(_fontsize);
        Jp4.add(OptionFarbeH);
        Jp4.add(FarbeH);
        Jp5.add(OptionFarbeS);
        Jp5.add(FarbeS);
        Jp9.add(OptionFarbeP);
        Jp9.add(FarbeP);
        Jp6.add(OptionName);
        Jp6.add(Name);
        Jp7.add(anwenden);
        Jp9_5.add(rgb);
        Jp10.add(OptionFont);
        Jp10.add(Font);
        Jp11.add(OptionPrefix);
        Jp11.add(Prefix);
        Jp72.add(anwenden2);
        Jp12.add(stdpl);
        Jp12.add(stdp);

        Generl.add(Jp6);
        Generl.add(Jp7);
        GridLayout g = new GridLayout(8,1);
        g.setVgap(0);
        visuals.setLayout(g);
        visuals.add(Jp4);
        visuals.add(Jp5);
        visuals.add(Jp9);
        visuals.add(Jp9_5);
        visuals.add(Jp10);
        visuals.add(Jp3);
        visuals.add(Jp12);
        visuals.add(Jp11);
        visuals.add(Jp72);

        pane.add(Generl, "General", 0);
        pane.add(visuals, "Visuals", 1);

        FarbeH.setSelectedIndex(Presentation.getColor(ConfHandler.getConf("backgroundcolor:")));
        FarbeS.setSelectedIndex(Presentation.getColor(ConfHandler.getConf("fontcolor:")));
        FarbeP.setSelectedIndex(Presentation.getColor(ConfHandler.getConf("prefixcolor:")));

        stdp.addActionListener(ee ->{
            if (!stdp.isSelected()) {
                ConfHandler.writeConf("stdprefix:", "Y");
                Presentation.FarbePrefix(ConfHandler.getConf("prefixcolor:"));
                Prefix.setFocusable(false);
            }
            else{
                ConfHandler.writeConf("stdprefix:", "N");
                Presentation.FarbePrefix(ConfHandler.getConf("prefixcolor:"));
                Prefix.setFocusable(true);
            }
        });

        FarbeH.addActionListener(ee -> {
            ConfHandler.setConf("backgroundcolor:", Presentation.getIndexRGB(FarbeH.getSelectedIndex()));
            Presentation.getColor(ConfHandler.getConf("backgroundcolor:"));
            setBackground(Presentation.getColor(parent));
        });

        FarbeS.addActionListener(ee -> {
            ConfHandler.setConf("fontcolor:", Presentation.getIndexRGB(FarbeS.getSelectedIndex()));
            Presentation.getColor(ConfHandler.getConf("fontcolor:"));
        });

        FarbeP.addActionListener(ee -> {
            ConfHandler.setConf("prefixcolor:", Presentation.getIndexRGB(FarbeP.getSelectedIndex()));
            Presentation.getColor(ConfHandler.getConf("prefixcolor:"));
        });

        rgb.addActionListener(ee ->{
            pane.remove(visuals);
            pane.add(new RGBmenue(pane, visuals, this), "Visuals", 1);
            pane.setSelectedIndex(1);
        });

        anwenden.addActionListener(ee -> save());
        anwenden2.addActionListener(ee -> save());

        ArrayList<String> a = Programmstart.getProgramm();
        JPanel links = new JPanel();
        JScrollPane s = new JScrollPane(links);

        JTextField[] Pname = new JTextField[10];
        JTextField[] Plink = new JTextField[10];
        JPanel[] Jps = new JPanel[11];
        Jps[0] = new JPanel();
        JLabel co = new JLabel("Command     ");
        Jps[0].add(co);
        JLabel ab = new JLabel("     path");
        Jps[0].add(ab);
        JButton fr = new JButton();
        JButton anwendenp = new JButton("apply");
        ImageIcon help = new ImageIcon(Iridium.class.getClassLoader().getResource("Button-help-icon.png").getFile());
        fr.setIcon(help);
        Jps[0].add(fr);
        fr.setOpaque(false);
        fr.setContentAreaFilled(false);
        fr.setBorderPainted(false);
        links.add(Jps[0]);
        int j = 0;
        for (int i = 0;i < 10;i++)
        {
            Jps[i+1] = new JPanel();
            Pname[i] = new JTextField(8);
            Plink[i] = new JTextField(12);
            Plink[i].setToolTipText(" path to .exe");
            Pname[i].setToolTipText("Start Command");
            try {
                Pname[i].setText(a.get(j));
                Plink[i].setText(a.get(j + 1));
                j += 2;
            } catch (Exception ignor) { }
            Jps[i+1].add(Pname[i]);
            Jps[i+1].add(Plink[i]);
            links.add(Jps[i+1]);
        }
        links.add(anwendenp);
        LayoutManager overlay = new OverlayLayout(this);
        setLayout(overlay);

        fr.addActionListener(eee -> {
            add(new OutputPanel("Help","with the function program is it possible to create a command witch executes an executable\nEnter the wished command left and the path to the .exe or .app to the left.\nIf you wish to add more programs then 10 feel free to use the command.\nIt is possible to use one command for more then one executable.",parent));
            links.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    remove(getComponentCount()-1);
                    repaint();
                    revalidate();
                    links.removeMouseListener(this);
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
        });

        pane.add(s, "Programs", 2);
        links.setLayout(new GridLayout(12,1));
        pane.setSelectedIndex(index);

        anwendenp.addActionListener(eee -> {
            String ad = "";
            for (int i = 0; i < 10;i++)
            {
                ad += Pname[i].getText() + " ";
                ad += Plink[i].getText() + " ";
                Programmstart.addProgramm(ad);
                ad = "";
            }
        });
        pane.add(new JLabel("if u are able to read this something went wrong"),"X", 3);
        pane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                pane.setPreferredSize(getPreferredSize());
                if (pane.getSelectedIndex() == 3) {
                    parent.removeAll();
                    parent.repaint();
                    parent.revalidate();
                }
            }
        });

        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setPreferredSize(parent.getSize());
                pane.setPreferredSize(getPreferredSize());
            }
        });
    }
}
