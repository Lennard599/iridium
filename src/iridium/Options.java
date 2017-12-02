package iridium;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Options {

    public static void Option(int index) {
        JTabbedPane pane = new JTabbedPane();
        JLabel OptionName = new JLabel("Your Name");
        String[] FarbListe = {"Weiß", "Grün", "Blau", "Schwarz", "Rot"};
        String[] FarbListeS = {"Schwarz", "Grün", "Weiß", "Blau", "Rot"};
        String[] FarbListeP = {"Schwarz", "Grün", "Weiß", "Blau", "Rot"};
        JComboBox FarbeH = new JComboBox(FarbListe);
        JComboBox FarbeS = new JComboBox(FarbListeS);
        JComboBox FarbeP = new JComboBox(FarbListeP);
        JButton anwenden = new JButton("apply");
        JLabel OptionFarbeH = new JLabel("backgroundcolor");
        JLabel OptionFarbeS = new JLabel("fontcolor");
        JLabel OptionFarbeP = new JLabel("Prefix color");
        JTextField Name = new JTextField(10);

        JPanel Generl = new JPanel();
        JPanel Key = new JPanel();

        JPanel Jp4 = new JPanel();
        JPanel Jp5 = new JPanel();
        JPanel Jp6 = new JPanel();
        JPanel Jp7 = new JPanel();
        JPanel Jp9 = new JPanel();

        JFrame OptionenF = new JFrame("Iridium");
        OptionenF.setSize(300,400);
        OptionenF.setResizable(true);
        OptionenF.setLocationRelativeTo(null);
        OptionenF.setVisible(true);
        OptionenF.setIconImage(Iridium.icon.getImage());
        OptionenF.setLocation(Iridium.meinFrame.getLocation());
        Name.setText(ConfHandler.NameB);

        Jp4.add(OptionFarbeH);
        Jp4.add(FarbeH);
        Jp5.add(OptionFarbeS);
        Jp5.add(FarbeS);
        Jp9.add(OptionFarbeP);
        Jp9.add(FarbeP);
        Jp6.add(OptionName);
        Jp6.add(Name);
        Jp7.add(anwenden);

        Generl.add(Jp6);
        Generl.add(Jp7);
        Key.add(Jp4);
        Key.add(Jp5);
        Key.add(Jp9);

        pane.add(Generl, "General", 0);
        pane.add(Key, "Key", 1);

        FarbeH.setSelectedIndex(Integer.parseInt(ConfHandler.FarbeH2));
        FarbeS.setSelectedIndex(Integer.parseInt(ConfHandler.FarbeS2));
        FarbeP.setSelectedIndex(Integer.parseInt(ConfHandler.FarbeP2));
        OptionenF.add(pane);

        FarbeH.addActionListener(ee -> {
            int FarbeH1 = FarbeH.getSelectedIndex();
            ConfHandler.FarbeH2 = Integer.toString(FarbeH1);
            Presentation.HintergrundF(ConfHandler.FarbeH2);
        });

        FarbeS.addActionListener(ee -> {
            int FarbeS1 = FarbeS.getSelectedIndex();
            ConfHandler.FarbeS2 = Integer.toString(FarbeS1);
            Presentation.SchriftF(ConfHandler.FarbeS2);
        });

        FarbeP.addActionListener(ee -> {
            int FarbeP1 = FarbeP.getSelectedIndex();
            ConfHandler.FarbeP2 = Integer.toString(FarbeP1);
            Presentation.PrefixF(ConfHandler.FarbeP2);
        });

        anwenden.addActionListener(ee -> {
            String B;
            ConfHandler.NameB =	Name.getText();
            B = ConfHandler.NameB + " " + ConfHandler.FarbeH2 + " " + ConfHandler.FarbeS2 + " " + ConfHandler.FarbeP2 + " ";
            for(int i = 3;i < Filehandeling.Lesen("iridium/Iridiumconfig.txt", false).size(); i++)
                B += Filehandeling.Lesen("iridium/Iridiumconfig.txt", false).get(i) + " ";
            Filehandeling.Schreiben(B, "iridium/Iridiumconfig.txt", false ,true);
        });

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
            methods.AusgabeFeld(ac, "Hilfe zu Programme", false, true);
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
