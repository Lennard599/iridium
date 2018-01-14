package iridium;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Options {

    static JTextField Name = new JTextField(10);
    static JTextField Font = new JTextField(10);
    static JTextField Prefix = new JTextField(10);


    private static void save() {
        ConfHandler.setConf("name:", Name.getText());
        ConfHandler.setConf("fontstyle:", Font.getText());
        ConfHandler.setConf("prefixtext:", Prefix.getText());
        ConfHandler.writeConf();
    }

    public static void Option(int index) {
        JTabbedPane pane = new JTabbedPane();
        JLabel OptionName = new JLabel("Your Name");
        String[] FarbListe = {"White", "Green", "Blue", "Black", "Rot", "Yellow"};
        String[] FarbListeS = {"Black", "Green", "White", "Blue", "Rot", "Yellow"};
        String[] FarbListeP = {"Black", "Green", "White", "Blue", "Rot", "Yellow"};
        JComboBox FarbeH = new JComboBox(FarbListe);
        JComboBox FarbeS = new JComboBox(FarbListeS);
        JComboBox FarbeP = new JComboBox(FarbListeP);
        JButton anwenden = new JButton("apply");
        JButton anwenden2 = new JButton("apply");
        JLabel OptionFarbeH = new JLabel("backgroundcolor");
        JLabel OptionFarbeS = new JLabel("fontcolor");
        JLabel OptionFarbeP = new JLabel("Prefix color");
        JLabel OptionFont = new JLabel("Enter Fontstyle");
        JLabel OptionPrefix = new JLabel("Prefix Text");
        JLabel stdpl = new JLabel("Use Custom Prefix");
        JCheckBox stdp = new JCheckBox();

        JPanel Generl = new JPanel();
        JPanel visuals = new JPanel();

        JPanel Jp4 = new JPanel();
        JPanel Jp5 = new JPanel();
        JPanel Jp6 = new JPanel();
        JPanel Jp7 = new JPanel();
        JPanel Jp9 = new JPanel();
        JPanel Jp10 = new JPanel();
        JPanel Jp11 = new JPanel();
        JPanel Jp72 = new JPanel();
        JPanel Jp12 = new JPanel();

        JFrame OptionenF = new JFrame("Iridium");
        OptionenF.setSize(300,400);
        OptionenF.setResizable(true);
        OptionenF.setLocationRelativeTo(null);
        OptionenF.setVisible(true);
        OptionenF.setIconImage(Iridium.icon.getImage());
        OptionenF.setLocation(Iridium.meinFrame.getLocation());

        Name.setText(ConfHandler.getConf("name:"));
        Font.setText(ConfHandler.getConf("fontstyle:"));
        Font.setToolTipText("you have to clear the console to apply Fontstyle");
        Prefix.setText(ConfHandler.getConf("prefixtext:"));

        if (ConfHandler.getConf("stdprefix:").trim().equals("N"))
            stdp.setSelected(true);

        if (!stdp.isSelected())
            Prefix.setFocusable(false);

        Jp4.add(OptionFarbeH);
        Jp4.add(FarbeH);
        Jp5.add(OptionFarbeS);
        Jp5.add(FarbeS);
        Jp9.add(OptionFarbeP);
        Jp9.add(FarbeP);
        Jp6.add(OptionName);
        Jp6.add(Name);
        Jp7.add(anwenden);
        Jp10.add(OptionFont);
        Jp10.add(Font);
        Jp11.add(OptionPrefix);
        Jp11.add(Prefix);
        Jp72.add(anwenden2);
        Jp12.add(stdpl);
        Jp12.add(stdp);

        Generl.add(Jp6);
        Generl.add(Jp7);
        visuals.add(Jp4);
        visuals.add(Jp5);
        visuals.add(Jp9);
        visuals.add(Jp10);
        visuals.add(Jp12);
        visuals.add(Jp11);
        visuals.add(Jp72);

        pane.add(Generl, "General", 0);
        pane.add(visuals, "Visuals", 1);

        FarbeH.setSelectedIndex(Integer.parseInt(ConfHandler.getConf("backgroundcolor:")));
        FarbeS.setSelectedIndex(Integer.parseInt(ConfHandler.getConf("fontcolor:")));
        FarbeP.setSelectedIndex(Integer.parseInt(ConfHandler.getConf("prefixcolor:")));
        OptionenF.add(pane);

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
            ConfHandler.setConf("backgroundcolor:", Integer.toString(FarbeH.getSelectedIndex()));
            Presentation.FarbeHintergrund(ConfHandler.getConf("backgroundcolor:"));
        });

        FarbeS.addActionListener(ee -> {
            ConfHandler.setConf("fontcolor:", Integer.toString(FarbeS.getSelectedIndex()));
            Presentation.Farbeschrift(ConfHandler.getConf("fontcolor:"));
        });

        FarbeP.addActionListener(ee -> {
            ConfHandler.setConf("prefixcolor:", Integer.toString(FarbeP.getSelectedIndex()));
            Presentation.FarbePrefix(ConfHandler.getConf("prefixcolor:"));
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
        ImageIcon help = new ImageIcon("iridium/Button-help-icon.png");
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

        fr.addActionListener(eee -> {
            ArrayList<String> ac = new ArrayList<>();
            ac.add("Mit der Funktion Programme kann mann Commands erstellen um .exe dateien zu Öffnen Links wird der Command und rechts der Absoltue Pfad zur .exe eingetragen. Wenn mann mehr als zehn Commands erstellen will kann mann dies mitt Commands tun (siehe hilfe).Es können auch Commands mehrmals verwendet werden.");
            new OutputField(ac, "Hilfe zu Programme", false, true,"");
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
    }
}
